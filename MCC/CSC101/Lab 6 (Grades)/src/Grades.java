/* Program: Grades.java
	Student: Andy Nguyen
	Date: November 11, 2010
	Purpose: Reads from a file a list of students with their Grade.
*/
import java.util.*;
import java.io.*;
class Grades{
	public static void main(String[] args)
									throws IOException{
									 
		//*****Variables & Declarations*****
		String firstName="", lastName="", //student name
				 fileStr, //String to split(tokenizer)
				 delim=" .,-\t\n"; //where to split(tokenizer)
				 
		String str69below="Students with grade 69 and below:\n",
			   str70to79="Students with grade 70 to 79:\n",
			   str80to89="Students with grade 80 to 89:\n",
			   str90to100="Students with grade 90 to 100:\n";
				
		int grade=0, //student grade
			average=0, //total average grade
			countGrade=0, //count # of students w/ grades
			countLine=0; //count file line #
		
		StringTokenizer read;
		Scanner fileIn = new Scanner(new FileReader("GPA.txt"));
		
		//******Calculations******
		while(fileIn.hasNextLine()!=false){//stop when no lines to read
			fileStr=fileIn.nextLine();//read next line of file
			read = new StringTokenizer(fileStr,delim,false);//instantiate
			countLine++;
			
			if(read.countTokens()==3){//check for missing data
				
			  while(read.countTokens()!=0){ //stop when no tokens to read
				  
			    firstName = read.nextToken();
			    lastName = read.nextToken();
			    grade = Integer.parseInt(read.nextToken());
			    if(grade<70)
			      str69below += firstName+" "+lastName+"  "
			      						+Integer.toString(grade)+"\n"; 
			    else if(grade<80)
			      str70to79 += firstName+" "+lastName+"  "
			      						+Integer.toString(grade)+"\n"; 
			    else if(grade<90)
			      str80to89 += firstName+" "+lastName+"  "
			      						+Integer.toString(grade)+"\n"; 
			    else if(grade<=100)
			      str90to100 += firstName+" "+lastName+"  "
			     						+Integer.toString(grade)+"\n";
			  countGrade++;
			  average+=grade;
			  
			  }//end while loop
			}//end if statement
			
			else
				System.out.println("Insufficient data on line #"
						+countLine+".\n");
			
		}//end while loop
		
		if(fileIn!=null)
			fileIn.close(); //close file
		
		//******Output*****
		System.out.println(str69below+"\n"+str70to79+"\n"+str80to89
										+"\n"+str90to100);
		System.out.println("The average grade is "+(average/countGrade)+".");

	}//end main
}//end class
	