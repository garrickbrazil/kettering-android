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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ku.ketteringapplication.MainActivity.StoreScheduleTermTask;

import android.annotation.SuppressLint;
import android.util.SparseArray;


/********************************************************************
 * Class: DynamicCourses
 * Purpose: holds dynamic courses and related functions
/*******************************************************************/
public class DynamicCourses {

	// Properties
	private Map<String, List<Course>> dynamicCourses;
	private SparseArray<Boolean> courseConflicts;
	private List<String> dynamicCoursesIDs;
	private List<String> currentCourseList;
	private List<List<Course>> workingSchedules;
	private List<String> dynamicCourseNames;
	private List<String> termsName;
	private List<String> termsValue;
	private String currentTerm;
	private boolean termsLoaded;
	private boolean loaded;
	private int termIndex; 
	
	/********************************************************************
	 * Constructor: DynamicCourses
	 * Purpose: default constructor
	/*******************************************************************/
	@SuppressLint("UseSparseArrays")
	public DynamicCourses(){
	
		this.termIndex = 0;
		this.loaded = false;
		this.termsLoaded = false;
		this.currentTerm = "";
		this.termsName = new ArrayList<String>();
		this.currentCourseList = new ArrayList<String>();
		this.termsValue = new ArrayList<String>();
		this.dynamicCourses = new HashMap<String, List<Course>>();
		this.dynamicCoursesIDs = new ArrayList<String>();
		this.dynamicCourseNames = new ArrayList<String>();
		this.dynamicCoursesIDs.add("<Classes>");
		this.workingSchedules = new ArrayList<List<Course>>();
		this.courseConflicts = new SparseArray<Boolean>();
	}
	
	
	/********************************************************************
	 * Accessors: getDynamicCourses, getResults getDynamicCourseIDs
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public Map<String, List<Course>> getDynamicCourses(){ return this.dynamicCourses; }
	public List<List<Course>> getWorkingSchedules(){ return this.workingSchedules; }
	public List<String> getDynamicCourseIDs(){ return this.dynamicCoursesIDs; }
	public List<String> getTermsName(){ return this.termsName; }
	public List<String> getTermsValues(){ return this.termsValue; }
	public boolean getTermsLoaded(){ return this.termsLoaded; }
	public boolean getLoaded(){	return this.loaded; }
	public String getCurrentTerm(){ return this.currentTerm; }
	public List<String> getDynamicCourseNames(){ return this.dynamicCourseNames; }
	public int getTermIndex(){ return this.termIndex; }
	public List<String> getCurrentCourseList(){ return this.currentCourseList; }
	
	
	/********************************************************************
	 * Method: setTerm
	 * Purpose: sets a new term for the scheduler
	/*******************************************************************/
	public void setTerm(int index){
		
		if (index < 0 || index >= this.termsName.size()){ this.termIndex = 0; return; }
		
		this.termIndex = index;
		this.currentTerm = this.termsValue.get(index);
		this.loaded = false;
		this.dynamicCourses = new HashMap<String, List<Course>>();
		this.dynamicCoursesIDs = new ArrayList<String>();
		this.dynamicCourseNames = new ArrayList<String>();
	}

	
	/********************************************************************
	 * Method: storeTerms
	 * Purpose: store available terms to use
	/*******************************************************************/
	public boolean storeTerms(){
		
		try {
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet dynamicGet = new HttpGet("https://jweb.kettering.edu/cku1/xhwschedule.P_SelectSubject");
			HttpResponse response = client.execute(dynamicGet);
			
			String html = HTMLParser.parse(response);
			
			Document doc = Jsoup.parse(html);
			Elements options = doc.getElementsByTag("option");
			
			for(Element option : options){
				
				if(!option.text().contains("None")){
					
					this.termsName.add(option.text());
					this.termsValue.add(option.val());
				}
				else {
					
					this.termsName.add("<Term>");
					this.termsValue.add("<Term>");
				}
			}
			
			
			termsLoaded = true;
			return true;
		}
		
		catch(Exception e){ e.printStackTrace(); termsLoaded = false; return false; }
	}
	
	
	
