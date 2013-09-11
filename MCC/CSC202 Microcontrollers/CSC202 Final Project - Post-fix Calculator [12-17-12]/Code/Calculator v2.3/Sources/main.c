//---------------CALC VERSION 2.3------------
/*  
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

*/


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

//----Function Prototypes---
void shiftMode(char key);
void stdMode(char key);
void clrLine(int ln);
void display(char *out, int idx, char crus);
void addToOper(char c);
void replaceWith(char c);
//==========================

//--------Global Variables---------
char kbuf[16];   //keypad buffer
int kidx = 0; //kbuf index

char oper[50]; //operation string
int oidx = 0; //oper index

char mem[16]; //saved number
int midx; //mem index

char sub[16];
int substart, subend;
int sidx;


//Flags:
bool shift = false;  //0 = standard input, 1 = operator input
bool olock = true; //if olock = 1, stop taking operator inputs
bool nlock = false; //if nlock = 1, stop taking number inputs
bool nota = false; //0 = prefix notation mode, 1 = postfix notation mode
bool save = false; //0 = no number saved, 1 = number saved
bool deci = false;
int leftpar = 0;
int opback = 1; //oidx - opback = operator location
 //===============================================

 
 
void main(void) {

  char key;  //key pressed
  
  
  PLL_init();         //set system clock freq to 24MHz
  lcd_init();         //enable lcd display (clear display)
  keypad_enable();    //enable keypad
  led_enable();       //enable leds
  seg7_disable();     //turn off and disable seg7 display
  set_lcd_addr(0x40); //set display on 2nd line
  
 
  while(1){
     
    key = getkey();     //read keypad   
    if(shift == true)  //shift key was pressed  
      shiftMode(key);  //operator input keys
 
    else  //shift != 1
      stdMode(key);   //standard input keys
      
  }  //end while(1)
}  //end main                            



void stdMode(char key){
   int i;
   static bool neg = false;

   char a = hex2asc(key);   //convert to ascii
   kbuf[kidx] = a;      // and store in kbuf
    
   if(key<10 && nlock == false){         //if 0 - 9
      olock = false;       // reset operator lock
     // data8(a);       // display on lcd
      kidx++;         //increment index
      display(kbuf, kidx,0x40);
      wait_keyup();   // wait to release key
     
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
            if(deci == false){
              
             deci = true;
             kbuf[kidx++] = '.'; //store '.' in kbuf
             //data8('.');  //display '.'
             display(kbuf, kidx, 0x40);
             wait_keyup();      //wait to release key
            } 
     
             
            
             break;
            
           case 0xA:   //if Postfix (A) key
             if(nota == false) {
               nota = true; //set flag to indicate input will be in postfix notation
               led_on(0); //turn on led 0
               clrLine(2);
               kidx = 0; //reset index
               kbuf[kidx] = '\0'; //clear keypad buffer for new input                    
             } 
             else {
               nota = false; //back to prefix mode
               led_off(0);  //turn off led 0
               clrLine(2);
               kidx = 0; 
               kbuf[kidx] = '\0'; 
             }
             
             break;       
            
             
          case 0xC: //if delete (C) key
            olock = true;
            if(kidx > 0){  //make sure index does not go below 0
             if(kbuf[kidx-1] == '.')
                deci = false;
             kbuf[--kidx] = '\0';  //delete char at i by changing to null char
             clrLine(2);
             display(kbuf, kidx, 0x40);
             
            }
            
            wait_keyup();
            break;
            
            
          case 0xD:   //if shift (D) key
             shift = true;          
             wait_keyup(); //wait to release key
             break;
               
          case 0xF: //if negative (#) key
            if(neg == false){
              neg = true;
              
              for(i = kidx; i > 0; i--){
                kbuf[i] = kbuf[i-1];
              }
              kbuf[0] = '-';
              kidx++;

            } 
            else{
              neg = false;
              
              for(i = 0; i < kidx; i++){
                kbuf[i] = kbuf[i+1];
                
              }
              kidx--;
               
            }
  
            //kbuf[kidx] = '\0'; 
            clrLine(2);
            display(kbuf, kidx, 0x40); 
            break;

            default: break;
         }  //switch
      }  //end else (c !< 10)
  
}


