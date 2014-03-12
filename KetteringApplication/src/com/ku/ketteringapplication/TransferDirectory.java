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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

/********************************************************************
 * Class: TransferDirectory
 * Purpose: search transfer courses from various universities
/***********************************************f********************/
public class TransferDirectory {
	
	// Properties
	private TransferCourses resultsByCourse;
	private TransferCourses resultsByCollege;
	private DefaultHttpClient client;
	private CollegeOptions options;
	private List<String> optionsStr;
	private String searchStr;
	private boolean isListLoaded;
	public final int BYCOURSE = 0, BYCOLLEGE = 1;
	private int collegeListPosition;
	private int type;
	
	
	/********************************************************************
	 * Constructor: TransferDirectory
	 * Purpose: create default transfer directory object
	/*******************************************************************/
	public TransferDirectory(){
		
		this.client = new DefaultHttpClient();
		this.resultsByCourse = new TransferCourses();
		this.resultsByCollege = new TransferCourses();
		this.optionsStr = new ArrayList<String>();
		this.isListLoaded = false;
		this.type = BYCOURSE;
		this.collegeListPosition = 0;
		this.searchStr = "";
	}
	
	/********************************************************************
	 * Accessors
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public List<TransferCourse> getResultsByCourse(){ return this.resultsByCourse.getCourses(); }
	public List<TransferCourse> getResultsByCollege(){ return this.resultsByCollege.getCourses(); }
	public List<CollegeOption> getOptions(){ return this.options.getOptions(); }
	public boolean getIsListLoaded(){ return this.isListLoaded; }
	public List<String> getOptionsString(){ return this.optionsStr; }
	public int getType(){ return this.type; }
	public int getCollegeListPosition(){ return this.collegeListPosition; }
	public String getSearchString(){ return this.searchStr; }
	public void setType(int type){ this.type = type; }
	
	
	public String getCodeByName(String name){
		
		for(CollegeOption option : this.options.getOptions()) if (option.getName().equals(name)) return option.getCode();
		
		return "";
	}
	
	
	public boolean storeCollegeList(){
		
		try{
			
			// Execute
			HttpGet listGet = new HttpGet("https://drupal.kettering.edu:8443/kumobile/rest/ces/college/list");
			HttpResponse response = this.client.execute(listGet);
			String json = HTMLParser.parse(response);
			
			// Set JSON
			this.options =  new Gson().fromJson(json, CollegeOptions.class);
			this.isListLoaded = true;
			
			for(CollegeOption option : this.options.getOptions()) this.optionsStr.add(option.getName());
			return true;
		}
		
		catch(Exception e) { 
			this.options = new CollegeOptions();
			this.optionsStr = new ArrayList<String>();
			this.isListLoaded = false; 
			return false; 
		}
		
	}
	
	/********************************************************************
	 * Method: searchByCollege()
	 * Purpose: search by college
	/*******************************************************************/
	public boolean searchByCollege(String code, int collegeListPosition){
		
		this.resultsByCollege = new TransferCourses();
		this.collegeListPosition = collegeListPosition;
		
		try{
			
			// Execute
			HttpGet transferGet = new HttpGet("https://drupal.kettering.edu:8443/kumobile/rest/ces/credits/sbgi/" + code);
			HttpResponse response = this.client.execute(transferGet);	
			String json = HTMLParser.parse(response);
			
			// JSON
			this.resultsByCollege =  new Gson().fromJson(json, TransferCourses.class);	
			for(TransferCourse course : this.resultsByCollege.getCourses()) course.fixWhiteSpace();
			
			return true;
		}
		catch (Exception e){ this.resultsByCollege = new TransferCourses(); return false; }	
	}
	
	
	
	/********************************************************************
	 * Method: searchByCourse()
	 * Purpose: search by course
	/*******************************************************************/
	public boolean searchByCourse(String course){
		
		this.resultsByCourse = new TransferCourses();
		this.searchStr = course;
		
		try{
			
			HttpPost searchCourse = new HttpPost("http://asection.ketteringdeltachi.org/krunal/kutrans/results.php");
			
	        // Parameters
	        List <NameValuePair> parameters = new ArrayList <NameValuePair>();
	        parameters.add(new BasicNameValuePair("input", course));
	        searchCourse.setEntity(new UrlEncodedFormEntity(parameters));
	        
	        
	        // Execute
	        HttpResponse response = this.client.execute(searchCourse);
	        String html = HTMLParser.parse(response);
	      
			
			Elements table = Jsoup.parse(html).getElementsByTag("tbody");
			
			// Correct format
			if(table.size() > 0 && table.get(0).getElementsByTag("tr").size() > 0) {
				
				Elements rows = table.get(0).getElementsByTag("tr");
				rows.remove(0);
				
				
				// All Rows
				for(Element row : rows){
					
					if(row.getElementsByTag("td").size() == 6){
						
						Elements td = row.getElementsByTag("td");
						
						// Properties
						String transID = td.get(0).text();
						String college = td.get(1).text();
						String kuID = td.get(2).text();
						String kuTitle = td.get(3).text();
						String credits = td.get(4).text();
						String comment = td.get(5).text();
						
						// Add
						this.resultsByCourse.getCourses().add(new TransferCourse(college, transID, kuID, kuTitle, credits, comment));
						
					}
				}
				
			}
	        
			return true;
		}
		
		catch(Exception e){ this.resultsByCourse = new TransferCourses(); return false;}
	}
	
}



