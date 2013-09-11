/*
---------------------------- INFO ----------------------------------
Purpose: Operation.c contains functions to convert an input from pre-fix notation to post-fix notation and calculate the result.

Conditions:
    - If an input contains a negative number, spaces must be used to seperate numbers from minus operators. ex. -1 - 2
    - The maximum output length for inToPost is 128 characters.

Author: Andy Nguyen
Last updated: 12/11/12

---------------------------- SOURCES --------------------------------
    inToPost Function based on the Shunting-yard sample code on Wikipedia.
    Changes:      - Work with Stack structure
                  - Handle more than 1 digit numbers
                  - Handle decimals
                  - Handle negative numbers
                  - Formatted output

    op_preced and op_left_assoc also taken from Shunting-yard sample code.
    Changes:      - Additional cases
------------------------------------------------------------------------------

*/

#include "C:\Users\Owner\Desktop\CSC202 Final\Code\Calculator v2.7\Sources\Calc.h"

/*
    The operate function performs the operation according to the passed parameter char c.
    input: Stack id - Stack to push the result to
           char c - operator
    output: none
*/
void operate(Stack *id, char op, Flag *flag)
{
    //error = false; //reset error flag
    double result = 0; double n1=0, n2=0;
    switch (op)
    {
        case '+': result = pop(id)+ pop(id); //pop from stack, perform operation, and store to result
                  break;
        case '-': n1 = pop(id); n2 = pop(id);
                  result = n2 - n1;
 
                  break;
        case '*': result = pop(id)*pop(id);
                  break;
        case '/': n1 = pop(id); n2 = pop(id);
                  if(n1 == 0) //prevent division by zero
                    flag->error = true;
                  else
                    result = n2/n1;
                  break;
        case '%': n1 = pop(id); n2 = pop(id);
                  result = fmod(n2, n1);
                  break;
        case '^': n1 = pop(id); n2 = pop(id);
                  result = pow(n2, n1);
                  break;
        case 'E': n1 = pop(id);
                  n2 = pop(id);
                  result = n2*pow(10, n1);
                  break;
        default: break;
    }

    push(id, result);
}

/*
    The fact function finds the factorial of the number passed to it.
    input: int num - number to find the factorial of
    output: factorial of num
*/
/*
    The calculate function determines the result of the post-fix notation string passed to it.
    input: const char post - postfix string
    output: answer
*/
double calculate(const char *post, Flag *flag)
{

    const int sz = strlen(post); //size of the post string
    const char *strpos = post, *strend = post + sz;
    char op; //current char
    char out[128], *num = out; //string used to store multi-digit numbers to convert to double.
    char *temp = num; //temp stores num pointer. used to return to starting location of the string.
    Stack *id = createStack(sz); //stack that hold the identifiers (numbers)
    bool next; //flag to indicate if the next char in the string is part of the current number.
    bool nega = false; //flag to indicate if a negative number
    double n;   //n used to hold converted multi-digit number.

    while (strpos < strend)
    {
        op = *strpos;


        if(is_number(op)) //if identifier
        {

            //Check if multiple digits
            if(is_number(*(strpos+1)))// check if next char is also identifier (operand)
            {

                next = true;
                *num++ = op;  //add current char to num string

                while(next)
                {

                    op = *++strpos; //c = next char

                    if(is_number(op))
                    {
                        *num++ = op; //add to num string
                    }
                    else
                    {
                        next = false;
                        op = *--strpos; //go back one since next char was not a number
                    }
                }

                *num = 0; //null terminate
                n = atof(out); //convert string to double
                out[0] = '\0';
                num = temp;
            }
            else
            {
                n = op-'0';
            }
            if(nega == true) //negative number
            {
                n = n*-1.0; //multiple n by -1 to make it negative
                nega = false; //reset negative flag
            }

            push(id, n);

        }

        else if(is_operator(op)) //operator
        {
            if(op == '-' && is_number(*(strpos+1)))//if c = '-' and the next char is an identifier, then c is a negative sign
            {
                nega = true;
            }
            else
                operate(id, op, flag);
        }

        ++strpos;
    }


    return pop(id);

}

