import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.text.NumberFormat;

public class FutureInvestment2 extends JFrame{

	private JLabel investLabel1 = new JLabel("Investment Amount");
	private JTextField investJtfArea1 = new JTextField();

	private JLabel yearLabel2 = new JLabel("Years");
	private JTextField yearJtfArea2 = new JTextField();

	private JLabel interestLabel3 = new JLabel("Annual Interest Rate");
	private JTextField interestJtfArea3 = new JTextField();

	private JLabel valueLabel4 = new JLabel("Future value");
	private JTextField valueJtfArea4 = new JTextField();{
			valueJtfArea4.setEditable(false);
	}
	private JButton btnCalc = new JButton("Calculate");

	private JButton btnClear = new JButton("Clear");

	// Menu items for the "File" and "Help" menu
	private JMenuItem jmiCalc;
	private JMenuItem jmiExit;
	private JMenuItem jmiAbout;

	private NumberFormat fmt = NumberFormat.getCurrencyInstance();
	private int errorCounter = 0; //tracks number of unsuccessful inputs
	


	public FutureInvestment2()
	{

		//set the GridLayout manager for the frame

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(4,2));// two rows and two columns
		panel1.add(investLabel1);
		panel1.add(investJtfArea1);

		panel1.add(yearLabel2);
		panel1.add(yearJtfArea2);

		panel1.add(interestLabel3);
		panel1.add(interestJtfArea3);

		panel1.add(valueLabel4);
		panel1.add(valueJtfArea4);

		//------------------------------------------
		// Create panel 2 to hold the buttons
		//--------------------------------------------

		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout() );

		panel2.add(btnCalc);
		panel2.add(btnClear);


		// Add the panels and the buttons to the frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel1, BorderLayout.CENTER);
		getContentPane().add(panel2, BorderLayout.SOUTH);


		//------------------------------------------
		//Register the Listeners
		//------------------------------------------

		ActionListener calcListener = new btnCalculateListener();
		btnCalc.addActionListener(calcListener);

		ActionListener clearListener = new btnClearListener();
		btnClear.addActionListener(clearListener);
		
		ActionListener aboutListener = new aboutMenuListener();
		ActionListener exitListener = new exitMenuListener();

		//Create a menu bar and associate it with a frame by
		//using the setJMenuBar method
		JMenuBar jmenuBar = new JMenuBar();

		//Set menu bar to the frame
		setJMenuBar(jmenuBar);

		//Create two menus labeled File and Help
		// They appear below the Title bar

		JMenu operationMenu = new JMenu("Operation");

		JMenu helpMenu = new JMenu("Help");

		jmenuBar.add(operationMenu);
		jmenuBar.add(helpMenu);


		//------------------------------------------------------------------------
		//Create menu items (jmiNew and jmiOpen) and add them to the
		//File menu. 

		operationMenu.add(jmiCalc = new JMenuItem("Calculate"));
		jmiCalc.addActionListener(calcListener);

		operationMenu.addSeparator();

		//Add menu items with mnemonic to menu fileMenu
		operationMenu.add(jmiExit = new JMenuItem("Exit"));
		jmiExit.addActionListener(exitListener);

		helpMenu.add(jmiAbout = new JMenuItem("About"));
		jmiAbout.addActionListener(aboutListener);

	}// end default constructor

	private class aboutMenuListener implements ActionListener 
	{         
		public void actionPerformed(ActionEvent e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Compute Future Investment Value",
				"About This Program", JOptionPane.INFORMATION_MESSAGE);
			System.out.println("About message displayed");
		}
	}   
	
	private class exitMenuListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Program close.");
			System.exit(0);
		} //end actionPerformed
		
	}
	

	//--------------------------------------------
	//Handle the Listeners - inner classes
	//--------------------------------------------

	private class btnClearListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Clear button pressed.");
			//if(e.getSource() == btnClear) //Clear
			//{
				investJtfArea1.setText("");
				yearJtfArea2.setText("");
				interestJtfArea3.setText("");
				valueJtfArea4.setText("");
				
				System.out.println("All text fields cleared.");
				errorCounter = 0; //reset counter after clear
			//}

		}//end Handle the Listeners

	}// end inner class btnClearListener



	private class btnCalculateListener implements ActionListener
	{
		
		public boolean isValid()
		{ //check for valid input
			Scanner tempTextValue1 = new Scanner(investJtfArea1.getText().trim());
			Scanner tempTextValue2 = new Scanner(yearJtfArea2.getText().trim());
			Scanner tempTextValue3 = new Scanner(interestJtfArea3.getText().trim());
			
			Boolean valid = true;
			String errorFields = "The following fields have invalid data: \n";
			if(tempTextValue1.hasNextDouble() == false || tempTextValue1.nextDouble() < 0)
			{
				errorFields += "Investment Amount\n";
				investJtfArea1.setText("");
				valid = false;
				
				System.out.println("Invalid input in text field 1.");
				System.out.println("Text field 1 cleared.");
			}
			
			if(tempTextValue2.hasNextDouble() == false || tempTextValue2.nextDouble() < 0)
			{
				errorFields += "Years\n";
				yearJtfArea2.setText("");
				valid = false;
				
				System.out.println("Invalid input in text field 2.");
				System.out.println("Text field 2 cleared.");
			}
			
			if(tempTextValue3.hasNextDouble() == false || tempTextValue3.nextDouble() < 0)
			{
				errorFields += "Annual Interest Rate\n";
				interestJtfArea3.setText("");
				valid = false;
				
				System.out.println("Invalid input in text field 3.");
				System.out.println("Text field 3 cleared.");
			}
			
			if(valid == false)
			{
				JOptionPane.showMessageDialog(new JFrame(), errorFields,
					"Error: Invalid Data", JOptionPane.ERROR_MESSAGE);
			}
			
			return valid;
			
		}//end isValid
		
		public double futureValue(double investAmt, double yrs, double annualIntRate)
		{
			return investAmt * Math.pow(1+(annualIntRate/(12*100)), yrs * 12);
		}
		
		public void actionPerformed(ActionEvent e)
		{	
			System.out.println("Calculate button pressed.");
			if(isValid())
			{
				errorCounter = 0; // reset error counter after successful calculation
				valueJtfArea4.setText(fmt.format(futureValue(new Scanner(investJtfArea1.getText().
					trim()).nextDouble(), 
					new Scanner(yearJtfArea2.getText().trim()).nextDouble(), 
					new Scanner(interestJtfArea3.getText().trim()).nextDouble())));
				System.out.println("Successful calculation.");
			}
			else
			{
				errorCounter += 1;
				System.out.println("Error counter = " + errorCounter);
			}
			
			if(errorCounter == 4) //close program if too many invalid inputs
			{
				JOptionPane.showMessageDialog(new JFrame(), "Too many invalid inputs. The program is closing.",
						"Error: Invalid Data", JOptionPane.ERROR_MESSAGE);
				System.out.println("Error limit reached. Program close.");
				System.exit(0);
			}
		
			
		}
	}// end inner class btnCalculateListener

	
	//----------------------------------------
	//Main method
	//----------------------------------------

	public static void main(String[] args) {
		FutureInvestment2 frame = new FutureInvestment2();
		frame.setTitle("Future Investment Value");
		frame.setLocationRelativeTo(null);
		// center the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);
		frame.setVisible(true);

	} //end main method
	
}//end FutureInvestment2
