package com.donotreadme.shoppingAnalysis.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.donotreadme.shoppingAnalysis.config.DatabaseConfiguration;

class ArticleTest {

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testArticleStringStringString() {
		Article article1 = new Article("testArticle1", "product1", "TEST");
		assertEquals("testArticle1", article1.getDescription());
		assertEquals(-1, article1.getId());
		assertEquals(0, article1.getProductId());
		assertTrue(article1.addArticle());
		assertEquals(1, article1.getId());
		Product product2 = new Product("product2", "TEST");
		product2.addProduct();
		Article article2 = new Article("testArticle2", "product2", "TEST");
		assertEquals(1, article2.getProductId());
		Product.deleteProductById(Product.getIdByProductName("product1"));
		Product.deleteProductById(Product.getIdByProductName(product2.getProductName()));
		Article.deleteArticleByDescription("testArticle1");
	}

	@Test
	void testAddArticle() {		
		try {
			Article article1 = new Article("testArticle1", "product1", "TEST");
			assertTrue(article1.addArticle());
			assertTrue(Article.checkIfArticleExist("testArticle1"));
			//clean up again
			Product.deleteProductById(Product.getIdByProductName("product1"));
			DatabaseConfiguration.connectDataBase();
			Statement statement = DatabaseConfiguration.connection.createStatement();
			String sql = "DELETE FROM article WHERE description = 'testArticle1'";
			assertTrue(statement.executeUpdate(sql) > 0);
			DatabaseConfiguration.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testGetJoinByDescription() {
		Product product1 = new Product("product1", "TEST");
		product1.addProduct();
		Product product2 = new Product("product2", "TEST");
		product2.addProduct();
		Article article1 = new Article("testArticle1", "product1", "TEST");
		article1.addArticle();
		Article article2 = new Article("testArticle2", "product1", "TEST");
		article2.addArticle();		
		assertEquals(1, Article.getJoinByDescription("testArticle1").getId());
		assertEquals("testArticle1", Article.getJoinByDescription("testArticle1").getDescription());
		assertEquals("TEST", Article.getJoinByDescription("testArticle1").getProductCategory());
		assertEquals(0, Article.getJoinByDescription("testArticle1").getProductId());
		assertEquals("product1", Article.getJoinByDescription("testArticle1").getProductName());
		assertEquals(2, Article.getJoinByDescription("testArticle2").getId());
		assertEquals("testArticle2", Article.getJoinByDescription("testArticle2").getDescription());
		//clean up again
		Product.deleteProductById(product1.getId());
		Product.deleteProductById(product2.getId());		
		Article.deleteArticleByDescription("testArticle1");
		Article.deleteArticleByDescription("testArticle2");		
	}

}
