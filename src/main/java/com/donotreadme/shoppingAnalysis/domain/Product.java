package com.donotreadme.shoppingAnalysis.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;
import java.util.LinkedList;

import com.donotreadme.shoppingAnalysis.config.DatabaseConfiguration;

public class Product {

	private int id;
	private Category category;
	private String productName;

	public Product(String productName, String category) {
		this.productName = productName;
		this.category = Category.valueOf(category);
		this.setId(this.getIdForCategory(this.category));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category Category) {
		this.category = Category;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public boolean addProduct() {
		boolean result = false;
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement(); //'%1$s'
			Formatter formatter = new Formatter();
			formatter.format("INSERT INTO product VALUES (%d, '%s', '%s')", this.id, this.category, this.productName);
			int i = statement.executeUpdate(formatter.toString());
			if (i > 0) {
				result = true;
			}
			formatter.close();
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private int getIdForCategory(Category category) {
		try {
			DatabaseConfiguration.connectDataBase();
			Statement selectQuery = DatabaseConfiguration.connection.createStatement();
			/*
			 * every category has a reserved number space of 10000, for the unlikely case
			 * you have more than this amount products in one category you will probably
			 * need a better solution (for example remove the number binding to categories by
			 * deleting this whole method) or you use "Sonstiges" for new products
			 */
			int highestNumber = category.getNumber() + 9999;
			String sql = "SELECT max(id) FROM product WHERE id < %s";
			sql = String.format(sql, highestNumber);
			ResultSet resultSet = selectQuery.executeQuery(sql); 
			resultSet.next();
			if (resultSet.getString(1) != null) { //TODO need rework (how does resultSet.wasNull work?)
				return resultSet.getInt(1) + 1;
			} else {
				return category.getNumber();
			}
		} catch (Exception e) {
			System.err.println(e + "\nCan't find ID for Category!");
		}
		return -1;
	}

	/**
	 * @param name of product
	 * @return the id of the product by it's name, when no matching name found it will return -1, 
	 * when there are more than one product with the same name it will return -2
	 */
	public static int getIdByProductName(String name) {
		int result = -1;
		try {
			DatabaseConfiguration.connectDataBase();
			Statement selectQuery = DatabaseConfiguration.connection.createStatement();
			String sql = String.format("SELECT id FROM product WHERE description = '%s'", name);
			ResultSet resultSet = selectQuery.executeQuery(sql);
			while (resultSet.next()) {
				if (result == -1) {
					result = resultSet.getInt("id");
				} else {
					result = -2;
				}
			}
		} catch (SQLException e) {
			System.err.println("No id found for product name: " + name);
		}
		return result;
	}

	public static LinkedList<String> getAllProductNames() {
		LinkedList<String> result = new LinkedList<String>();
		try {
			DatabaseConfiguration.connectDataBase();
			Statement selectQuery = DatabaseConfiguration.connection.createStatement();
			String sql = "SELECT * FROM product";
			ResultSet resultSet = selectQuery.executeQuery(sql);
			while (resultSet.next()) {
				result.add(resultSet.getString("description"));
			}
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			System.err.println("Unable to load entries from database 'product'");
			e.printStackTrace();
		}
		return result;
	}

	public static boolean deleteProductById(int id) {
		Boolean result = false;
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = "DELETE FROM product WHERE id = " + id;
			int i = statement.executeUpdate(sql);
			if (i > 0) {
				result = true;
				System.out.println("Deleted id " + id + " from 'product' database");
			}
		} catch (Exception e) {
			System.err.println("Unable to delete entry from database 'product'");
			e.printStackTrace();
		}
		return result;
	}

}
