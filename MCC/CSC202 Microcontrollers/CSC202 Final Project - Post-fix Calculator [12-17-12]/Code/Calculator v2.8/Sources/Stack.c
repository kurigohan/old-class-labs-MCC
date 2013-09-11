// Taken from thelearningpoint.net;
// Changes: moved typedef struct to header, changed data type from int to double, modified pop function to return element popped.

/* Stack Data Structure:
   Stack has three properties. capacity stands for the maximum number of elements stack can hold.
   Size stands for the current size of the stack and elements is the array of elements.

   The createStack function takes argument the maximum number of elements the stack can hold, creates
   a stack according to it and returns a pointer to the stack. */

#include "Calc.h"


Stack * createStack(int maxElements)
{
        /* Create a Stack */
        Stack *S;
        S = (Stack *)malloc(sizeof(Stack));
        /* Initialise its properties */
        S->elements = (int *)malloc(sizeof(int)*maxElements);
        S->size = 0;
        S->capacity = maxElements;
        /* Return the pointer */
        return S;
}
double pop(Stack *S)
{
        /* If stack size is zero then it is empty. So we cannot pop */
        if(S->size==0)
        {

        }
        /* Removing an element is equivalent to reducing its size by one */
        else
        {
                S->size--;
        }

        return S->elements[S->size];
}
double top(Stack *S)
{
        if(S->size==0)
        {

        }
        /* Return the topmost element */
        return S->elements[S->size-1];
}
void push(Stack *S,double element)
{
        /* If the stack is full, we cannot push an element into it as there is no space for it.*/
        if(S->size == S->capacity)
        {

        }
        else
        {
                /* Push an element on the top of it and increase its size by one*/
                S->elements[S->size++] = element;
        }
        return;
}
