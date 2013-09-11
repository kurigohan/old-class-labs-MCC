import java.io.*;
import java.text.DecimalFormat;

public class QuadTest {
	private BufferedWriter outfile;
	private DecimalFormat format;
	
	public QuadTest () {
	    format = new DecimalFormat ("0.##");
	    
	    // attempt to create a file to store output in
	    try {
	    	System.out.println ("Creating output file.\n");
	        outfile = new BufferedWriter (new FileWriter ("Output.txt") );
	    }
	    catch (IOException e) {
	        System.out.println ("Error creating output file.");
	    }
	}
	
	public void close () {
		// try closing the output file
	    try {
	    	System.out.println ("Closing output file.");
	        outfile.close ();
	    }
	    catch (IOException e) {
	        System.out.println ("Error closing file.");
	    }
	}
	
	public void intro (int testNum) {
		// write the initial output for each test
	    try {
	    	System.out.println ("Writing intro.");
	        outfile.write ("*********************************");
	        outfile.newLine ();
	        outfile.write ("Test #" + testNum);
	        outfile.newLine ();
	        outfile.write ("-------------");
	        outfile.newLine ();
	    }
	    catch (IOException e) {
	        System.out.println ("Error writing intro to file.");
	    }
	}
	
	public void parse (String line, int testNum) {
	    String[] params = line.split (" ");
	    
	    intro (testNum);
	    
	    // try parsing the line of input
	    System.out.println("Checking parameters...");
	    if (params.length == 8) {
	        try {
	        	// try to convert the text into numbers
	            double x = Double.parseDouble (params[3]);
	            double scale = Double.parseDouble (params[4]);
	            Quadratic quad1 = new Quadratic (Double.parseDouble (params[0]), Double.parseDouble (params[1]), Double.parseDouble (params[2]) );
	            Quadratic quad2 = new Quadratic (Double.parseDouble (params[5]), Double.parseDouble (params[6]), Double.parseDouble (params[7]) );
	            
	            // if the data is correct then start the calculations
	            calculations (quad1, quad2, x, scale);
	        }
	        catch (NumberFormatException e) {
	            System.out.println ("The parameters are not numbers, skipping line...");
	            output ("Parse error: The parameters are not numbers\r\n\r\n");
	        }
	    }
	   else {
	       System.out.println ("Incorrect number of parameters in line " + testNum + ", skipping line...");
	       output ("Parse error: Incorrect number of parameters\r\n\r\n");
	   }
	}
	
	public void calculations (Quadratic quad1, Quadratic quad2, double x, double scale) {
		output ("The first quadratic is:");
		output ("    " + format.format(quad1.getCoefA () ) + "x^2 + "
				       + format.format(quad1.getCoefB () ) + "x + "
				       + format.format(quad1.getCoefC () ) + "\r\n");
		
		output ("The values of the first quadratic expression with x = " + x + " is: " + quad1.evalExpression (x) + "\r\n");
		
		Quadratic qScale = quad1.scale(scale);
		output ("The first quadratic after scaling with R = " + format.format (scale) + ":");
		output ("    " + format.format(qScale.getCoefA () ) + "x^2 + "
			           + format.format(qScale.getCoefB () ) + "x + "
			           + format.format(qScale.getCoefC () ) + "\r\n");
		
		if (quad1.getRootNum() == 3)//check if infinite # of roots
			output ("Number of roots: infinity");
		else { 
			output ("Number of roots: " + quad1.getRootNum () );
			
			output ("Value of the roots:");
			if (quad1.getRootNum () == 0)//check if any real roots
				output ("    There are no real roots.");
			else if (quad1.getRootNum () == 1)//if 1 real root, display only 1 root
				output ("    Value of root 1: " + format.format (quad1.getRootOne () ) );
			else {
				output ("    Value of root 1: " + format.format (quad1.getRootOne () ) );
				output ("    Value of root 2: " + format.format (quad1.getRootTwo () ) );
			}
		}
		output ("");
		
		output ("The second quadratic is:");
		output ("    " + format.format(quad2.getCoefA () ) + "x^2 + "
				       + format.format(quad2.getCoefB () ) + "x + "
				       + format.format(quad2.getCoefC () ) + "\r\n");
		
		Quadratic qSum = quad1.sum(quad2);
		output ("The quadratic which is sum of first and second quadratics:");
		output ("    " + format.format(qSum.getCoefA () ) + "x^2 + "
				       + format.format(qSum.getCoefB () ) + "x + "
				       + format.format(qSum.getCoefC () ) + "\r\n");
		
		Quadratic qClone = (Quadratic)quad1.clone ();
		output ("A clone of the first quadratic is:");
		output ("    " + format.format(qClone.getCoefA () ) + "x^2 + "
			           + format.format(qClone.getCoefB () ) + "x + "
			           + format.format(qClone.getCoefC () ) + "\r\n");
		
		if (quad1 != qClone && quad1.equals(qClone) )
			output ("The first quadratic and the clone are not aliases, but are equal to each other.\r\n\r\n");
	}
	
	public void output (String str) {
		// try writing a line to the output file
		try {
			outfile.write (str);
			outfile.newLine ();
		}
		catch (IOException e) {
			System.out.println ("Failed to write to output file.");
		}
	}
}