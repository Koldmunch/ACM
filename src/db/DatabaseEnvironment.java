package db;
import java.sql.SQLException;
import db.CalendarDatabase;

public class DatabaseEnvironment {
	
	private CalendarDatabase db;
	private final String dbName = "calendardb";
	
	public DatabaseEnvironment(){
		
		db = db.SingletonInstance();
		db.setSelectedDatabase(dbName);
		
		// check if database exists
		if(!db.testConnection()){
			System.err.println("No database " + "'" + dbName + "'" + " detected.");
			// if not, create new database environment
			try{
				db.setSelectedDatabase(null);
				createDatabase();
				db.setSelectedDatabase(dbName);
				
				// createTables();
				// createRows();
				System.out.println("New database environment has been created.");
			} catch(Exception ex){
				ex.printStackTrace();
			}
			
		} else {
			// if database already exists
			System.out.println("Database " + dbName + " exists.");
		}
	}
	
	// method for creating new database
	private void createDatabase() throws SQLException{
		String sqlDrop = "DROP DATABASE IF EXISTS " + dbName + ";";
		String sqlCreate = "CREATE DATABASE " + dbName;
		
		db.doUpdate(sqlDrop);
		db.doUpdate(sqlCreate);
	}
	
	// method for creating needed tables
	private void createTables() throws SQLException{
		String sqlCreateUserProfilesTable = "CREATE TABLE userprofiles" + "(username varchar(255), password varchar(255), balance double, usertype varchar(255), UID int NOT NULL UNIQUE AUTO_INCREMENT);";
		db.doUpdate(sqlCreateUserProfilesTable);
		String sqlCreateTransactionsTable = "CREATE TABLE transactions" + "(transaction_type varchar(255), amount double, UID int, recipientUID int, timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);";
		db.doUpdate(sqlCreateTransactionsTable);
		String sqlCreateExchangeRateTable = "CREATE TABLE exchangerate" + "(rate double);";
		db.doUpdate(sqlCreateExchangeRateTable);
	}
	
	// method for creating default database rows
	private void createRows() throws SQLException{
		// add default users
		String sqlCreateUsers = "INSERT INTO userprofiles (username, password, balance, usertype) " + "VALUES ('peter@cbs.dk', 'password1234', 1.1, 'standarduser'),"
				+ "('user@cbs.dk', 'mypassword', 4.6, 'standarduser'), ('88888888', '1234abcd', 9.2, 'administrator');";
		db.doUpdate(sqlCreateUsers);
		
		String sqlCreateExchangeRate = "INSERT INTO exchangerate (rate) " + "VALUES (2662.4);"; // default exchange rate
		db.doUpdate(sqlCreateExchangeRate);
	}

}
