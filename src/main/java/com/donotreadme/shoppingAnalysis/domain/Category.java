package com.donotreadme.shoppingAnalysis.domain;

public enum Category {
	
	LEBENSMITTEL(10000, "Lebensmittel"),
	GETRAENK(20000, "Getränk"),
	SNACK(30000, "Snack"),
	KOSMETIKARTIKEL(40000, "Kosmetikartikel"),
	KLEIDUNG(50000, "Kleidung"),
	SONSTIGES(90000, "Sonstiges"),
	TEST(0, "Testprodukte");
	
	private Category(int number, String name) {
		this.name = name;
		this.number = number;
	}
	
	private int number;
	private String name;
	
	public int getNumber() {
		return number;
	}
	
	public String getName() {
		return name;
	}
	
	
}
