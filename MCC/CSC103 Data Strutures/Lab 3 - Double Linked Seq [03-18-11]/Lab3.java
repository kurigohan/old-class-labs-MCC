import java.io.*;
import java.util.*;


public class Lab3 {
	  public static void main (String[] args) {
	       try {
	        	Scanner fileIn = new Scanner(new FileReader("sequence.txt"));
                
	            SequenceTest tester = new SequenceTest ();
	            
	            while (fileIn.hasNextLine()!=false) {
	        		tester.menu(fileIn.nextLine());
	            }
	            
	            fileIn.close ();
	        }
	        catch (IOException e) {
	            System.out.println ("Error reading file.");
	        }
	    }
}

