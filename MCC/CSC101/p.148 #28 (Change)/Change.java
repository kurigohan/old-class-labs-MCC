/***********
Program Name: Change.java
Name: Andy Nguyen
Due Date: October 14, 2010
Program Description: Finds change in quarters, dimes, nickels, pennies
************/

import java.util.*;
import java.text.DecimalFormat;
public class Change {
	
	public static void main(String args[]){
		//***Variables & declarations***
		Scanner input = new Scanner(System.in); /*Scanner object for 
																keyboard input*/
		DecimalFormat form = new DecimalFormat("$0.00"); /*object to format
																			output*/
		final double total; //Constant, will be properly formatted change
		int purchasePrice, amountTender, change;
		int dollar, quarter, dime, nickel, penny; //Change
	
		//****Prompt & Input****
		System.out.print("Enter purchase price in pennies: ");
		purchasePrice = input.nextInt(); //Store user input in purchasePrice
		
		System.out.print("Enter amount tendered: ");
		amountTender = input.nextInt(); //Store user input in amountTender
		
		//****Calculations****
		change = amountTender - purchasePrice;//Find change
		total = change/100.0; /*Convert to dollars cents format, 
										store in constant*/
		
		dollar = change/100; //Find dollars
		change = change%100; //Find remaining change
		
		quarter = change/25; //Find quarters
		change = change%25; //remaining change
		
		dime = change/10; //Find dimes
		change = change%10; //Remaining change
		
		nickel = change/5; //Find nickels
		change = change%5; //Remaining change
		
		penny = change; //Pennies equals remaining change
		
		//****Output Result****
		System.out.println();
		System.out.println("Change: "+form.format(total));
		System.out.println("Dollars:\t"+dollar);
		System.out.println("Quarters:\t"+quarter);
		System.out.println("Dimes:\t\t"+dime);
		System.out.println("Nickels:\t"+nickel);
		System.out.println("Pennies:\t"+penny);
	
	}//End main method
}