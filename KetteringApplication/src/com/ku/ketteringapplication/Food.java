package com.ku.ketteringapplication;


/********************************************************************
 * Class: Food
 * Purpose: holds food items
/*******************************************************************/
public class Food {
	
	// Properties
	private String soup;
	private String pastaStation;
	private String grill;
	private String pizza;
	private String deli;
	private String expo;
	private String dessert;
	
	
	/********************************************************************
	 * Constructor: Food
	 * Purpose: create food object
	/*******************************************************************/
	public Food(){
		
		// Default
		this.soup = "";
		this.pastaStation = "";
		this.grill = "";
		this.pizza = "";
		this.deli = "";
		this.expo = "";
		this.dessert = "";
	}
	
	/********************************************************************
	 * Accessors: getSoup, getPastaStation, getGrill, getPizza, getDeli,
	 * 		getExpo, getDessert
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getSoup() { return this.soup; }
	public String getPastaStation() { return this.pastaStation; }
	public String getGrill() { return this.grill; }
	public String getPizza() { return this.pizza; }
	public String getDeli() { return this.deli; }
	public String getExpo() { return this.expo; }
	public String getDessert() { return this.dessert; }
	
	
	/********************************************************************
	 * Mutuators: setSoup, setPastaStation, setGrill, setPizza, setDeli,
	 * 		setExpo, setDessert
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public void setSoup(String soup) { this.soup = soup; }
	public void setPastaStation(String pastaStation) { this.pastaStation = pastaStation; }
	public void setGrill(String grill) { this.grill = grill; }
	public void setPizza(String pizza) { this.pizza = pizza; }
	public void setDeli(String deli) { this.deli = deli; }
	public void setExpo(String expo) { this.expo = expo; }
	public void setDessert(String dessert) { this.dessert = dessert; }
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		String foodStr = "";
		
		if(!this.soup.equals("\u00A0")) foodStr += "Soup: " + this.soup + " ";
		if(!this.pastaStation.equals("\u00A0")) foodStr += "Pasta Station: " + this.pastaStation + " ";
		if(!this.grill.equals("\u00A0")) foodStr += "Grill: " + this.grill + " ";
		if(!this.pizza.equals("\u00A0")) foodStr += "Pizza: " + this.pizza + " ";
		if(!this.deli.equals("\u00A0")) foodStr += "Deli: " + this.deli + " ";
		if(!this.expo.equals("\u00A0")) foodStr += "Expo: " + this.expo + " ";
		if(!this.dessert.equals("\u00A0")) foodStr += "Dessert: " + this.dessert + " ";
		
		return foodStr;
	}
	
}
