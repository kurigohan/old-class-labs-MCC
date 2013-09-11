/* Program Name: Vowels.java
	Student Name: Andy Nguyen
	Date: October 4, 2010
	Purpose: Count number of vowels & non-vowels in a String.
*/

import java.io.*;
class Vowels{
	public static void main(String[] args)
									throws IOException{
		
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String userStr; //User inputted String
		int strLength, //String length
			 vowelNum=0, //# of vowels
			 nonVowNum=0; //# of non-vowels
		
	
		
		
		System.out.print("Enter a string: ");
		userStr = stdin.readLine();
		strLength = userStr.length();
		
		for(int count=0; count<strLength; count++){
			if(userStr.charAt(count)=='a')
				vowelNum +=1;
			else if(userStr.charAt(count)=='e')
				vowelNum +=1;
			else if(userStr.charAt(count)=='i')
				vowelNum +=1;
			else if(userStr.charAt(count)=='o')
				vowelNum +=1; 
			else if(userStr.charAt(count)=='u')
				vowelNum +=1;
			else
				nonVowNum +=1;
		System.out.println(userStr.charAt(count));
		}
		
		System.out.println();
		System.out.println("# of vowels: "+vowelNum);
		System.out.println("# of nonvowels: "+nonVowNum);
	
	
	
	
	}//end main

}