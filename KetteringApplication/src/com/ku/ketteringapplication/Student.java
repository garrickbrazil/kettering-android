package com.ku.ketteringapplication;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/********************************************************************
 * Class: Student
 * Purpose: Hold all student data and connections
/*******************************************************************/
public class Student {
	
	// Properties
	private String username;
	private String password;
	private String transcript;
	private boolean loggedIn;
	private DefaultHttpClient clientBanner;
	private DefaultHttpClient clientBlackboard;
	private List<Course> courses;
	private List<CurrentGrade> currentGrades;
	private List<FinalGrade> finalGrades;
	private List<MidtermGrade> midtermGrades;
	private UndergradSummary undergradSummary;
	private AccountTotal accountTotal;
	private String scheduleHTML;
	private boolean scheduleLoaded;
	private boolean currentGradesLoaded;
	private boolean finalGradesLoaded;
	private boolean midtermGradesLoaded;
	private int gradesPage;
	private List<TermOption> midtermTermList;
	private List<TermOption> finalTermList;
	private boolean accountLoaded;
	private boolean midtermListLoaded;
	private boolean finalListLoaded;
	private int finalSelectedTerm;
	private int midtermSelectedTerm;
	
	/********************************************************************
	 * Constructor: Student()
	 * Purpose: create default student object
	/*******************************************************************/
	public Student(){
		
		// Initialize
		this.username = "";
		this.password = "";
		this.loggedIn = false;
		this.scheduleLoaded = false;
		this.clientBanner = new DefaultHttpClient();
		this.clientBlackboard = new DefaultHttpClient();
		this.courses = new ArrayList<Course>();
		this.currentGrades = new ArrayList<CurrentGrade>();
		this.finalGrades = new ArrayList<FinalGrade>();
		this.midtermGrades = new ArrayList<MidtermGrade>();
		this.finalTermList = new ArrayList<TermOption>();
		this.midtermTermList = new ArrayList<TermOption>();
		this.scheduleHTML = "";
		this.gradesPage = 0;
		this.accountLoaded = false;
		this.currentGradesLoaded = false;
		this.finalGradesLoaded = false;
		this.midtermGradesLoaded = false;
		this.scheduleLoaded = false;
		this.finalListLoaded = false;
		this.midtermListLoaded = false;
		this.finalSelectedTerm = -1;
		this.midtermSelectedTerm = -1;
	}
	
	
		
