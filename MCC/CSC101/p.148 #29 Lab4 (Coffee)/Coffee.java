/****************
Program Name: Coffee.java
Name: Andy Nguyen
Due Date: 10/28/10
Program Decription: Computes total cost of a coffee order.
*****************/


import java.util.*;
import java.text.DecimalFormat;
public class Coffee{
	public static void main(String[] args){
		//*****Declarations*****
		Scanner input = new Scanner(System.in);//Object for input
		DecimalFormat form = new DecimalFormat("$0.00");//Format output
		
		final int bagsOrdered;
		int bagsLeft;
		int small, medium, large;//Box Sizes
		double totalCost;
		
		//******User prompt & input*****
		System.out.print("Enter # of bags ordered: ");
		bagsOrdered = input.nextInt(); //Store input
		
		//*****Calculations*****
		bagsLeft = bagsOrdered;
		
		large = bagsLeft/20;//Find large boxes
		bagsLeft -= large*20;//Find bags left
		
		medium = bagsLeft/10;//Find medium boxes
		bagsLeft -= medium*10;
		
		small = bagsLeft/5;//Find small boxes
		bagsLeft -= small*5;
		
		if(bagsLeft > 0){ //Put remaining bags into a small box (not filled)
			small += 1;}
		
		totalCost = (bagsOrdered*5.5)+(large*1.8)+medium+(small*0.6);//Find total

		//******Output*****
		System.out.println();
		System.out.print("Number of Bags Ordered: "+bagsOrdered+" - "
								+form.format(bagsOrdered*5.5));
		System.out.println("\n\nBoxes Used:");
		System.out.println("\t"+large+" Large  - "
									+form.format(large*1.8));
		System.out.println("\t"+medium+" Medium - "
									+form.format(medium));
		System.out.println("\t"+small+" Small  - "
									+form.format(small*0.6));
		System.out.println("\nYour total cost is:  "+form.format(totalCost));

	} //End main method
}