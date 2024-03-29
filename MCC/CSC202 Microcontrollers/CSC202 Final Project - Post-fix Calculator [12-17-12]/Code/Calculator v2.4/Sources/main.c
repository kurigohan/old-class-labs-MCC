//---------------CALC VERSION 2.4------------
/*  
2.4 Changelog:
    - Fixed parenthesis display
    - Created flag struct to replace global flag variables
    - Revised addToOper function


2.3 Changelog:
      - Minus signs add additional space after (to differentiate from negative)
      - Pressing consecutive operator keys will result in a swap rather than lock
      - Right parenthesis only allowed after left parenthesis
      - One decimal per number input, can reenter decimal if deleted or operator key pressed
  

2.2 Changelog:
      - Replaced clear_lcd() usage with new clrLine() func
      - Added another char array to store the operation 
        (instead of storing everything in kbuf)
      - Added display() function to display strings and set cursor loc
      - Replaced type_lcd() usage with display() func
      - Negative key now functional
      - Fixed Mem Set/Clear
      - Added nlock

2.1 Changelog:
      - Changed some local variables to global variables
      - Key input modes turned into functions for better readability 
      - Added key lock to prevent consecutive operator input
      - Added prefix/postfix key (no functionality yet)
      - Added Memoty Set/Clear key and functionality
      - Added clear() function to clear display line, improves readability
      
// Based on Example 13 Calculator p.62 

------------------------------------------------------------------------*/


#include <hidef.h>      /* common defines and macros */
#include <mc9s12dg256.h>     /* derivative information */
#pragma LINK_INFO DERIVATIVE "mc9s12dg256b"

#include "main_asm.h" /* interface to the assembly module */

#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<math.h>

#define bool int
#define false 0
#define true 1

#define is_operator(c)  (c == '+' || c == '-' || c == '/' || c == '*' || c == '!' || c == '%' || c == '=' || c == '^' || c == 'r')
#define is_number(c)     ((c >= '0' && c <= '9') || c == '.')

struct Flag;

typedef struct Flag
{
        bool shift;
        bool olock;
        bool nlock;
        bool post;
        bool save;
        bool deci;
        bool neg;
        bool full;
        int leftpar;
        int opback;
}Flag;
 
//----Function Prototypes---
void shiftMode(char key, struct Flag *flag);
void stdMode(char key, struct Flag *flag);
void clrLine(int ln);
void display(char *out, int idx, char crus);
void addOper(char c, Flag *flag);
void subString();
void * createFlag();
//==========================

//--------Global Variables---------
char kbuf[16];   //keypad buffer
int kidx = 0; //kbuf index

char oper[50]; //operation string
int oidx = 0; //oper index

char mem[16]; //saved number
int midx; //mem index

char sub[16];
int substart = 0;
//=================================


void main(void) {

  char key;  //key pressed
  Flag *flag = createFlag();
  
  PLL_init();         //set system clock freq to 24MHz
  lcd_init();         //enable lcd display (clear display)
  keypad_enable();    //enable keypad
  led_enable();       //enable leds
  seg7_disable();     //turn off and disable seg7 display
  set_lcd_addr(0x40); //set display on 2nd line
  
 
  while(1){
     
    key = getkey();     //read keypad   
    if(flag->shift == true)  //shift key was pressed  
      shiftMode(key, flag);  //operator input keys
 
    else 
      stdMode(key, flag);   //standard input keys
      
  }  //end while(1)
}  //end main                            

Flag * createFlag(){
  Flag *f;
  f = (Flag *)malloc(sizeof(Flag));
  f->shift = false;
  f->olock = true;
  f->nlock = false;
  f->post = false;
  f->save = false;
  f->deci = false;
  f->neg = false;
  f->leftpar = 0;
  f->opback = 1;
  return f; 
        
}       

