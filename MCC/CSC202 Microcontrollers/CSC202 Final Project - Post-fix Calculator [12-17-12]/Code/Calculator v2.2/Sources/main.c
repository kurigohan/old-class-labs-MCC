//---------------CALC VERSION 2.2------------
/*  
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

//----Function Prototypes---
void shiftMode(char key);
void stdMode(char key);
void clrLine(int ln);
void display(char *out, char crus);
void addToOper(char c);
//==========================

//--------Global Variables---------
char kbuf[16];   //keypad buffer
int kidx = 0; //kbuf index

char oper[50]; //operation string
int oidx = 0; //oper index

char mem[16]; //saved number
int midx; //mem index

//Flags:
int shift = 0;  //0 = standard input, 1 = operator input
int olock = 1; //if olock = 1, stop taking operator inputs
int nlock = 0; //if nlock = 1, stop taking number inputs
int nota = 0; //0 = prefix notation mode, 1 = postfix notation mode
int save = 0; //0 = no number saved, 1 = number saved
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
      if(shift == 1)  //shift key was pressed  
        shiftMode(key);  //operator input keys
 
      else  //shift != 1
        stdMode(key);   //standard input keys
      
    }  //end while(1)
}  //end main                            



void stdMode(char key){
   int i;
   static int neg;
   char a = hex2asc(key);   //convert to ascii
   kbuf[kidx] = a;      // and store in kbuf 
   if(key<10 && nlock == 0){         //if 0 - 9
      olock = 0;       // reset operator lock
      data8(a);       // display on lcd
      wait_keyup();   // wait to release key
      kidx++;         //increment index
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
             kbuf[kidx++] = '.'; //store '.' in kbuf
             data8('.');  //display '.'
             wait_keyup();      //wait to release key
             break;
            
           case 0xA:   //if Postfix (A) key
             if(nota == 0) {
               nota = 1; //set flag to indicate input will be in postfix notation
               led_on(0); //turn on led 0
               clrLine(2);
               kidx = 0; //reset index
               kbuf[kidx] = '\0'; //clear keypad buffer for new input                    
             } 
             else {
               nota = 0; //back to prefix mode
               led_off(0);  //turn off led 0
               clrLine(2);
               kidx = 0; 
               kbuf[kidx] = '\0'; 
             }
             
             break;       
            
             
          case 0xC: //if delete (C) key
            olock = 1;
            if(kidx > 0){  //make sure index does not go below 0
             //kidx--;     //decrement index
             kbuf[--kidx] = '\0';  //delete char at i by changing to null char
             clrLine(2);
             display(kbuf, 0x40);
             
            } else
                olock = 1;
            
            wait_keyup();
            break;
            
            
          case 0xD:   //if shift (D) key
             shift = 1;          
             wait_keyup(); //wait to release key
             break;
               
          case 0xF: //if negative (#) key
            if(neg == 0){
              neg = 1;
              for(i = kidx+1; i > 0; i--){
                kbuf[i] = kbuf[i-1];
              }
              kbuf[0] = '-';
              kidx++;
            } 
            else{
              neg = 0;
              for(i = 0; i < kidx; i++){
                kbuf[i] = kbuf[i+1];
                
              }
              kidx--; 
            }
             
            //kbuf[kidx++] = '-';   
            kbuf[kidx] = '\0'; 
            clrLine(2);
            display(kbuf, 0x40); 
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
            save = 1;
            led_on(1); //turn on 2nd led to indicate saved number
            wait_keyup(); 
            break;
            
          case 0xB: //if memory recall (B) key - get saved number
            if(save == 1){
              olock = 0;
              nlock = 1;
              strcpy(kbuf, mem);
              kidx = midx;
              kbuf[kidx] = '\0';                     
              clrLine(2); //clear line 2
              //set_lcd_addr(0x40); 
              display(kbuf, 0x40); 
              wait_keyup();    
            }
            break;
            
             
    
          case 0x1: //if plus '+' (1) key
            if(olock == 0){ 
              olock = 1;
              clear_lcd(); //clear display, set cursor to 1st line
              //kbuf[kidx++] = '+'; //store '+' in kbuf, then increment index
             // kbuf[kidx] = '\0'; //set char at kbuf[i] to null char for ascii conversion/
              addToOper('+');
              display(oper, 0x00);
            }
            break;
            
          case 0x2: //if minus '-' (2) key
            if(olock == 0){ 
              olock = 1;
              clear_lcd();
              addToOper('-');
              display(oper, 0x00);//
              
            }
            break;
            
          case 0x3: //if mod '%' (3) key
           if(olock == 0){ 
              olock = 1;
              clear_lcd();
              addToOper('%');
              display(oper, 0x00);
           }
           break;
            
          case 0x4: //if multiply '*' (4) key
            if(olock == 0){ 
              olock = 1;
              clear_lcd();
              addToOper('*'); 
              display(oper, 0x00);
            }
            break;
          
          case 0x5: //if divide key '/' (5) key
            if(olock == 0){ 
              olock = 1;
              clear_lcd(); 
              addToOper('/'); 
              display(oper, 0x00); 
            }
            break;
            
          case 0x6: //if power '^' (6) key
            if(olock == 0){ 
              olock = 1;
              clear_lcd(); 
              addToOper('^'); 
              display(oper, 0x00);
            }
            break;
          
          case 0x7: //if left parenthesis '('  (7) key    
              clear_lcd();
              addToOper('('); 
              display(oper, 0x00);;

            break;
            
          case 0x8: //if right parenthesis ')' (8) key
              clear_lcd(); 
              addToOper(')');
              display(oper, 0x00); 
              break;
            
          case 0x9: //if factorial '!' (9) key
            if(olock == 0){ 
              olock = 1;
              clear_lcd();
              addToOper('!'); 
              display(kbuf, 0x00);
            }
            break;
                  
          case 0xC:   //if clear (C) key
            olock = 1;
            clear_lcd();
            wait_keyup(); //wait to release key
            kidx = 0;        //reset kbuf index
            kbuf[kidx] = '\0'; //clear kbuf
            oidx = 0;          //reset oper index
            oper[oidx] = '\0';  //clear oper
            
            break;  
          
          
          case 0xD:  // if shift (D) key
            shift = 0;
            set_lcd_addr(0x40);
            wait_keyup();  //wait to release key

            break;
          
          case 0xF:   //if equal (#) key
            clear_lcd();
            
            addToOper(' '); //add current input to operation
            oper[--oidx] = '\0'; //remove space
            
            display(oper, 0x40);
            wait_keyup();
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
 
 void display(char *out, char curs){
    set_lcd_addr(curs); //set cursor to location curs 
    type_lcd(out); //display ASCIIZ string on LCD
    wait_keyup();  //wait til key released
  
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
     kbuf[kidx] = '\0';
     oper[oidx++] = c;
     oper[oidx] = '\0';
        
     nlock = 0;
 }
 
        
      
      
  

