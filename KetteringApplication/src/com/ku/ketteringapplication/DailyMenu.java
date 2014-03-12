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
