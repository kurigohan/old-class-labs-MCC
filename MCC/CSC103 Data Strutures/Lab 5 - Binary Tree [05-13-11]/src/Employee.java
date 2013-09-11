
public class Employee implements Cloneable, Comparable<Employee>
{
	
	private int acctID;
	private String firstName, lastName;
	private double salary;
	
	
	public Employee(int id)
	{
		acctID = id;
	}
	
	public Employee(int id, String f_name, String l_name, double amount)
	{
		acctID = id;
		firstName = f_name;
		lastName = l_name;
		salary = amount;
	}
	
	public int getID()
	{
		return acctID;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public double getSalary()
	{
		return salary;
	}
	
	public void setSalary(double amount)
	{
		salary = amount;
	}
	
	public String toString()
	{
		return acctID + " " + firstName + " " + lastName + " " + salary;
	}
	
	public int compareTo(Employee obj)
	{
		return acctID - obj.acctID;
	}
	
	
	
}
