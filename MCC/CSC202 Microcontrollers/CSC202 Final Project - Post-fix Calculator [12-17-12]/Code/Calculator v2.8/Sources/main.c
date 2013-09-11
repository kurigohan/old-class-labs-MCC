//---------------CALC VERSION 2.8------------
/*
2.8 ========
      - Functional Postfix mode 
2.7 ========
      - Read exponents symbol (E)
2.6 ========
      - Integrated calculator code
2.5 ========
      - Added extended display
      - More comments
      - Oper size check
2.4 ========
      - Fixed parenthesis display
      - Created flag struct to replace global flag variables
      - Revised addToOper function
2.3 ========
      - Minus signs add additional space after (to differentiate from negative)
      - Pressing consecutive operator keys will result in a swap rather than lock
      - Right parenthesis only allowed after left parenthesis
      - One decimal per number input, can reenter decimal if deleted or operator key pressed
2.2 ========
      - Replaced clear_lcd() usage with new clrLine() func
      - Added another char array to store the operation 
        (instead of storing everything in kbuf)
      - Added display() function to display strings and set cursor loc
      - Replaced type_lcd() usage with display() func
      - Negative key now functional
      - Fixed Mem Set/Clear
      - Added nlock
2.1 ========
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
#include "Calc.h"



//--------Global Variables---------
char kbuf[32];   //keypad buffer
int kidx = 0; //kbuf index

char oper[64]; //operation string
int oidx = 0; //oper index

char mem[16]; //saved number
int midx; //mem index


char postfix[128];

    
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
 
    else if(flag->shift == false)
      stdMode(key, flag);   //standard input keys
      
  }  //end while(1)
}  //end main                            

Flag * createFlag(){
  Flag *f;
  f = (Flag *)malloc(sizeof(Flag));
  f->shift = false;  //shift mode
  f->olock = true;   //operator lock: prevent operator input
  f->nlock = false;  //number lock: prevent number input 
  f->post = false;   //postfix mode
  f->save = false;   //number saved
  f->deci = false;   //decimal used
  f->neg = false;    //negative sign present
  f->dellock = false; //megative sign lock
  f->rep = false;    //allow or prohibit operator replacing
  f->error = false;
  f->space = true;
  f->leftpar = 0;    //left parenthesis counter
  f->opback = 1;     //oidx - oback gives operator location in oper
  return f; 
        
}       

void stdMode(char key, Flag *flag){
   int i;
   char a = hex2asc(key);   //convert to ascii
   kbuf[kidx] = a;      // and store in kbuf
    
   if(key<10 && flag->nlock == false){         //if 0 - 9
     if(kidx < 10){ 
       flag->olock = false;       // reset operator lock
       flag->space = false;
       wait_keyup();   // wait to release key
       kidx++;         //increment index
       display(kbuf, kidx,0x40);
     }
      
     
   }  //end if(c<10)
       
   else{        //c !<10
      switch(key){ 
         
            
           case 0xA:   //=====if Postfix (A) key
             if(flag->post == false) {
               flag->post = true; //set flag to indicate input will be in postfix notation
               led_on(2); //turn on led 0                   
             } 
             else {
               flag->post = false; //back to prefix mode
               flag->olock = true;
               led_off(2);  //turn off led 0       
             }
             clear_lcd();
             set_lcd_addr(0x40);
             
             kidx = 0;      //reset kbuf
             kbuf[kidx] = '\0'; 
             
             wait_keyup();
             break;
             
          case 0xB: //====if exponent 'E' (B) key
            addOper('E', flag);
            break;              
            
             
          case 0xC: //=====if delete (C) key
            if(kidx > 0 && flag->dellock == false){  //make sure index does not go below 0
             if(kbuf[kidx-1] == '.')
                flag->deci = false;
             
             else if(kbuf[kidx-1] == '(') //if '(' to be deleted
              {
                
                flag->leftpar -= 1;  //decrement leftpar
                flag->rep = true;
              }
             else if(kbuf[kidx-1] == ')') { //if ')' to be deleted          
                flag->leftpar += 1;   //increment leftpar
                flag->nlock = false;
                flag->olock = false;
             } 
             
             kbuf[--kidx] = '\0';  //delete char at i by changing to null char
             clrLine(2);
             display(kbuf, kidx, 0x40);
            }
            if(kidx == 0) {
              
               flag->olock = true;
               if(oper[oidx] == ' ');
                flag->space = true;
            }
            wait_keyup();
            break;
            
            
          case 0xD:   //======if shift (D) key
             flag->shift = true;
             led_on(0);          
             wait_keyup(); //wait to release key
             break;
          
          case 0xE:   //=======if decimal (*) key
           if(flag->deci == false){  
            flag->deci = true;
            wait_keyup();      //wait to release key
            kbuf[kidx++] = '.'; //store '.' in kbuf
            clrLine(2);
            display(kbuf, kidx, 0x40);   
           } 
           break;
               
          case 0xF: //========if negative (#) key
            if(flag->nlock == false && kbuf[0] != '('){
              
             if(flag->neg == false){
               if(kidx >0)
               {
                for(i = kidx; i > 0; i--)   //shift kbuf contents  right 
                  kbuf[i] = kbuf[i-1]; 
               }        
               
               flag->neg = true;
               kbuf[0] = '-';  //insert negative sign at beginning of kbuf
               kidx++;

              } 
             else{
             
              if(kidx > 0){
                for(i = 0; i < kidx; i++) //shift kbuf contents left and remove negative sign
                  kbuf[i] = kbuf[i+1];
                kidx--;
              }
              
              flag->neg = false;
              
             } 
            clrLine(2);
            display(kbuf, kidx, 0x40);
            
           }

           wait_keyup(); 
           break;

           default: break;
         }  //switch
      }  //end else (c !< 10)
  
}


void shiftMode(char key, Flag *flag){
   switch(key){

    
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
       
          case 0x9: //if space ' ' (9) key - only available in postfix mode
            if(flag->post == true && flag->olock == false && flag->space ==false) {
                
                addOper(' ', flag);
                flag->olock == true;
                flag->space == true; 
            }
            
            wait_keyup();
            break;
          
          
          case 0xA: //if left parenthesis '('  (A) key
            if(flag->post == false && flag->olock == true){  //if olock enabled (previous char was an operator)       
              flag->leftpar += 1; //increment leftpar flag 
              kbuf[kidx++] = '(';  //add '(' to kbuf
              display(kbuf,kidx, 0x40);  //display new kbuf
              flag->rep = false;
              
            } 
            
            wait_keyup();
            break;
            
          case 0xB: //if right parenthesis ')' (B) key
            if(flag->leftpar>0 && flag->olock==false && flag-> post == false){ //if '(' present and olock disabled (previous char was number)
              flag->leftpar -= 1;   //decrement leftpar
              kbuf[kidx++] = ')';   //add ')' to kbuf
              display(kbuf,kidx, 0x40);  //display new kbuf
              flag->nlock = true; //nlock = true to stop taking number inputs
             } 
             wait_keyup();
             break;
             
         case 0xC:   //if clear (C) key
            flag->nlock = false;
            flag->olock = true;
            flag->deci = false;
            flag->error = false;
            flag-> dellock = false;
            flag-> space = true;
            flag-> neg = false;
            flag->leftpar = 0;
            clear_lcd();

            kidx = 0;        //reset kbuf index
            kbuf[kidx] = '\0'; //clear kbuf
            oidx = 0;          //reset oper index
            oper[oidx] = '\0';  //clear oper
            postfix[0] = '\0';
            set_lcd_addr(0x40);
            wait_keyup(); //wait to release key
            
            break; 
            
       case 0xD:  // if shift (D) key
            flag->shift = false;
            led_off(0);
            wait_keyup();  //wait to release key
            break;
                 case 0xE:  //if memory set (E) key - save number
            if(kidx > 0){
              
              midx = kidx;
              (void) strcpy(mem, kbuf); //copy data from kbuf to mem
              flag->save = true;
              led_on(1); //turn on 2nd led to indicate saved number
            }
            wait_keyup(); 
            break;
            
      case 0x0: //if memory recall (0) key - get saved number
            if(flag->save == true){
              flag->olock = false;
              flag->nlock = true;
              (void) strcpy(kbuf, mem);
              kidx = midx;                    
              clrLine(2);
              display(kbuf, kidx,  0x40); 
                  
            }
            wait_keyup();
            break;
            

          
            
      case 0xF: //if equal (#) key
            if(flag->post == false)
              getResult(flag);
            else
              getPostResult(flag);
            wait_keyup();
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

 
 void addOper(char c, Flag *flag){

   if( oidx+kidx+1 < 64){  //make sure oper does not exceed its size
     kbuf[kidx] = '\0';
     if(flag->post==true)
        flag->olock = false;
    if(flag->olock == false && flag->space == false){  //check if olock disabled
    
     // if(flag-> post == false)
        flag->olock = true;   //olock enabled
      clear_lcd(); //clear display
      
        //----------Add kbuf and c to oper string-------------
      if(oidx > 0){   //if oper is not empty
        (void) strcat(oper, kbuf); //append kbuf to oper
        oidx += kidx;       //increase oidx by kidx

      } 
      else{  //if oper is empty
        (void) strcpy(oper, kbuf); //copy kbuf to oper
        oidx = kidx; //oidx same as kidx
      }
      
      oper[oidx++] = c; //add c (the operator) to oper
      
      if(c == '-'){   //if c is a minus sign
        oper[oidx++] = ' ';  //add a space after it in oper
        flag->opback = 2;  //set opback to 2 (the operator is 2 "back" from the end)
      } 
      else   //if not minus sign
        flag->opback = 1;  //opback is 1
     
      flag->nlock = false; //disable number lock
      flag->deci = false;  //disable decimal lock
      flag ->rep = true;
      kidx = 0;          //reset kidx
     //==============================================
     
    } //end olock == false
    
    //----------------Replace Operator---------------
    else if(flag->rep == true && flag->space == false){    //if olock enabled, and the first char in kbuf is not '('
      oper[oidx-flag->opback] = c; //replace most recent operator with operator c
      if(flag->opback == 2) {  //opback = 2 means minus was last replaced   
        oper[--oidx] = '\0'; //get rid of the space added by the minus
        flag->opback = 1; //change opback back to 1
      }
      
      if(c == '-') { //if replacing with minus, add a space
        oper[oidx++] = ' ';
        flag->opback = 2; //opback becomes 2 since space added
      }  
    } //===============================================

     subDisplay();
    

   }//end if < 32
  
    wait_keyup(); 
  
 }
 
 

 //void subString(int substart) {

   //free(sub);

 //}
 
 void getResult(Flag *flag){
  double a;
  int substart = 0;   
  if(flag-> olock == false && oidx+kidx < 64 ){
    //--------------------
      if(oidx > 0){
        (void) strcat(oper, kbuf);
         oidx += kidx; } 
      else{
        oidx = kidx;
        (void) strcpy(oper, kbuf); }
      
      oper[oidx] = '\0';
      kidx = 0;
      kbuf[0] = '\0';
      
      //Flags set to prevent further input (force user to hit clear to proceed)
      flag->nlock = true;
      flag->olock = true;
      flag->dellock = true;
      flag->rep = false;   
           
      clear_lcd(); 
      set_lcd_addr(0x00);
      type_lcd(oper);   
      
      //-------------------
       subDisplay();
      //-------------------
      
        if(inToPost(oper, postfix)){
         a = calculate(postfix, flag);
         if(flag->error == false) {
          (void) sprintf(kbuf, "%G",a );
          kidx = strlen(kbuf);       
          type_lcd(kbuf);
          
         } 
         else {
          clear_lcd();
          type_lcd("ERROR");
         } 
        }     //end if inToPost
        else {
        clear_lcd();
        type_lcd("ERROR");
        }
              
   }  //end if olock == false && oidx+kidx < 64

  wait_keyup();
}

void subDisplay() 
{
    int substart = 0;
    char sub[16];// = malloc(16);
    if(oidx > 16){ //if oper is larger than the 16 characters (the display limit) 
       substart += oidx - 16; //add the char  difference to substart
       oper[oidx] = '\0'; //terminate oper at location oidx
       (void) strncpy(sub, oper+substart, 16); //copy oper from index substart to the end into sub
       set_lcd_addr(0x00); 
       type_lcd(sub);    //display sub
       set_lcd_addr(0x40);
    }
      
    else 
      display(oper, oidx, 0x00); 
}

void getPostResult(Flag *flag) {
    double a;
    if(flag->dellock == false){
      
     flag->nlock = true;
     flag->olock = true;
     flag->dellock = true;
     flag->rep = false;
    
    clear_lcd();    
    subDisplay(); 
    oper[oidx] = '\0';
    kbuf[0] = '\0';
    
    a = calculate(oper, flag);
    if(flag-> error == false ){      
       (void) sprintf(kbuf, "%G", a);
       kidx = strlen(kbuf);
       type_lcd(kbuf);
    }
    else {
      clear_lcd();
      type_lcd("ERROR");
    }

  }
}
 
 
        
      
      
  

