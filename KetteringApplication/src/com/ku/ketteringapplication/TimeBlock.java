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

import java.util.List;

/********************************************************************
 * Class: TimeBlock
 * Purpose: holds a start and end time
/*******************************************************************/
public class TimeBlock implements Comparable<TimeBlock> {
	
	private int startHours, startMin, endHours, endMin;
	Course course;
	
	/********************************************************************
	 * Constructor: TimeBlock
	 * Purpose: creates TimeBlock object with given parameters
	/*******************************************************************/
	public TimeBlock(int startH, int startM, int endH, int endM, Course course){
		
		this.course = course;
		this.startHours = startH;
		this.startMin = startM;
		this.endHours = endH;
		this.endMin = endM;
	}
	
	/********************************************************************
	 * Constructor: TimeBlock
	 * Purpose: creates TimeBlock object with given parameters
	/*******************************************************************/
	public TimeBlock(int startH, int startM, int endH, int endM){
		
		this.startHours = startH;
		this.startMin = startM;
		this.endHours = endH;
		this.endMin = endM;
	}
	
	/********************************************************************
	 * Method: testDay
	 * Purpose: test to see if a TimeBlock fits into a day
	/*******************************************************************/
	public static boolean testDay(List<TimeBlock> day, TimeBlock block){
		
		 // Test conflicts
		 for (TimeBlock currentBlock : day ) if(currentBlock.conflicts(block)) return false;
		 
		 return true;
		 
	}

	
	/********************************************************************
	 * Method: convertToTimeBlock
	 * Purpose: converts a time string into a TimeBlock object
	/*******************************************************************/
	public static TimeBlock convertToTimeBlock(String time, Course courseIn){
		
		// Properties
		int startHours, startMinutes, endHours, endMinutes;
		String[] splitTime = time.split("\\s-\\s");
		
		
		if(splitTime.length == 2){// && splitTime[0].matches("\\d+:\\d\\d\\s(am|pm)") && splitTime[1].matches("\\d+:\\d\\d\\s(am|pm)")){
			
			try{

				
				 // Start
				 startHours = Integer.parseInt(splitTime[0].split(":")[0]);
				 startMinutes = Integer.parseInt((splitTime[0].split(":")[1].replaceAll("[^\\d]", "")));
				 
				 // End
				 endHours = Integer.parseInt(splitTime[1].split(":")[0]);
				 endMinutes = Integer.parseInt((splitTime[1].split(":")[1].replaceAll("[^\\d]", "")));
				 
				 
				 // Convert start to military 
				 if(splitTime[0].replaceAll("[^(am|pm)]", "").equals("pm") && startHours < 12) startHours += 12;
				 else if (splitTime[0].replaceAll("[^(am|pm)]", "").equals("am") && startHours == 12) startHours = 0;
				 

				 // Convert end to military 
				 if(splitTime[1].replaceAll("[^(am|pm)]", "").equals("pm") && endHours < 12) endHours += 12;
				 else if (splitTime[1].replaceAll("[^(am|pm)]", "").equals("am") && endHours == 12) endHours = 0;
				 
				return new TimeBlock(startHours, startMinutes, endHours, endMinutes, courseIn);
				
			}
			
			catch(Exception e){ return null; }
		}
		
		else return null;
		
	}
	
	
	public int getRowSpan(){
		
		
		double minuteDif = (this.endHours * 60 + this.endMin)  - (this.startHours * 60 + this.startMin);
		int rowCount = (int) ((minuteDif)/5 + .5);
		
		return rowCount;
	}
	
	
	public int compareToRowEnd(TimeBlock compareBlock){
		
		double minuteDif = (compareBlock.getStartHours() * 60 + compareBlock.getStartMin())  - (this.getEndHours() * 60 + this.getEndMin());
		int rowCount = (int) ((minuteDif)/5 + .5);
		
		return rowCount;
	}
	
	
	/********************************************************************
	 * Method: compareTo
	 * Purpose: format object into a string
	/*******************************************************************/
	public int compareTo(TimeBlock compareBlock){
		
		if (this.startHours < compareBlock.getStartHours()){
			return -1;
		}
		else if(this.startHours > compareBlock.getStartHours()){
			return 1;
		}
		else {
			if (this.startMin < compareBlock.getStartMin()) return -1;
			else if (this.startMin > compareBlock.getStartMin()) return 1;
			else return 0;
		}
	}
	
	/********************************************************************
	 * Method: conflicts
	 * Purpose: checks to see if the given TimeBlock conflicts
	/*******************************************************************/
	public boolean conflicts(TimeBlock block){
		
		// Problems ?
		if(block == null || (this.compareTo(block) <= 0 && this.compareTo(block) >= 0)) return true;
		else if(block == null || (this.compareTo(block) <= 0 && this.compareTo(block) >= 0)) return true;
		
		else return false;
	}
	
	/********************************************************************
	 * Accessors
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public int getStartHours(){ return this.startHours; }
	public int getStartMin(){ return this.startMin; }
	public int getEndHours(){ return this.endHours; }
	public int getEndMin(){ return this.endMin; }
	public Course getCourse(){ return this.course; }
}