/*
    The op_preced function determines operator precedence.
    operators
    precedence   operators       associativity
    3            ^             right to left
    2            * / %           left to right
    1            + -             left to right

    input: char op - operator
    output: operator precedence (1-3)
*/
int op_preced(char op)
{
     switch(op)
     {
        case '^':
            return 3;
        case 'E': case '*':  case '/': case '%':  
            return 2;
        case '+': case '-':
            return 1;
    }
    return 0;
}


/*
    The inToPost function converts from pre-fix notation to post-fix notation.
    input: const char input - prefix notation string
    output: returns true if successful, false if failure.

*/
bool inToPost(const char *input, char *output)
{
    const int sz = strlen(input); //length of input string
    const char *strpos = input, *strend = input + sz;
    char c, *outpos = output;
    char sc; //stores element at top of stack
    bool next; //indicates that next char is a digit


    Stack *stk = createStack(sz);

    while (strpos < strend)
    {
        c = *strpos;

        if(c != ' '){//-----------

        if(c == '-' && is_number(*(strpos+1)))//if c = '-' and the next char is an identifier, then c is a negative sign
        {
                *outpos++ = c; //add the '-' to the output queue
        }
        // If the token is a number (identifier), then add it to the output queue.
        else if(is_number(c)){
            *outpos++ = c;

            if(is_number(*(strpos+1)))
            {
                next = true;
                while(next){ //check if numnber is more than 1 digit
                    c = *++strpos;
                    if(is_number(c))
                    {
                        *outpos++ = c;
                    }
                    else
                    {
                        next = false;
                        c = *--strpos;
                    }
                }
            }

            *outpos++ = ' ';  //add space
        }

         // If the token is an operator, op1, then:
        else if(is_operator(c))  {
            if((c == 'E') && (*(strpos+1) == '+'))
                strpos++; //skip over '+' since it only indicates positive
            while(stk->size > 0)    {
                sc = top(stk);
                // While there is an operator token, op2, at the top of the stack
                // op1 is left-associative and its precedence is less than or equal to that of op2,
                // or op1 has precedence less than that of op2,
                // Let + and ^ be right associative.
                // Correct transformation from 1^2+3 is 12^3+
                // The differing operator priority decides pop / push
                // If 2 operators have equal priority then associativity decides.
                if(is_operator(sc) &&
                    ((op_preced(c) <= op_preced(sc)) ||
                        (op_preced(c) < op_preced(sc))))   {
                    // Pop op2 off the stack, onto the output queue;
                    *outpos++ = pop(stk);
                    *outpos++ = ' ';
                }
                else  {
                    break;
                }
            }
            // push op1 onto the stack.
            push(stk,c);
        }

        // If the token is a left parenthesis, then push it onto the stack.
        else if(c == '(')   {
            push(stk,c);
        }
        // If the token is a right parenthesis:
        else if(c == ')')    {
            bool pe = false;
            // Until the token at the top of the stack is a left parenthesis,
            // pop operators off the stack onto the output queue
            while(stk->size > 0)     {
                sc = top(stk);
                if(sc == '(')    {
                    pe = true;
                    break;
                }
                else  {
                    *outpos++ = pop(stk);

                    *outpos++ = ' ';
                }
            }
            // If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
            if(!pe)  {
                return false;
            }
            // Pop the left parenthesis from the stack, but not onto the output queue.
           (void) pop(stk);
        }
        else  {
            return false; // Unknown token
        }

        }//------

        ++strpos;
    }

    // When there are no more tokens to read:
    // While there are still operator tokens in the stack:
    while(stk->size > 0)  {
        sc = top(stk);
        if(sc == '(' || sc == ')')   {
           // error = true;
            return false;
        }
        *outpos++ = pop(stk);

        *outpos++ = ' ';
    }
    *outpos = 0; // Null terminator
    return true;

}
