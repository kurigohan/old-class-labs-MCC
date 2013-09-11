//--------------------------------
// Name: Andy Nguyen
// Date: February 29, 2012
// Purpose: This program calculates the time taken to pay off a loan for 
//			a given monthly payment. 
//---------------------------------

#include <iostream>
#include <iomanip>
using namespace std;


//-------Function Prototypes-------
void Intro();
void calculateMonths(double& loanLeft, double& mPayment, double mInterest, 
						double& tInterest, int& count);
void calcMInterest(double& mInterest, double yInterest);
void getLoanAmount(double& loan, double& loanLeft);
void getInterest(double& yInterest);
void getPaymentAmount(double& mPayment, double mInterest, double loan);
void outputHeader(double loan, double yInterest);
void displayResult(double loan, double tInterest, int count);
void rerun(int&	quit);
//---------------------------------



//********************************************
// function: Intro
// A function to disply the program introduction.
// parameter: none
// output : none
//********************************************
void Intro()
{
	cout << "-----Welcome to Loan Calculator v1.1-----\n";
	cout << "This program will calculate the time taken to pay off a loan.\n\n";
}

//********************************************
// function: getLoanAmount
// A function to retrieve the loan amount from the user.
// parameter: loan - loan amount, 
//			 loanLeft - loan left to pay off
// output : none
//********************************************
void getLoanAmount(double& loan, double& loanLeft)
{
	cout << "Enter loan amount: ";
	cin >> loan ;
	loanLeft = loan;
}

//********************************************
// function: getInterest
// A function to retrieve the annual interest rate from the user.
// parameter: yInterest - yearly interest rate
// output : none
//********************************************
void getInterest(double& yInterest)
{
	cout << "Enter yearly interest rate (e.g. 0.10 for 10%): ";
	cin >> yInterest;
}

//********************************************
// function: getPaymentAmount
// A function to retrieve the monthly payment from the user.
// parameter: mPayment - monthly payment,
//			 mInterest - monthly interest rate,
//			 loan - loan amount
// output : none
//********************************************
void getPaymentAmount(double& mPayment, double mInterest, double loan)
{
	do//run until valid amount entered
	{
		cout << "Enter monthly payment: ";
		cin >> mPayment;
		if(mPayment < (loan*mInterest))
			cout << "\nThe payment does not cover the interest. Try again.\n\n";
	}while(mPayment < (loan*mInterest));//end do while loop
}

//********************************************
// function: calcMInterest
// A function to calculate the monthly interest rate.
// parameter: mInterest - monthly interest rate,
//			 yInterest - yearly interest rate
// output : none
//********************************************
void calcMInterest(double& mInterest, double yInterest)
{
	mInterest = yInterest/12.0;

}

//********************************************
// function: calculateMonths
// A function to calculate the months taken to pay off the loan
// and the payment info per month.
// parameter: loanLeft - loan left to payoff,
//			  mPayment - monthly payment,
//			  mInterest - monthly interest rate,
//			  tInterest - total accumulated interest,
//			  count - months taken to pay loan;
// output : none
//********************************************
void calculateMonths(double& loanLeft, double& mPayment, double mInterest, double& tInterest, int& count)
{
	while(loanLeft > 0)
	{ //stops when monthly payment exceeds loan amount
		count++;

		if(loanLeft < (mPayment - (mPayment*mInterest)))//if payment exceeds remaining loan
			mPayment = loanLeft + (loanLeft*mInterest); //set payment to remaining loan
	
		cout << setw(3) << count << setw(18) << loanLeft << setw(18) << mPayment << setw(18) 
			 << (loanLeft*mInterest) << setw(18);
		cout << (mPayment - (loanLeft*mInterest)) << endl;;

		tInterest += (loanLeft*mInterest);
		loanLeft -= mPayment - (loanLeft*mInterest);
	}//end while loop
}

//********************************************
// function: displayResult
// A function to display the results.
// parameter: loan, tInterest, count
// output : none
//********************************************
void displayResult(double loan, double tInterest, int count)
{
	cout << "\nNumber of months to pay off loan: " << count << " month(s).";
	cout << "\nTotal interest: $" << tInterest << endl;
	cout << "Annualized interest: $" << tInterest/(count/12.0) << endl;
	cout << "Percent of the annualized interest from the total loan: %" 
		 << ((tInterest/(count/12.0))/loan)*100 << endl;
}

//********************************************
// function: outputHeader
// A function to output the monthly chart header.
//parameter: loan, loanLeft
//output : none
//********************************************
void outputHeader(double loan, double yInterest)
{
	cout << "\n\nLoan Amount: $" << loan;
	cout << "\nInterest Rate: " << (yInterest*100) << "%" << endl;
	cout << "\n Month" << setw(14) << "Balance" << setw(24) << "Monthly Payment" << setw(14)
		 << "Interest" << setw(18) << "Principle";
	cout << "\n---------------------------------------------------------------------------\n";

}

