package com.ku.ketteringapplication;


/********************************************************************
 * Class: DailyMenu
 * Purpose: holds a single days menu
/*******************************************************************/
public class DailyMenu {
	
	// Properties
	private String date;
	private Food lunch;
	private Food dinner;
	
	/********************************************************************
	 * Constructor: DailyMenu
	 * Purpose: creates a default daily menu
	/*******************************************************************/
	public DailyMenu(){
		
		// Cook
		this.lunch = new Food();
		this.dinner = new Food();
		this.date = "";
	}
	
	/********************************************************************
	 * Accessors: getLunch, getDinner
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public Food getLunch() { return this.lunch; }
	public Food getDinner(){ return this.dinner; }
	public String getDate(){ return this.date; }
	public void setDate(String date){ this.date = date; }
	
}
