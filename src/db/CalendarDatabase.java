package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


public class CalendarDatabase {
	
	private static CalendarDatabase _instance;
    private static String sqlUrl = "jdbc:mysql://localhost:3306/";
    private static String sqlUser = "root";
    private static String sqlPass = "";
    private Statement stmt;
    private ResultSet rs;
    private Connection conn = null;
    
    private CalendarDatabase(){
    	// private constructor to avoid instances from other classes
    }
   
    // create SingletonInstance to ensure only one instance of the database will run
    public static CalendarDatabase SingletonInstance(){
 	   if(_instance == null)
 		   _instance = new CalendarDatabase();
 	   return _instance;
    }
   
    
    // sets variable setSelectedDatabase, which is needed for us to work with the mySql server
    public static void setSelectedDatabase(String db){
    	sqlUrl = "jdbc:mysql://localhost:3306/";
    	
    	// check whether a database name has been entered
    	if(db != null && db.length() > 0){
    		sqlUrl += db;
    	}
    }
    
    // check if connection to database can be established
    public boolean testConnection(){
    	try {
    		getConnection();
    		if(conn.isValid(5000)){
    			return true;
    		}
    	} catch (SQLException e) {
    		// display error messages if connection cannot be established
    		System.out.println("Cannot connect to database");
    		System.out.println(e.getMessage());
    	}
    	return false;
    }
    
    // executes a query in database
    public ResultSet doQuery(String query) throws SQLException{
    	getConnection();
    	try {
    		stmt = conn.createStatement();
    		rs = stmt.executeQuery(query);
    	} catch(SQLException ex) {
    		System.out.println(ex);
    		throw ex;
    	}
    	
    	return rs;
    }
    
    public PreparedStatement CreatePreparedStatement(String sql) throws SQLException{
    	getConnection();
    	return conn.prepareStatement(sql);
    }
    
    public int doUpdate(String update) throws SQLException {
    	getConnection();
    	int temp = 0;
    	
    	try {
    		stmt = conn.createStatement();
    		temp = stmt.executeUpdate(update);
    	} catch(SQLException ex){
    		ex.printStackTrace();
    		throw ex;
    		
    	}
    	
    	// close connections to database after update
    	finally {
    		if(stmt!=null){
    			try {
    				stmt.close();
    			} catch(SQLException sqlEx){
    				stmt = null;
    			}
    		}
    	}
    	return temp;
    }
    
    
    private void getConnection() throws SQLException {
    	conn = DriverManager.getConnection(sqlUrl, sqlUser,sqlPass);
    }

}