/********************************************************************
 * Class: TransferCourses
 * Purpose: holds transfer courses
/*******************************************************************/
class TransferCourses{
	
	// Properties
	private List<TransferCourse> entries;
	
	
	public TransferCourses(){
		this.entries = new ArrayList<TransferCourse>();
	}
	
	
	// Accessors
	public List<TransferCourse> getCourses(){ return this.entries; }
}


/********************************************************************
 * Class: TransferCourse
 * Purpose: holds a single transfer course
/*******************************************************************/
class TransferCourse{
	
	// Properties
	private String institution;
	private String trnscrse;
	private String kucourse;
	private String kucoursetitle;
	private String credits;
	private String sbgi;
	private String comment;
	
	
	/********************************************************************
	 * Constructor: TransferCourse
	 * Purpose: holds a single transfer course
	/*******************************************************************/
	public TransferCourse(String college, String transID, String kuID, String kuTitle, String credits, String comment){
		
		// Set properties
		this.institution = college;
		this.trnscrse = transID;
		this.kucourse = kuID;
		this.kucoursetitle = kuTitle;
		this.credits = credits;
		this.comment = comment;
	}
	
	
	// Accessors
	public String getInstitution(){ return this.institution; }
	public String getTitle(){ return this.kucoursetitle; }
	public String getCredits(){ return this.credits; }
	public String getComment(){ return this.comment; }
	public String getCode(){ return this.sbgi; }
	public String getKUCourseID(){ return this.kucourse; }
	public String getTransferID(){ return this.trnscrse; }
	
	
	/********************************************************************
	 * Method: fixWhiteSpace
	 * Purpose: removes unnecessary white space
	/*******************************************************************/
	public void fixWhiteSpace(){
		
		if(this.institution!=null) this.institution = this.institution.replaceAll("\\d\\d\\d\\d\\d\\d\\s\\s+", "");
		if(this.trnscrse!=null) this.trnscrse = this.trnscrse.replaceAll("\\s\\s+", "");
		if(this.kucourse!=null) this.kucourse = this.kucourse.replaceAll("\\s\\s+", "");
		if(this.kucoursetitle!=null) this.kucoursetitle = this.kucoursetitle.replaceAll("\\s\\s+", "");
		if(this.credits!=null) this.credits = this.credits.replaceAll("\\s\\s+", "");
		if(this.sbgi!=null) this.sbgi = this.sbgi.replaceAll("\\s\\s+", "");
		if(this.comment!=null) this.comment = this.comment.replaceAll("\\s\\s+", "");
	}
	
	
	
	/********************************************************************
	 * Method: toString()
	 * Purpose: converts the data into a formatted string
	/*******************************************************************/
	public String toString(){
		
		String transStr = "";
		
		
		// Add valid properties
		if(this.getInstitution() != null) transStr += "Institution: " + this.getInstitution() + " ";  
		if(this.getTitle() != null) transStr += "Title: " + this.getTitle() + " ";
		if(this.getCredits() != null) transStr += "Credits: " + this.getCredits() + " ";
		if(this.getComment() != null) transStr += "Comment: " + this.getComment() + " ";
		if(this.getCode() != null) transStr += "Code: " + this.getCode() + " ";
		if(this.getKUCourseID() != null) transStr += "KUCourseID: " + this.getKUCourseID() + " ";
		if(this.getTransferID() != null) transStr += "TransferID: " + this.getTransferID() + " ";
		
		return transStr;
	}	
}


/********************************************************************
 * Class: CollegeOptions
 * Purpose: holds college options
/*******************************************************************/
class CollegeOptions{
	
	// Properties
	private List<CollegeOption> entries;
	
	
	/********************************************************************
	 * Constructor: CollegeOptions
	 * Purpose: creates college options
	/*******************************************************************/
	public CollegeOptions(){
		this.entries = new ArrayList<CollegeOption>();
	}
	
	// Accessors
	public List<CollegeOption> getOptions(){ return this.entries; }
}


/********************************************************************
 * Class: CollegeOption
 * Purpose: holds a single college option
/*******************************************************************/
class CollegeOption{
	
	// Properties
	private String ku_trnscrse_sbgi_code;
	private String stvsbgi_desc;
	
	// Accessors
	public String getCode(){ return this.ku_trnscrse_sbgi_code; }
	public String getName(){ return this.stvsbgi_desc; }
	
	/********************************************************************
	 * Method: toString()
	 * Purpose: converts the data into a formatted string
	/*******************************************************************/
	public String toString(){
		return this.getName();
	}
	
}