	/********************************************************************
	 * Accessors
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getUsername() { return this.username; }
	public String getPassword(){ return this.password; }
	public List<Course> getCourses(){ return this.courses; }
	public List<CurrentGrade> getCurrentGrades(){ return this.currentGrades; }
	public String getTranscript() { return this.transcript; }
	public List<FinalGrade> getFinalGrades(){ return this.finalGrades; }
	public List<MidtermGrade> getMidtermGrades(){ return this.midtermGrades; }
	public UndergradSummary getUndergradSummary(){ return this.undergradSummary; }
	public AccountTotal getAccountTotal(){ return this.accountTotal; }
	public boolean getLoggedIn(){ return this.loggedIn; }
	public String getScheduleHTML(){ return this.scheduleHTML; }
	public boolean getScheduleLoaded(){ return this.scheduleLoaded; }
	public int getGradesPage(){ return this.gradesPage; }
	public boolean getCurrentGradesLoaded(){ return this.currentGradesLoaded; }
	public boolean getFinalGradesLoaded(){ return this.finalGradesLoaded; }
	public boolean getMidtermGradesLoaded(){ return this.midtermGradesLoaded; }
	public List<TermOption> getMidtermList(){ return this.midtermTermList; }
	public List<TermOption> getFinalTermList(){ return this.finalTermList; }
	public boolean getFinalListLoaded(){ return this.finalListLoaded; }
	public boolean getMidtermListLoaded(){ return this.midtermListLoaded; }
	public int getMidtermSelectedTerm(){ return this.midtermSelectedTerm; }
	public int getFinalSelectedTerm(){ return this.finalSelectedTerm; }
	public boolean getAccountLoaded(){ return this.accountLoaded; }
	
	
	/********************************************************************
	 * Mutators: setUsername(), setPassword()
	 * Purpose: set the corresponding data
	/*******************************************************************/
	public void setUsername(String username) { this.username = username; }
	public void setPassword(String password) { this.password = password; }
	public void setGradesPage(int gradesPage) { this.gradesPage = gradesPage; }
	
	
	/********************************************************************
	 * Method: loginBanner()
	 * Purpose: logs user into banner web
	/*******************************************************************/
	public boolean loginBanner(){
		
		try{
			
			HttpGet initialLoad = new HttpGet("http://jweb.kettering.edu/cku1/twbkwbis.P_ValLogin");
			HttpPost login = new HttpPost("http://jweb.kettering.edu/cku1/twbkwbis.P_ValLogin");
			boolean sessionSet = false;
			
			// Initial load
			HttpResponse response = this.clientBanner.execute(initialLoad);
	        HttpEntity entity = response.getEntity();
	        if (entity != null) entity.consumeContent();	
	        
	        // Parameters
	        List <NameValuePair> parameters = new ArrayList <NameValuePair>();
	        parameters.add(new BasicNameValuePair("sid", this.username));
	        parameters.add(new BasicNameValuePair("PIN", this.password));
	        login.setEntity(new UrlEncodedFormEntity(parameters));
	        
	        
	        // Login
	        response = this.clientBanner.execute(login);
	        entity = response.getEntity();
	        if (entity != null) entity.consumeContent();
	        
			
	        List<Cookie> cookies = this.clientBanner.getCookieStore().getCookies();
	        
	        // Check cookies
	        for (int i = 0; i < cookies.size(); i++) if (cookies.get(i).getName().equals("SESSID") && !cookies.get(i).getValue().equals("")) sessionSet = true;
	        
	        // Success
	        this.loggedIn = sessionSet;
	        return sessionSet;
		}
			
		catch(Exception e){  e.printStackTrace(); return false; }
	}
	
	
	/********************************************************************
	 * Method: loginBlackboard()
	 * Purpose: logs user into blackboard
	/*******************************************************************/
	public boolean loginBlackboard(){
		
		try{
			
			HttpGet initialLoad = new HttpGet("https://blackboard.kettering.edu/webapps/login/");
			HttpPost login = new HttpPost("https://blackboard.kettering.edu/webapps/login/");
			
			// Parameters
			List <NameValuePair> parameters = new ArrayList <NameValuePair>();
	        parameters.add(new BasicNameValuePair("user_id", this.username));
	        parameters.add(new BasicNameValuePair("password", ""));
	        parameters.add(new BasicNameValuePair("login", "Login"));
	        parameters.add(new BasicNameValuePair("action", "login"));
	        parameters.add(new BasicNameValuePair("remote-user", ""));
	        parameters.add(new BasicNameValuePair("new_loc", "\u00C2\u00A0"));
	        parameters.add(new BasicNameValuePair("auth_type", ""));
	        parameters.add(new BasicNameValuePair("one_time_token", ""));
	        parameters.add(new BasicNameValuePair("encoded_pw", Base64.encodeToString(this.password.getBytes(), Base64.DEFAULT)));
	        parameters.add(new BasicNameValuePair("encoded_pw_unicode", ""));
	        login.setEntity(new UrlEncodedFormEntity(parameters));
			
	        
			// Initial load
	        HttpResponse response = this.clientBlackboard.execute(initialLoad);
	        HttpEntity entity = response.getEntity();
	        if (entity != null) entity.consumeContent();	
	        
	        
	        // Login
	        response = this.clientBlackboard.execute(login);
	        entity = response.getEntity();
	        if (entity != null) entity.consumeContent();
	        
	        return true;
		}
			
		catch(Exception e){ return false; }
	}
	
	
	
	/********************************************************************
	 * Method: storeTranscript()
	 * Purpose: stores transcript to memory
	/*******************************************************************/
	public void storeTranscript(){
		
		try{
			
			// Connect
			HttpGet transcript = new HttpGet("https://jweb.kettering.edu/cku1/ku_web_trans.view_transcript?tprt=SHRTRTC&levl=U");
			HttpResponse response = this.clientBanner.execute(transcript);
			
			String html = HTMLParser.parse(response);
			
			this.transcript = html;
		}
		
		catch(Exception e){ e.printStackTrace(); }
	}
	
	
	
