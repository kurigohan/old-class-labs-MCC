// Based on Example 13 Calculator p.62 
//VERSION 2.1;
/*  Changelog:
      - Changed some local variables to global variables
      - Key input modes turned into functions for better readability 
      - Added key lock to prevent consecutive operator input
      - Added prefix/postfix key (no functionality yet)
      - Added Memoty Set/Clear key and functionality
      - Added clrLine() function to improve readability

*/


#include <hidef.h>      /* common defines and macros */
#include <mc9s12dg256.h>     /* derivative information */
#pragma LINK_INFO DERIVATIVE "mc9s12dg256b"

#include "main_asm.h" /* interface to the assembly module */

#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<math.h>

void shiftMode(char key);
void stdMode(char key);
void clrLine(int ln, char curs);

char kbuf[16];   //keypad buffer
int idx; //kbuf index
char mem[16]; //stored number
int midx; //mem index

//flags
int shift = 0;  //0 = standard input, 1 = operator input
int lock = 0; //if lock = 1, program should stop taking operator inputs
int nota = 0; //0 = prefix notation mode, 1 = postfix notation mode
int save = 0; //0 = no number saved, 1 = number saved

void main(void) {

  char key;  //key pressed
  
  
  PLL_init();         //set system clock freq to 24MHz
  lcd_init();         //enable lcd display (clear display)
  keypad_enable();    //enable keypad
  led_enable();       //enable leds
  seg7_disable();     //turn off and disable seg7 display
  set_lcd_addr(0x40); //set display on 2nd line
  
  

    idx = 0; //reset index
    while(1){
     
      key = getkey();     //read keypad   
      if(shift == 1)  //shift key was pressed  
        shiftMode(key);  //operator input keys
 
      else  //shift != 1
        stdMode(key);   //standard input keys

      
    }  //end while(1)
}  //end main                            



void stdMode(char key){
   char a = hex2asc(key);   //convert to ascii
   kbuf[idx] = a;      // and store in kbuf 
   if(key<10){         //if 0 - 9
      data8(a);      //   display on lcd
      wait_keyup();  //   wait to release key
      idx++;           //increment index
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
             kbuf[idx] = '.'; //store '.' in kbuf
             data8('.');  //display '.'
             wait_keyup();      //wait to release key
             idx++;             //increment index
             break;
            
           case 0xA:   //if Postfix (A) key
             if(nota == 0) {
               nota = 1; //set flag to indicate input will be in postfix notation
               led_on(0); //turn on led 0
               clear_lcd();
               set_lcd_addr(0x40);
               idx = 0; //reset index
               kbuf[idx] = '\0'; //clear keypad buffer for new input                    
             } 
             else {
               nota = 0; //back to prefix mode
               led_off(0);  //turn off led 0
               clear_lcd();
               set_lcd_addr(0x40);
               idx = 0; 
               kbuf[idx] = '\0'; 
             }
             
             break;       
            
             
          case 0xC: //if delete (C) key
            
            if(idx>0){  //make sure index does not go below 0
             idx--;     //decrement index
             kbuf[idx] = '\0';  //delete char at i by changing to null char
             clear_lcd();
             set_lcd_addr(0x40);
             type_lcd(kbuf);
            } 
            
            wait_keyup();
            break;
            
            
          case 0xD:   //if shift (D) key
             shift = 1;
             
             wait_keyup(); //wait to release key
             break;
               
          case 0xF: //if negative (#) key  
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[idx++] = '-';   
            kbuf[idx] = '\0'; 
            type_lcd(kbuf);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;

           
            

            default: break;
         }  //switch
      }  //end else (c !< 10)
  
}


