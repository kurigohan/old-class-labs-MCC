/*	Andy Nguyen
	Lab 2 - BMI (Part 1)
	October 7, 2010
	Purpose: Gets height & weight from user and calculates BMI.
 */

import javax.swing.*;
public class Bmi {
	
	public static void main(String[] args){
		
		//Variables & Objects declaration
		String heightString, weightString;
		int height, weight;
		double BMI;
		final double FACTOR = 100.0; //Constant
		//Create display window
		JFrame window;
		window = new JFrame();
		window.setTitle("BMI Program");
		window.setVisible(true);
		//*******************************
		
		//Get height & weight input from user
		heightString = JOptionPane.showInputDialog
									(null, "Enter your height in centimeters: ");
		weightString = JOptionPane.showInputDialog
									(null, "Enter your weight in kilograms: ");
		
		//Convert height & weight strings to integer
		height = Integer.parseInt(heightString);
		weight = Integer.parseInt(weightString);
		
		//Calculate BMI 
		BMI = (weight)/((height/FACTOR)*(height/FACTOR));
		
		//Show output message
		JOptionPane.showMessageDialog(null, "Your BMI is "+BMI+".");
		
	}//End main method
}
