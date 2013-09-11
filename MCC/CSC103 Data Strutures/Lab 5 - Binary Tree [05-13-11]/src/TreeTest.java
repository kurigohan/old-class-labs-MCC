import java.io.FileNotFoundException;


public class TreeTest {
	public static void main(String[] args) throws FileNotFoundException 
	{
		  System.out.println("1. Read from a file the list of employees " +
				"and store each employee in a Binary Search Tree based on employee id.\n" +
				"2. Add an employee to the tree.\n" +
				"3. Remove an employee from the tree.\n" +
				"4. Retrieve an employee.\n" + 
				"5. Update an employee.\n" + 
				"6. Display the firm's employees in ascending order based on employee id.\n" + 
				"7. Quit the program.\n");
		
		  Company company = new Company();
		  System.out.println ("-----------------------------------------------");
	      System.out.println ("create the tree from an input file Employee.txt");
	      System.out.println ("-----------------------------------------------");
	      company.menu("1 - Employee.txt");
	      System.out.println ();
	      System.out.println ("---------------------------");
	      System.out.println ("add an employee to the tree");
	      System.out.println ("---------------------------");
	      company.menu("2 - 5290 George  Truman      	16110.68");
	      System.out.println ();
	      System.out.println ("--------------------------------");
	      System.out.println ("remove an employee from the tree");
	      System.out.println ("--------------------------------");
	      company.menu("3 - 4892");
	      System.out.println ();
	      System.out.println ("----------------------------------------------------------------");
	      System.out.println ("retrieve an employee from the tree and print the employee record");
	      System.out.println ("----------------------------------------------------------------");
	      company.menu("4 - 3924");
	      System.out.println ();
	      System.out.println ("---------------------------------------------------------------");
	      System.out.println ("update an employee from the tree and print the new ");
	      System.out.println ("---------------------------------------------------------------");
	      company.menu("5 - 3924 20000.00");
	      System.out.println ();
	      System.out.println ("----------------");
	      System.out.println ("display the tree");
	      System.out.println ("----------------");
	      company.menu("6");
	      System.out.println ("----------------------------------------------------------------------------");
	      System.out.println ();
	      System.out.println ();

		
	}
	
}
