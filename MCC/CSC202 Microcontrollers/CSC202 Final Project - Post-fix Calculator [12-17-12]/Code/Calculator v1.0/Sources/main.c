// Example 13 Calculator p.62
#include <hidef.h>      /* common defines and macros */
#include <mc9s12dg256.h>     /* derivative information */
#pragma LINK_INFO DERIVATIVE "mc9s12dg256b"

#include "main_asm.h" /* interface to the assembly module */


void main(void) {
  long op1, op2;   //32-bit operands
  char* ptr;       //pointer to keypad buffer
  char* blanks;
  char kbuf[12];   //keypad buffer
  char c, a;
  int i;
  ptr = kbuf;
  blanks = "      ";
  
  
  PLL_init();           //set system clock freq to 24MHz
    lcd_init();         //enable lcd
    keypad_enable();    //enable keypad
    set_lcd_addr(0x00); //display on 1st line
    i = 0;
    while(1){
      c = getkey();     //read keypad
      a = hex2asc(c);   //convert to ascii
      kbuf[i] = a;      // and store in kbuf
      if(c<10){         //if 0 - 9
        data8(a);      //   display on lcd
        wait_keyup();  //   wait to release key
        i++;           //increment index
      } else{
        switch(c){
          case 0xE:   //if enter (*) key
            op1 = number(ptr);  //convert to binary
            set_lcd_addr(0x40); //display on 2nd line
            write_long_lcd(op1);
            set_lcd_addr(0x00); //clear 1st line
            type_lcd(blanks);
            wait_keyup();      //wait to release key
            i = 0;             //reset kbuf index
            set_lcd_addr(0x00); //display on 2nd line
            break;
            
          case 0xA:   //if key A
            op2 = number(ptr);  //convert to binary
            op1 = op1 + op2;    //compete sum
            set_lcd_addr(0x40); //display on 2nd line
            write_long_lcd(op1);
            set_lcd_addr(0x00);  //clear 1st line
            type_lcd(blanks);
            wait_keyup();        //wait to release key
            i = 0;               //reset kbuf index
            set_lcd_addr(0x00);  //display on 1st line
            break;
          case 0xF:   //if clear (#) key
            clear_lcd();  //clear lcd display
            wait_keyup(); //wait to release key
            i = 0;        //reset kbuf index
            break;
          default:
            break;
        }
      }
    }
}

          
        
      
      
  

