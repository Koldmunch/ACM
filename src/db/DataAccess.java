package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import db.CalendarDatabase;

public class DataAccess {
	// all sql-statements executed here
	public int getUID(String userName, String userPass) {
		
    	String username = userName;
    	String userpass = userPass;
		// Ask database if user exists
		String sqlCheckUser = "SELECT UID FROM userprofiles WHERE username = ? AND password = ?;";
		try {
		PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlCheckUser);
		prest.setString(1, username);
		prest.setString(2, userpass);
			ResultSet rs = prest.executeQuery();
			if(rs.next()){
				// login successful
				// set userID of current user
				return rs.getInt(1);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return -1;
	}
	
	public double getUserBalance(int UID){
		double balance = 0;
		String sqlBalance = "SELECT balance FROM userprofiles WHERE UID = ?;";
		try {
			// get balance of current user
			PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlBalance);
			prest.setInt(1, UID);
			ResultSet rs = prest.executeQuery();
			if(rs.next()){
				balance = rs.getDouble(1);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return balance;
	}
	
	public void AddUser(String userName, String userPass, String userType) throws SQLException{
		String sqlAddUser = "INSERT INTO userprofiles (username, password, balance, usertype) " + 
				"VALUES (?, ?, 1.0, ?);";
		PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlAddUser);
		prest.setString(1, userName);
		prest.setString(2, userPass);
		prest.setString(3, userType);
		prest.executeUpdate();
	}
	
	public String getUserType(int UID){
		String userType = "";
		String sqlUserType = "SELECT usertype FROM userprofiles WHERE UID = ?;";
		try {
			PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlUserType);
			prest.setInt(1, UID);
			ResultSet rs = prest.executeQuery();
			if(rs.next()){
				userType = rs.getString(1);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return userType;
	}
	
	public int userExists(String username){
		int UID = 0;
		String sqlUserID = "SELECT UID FROM userprofiles WHERE username = ?;";
		try {
			PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlUserID);
			prest.setString(1, username);
			ResultSet rs = prest.executeQuery();
			if(rs.next()){
				UID = rs.getInt(1);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return UID;
	}
	
	public double getCurrentExchangeRate(){
		double rate = 0;
		String sqlExchangeRate = "SELECT rate FROM exchangerate;";
		try {
			ResultSet rs = CalendarDatabase.SingletonInstance().doQuery(sqlExchangeRate);
			if(rs.next()){
				rate = rs.getDouble(1);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return rate;
	}
	
	public void setCurrentExchangeRate(double Rate) throws SQLException{
		String sqlExchangeRate = "UPDATE exchangerate SET rate = ?;";
		PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlExchangeRate);
		prest.setDouble(1, Rate);
		prest.executeUpdate();
	}
	
	public void CreateTransaction(String type, double amount, int UID, int recipientUID) throws SQLException{
		String sqlCreateTransaction = "INSERT INTO transactions (transaction_type, amount, UID, recipientUID)"
				+ "VALUES (?, ?, ?, ?)";
		PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlCreateTransaction);
		prest.setString(1, type);
		prest.setDouble(2, amount);
		prest.setInt(3, UID);
		prest.setInt(4, recipientUID);
		prest.executeUpdate();
	}
	
	public String getTransactions(int UID) throws SQLException{
		String sqlGetTransactions = "SELECT * FROM transactions WHERE UID = ? ORDER BY timestamp desc;";
		String result = "";
		try {
			PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlGetTransactions);
			prest.setInt(1, UID);
			ResultSet rs = prest.executeQuery();
			while(rs.next()){
				result += rs.getString(5) + "\t" + rs.getString(1) + "\t" + rs.getDouble(2) + "\n";
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return result;
	}
	
	public void setBalance(double amount, int UID) throws SQLException{
		String sqlSetBalance = "UPDATE userprofiles SET balance = ? WHERE UID = ?;";
		PreparedStatement prest = CalendarDatabase.SingletonInstance().CreatePreparedStatement(sqlSetBalance);
		prest.setDouble(1, amount);
		prest.setInt(2, UID);
		prest.executeUpdate();
	}
	
}
