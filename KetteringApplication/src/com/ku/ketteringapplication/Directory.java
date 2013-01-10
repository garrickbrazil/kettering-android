package com.ku.ketteringapplication;

import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.google.gson.Gson;

/********************************************************************
 * Class: Directory
 * Purpose: search directory of students and faculty
/*******************************************************************/
public class Directory {

	// Properties
	private StudentQuery studentQuery;
	private FacultyQuery facultyQuery;
	private String type;
	private String search;
	
	/********************************************************************
	 * Constructor: Directory
	 * Purpose: create a default directory object
	/*******************************************************************/
	public Directory(){
		
		// Initialize
		this.type = "";
		this.search = "";
		
	}
		
	
	/********************************************************************
	 * Method: search()
	 * Purpose: search for given parameters
	/*******************************************************************/
	public boolean search(String search, String type){
		
		// Properties
		this.type = type.toLowerCase();
		this.search = search;
		
		try{
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet jsonGet = new HttpGet("https://drupal.kettering.edu:8443/kumobile/rest/directory/search/query/" + this.search.replaceAll("\\s", "+") + "/type/" + this.type);
			
			// Connect
			String json = HTMLParser.parse(client.execute(jsonGet));
			
			// Student or Faculty ?
			if(this.type.equals("student")) this.studentQuery = new Gson().fromJson(json, StudentQuery.class);
			else if (this.type.equals("faculty")) this.facultyQuery = new Gson().fromJson(json, FacultyQuery.class); 
			else return false;
			
			return true;
		}
		
		catch (Exception e){ return false; }
	}

	
	
	/********************************************************************
	 * Accessors: getType, getSearch, getFacultyQuery, getStudentQuery
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getType(){ return this.type; }
	public String getSearch() { return this.search; }
	public FacultyQuery getFacultyQuery() { return this.facultyQuery; }
	public StudentQuery getStudentQuery() { return this.studentQuery; }
	
	
}

/********************************************************************
 * Class: StudentQuery
 * Purpose: holds JSON information for a student query
/*******************************************************************/
class StudentQuery{
	
	// Properties
	private boolean resultTruncated;
	private boolean containsSecureData;
	private List<StudentResult> entries;
	
	
	/********************************************************************
	 * Accessors: getResultTruncated, getContainsSecureData, getEntries
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public boolean getResultTruncated(){ return this.resultTruncated; }
	public boolean getContainsSecureData(){ return this.containsSecureData; }
	public List<StudentResult> getEntries(){ return this.entries; }
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){ 
		
		String entryStr = "";
		
		// Add entries
		if (this.entries.size() != 0) for(int i = 0; i < this.entries.size(); i++) entryStr += this.entries.get(i).toString() + "\n";
		
		else entryStr = "No results.\n";
		
		return entryStr;
	}
}


/********************************************************************
 * Class: FacultyQuery
 * Purpose: holds JSON information for a faculty query
/*******************************************************************/
class FacultyQuery{
	
	// Properties
	private boolean resultTruncated;
	private boolean containsSecureData;
	private List<FacultyResult> entries;
	
	
	/********************************************************************
	 * Accessors: getResultTruncated, getContainsSecureData, getEntries
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public boolean getResultTruncated(){ return this.resultTruncated; }
	public boolean getContainsSecureData(){ return this.containsSecureData; }
	public List<FacultyResult> getEntries(){ return this.entries; }
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){ 
		
		String entryStr = "";
		
		// Add entries
		if (this.entries.size() != 0) for(int i = 0; i < this.entries.size(); i++) entryStr += this.entries.get(i).toString() + "\n";
		
		else entryStr = "No results.\n";
		
		return entryStr;
	}
	
}

/********************************************************************
 * Class: StudentResult
 * Purpose: holds JSON information a single student result
/*******************************************************************/
class StudentResult{
	
	// Properties
	private String type;
	private String firstName;
	private String lastName;
	private String prefFirstName;
	private String email;
	
	
	/********************************************************************
	 * Accessors: getType, getFirstName, getLastName, getPrefFirstName, getEmail
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getType(){ return this.type; }
	public String getFirstName(){ return this.firstName; }
	public String getLastName(){ return this.lastName; }
	public String getPrefFirstName(){ return this.prefFirstName; }
	public String getEmail(){ return this.email; }	
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		String studentStr = "";
		
		// Add valid properties
		if(this.firstName != null) studentStr += this.firstName + " ";
		if(this.lastName != null) studentStr += this.lastName + " ";
		if(this.email != null) studentStr += this.email;
		
		return studentStr; 
	}
	
}



/********************************************************************
 * Class: FacultyResult
 * Purpose: holds JSON information a single faculty result
/*******************************************************************/
class FacultyResult{
	
	// Properties
	private String type;
	private String firstName;
	private String lastName;
	private String pidm;
	private List<FacultyAddress> address;
	private List<FacultyPhone> phone;
	private String email;
	
	
	/********************************************************************
	 * Accessors: getType, getFirstName, getLastName, getPidm, getAddress, 
	 * 		getPhone, getEmail
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getType(){ return this.type; }
	public String getFirstName(){ return this.firstName; }
	public String getLastName(){ return this.lastName; }
	public String getPidm(){ return this.pidm; }
	public List<FacultyAddress> getAddress(){ return this.address; }
	public List<FacultyPhone> getPhone(){ return this.phone; }
	public String getEmail(){ return this.email; }	
	
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){ 
		
		String facultyStr = "";
		
		// Add valid properties
		if(this.firstName != null) facultyStr += this.firstName + " ";
		if(this.lastName != null) facultyStr += this.lastName + " ";
		
		// Add valid lists
		for(int i = 0; i < this.address.size(); i++) facultyStr += this.address.get(i).toString() +  " ";
		for(int i = 0; i < this.phone.size(); i++) facultyStr += this.phone.get(i).toString() + " ";
		
		// Add valid property
		if(this.email != null) facultyStr += this.email;
		
		return facultyStr;
		
	}
}


/********************************************************************
 * Class: FacultyAddress
 * Purpose: holds JSON information a faculy's address
/*******************************************************************/
class FacultyAddress{
	
	// Properties
	private String streetOne;
	private String streetTwo;
	private String streetThree;
	private String city;

	
	/********************************************************************
	 * Accessors: getStreetOne, getStreetTwo, getStreetThree, getCity
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getStreetOne(){ return this.streetOne; }
	public String getStreetTwo(){ return this.streetTwo; }
	public String getStreetThree(){ return this.streetThree; }
	public String getCity(){ return this.city; }
	
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){ 
		
		String addressStr = "";
		
		// Add valid properties
		//if(this.streetOne != null) addressStr += this.streetOne + " ";
		if(this.streetTwo != null) addressStr += this.streetTwo + " ";
		if(this.streetThree != null) addressStr += this.streetThree + " ";
		//if(this.city != null) addressStr += this.city + " ";
		
		return addressStr;
	}
	
}

/********************************************************************
 * Class: FacultyPhone
 * Purpose: holds JSON information a faculy's phone
/*******************************************************************/
class FacultyPhone{
	
	// Properties
	private String area;
	private String number;
	private String extn;
	
	
	/********************************************************************
	 * Accessors: getArea, getNumber, getExtn
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getArea(){ return this.area; }
	public String getNumber(){ return this.number; }
	public String getExtn(){ return this.extn; }
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		String phoneStr = "";
		
		// Add valid properties
		if(this.area != null) phoneStr += this.area + " " ;
		if(this.number != null) phoneStr += this.number + " ";
		if(this.extn != null) phoneStr += this.extn + " ";
		
		return phoneStr;
		
	}
}