	/********************************************************************
	 * Method: storeDynamicCourses()
	 * Purpose: store all dynamic courses to memory
	/
	 * @param context *******************************************************************/
	public boolean storeDynamicCourses(StoreScheduleTermTask context){
		
		try {
			
			if (this.currentTerm.equals("") || this.currentTerm.equals("<Term>")) return false;
			
			//DefaultHttpClient client = new DefaultHttpClient();
			
			// URLs
			String begin = "https://jweb.kettering.edu/cku1/bwckschd.p_get_crse_unsec?term_in=" + currentTerm + "&sel_subj=dummy&sel_day=dummy&sel_schd=dummy&sel_insm=dummy&sel_camp=dummy&sel_levl=dummy&sel_sess=dummy&sel_instr=dummy&sel_ptrm=dummy&sel_attr=dummy";
			String end = "&sel_crse=&sel_title=&sel_from_cred=&sel_to_cred=&sel_instr=%25&begin_hh=0&begin_mi=0&begin_ap=0&end_hh=0&end_mi=0&end_ap=a";
			
			String subjectList = "";
			
			// 32 Subjects
			subjectList = "&sel_subj=ACCT&sel_subj=BIOL&sel_subj=BUSN&sel_subj=CHME&sel_subj=CHEM&sel_subj=CHN&sel_subj=COMM&sel_subj=CE&sel_subj=CS&sel_subj=ECON&sel_subj=ECE&sel_subj=EE&sel_subj=ESL&sel_subj=FINC&sel_subj=GER&sel_subj=HMGT&sel_subj=HIST&sel_subj=HUMN&sel_subj=IME&sel_subj=ISYS&sel_subj=MFGO&sel_subj=LS&sel_subj=LIT&sel_subj=MGMT&sel_subj=MRKT&sel_subj=MATH&sel_subj=MECH&sel_subj=MEDI&sel_subj=ORTN&sel_subj=PHIL&sel_subj=PHYS&sel_subj=SSCI&sel_subj=SOC";
						
			
			URL url = new URL(begin + subjectList + end);
			Elements mainContainer = Jsoup.parse(url, 0).getElementsByClass("datadisplaytable");
			
			// Valid container ?
			if(mainContainer.size() > 0 && mainContainer.get(0).getElementsByTag("tbody").size() > 0){
					
				Elements coursesOffered = mainContainer.get(0).getElementsByTag("tbody").get(0).children();
				
				// Regular expression to get credits (no HTML consistency)
				Pattern p = Pattern.compile("^.+([0-9].[0-9][0-9][0-9])\\sCredits.*");
				
				for (int j = 0; j < coursesOffered.size()/2; j++) {
					
					if(context.isCancelled()){
						
						this.loaded = false;
						return false;
					}
					
					// New course
					Course currentCourse = new Course();
					String[] courseTitle = coursesOffered.get(j*2).text().split("\\s[-]\\s");
							
					// Properties
					String courseName, courseID, section, instructor, location, dateRange, time, days;
					double credits; int crn;
					
					if ((courseTitle.length == 4 || courseTitle.length == 5) && coursesOffered.get(j*2+1).getElementsByTag("td").size() >= 8 && coursesOffered.get(j*2+1).getElementsByTag("td").get(0).text().split("\\d.\\d\\d\\d\\sCredits").length >= 2){
						
						
						if(courseTitle.length == 4){

							// Title info
							courseName = courseTitle[0];
							courseID = courseTitle[2];
							section = courseTitle[3];
							
							// CRN
							try{ crn = Integer.parseInt(courseTitle[1]); } catch(Exception e){ crn = 0; }
						}
						

						else{

							// First hyphen is part of title ?
							courseName = courseTitle[0] + " - " + courseTitle[1];
							courseID = courseTitle[3];
							section = courseTitle[4];
							
							// CRN
							try{ crn = Integer.parseInt(courseTitle[2]); } catch(Exception e){ crn = 0; }
						}
							
						
						// Special cases
						if(section.contains("L")) courseID += "L";
						if(section.contains("W")) courseID += "W";
						if(section.contains("D")) courseID += "D";
						
						
						// Credits
						Matcher m = p.matcher(coursesOffered.get(j*2+1).getElementsByTag("td").get(0).text());
						credits = 0;
						
						while(m.find()){
							try{ credits = Double.parseDouble(m.group(1)); }
							catch(Exception e){ credits = 0; }
						}
						
						
						Elements timeInfo = coursesOffered.get(j*2 + 1).getElementsByTag("td");
						// Remove headers
						timeInfo.remove(0); timeInfo.remove(0);
						
						
						// Set time information
						time = timeInfo.get(0).text();
						days = timeInfo.get(1).text();
						location = timeInfo.get(2).text();
						dateRange = timeInfo.get(3).text();
						instructor = timeInfo.get(5).text();
						
						
						// Set all properties
						currentCourse.setCourseName(courseName);
						currentCourse.setCourseID(courseID);
						currentCourse.setCredits(credits);
						currentCourse.setCRN(crn);
						currentCourse.setDateRange(dateRange);
						currentCourse.setDays(days);
						currentCourse.setInstructor(instructor);
						currentCourse.setLocation(location);
						currentCourse.setSection(section);
						currentCourse.setTime(time);
						currentCourse.M = days.contains("M");
						currentCourse.T = days.contains("T");
						currentCourse.W = days.contains("W");
						currentCourse.R = days.contains("R");
						currentCourse.F = days.contains("F");
						
						// Course doesn't already exists
						if(this.dynamicCourses.get(courseID) == null){
							
							// Create new
							List<Course> newList = new ArrayList<Course>();
							newList.add(currentCourse);
							this.dynamicCourses.put(courseID, newList);
							this.dynamicCoursesIDs.add(courseID);
							this.dynamicCourseNames.add(courseName);
						}
						
						// Already exists
						else this.dynamicCourses.get(courseID).add(currentCourse);	
					}
				}	
			}	
			
			
			loaded = true;
			//this.dynamicCoursesIDs.remove(0);
			return true;
		}
		
		catch(Exception e){ e.printStackTrace(); loaded = false; return false; }
	}
	
		
	/********************************************************************
	 * Method: generatePermutations
	 * Purpose: sets class options with given class ID's
	/*******************************************************************/
	public void generatePermutations(){
		
		// Initialize working set
		List<List<Course>> workingSet = new ArrayList<List<Course>>();
		
		// For each course
		for(String courseID : currentCourseList){
			
			// Create a new working set
			List<List<Course>> newSet = new ArrayList<List<Course>>();
			
			// For each section in the course
			for(Course course : this.dynamicCourses.get(courseID)){
				
				if(workingSet.size() == 0){
					List<Course> cloneSet = new ArrayList<Course>();
					testClasses(new ArrayList<Course>(),course,cloneSet);
					newSet.add(cloneSet);
				}
				
				else{
					// Check all current workingSets
					for(List<Course> courseSet : workingSet){
						
						List<Course> cloneSet = new ArrayList<Course>();
						
						// If fits into working set then add to newSet
						if(testClasses(courseSet, course, cloneSet)){
							newSet.add(cloneSet);
						}
					}
				}
			}
			
			workingSet = newSet;
		}
		
		workingSchedules = workingSet;	
	}
	
