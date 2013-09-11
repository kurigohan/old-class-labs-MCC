import java.util.StringTokenizer;
import java.io.*;
import javax.swing.JOptionPane;
import java.text.NumberFormat;
import java.util.Scanner;

/**
 * The purpose of this class is to run the program. It calls methods in the 
 * Customer class in order to update data collected via GUI interaction with  
 * the user. The options available are withdraw, deposit, create an accout, 
 * delete an account, and view all accounts. It also displays data and quits 
 * the program when told to do so as well as sort Customer array alphabetically.
 * 
 * @author Alessandro Melino & Andy Nguyen
 *
 */
public class BankTestDriver
{
	//----------------------------------------------------------------------
	// Reads data from a Customer input file (customer.txt). Put the records
	// in an array. Display the unsorted records to the user. Present a menu
	// of choices to the user:1) deposit sum, 2) withdraw amount,3) create a
	// new customer, 4) view all customers, 5) delete a customer 9) Quit
	// At end of file, sort the array int alphabetical order by name and
	// display all customer's accounts,
	//-----------------------------------------------------------------

	private NumberFormat fmt = NumberFormat.getCurrencyInstance();
	final double FEE = 1.50; //withdraw fee
	final int MAX = 30; //the array size
	final int MIN = 0; //the lowest account related number possible

	Customer [] custsArray = new Customer[MAX];
	int count = 0;  /* keeps track of the number of objects placed in
	 				 * the custArray array
	 				 */

	int index;      //points to the current object in the array that
									//is being accessed

	int tempAccountNumAmt; //temporary input choice, account number, or amount
	Double tempDepWithAmt; //temporary input for deposit or withdraw amount


	Scanner sc;
	StringTokenizer tokenizer; 
	String line;
	String file = "customer.txt"; //file containing customer data
	Integer tempAccNumAmt = 0; //temporary account number

	String name; //customer name
	long custNumber; //customer ID number 
	double balance; //customer account balance
	String phone; //customer phone number
	String strChoice = ""; //menu choice

	boolean quit = false; //true when customer selects quit in the menu
	int errorCounter = 0; //counts up and resets after logic in the program dictates
	int errorCounter2 = 0; //the user has failed to input valid data too many times

	double flag;         /* checks with the customer class on whether the input 
	 					  * is valid or not
	 					  */
	int choice; //menu selection


	String OutPutText = "";
	String xnam; //GUI input 

	//-----------------------------------------------------------------------------