void stdMode(char key, Flag *flag){
   int i;
   char a = hex2asc(key);   //convert to ascii
   kbuf[kidx] = a;      // and store in kbuf
    
   if(key<10 && flag->nlock == false){         //if 0 - 9
     if(kidx < 16){ 
       flag->olock = false;       // reset operator lock
       wait_keyup();   // wait to release key
       kidx++;         //increment index
       display(kbuf, kidx,0x40);
     }
      
     
   }  //end if(c<10)
       
   else{        //c !<10
      switch(key){
         /* case 0xB: 
            sprintf(s, "%g", d);
            clear_lcd();
            set_lcd_addr(0x40);
            type_lcd(s);
            wait_keyup();
            break;*/
      
          
         case 0xE:   //if decimal (*) key
           if(flag->deci == false){  
            flag->deci = true;
            wait_keyup();      //wait to release key
            kbuf[kidx++] = '.'; //store '.' in kbuf
            display(kbuf, kidx, 0x40);   
           } 
           break;
            
           case 0xA:   //if Postfix (A) key
             if(flag->post == false) {
               flag->post = true; //set flag to indicate input will be in postfix notation
               led_on(0); //turn on led 0
               clrLine(2);
               kidx = 0; //reset index
               kbuf[kidx] = '\0'; //clear keypad buffer for new input                    
             } 
             else {
               flag->post = false; //back to prefix mode
               led_off(0);  //turn off led 0
               clrLine(2);
               kidx = 0; 
               kbuf[kidx] = '\0'; 
             }
             wait_keyup();
             
             break;       
            
             
          case 0xC: //if delete (C) key
            if(kidx > 0){  //make sure index does not go below 0
             if(is_number(kbuf[kidx-1]))
                flag->olock = true;
             if(kbuf[kidx-1] == '.')
                flag->deci = false;
             if(kbuf[kidx-1] == '(')
                flag->leftpar -= 1;
             if(kbuf[kidx-1] == ')') {          
                flag->leftpar += 1;
                flag->nlock = false;
                flag->olock = false;
             } 
             else
              flag->olock = true;
             
             kbuf[--kidx] = '\0';  //delete char at i by changing to null char
             clrLine(2);
             display(kbuf, kidx, 0x40);
             
            }
            wait_keyup();
            break;
            
            
          case 0xD:   //if shift (D) key
             flag->shift = true;          
             wait_keyup(); //wait to release key
             break;
               
          case 0xF: //if negative (#) key
            if(flag->neg == false){
              flag->neg = true;
              
              for(i = kidx; i > 0; i--){
                kbuf[i] = kbuf[i-1];
              }
              kbuf[0] = '-';
              kidx++;

            } 
            else{
              flag->neg = false;
              
              for(i = 0; i < kidx; i++){
                kbuf[i] = kbuf[i+1];
                
              }
              kidx--;
               
            } 
            clrLine(2);
            display(kbuf, kidx, 0x40);
            wait_keyup(); 
            break;

            default: break;
         }  //switch
      }  //end else (c !< 10)
  
}


