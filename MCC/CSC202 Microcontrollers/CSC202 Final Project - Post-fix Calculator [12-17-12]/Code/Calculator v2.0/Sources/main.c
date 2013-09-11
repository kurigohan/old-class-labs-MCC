// Example 13 Calculator p.62
#include <hidef.h>      /* common defines and macros */
#include <mc9s12dg256.h>     /* derivative information */
#pragma LINK_INFO DERIVATIVE "mc9s12dg256b"

#include "main_asm.h" /* interface to the assembly module */

#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<math.h>

void main(void) {

 // long op1, op2;   //32-bit operands
  char* ptr;       //pointer to keypad buffer
  char* blanks;
  char kbuf[12];   //keypad buffer
  char oper[16]; //operation
  char c, a;
  int i; //kbuf index
  int shift = 0;  //indicates if in operator mode
  ptr = kbuf;
  blanks = "      ";
  
  PLL_init();           //set system clock freq to 24MHz
  lcd_init();         //enable lcd
  keypad_enable();    //enable keypad
  set_lcd_addr(0x40); //set display on 2nd line
  
  

    i = 0;
    while(1){
      c = getkey();     //read keypad   

      //---------------OPERATOR MODE--------------------
      if(shift == 1){    
        switch(c){
          case 0x1: //if plus '+' (1) key
            clear_lcd();  //clear lcd display
            set_lcd_addr(0x00); //set display on 1st line
            kbuf[i] = '+'; //store '+' in kbuf
            i++;  //increment index
            kbuf[i] = '\0'; //set char at kbuf[i] to null char for ascii conversion
            type_lcd(ptr);  //display ASCIIZ string on LCD
            wait_keyup();  //wait to release key
            
            set_lcd_addr(0x40); //set display on 2nd line
            break;
            
          case 0x2: //if minus '-' (2) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i] = '-'; 
            i++;  
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
            
          case 0x3: //if mod '%' (3) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i] = '%'; 
            i++;  
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
            
          case 0x4: //if multiply '*' (4) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i] = '*'; 
            i++;  
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
          
          case 0x5: //if divide key '/' (5) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i] = '/'; 
            i++;  
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
            
          case 0x6: //if power '^' (6) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i] = '^'; 
            i++;  
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
          
          case 0x7: //if left parenthesis '('  (7) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i] = '('; 
            i++;  
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
            
          case 0x8: //if right parenthesis ')' (8) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i] = ')'; 
            i++;  
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
            
          case 0x9: //if factorial '!' (9) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i] = '!'; 
            i++;  
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;
            
          
          /*case 0x0: //if delete (0) key
            clear_lcd();
            if(i>0){  //make sure index does not go below 0
             i--;     //decrement index
             kbuf[i] = '\0';  //delete char[i] by changing to null char
            }
            type_lcd(ptr);
            wait_keyup();
            break;     */
            
          case 0xC:   //if clear (C) key
              clear_lcd();  //clear lcd display
              wait_keyup(); //wait to release key
              set_lcd_addr(0x40); //display on 2nd line
              i = 0;        //reset kbuf index
             break;  
          
          
          case 0xD:  // if shift (D) key
            shift = 0;
            wait_keyup();  //wait to release key
            break;
          
          case 0xF:   //if equal (#) key
            break;
            
          default: break; 
        }  //end switch
      }   //end if(shift==1)
      //--------------END OPERATOR MODE CODE-----------
      
     
      //--------------OPERAND MODE---------------------- 
      else{  //shift != 1
       a = hex2asc(c);   //convert to ascii
       kbuf[i] = a;      // and store in kbuf 
        
       if(c<10){         //if 0 - 9
          data8(a);      //   display on lcd
          wait_keyup();  //   wait to release key
          i++;           //increment index
       }  //end if(c<10)
       
       else{        //c !<10
         switch(c){
           case 0xE:   //if decimal (*) key
             kbuf[i] = '.'; //store '.' in kbuf
             data8('.');  //display '.'
             wait_keyup();      //wait to release key
              i++;             //increment index
              break;
            
           case 0xA:   //if key A
           /* op2 = number(ptr);  //convert to binary
            op1 = op1 + op2;    //compete sum
            set_lcd_addr(0x40); //display on 2nd line
            write_long_lcd(op1);
            set_lcd_addr(0x00);  //clear 1st line
            type_lcd(blanks);
            wait_keyup();        //wait to release key
            i = 0;               //reset kbuf index
            set_lcd_addr(0x40);  //display on 1st line    */
            
             break;     
            
           /*case 0xC:   //if clear (C) key
              clear_lcd();  //clear lcd display
              wait_keyup(); //wait to release key
              set_lcd_addr(0x40); //display on 2nd line
              i = 0;        //reset kbuf index
             break;  */
             
           case 0xC: //if delete (C) key
            
            if(i>0){  //make sure index does not go below 0
             i--;     //decrement index
             kbuf[i] = '\0';  //delete char at i by changing to null char
             clear_lcd();
             set_lcd_addr(0x40);
             type_lcd(ptr);
            } 
            
       
            
            wait_keyup();
            break;
            
            
           case 0xD:   //if shift (D) key
             shift = 1;
             wait_keyup(); //wait to release key
             break;
            
            
           //case 0xF:   //if equal (#) key
            //break;
             
           case 0xF: //if negative (#) key
            clear_lcd(); 
            set_lcd_addr(0x00); 
            kbuf[i++] = '-';   
            kbuf[i] = '\0'; 
            type_lcd(ptr);  
            wait_keyup();  
            set_lcd_addr(0x40); 
            break;

            default: break;
         }  //switch
       }  //end else (c !< 10)
      } //end else (shift != 1) 
      //----------------------END OPERAND MODE----------------   
    }  //end while(1)
}  //end main                            

          
        
      
      
  

