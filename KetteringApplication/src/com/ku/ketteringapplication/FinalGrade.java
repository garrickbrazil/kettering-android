package com.ku.ketteringapplication;

import org.jsoup.select.Elements;

/********************************************************************
 * Class: FinalGrade
 * Purpose: holds a single final grade and all its properties
/*******************************************************************/
public class FinalGrade {
	
	// Properties
	private int crn;
	private String subject;
	private int courseID;
	private int section;
	private String title;
	private String campus;
	private String grade;
	private double attemptedCredits;
	private double earnedCredits;
	private double GPAHours;
	private double qualityPoints;
	
	/********************************************************************
	 * Constructor: FinalGrade
	 * Purpose: create a default final grade object
	/*******************************************************************/
	public FinalGrade(){
		
		// Defaults
		this.crn = 0;
		this.subject = "";
		this.courseID = 0;
		this.section = 0;
		this.title = "";
		this.campus = "";
		this.grade = "";
		this.attemptedCredits = 0;
		this.earnedCredits = 0;
		this.GPAHours = 0;
		this.qualityPoints = 0;
		
	}
	
	/********************************************************************
	 * Constructor: FinalGrade
	 * Purpose: create a final grade object with parameters
	/*******************************************************************/
	public FinalGrade(Elements details){
	
		
		// Properties
		this.subject = details.get(1).text();
		this.title = details.get(4).text();
		this.campus = details.get(5).text();
		this.grade = details.get(6).text();
		
		
		// Number properties
		try{ this.crn = Integer.parseInt(details.get(0).text()); } catch(Exception e){ this.crn = 0; }
		try{ this.courseID = Integer.parseInt(details.get(2).text()); } catch(Exception e){ this.courseID = 0; }
		try{ this.section = Integer.parseInt(details.get(3).text()); } catch(Exception e){ this.section = 0; }
		try{ this.attemptedCredits = Double.parseDouble(details.get(7).text()); } catch(Exception e){ this.attemptedCredits = 0; }
		try{ this.earnedCredits = Double.parseDouble(details.get(8).text()); } catch(Exception e){ this.earnedCredits = 0; }
		try{ this.GPAHours = Double.parseDouble(details.get(9).text()); } catch(Exception e){ this.GPAHours = 0; }
		try{ this.qualityPoints = Double.parseDouble(details.get(10).text()); } catch(Exception e){ this.qualityPoints = 0; }
			
	}
	
	
	
	/********************************************************************
	 * Accessors: getCrn, getSubject, getCourseId, getSection, getTitle, 
	 * 		getCampus, getGrade, getAttemptedCredits, getEarnedCredits,
	 * 		getGPAHours, getQualityPoints
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public int getCrn(){ return this.crn; }
	public String getSubject(){ return this.subject; }
	public int getCourseId(){ return this.courseID; }
	public int getSection(){ return this.section; }
	public String getTitle(){ return this.title; }
	public String getCampus(){ return this.campus; }
	public String getGrade(){ return this.grade; }
	public double getAttemptedCredits(){ return this.attemptedCredits; }
	public double getEarnedCredits(){ return this.earnedCredits; }
	public double getGPAHours(){ return this.GPAHours; }
	public double getQualityPoints(){ return this.qualityPoints; }

	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		return "CRN: " + this.crn +  " CourseID: " + this.courseID + " Section: " + this.section + " Earned credits: " + this.earnedCredits  + " Grade: " + this.grade + " Subject: " + this.subject + " Title: " + this.title;  
	}
}