	/********************************************************************
	 * Method: storeSchedule(int term)
	 * Purpose: stores schedule to memory
	/*******************************************************************/
	public boolean storeSchedule(String term){
		
		try{
			
			HttpGet schedule = new HttpGet("https://jweb.kettering.edu/cku1/bwskfshd.P_CrseSchdDetl?term_in=" + term);
		    
			// Execute
			HttpResponse response = this.clientBanner.execute(schedule);
			String html = HTMLParser.parse(response);
		    
			Document doc = Jsoup.parse(html);
			Elements classSchedules = doc.getElementsByClass("datadisplaytable");

						
			// Store Schedules
			for (int i = 0; i < classSchedules.size()/2; i++) {
				
					for (int index = 0, originalSize = classSchedules.get(i*2 + 1).getElementsByTag("tr").size() - 1; index < originalSize; index++){
						this.courses.add(new Course(classSchedules.get(i*2), classSchedules.get(i*2 + 1).getElementsByTag("tr").get(index + 1)));
					}
			}
			
			/*
			schedule = new HttpGet("https://jweb.kettering.edu/cku1/bwskfshd.P_CrseSchdDetl?term_in=" + term);
		    
			// Execute
			response = this.clientBanner.execute(schedule);
			html = HTMLParser.parse(response);
		    
			doc = Jsoup.parse(html);
			classSchedules = doc.getElementsByClass("datadisplaytable");

			if(classSchedules.size() > 0) this.scheduleHTML = classSchedules.get(0).toString();
			*/
			
			
			this.scheduleLoaded = true;
			return true;

		}
		
		catch(Exception e){ this.courses = new ArrayList<Course>(); this.scheduleLoaded = false; return false; }
				
	}
	
	
	
	/********************************************************************
	 * Method: storeCurrentGrades()
	 * Purpose: stores grades to memory
	/*******************************************************************/
	public boolean storeCurrentGrades(){
		
		try{
			
			HttpGet grade = new HttpGet("https://blackboard.kettering.edu/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_104_1&forwardUrl=detach_module%2F_573_1%2F");
			
			// Execute
		    HttpResponse response = this.clientBlackboard.execute(grade);
			String html = HTMLParser.parse(response);
			
			// Class grades
			Elements classGrades = Jsoup.parse(html).getElementsByTag("td");
			if(classGrades.size() > 0) classGrades.remove(classGrades.size() -1);
			
			// Store grades
			for (int i = 0; i < classGrades.size()/2; i++) {
				
				// Check size
				if(classGrades.get(i*2 +1).childNodes().size() > 0 && classGrades.get(i*2 +1).childNode(0).childNodes().size() > 0){
					
					// Parameters
					String gradeLink = "https://blackboard.kettering.edu" + classGrades.get(i*2+1).childNode(0).childNode(0).attr("href");
					String className = classGrades.get(i*2).text();
					String titleTotal = classGrades.get(i*2+1).child(0).child(0).text();
					boolean valid = className.contains("WINTER") || className.contains("SPRING") || className.contains("SUMMER") || className.contains("FALL"); 

					// Fix name format
					if(className.split(":\\s").length > 2){
						String[] split = className.split(":\\s");
						className = "";
						//for(int j = 1; j < split.length; j++) 
						className += split[2] + " ";
					}
					
					className = className.split("\\s-\\s*+")[0];
					
					// Create
					CurrentGrade current = new CurrentGrade(className, gradeLink, titleTotal);
					if (valid) this.currentGrades.add(current);
				}
			}
			
			this.currentGradesLoaded = true;
			return true;
		}
		
		catch(Exception e){ e.printStackTrace(); this.currentGradesLoaded = true; return false;}
	}

	
	/********************************************************************
	 * Method: storeGradeItems
	 * Purpose: store grade items for a course
	/*******************************************************************/
	public boolean storeGradeItem(int index){

		try{

			// Details
			HttpGet gradeDetail = new HttpGet(this.currentGrades.get(index).getLink());
			HttpResponse gradeResponse = this.clientBlackboard.execute(gradeDetail);
			
			return this.currentGrades.get(index).storeGradeItems(HTMLParser.parse(gradeResponse));
		}
		
		catch(Exception e){ e.printStackTrace(); return false; }
		
	}
	
