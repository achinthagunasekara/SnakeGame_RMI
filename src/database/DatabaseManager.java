/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package database;

import helpers.ConfigFileReader;
import java.sql.*;
import java.util.Calendar;

public class DatabaseManager {

	private static DatabaseManager instance;
	private Connection connection;
	private String user = ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("DB_USER");
	private String pass = ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("DB_PASS");
	private String url = ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("DB_URL");
	
	
	public static DatabaseManager getDatabaseManagerInstance() {
		
		if(instance == null) {
			
			instance = new DatabaseManager();
		}
		
		return instance;
	}

	private DatabaseManager() {
		
		setup();
	}
	
	private Connection getDbConnection() throws ClassNotFoundException, SQLException {
		
		//get a connection to the Oracle database server
		Class.forName("oracle.jdbc.driver.OracleDriver");
		return DriverManager.getConnection(url, user, pass);
	}
	
	/*
	 * This method will check if players and scores tables exists
	 * if not they will be created
	 */
	private void setup() {
	
		try {
			
			connection = getDbConnection();
			Statement statement = connection.createStatement();

			String testQuery;
			PreparedStatement preparedStatement;
			
			try {
				
				testQuery = "SELECT * FROM players";
				preparedStatement = connection.prepareStatement(testQuery);
				preparedStatement.executeQuery();
			}
			catch(Exception SQLException) {
				
				String query = "CREATE TABLE players " +
						"(" +
						"first_name VARCHAR(255)," +
						"last_name VARCHAR(255)," +
						"address VARCHAR(255)," +
						"phone_number VARCHAR(255)," +
						"user_name VARCHAR(255) PRIMARY KEY, " +
						"password VARCHAR(255)" +
						")";
		
				statement.executeUpdate(query);
			}
			
			try {
				
				testQuery = "SELECT * FROM scores";
				preparedStatement = connection.prepareStatement(testQuery);
				preparedStatement.executeQuery();
			}
			catch(Exception SQLException) {
				
				String query = "CREATE TABLE scores " +
						"(" +
						"user_name VARCHAR(255)," +
						"date_recoreded VARCHAR(255)," +
						"score NUMBER" +
						")";
		
				statement.executeUpdate(query);
			}
			
			connection.close();	
		}
		catch(Exception ex) {
			
			System.out.println("Error setting up the database");
			ex.printStackTrace();
			System.exit(0);	
		}
	}
	
	public void addPlayerScore(String userName, int score) {
		
		try {
		
			connection = getDbConnection();
			Timestamp ts = new Timestamp(Calendar.getInstance().getTimeInMillis());

			String query = "INSERT INTO scores " +
						"(user_name, date_recoreded, score)" +
						"VALUES(?, ?, ?)";
		
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, ts.toString());
			preparedStatement.setInt(3, score);
			
			preparedStatement.execute();
			connection.close();
		}
		catch(Exception ex) {
			
			System.out.println("Error updateing score");
			ex.printStackTrace();
		}
	}
	
	public boolean authenticatePlayer(String user, String pass) {
		
		try {
			
			connection = getDbConnection();
			
			String query = "SELECT COUNT(user_name) FROM players " +
							"WHERE user_name = ? AND password = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);
			
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			
			if(rs.getInt(1) > 0) {
				
				return true;
			}

			connection.close();
		}
		catch(Exception ex) {
			
			System.out.println("Error authenticating player");
			ex.printStackTrace();
			System.exit(0);	
		}
		
		return false;
	}
	
	public void registerNewPlayer(String firstName, String lastName, String address, String phone, String userName, String password) throws Exception {

			
		connection = getDbConnection();

		String query = "INSERT INTO players " +
					"(first_name, last_name, address, phone_number, user_name, password)" +
					"VALUES(?, ?, ?, ?, ?, ?)";
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, firstName);
		preparedStatement.setString(2, lastName);
		preparedStatement.setString(3, address);
		preparedStatement.setString(4, phone);
		preparedStatement.setString(5, userName);
		preparedStatement.setString(6, password);

		preparedStatement.execute();
		connection.close();
	}
	
	public String[][] getPlayerData(String user) {
		
		try {
			
			connection = getDbConnection();
			
			String query = "SELECT * FROM scores " +
							"WHERE user_name = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			int resultCount = 0;
			
			while (rs.next()) {
				
				resultCount++;
			}
			
			rs = preparedStatement.executeQuery();
			
			String[][] results = new String[resultCount][2];
			int i = 0;
			
			while(rs.next()) {
				
				results[i][0] = rs.getString(2);
				results[i][1] = rs.getString(3);
				i++;
			}
			connection.close();
			return results;
		}
		catch(Exception ex) {
			
			System.out.println("Error getting player's score data");
			ex.printStackTrace();
			System.exit(0);	
		}
		
		return null;
	}
	
	public String[] getPlayerDetails(String user) {
		
		try {
			
			connection = getDbConnection();
			
			String query = "SELECT * FROM players " +
							"WHERE user_name = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			String[] results = new String[6];
			
			while(rs.next()) {
				
				results[0] = rs.getString(1);
				results[1] = rs.getString(2);
				results[2] = rs.getString(3);
				results[3] = rs.getString(4);
				results[4] = rs.getString(5);
				results[5] = rs.getString(6);
			}
			connection.close();
			return results;
		}
		catch(Exception ex) {
			
			System.out.println("Error getting player's score data");
			ex.printStackTrace();
			System.exit(0);	
		}
		
		return null;
	}
}
