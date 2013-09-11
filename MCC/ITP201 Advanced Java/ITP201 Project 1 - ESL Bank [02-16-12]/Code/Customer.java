import java.text.NumberFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Customer class represents customer accounts at a bank.
 * The class Customer includes methods to change customer information,
 * sort customer arrays, search for customer name, and add interest.'
 *
 * @author Andy Nguyen & Alessandro Melino
 */
public class Customer
{

	private String name;
	private long idNumber;
	private double balance;
	private String phoneNumber;

	//Used to format balance amount to correct decimal places
	private NumberFormat fmt = NumberFormat.getCurrencyInstance();

	/**
	 * Initializes a new empty Customer object.
	 */
	Customer()
	{
		name = "";
		idNumber = 0;
		phoneNumber = "";
		balance = 0;
	}

	/**
	 * Constructs a new Customer object with the specified parameters.
	 * @param n Customer Name
	 * @param i Customer Number
	 * @param b Customer Account Balance
	 * @param p Customer Phone Number
	 */
	Customer(String n, long i, double b, String p)
	{
		name = n;
		idNumber = i;
		phoneNumber = p;
		balance = b;
	}

	/**
	 * Subtracts the withdrawal amount and withdrawal fee from the customer's
	 * balance.
	 * @param amount Withdrawal Amount
	 * @param fee Withdrawal Fee
	 * @return Updated account balance
	 */
	public double withdraw(double amount, double fee)
	{
		if(amount < 0){
			JOptionPane.showMessageDialog(new JFrame(), "Error: Withrawal amount " 
					+ "is invalid\n" + "The withdrawal amount must be positive",
					"Error", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		else if (amount == 0)
			return balance;

		else if(balance < (amount+fee))
			JOptionPane.showMessageDialog(new JFrame(), "Error: Insufficient funds.\n"
					+ "Customer: " + name + "\n Requested: " + fmt.format(amount+fee)
					+ "\nBalance: " + fmt.format(balance), 
					"Error", JOptionPane.ERROR_MESSAGE);
		else
			balance -= amount+fee;

		return balance;
	}

	/**
	 * Adds the deposit amount to the customer's balance.
	 * @param amount Deposit amount
	 * @return Updated account balance
	 */
	public double deposit(double amount)
	{
		if(amount < 0){
			JOptionPane.showMessageDialog(new JFrame(), 
					"Error: Deposit amount is invalid.\n"
					+"The deposit amount must be positive.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		else
			balance += amount;

		return balance;
	}


	/**
	 * Adds interest to the customer balance.
	 * @return Updated account balance
	 */
	public double addInterest()
	{
		balance += balance*.045;

		if (balance < 0)
			balance = 0;

		return balance;
	}


	/**
	 * Returns the customer's balance.
	 * @return Account balance
	 */
	public double getBalance()
	{
		return balance;
	}

	/**
	 * Sets the balance to the amount specified.
	 * @param amount Balance amount
	 */
	public void setBalance(double amount)
	{
		if(amount < 0)
			JOptionPane.showMessageDialog(new JFrame(), 
					"Error: Amount entered is invalid.\n"
					, "Error", JOptionPane.ERROR_MESSAGE);

		else
			balance = amount;
	}

	/**
	 * Returns the phone number.
	 * @return Customer phone number
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * Sets the phone number to the number passed
	 * @param num Customer phone number
	 */
	public void setPhoneNumber(String num)
	{
		phoneNumber = num;
	}

	/**
	 * Returns the customer id number.
	 * @return Customer ID number
	 */
	public long getCustomerNumber()
	{
		return idNumber;
	}


	/**
	 * Sets customer ID number to num.
	 * @param num Customer ID number
	 */
	public void setCustomerNumber(int num)
	{
		if(num < 0)
			JOptionPane.showMessageDialog(new JFrame(),
					"Error: Number entered is invalid.\n"
					, "Error", JOptionPane.ERROR_MESSAGE);

		else
			idNumber = num;
	}

	/**
	 * Returns the customer name.
	 * @return Customer name
	 */
	public String getCustomerName()
	{
		return name;
	}

	/**
	 * Sets customer name to n.
	 */
	public void setCustomerName(String n)
	{
		name = n;
	}

	/**
	 * Searches the customer array for the specified customer name and
	 * returns the index. Returns -1 if the name was not found.
	 * @param custArray Customer array
	 * @param name Customer name
	 * @param count Current array size
	 * @return Index number
	 */
	public int findIndex(Customer[] custArray, String name, int count)
	{
		for(int i = 0; i < count; i++) //search entire array until match found

			if(custArray[i].getCustomerName().compareTo(name) == 0)
				return i;

		return  -1;
	}//end findIndex method

	/**
	 * Sorts the array of customers using a selection sort.
	 * @param custArray Customer array
	 * @param count Current array Size
	 */
	public void nameSort(Customer[] custArray, int count)
	{
		for(int i = 0; i < count - 1; i++)
		{
			boolean exchange = false;
			int smallPos = i;
			
			for(int j = i+1; j < count; j++)
			{
				if(custArray[smallPos].getCustomerName().compareTo(custArray[j].
						getCustomerName()) > 0)
				{
					smallPos = j;
					exchange = true;
				}
			}
		
		//switch smallest to ith location
			if(exchange)
			{
				Customer temp = custArray[i];
				custArray[i] = custArray[smallPos];
				custArray[smallPos] = temp;
				exchange = false;
			}
		}//end for i loop
	}//end method nameSort
	
	/**
	 * 	Returns the customer name, ID, balance, and phone number.
	 * @return String object containing customer information
	 */
	public String toString(){
		return (name + "\t" + idNumber + "\t" + fmt.format(balance) + "\t" + phoneNumber);
	}
}