	/********************************************************************
	 * Method: storeFinalTermList()
	 * Purpose: store final term list
	/*******************************************************************/
	public boolean storeFinalTermList(){
		
		try {
			
			// Connect
			HttpGet listGet = new HttpGet("https://jweb.kettering.edu/cku1/wbwskogrd.P_ViewTermGrde");
			HttpResponse response = this.clientBanner.execute(listGet);
			
			String html = HTMLParser.parse(response);
			
			Elements list = Jsoup.parse(html).getElementsByTag("select");
			
			// Correct amount ?
			if (list.size() > 0 && 	list.get(0).children().size() > 0){
				
				Elements options = list.get(0).children();
				
				for(Element option : options) this.finalTermList.add(new TermOption(option.text(), option.attr("value")));
			
				this.finalListLoaded = true;
				return true;
			}
			
			else{
				
				this.finalListLoaded = false;
				return false;
			}
		}
		
		catch(Exception e){
			
			this.finalListLoaded = false;
			e.printStackTrace(); 
			return false;
		}

	}
	

	/********************************************************************
	 * Method: storeMidtermList()
	 * Purpose: stores available list of midterm terms and years
	/*******************************************************************/
	public boolean storeMidtermList(){
		
		try {
			
			// Connect
			HttpGet listGet = new HttpGet("https://jweb.kettering.edu/cku1/bwskmgrd.p_write_term_selection");
			HttpResponse response = this.clientBanner.execute(listGet);
			
			String html = HTMLParser.parse(response);
			
			Elements list = Jsoup.parse(html).getElementsByTag("select");
			
			// Correct amount ?
			if (list.size() > 0 && 	list.get(0).children().size() > 0){
				
				Elements options = list.get(0).children();
				
				for(Element option : options) this.midtermTermList.add(new TermOption(option.text(), option.attr("value")));
				
				this.midtermListLoaded = true;
				return true;
			}
			
			else {
				this.midtermListLoaded = false;
				return false;
			}
		}
		
		catch(Exception e){ 
		
			e.printStackTrace(); 
			this.midtermListLoaded = false;
			return false; 
		}

	}
	
	/********************************************************************
	 * Method: storeFinalGrades()
	 * Purpose: store final grades to memory
	/*******************************************************************/
	public boolean storeFinalGrades(String term, int pageIndex){
		
		try {
			
			this.finalSelectedTerm = pageIndex;
			this.finalGrades = new ArrayList<FinalGrade>();
			
			// Connect
			HttpGet finalGet = new HttpGet("https://jweb.kettering.edu/cku1/wbwskogrd.P_ViewGrde?term_in=" + term + "&inam=on&snam=on&sgid=on");
			HttpResponse response = this.clientBanner.execute(finalGet);
			
			String html = HTMLParser.parse(response);
			
			Elements tables = Jsoup.parse(html).getElementsByClass("datadisplaytable");
			
			// Correct amount ?
			if (tables.size() == 3 && tables.get(2).getElementsByTag("tbody").size() > 0 && tables.get(2).getElementsByTag("tbody").get(0).children().size() == 5 && tables.get(1).getElementsByTag("tbody").size() > 0){
				
				// Store undergrad summary
				this.undergradSummary = new UndergradSummary(tables.get(2).getElementsByTag("tbody").get(0).children());
				
				Elements courses = tables.get(1).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				
				// Titles remove
				if (courses.size() > 0) courses.remove(0);
				
				// Add final grades
				for(int i = 0; i < courses.size();  i++) if(courses.get(i).getElementsByTag("td").size() == 12) this.finalGrades.add(new FinalGrade(courses.get(i).getElementsByTag("td")));
			
				return true;
			}
			
			else return false;
		}
		
		catch(Exception e){ e.printStackTrace(); return false; }

	}
	

