package com.donotreadme.shoppingAnalysis.config;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.Comparator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {

	@BeforeEach
	void setUp() throws Exception {
		try {
			Object[] a = Files.walk(Paths.get("../Shopping/test").toAbsolutePath()).sorted(Comparator.reverseOrder())
					.toArray();
			for (int i = 0; i < a.length; i++) {
				Files.delete(Paths.get(a[i].toString()));
			}
		} catch (Exception e) {
			System.err.println("test folder couldn't be deleted");
			e.printStackTrace();
		}
	}

	@AfterEach
	void tearDown() throws Exception {		
		DatabaseConfiguration.connection.close();
		DatabaseConfiguration.connection = null;
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		try {
			Object[] a = Files.walk(Paths.get("../Shopping/test").toAbsolutePath()).sorted(Comparator.reverseOrder())
					.toArray();
			for (int i = 0; i < a.length; i++) {
				Files.delete(Paths.get(a[i].toString()));
			}
		} catch (Exception e) {
			System.err.println("test folder couldn't be deleted");
			e.printStackTrace();
		}
	}

	@Test
	void testConnectDataBase() {

	}

	@Test
	void testCreateDatabase() {
		assertTrue(DatabaseConfiguration.createDatabase("test"));
		try {
			Statement s = DatabaseConfiguration.connection.createStatement();
			s.executeUpdate("DROP table bill");
			s.executeUpdate("DROP table article");
			s.executeUpdate("DROP table product");
			s.close();
			DatabaseConfiguration.connection.close();
		} catch (SQLSyntaxErrorException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