//********************************************
// function: rerun
// A function to rerun the program.
//parameter: quit - keeps running if 0, close if 1;
//output : none
//********************************************
void rerun(int& quit)
{
	int flag = 0;
	char exit;
	while (flag == 0)
	{ //keep running until y, Y, n, or N is input

		cout << "\nWould you like to rerun the program? (y/n) ";
		cin >> exit;
	
		if(exit == 'y' || exit == 'Y'){
			cout << "Rerunning program...\n\n";
			flag = 1;
		}
		else if(exit == 'n' || exit == 'N'){
			cout << "Exiting program...\n\n";
			flag = 1;
			quit = 1;
		}
		else
			cout << "\nInvalid input. Please enter 'y' or 'n'.";		
	} //end while loop 

}


void main()
{
	double  loan = 0, //loan amount
			loanLeft = 0, //loan left to pay off
			yInterest = 0, //yearly interest rate
			mInterest = 0, //monthly interest rate
			mPayment = 0, //monthly payment
			tInterest = 0; //total interest amount
	int count,
		quit = 0;
	   

	Intro();

	while(quit == 0)
	{//runs until user inputs 'n' or 'N'
		count = 0; //reset months for reruns
		//---user prompts---
		getLoanAmount(loan, loanLeft);
		getInterest(yInterest);
		calcMInterest(mInterest, yInterest);
		getPaymentAmount(mPayment, mInterest, loan);
		//------------------

		//---format output to 2 decimal places---
		cout.setf(ios::fixed);                      
		cout.setf(ios::showpoint);      
		cout.precision(2); 
		//---------------------------------------

		outputHeader(loan, yInterest);

		calculateMonths(loanLeft, mPayment, mInterest, tInterest, count);
		displayResult(loan, tInterest, count);
		rerun(quit);
	}//end while loop
}

/*--------- Original program without functions----------
void main()
{
	double  loan, //loan amount
			loanLeft, //loan left to pay off
			yInterest, //yearly interest rate
			mInterest, //monthly interest rate
			mPayment, //monthly payment
			aInterest, //annualized interest amount
			tInterest = 0; //total interest amount

	int count = 0, //months to pay off loan 
		quit = 0, //quit program when 1
		flag = 0; //flag = 1 when condition met

	char rerun;

	do{
	//user prompt
	cout << "Enter loan amount: ";
	cin >> loan ;

	loanLeft = loan;

	cout << "Enter interest rate (e.g. 0.10 for 10%): ";
	cin >> yInterest;

	mInterest = yInterest/12.0; //find monthly interest rate

	do{
		cout << "Enter monthly payment: ";
		cin >> mPayment;

		if(mPayment > (loanLeft*mInterest)){
			flag = 1;
			
		}
		else
			cout << "\nThe payment does not cover the interest. Try again.";

	} while(flag != 1); //keeps looping until a valid payment is entered

	//format output to 2 decimal places
	cout.setf(ios::fixed);                      
    cout.setf(ios::showpoint);      
    cout.precision(2);                       
	
	cout << "\n\nLoan Amount: $" << loan;
	cout << "\nInterest Rate: " << (yInterest*100) << "%" << endl;
	cout << "\n Month" << setw(14) << "Balance" << setw(24) << "Monthly Payment" << setw(14)
		 << "Interest" << setw(18) << "Principle";
	cout << "\n---------------------------------------------------------------------------\n";

	while(loanLeft > mPayment){ //stops when monthly payment exceeds loan amount

		tInterest += (loanLeft*mInterest);
		loanLeft -= mPayment - (loanLeft*mInterest);
		
		count++;

		cout << setw(3) << count << setw(18) << loanLeft << setw(18) << mPayment << setw(18)
			 << (loanLeft*mInterest) << setw(18);
		cout << (mPayment - (loanLeft*mInterest)) << endl;;

	}//end while loop

	//calculate and output last row
	count++;
	mPayment = loanLeft + (loanLeft*mInterest);
	cout << setw(3) << count << setw(18) << loanLeft << setw(18) << mPayment << setw(18) 
		 << (loanLeft*mInterest) << setw(18);
	cout << (mPayment - (loanLeft*mInterest)) << endl;;
	tInterest += (loanLeft*mInterest);

	aInterest = tInterest/(count/12.0); //calculate annualized interest

	cout << "\nNumber of months to pay off loan: " << count << " month(s).";
	cout << "\nTotal interest: $" << tInterest << endl;
	cout << "Annualized interest: $" << aInterest << endl;
	cout << "Percent of the annualized interest from the total loan: %" << (aInterest/loan)*100;
	
	while (flag == 1){ //keep running until y or n is input

		cout << "\n\nWould you like to rerun the program? (y/n) ";
		cin >> rerun;
	
		if(rerun == 'y' || rerun == 'Y'){
			cout << "Rerunning program..." << endl;
			flag = 0;
		}

		else if(rerun == 'n' || rerun == 'N'){
			cout << "Exiting program...\n\n";
			flag = 0;
			quit = 1;
		}

		else
			cout << "\nInvalid input. Please enter 'y' or 'n'.";		

	} //end while loop 

	}while (quit != 1); //end do while loop

}//end main*/
//-----------------------------------------------------