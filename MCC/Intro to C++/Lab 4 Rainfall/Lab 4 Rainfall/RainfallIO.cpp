//-------------------------------
//Andy Nguyen & David Rhoads
//Date: 3/15/12
//Purpose: Reads an input file containing months and rainfall
//		   and then outputs to a file a bar graph of the data.
//-------------------------------

#include <fstream>
#include <iostream>
#include <iomanip>
#include <iostream>
#include <string>

using namespace std;

//------Function Prototypes------
string writeMonth(int monthNum);
void drawScaleLine(ofstream& out_stream);
void getInput(ifstream& in_stream, ofstream& out_stream, int arrayRain[][3], int row, int column);
void drawBar(ofstream& out_stream, int arrayRain[][3], int row, int& currentYear);
void monthlyTotal(int arrayRain[][3], int row, int& maxRain, int& maxMonth, int& totalRain);
void statistics(ofstream& out_stream,int& maxRain, int& maxMonth);
void openOutputFile();
//-------------------------------


void main()
{
	ifstream in_stream("rainInput.txt");
	ofstream out_stream("rainOutput.txt");

	//----Variables-----------
	int totalRain = 0, //total rainfall
		maxRain = 0, //max rainfall
		maxYear = 0, //year of most rainfall
		maxMonth = 1, //month of most rainfall
		currentYear = 0; //current year

	int arrayRain[15][3]; //array to store year, month, rainfall
	int row = 0, //current row
		column = 0; //current column
	//------------------------


	if (in_stream.fail())           
	{
		cout << "Error: Input file could not be opened.\n";
		out_stream << "Cannot open input file.";
		exit(1); // terminate the program
	}

	//while(in_stream.good())
	while(row < 15)
	{	
		getInput(in_stream, out_stream, arrayRain, row, column);
		drawBar(out_stream, arrayRain, row, currentYear);
		monthlyTotal(arrayRain, row, maxRain, maxMonth, totalRain);
		row++;
	}

	drawScaleLine(out_stream);

	statistics(out_stream,totalRain, maxMonth);

	
	in_stream.close();
	out_stream.close();
	cout << "Program run successful.\n";
	openOutputFile();
	exit(1); // terminate the program
}



//********************************************
// function: openOutputFile
// Opens output file in notepad.
// parameter: none
// output : none
//********************************************
void openOutputFile()
{
	system("notepad.exe rainOutput.txt"); 
}



//********************************************
// function: writeMonth
// Converts number to month.
// parameter: month number
// output : string containing month
//********************************************
string writeMonth(int monthNum)
{
	string month;
	switch(monthNum)
	{
		case 1: 
			month = "January";
			return month;
		case 2: 
			month = "February";
			return month;
		case 3: 
			month = "March";
			return month;
		case 4: 
			month = "April";
			return month;
		case 5: 
			month = "May";
			return month;
		case 6: 
			month = "June";
			return month;
		case 7: 
			month = "July";
			return month;
		case 8: 
			month = "August";
			return month;
		case 9: 
			month = "September";
			return month;
		case 10: 
			month = "October";
			return month;
		case 11: 
			month = "November";
			return month;
		case 12: 
			month = "December";
			return month;
		default:
			month = "Invalid month";
			return month;

	}

}



//********************************************
// function: getInput
// Retrieves data from input file and stores in array.
// parameter: in_stream, out_stream, arrayRain, row, column
// output : none
//********************************************
void getInput(ifstream& in_stream, ofstream& out_stream, int arrayRain[][3],int row, int column)
{ // year, month, rainfall
	in_stream  >> arrayRain[row][column] >> arrayRain[row][column+1] >> arrayRain[row][column+2] ;
}



//********************************************
// function: drawBar
// Writes year and month and draws bar graph to output file.
// parameter: out_stream, arrayRain, row, currentYear
// output : bar graph
//********************************************
void drawBar(ofstream& out_stream, int arrayRain[][3], int row, int& currentYear)
{
	
	//format output
	out_stream.setf(ios::left);
	if(currentYear != arrayRain[row][0]) // check if year changed
	{
		currentYear = arrayRain[row][0];
		out_stream << setw(5) << arrayRain[row][0]; // display new year
	}
	else
		out_stream << setw(5) << "   ";

	out_stream << setw(10) << writeMonth (arrayRain[row][1]) << "|";
	out_stream << " ";

	for(int i = 0; i < arrayRain[row][2]; i++)
	{// draw bar
		out_stream << "*****";
	}

	out_stream << endl;

}



//********************************************
// function: drawScaleLine
// Draws scale line to output file.
// parameter: out_stream
// output : scale line
//********************************************
void drawScaleLine(ofstream& out_stream)
{
	out_stream.setf(ios::right);
	out_stream << setw(68) << "-----------------------------------------------------" << endl;
	out_stream << setw(68) << "****1****2****3****4****5****6****7****8****9****10" << endl;
}



//********************************************
// function: monthlyTotal
// Calculates monthly totals and maxes.
// parameter: arrayRain, row, maxRain, maxMonth, totalRain
// output : none
//********************************************
void monthlyTotal(int arrayRain[][3], int row, int& maxRain, int& maxMonth, int& totalRain)
{
	//int temp = arrayRain[row][2];
	if(arrayRain[row][2] > maxRain)
	{
		maxRain = arrayRain[row][2];
		maxMonth = arrayRain[row][1];
	}

	totalRain += arrayRain[row][2];
}


void rainPerYear(int arrayRain[][3], int row)
{
	//int r = 0;
	int currentYear;
	for(int i = 0; i < row; i++)
		for(int j = 0; j < 3; j++)
			currentYear +;


}


//********************************************
// function: statistics
// Outputs statistics to output file.
// parameter: out_stream, totalRain, maxMonth
// output : statistics data
//********************************************
void statistics(ofstream& out_stream,int& totalRain, int& maxMonth)
{
	out_stream << "\nMonth with most rainfall: " << writeMonth(maxMonth) << endl;
	out_stream << "Total rainfall: " << totalRain << " inches" << endl;
}
