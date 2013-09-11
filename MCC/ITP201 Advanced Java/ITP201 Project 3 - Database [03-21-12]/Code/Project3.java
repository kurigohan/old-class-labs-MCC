import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
The Project3 class creates a Database object for testing. It contains a method
that reads a file with SQL formatted data that will be sent as queries to the Database object.
Project3 calls methods from the Database class to modify and display information in the Access database.
 @author Andy Nguyen
*/

public class Project3 {

	/**
	 * Main method. Runs the program.
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 */

	public static void main(String[] args) throws SQLException, IOException
	{
		//Create the database manager for an Access database
		// for database TestDB

		Database testDB =
			new Database("sun.jdbc.odbc.JdbcOdbcDriver",
			"jdbc:odbc:Project3");
		// get a Connection

		String file1 = "table1.txt",
		file2 = "table2.txt";

		String fieldNames = "Name, Id, City, State";
		String tableName = "Person2";
		String fieldValues = "Name VARCHAR(30), Id INTEGER, City VARCHAR(20), State CHAR(2)";
		testDB.createTable(tableName, fieldValues);

		readFile(testDB, tableName, fieldNames, file1);

		file1 = "table2.txt";
		tableName = "Classes";
		fieldNames = "Id, Course, Grade";
		testDB.createTable(tableName, "Id INTEGER, Course VARCHAR(20), Grade INTEGER");

		readFile(testDB, tableName, fieldNames, file2);

		//Display Table info
		tableName = "Person2";
		testDB.showTablesInfo(tableName);

		tableName = "Classes";
		testDB.showTablesInfo(tableName);

		//Queries
		System.out.println("---- Query 1 - CIS 225 ----\n");
		String query = "SELECT * FROM Classes "+
		"WHERE Course= 'CIS 225' ORDER BY Id ASC";
		testDB.createQuery(query, 2);

		System.out.println("---- Query 2 - People in NY ----\n");
		query = "SELECT Name, State FROM Person2 "+
		"WHERE State= 'NY' ORDER BY Name ASC";
		testDB.createQuery(query, 2);

		System.out.println("---- Query 3 - Records from Person2 and Classes table ----\n");
		query = "SELECT Person2.Name, Classes.Id, Classes.Course, Classes.Grade FROM Person2, Classes "+
		"WHERE (Course= 'CIS 225' AND Grade> 70) AND Person2.Id = Classes.Id ORDER BY Person2.Id ASC";
		testDB.createQuery(query, 4);

		testDB.close();
	}

	/**
	 * Reads a SQL formatted data from a text file and inserts it into the table.
	 * @param db database object
	 * @param tableN table name
	 * @param fieldN field name
	 * @param fileN file name
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void readFile(Database db, String tableN, String fieldN, String fileN)
													throws IOException, SQLException
	{
		FileReader fr = new FileReader(fileN);
		BufferedReader br = new BufferedReader(fr);

		String s;
		while((s = br.readLine()) != null)
		{
			System.out.println("(" + s + ")");
			db.insertDataToTable(tableN, fieldN, s);
		}

		fr.close();
	}

}
