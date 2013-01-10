package com.ku.ketteringapplication;

import org.jsoup.select.Elements;

/********************************************************************
 * Class: CurrentGradeDetail
 * Purpose: stores 1 detailed grade
/*******************************************************************/
public class CurrentGradeItem {

	// Properties
	private String gradeName;
	private boolean validGrade;
	private double score;
	private double pointsPossible;
	
	
	/********************************************************************
	 * Constructor: CurrentGradeDetail
	 * Purpose: create a grade detail object
	 * Parameters:
	 * 		Element gradeDetail: contains HTML grade information
	/*******************************************************************/
	public CurrentGradeItem(Elements gradeDetail){

		// 6 Children?
		if (gradeDetail.size() >=6){
			
			// Set properties
			this.gradeName = gradeDetail.get(0).text();
			this.validGrade = true;		
		
		
			// Score
			try {
				this.score = Double.parseDouble(gradeDetail.get(5).text());
			}
			
			// Invalid
			catch(Exception e) {
				this.score = -1;
				this.validGrade = false;
			}
			
			
			// Points Possible
			try {
				this.pointsPossible = Double.parseDouble(gradeDetail.get(6).text());
			}
			
			// Invalid
			catch(Exception e) {
				this.pointsPossible = -1;
				this.validGrade = false;
			}
		}
		
		// Not enough children
		else {
			this.validGrade = false;
			this.gradeName = "error";
			this.score = -1;
			this.pointsPossible = -1;
		}
		
	}
	
	/********************************************************************
	 * Accessors: getGradeName, getValidGrade, getScore, getPointsPossible
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getGradeName() { return this.gradeName; }
	public boolean getValidGrade() { return this.validGrade; }
	public double getScore() { return this.score; }
	public double getPointsPossible() { return this.pointsPossible; }
	
}
