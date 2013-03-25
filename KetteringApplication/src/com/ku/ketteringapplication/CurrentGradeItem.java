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
	private String score;
	private String pointsPossible;
	
	
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
			
			this.score = gradeDetail.get(5).text();
			
			// Points Possible
			this.pointsPossible = gradeDetail.get(6).text();
		}
		
		// Not enough children
		else {
			this.validGrade = false;
			this.gradeName = "error";
			this.score = "";
			this.pointsPossible = "";
		}
		
	}
	
	/********************************************************************
	 * Accessors: getGradeName, getValidGrade, getScore, getPointsPossible
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getGradeName() { return this.gradeName; }
	public boolean getValidGrade() { return this.validGrade; }
	public String getScore() { return this.score; }
	public String getPointsPossible() { return this.pointsPossible; }
	
}
