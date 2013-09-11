import java.sql.*;
import java.io.*;
import java.util.*;

/**
   The Database class represents a Database object that connects to a Microsoft Access Database.
   The object can create new tables or edit existing tables in the database,
   as well as retrieve information from them.

    @author Andy Nguyen
 */
class Database
{
	//private String driverName;
	private String urlstr;
	Connection conn;
	Statement stat;


	//---------------------------------------------------------
	//        constructor
	//---------------------------------------------------------
	/**
	 * Contructor. Establishes the connection to the databse after creating the object.
	 */
	public Database (String driver, String url) throws SQLException
	{
		//this.driverName = driver;
		this.urlstr = url;


		//try the following
		try
		{
			// load the driver class
			Class.forName(driver);
		} // end try block

		catch (ClassNotFoundException ex)
		{
			System.out.println("Can't find the driver class " +
					driver + ", check the classpath");
		}//end catch block

	    getConnection();
	}// end constructor


	//---------------------------------------------------
	//method(s)
	//----------------------------------------------------

/**
 * Establishes a connection with the Microsoft Access database.
 */
	public void getConnection()
	{
		try{
			// open the connection to the database
			conn = DriverManager.getConnection(urlstr);

			// Tell the user the connection was opened
			System.out.println("Connection established");

		}

		catch (SQLException ex)
		{
			while (ex != null)
			{
				System.out.println("Trouble with the connection");
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}


	} // end getConnected

/**
 * Creates a table in the Access database.
 * @param tName table name
 * @param fields fields
 * @throws SQLException
 */
	public void createTable(String tName, String fields) throws SQLException
	{
		//Create the statement
		stat = conn.createStatement();

		String cmd;
		try
		{
			cmd = "DROP TABLE " + tName;
			stat.execute(cmd);
			System.out.println("TABLE Name --> " + tName);
		}
		catch ( SQLException e)
		{
			System.out.println ( "Table Person did not exist-->"+
					"continue to create" + tName + "table");
		}

		stat.execute("CREATE TABLE " + tName + " (" + fields + ")");
	}//end createTable

	/**
	 * Inserts data into the table specified.
	 * @param tName table name
	 * @param fields fields
	 * @param line data
	 * @throws SQLException
	 */
	public void insertDataToTable(String tName, String fields, String line) throws SQLException
	{
		//inserted only the first record
		stat = conn.createStatement();

		System.out.println("----Inserting records to table----");
		try
		{
			System.out.print("in insertDataToTable -->");
			stat.execute("INSERT INTO " + tName + "(" + fields
					+ ") VALUES(" + line + ")");
			System.out.println("java.SQL.Exception: No ResultSet was produced");
			System.out.println("Data was inserted correctly!!!!\n");

		}
		catch (SQLException e)
		{
			System.out.println(e.toString());
			System.out.println( "Error in Inserting data!!!!!\n");
		}

	}  // end insertDataToTable

	/**
	  Uses Meta Data to display the information about each table. Ex. column
	  name, column size, column type.
	 */
	public void showTablesInfo(String tableName)
	{
		try
		{
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM " + tableName);
			ResultSetMetaData rsmd = rs.getMetaData();

			System.out.println("number of columns in " + tableName + " table is "
					+rsmd.getColumnCount());
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				String columnName = rsmd.getColumnLabel(i);
				int columnWidth  = rsmd.getColumnDisplaySize(i);


				System.out.print( "   column Name  " + " " + columnName);
				System.out.println( " \t column Width  " + " " + columnWidth);

			}
			System.out.println();

			ArrayList<String> fields = new ArrayList<String>(); //stores row data

			fields.add(Integer.toString(rsmd.getColumnCount())); //store column count at index 0

			while (rs.next())
			{
				for(int i=1;i <= rsmd.getColumnCount();i++)
					fields.add(rs.getString(i));
			}
			//display the records of the Person table
			displayTableData(tableName, fields);
			rs.close();

		}
		catch(SQLException e)
		{
			System.out.println("in showTableInfo --> "+e.toString());
		}

	}


	/**
	      Displays the records of the table.
	 */
	public void displayTableData(String tableName, ArrayList<String> fields) throws SQLException
	{

			System.out.println("\n----displaying the records for " + tableName +" Table ----"
								+ "\nPrinting using ArrayList");

			int column = Integer.parseInt(fields.get(0)); // gets column count which
														// indicates when a new line is needed
			for(int i=1; i < fields.size(); i++)
			{
				if(i%column == 1) //end of row data, go to new line
					System.out.println();

				System.out.print(fields.get(i) + " ");

			}//end for loop
			System.out.println("\n");

	}// end displayTableData

	/**
	 * Creates a query to retrieve specific information from the databse.
	 * @param query query
	 * @param numberOfFields number of fields
	 * @throws SQLException
	 */
	public void createQuery(String query, int numberOfFields) throws SQLException
	{
		try
		{

			ResultSet result = stat.executeQuery(query);

			while(result.next())
			{
				for(int i = 1; i <= numberOfFields; i++)
				{
					System.out.print(result.getString(i) + " ");
				}

				System.out.println();
			}

			System.out.println();
		}

		catch(SQLException e)
		{
			System.out.println("in showTableInfo --> "+e.toString());
		}
	}// end createQuery

	/**
	 * Closes the connection to the database. Also closes statement object.
	 * @throws SQLException
	 * @throws IOException
	 */
	public void close() throws SQLException, IOException
	{
		//close the connection
		conn.close();
		stat.close();
		//tell the user the connection was closed
		System.out.println("The connection was closed.");
	}// end close method



	//---------------------------------------------------------



}// end class Database

