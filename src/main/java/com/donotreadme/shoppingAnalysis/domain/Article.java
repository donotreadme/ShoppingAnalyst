package com.donotreadme.shoppingAnalysis.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

import com.donotreadme.shoppingAnalysis.config.DatabaseConfiguration;

public class Article {

	private int id = -1;
	private String description;
	private int productId;

	private Date date = null;
	private String productName = null;
	private String productCategory = null;

	public Article(String description, String productName, String productCategory) {
		this.description = description;
		this.productName = productName;
		this.productCategory = productCategory;
		int i = Product.getIdByProductName(productName);
		if (i >= 0) {
			this.productId = i;
		} else if (i == -1) {
			Product newProduct = new Product(productName, productCategory);
			newProduct.addProduct();
			this.productId = newProduct.getId();
		} else if (i == -2) {
			System.err.println("This product already exist several times in the database");
		} else {
			System.err.println("Unknown error at article database! Not expected " + i);
		}
	}

	public Article(int articleId, String articleDescription, Date articleDate, int productId, String productName,
			String productCategory) {
		this.id = articleId;
		this.description = articleDescription;
		this.date = articleDate;
		this.productId = productId;
		this.productName = productName;
		this.productCategory = productCategory;
	}

	public boolean addArticle() {
		boolean result = false;
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = "SELECT max(id) FROM article";
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.next();
			this.id = resultSet.getInt(1) + 1;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Formatter formatter = new Formatter();
			formatter.format("INSERT INTO article VALUES (%d, '%s', '%s', %d)", this.id, this.description,
					format.format(new Date()), this.productId);
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

	public static boolean checkIfArticleExist(String description) {
		boolean result = false;
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = String.format("SELECT * FROM article WHERE description = '%s'", description);
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				result = true;
			}
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean deleteArticleByDescription(String description) {
		boolean result = false;
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = String.format("DELETE FROM article WHERE description = '%s'", description);
			if (statement.executeUpdate(sql) > 0) {
				result = true;
			}
			DatabaseConfiguration.closeConnection();
		} catch (SQLException e) {
			System.err.println("Couldn't delete article: " + description);
			e.printStackTrace();
		}

		return result;
	}

	public static Article getJoinByDescription(String description) {
		Article result = null;
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = String.format("SELECT articleValues.id, articleValues.description, "
					+ "articleValues.created, productValues.id, productValues.description, "
					+ "productValues.productgroup FROM product AS productValues "
					+ "INNER JOIN article AS articleValues ON productValues.id = articleValues.product_id "
					+ "AND articleValues.description = '%s'", description);
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				result = new Article(resultSet.getInt(1), resultSet.getString(2), resultSet.getDate(3),
						resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6));
			}
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @return the ID of the article or -1 when it's not written in database till now
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

}
