package com.donotreadme.shoppingAnalysis.domain;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import com.donotreadme.shoppingAnalysis.config.DatabaseConfiguration;

public class Bill {

	private int id;
	private static int billId = 0;
	private Date date;
	private String market;
	private int articleId;
	private double price;

	
	/**
	 * create a new bill_position and save it to the database 
	 * (changeBillId should be used before) 
	 * @param date: the date of the purchase 
	 * @param market
	 * @param articleDescription: scanned text of the bill_position
	 * @param price: the price of the article
	 */
	public Bill(Date date, String market, String articleDescription, double price) {
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = "SELECT max(id) FROM billposition";
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.next();
			this.id = resultSet.getInt(1) + 1;
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			System.err.println("Couldn't find id for billposition");
			e.printStackTrace();
		}
		this.date = date;
		this.market = market;
		try {
			this.articleId = Article.getJoinByDescription(articleDescription).getId();
		} catch (NullPointerException e) {
			System.err.println("Article doesn't exist, please create it first");
			throw e;
		}		
		this.price = price;
		this.addBillposition();		
	}

	private Bill(int id, int bill_Id, Date date, String market, int articleId, double price) {
		this.id = id;
		billId = bill_Id;
		this.date = date;
		this.market = market;
		this.articleId = articleId;
		this.price = price;
	}
	
	private void addBillposition() {
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			String sql = String.format(Locale.ENGLISH, "INSERT INTO billposition VALUES (%d, %d, '%s', '%s', %f, %d)", 
					this.id, billId, this.market, dateFormat.format(this.date), this.price, this.articleId);
			statement.executeUpdate(sql);
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			System.err.println("Unable to add position id " + this.id);
			e.printStackTrace();
		}
	}

	/**
	 * before positions of a new bill get saved the billId have to be changed
	 */
	public static void changeBillId() {
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = "SELECT max(bill_id) FROM billposition";
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.next();
			billId = resultSet.getInt(1) + 1;
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			System.err.println("Couldn't change the bill_id");
			e.printStackTrace();
		}
	}
	public static LinkedList<Bill> getAllPositionsForBillId(int billId){
		LinkedList<Bill> result = null;
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = String.format("SELECT id, bill_id, date, market, article_id, price "
					+ "FROM billposition WHERE bill_id = %d", billId);
			ResultSet resultSet = statement.executeQuery(sql);
			result = new LinkedList<Bill>();
			while (resultSet.next()) {
				result.add(new Bill(resultSet.getInt(1), resultSet.getInt(2), resultSet.getDate(3), 
						resultSet.getString(4), resultSet.getInt(5), resultSet.getDouble(6)));
			}
			DatabaseConfiguration.closeConnection();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static LinkedList<Bill> getAllPositionsForSpanOfTime(Date start, Date end){
		LinkedList<Bill> result = new LinkedList<Bill>();
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String sql = String.format("SELECT * FROM billposition WHERE "
					+ "date BETWEEN DATE('%s') and DATE('%s')", 
					dateFormatter.format(start), dateFormatter.format(end));
			ResultSet resultSet = statement.executeQuery(sql);			
			while (resultSet.next()) {
				result.add(new Bill(resultSet.getInt("id"), resultSet.getInt("bill_id"), resultSet.getDate("date"), 
						resultSet.getString("market"), resultSet.getInt("article_id"), resultSet.getDouble("price")));
			}
			DatabaseConfiguration.closeConnection();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void deleteByBillId(int bill_id) {
		try {
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = "DELETE FROM billposition WHERE bill_id = " + bill_id;			
			System.out.println(String.format("Deleted %s bill positions with the matching bill_id %d", 
					statement.executeUpdate(sql), bill_id));
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			System.err.println("Couldn't delete bill_id " + bill_id);
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBillId() {
		return billId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