void shiftMode(char key, Flag *flag){ 
   switch(key){
          case 0xA:  //if memory set (A) key - save number
            midx = kidx;
            strcpy(mem, kbuf); //copy data from kbuf to mem
            flag->save = true;
            led_on(1); //turn on 2nd led to indicate saved number
            wait_keyup(); 
            break;
            
          case 0xB: //if memory recall (B) key - get saved number
            if(flag->save == true){
              flag->olock = false;
              flag->nlock = true;
              strcpy(kbuf, mem);
              kidx = midx;
              //kbuf[kidx] = '\0';                     
              clrLine(2); //clear line 2
              display(kbuf, kidx,  0x40); 
              wait_keyup();    
            }
            break;
            
             
    
          case 0x1: //if plus '+' (1) key
            addOper('+', flag);
            break;
            
          case 0x2: //if minus '-' (2) key
            addOper('-', flag);
            break;
            
          case 0x3: //if mod '%' (3) key
            addOper('%', flag);
            break;
            
          case 0x4: //if multiply '*' (4) key
            addOper('*', flag);
            break;
          
          case 0x5: //if divide key '/' (5) key
            addOper('/', flag);
            break;
            
          case 0x6: //if power '^' (6) key
            addOper('^', flag);
            break;
          
          case 0x7: //if left parenthesis '('  (7) key
            if(flag->olock == true){         
              flag->leftpar += 1;
              kbuf[kidx++] = '(';
              display(kbuf,kidx, 0x40);
              wait_keyup();
            }

            break;
            
          case 0x8: //if right parenthesis ')' (8) key
            if(flag->leftpar>0 && flag->olock==false){ //if '(' present
              flag->leftpar -= 1;
              kbuf[kidx++] = ')';
              display(kbuf,kidx, 0x40);
              flag->nlock = true; //nlock = true to stop taking number inputs
              wait_keyup();
              
             } 
             break;
            
          case 0x9: //if factorial '!' (9) key
            addOper('!', flag);            
            break;
                  
          case 0xC:   //if clear (C) key
            flag->nlock = false;
            flag->olock = true;
            flag->deci = false;
            flag = createFlag();
            clear_lcd();

            kidx = 0;        //reset kbuf index
            kbuf[kidx] = '\0'; //clear kbuf
            oidx = 0;          //reset oper index
            oper[oidx] = '\0';  //clear oper
            substart = 0;
            set_lcd_addr(0x40);
            wait_keyup(); //wait to release key
            
            break;  
          
          
          case 0xD:  // if shift (D) key
            flag->shift = false;
            wait_keyup();  //wait to release key
            break;
          
          case 0xF:   //if equal (#) key
            clrLine(1);
            //--------------------
            if(oidx > 0){
              strcat(oper, kbuf);
              oidx += kidx; 

            } 
            else{
              oidx = kidx;
              strcpy(oper, kbuf); 
            }
            oper[oidx++] = ' ';
            flag->nlock = false;
            flag->deci = false;
            kidx = 0; 
            //-------------------

            --oidx; //remove space
            
            display(oper, oidx, 0x40);
            wait_keyup();
            break;
            
          case 0x0:
            break;
          default: break; 
        }  //end switch
 
 }

 
 void clrLine(int ln){
    char *blank = "                ";
    
    switch(ln) {
      
      case 1: //clear 1st line
        set_lcd_addr(0x00); //set cursor to 1st line 
        type_lcd(blank);
        break;
      case 2: //clear 2nd line
        set_lcd_addr(0x40); 
        type_lcd(blank); //set cursor to 2nd line
        break;
      default: //clear entire display
         clear_lcd(); 
         break;
    }
    
    //set_lcd_addr(curs); //set cursor to location curs
  
 }
 
 void display(char *out, int idx, char curs){
    out[idx] = '\0';
    set_lcd_addr(curs); //set cursor to location curs 
    type_lcd(out); //display ASCIIZ string on LCD
    set_lcd_addr(0x40);

  
 }


 void subString() {
   strncpy(sub, oper+substart, 16);
   clrLine(2);
   set_lcd_addr(0x40); 
   type_lcd(sub); 
   set_lcd_addr(0x40);

 }
 
 void addOper(char c, Flag *flag){
    if(flag->olock == false){ 
      flag->olock = true;
      clear_lcd(); //clear display, set cursor to 1st line
      //----------Add c to oper string-------------
      if(oidx > 0){
        strcat(oper, kbuf);
      oidx += kidx; 

      } 
      else{
       oidx = kidx;
       strcpy(oper, kbuf); 
      }
     oper[oidx++] = c;
     if(c == '-'){
       oper[oidx++] = ' ';
       flag->opback = 2;
     } 
     else
       flag->opback = 1;
     flag->nlock = false;
     flag->deci = false;
     kidx = 0; 
     //-----------------------------------------------
    } //end olock == false
    
    //----------------Replace Operator---------------
    else {
      oper[oidx-flag->opback] = c; //replace most recent operator with operator c
      if(flag->opback == 2) {  //opback = 2 means minus was last replaced   
        oper[--oidx] = '\0'; //get rid of the space added by the minus
        flag->opback = 1; //change opback back to 1
       }
      if(c == '-') { //if replacing with minus, add a space
        oper[oidx++] = ' ';
        flag->opback = 2; //opback becomes to since space added
      }  
    } //---------------------------------------------
    
    
    display(oper, oidx, 0x00);
    wait_keyup();  
  
 }
 
 
        
      
      
  

