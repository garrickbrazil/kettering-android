package com.ku.ketteringapplication;

import java.io.PrintWriter;
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
import org.jsoup.select.Elements;

import com.google.gson.Gson;

/********************************************************************
 * Class: DynamicCourses
 * Purpose: holds dynamic courses and related functions
/*******************************************************************/
public class DynamicCourses {

	// Properties
	private Map<String, List<Course>> dynamicCourses;
	private List<String> dynamicCoursesIDs;
	private List<List<Course>> workingSchedules;
	
	
	/********************************************************************
	 * Constructor: DynamicCourses
	 * Purpose: default constructor
	/*******************************************************************/
	public DynamicCourses(){
	
		this.dynamicCourses = new HashMap<String, List<Course>>();
		this.dynamicCoursesIDs = new ArrayList<String>();
	}
	
	
	/********************************************************************
	 * Accessors: getDynamicCourses, getResults getDynamicCourseIDs
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public Map<String, List<Course>> getDynamicCourses(){ return this.dynamicCourses; }
	public List<List<Course>> getWorkingSchedules(){ return this.workingSchedules; }
	public List<String> getDynamicCourseIDs(){ return this.dynamicCoursesIDs; }
	
	
	
	/********************************************************************
	 * Method: storeDynamicCourses()
	 * Purpose: store all dynamic courses to memory
	/*******************************************************************/
	public void storeDynamicCourses(int term){
		
		try {
			
			DefaultHttpClient client = new DefaultHttpClient();
			
			// URLs
			String begin = "https://jweb.kettering.edu/cku1/bwckschd.p_get_crse_unsec?term_in=" + term + "&sel_subj=dummy&sel_day=dummy&sel_schd=dummy&sel_insm=dummy&sel_camp=dummy&sel_levl=dummy&sel_sess=dummy&sel_instr=dummy&sel_ptrm=dummy&sel_attr=dummy&sel_subj=";
			String end = "&sel_crse=&sel_title=&sel_from_cred=&sel_to_cred=&sel_instr=%25&begin_hh=0&begin_mi=0&begin_ap=0&end_hh=0&end_mi=0&end_ap=a";
			
			// 32 Subjects
			List<String> subjects = new ArrayList<String>();
			subjects.add("ACCT"); subjects.add("BIOL"); subjects.add("BUSN"); subjects.add("CHME"); subjects.add("CHEM"); subjects.add("CHN"); subjects.add("COMM"); subjects.add("CE"); subjects.add("CS"); subjects.add("ECON"); subjects.add("ECE"); subjects.add("EE"); subjects.add("ESL"); subjects.add("FINC"); subjects.add("GER"); subjects.add("HMGT"); subjects.add("HIST"); subjects.add("HUMN"); subjects.add("IME"); subjects.add("ISYS"); subjects.add("MFGO"); subjects.add("LS"); subjects.add("LIT"); subjects.add("MGMT"); subjects.add("MRKT"); subjects.add("MATH"); subjects.add("MECH"); subjects.add("MEDI"); subjects.add("ORTN"); subjects.add("PHIL"); subjects.add("PHYS"); subjects.add("SSCI"); subjects.add("SOC");
			
			
			for(int i = 0; i < subjects.size(); i++){
			
				// Connect
				HttpGet dynamicGet = new HttpGet(begin + subjects.get(i) + end);
				HttpResponse response = client.execute(dynamicGet);
				
				String html = HTMLParser.parse(response);
				
				// Write to file
				PrintWriter printer = new PrintWriter("artifacts/Subjects/" + subjects.get(i) + ".html");
				printer.print(html);	    	
				printer.close();
				
				System.out.println("Successfully stored \"" + subjects.get(i) + ".html\"");
				
				
				Elements mainContainer = Jsoup.parse(html).getElementsByClass("datadisplaytable");
				
				// Valid container ?
				if(mainContainer.size() > 0 && mainContainer.get(0).getElementsByTag("tbody").size() > 0){
				
					
					Elements coursesOffered = mainContainer.get(0).getElementsByTag("tbody").get(0).children();
					
					// Regular expression to get credits (no HTML consistency)
					Pattern p = Pattern.compile("^.+([0-9].[0-9][0-9][0-9])\\sCredits.*");
					
					for (int j = 0; j < coursesOffered.size()/2; j++) {
						
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
							
							// Course doesn't already exists
							if(this.dynamicCourses.get(courseID) == null){
								
								// Create new
								List<Course> newList = new ArrayList<Course>();
								newList.add(currentCourse);
								this.dynamicCourses.put(courseID, newList);
								this.dynamicCoursesIDs.add(courseID);
							}
							
							// Already exists
							else this.dynamicCourses.get(courseID).add(currentCourse);
							
						}
					}	
				}	
			}
		}
		
		catch(Exception e){ e.printStackTrace(); }
	}
	
	
	/********************************************************************
	 * Method: checkPermutations
	 * Purpose: goes through all possible outcomes of a class set
	/*******************************************************************/
	private void checkPermutations(String json, List<List<Course>> sets, int pos, List<List<Course>> workingCourses) {
	    
		// Base case
		if (pos == sets.size()) {

			try{
				
				List<Course> testCourses = new ArrayList<Course>();
				
				// Get JSON Courses
				json = json.substring(0, json.length()-1); json += "]}";
				JSONCourses jsonCourses = new Gson().fromJson(json, JSONCourses.class);
		        
				// Add JSON to testCourses
		        for (JSONCourse currentCourse : jsonCourses.getCourses()) testCourses.add(sets.get(currentCourse.getSet()).get(currentCourse.getIndex()));
		        
		        // Test valid course combination
		        if(this.testClasses(testCourses)) workingCourses.add(testCourses);
			}
			
			catch(Exception e){}
	    } 
	    
		// Fun stuff
	    else for (int i = 0; i < sets.get(pos).size(); i++) checkPermutations(json + "{\"set\":" + pos + ",\"index\":" + i +"},", sets, pos + 1, workingCourses);		
	}
	
	
	/********************************************************************
	 * Method: setClassOptions
	 * Purpose: sets class options with given class ID's
	/*******************************************************************/
	public void setClassOptions(List<String> givenIDs){
		
		
		List<List<Course>> choices = new ArrayList<List<Course>>();
		List<List<Course>> results = new ArrayList<List<Course>>();
		
		// Get givenIDs
		for(String givenID : givenIDs){
			
			// Try to add course
			try{ choices.add(this.dynamicCourses.get(givenID)); }
			
			catch(Exception e){ 
				this.workingSchedules = results; 
				System.out.println(givenID + " is not in our records."); 
				return;
			}
		}
		
		// Permutations
		this.checkPermutations("{\"jsonCourses\": [", choices, 0, results);
		this.workingSchedules = results;
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
			TimeBlock newBlock = TimeBlock.convertToTimeBlock(current.getTime());
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