void shiftMode(char key){
   
   switch(key){
          case 0xA:  //if memory set (A) key - save number
            midx = idx+1;
            memcpy(mem, kbuf, midx); //copy data from kbuf to mem
            save = 1;
            led_on(1); //turn on 2nd led to indicate saved number
            wait_keyup(); 
            break;
          case 0xB: //if memory recall (B) key - get saved number
            if(save == 1){
              idx = midx-1;
              memcpy(kbuf, mem, idx);           
              kbuf[idx] = '\0';            
              clrLine(2, 0x40); //clear line 2
              //set_lcd_addr(0x40); 
              type_lcd(kbuf);  
              wait_keyup();    
            }
            break;
            
             
    
          case 0x1: //if plus '+' (1) key
            if(lock == 0){ 
              lock = 1;
              clear_lcd();  //clear lcd display
              set_lcd_addr(0x00); //set display on 1st line
              kbuf[idx++] = '+'; //store '+' in kbuf, then increment index
              kbuf[idx] = '\0'; //set char at kbuf[i] to null char for ascii conversion
              type_lcd(kbuf);  //display ASCIIZ string on LCD
              wait_keyup();  //wait to release key
              set_lcd_addr(0x40); //set display on 2nd line
            }
            break;
            
          case 0x2: //if minus '-' (2) key
            if(lock == 0){ 
              lock = 1;
              clear_lcd(); 
              set_lcd_addr(0x00); 
              kbuf[idx++] = '-'; 
              kbuf[idx] = '\0'; 
              type_lcd(kbuf);  
              wait_keyup();  
              set_lcd_addr(0x40);
            }
            break;
            
          case 0x3: //if mod '%' (3) key
           if(lock == 0){ 
              lock = 1;
              clear_lcd(); 
              set_lcd_addr(0x00); 
              kbuf[idx++] = '%'; 
              kbuf[idx] = '\0'; 
              type_lcd(kbuf);  
              wait_keyup();  
              set_lcd_addr(0x40);
           }
           break;
            
          case 0x4: //if multiply '*' (4) key
            if(lock == 0){ 
              lock = 1;
              clear_lcd(); 
              set_lcd_addr(0x00); 
              kbuf[idx++] = '*';   
              kbuf[idx] = '\0'; 
              type_lcd(kbuf);  
              wait_keyup();  
              set_lcd_addr(0x40);
            }
            break;
          
          case 0x5: //if divide key '/' (5) key
            if(lock == 0){ 
              lock = 1;
              clear_lcd(); 
              set_lcd_addr(0x00); 
              kbuf[idx++] = '/'; 
              kbuf[idx] = '\0'; 
              type_lcd(kbuf);  
              wait_keyup();  
              set_lcd_addr(0x40); 
            }
            break;
            
          case 0x6: //if power '^' (6) key
            if(lock == 0){ 
              lock = 1;
              clear_lcd(); 
              set_lcd_addr(0x00); 
              kbuf[idx++] = '^'; 
              kbuf[idx] = '\0'; 
              type_lcd(kbuf);  
              wait_keyup();  
              set_lcd_addr(0x40);
            }
            break;
          
          case 0x7: //if left parenthesis '('  (7) key    
              clear_lcd(); 
              set_lcd_addr(0x00);
              kbuf[idx++] = '(';
              kbuf[idx] = '\0'; 
              type_lcd(kbuf);  
              wait_keyup();  
              set_lcd_addr(0x40);

            break;
            
          case 0x8: //if right parenthesis ')' (8) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[idx++] = ')';  
            kbuf[idx] = '\0'; 
            type_lcd(kbuf);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
            
          case 0x9: //if factorial '!' (9) key
            if(lock == 0){ 
              lock = 1;
              clear_lcd(); 
              set_lcd_addr(0x00); 
              kbuf[idx++] = '!';  
              kbuf[idx] = '\0'; 
              type_lcd(kbuf);  
              wait_keyup();  
              set_lcd_addr(0x40);
            }
            break;
                  
          case 0xC:   //if clear (C) key
            lock = 1;
            clear_lcd();  //clear lcd display
            wait_keyup(); //wait to release key
            set_lcd_addr(0x40); //display on 2nd line
            idx = 0;        //reset kbuf index
            break;  
          
          
          case 0xD:  // if shift (D) key
            shift = 0;
            lock = 0; //reset operator lock
            wait_keyup();  //wait to release key
            break;
          
          case 0xF:   //if equal (#) key
            break;
            
          default: break; 
        }  //end switch
 
}

//void postShiftMode(char key){}
          
 
 void clrLine(int ln, char curs){
    char *blank = "                ";
    
    switch(ln) {
      
      case 1: //Clear first line
        set_lcd_addr(0x00); 
        type_lcd(blank);
        break;
      case 2: //Clear first line
        set_lcd_addr(0x40); 
        type_lcd(blank);
        break;
      default: //Clear entire display
         clear_lcd(); 
         break;
    }
    
    set_lcd_addr(curs);
  
 }
 
        
      
      
  