	/**
	 * The runBankTest method runs the BankTest system.
	 * It reads the input file and store the records in custsArray
	 * The method contains a loop that prompts the users with a
	 * dialog window for choices.
	 */
	public void runBankTest()
	{
		System.out.println("Welcome to the ES&L Bank");

		try
		{
			//Reads file containing customer data and creates customer array 
			FileReader fr = new FileReader (Customer.class.getClassLoader().
					getResource("customer.txt").getPath().replaceAll("%20", " "));
			BufferedReader inFile = new BufferedReader (fr);

			line = inFile.readLine();

			while (line != null && count < MAX)
			{
				tokenizer = new StringTokenizer (line);
				name       = tokenizer.nextToken();


				try
				{
					custNumber = Long.parseLong (tokenizer.nextToken());
					balance    = Double.parseDouble (tokenizer.nextToken());
					phone      = tokenizer.nextToken();
					custsArray [count++] = new Customer(name, custNumber,
							balance, phone);

				}// end inner try block

				catch (NumberFormatException exception)
				{
					System.out.println( "Error in input. Line ignored: ");
					System.out.println (line);
				} // end catch block

				line = inFile.readLine(); // read another  record

			} //end while block

			inFile.close();

			System.out.println(" \t\tUnsorted customer.txt records");
			System.out.println("");

			for (int scan = 0; scan < count; scan++)
				System.out.println (custsArray[scan]);

			//--------------------------------------------------------------

			/*  This block gets choices from the user. Loops until 9 is input  
			 *  or cancel pressed. Does not accept invalid input. The program 
			 *  will automatically close if the user inputs invalid data five 
			 *  times (when errorCounter = 5)
			 */
			while (!quit)
			{
				do
				{
					if (errorCounter == 5){
						errorCounter = 0;
						break;
					} //end if stmt

					strChoice =
						JOptionPane.showInputDialog(null,"Please select an option: \n"
								+ "\t1. Deposit sum to account\n"
								+ "\t2. Withdraw sum from account\n"
								+ "\t3. Create account\n"
								+ "\t4. View all accounts\n"
								+ "\t5. Delete an account\n"
								+ "\t9. Quit", "ES&L Bank", JOptionPane.QUESTION_MESSAGE);

					if (strChoice == null){
						errorCounter = 0;
						strChoice = "9";
					} //end if stmt

					strChoice = strChoice.trim();
					sc = new Scanner(strChoice);

					while(!sc.hasNextInt()){
						if(errorCounter2 == 5)
							strChoice = "9";
						else{
							errorCounter2++;
							JOptionPane.showMessageDialog(null,"The input must be 1-5 or 9",
									"ES&L Bank System",
									JOptionPane.INFORMATION_MESSAGE);

							strChoice =
								JOptionPane.showInputDialog(null,"Please select an option: \n"
										+ "\t1. Deposit sum to account\n"
										+ "\t2. Withdraw sum from account\n"
										+ "\t3. Create account\n"
										+ "\t4. View all accounts\n"
										+ "\t5. Delete an account\n"
										+ "\t9. Quit", "ES&L Bank", JOptionPane.QUESTION_MESSAGE);

							if (strChoice == null){ //when cancel is pressed at the menu
								errorCounter2 = 0;
								strChoice = "9"; 
							}//end if stmt

							strChoice = strChoice.trim();
							sc = new Scanner(strChoice);
						} //end else stmt

					} //end while loop

					tempAccountNumAmt = sc.nextInt();
					if(1>tempAccountNumAmt || tempAccountNumAmt>5 && tempAccountNumAmt!=9)
					{
						JOptionPane.showMessageDialog(null,"The input must be 1-5 or 9",
								"ES&L Bank System",
								JOptionPane.INFORMATION_MESSAGE);
						errorCounter++;
						errorCounter2 = 0;
					}
				}while(1>tempAccountNumAmt || tempAccountNumAmt>5 && tempAccountNumAmt!=9); 
				//end do while loop

				tempAccNumAmt = tempAccountNumAmt;

				//reset error counters
				errorCounter = 0;
				errorCounter2 = 0;

				choice = Integer.parseInt(tempAccNumAmt.toString());

				//-----------------------------------------------------------------------

				first:switch (choice) /* "first" is a named break. Any time "break first;" 
				 					   * is executed (when the user presses 'cancel'), 
				 					   * the program will exit out of the entire switch statement 
				 					   * instead of just the loop, bringing the user back to the 
				 					   * menu.
				 					   */
				{
				case 1: /* Deposits the amount into the specified account if the user  
				 		     * exists. Loops until the input value for the deposit is valid 
				 		     * or until the user cancels or until errorCounter = 5 
				 		     * or until errorCounter2 = 5
				 		     */

					xnam  =
						JOptionPane.showInputDialog
						(null,"Enter the Customer's Name: ",
								"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

					if (xnam == null) //Pressing 'cancel' returns null to the string xnam.
						break first;    // The program will break out to "first" out of the 
					                  // switch and bring the user back to the menu. 

					if (count > 0){ //makes sure the array is not empty
						//calls the findIndex method to find customer
						index = custsArray[0].findIndex(custsArray, xnam, count);          

						if (index != -1) //if customer is found
						{
							System.out.println("");

							do
							{
								if (errorCounter == 5)
									break first;

								String strDepositAmt =
									JOptionPane.showInputDialog
									(null,"Enter the deposit, e.g., 10000.00: ",
											"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

								if(strDepositAmt == null)
									break first;

								strDepositAmt = strDepositAmt.trim();
								sc = new Scanner(strDepositAmt);

								while(!sc.hasNextDouble()){
									errorCounter2++;
									if (errorCounter2 == 5) //if too many invalid inputs
									break first; //break out of switch stmt

									else{

										JOptionPane.showMessageDialog(null,
												"The Deposit Amount must be a positive number",
												"ES&L Bank System",
												JOptionPane.INFORMATION_MESSAGE);
										strDepositAmt =
											JOptionPane.showInputDialog
											(null,"Enter the deposit, e.g., 10000.00: ",
													"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

										if(strDepositAmt == null)
											break first;


										strDepositAmt = strDepositAmt.trim();
										sc = new Scanner(strDepositAmt);
									} //end else loop
								} //end while loop	


								tempDepWithAmt = sc.nextDouble();
								if(MIN > tempDepWithAmt){
									flag = custsArray[index].deposit(tempDepWithAmt);
									errorCounter++;
									errorCounter2 = 0;

								}
								else
									flag = 0;    

							}while(flag == -1); //end do while loop

							custsArray[index].deposit(Double.parseDouble(tempDepWithAmt.
																				toString()));

							String tempName = xnam;

							JOptionPane.showMessageDialog
							(null, xnam + " balance after deposit: "        +
									fmt.format(custsArray[index].getBalance() )
									+ "\n" + tempName
									+ " balance after interest is added: " +
									fmt.format(custsArray[index].addInterest() ),
									"ES&L Bank System",
									JOptionPane.INFORMATION_MESSAGE);


						} // end if stmt

						else
						{
							JOptionPane.showMessageDialog(null,
									"The name must be a Customer!",
									"ES&L Bank System",
									JOptionPane.INFORMATION_MESSAGE);
							System.out.println("");
							System.out.println( xnam + " was not found");

						}  //end else stmt
					} // end if stmt

					break; // end case 1


					//---------------------------------------------------------------

				case 2:   /* Withdraw the amount into the specified account if the user 
				           * exists. Loops until the input value for the withdraw is 
				           * valid or until errorCounter = 5 or until errorCounter2 = 5
				           */

					xnam  =
						JOptionPane.showInputDialog(null,"Enter the Customer's Name: ",
								"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);
					if (xnam == null)
						break first;
					if(count > 0){ //Checks for empty array
						index = custsArray[0].findIndex(custsArray, xnam, count);

						if (index != -1)
						{
							System.out.println("");

							do
							{
								if (errorCounter == 5)
									break first;

								String strWithdrawAmt =
									JOptionPane.showInputDialog
									(null,"Enter the withdrawal, e.g., 10.00:",
											"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

								if(strWithdrawAmt == null)
									break first;

								strWithdrawAmt = strWithdrawAmt.trim();
								sc = new Scanner(strWithdrawAmt);

								while(!sc.hasNextDouble() && errorCounter2 !=5){

									errorCounter2 ++;

									if (errorCounter2 == 5)
										break first;

									else{

										JOptionPane.showMessageDialog(null,
												"The Deposit Amount must be a positive number",
												"ES&L Bank System",
												JOptionPane.INFORMATION_MESSAGE);
										strWithdrawAmt =
											JOptionPane.showInputDialog
											(null,"Enter the withdrawal, e.g., 10.00: ",
													"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

										if(strWithdrawAmt == null)
											break first;

										strWithdrawAmt = strWithdrawAmt.trim();
										sc = new Scanner(strWithdrawAmt);
									} //end else stmt

								} //end while loop

								tempDepWithAmt = sc.nextDouble();

								if(MIN > tempDepWithAmt){
									flag = custsArray[index].withdraw(tempDepWithAmt,FEE);
									errorCounter++;
									errorCounter2 = 0;
								} //end if stmt

								else
									flag = 0;

							}while(MIN > tempDepWithAmt); //end do while loop 

							double withdrawAmt = Double.parseDouble(tempDepWithAmt.toString());
							custsArray[index].withdraw(withdrawAmt,FEE);

							String tempName = xnam;

							JOptionPane.showMessageDialog
							(null, xnam + " balance after withdrawal "        +
									fmt.format(custsArray[index].getBalance() )
									+ "\n" + tempName
									+ " balance after interest is added: " +
									fmt.format(custsArray[index].addInterest() ),
									"ES&L Bank System",
									JOptionPane.INFORMATION_MESSAGE);


						} // end if stmt

						else
						{
							JOptionPane.showMessageDialog(null,"The name must be a Customer!",
									"ES&L Bank System",
									JOptionPane.INFORMATION_MESSAGE);
							System.out.println("");
							System.out.println( xnam + " was not found");
						}  //end else stmt

					} //end if stmt

					break; //end case 2

					//-------------------------------------------------------------

				case 3:    /* Create an account with inputted data. Loops where  
				 						* required in order to get valid input or until 
				 						* errorCounter = 5 or until  errorCounter2 == 5
				            */

					if (count < custsArray.length)
					{
						name = JOptionPane.showInputDialog
						(null,"Enter Customer's name: ",
								"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

						if (name == null)
							break first;

						do  //start of error detection
						{
							if (errorCounter == 5)
								break first;

							String strCustNum = JOptionPane.showInputDialog
							(null,"Enter Customer's Number, e.g., 11111: ",
									"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

							if(strCustNum == null)
								break first;

							strCustNum = strCustNum.trim();
							sc = new Scanner(strCustNum);

							while(!sc.hasNextInt()){
								errorCounter2++;
								if (errorCounter2 == 5)
									break first; 

								else{

									JOptionPane.showMessageDialog(null,
											"The Customer Number must be a number " +
											" using any of the 0 through 9 characters!",
											"ES&L Bank System",
											JOptionPane.INFORMATION_MESSAGE);
									strCustNum = JOptionPane.showInputDialog
									(null,"Enter Customer's Number, e.g., 11111: ",
											"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);
									if(strCustNum == null)
										break first;
									strCustNum = strCustNum.trim();
									sc = new Scanner(strCustNum);

								}//end else stmt

							} //end while loop

							tempAccountNumAmt = sc.nextInt();

							if(MIN > tempAccountNumAmt){
								JOptionPane.showMessageDialog(null,
										"The Customer Number cannot be negative!",
										"ES&L Bank System",
										JOptionPane.INFORMATION_MESSAGE);
								errorCounter++;
								errorCounter2 = 0;
							} //end if stmt

						}while(MIN > tempAccountNumAmt);  //end of do while loop

						tempAccNumAmt = tempAccountNumAmt;
						custNumber = Long.parseLong(tempAccNumAmt.toString());
						errorCounter = 0;
						errorCounter2 = 0;
						//--------------------------------------------------------------------         
						/* makes sure the customers balance is valid, if it isn't it
						 * loops until errorCounter == 5 or until errorCounter2 == 5
						 */

						do  //start of error detection
						{

							if (errorCounter == 5)
								break first; 
								
							String strBalance = JOptionPane.showInputDialog
							(null,"Enter Customer's Balance, e.g., 1000.00: ",
									"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

							if(strBalance == null)
								break first; 

							strBalance = strBalance.trim();
							sc = new Scanner(strBalance);

							while(!sc.hasNextDouble()){

								errorCounter2++;

								if (errorCounter2 == 5)
									break first; 

								else{

									JOptionPane.showMessageDialog(null, 
											"The customer's balance must must be a number " 
											+ " using any of the 0 through 9 characters!",
											"ES&L Bank System",
											JOptionPane.INFORMATION_MESSAGE);
									strBalance = JOptionPane.showInputDialog
									(null,"Enter " +
											"Customer's Balance, e.g., 1000.00: ",
											"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

									if(strBalance == null)
										break first; 

									strBalance = strBalance.trim();
									sc = new Scanner(strBalance);

								}//end else loop

							} //end while loop

							if (errorCounter2 == 5)
								break first; 

							balance = sc.nextDouble();

							if(MIN > balance){
								JOptionPane.showMessageDialog(null,
										"The Customer Number cannot be negative!",
										"ES&L Bank System",
										JOptionPane.INFORMATION_MESSAGE);
								errorCounter++;
								errorCounter2 = 0;
							} //end if

						}while(MIN > balance);  // end of do while loop

						tempAccNumAmt = tempAccountNumAmt;
						phone = JOptionPane.showInputDialog
						(null,"Enter Customer's phone number: ",
								"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

						/* Creates the customer with all 
						 * the information previously recorded
						 */
						custsArray[count++]= new Customer(name, custNumber, balance,
								phone);  

					}// end if stmt

					else
						JOptionPane.showMessageDialog(null,
								"The array is full. No new record added ",
								"ES&L Bank System",
								JOptionPane.INFORMATION_MESSAGE);

					break; //end case 3

					//------------------------------------------------------------------

				case 4:   //View all accounts (displays all customers and their info)

					//sort by name for output
					if (count > 1)
						custsArray[0].nameSort(custsArray,count);

					OutPutText = "";

					for (int scan = 0; scan < count; scan++)
					{
						OutPutText =
							(OutPutText + custsArray[scan].getCustomerName() + " " +
									custsArray[scan].getCustomerNumber() + " "   +
									fmt.format(custsArray[scan].getBalance()) + " "  +
									custsArray[scan].getPhoneNumber()+ "\n");

					}

					JOptionPane.showMessageDialog(null,OutPutText,
							"ES&L Bank System",
							JOptionPane.INFORMATION_MESSAGE);

					break;  // end case 4
					//-----------------------------------------------------------------
				case 5: //Delete a customer (deletes a customer if the name is valid)

					xnam  =
						JOptionPane.showInputDialog(null,"Enter the Customer's Name: ",
								"ES&L Bank System",JOptionPane.QUESTION_MESSAGE);

					if (xnam == null)
						break first;

					if (count > 0){
						index = custsArray[0].findIndex(custsArray, xnam, count);

						if (index != -1) 
						{
							if (count >= 1 && count <= custsArray.length)
							{
								custsArray[index] = custsArray[count-1]; //pack the hole
								count--; //decrement count now that we have one less
								//element

								JOptionPane.showMessageDialog
								(null, xnam + " is deleted. ",
										"ES&L Bank System",
										JOptionPane.INFORMATION_MESSAGE);
							} // end nested if statement

						} // end if stmt

						else
						{
							JOptionPane.showMessageDialog
							(null, xnam + " was not found ",
									"ES&L Bank System",
									JOptionPane.INFORMATION_MESSAGE);

						}  //end else stmt
					}

					break; // end case 5

					//----------------------------------------------------------------       

				default: quit = true;  //set done to true to exit the system

				//--------------------------------------------------------------------

				}// end switch statement
				errorCounter = 0;
				errorCounter2 = 0;

			}// end while loop  for menu





			//----------------------------------------------------------------------
			if (count > 1) //Checks if customer array has more than 1 object to sort.
				custsArray[0].nameSort(custsArray,count);//sort for output by names

			System.out.println("");
			System.out.println(" \t\tSorted updated customer.txt records");

			//prints customers in array
			for (int scan = 0; scan < count; scan++)
				System.out.println (custsArray[scan]);


		}//end try block

		//----------------------------------------------------------------
		//throws exceptions in regards to the error at hand

		catch (FileNotFoundException exception)
		{
			System.out.println ("The file " + file + " was not found");
		}

		catch (IOException exception)
		{
			System.out.println (exception);
		}


		//----------------------------------------------------------

	} //end method runBankTest()

	//------------------------------------------------------------

	/**Calls the class's that run the program
	 * 
	 * @param args command line arguments
	 */
	public static void main (String[] args)
	{
		BankTestDriver bankTest = new BankTestDriver();
		bankTest.runBankTest();
		System.exit(0);
	} // end main method

	//---------------------------------------------------------------

}// end class BankTestDriver

