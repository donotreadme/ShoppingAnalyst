package com.donotreadme.shoppingAnalysis.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}
	
	int testProductId;
	Product product;

	@BeforeEach
	void setUp() throws Exception {
		product = new Product("product", "TEST");
		testProductId = product.getId();
		assertTrue(product.addProduct());
	}

	@AfterEach
	void tearDown() throws Exception {
		Product.deleteProductById(testProductId);
	}

	@Test
	void testProduct() {
		assertEquals(0, product.getId());
		assertEquals("product", product.getProductName());
		assertEquals(Category.TEST, product.getCategory());
	}
	
	@Test
	void testGetIdByProductName() {
		Product product2 = new Product("idTest", "TEST");
		product2.addProduct();
		assertEquals(1, Product.getIdByProductName("idTest"));
		assertEquals(0, Product.getIdByProductName("product"));
		assertEquals(-1, Product.getIdByProductName("unknown"));
		Product.deleteProductById(1);
	}
	
	@Test
	void testGetAllProductNames() {
		LinkedList<String> a = Product.getAllProductNames();
		assertTrue(a.size() > 0);
	}

}
