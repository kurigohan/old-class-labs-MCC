/***********
Program Name: Root.java
Name: Andy Nguyen
Due Date: October 7, 2010
Program Description: Gets variables a, b, c and
						   finds x1, x2 using quadratic formula.
************/

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.*;

public class Root {

	public static void main(String[] args){
	
	//Variables & Objects declaration
	String vString;
	int a, b, c;  //Coefficients
	double x1, x2; //Outputs
	
	JFrame window;
	window = new JFrame();
	window.setTitle("Root Program");
	window.setVisible(true);
	
	//*****User Input*****
	vString = JOptionPane.showInputDialog(null, "Enter variable A: ");
	a = Integer.parseInt(vString);
	
	vString = JOptionPane.showInputDialog(null, "Enter variable B: ");
	b = Integer.parseInt(vString);
	
	vString = JOptionPane.showInputDialog(null, "Enter variable C: ");
	c = Integer.parseInt(vString);
	//********************
	
	//Quadratic Formula	
	x1 = (-b - (Math.sqrt((b*b)-(4*a*c))))/(2*a);
	x2 = (-b + (Math.sqrt((b*b)-(4*a*c))))/(2*a);
	
	DecimalFormat output = new DecimalFormat("0.000");
	//Output result
	JOptionPane.showMessageDialog(null, "If A = "+a+"  B = "+b+
					"  C = "+c+", then X1 = "+output.format(x1)+
					" and X2 = "+output.format(x2));
	
	}//End main method
}
	