	/********************************************************************
	 * Method: testClasses
	 * Purpose: tests if classes work together
	/*******************************************************************/
	public boolean testClasses(List<Course> courses, Course courseCmp, List<Course> clone){
	
		
		clone.add(courseCmp);
		TimeBlock cmpBlock = TimeBlock.convertToTimeBlock(courseCmp.getTime(), courseCmp);
		
		int startC = cmpBlock.getStartHours() * 60 + cmpBlock.getStartMin();
		int endC = cmpBlock.getEndHours() * 60 + cmpBlock.getEndMin();
		int crn = courseCmp.getCRN();
		int crnS;
		
		for(Course course : courses){	
			
			if (crn < course.getCRN()){
				crnS = crn*100000 + (course.getCRN());
			}
			else {
				crnS = (course.getCRN()*100000) + crn;
			}
			clone.add(course);
			if(this.courseConflicts.get(crnS) != null){
				if (!this.courseConflicts.get(crnS)) return false;
			}
			else {
				TimeBlock block = TimeBlock.convertToTimeBlock(course.getTime(), course);
				
				int startB = block.getStartHours() * 60 + block.getStartMin();
				int endB = block.getEndHours() * 60 + block.getEndMin();
					
				if((startB >= startC && startB <= endC) || (endB <= endC && endB >= startC)){
					
					if ((course.M && courseCmp.M) || (course.T && courseCmp.T) || (course.W && courseCmp.W) || (course.R && courseCmp.R) || (course.F && courseCmp.F)){
						this.courseConflicts.put(crnS, false);
						return false;
					}
					else{
						this.courseConflicts.put(crnS, true);
					}
				}
				else{
					this.courseConflicts.put(crnS, true);
				}
			}
		}
					
		return courses.size() > 0;
		
		/*
		clone.add(courseCmp);

		// Which days ?
		boolean M = courseCmp.getDays().contains("M");
		boolean T = courseCmp.getDays().contains("T");
		boolean W = courseCmp.getDays().contains("W");
		boolean R = courseCmp.getDays().contains("R");
		boolean F = courseCmp.getDays().contains("F");
		
		boolean[] mon = new boolean[21-8 + 1];
		boolean[] tues = new boolean[21-8 + 1];
		boolean[] weds = new boolean[21-8 + 1];
		boolean[] thurs = new boolean[21-8 + 1];
		boolean[] fri = new boolean[21-8 + 1];
	
		
		// Convert to TimeBlock
		TimeBlock newBlock = TimeBlock.convertToTimeBlock(courseCmp.getTime(), courseCmp);
		if(newBlock == null ) return false;
		 
		 for(int i = newBlock.getStartHours(); i < newBlock.getEndHours(); i++){
			 
			 if(M){
				 if(!mon[i-8]) mon[i-8] = true;
				 else return false;
			 }
			 if(T){
				 if(!tues[i-8]) tues[i-8] = true;
				 else return false;
			 }
			 if(W){
				 if(!weds[i-8]) weds[i-8] = true;
				 else return false;
			 }
			 if(R){
				 if(!thurs[i-8]) thurs[i-8] = true;
				 else return false;
			 }
			 if(F){
				 if(!fri[i-8]) fri[i-8] = true;
				 else return false;
			 }
		 }
		
		
		for(Course current : courses){
			
			clone.add(current);
			
			// Which days ?
			M = current.getDays().contains("M");
			T = current.getDays().contains("T");
			W = current.getDays().contains("W");
			R = current.getDays().contains("R");
			F = current.getDays().contains("F");
				
			// Convert to TimeBlock
			newBlock = TimeBlock.convertToTimeBlock(current.getTime(),current);
			if(newBlock == null ) return false;
		
			for(int i = newBlock.getStartHours(); i < newBlock.getEndHours(); i++){
		
				 if(M){
					 if(!mon[i-8]) mon[i-8] = true;
					 else return false;
				 }
				 if(T){
					 if(!tues[i-8]) tues[i-8] = true;
					 else return false;
				 }
				 if(W){
					 if(!weds[i-8]) weds[i-8] = true;
					 else return false;
				 }
				 if(R){
					 if(!thurs[i-8]) thurs[i-8] = true;
					 else return false;
				 }
				 if(F){
					 if(!fri[i-8]) fri[i-8] = true;
					 else return false;
				 }
			}
		}
				
		return courses.size() > 0;
		*/
	}
	
	
	public void generatePermutations(int setNum, List<Course> currentSet){
		
		if(setNum == currentCourseList.size()){
			
			// Clone
			List<Course> clone = new ArrayList<Course>();
			for(Course current : currentSet) clone.add(current);
			
			workingSchedules.add(clone);
			if(currentSet.size() > 0) currentSet.remove(currentSet.size() -1);
		}
		
		else{
			
			for(Course course : this.dynamicCourses.get(currentCourseList.get(setNum))){
				currentSet.add(course);
				if(testClasses(currentSet)){
					generatePermutations(setNum + 1, currentSet);
				}
				else {
					if(currentSet.size() > 0) currentSet.remove(currentSet.size() -1);
				}
			}
			if(currentSet.size() > 0) currentSet.remove(currentSet.size() -1);
		}
	}
	
