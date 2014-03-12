/*
 	This file is part of KetteringApplication.

	KetteringApplication is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    KetteringApplication is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with KetteringApplication.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.ku.ketteringapplication;

import org.jsoup.select.Elements;

/********************************************************************
 * Class: MidtermGrade
 * Purpose: holds a single midterm grade and all its properties
/*******************************************************************/
public class MidtermGrade {
	
	// Properties
	private int crn;
	private String subject;
	private int courseID;
	private int section;
	private String title;
	private String campus;
	private String grade;
	private double attemptedCredits;
	
	
	/********************************************************************
	 * Constructor: MidtermGrade
	 * Purpose: create a default midterm grade object
	/*******************************************************************/
	public MidtermGrade(){
		
		// Initialize
		this.crn = 0;
		this.subject = "";
		this.courseID = 0;
		this.section = 0;
		this.title = "";
		this.campus = "";
		this.grade = "";
		this.attemptedCredits = 0;
	}
	
	/********************************************************************
	 * Constructor: MidtermGrade
	 * Purpose: create a midterm grade object with parameters
	/*******************************************************************/
	public MidtermGrade(Elements details){
		
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

	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		return "CRN: " + this.crn +  " CourseID: " + this.courseID + " Section: " + this.section + " Grade: " + this.grade + " Subject: " + this.subject + " Title: " + this.title;  
	}
}
