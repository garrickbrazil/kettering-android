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
