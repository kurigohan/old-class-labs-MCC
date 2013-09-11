/*
---------------------------- INFO ----------------------------------
Purpose: Operation.c contains functions to convert an input from pre-fix
         notation to post-fix notation and calculate the result.

---------------------------- SOURCES --------------------------------
// Taken from thelearningpoint.net;
// Changes: moved typedef struct to header, changed data type from int 
            to double, modified pop function to return element popped.
 //------------------------------------------------------------------
 
   Stack Data Structure:
   Stack has three properties. capacity stands for the maximum number 
   of elements stack can hold. Size stands for the current size of the 
   stack and elements is the array of elements.

   The createStack function takes argument the maximum number of elements the stack can hold, creates
   a stack according to it and returns a pointer to the stack. */

#include "Calc.h"


Stack * createStack(int maxElements)
{
    /* Create a Stack */
    Stack *S;
    S = (Stack *)malloc(sizeof(Stack));
    /* Initialise its properties */
    S->elements = (double *)malloc(sizeof(int)*maxElements);
    S->size = 0;
    S->capacity = maxElements;
    /* Return the pointer */
    return S;
}
double pop(Stack *S)
{

    S->size--;
    return S->elements[S->size];
}
double top(Stack *S)
{

    /* Return the topmost element */
    return S->elements[S->size-1];
}
void push(Stack *S,double element)
{
    /* Push an element on the top of it and increase its size by one*/
    S->elements[S->size++] = element;
    return;
}