	/********************************************************************
	 * Method: storeMidtermGrades()
	 * Purpose: store midterm grades to memory
	/*******************************************************************/
	public boolean storeMidtermGrades(String term, int pageIndex){
		
		try {
			
			this.midtermSelectedTerm = pageIndex;
			this.midtermGrades = new ArrayList<MidtermGrade>();
			
			// Connect
			HttpGet midtermGet = new HttpGet("https://jweb.kettering.edu/cku1/bwskmgrd.p_write_midterm_grades?term_in=" + term);
			HttpResponse response = this.clientBanner.execute(midtermGet);
			
			String html = HTMLParser.parse(response);
			
			Elements tables = Jsoup.parse(html).getElementsByClass("datadisplaytable");
			
			// Correct amount ?
			if (tables.size() >= 2 && tables.get(1).getElementsByTag("tbody").size() > 0){
				
				Elements courses = tables.get(1).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				
				// Titles remove
				if(courses.size() > 0) courses.remove(0);
				
				// Store midterm grades 
				for(int i = 0; i < courses.size();  i++) if(courses.get(i).getElementsByTag("td").size() >= 8) this.midtermGrades.add(new MidtermGrade(courses.get(i).getElementsByTag("td")));
			
				return true;
			}
			
			else return false;
		}
		
		catch(Exception e){ e.printStackTrace(); return false; }
	}
	
	
	/********************************************************************
	 * Method: storeAccount()
	 * Purpose: store financial account information to memory
	/*******************************************************************/
	public boolean storeAccount(){
		
		try {
			
			// Connect
			HttpGet accountGet = new HttpGet("https://jweb.kettering.edu/cku1/bwskoacc.P_ViewAcctTotal");
			HttpResponse response = this.clientBanner.execute(accountGet);
			
			String html = HTMLParser.parse(response);
			
			Elements elements = Jsoup.parse(html).getElementsByClass("datadisplaytable");
		
			// Set account info
			if(elements.size() > 0 && elements.get(0).getElementsByTag("tbody").size() > 0 && elements.get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").size() > 6){
				this.accountTotal = new AccountTotal(elements.get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr"));
				
				this.accountLoaded = true;
				return true;
			}
			
			else{
				
				this.accountLoaded = false;
				return false;
			}
			
		}
		
		catch(Exception e){ 
			e.printStackTrace();
			return false;
		}
		
	}

	
	/********************************************************************
	 * Method: getFinalListString
	 * Purpose: converts final term list into String list
	/*******************************************************************/
	public List<String> getFinalListString(){
		
		List<String> finalList = new ArrayList<String>();
		
		for (TermOption term : finalTermList) finalList.add(term.getName());
		
		return finalList;
	}
	
	/********************************************************************
	 * Method: getMidtermListString
	 * Purpose: converts midterm term list into String list
	/*******************************************************************/
	public List<String> getMidtermListString(){
		
		List<String> midtermList = new ArrayList<String>();
		
		for (TermOption term : midtermTermList) midtermList.add(term.getName());
		
		return midtermList;
	}
	
	/********************************************************************
	 * Method: getFinalCodeByName
	 * Purpose: gets the correct final term code for the terms name
	/*******************************************************************/
	public String getFinalCodeByName(String name){
		
		String code = "";
		
		for (TermOption term : finalTermList) if(term.getName().equals(name)) code = term.getCode();
		
		return code;
	}
	
	/********************************************************************
	 * Method: getMidtermCodeByName
	 * Purpose: gets the correct Midterm code for the terms name
	/*******************************************************************/
	public String getMidtermCodeByName(String name){
		
		String code = "";
		
		for (TermOption term : midtermTermList) if(term.getName().equals(name)) code = term.getCode();
		
		return code;
	}
	
}



/********************************************************************
 * Class: TermOption
 * Purpose: stores an acceptable term for either final or midterm grades
/*******************************************************************/
class TermOption{
	
	private String name;
	private String code;
	
	public TermOption(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName(){ return this.name; }
	public String getCode(){ return this.code; }
	
}