void shiftMode(char key){ 
   switch(key){
          case 0xA:  //if memory set (A) key - save number
            midx = kidx;
            strcpy(mem, kbuf); //copy data from kbuf to mem
            save = true;
            led_on(1); //turn on 2nd led to indicate saved number
            wait_keyup(); 
            break;
            
          case 0xB: //if memory recall (B) key - get saved number
            if(save == true){
              olock = false;
              nlock = true;
              strcpy(kbuf, mem);
              kidx = midx;
              //kbuf[kidx] = '\0';                     
              clrLine(2); //clear line 2
              display(kbuf, kidx,  0x40); 
              wait_keyup();    
            }
            break;
            
             
    
          case 0x1: //if plus '+' (1) key
            if(olock == false){ 
              olock = true;
              clear_lcd(); //clear display, set cursor to 1st line
              addToOper('+');;
            }
            else 
              replaceWith('+');
            display(oper, oidx, 0x00);

            break;
            
          case 0x2: //if minus '-' (2) key
            if(olock == false){ 
              olock = true;
              clear_lcd();
              addToOper('-');
            }
            else
              replaceWith('-');
            display(oper, oidx, 0x00);
            break;
            
          case 0x3: //if mod '%' (3) key
           if(olock == false){ 
              olock = true;
              clear_lcd();
              addToOper('%');
           }
           else 
              replaceWith('%');
            display(oper, oidx, 0x00);
           break;
            
          case 0x4: //if multiply '*' (4) key
            if(olock == false){ 
              olock = true;
              clear_lcd();
              addToOper('*'); 
            }
            else 
              replaceWith('*');
            display(oper, oidx, 0x00);
            break;
          
          case 0x5: //if divide key '/' (5) key
            if(olock == false){ 
              olock = true;
              clear_lcd(); 
              addToOper('/');  
            }
            else 
              replaceWith('/');
            display(oper, oidx, 0x00);
            break;
            
          case 0x6: //if power '^' (6) key
            if(olock == false){ 
              olock = true;
              clear_lcd(); 
              addToOper('^'); 
            }
            else 
              replaceWith('^');
            display(oper, oidx, 0x00);
            break;
          
          case 0x7: //if left parenthesis '('  (7) key
            if(olock == true){
              /*leftpar++;  
              clear_lcd();
              addToOper('('); 
              display(oper, 0x00);*/
              
              leftpar++;
              kbuf[kidx++] = '(';
              data8('(');
              wait_keyup();
            }

            break;
            
          case 0x8: //if right parenthesis ')' (8) key
            if(leftpar>0 && olock == false){ //if '(' present, and olock off
              /*clear_lcd(); 
              addToOper(')');
              display(oper, 0x00);
              leftpar--;*/
              
              kbuf[kidx++] = ')';
              data8(')');
              nlock = true; //nlock = true to stop taking number inputs
              wait_keyup();
              
             } 
             break;
            
          case 0x9: //if factorial '!' (9) key
            if(olock == false){ 
              olock = true;
              clear_lcd();
              addToOper('!'); 
            }
            else
              replaceWith('!');
            display(oper, oidx, 0x00);
            break;
                  
          case 0xC:   //if clear (C) key
            olock = true;
            deci = false;
            clear_lcd();
            wait_keyup(); //wait to release key
            kidx = 0;        //reset kbuf index
            kbuf[kidx] = '\0'; //clear kbuf
            oidx = 0;          //reset oper index
            oper[oidx] = '\0';  //clear oper
            set_lcd_addr(0x40);
            
            break;  
          
          
          case 0xD:  // if shift (D) key
            shift = false;
            wait_keyup();  //wait to release key
            break;
          
          case 0xF:   //if equal (#) key
            clear_lcd();
            
            addToOper(' '); //add current input to operation
            --oidx; //remove space
            
            display(oper, oidx, 0x40);
            wait_keyup();
            break;
            
          case 0x0:
            if(oidx > 16) {
            
              
              
            }
            
      
          break;
          default: break; 
        }  //end switch
 
}

//void postShiftMode(char key){}
          
 
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
    wait_keyup();  //wait til key released
         set_lcd_addr(0x40);
  
 }
 
 void addToOper(char c){
    if(oidx > 0){
      strcat(oper, kbuf);
      oidx += kidx; 

    } else{
       oidx = kidx;
       strcpy(oper, kbuf); 
      
    }
     kidx = 0;
     //kbuf[kidx] = '\0';
    // if(c == '-')
       // oper[oidx++] = ' ';
     
     oper[oidx++] = c;
     
     if(c == '-'){
      
       oper[oidx++] = ' ';
       opback = 2;
     } 
     else
       opback = 1;
     
    // oper[oidx] = '\0';
        
     nlock = false;
     deci = false;
 }
 
 void replaceWith (char c){
    oper[oidx-opback] = c; //replace most recent operator with operator c
    if(opback == 2) {  //opback = 2 means minus was last replaced   
      oper[--oidx] = '\0'; //get rid of the space added by the minus
      opback = 1; //change opback back to 1
    }
    if(c == '-') { //if replacing with minus, add a space
     oper[oidx++] = ' ';
     opback = 2; //opback becomes to since space added
    } ;
 }
 
 void subString() {
     
 
  
 }
 
        
      
      
  