	/********************************************************************
	 * Method: testClasses
	 * Purpose: tests if classes work together
	/*******************************************************************/
	public boolean testClasses(List<Course> courses){
		
		
		// Days of the week
		List<TimeBlock> Monday = new ArrayList<TimeBlock>();
		List<TimeBlock> Tuesday = new ArrayList<TimeBlock>();
		List<TimeBlock> Wednesday = new ArrayList<TimeBlock>();
		List<TimeBlock> Thursday = new ArrayList<TimeBlock>();
		List<TimeBlock> Friday = new ArrayList<TimeBlock>();
		
		for(Course current : courses){
			
			// Which days ?
			boolean M = current.getDays().contains("M");
			boolean T = current.getDays().contains("T");
			boolean W = current.getDays().contains("W");
			boolean R = current.getDays().contains("R");
			boolean F = current.getDays().contains("F");
				
			// Convert to TimeBlock
			TimeBlock newBlock = TimeBlock.convertToTimeBlock(current.getTime(), current);
			if(newBlock == null ) return false;
			 
			 if(M){
				 
				 // Monday
				 if(TimeBlock.testDay(Monday, newBlock)) Monday.add(newBlock);
				 else return false;
			 }
			 
			 if(T){

				 // Tuesday
				 if(TimeBlock.testDay(Tuesday, newBlock)) Tuesday.add(newBlock);
				 else return false;
			 }
			 
			 if(W){
				 
				 // Wednesday
				 if(TimeBlock.testDay(Wednesday, newBlock)) Wednesday.add(newBlock);
				 else return false;
			 }
			 
			 
			 if(R){
				 
				 // Thursday
				 if(TimeBlock.testDay(Thursday, newBlock)) Thursday.add(newBlock);
				 else return false;
			 }
			 
			 
			 if(F){
				 
				 // Friday
				 if(TimeBlock.testDay(Friday, newBlock)) Friday.add(newBlock);
				 else return false;
			 }
		}
		
		return courses.size() > 0;
	}
}


/********************************************************************
 * Class: JSONCourses
 * Purpose: holds list of JSONCourse
/*******************************************************************/
class JSONCourses{
	
	private List<JSONCourse> jsonCourses;
	public List<JSONCourse> getCourses(){ return this.jsonCourses; }
}

/********************************************************************
 * Class: JSONCoruse
 * Purpose: holds set and index
/*******************************************************************/
class JSONCourse{
	
	// Properties
	private int set;
	private int index;
	
	// Accessors
	public int getSet(){ return this.set; }
	public int getIndex(){ return this.index; }
	
}
