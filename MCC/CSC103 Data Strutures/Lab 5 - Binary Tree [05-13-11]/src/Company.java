import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;


public class Company
{
	private String[] params; 
	private Scanner fileIn;
	private TreeBag tree;
	private Employee employee;
	
	Company()
	{
		params = null;
		fileIn = null;
		tree = new TreeBag();
		employee = null;
	}

	public void menu(String input) throws FileNotFoundException
	{
		
		System.out.println("Input line: "+input+"\n");
		
		params = input.split("[ -]+"); //split string
		
		int choice = Integer.parseInt(params[0]);
		
		switch(choice){
		case 1: 
				//System.out.println("Employees stored in binary tree by account ID.");
				fileIn = new Scanner(new FileReader(params[1]));
				String fileLine;
				while(fileIn.hasNextLine() != false) //read file
				{
					fileLine = fileIn.nextLine();
					params = fileLine.split("\\s+"); //split at spaces
					employee = new Employee(Integer.parseInt(params[0]), params[1], 
		        						params[2], Double.parseDouble(params[3])); //create employee
		        	tree.add(employee); //add employee to tree
				}
		        fileIn.close();
		        tree.display();
		        
				break;
				
		case 2: 
				//System.out.println("Add an employee to the tree.");
				employee = new Employee(Integer.parseInt(params[1]), params[2], 
						params[3], Double.parseDouble(params[4]));
				tree.add(employee);
				tree.display();
				break;
		
		case 3: 
				//System.out.println("Remove an employee from the tree.");
                employee = new Employee (Integer.parseInt(params[1]));
			    if (!tree.remove(employee) ) //remove employee
					System.out.println("ID could not be found.");
				tree.display();
				break;
				
		case 4:
				//System.out.println("Retrieve an employee.");
                employee = new Employee (Integer.parseInt(params[1]));
				if(tree.retrieve(employee) != null)
					System.out.println(tree.retrieve(employee).getData());
				else
					System.out.println("ID could not be found.");
				
				break;
				
		case 5:
				//System.out.println("Update an employee.");
                employee = new Employee (Integer.parseInt(params[1]));
				if(tree.retrieve(employee) != null)
				{
					employee = (Employee) tree.retrieve(employee).getData();
					employee.setSalary(Double.parseDouble(params[2]));
				}
				else
					System.out.println("ID could not be found.");
				tree.display();
				break;
		case 6:
				//System.out.println("Display employees.");
				tree.display();
                
                try
                {
                    FileWriter output = new FileWriter ("output.txt");
                    output.write (tree.toString () );
                    output.close ();
                }
                catch (IOException ex)
                {
                    System.out.println ("File write error");
                }
				
				break;
				
		case 7:
				System.out.println("Quit program.");
				
				break;
				
		default:
				System.out.println("Invalid input.");
				break;
				
		}
		
		
	}
	
}
	
	

