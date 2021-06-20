package com.donotreadme.shoppingAnalysis.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillTest {

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
	void testBill() {
		Bill.changeBillId();
		assertThrows(NullPointerException.class, () -> new Bill(new Date(), Shop.ALDI.name(), "testBillArticle", 1.10));
		Article testArticle = new Article("testBillArticle", "testBillProduct", Category.TEST.name());
		testArticle.addArticle();
		Bill testBill1 = new Bill(new Date(), Shop.ALDI.name(), "testBillArticle", 1.10);
		assertEquals(Article.getJoinByDescription("testBillArticle").getId(), testBill1.getArticleId());
		assertEquals(1, testBill1.getId());
		assertEquals(1, testBill1.getBillId());
		assertEquals("ALDI", testBill1.getMarket());
		assertEquals(1.1, testBill1.getPrice());
		Bill.deleteByBillId(testBill1.getId());
		Article.deleteArticleByDescription(testArticle.getDescription());
		Product.deleteProductById(testArticle.getProductId());
	}

	@Test
	void testChangeBillId() {
		Bill.changeBillId();
		Article testArticle = new Article("testBillArticle", "testBillProduct", Category.TEST.name());
		testArticle.addArticle();
		Bill testBill1 = new Bill(new Date(), Shop.ALDI.name(), "testBillArticle", 1.10);
		assertEquals(1, testBill1.getBillId());
		Bill.changeBillId();
		Bill testBill2 = new Bill(new Date(), Shop.ALDI.name(), "testBillArticle", 1.10);
		assertEquals(2, testBill2.getBillId());
		Bill.deleteByBillId(2);
		Bill.changeBillId();
		Bill testBill3 = new Bill(new Date(), Shop.ALDI.name(), "testBillArticle", 1.10);
		assertEquals(2, testBill3.getBillId());
		Bill.deleteByBillId(1);
		Bill.deleteByBillId(2);
		Product.deleteProductById(testArticle.getProductId());
		Article.deleteArticleByDescription(testArticle.getDescription());		
	}

	@Test
	void testGetAllPositionsForBillId() {
		Article testArticle = new Article("testBillArticle", "testBillProduct", Category.TEST.name());
		testArticle.addArticle();
		Bill.changeBillId();
		Bill testBill1 = new Bill(new Date(), Shop.ALDI.name(), "testBillArticle", 1.10);
		Bill testBill2 = new Bill(new Date(), Shop.ALDI.name(), "testBillArticle", 1.10);
		Bill.changeBillId();
		Bill testBill3 = new Bill(new Date(), Shop.ALDI.name(), "testBillArticle", 1.10);
		assertEquals(2, Bill.getAllPositionsForBillId(1).size());
		assertEquals(3, Bill.getAllPositionsForBillId(2).getFirst().getId());
		Product.deleteProductById(testArticle.getProductId());
		Article.deleteArticleByDescription(testArticle.getDescription());
		Bill.deleteByBillId(1);
		Bill.deleteByBillId(2);		
	}

	@Test
	void testGetAllPositionsForSpanOfTime() {
		Article testArticle = new Article("testBillArticle", "testBillProduct", Category.TEST.name());
		testArticle.addArticle();
		Bill.changeBillId();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 		
		try {
			Bill testBill1 = new Bill(dateFormat.parse("2020-01-01"), Shop.ALDI.name(), "testBillArticle", 1.10);
			Bill testBill2 = new Bill(dateFormat.parse("2020-01-03"), Shop.ALDI.name(), "testBillArticle", 1.10);
			assertEquals(1, Bill.getAllPositionsForSpanOfTime(dateFormat.parse("2020-01-01"), 
					dateFormat.parse("2020-01-02")).size());
			assertEquals(testBill2.getId(), Bill.getAllPositionsForSpanOfTime(dateFormat.parse("2020-01-03"), 
					dateFormat.parse("2020-01-03")).getFirst().getId());			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Product.deleteProductById(testArticle.getProductId());
		Article.deleteArticleByDescription(testArticle.getDescription());
		Bill.deleteByBillId(1);	
	}

}
