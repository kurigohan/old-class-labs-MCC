import java.io.*;

public class Lab1 {
    public static void main (String[] args) {
        try {
            BufferedReader infile = new BufferedReader (new FileReader ("inputCoef.txt") );
            
            String line = null;
            QuadTest tester = new QuadTest ();
            int testNum = 1;
            
            // read the input file one line at a time and parse each line
            while ( (line = infile.readLine () ) != null) {
        		System.out.println ("Parsing: " + line);
        		tester.parse (line, testNum);
        		
        		testNum++;
            }
            
            infile.close ();
            tester.close ();
        }
        catch (IOException e) {
            System.out.println ("Error reading file.");
        }
    }
}