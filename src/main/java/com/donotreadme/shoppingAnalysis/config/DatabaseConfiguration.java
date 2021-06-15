package com.donotreadme.shoppingAnalysis.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfiguration {

	
	public static Connection connection = null;
	
	/** 
	 * checks if a connection to the database is possible and then make it accessible 
	 * over the static field connection, when no database exist a new will be created
	 * @return returns true when the database was connectable
	 */
	public static boolean connectDataBase() {
		boolean retry = true;
		while(retry) {
			try {
				String driver = "org.apache.derby.jdbc.EmbeddedDriver";
				Class.forName(driver);
				String url = "jdbc:derby:../Shopping/articleDatabase;"; 
				connection = DriverManager.getConnection(url);
				return true;
			} catch (SQLException e) {
				retry = createDatabase("articleDatabase");
				if (retry) {
					System.out.println("Database created");
				} else {
					System.err.println("Couldn't create necessary database");
					retry = false;
				}
			} catch (Exception e) {
				System.err.println("Unhandled excpetion in database connection");
				e.printStackTrace();
				retry = false;
			}
		}		
		return false;
	}
	
	public static void closeConnection() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			System.err.println("Unable to close connection");
			e.printStackTrace();
		}
	}
	
	public static boolean createDatabase(String name) {
		try {
			String driver = "org.apache.derby.jdbc.EmbeddedDriver";
			Class.forName(driver);
			String url = "jdbc:derby:../Shopping/%s; create=true";
			url = String.format(url, name);
			System.out.println(url);
			connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			statement.executeUpdate("CREATE table billposition (id INTEGER primary key, "
					+ "bill_id INTEGER, "
					+ "market VARCHAR(25), "
					+ "date DATE, "
					+ "price DOUBLE, "
					+ "article_id INTEGER)");
			statement.executeUpdate("CREATE table article (id INTEGER primary key, "
					+ "description VARCHAR(50), "
					+ "created DATE, "
					+ "product_id INTEGER)");
			statement.executeUpdate("CREATE table product (id INTEGER primary key, "
					+ "productgroup VARCHAR(25), "
					+ "description VARCHAR(50))");
			return true;
		} catch (Exception e) {
			System.err.println("Unexpected error during the creation of a new database");
			e.printStackTrace();
			return false;
		}
		
	}
}
