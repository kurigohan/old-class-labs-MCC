#ifndef CALC_H_INCLUDED
#define CALC_H_INCLUDED

#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<math.h>

#define bool int
#define false 0
#define true 1

#define is_operator(c)  (c == '+' || c == '-' || c == '/' || c == '*' ||  c == '%' || c == '^' || c == 'E')
#define is_number(c)     ((c >= '0' && c <= '9') || c == '.')

//----main ---------
typedef struct Flag
{
        bool shift;
        bool olock;
        bool nlock;
        bool post;
        bool save;
        bool deci;
        bool neg;
        bool dellock;
        bool rep;
        bool error;
        bool space;
        int leftpar;
        int opback;
}Flag;

void shiftMode(char key, struct Flag *flag);
void stdMode(char key, struct Flag *flag);
void clrLine(int ln);
void display(char *out, int idx, char crus);
void addOper(char c, Flag *flag);
void subString(int substart);
Flag * createFlag(void);
 void getResult(Flag *flag);
 void getPostResult(Flag *flag);
 void subDisplay();
//==========================



//--------------Stack -------------
typedef struct Stack
{
        int capacity;
        int size;
        double *elements;
}Stack;

Stack * createStack(int maxElements);
double pop(Stack *S);
double top(Stack *S);
void push(Stack *S, double element);
//========================


//--------------Operation ------------
bool inToPost(const char *input, char *output);
void operate(Stack *s, char c, Flag *flag);
double calculate(const char *input, Flag *flag);
//====================================

#endif // CALC_H_INCLUDED
