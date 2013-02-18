package com.ku.ketteringapplication;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/********************************************************************
 * Class: CurrentGrade
 * Purpose: stores grades for a single class
/*******************************************************************/
public class CurrentGrade {
	
	// Properties
	private String courseName;
	private List<CurrentGradeItem> gradeItems;
	private double adjPointsPossible;
	private double adjTotal;
	private boolean isLoaded;
	private String link;
	private String titleTotal;
	
	/********************************************************************
	 * Constructor: CurrentGrade()
	 * Purpose: create a grade object
	/*******************************************************************/
	public CurrentGrade(String courseName, String link, String titleTotal){
		
		// Initialize properties
		this.courseName = courseName;
		this.gradeItems = new ArrayList<CurrentGradeItem>();
		this.adjPointsPossible = 0;
		this.adjTotal = 0;
		this.isLoaded = false;
		this.link = link;
		this.titleTotal = titleTotal;
		
	}

	/********************************************************************
	 * Method: storeGradeItems
	 * Purpose: store grade items for course
	/*******************************************************************/
	public boolean storeGradeItems(String details){

		// Document
		Document doc = Jsoup.parse(details);
		
		// Loaded correctly?
		if (doc.getElementById("pageTitleText").text().equalsIgnoreCase("My Grades") && doc.getElementsByTag("tbody").size() > 0){
			
			Elements gradeElements = doc.getElementsByTag("tbody").get(0).children();
			
			// Remove title row
			if(gradeElements.size() > 0) gradeElements.remove(0);
			
			
			// Grade Items
			for (int i = 0; i < gradeElements.size(); i++){
				
				// Create
				CurrentGradeItem current = new CurrentGradeItem(gradeElements.get(i).children()); 
				
				// Adjusted total & points possible
				if (current.getValidGrade() && !(current.getGradeName().equalsIgnoreCase("Total")|| current.getGradeName().equalsIgnoreCase("Weighted Total"))) { 
					
					// Add
					this.gradeItems.add(current);
					this.adjTotal += current.getScore();
					this.adjPointsPossible += current.getPointsPossible();
				}
			}
			this.isLoaded = true;
			return true;
		}
		
		
		else{
			
			this.isLoaded = false;
			return false;
		}
	}
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		String gradeStr = "";
		
		gradeStr = "[" + this.courseName + "]\n";
		
		for(int j = 0; j < this.gradeItems.size(); j++){
			CurrentGradeItem currentDetail = this.gradeItems.get(j);
			if (currentDetail.getValidGrade()){
				gradeStr += "\n" + currentDetail.getGradeName();
				gradeStr += " Score: " + currentDetail.getScore();
				gradeStr += " Possible: " + currentDetail.getPointsPossible();
				gradeStr += " Percentage: " + Math.round(currentDetail.getScore()/currentDetail.getPointsPossible()*100) + "%";
			}	
		}
		
		gradeStr += "\n\nAdjusted Total: " + this.adjTotal + "\n";
		gradeStr += "Adjusted Points Possible: " + this.adjPointsPossible + "\n";
		gradeStr += "Percentage: " + Math.round(this.adjTotal/this.adjPointsPossible * 100) + "%\n";

		return gradeStr;
	}
	
	
	/********************************************************************
	 * Accessors: getCourseName, getValidGrade, getScore, getPointsPossible
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getCourseName() { return this.courseName; }
	public boolean getIsLoaded() { return this.isLoaded; }
	public double getAdjTotal() { return this.adjTotal; }
	public List<CurrentGradeItem> getGradeItems() { return this.gradeItems; }
	public double getAdjPointsPossible() { return this.adjPointsPossible; }
	public String getPercent() { return (this.adjTotal*100) / (this.adjPointsPossible*100) + ""; }
	public String getLink() { return this.link; }
	public String getTitleTotal(){ return this.titleTotal; }
	
}
