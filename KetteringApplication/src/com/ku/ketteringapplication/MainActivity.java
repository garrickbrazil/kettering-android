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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ku.ketteringapplication.R.drawable;

/********************************************************************
 * Class: MainActivity
 * Purpose: full program for Kettering Application
/*******************************************************************/
public class MainActivity extends Activity {
	
	// Objects
	private ProgressDialog load_dialog;
	private TransferDirectory trans_dir;
	private List<String> last_view;
	private Student student;
	private Events events;
	//private KUMenu ku_menu;
	private Directory dir;
	private News news;
	private Library lib;
	private DynamicCourses scheduler;
	private Context c;
	private ViewPager schedulerPager;
	private Spinner schedulerIndexer;
	private int orientation;
	private EventsTask eventTask;
	private NewsTask newsTask;
	private TransferTask transferTask;
	private DirectoryTask directoryTask;
	private NewsArticleTask newsArticleTask;
	private NewsPageTask newsPageTask;
	private LibraryPageTask libraryPageTask;
	private LibraryTask libraryTask;
	private GradeSingleTask gradeSingleTask;
	private GradesTask gradesTask;
	private FinalMidtermRetrival finalMidtermRetrival;
	private CourseSchedulerTask courseSchedulerTask;
	private AccountTask accountTask;
	private ScheduleTask scheduleTask;
	private TransferSearchTask transferSearchTask;
	private ViewScheduleTask viewScheduleTask;
	private EventsPageTask eventsPageTask;
	private EventsTask eventsTask;
	private StoreScheduleTermTask storeScheduleTermTask;
	private EventArticleTask eventArticleTask;
	private Login login;
	
	// Functions
	private Icon[] icons_all;
	private Icon[] icons_glob;
	
	
	// Constants
	private final String HOME = "HOME", 
			EVENTS = "EVENTS", NEWS = "NEWS", DIRECTORY = "DIRECTORY", 
			LIBRARY = "LIBRARY", GRADES = "GRADES", SCHEDULER = "SCHEDULER",
			BG_WEB_COLOR = "7092d8";
	
	/********************************************************************
	 * Method: onCreate
	 * Purpose: method for when application loads
	/*******************************************************************/
    public void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState); 
		c = this;
		// Initialize all objects
		this.trans_dir = new TransferDirectory();
        this.last_view = new ArrayList<String>();
		this.student = new Student();
        //this.ku_menu = new KUMenu();
        this.events = new Events();
        this.dir = new Directory();
        this.lib = new Library();
        this.news = new News();
        this.scheduler = new DynamicCourses();
        
        // Setup loading dialog
        this.load_dialog = new ProgressDialog(this);
        this.load_dialog.setCanceledOnTouchOutside(false);
        this.load_dialog.setIndeterminate(true);
        this.load_dialog.setTitle("In progress");
        this.load_dialog.setMessage("Loading...");
        this.load_dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
				if(eventTask != null) eventTask.cancel(true);
				if(newsTask != null) newsTask.cancel(true);
				if(transferTask != null) transferTask.cancel(true);
				if(directoryTask != null) directoryTask.cancel(true);
				if(newsArticleTask != null) newsArticleTask.cancel(true);
				if(newsPageTask != null) newsPageTask.cancel(true);
				if(libraryPageTask != null) libraryPageTask.cancel(true);
				if(libraryTask != null) libraryTask.cancel(true);
				if(gradeSingleTask != null) gradeSingleTask.cancel(true);
				if(gradesTask != null) gradesTask.cancel(true);
				if(finalMidtermRetrival != null) finalMidtermRetrival.cancel(true);
				if(courseSchedulerTask != null) courseSchedulerTask.cancel(true);
				if(accountTask != null) accountTask.cancel(true);
				if(scheduleTask != null) scheduleTask.cancel(true);
				if(transferSearchTask != null) transferSearchTask.cancel(true);
				if(viewScheduleTask != null) viewScheduleTask.cancel(true);
				if(eventsPageTask != null) eventsPageTask.cancel(true);
				if(eventsTask != null) eventsTask.cancel(true);
				if(storeScheduleTermTask != null) storeScheduleTermTask.cancel(true);
				if(eventArticleTask != null) eventArticleTask.cancel(true);
				if(login != null) login.cancel(true);
            }
        });
        
        // Setup icons for the grid
        Icon events = new Icon("Events", drawable.events, events_listener);
        Icon news = new Icon("News", drawable.news, news_listener);
        //Icon menu = new Icon("Menu", drawable.menu, menu_listener);
        Icon scheduler = new Icon("Scheduler", drawable.coursescheduler, scheduler_listener);
        Icon map = new Icon("Map", drawable.maps, maps_listener);
        Icon directory = new Icon("Directory", drawable.directory, directory_listener);
        Icon transfer = new Icon("Transfer", drawable.transfer, transfer_listener);
        Icon library = new Icon("Library", drawable.library, library_listener);
        Icon account = new Icon("Account", drawable.account, account_listener);
        Icon grades = new Icon("Grades", drawable.grades, grades_listener);
        Icon schedule = new Icon("Schedule", drawable.schedule, schedule_listener);
        
        // Global used when not logged in, all used when logged in
        Icon[] icons_all = {events, news, scheduler, map, directory, library, transfer, account, grades, schedule};
        Icon[] icons_glob = {events, news, scheduler, map, directory, library, transfer};
        
        this.icons_glob = icons_glob;
        this.icons_all = icons_all;
        
        homeScreen();
        this.orientation = getRequestedOrientation();
        
        /*
        EditText login = (EditText) findViewById(R.id.signinField);
        EditText pass = (EditText) findViewById(R.id.passwordField);
        
        login.setText("");
        pass.setText("");
        
        new Login().execute(this.student);
        */
        
    }
    

    public void cancel(){
    	
    	
    }
    
	/********************************************************************
	 * Method: onCreateOptionsMenu
	 * Purpose: method to inflate options menu 
	/*******************************************************************/
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	getMenuInflater().inflate(R.menu.activity_main, menu); return true; 
    }
    
    
	/********************************************************************
	 * Method: onBackPressed
	 * Purpose: catches back pressed event and moves to last view
	/*******************************************************************/
    @Override
    public void onBackPressed() {
    	
    	setRequestedOrientation(this.orientation);
    	
    	if(this.last_view.size() > 0){
    		
    		String last = last_view.get(0);
    	
    		// Move to latest state 
	    	//if(last.equals(MENU)) new KUMenuTask().execute(ku_menu); 
	    	if (last.equals(EVENTS)) eventTask = (EventsTask) new EventsTask().execute(events);
	    	else if (last.equals(NEWS)) newsTask = (NewsTask) new NewsTask().execute(news);
	    	else if (last.equals(DIRECTORY)){ setContentView(R.layout.directory); populateSearch(); }
	    	else if (last.equals(LIBRARY)){ setContentView(R.layout.library); populateLibrary(); }
	    	else if (last.equals(GRADES)){ setContentView(R.layout.grades); populateGrades(); }
	    	else if (last.equals(SCHEDULER)){ setContentView(R.layout.scheduler); populateScheduler(); }
	    	else homeScreen();
	    	
	    	// Remove 1st state
	    	this.last_view.remove(0);
    	}
    }
    
    
    // Menu click
    /*
    private OnClickListener menu_listener = new OnClickListener() { 
    	public void onClick(View v) { 
    		
    		last_view.add(0, HOME); 
    		//new KUMenuTask().execute(ku_menu); 
    	} 
    };
    */
    
    // Events click
    private OnClickListener events_listener = new OnClickListener() {
    	public void onClick(View v) {
    		
    		last_view.add(0, HOME); 
    		eventTask = (EventsTask) new EventsTask().execute(events); 
    	} 
    };
    
    
    // News click
    private OnClickListener news_listener = new OnClickListener() { 
    	public void onClick(View v) { 
    		
    		last_view.add(0, HOME); 
    		
    		newsTask = (NewsTask) new NewsTask().execute(news); 
    	} 
    };
    
    
    // Transfer click
    private OnClickListener transfer_listener = new OnClickListener() { 
    	public void onClick(View v) { 
    		
    		last_view.add(0, HOME); 
    		transferTask = (TransferTask) new TransferTask().execute(trans_dir); 
    	} 
    };
    
    
    // Directory click
    private OnClickListener directory_listener = new OnClickListener() { 
    	public void onClick(View v) { 
    		
    		last_view.add(0, HOME); 
    		setContentView(R.layout.directory); 
    		populateSearch(); 
    	} 
    };
    
    
    // Library click
    private OnClickListener library_listener = new OnClickListener() { 
    	public void onClick(View v) { 
    		
    		last_view.add(0, HOME); 
    		setContentView(R.layout.library); 
    		populateLibrary(); 
    	} 
    };
    
    
    // Map click
    private OnClickListener maps_listener = new OnClickListener() { 
    	public void onClick(View v) { 
	    	
    		// Load map activity
    		Intent myIntent = new Intent(MainActivity.this, Map.class);
	    	MainActivity.this.startActivity(myIntent); 
	    } 
    };
    
    // Schedule click 
    private OnClickListener schedule_listener = new OnClickListener() {
    	public void onClick(View v) { 
    		
    		scheduleTask = (ScheduleTask) new ScheduleTask().execute(student);
    	} 
    };
    
    // Scheduler click
    private OnClickListener scheduler_listener = new OnClickListener() { 
    	public void onClick(View v) { 
    	
    		courseSchedulerTask = (CourseSchedulerTask) new CourseSchedulerTask().execute(scheduler);
    	} 
    };
    
    
    // Account click
    private OnClickListener account_listener = new OnClickListener() { 
    	public void onClick(View v) { 
    
    		last_view.add(0, HOME);
    		accountTask = (AccountTask) new AccountTask().execute(student);
    	} 
    };
    
    
    // Grades click
    private OnClickListener grades_listener = new OnClickListener() { 
    	public void onClick(View v) { 
    		
    		last_view.add(0, HOME);
    		gradesTask = (GradesTask) new GradesTask().execute(student);
    	} 
    };    
    
    public void showAlert(String alert){
    	
    	Alert.show(c, alert);
    	
    }
    
    public void homeScreen(View v){
    	last_view = new ArrayList<String>();
    	homeScreen();
    }
    
    /********************************************************************
     * Method: homeScreen
     * Purpose: displays the home screen
    /*******************************************************************/
    public void homeScreen(){
    	
    	setContentView(R.layout.activity_main);
    	
    	// Elements
    	GridView grid = (GridView) findViewById(R.id.grid);
		EditText signinField = (EditText) findViewById(R.id.signinField);
		EditText passwordField = (EditText) findViewById(R.id.passwordField);
		Button signinButton = (Button) findViewById(R.id.signinButton);
		Button signoutButton = (Button) findViewById(R.id.signoutButton);
    	

    	if(this.student.getLoggedIn()){
			
    		// Layout
    		ViewGroup.MarginLayoutParams gridParams = (ViewGroup.MarginLayoutParams) grid.getLayoutParams();
			
			// Show functions
			grid.setAdapter(new IconAdapter(getApplicationContext(), this.icons_all));

			// Show & hide
			signinField.setVisibility(View.GONE);
			passwordField.setVisibility(View.GONE);
			signinButton.setVisibility(View.GONE);
			signoutButton.setVisibility(View.VISIBLE);
			
			// Convert to DIP
			gridParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, getResources().getDisplayMetrics());

			// Adjust weight
			grid.setLayoutParams(gridParams);
			signoutButton.requestFocus();
    	}
    	
    	
    	else{
    		
    		// Layout
    		ViewGroup.MarginLayoutParams gridParams = (ViewGroup.MarginLayoutParams) grid.getLayoutParams();
    					
            // Show functions
            grid.setAdapter(new IconAdapter(this, this.icons_glob));
            
            // Show & hide
            signinField.setVisibility(View.VISIBLE);
            passwordField.setVisibility(View.VISIBLE);
            signinButton.setVisibility(View.VISIBLE);
    		signoutButton.setVisibility(View.GONE);

    		
    		gridParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 148, getResources().getDisplayMetrics());
    		
			// Adjust weight
			grid.setLayoutParams(gridParams);
			
			signinField.requestFocus();
    	}
    }
    
    
    /********************************************************************
     * Method: back
     * Purpose: returns to previous screen
    /*******************************************************************/
    public void back(View view){ onBackPressed(); }
    
    
    /********************************************************************
     * Method: changeTransferType
     * Purpose: changes transfer type to given type
    /*******************************************************************/
    public void changeTransferType(View view){
    	
    	Button button = (Button) view;
    	String type = button.getText().toString();
    	
    	// Type
    	if(type.equals("By Course")) this.trans_dir.setType(trans_dir.BYCOURSE);
    	if(type.equals("By College")) this.trans_dir.setType(trans_dir.BYCOLLEGE);
    	
    	populateTransfer();
    }
    
    
    /********************************************************************
     * Method: searchTransfer
     * Purpose: searches a transfer
    /*******************************************************************/
    public void searchTransfer(View view){ transferSearchTask = (TransferSearchTask) new TransferSearchTask().execute(this.trans_dir); }
    
    
    /********************************************************************
     * Method: displayFinalMidtermGrades
     * Purpose: searches a transfer
    /*******************************************************************/
    public void displayFinalMidtermGrades(View view){ finalMidtermRetrival = (FinalMidtermRetrival) new FinalMidtermRetrival().execute(this.student); }
    
    
    /********************************************************************
     * Method: changeGradeType
     * Purpose: changes type of grades being shown
    /*******************************************************************/
    public void changeGradeType(View view){ 
    	
    	Button gradeButton = (Button) view;
    	
    	if(gradeButton.getText().equals("Current")) this.student.setGradesPage(0);
    	else if(gradeButton.getText().equals("Final")) this.student.setGradesPage(1);
    	else if(gradeButton.getText().equals("Midterm")) this.student.setGradesPage(2);
    	
    	gradesTask = (GradesTask) new GradesTask().execute(student);
    }
    
    
    /********************************************************************
     * Method: populateGrades
     * Purpose: populates grades (current, final or midterm)
    /*******************************************************************/
    public void populateGrades(){
    	
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	LinearLayout gradesContainer = (LinearLayout) findViewById(R.id.gradesContainer);
    	Button currentButton = (Button) findViewById(R.id.currentButton);
    	Button finalButton = (Button) findViewById(R.id.finalButton);
    	Button midtermButton = (Button) findViewById(R.id.midtermButton);
    	
    	currentButton.setBackgroundResource(R.layout.food_menu_button);
    	finalButton.setBackgroundResource(R.layout.food_menu_button);
    	midtermButton.setBackgroundResource(R.layout.food_menu_button);
    	
    	
    	// Current grades
    	if(this.student.getGradesPage() == 0){
    		
    		gradesContainer.removeAllViews();
    		
    		currentButton.setBackgroundResource(R.layout.food_menu_button_selected);
    		List<CurrentGrade> currentGrades = this.student.getCurrentGrades();
    		
    		for(int i = 0; i < currentGrades.size(); i++){
    			
    			CurrentGrade current = currentGrades.get(i);
    			
    			TableLayout currentView = (TableLayout) inflater.inflate(R.layout.grade, null);
    			TextView title = (TextView) currentView.findViewById(R.id.title);
    			TextView total = (TextView) currentView.findViewById(R.id.total);
    			TextView index = (TextView) currentView.findViewById(R.id.index);
    			
    			title.setText(current.getCourseName());
    			total.setText(current.getTitleTotal() + "");
    			index.setText(i + "");
    			
    			currentView.setOnClickListener(new OnClickListener() {
        			public void onClick(View v) {
        				
        				// Always hidden
        				TextView indexView = (TextView) v.findViewById(R.id.index);
        				
    	        		// Article parameters
        				String index = indexView.getText().toString();
        				String[] params = {index};
        				
        				gradeSingleTask = (GradeSingleTask) new GradeSingleTask().execute(params);
                    }
                });
    			
    			gradesContainer.addView(currentView);
    		}
    		
    	}
    	
    	// Final grades
    	else if(this.student.getGradesPage() == 1 && this.student.getFinalSelectedTerm() == -1){
    		
    		// Adjust selected item
    		finalButton.setBackgroundResource(R.layout.food_menu_button_selected);
    		
    		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, student.getFinalListString());
    		Spinner selectList = (Spinner) findViewById(R.id.acceptedTerms);
        	selectList.setAdapter(spinnerAdapter);
        	
        	selectList.setSelection(student.getFinalSelectedTerm(), true);
        	
    		
    		finalMidtermRetrival = (FinalMidtermRetrival) new FinalMidtermRetrival().execute(this.student);
    	}
    	
    	else if(this.student.getGradesPage() == 1 && this.student.getFinalSelectedTerm() != -1){
    		
    		// Adjust selected item
    		finalButton.setBackgroundResource(R.layout.food_menu_button_selected);
    		
    		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, student.getFinalListString());
    		Spinner selectList = (Spinner) findViewById(R.id.acceptedTerms);
        	selectList.setAdapter(spinnerAdapter);
        	
        	selectList.setSelection(student.getFinalSelectedTerm(), true);
        	
        	for(FinalGrade currentGrade : student.getFinalGrades()){
        		
        		if(student.getFinalGrades().size() > 1 && currentGrade.getTitle().contains("Educat")){}
        		
        		else {
        			
        			TableLayout currentView = (TableLayout) inflater.inflate(R.layout.finalmidtermgrade, null);
            		
        			TextView title = (TextView) currentView.findViewById(R.id.title);
        			TextView grade = (TextView) currentView.findViewById(R.id.grade);
        			TextView credits = (TextView) currentView.findViewById(R.id.credits);
        			
        			title.setText(currentGrade.getTitle());
        			grade.setText(currentGrade.getGrade());
        			credits.setText(currentGrade.getEarnedCredits() + "");
        			
        			gradesContainer.addView(currentView);
        		}
        		
        	}
    		
    	}
    	
    	// Midterm grades
    	else if(this.student.getGradesPage() == 2 && this.student.getMidtermSelectedTerm() == -1){
    		
    		midtermButton.setBackgroundResource(R.layout.food_menu_button_selected);
    		
    		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, student.getMidtermListString());
    		Spinner selectList = (Spinner) findViewById(R.id.acceptedTerms);
        	selectList.setAdapter(spinnerAdapter);
        	
        	selectList.setSelection(student.getMidtermSelectedTerm(), true);
        
    		
    		finalMidtermRetrival = (FinalMidtermRetrival) new FinalMidtermRetrival().execute(this.student);
    	}
    	
    	else if(this.student.getGradesPage() == 2 && this.student.getMidtermSelectedTerm() != -1){
    		
    		midtermButton.setBackgroundResource(R.layout.food_menu_button_selected);
    		
    		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, student.getMidtermListString());
    		Spinner selectList = (Spinner) findViewById(R.id.acceptedTerms);
        	selectList.setAdapter(spinnerAdapter);
        	
        	selectList.setSelection(student.getMidtermSelectedTerm(), true);
        
        	for(MidtermGrade currentGrade : student.getMidtermGrades()){
        		
        		if(student.getFinalGrades().size() > 1 && currentGrade.getTitle().contains("Educat")){}
        		
        		else {
        			
        			TableLayout currentView = (TableLayout) inflater.inflate(R.layout.finalmidtermgrade, null);
            		
        			TextView title = (TextView) currentView.findViewById(R.id.title);
        			TextView grade = (TextView) currentView.findViewById(R.id.grade);
        			TextView credits = (TextView) currentView.findViewById(R.id.credits);
        			
        			title.setText(currentGrade.getTitle());
        			grade.setText(currentGrade.getGrade());
        			credits.setText(currentGrade.getAttemptedCredits() + "");
        			
        			gradesContainer.addView(currentView);
        		}
        		
        	}
    	
    	}
    	
    }
    
    /********************************************************************
     * Method: populateAccount
     * Purpose: populates account information
    /*******************************************************************/
    public void populateAccount(){
    	
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	LinearLayout accountContainer = (LinearLayout) findViewById(R.id.accountContainer);
    	
		TextView balanceTot = (TextView) accountContainer.findViewById(R.id.balance);
		TextView charges = (TextView) accountContainer.findViewById(R.id.charges);
		TextView payments = (TextView) accountContainer.findViewById(R.id.payments);
		
		balanceTot.setText(this.student.getAccountTotal().getBalance());
		charges.setText(this.student.getAccountTotal().getCharges());
		payments.setText(this.student.getAccountTotal().getCredits());
		
		
		for(AccountDetail currentDetail : this.student.getAccountTotal().getDetails()){
			
			TableLayout currentView = (TableLayout) inflater.inflate(R.layout.account_detail, null);
			
			TextView balance = (TextView) currentView.findViewById(R.id.balance);
			TextView charge = (TextView) currentView.findViewById(R.id.charge);
			TextView payment = (TextView) currentView.findViewById(R.id.payment);
			TextView description = (TextView) currentView.findViewById(R.id.description);
			
			balance.setText(currentDetail.getBalance());
			charge.setText(currentDetail.getCharge());
			payment.setText(currentDetail.getPayment());
			description.setText(currentDetail.getDescription());
			
			accountContainer.addView(currentView);
			
		}
		
    }
    
    /********************************************************************
     * Method: populateSchedule
     * Purpose: populates schedule results
    /*******************************************************************/
    public void populateSchedule(){
    	
    	String[] jsonArray = new String[this.student.getCourses().size()];
    	Gson gson = new Gson();
    	
    	for(int i = 0; i < this.student.getCourses().size(); i++) jsonArray[i] = gson.toJson(this.student.getCourses().get(i));
    	
    	Intent myIntent = new Intent(MainActivity.this, Schedule.class);
    	myIntent.putExtra("jsonCourses", jsonArray);
    	MainActivity.this.startActivity(myIntent);
    }
    
    /********************************************************************
     * Method: populateScheduler
     * Purpose: populates scheduler page
    /*******************************************************************/
    public void populateScheduler(){
    
    	List<String> courseNamesAndIDs = new ArrayList<String>();
    	
    	for(int i = 0; i < this.scheduler.getDynamicCourseIDs().size(); i++){
    		
    		String courseID = this.scheduler.getDynamicCourseIDs().get(i);
    		
    		if(i < scheduler.getDynamicCourseNames().size()){
    			courseNamesAndIDs.add(courseID + ":  " + this.scheduler.getDynamicCourseNames().get(i));
    		}
    		else{
    			courseNamesAndIDs.add(courseID);
    		}
    	}
    	
    	// Elements
    	ArrayAdapter<String> termsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, this.scheduler.getTermsName());
    	ArrayAdapter<String> classesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseNamesAndIDs);
    	Spinner termsList = (Spinner) findViewById(R.id.terms);
    	Spinner classesList = (Spinner) findViewById(R.id.classes);
    	LinearLayout container = (LinearLayout) findViewById(R.id.classContainer);
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	Button view = (Button) findViewById(R.id.viewSchedulesButton);
    	
    	termsList.setAdapter(termsAdapter);
    	classesList.setAdapter(classesAdapter);
    	
    	if(!scheduler.getLoaded()){
    		classesList.setEnabled(false);
    		((RelativeLayout) classesList.getParent()).findViewById(R.id.addClasses).setEnabled(false);
    		view.setEnabled(true);
    		//view.setEnabled(false);
    	}
    	
    	else{
	    	
    		termsList.setSelection(scheduler.getTermIndex());
    		classesList.setEnabled(true);
    		((RelativeLayout) classesList.getParent()).findViewById(R.id.addClasses).setEnabled(true);
    		
    		view.setEnabled(true);
    		//view.setEnabled(this.scheduler.getCurrentCourseList().size() > 0);
    		
	    	for(int i = 0; i < this.scheduler.getCurrentCourseList().size(); i++){
	    	
	    		String current = scheduler.getCurrentCourseList().get(i);
	    		String courseTitle = "";
	    		
	    		RelativeLayout classCont = (RelativeLayout) inflater.inflate(R.layout.scheduler_class, null);
	    		TextView className = (TextView) classCont.findViewById(R.id.className);
	    		TextView classTitle = (TextView) classCont.findViewById(R.id.classTitle);
	    		className.setText(current);
	    		courseTitle = current;
	    		if(this.scheduler.getDynamicCourses().get(current).size() > 0){
	    			courseTitle = courseTitle + ":  " + this.scheduler.getDynamicCourses().get(current).get(0).getCourseName();
	    		}
	    		
	    		classTitle.setText(courseTitle);
	    		
	    		ImageView delete = (ImageView) classCont.findViewById(R.id.removeClass);
	        	delete.setOnClickListener(new OnClickListener(){
	        		public void onClick(View v){
	        		    	
	        			LinearLayout parent = (LinearLayout) v.getParent().getParent();
	        		    String removeID = (String) ((TextView) ((RelativeLayout) v.getParent()).findViewById(R.id.className)).getText();
	        		    parent.removeView((RelativeLayout)v.getParent());
	        		    scheduler.getCurrentCourseList().remove(removeID);	
	        		}
	        		
	        	});
	        	
	    		container.addView(classCont);
	    	}
    	}
    }
    
    
    
    /********************************************************************
     * Method: populateTransfer
     * Purpose: populates transfer results
    /*******************************************************************/
    public void populateTransfer(){
    
    	
    	// Elements
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, this.trans_dir.getOptionsString());
    	LinearLayout transContainer = (LinearLayout) findViewById(R.id.transferContainer);
    	Button byCollegeButton = (Button) findViewById(R.id.byCollegeButton);
    	Button byCourseButton = (Button) findViewById(R.id.byCourseButton);
    	EditText searchBar = (EditText) findViewById(R.id.searchField);
    	Spinner selectList = (Spinner) findViewById(R.id.college);
    	
    	selectList.setAdapter(adapter);
    	transContainer.removeAllViews();
    	
    	// Last session
    	if(!trans_dir.getSearchString().equals("")) searchBar.setText(trans_dir.getSearchString());
    	selectList.setSelection(trans_dir.getCollegeListPosition(), true);
    	
    	
    	if (trans_dir.getType() == trans_dir.BYCOLLEGE){
    		
    		// Settings
    		byCourseButton.setBackgroundResource(R.layout.food_menu_button);
    		byCollegeButton.setBackgroundResource(R.layout.food_menu_button_selected);
    		selectList.setVisibility(View.VISIBLE);
    		searchBar.setVisibility(View.GONE);
    		
    		
	    	if(trans_dir.getResultsByCollege() != null){
	    	
	    		for(TransferCourse course : trans_dir.getResultsByCollege()){
	    			
	    			
	    			LinearLayout currentView = (LinearLayout) inflater.inflate(R.layout.transfer_course_college, null);		
	    			
	    			// Row
	    			TableRow kuCourseRow = (TableRow) currentView.findViewById(R.id.kuCourseRow);
	    			TableRow trCourseRow = (TableRow) currentView.findViewById(R.id.trCourseRow);
	    			TableRow creditsRow = (TableRow) currentView.findViewById(R.id.creditsRow);
	    			TableRow commentsRow = (TableRow) currentView.findViewById(R.id.commentsRow);
	    			
	    			// Text
	    			TextView title = (TextView) currentView.findViewById(R.id.title);
	    			TextView kuCourse = (TextView) currentView.findViewById(R.id.kuCourse);
	    			TextView trCourse = (TextView) currentView.findViewById(R.id.trCourse);
	    			TextView credits = (TextView) currentView.findViewById(R.id.credits);
	    			TextView comments = (TextView) currentView.findViewById(R.id.comments);
	    			
	    			
	    			if(course.getTitle() != null && !course.getTitle().equals("") && course.getCredits() != null && !course.getCredits().equals("")){
	    				
	    				title.setText(course.getTitle());
	    				credits.setText(course.getCredits());
	    				
	    				// KU Course Id
	    				if(course.getKUCourseID() != null && !course.getKUCourseID().equals("")) kuCourse.setText(course.getKUCourseID());
	    				else kuCourseRow.setVisibility(View.GONE);
	    				
	    				// Transfer ID
	    				if(course.getTransferID() != null && !course.getTransferID().equals("")) trCourse.setText(course.getTransferID());
	    				else trCourseRow.setVisibility(View.GONE);
	    				
	    				// Comment
	    				if(course.getComment() != null && !course.getComment().equals("")) comments.setText(course.getComment());
	    				else {
	    					commentsRow.setVisibility(View.GONE);
	    					creditsRow.setBackgroundResource(R.layout.table_even_bottom);
	    					TableLayout.LayoutParams params = (TableLayout.LayoutParams) creditsRow.getLayoutParams();
	    					params.bottomMargin = 18;
	    				}
	    				
	    				transContainer.addView(currentView);
	    			}
	    		}
	    	}
	    	
	    	this.load_dialog.dismiss();
    	}
    	
    	
    	else if (trans_dir.getType() == trans_dir.BYCOURSE) {
    		
    		// Settings
    		byCourseButton.setBackgroundResource(R.layout.food_menu_button_selected);
    		byCollegeButton.setBackgroundResource(R.layout.food_menu_button);
    		searchBar.setVisibility(View.VISIBLE);
    		selectList.setVisibility(View.GONE);
    		
    		
    		if(trans_dir.getResultsByCourse() != null){
	    		
	    		for(TransferCourse course : trans_dir.getResultsByCourse()){
	    			
	    			LinearLayout currentView = (LinearLayout) inflater.inflate(R.layout.transfer_course, null);
	    	
	    			// Row
	    			TableRow kuCourseRow = (TableRow) currentView.findViewById(R.id.kuCourseRow);
	    			TableRow trCourseRow = (TableRow) currentView.findViewById(R.id.trCourseRow);
	    			TableRow creditsRow = (TableRow) currentView.findViewById(R.id.creditsRow);
	    			TableRow commentsRow = (TableRow) currentView.findViewById(R.id.commentsRow);
	    			
	    			// Text
	    			TextView college = (TextView) currentView.findViewById(R.id.collegeVal);
	    			TextView kuCourse = (TextView) currentView.findViewById(R.id.kuCourse);
	    			TextView trCourse = (TextView) currentView.findViewById(R.id.trCourse);
	    			TextView credits = (TextView) currentView.findViewById(R.id.credits);
	    			TextView comments = (TextView) currentView.findViewById(R.id.comments);
	    			
	    			
	    			if(course.getInstitution() != null && !course.getInstitution().equals("") && course.getCredits() != null && !course.getCredits().equals("")){
		    				
	    				college.setText(course.getInstitution());
	    				credits.setText(course.getCredits());
	    				
	    				// KU Course ID
	    				if(course.getKUCourseID() != null && !course.getKUCourseID().equals("")) kuCourse.setText(course.getKUCourseID());
	    				else kuCourseRow.setVisibility(View.GONE);
	    				
	    				// Transfer ID
	    				if(course.getTransferID() != null && !course.getTransferID().equals("")) trCourse.setText(course.getTransferID());
	    				else trCourseRow.setVisibility(View.GONE);
	    				
	    				// Comment
	    				if(course.getComment() != null && !course.getComment().equals("")) comments.setText(course.getComment());
	    				else {
	    					commentsRow.setVisibility(View.GONE);
	    					creditsRow.setBackgroundResource(R.layout.table_even_bottom);
	    					TableLayout.LayoutParams params = (TableLayout.LayoutParams) creditsRow.getLayoutParams();
	    					params.bottomMargin = 18;
	    				}
		    				
	    				transContainer.addView(currentView);
	    			}
	    		}
	    	}
    		
    		this.load_dialog.dismiss();
    	}
    }
    
    
    
    /********************************************************************
     * Method: populateEvents
     * Purpose: populates current events into view
    /*******************************************************************/
	public void populateEvents(){
    	
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	LinearLayout eventsContainer = (LinearLayout) findViewById(R.id.eventsContainer);
    	List<EventDay> eventDays = events.getEvents(); 
    	
    	
    	// All Days
    	for(int countDay = 0; countDay < eventDays.size(); countDay++){
    		
    		EventDay eventDay = eventDays.get(countDay);
    		
    		// Elements
    		RelativeLayout titleContainer = (RelativeLayout) inflater.inflate(R.layout.title, null);
    		TextView titleLeft = (TextView) titleContainer.findViewById(R.id.titleLeft);
    		TextView titleRight = (TextView) titleContainer.findViewById(R.id.titleRight);
    		
    		// Titles
    		titleLeft.setText(eventDay.getDate().getDayOfWeek());
    		titleRight.setText(eventDay.getDate().toString());
    		
    		eventsContainer.addView(titleContainer);

    		// All Events
    		for(int count = 0; count < eventDay.getEvents().size(); count++){
    			LinearLayout spacer = (LinearLayout) inflater.inflate(R.layout.spacer, null);
    			
    			Event event = eventDay.getEvents().get(count);
    			RelativeLayout eventView = (RelativeLayout) inflater.inflate(R.layout.event, null);
    	
	        	// Elements
	        	TextView summary = (TextView) eventView.findViewById(R.id.summary);
	        	TextView index = (TextView) eventView.findViewById(R.id.index);
	        	TextView indexDay = (TextView) eventView.findViewById(R.id.indexDay);
	        	TextView info = (TextView) eventView.findViewById(R.id.info);
	        	TextView link = (TextView) eventView.findViewById(R.id.link);
	        	ImageView img = (ImageView) eventView.findViewById(R.id.image);
	        	//img.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	        	
	        	if(event.getSummary() != null && event.getDescription() != null) {

	        		//img.setImageDrawable(new BitmapDrawable(event.getBitmap()));
	        		if(event.getIsValidImg())
	        			img.setImageBitmap(event.getBitmap());
	        		else
	        			img.setImageResource(R.drawable.blank);
	        		
	        		// Event click
	        		eventView.setOnClickListener(new OnClickListener() {        
	        			public void onClick(View v) {
	        				
	        				// Elements
	        				TextView linkView = (TextView) v.findViewById(R.id.link);
	        				TextView indexView = (TextView) v.findViewById(R.id.index);
	        				TextView indexDayView = (TextView) v.findViewById(R.id.indexDay);
	        				
	    	        		// Article parameters
	        				String link = linkView.getText().toString();
	        				String index = indexView.getText().toString();
	        				String indexDay = indexDayView.getText().toString(); 
	        				String[] params = {link, index, indexDay};
	        				
	        				eventArticleTask = (EventArticleTask) new EventArticleTask().execute(params);
	                    }
	                });

	        		
	        		/*
	        		// Hide (All Day)
	        		if(event.getInfo().equals("(All day)")){
	        			
	        			LinearLayout.LayoutParams params = (LayoutParams) summary.getLayoutParams();
	        			info.setVisibility(View.INVISIBLE);
	        			params.setMargins(8,18,8,18);
	        			summary.setLayoutParams(params);
	        		}
	        		
	        		if(!event.getIsValidImg() && false){
	        			img.setVisibility(View.GONE);
	        			imgDef.setVisibility(View.VISIBLE);
	        		}
	        		else{
	        			img.setVisibility(View.VISIBLE);
	        			imgDef.setVisibility(View.GONE);
	        		}
	        		*/
	        		// Fields
	        		//img.loadDataWithBaseURL("http://kettering.edu", "<body style=\"background:#7F95FA\">" + event.getImg() + "</body>", "text/html", null, null);
	        		//img.setHorizontalFadingEdgeEnabled(false);
	        		//img.setVerticalScrollBarEnabled(false);
	        		String temp[] = event.getInfo().split(Pattern.quote("|"));
	        		
	        		summary.setText(event.getSummary());
	        		indexDay.setText(countDay + "");
	        		index.setText(count + "");
	        		//if((temp.length < 2))
	        			info.setText("Time: " + temp[0]);
	        		//else 
	        			//info.setText("Time: " + temp[0] + "\n" + "Location: " + temp[1]);
	        		
	        		link.setText(event.getLink());
	        		eventsContainer.addView(eventView);
	        		eventsContainer.addView(spacer);
	        	}        
    		}
    	}	
    }
    
    
    /********************************************************************
     * Method: populateLibrary
     * Purpose: populates library search results 
    /*******************************************************************/
    public void populateLibrary(){
    	
    	List<LibraryItem> libList = lib.getLibrary();
    
    	// Elements
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	LinearLayout libContainer = (LinearLayout) findViewById(R.id.libraryContainer);
    	Button loadMoreButton = (Button) inflater.inflate(R.layout.library_load_more_button, null);
    	EditText searchField = (EditText) findViewById(R.id.searchField);
    	Spinner type = (Spinner) findViewById(R.id.type);
    	
    	// Last session
    	if(lib.getType().equalsIgnoreCase("Title")) type.setSelection(0);
    	else if (lib.getType().equalsIgnoreCase("Phrase")) type.setSelection(1);
    	else if (lib.getType().equalsIgnoreCase("Author")) type.setSelection(2);
    	else if (lib.getType().equalsIgnoreCase("Subject")) type.setSelection(3);
    	else if (lib.getType().equalsIgnoreCase("Series")) type.setSelection(4);
    	else if (lib.getType().equalsIgnoreCase("Periodical")) type.setSelection(5);
    	if(!lib.getSearchString().equals("")) searchField.setText(lib.getSearchString());
    	
    	libContainer.removeAllViews();
    	
    	for(LibraryItem libItem : libList){
			
			if(!libItem.getTitle().replaceAll("\\s+", "").equals("") && !libItem.getHoldings().replaceAll("\\s+", "").equals("")){
    			
				// Elements
				TableLayout libItemView = (TableLayout) inflater.inflate(R.layout.library_search_item, null);
				LinearLayout spacer = (LinearLayout) inflater.inflate(R.layout.spacer,null);
				LinearLayout spacer2 = (LinearLayout) inflater.inflate(R.layout.spacer,null);
				TableRow callNumberRow = (TableRow) libItemView.findViewById(R.id.callNumberRow);
    			TableRow publisherRow = (TableRow) libItemView.findViewById(R.id.publisherRow);
    			TableRow editionRow = (TableRow) libItemView.findViewById(R.id.editionRow);
    			TableRow pubDateRow = (TableRow) libItemView.findViewById(R.id.pubDateRow);
    			TextView callNumber = (TextView) libItemView.findViewById(R.id.callNumber);
    			TableRow authorRow = (TableRow) libItemView.findViewById(R.id.authorRow);
    			TextView publisher = (TextView) libItemView.findViewById(R.id.publisher);
    			TextView holdings = (TextView) libItemView.findViewById(R.id.holdings);
    			TextView pubDate = (TextView) libItemView.findViewById(R.id.pubDate);
    			TextView edition = (TextView) libItemView.findViewById(R.id.edition);
    			TextView author = (TextView) libItemView.findViewById(R.id.author);
				TextView title = (TextView) libItemView.findViewById(R.id.title);
    			
    		
    			title.setText(libItem.getTitle());
    			holdings.setText(libItem.getHoldings());
    			
    			// Author
    			if(!libItem.getAuthor().replaceAll("\\s+", "").equals("")) author.setText(libItem.getAuthor());
    			else authorRow.setVisibility(View.GONE);
    			
    			// Call number
    			if(!libItem.getCallNumber().replaceAll("\\s+", "").equals("")) callNumber.setText(libItem.getCallNumber());
    			else callNumberRow.setVisibility(View.GONE);
    			
    			// Publisher
    			if(!libItem.getPublisher().replaceAll("\\s+", "").equals("")) publisher.setText(libItem.getPublisher());
    			else publisherRow.setVisibility(View.GONE);
    			
    			// Edition
    			if(!libItem.getEdition().replaceAll("\\s+", "").equals("")) edition.setText(libItem.getEdition());
    			else editionRow.setVisibility(View.GONE);
    			
    			// Publish date
    			if(!libItem.getPubDate().replaceAll("\\s+", "").equals("")) pubDate.setText(libItem.getPubDate());
    			else pubDateRow.setVisibility(View.GONE);
    			
    			libContainer.addView(libItemView);
    			libContainer.addView(spacer);
    			libContainer.addView(spacer2);
			}		
    	}	
    	
    	// Max page?
    	if(lib.getPage() < lib.getMaxPage()){
    		libContainer.addView(loadMoreButton);
    		loadMoreButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					libraryPageTask = (LibraryPageTask) new LibraryPageTask().execute(lib);
				}
			});
    	}
    }
    
    
    /********************************************************************
     * Method: populateSearch
     * Purpose: populates search results 
    /*******************************************************************/
    public void populateSearch(){
    	
    	// Elements
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	LinearLayout dirContainer = (LinearLayout) findViewById(R.id.dirContainer);
    	EditText searchField = (EditText) findViewById(R.id.searchField);
    	Spinner type = (Spinner) findViewById(R.id.type);
    	
    	
    	// Last session
    	if(!dir.getSearch().equals("")) searchField.setText(dir.getSearch());
    	if(dir.getType().equalsIgnoreCase("Faculty")) type.setSelection(0);
    	else if (dir.getType().equalsIgnoreCase("Student")) type.setSelection(1);
    	
    	dirContainer.removeAllViews();
    	
    	if(dir.getStudentQuery() != null && dir.getType().equals("student")){
	    		
    		List<StudentResult> query = dir.getStudentQuery().getEntries();
    		
    		for(StudentResult result : query){
    			
    			// Elements
    			View resultView = inflater.inflate(R.layout.student_search_result, null);
    			TextView name = (TextView) resultView.findViewById(R.id.name);
    			TextView email = (TextView) resultView.findViewById(R.id.email);
    			
    			// Fields
    			name.setText(result.getFirstName() + " " + result.getLastName());
    			email.setText(result.getEmail());
    			dirContainer.addView(resultView);
    		}
    	}
	    	
    	else if(dir.getFacultyQuery() != null && dir.getType().equals("faculty")){
    		
    		List<FacultyResult> query = dir.getFacultyQuery().getEntries();
    		
    		for(FacultyResult result : query){
    			
    			if(result.getFirstName() != null && result.getEmail() != null){
	    			
    				// Elements
    				TableLayout resultView = (TableLayout) inflater.inflate(R.layout.faculty_search_result, null);
	    			TextView name = (TextView) resultView.findViewById(R.id.name);
	    			TextView email = (TextView) resultView.findViewById(R.id.email);
	    			TextView phone = (TextView) resultView.findViewById(R.id.phone);
	    			TextView room = (TextView) resultView.findViewById(R.id.room);
	    			TableRow phoneRow = (TableRow) resultView.findViewById(R.id.phoneRow);
	    			TableRow roomRow = (TableRow) resultView.findViewById(R.id.roomRow);
	    			
	    			// Fields
	    			name.setText(result.getFirstName() + " " + result.getLastName());
	    			email.setText(result.getEmail());
	    			
	    			if(result.getPhone() != null && result.getPhone().size() > 0) {
	    				
	    				String phoneStr = result.getPhone().get(0).toString();
	    				
	    				// All phones
	    				for(int i = 1; i < result.getPhone().size(); i++) phoneStr += "\n" + result.getPhone().get(i).toString();
	    				
	    				// No phone ?
	    				if(phoneStr.replaceAll("\\s", "").equals("")) phoneRow.setVisibility(View.GONE);
	    				else phone.setText(phoneStr);
	    			}
	    			
	    			else phoneRow.setVisibility(View.GONE);
	    			
	    			if(result.getAddress() != null && result.getAddress().size() > 0){
	    				
	    				String roomStr = result.getAddress().get(0).toString();
	    				
	    				// All rooms
	    				for(int i = 1; i < result.getAddress().size(); i++) roomStr += "\n" + result.getAddress().get(i).toString();
	    				
	    				// No room ?
	    				if(roomStr.replaceAll("\\s", "").equals("")) roomRow.setVisibility(View.GONE);
	    				else room.setText(roomStr);
	    			}
	    			
	    			else roomRow.setVisibility(View.GONE);
	    			
	    			dirContainer.addView(resultView);
    			}		
	    	}
    	}	
    }
    
    
    /********************************************************************
     * Method: changeMenuDay
     * Purpose: changes the menus day
    /*******************************************************************/
    public void changeMenuDay(View v){
    	
    	String dayStr = ((TextView) v).getText().toString();
    	
    	// Switch day
    	if(dayStr.equals("Sun")) populateMenu(0);
    	else if(dayStr.equals("Mon")) populateMenu(1);
    	else if(dayStr.equals("Tues")) populateMenu(2);
    	else if(dayStr.equals("Wed")) populateMenu(3);
    	else if(dayStr.equals("Thurs")) populateMenu(4);
    	else if(dayStr.equals("Fri")) populateMenu(5);
    	else if(dayStr.equals("Sat")) populateMenu(6);
    }
    
    
    /********************************************************************
     * Method: populateNews
     * Purpose: populates current news into view
    /*******************************************************************/
    @SuppressWarnings("deprecation")
	public void populateNews(){
    	
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	LinearLayout newsContainer = (LinearLayout) findViewById(R.id.newsContainer);
    	
    	List<NewsItem> newsList = this.news.getNews();
    	
    	// All Days
    	for(int count = 0; count < newsList.size(); count++){
    		LinearLayout spacer = (LinearLayout) inflater.inflate(R.layout.spacer, null);
			View newsView = inflater.inflate(R.layout.news_item, null);
	
        	// Elements
			TextView authorDate = (TextView) newsView.findViewById(R.id.info);
			TextView summary = (TextView) newsView.findViewById(R.id.summary);
			TextView index = (TextView) newsView.findViewById(R.id.index);
        	TextView link = (TextView) newsView.findViewById(R.id.link);
        	//WebView img = (WebView) newsView.findViewById(R.id.image);
        	ImageView img = (ImageView) newsView.findViewById(R.id.image);
        	
    		newsView.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				
    				// Always hidden
    				TextView linkView = (TextView) v.findViewById(R.id.link);
    				TextView indexView = (TextView) v.findViewById(R.id.index);
    				
	        		// Article parameters
    				String link = linkView.getText().toString();
    				String index = indexView.getText().toString();
    				String[] params = {link, index};
    				
    				newsArticleTask = (NewsArticleTask) new NewsArticleTask().execute(params);
                }
            });
    		
    		img.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    		img.setImageDrawable(new BitmapDrawable(newsList.get(count).getBitmap()));
    		
    		// Fields
    		//img.loadDataWithBaseURL("http://kettering.edu", "<body style=\"background:" + bg_color + "\">" + newsList.get(count).getImg() + "</body>", "text/html", null, null);
        	//img.setVerticalScrollBarEnabled(false);
    		//img.setHorizontalFadingEdgeEnabled(false);
    		authorDate.setText(newsList.get(count).getInfo());
    		summary.setText(newsList.get(count).getTitle());
    		link.setText(newsList.get(count).getLink());
    		index.setText(count + "");
    		
        	newsContainer.addView(newsView);
        	newsContainer.addView(spacer);
		}
    }
    
    /********************************************************************
     * Method populateMenu
     * Purpose: populates menu with a single day of the week
    /*******************************************************************/
    public void populateMenu(int dayOfWeek){
    	/*
    	// Buttons
    	((Button) findViewById(R.id.sun)).setBackgroundResource((dayOfWeek == 0)? R.layout.food_menu_button_selected:R.layout.food_menu_button);
    	((Button) findViewById(R.id.mon)).setBackgroundResource((dayOfWeek == 1)? R.layout.food_menu_button_selected:R.layout.food_menu_button);
    	((Button) findViewById(R.id.tues)).setBackgroundResource((dayOfWeek == 2)? R.layout.food_menu_button_selected:R.layout.food_menu_button);
    	((Button) findViewById(R.id.wed)).setBackgroundResource((dayOfWeek == 3)? R.layout.food_menu_button_selected:R.layout.food_menu_button);
    	((Button) findViewById(R.id.thurs)).setBackgroundResource((dayOfWeek == 4)? R.layout.food_menu_button_selected:R.layout.food_menu_button);
    	((Button) findViewById(R.id.fri)).setBackgroundResource((dayOfWeek == 5)? R.layout.food_menu_button_selected:R.layout.food_menu_button);
    	((Button) findViewById(R.id.sat)).setBackgroundResource((dayOfWeek == 6)? R.layout.food_menu_button_selected:R.layout.food_menu_button);
    	
        DailyMenu day = ku_menu.getDayOfWeek(dayOfWeek);
        
    	// Data        
        ((TextView) findViewById(R.id.date)).setText(day.getDate());
        ((TextView) findViewById(R.id.soupLunch)).setText(day.getLunch().getSoup());
        ((TextView) findViewById(R.id.soupDinner)).setText(day.getDinner().getSoup());
        ((TextView) findViewById(R.id.pastaLunch)).setText(day.getLunch().getPastaStation());
        ((TextView) findViewById(R.id.pastaDinner)).setText(day.getDinner().getPastaStation());
        ((TextView) findViewById(R.id.grillLunch)).setText(day.getLunch().getGrill());
        ((TextView) findViewById(R.id.grillDinner)).setText(day.getDinner().getGrill());
        ((TextView) findViewById(R.id.pizzaLunch)).setText(day.getLunch().getPizza());
        ((TextView) findViewById(R.id.pizzaDinner)).setText(day.getDinner().getPizza());
        ((TextView) findViewById(R.id.deliLunch)).setText(day.getLunch().getDeli());
        ((TextView) findViewById(R.id.deliDinner)).setText(day.getDinner().getDeli());
        ((TextView) findViewById(R.id.expoLunch)).setText(day.getLunch().getExpo());
        ((TextView) findViewById(R.id.expoDinner)).setText(day.getDinner().getExpo());
        ((TextView) findViewById(R.id.dessertLunch)).setText(day.getLunch().getDessert());
        ((TextView) findViewById(R.id.dessertDinner)).setText(day.getDinner().getDessert());
        */
    }
    
    
    /********************************************************************
     * Method: loadMoreLibrary
     * Purpose: loads next page of events
    /*******************************************************************/
    public void loadMoreLibrary(View view){ libraryPageTask = (LibraryPageTask) new LibraryPageTask().execute(lib); }
    
    
    /********************************************************************
     * Method: loadMoreEvents
     * Purpose: loads next page of events
    /*******************************************************************/
    public void loadMoreEvents(View view){ eventsPageTask = (EventsPageTask) new EventsPageTask().execute(events); }
    
    
    /********************************************************************
     * Method: loadMoreNews
     * Purpose: loads next page of events
    /*******************************************************************/
    public void loadMoreNews(View view){ newsPageTask = (NewsPageTask) new NewsPageTask().execute(news); }

    

    /********************************************************************
     * Method: signIn
     * Purpose: sign user into banner and blackboard connections
    /*******************************************************************/
    public void signIn(View view){ login = (Login) new Login().execute(this.student); }
    
    
    /********************************************************************
     * Method: signOut
     * Purpose: sign user out of banner and blackboard connections
    /*******************************************************************/
    public void signOut(View view){
    	
    	this.student = new Student();
    	
    	homeScreen();
    }
    
    
    /********************************************************************
     * Method: search
     * Purpose: searches for faculty / students
    /*******************************************************************/
    public void search(View v){ directoryTask = (DirectoryTask) new DirectoryTask().execute(dir); }
    

    /********************************************************************
     * Method: storeSchedulerTerm
     * Purpose: stores a scheulders new term data
    /*******************************************************************/
    public void storeSchedulerTerm(View v){ storeScheduleTermTask = (StoreScheduleTermTask) new StoreScheduleTermTask().execute(scheduler); }
    
    /********************************************************************
     * Method: storeSchedulerTerm
     * Purpose: stores a scheulders new term data
    /*******************************************************************/
    public void viewSchedulePermutations(View v){ 
    	
    	this.last_view.add(0, SCHEDULER);
    	viewScheduleTask = (ViewScheduleTask) new ViewScheduleTask().execute(scheduler);
    	
    	/*
    	// Calculate scheduler permutations
    	scheduler.generatePermutations();
    	
    	// If no working schedules
    	if(scheduler.getWorkingSchedules().size() <= 0) return;
    	List<List<Course>> workingSchedules = scheduler.getWorkingSchedules();
    	
    	*/
    	
    	/*
    	Course math = new Course();
    	math.setCourseID("MATH-102");
    	math.setCourseName("Calc 2");
    	math.setCredits(4);
    	math.setCRN(333333);
    	math.setDays("MT");
    	math.setInstructor("Stock");
    	math.setLocation("AB-3320");
    	math.setSection("01");
    	math.setTime("4:00 pm - 5:00 pm");
    	
    	Course math2 = new Course();
    	math2.setCourseID("MATH-102");
    	math2.setCourseName("Calc 2");
    	math2.setCredits(4);
    	math2.setCRN(333333);
    	math2.setDays("WF");
    	math2.setInstructor("Stock");
    	math2.setLocation("AB-3320");
    	math2.setSection("02");
    	math2.setTime("2:00 pm - 3:00 pm");
    	  	
    	Course cs = new Course();
    	cs.setCourseID("CS-101");
    	cs.setCourseName("Comp and Algo");
    	cs.setCredits(4);
    	cs.setCRN(444444);
    	cs.setDays("MW");
    	cs.setInstructor("Stanchev");
    	cs.setLocation("AB-3500");
    	cs.setSection("01");
    	cs.setTime("11:00 am - 12:00 pm");
    	
    	List<List<Course>> workingSchedules = new ArrayList<List<Course>>();
    	List<Course> set1 = new ArrayList<Course>();
    	List<Course> set2 = new ArrayList<Course>();
    	
    	set1.add(math);
    	set1.add(cs);
    	set2.add(math2);
    	set2.add(cs);
    	
    	workingSchedules.add(set1);
    	workingSchedules.add(set2);
    	*/
		
    	/*
    	setContentView(R.layout.scheduler_view_main);	    
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	
    	
    	this.schedulerIndexer = (Spinner) findViewById(R.id.scheduleIndex);
        
        schedulerIndexer.setOnItemSelectedListener(new OnItemSelectedListener() {
           	@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
           		schedulerPager.setCurrentItem(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				//  Auto-generated method stub
				
			}

        });
        
        // Clear courses
	    List<String> earliestTimes = new ArrayList<String>();
	    List<String> latestTimes = new ArrayList<String>();
	    
	    int latestTime;
	    int earliestTime;
        
        List<String> indexerList = new ArrayList<String>();
        
        
        for(int i = 1; i <= workingSchedules.size(); i++){
        	
        	latestTime = 8;
    		earliestTime = 8;
            
        	
        	for(int j = 0; j < workingSchedules.get(i - 1).size(); j++){
        		
        		Course current = workingSchedules.get(i - 1).get(j);
        		
        		TimeBlock newBlock = TimeBlock.convertToTimeBlock(current);
		    	
		    	if(newBlock != null){
		    	
		    		// Check for latest time
			    	@SuppressWarnings("deprecation")
					int hoursEnd = newBlock.getEnd().getHours();
			    	if (hoursEnd > latestTime) latestTime = hoursEnd;
		    	}
        		
        	}
        	
        	latestTimes.add(latestTime + "");
        	earliestTimes.add(earliestTime + "");
        	indexerList.add(i + "/" + workingSchedules.size());
        }
        
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, indexerList);
        schedulerIndexer.setAdapter(spinnerAdapter);
        
        SchedulerPagerAdapter adapter = new SchedulerPagerAdapter(workingSchedules, latestTimes, earliestTimes);
    	
        schedulerPager = (ViewPager) findViewById(R.id.schedulerPager);
        schedulerPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            	
            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
            	schedulerIndexer.setSelection(position);
            }
        });
        schedulerPager.setAdapter(adapter);
        schedulerPager.setCurrentItem(0);
        this.load_dialog.dismiss();
        */
    }
    
    /********************************************************************
     * Method: addSchedulerClass
     * Purpose: adds a class to the scheduler screen and data
    /*******************************************************************/
    public void addSchedulerClass(View v){
    	
    	Spinner classSpin = (Spinner) findViewById(R.id.classes);
    	LinearLayout container = (LinearLayout) findViewById(R.id.classContainer);
    	LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	Button view = (Button) findViewById(R.id.viewSchedulesButton);
    	
    	this.scheduler.getCurrentCourseList().add(scheduler.getDynamicCourseIDs().get(classSpin.getSelectedItemPosition()));
    	
    	RelativeLayout classCont = (RelativeLayout) inflater.inflate(R.layout.scheduler_class, null);
    	TextView className = (TextView) classCont.findViewById(R.id.className);
    	TextView classTitle = (TextView) classCont.findViewById(R.id.classTitle);
    	//TextView courseNumber = (TextView) classCont.findViewById(R.id.courseNumber);
    	className.setText(scheduler.getDynamicCourseIDs().get(classSpin.getSelectedItemPosition()));
    	classTitle.setText(scheduler.getDynamicCourseIDs().get(classSpin.getSelectedItemPosition())
    			+ ":  " + scheduler.getDynamicCourseNames().get(classSpin.getSelectedItemPosition()));
    	
    	ImageView delete = (ImageView) classCont.findViewById(R.id.removeClass);
    	delete.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    		    
    			LinearLayout parent = (LinearLayout) v.getParent().getParent();
    		    String removeID = (String) ((TextView) ((RelativeLayout) v.getParent()).findViewById(R.id.className)).getText();
    		    parent.removeView((RelativeLayout)v.getParent());
    		    scheduler.getCurrentCourseList().remove(removeID);
    		}
    	});
    	
    	view.setEnabled(true);
    	//courseNumber.setText("Course #" + scheduler.getCurrentCourseList().size());
    	
    	container.addView(classCont);
    }
    
    
    /********************************************************************
     * Method: librarySearch
     * Purpose: searches for faculty / students
    /*******************************************************************/
    public void librarySearch(View v){
    	
    	((LinearLayout) findViewById(R.id.libraryContainer)).removeAllViews();
		
    	libraryTask = (LibraryTask) new LibraryTask().execute(lib);
    }
    
    
    /********************************************************************
     * Task: Login
     * Purpose: task to log user into blackboard and banner
    /*******************************************************************/
    private class Login extends AsyncTask<Student, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(Student... user) {  
    		
        	// Parameters
        	user[0].setUsername(((EditText) findViewById(R.id.signinField)).getText().toString());
        	user[0].setPassword(((EditText) findViewById(R.id.passwordField)).getText().toString());
        	
        	// Login
        	if(user[0].loginBanner()) return user[0].loginBlackboard();
        	else return false;
        }      

        @Override
        protected void onPostExecute(Boolean result) {
        	
			if(result){
				((TextView) findViewById(R.id.loginNotification)).setText("");
				homeScreen();
			}

			// Failed
			else ((TextView) findViewById(R.id.loginNotification)).setText("Login failed.");
			
			if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
    }

    /********************************************************************
     * Task: TransferSearchTask
     * Purpose: gets college list and shows page
    /*******************************************************************/
    private class TransferSearchTask extends AsyncTask<TransferDirectory, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(TransferDirectory... trans) {  
    		
    		// Elements
    		EditText searchBar = (EditText) findViewById(R.id.searchField);
    		Spinner collegeList = (Spinner) findViewById(R.id.college);
    		
    		// Type
    		if(trans[0].getType() == trans[0].BYCOLLEGE) return trans[0].searchByCollege(trans[0].getCodeByName(String.valueOf(collegeList.getSelectedItem())), collegeList.getSelectedItemPosition());
    		else if(trans[0].getType() == trans[0].BYCOURSE) return trans[0].searchByCourse(searchBar.getText().toString());
    		
    		else return false;
        }      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	if(success) {
        		
        		
        		// Show
        		setContentView(R.layout.transfer);
        		populateTransfer();
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }

    
    /********************************************************************
     * Task: FinalMidtermRetrival
     * Purpose: gets college list and shows page
    /*******************************************************************/
    private class FinalMidtermRetrival extends AsyncTask<Student, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(Student... student) {  
    		
    		// Elements
    		Spinner termList = (Spinner) findViewById(R.id.acceptedTerms);
    		
    		// Type
    		if(student[0].getGradesPage() == 1) return student[0].storeFinalGrades(student[0].getFinalCodeByName(String.valueOf(termList.getSelectedItem())), termList.getSelectedItemPosition());
    		else return student[0].storeMidtermGrades(student[0].getMidtermCodeByName(String.valueOf(termList.getSelectedItem())), termList.getSelectedItemPosition());
    		
        }      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	if(success) {

        		// Show
        		setContentView(R.layout.grades);
        		populateGrades();
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    
    
    
    /********************************************************************
     * Task: TransferTask
     * Purpose: gets college list and shows transfer page
    /*******************************************************************/
    private class TransferTask extends AsyncTask<TransferDirectory, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(TransferDirectory... trans) {  
    		
    		if(!trans[0].getIsListLoaded()) return trans[0].storeCollegeList();
    		else return true;
        }      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	if(success) {
        		
        		// Show
        		setContentView(R.layout.transfer);
        		populateTransfer();
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    

    /********************************************************************
     * Task: DirectoryTask
     * Purpose: task to search a directory
    /*******************************************************************/
    private class DirectoryTask extends AsyncTask<Directory, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(Directory... directory) {  
    		
    		// Elements
    		Spinner type = (Spinner) findViewById(R.id.type);
    		EditText query = (EditText) findViewById(R.id.searchField);
    		
    		return directory[0].search(query.getText().toString(), String.valueOf(type.getSelectedItem()));
        }      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	// Show
        	if(success) populateSearch();
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    
    /********************************************************************
     * Task: LibraryTask
     * Purpose: task to search library
    /*******************************************************************/
    private class LibraryTask extends AsyncTask<Library, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(Library... library) {  
    	
    		// Elements
    		Spinner type = (Spinner) findViewById(R.id.type);
    		EditText query = (EditText) findViewById(R.id.searchField);
    		
    		return library[0].search(query.getText().toString(), String.valueOf(type.getSelectedItem()));
        }      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	// Show
        	if(success) populateLibrary();
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    
    
    /********************************************************************
     * Task: EventArticleTask
     * Purpose: task to display an event article
    /*******************************************************************/
    private class EventArticleTask extends AsyncTask<String, Void, Event> {
    	
    	@Override
        protected Event doInBackground(String... params) {  
    		
			try {

				if(!events.getEvents().get(Integer.parseInt(params[2])).getEvents().get(Integer.parseInt(params[1])).getIsLoaded()){
				
					// Connection
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(params[0]);
					String html = HTMLParser.parse(client.execute(get));
					Document doc = Jsoup.parse(html);
					
					// HTML
					if(doc.select("div.content.clearfix").size() > 0) html = doc.select("div.content.clearfix").get(0).toString();
					else html = "Unable to load.";
					
					// Store HTML
					events.getEvents().get(Integer.parseInt(params[2])).getEvents().get(Integer.parseInt(params[1])).setHTML(html);
					events.getEvents().get(Integer.parseInt(params[2])).getEvents().get(Integer.parseInt(params[1])).setIsLoaded(true);
					
					return events.getEvents().get(Integer.parseInt(params[2])).getEvents().get(Integer.parseInt(params[1]));
				}
				
				else return events.getEvents().get(Integer.parseInt(params[2])).getEvents().get(Integer.parseInt(params[1]));
			}
			
			catch (Exception e){ e.printStackTrace(); return null;}
        }      

    	@Override
        protected void onPostExecute(Event event) {
        	
        	if(event != null){
        		
        		// Show
        		setContentView(R.layout.events_article);
        		WebView webView = (WebView) findViewById(R.id.webView);
        		webView.loadDataWithBaseURL("http://kettering.edu", "<body style=\"background:#" + BG_WEB_COLOR + "\">" + event.getHTML() + "</body>", "text/html", null, null);
        		
        		// Set title
        		TextView title = (TextView) findViewById(R.id.event_title);
        		title.setText(event.getSummary());;
        		
        		last_view.add(0, EVENTS);
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    
    
    /********************************************************************
     * Task: NewsArticleTask
     * Purpose: task to display a news article
    /*******************************************************************/
    private class NewsArticleTask extends AsyncTask<String, Void, String> {
    	
    	@Override
        protected String doInBackground(String... params) {  
    		
			try {
				
				if(!news.getNews().get(Integer.parseInt(params[1])).getIsLoaded()){
		
					// Connection
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(params[0]);
					String html = HTMLParser.parse(client.execute(get));
					Document doc = Jsoup.parse(html);
					
					// HTML
					if(doc.select("div.article-wrapper.content.clearfix").size() > 0) html = doc.select("div.article-wrapper.content.clearfix").get(0).toString();
					else html = "Unable to load.";
					
					// Store HTML
					news.getNews().get(Integer.parseInt(params[1])).setHTML(html);
					news.getNews().get(Integer.parseInt(params[1])).setIsLoaded(true);
					
					return html;
				}
				
				else return news.getNews().get(Integer.parseInt(params[1])).getHTML();
			}
			catch (Exception e){ e.printStackTrace(); return null;}
        }      

    	@Override
        protected void onPostExecute(String html) {
        	
        	if(html != null){
        		
        		// Show
        		setContentView(R.layout.news_article);
        		WebView webView = (WebView) findViewById(R.id.webView);
        		webView.loadDataWithBaseURL("http://kettering.edu", "<body style=\"background:#" + BG_WEB_COLOR+ "\">" + html + "</body>", "text/html", null, null);
        		
        		last_view.add(0, NEWS);
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    
    
    /********************************************************************
     * Task: LibraryTask
     * Purpose: task to search library
    /*******************************************************************/
    private class LibraryPageTask extends AsyncTask<Library, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(Library... library) {  
    		
    		return library[0].storeNextPage();
        }      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	// Show
        	if(success) populateLibrary();
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    
    
    /********************************************************************
     * Task: NewsTask
     * Purpose: task to store events
    /*******************************************************************/
    private class NewsTask extends AsyncTask<News, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(News... news) {  
    		
    		// Load
    		if(news[0].getIsLoaded()) return true;
    		else return news[0].store();
        }      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	if(success){
	        	
        		// Show
        		setContentView(R.layout.news);
	        	populateNews();
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    
    
    /********************************************************************
     * Task: EventsTask
     * Purpose: task to store events
    /*******************************************************************/
    private class EventsTask extends AsyncTask<Events, Void, Boolean>{
    	
    	@Override
        protected Boolean doInBackground(Events... events) {  
    		
    		// Load
    		if(events[0].getIsLoaded()) return true;
    		else return events[0].store();
        }      

    	
        @Override
        protected void onPostExecute(Boolean success) {
        	
        	if(success && !isCancelled()){
	        	
        		// Show
        		setContentView(R.layout.events);
	        	populateEvents();
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { 
        	load_dialog.show(); 
        }

        @Override
        protected void onProgressUpdate(Void... values) { }
    }
    
    /********************************************************************
     * Task: NewsPageTask
     * Purpose: task to store next page news
    /*******************************************************************/
    private class NewsPageTask extends AsyncTask<News, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(News... news) { 
    	
    		return news[0].storeNextPage(); 
    	}      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	if(success){

        		// Show
        		setContentView(R.layout.news);
	        	populateNews();
	        	
	        	// Scroll
	        	final ScrollView newsScroll = (ScrollView) findViewById(R.id.newsScroll);
	        	newsScroll.post(new Runnable() { public void run() { newsScroll.fullScroll(ScrollView.FOCUS_DOWN); } });
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    
    
    /********************************************************************
     * Task: EventsPageTask
     * Purpose: task to store next page events
    /*******************************************************************/
    private class EventsPageTask extends AsyncTask<Events, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(Events... events) { 
    	
    		return events[0].storeNextPage(); 
    	}      

        @Override
        protected void onPostExecute(Boolean success) {
        	
        	if(success){
	        
        		// Show
        		setContentView(R.layout.events);
	        	populateEvents();
	        	
	        	// Scroll down
	        	final ScrollView eventsScroll = (ScrollView) findViewById(R.id.eventsScroll);
	        	eventsScroll.post(new Runnable() { public void run() { eventsScroll.fullScroll(ScrollView.FOCUS_DOWN); } });
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
    }


    /********************************************************************
     * Task: StoreScheduleTermTask
     * Purpose: task to load terms 
    /*******************************************************************/
    public class StoreScheduleTermTask extends AsyncTask<DynamicCourses, Void, Boolean> {
    	
        protected Boolean doInBackground(DynamicCourses... scheduler) {  
    		
        	
        	Spinner termSpin = (Spinner) findViewById(R.id.terms);
        	
    		// Load
    		scheduler[0].setTerm(termSpin.getSelectedItemPosition());
    		return scheduler[0].storeDynamicCourses(this);
        }      

        protected void onPostExecute(Boolean success) {
        	
    		if(success){
	        
        		// Show
        		setContentView(R.layout.scheduler);
    			
    			// Show
	            populateScheduler();
    		}
    		
            if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }

    /********************************************************************
     * Task: ViewScheduleTask
     * Purpose: view schedule permutations 
    /*******************************************************************/
    private class ViewScheduleTask extends AsyncTask<DynamicCourses, Void, Boolean> {
    	
        protected Boolean doInBackground(DynamicCourses... scheduler) {  
    		
        	scheduler[0].generatePermutations();
        	
        	// If no working schedules
        	return (scheduler[0].getWorkingSchedules().size() > 0);
        }      

        protected void onPostExecute(Boolean success) {
        	
    		if(success){
	        
    			setContentView(R.layout.scheduler_view_main);	    
    	    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	    	
    	    	/*
    	    	Course math = new Course();
    	    	math.setCourseID("MATH-102");
    	    	math.setCourseName("Calc 2");
    	    	math.setCredits(4);
    	    	math.setCRN(333333);
    	    	math.setDays("MT");
    	    	math.setInstructor("Stock");
    	    	math.setLocation("AB-3320");
    	    	math.setSection("01");
    	    	math.setTime("4:00 pm - 5:00 pm");
    	    	
    	    	Course math2 = new Course();
    	    	math2.setCourseID("MATH-102");
    	    	math2.setCourseName("Calc 2");
    	    	math2.setCredits(4);
    	    	math2.setCRN(333333);
    	    	math2.setDays("WF");
    	    	math2.setInstructor("Stock");
    	    	math2.setLocation("AB-3320");
    	    	math2.setSection("02");
    	    	math2.setTime("2:00 pm - 3:00 pm");
    	    	  	
    	    	Course cs = new Course();
    	    	cs.setCourseID("CS-101");
    	    	cs.setCourseName("Comp and Algo");
    	    	cs.setCredits(4);
    	    	cs.setCRN(444444);
    	    	cs.setDays("MW");
    	    	cs.setInstructor("Stanchev");
    	    	cs.setLocation("AB-3500");
    	    	cs.setSection("01");
    	    	cs.setTime("11:00 am - 12:00 pm");
    	    	
    	    	List<List<Course>> workingSchedules = new ArrayList<List<Course>>();
    	    	
    	    	List<Course> set1 = new ArrayList<Course>();
    	    	List<Course> set2 = new ArrayList<Course>();
    	    	
    	    	set1.add(math);
    	    	set1.add(cs);
    	    	set2.add(math2);
    	    	set2.add(cs);
    	    	
    	    	workingSchedules.add(set1);
    	    	workingSchedules.add(set2);
    	    	*/
    	    	
    	    	List<List<Course>> workingSchedules = scheduler.getWorkingSchedules();
    	    	
    	    	schedulerIndexer = (Spinner) findViewById(R.id.scheduleIndex);
    	        
    	        schedulerIndexer.setOnItemSelectedListener(new OnItemSelectedListener() {
    	           	@Override
    				public void onItemSelected(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3) {
    					
    	           		schedulerPager.setCurrentItem(arg2);
    				}

    				@Override
    				public void onNothingSelected(AdapterView<?> arg0) {}

    	        });
    	        
    	        
    	        // Clear courses
    		    int[] earliestTimes = new int[workingSchedules.size()];
    		    int[] latestTimes = new int[workingSchedules.size()];
    		    
    		    int latestTime;
    		    int earliestTime;
    	        
    	        List<String> indexerList = new ArrayList<String>();
    	        
    	        for(int i = 1; i <= workingSchedules.size(); i++){
    	        	
    	        	latestTime = 8;
    	    		earliestTime = 8;
    	            
    	        	
    	        	for(int j = 0; j < workingSchedules.get(i - 1).size(); j++){
    	        		
    	        		Course current = workingSchedules.get(i - 1).get(j);
    	        		
    	        		TimeBlock newBlock = TimeBlock.convertToTimeBlock(current.getTime(), current);
    			    	
    			    	if(newBlock != null){
    			    	
    			    		// Check for latest time
    						int hoursEnd = newBlock.getEndHours();
    				    	if (hoursEnd > latestTime) latestTime = hoursEnd;
    			    	}
    	        		
    	        	}
    	        	
    	        	latestTimes[i-1] = latestTime;
    	        	earliestTimes[i-1] = earliestTime;
    	        	indexerList.add(i + " / " + workingSchedules.size());
    	        }
    	        
    	        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(c,android.R.layout.simple_spinner_item, indexerList);
    	        //spinnerAdapter.setDropDownViewResource(R.layout.schedule_index);
    	        schedulerIndexer.setAdapter(spinnerAdapter);
    	        
    	        //TextView scheduleMax = (TextView) findViewById(R.id.scheduleMax);
    	        //scheduleMax.setText(" / " + workingSchedules.size());
    	        
    	        SchedulerPagerAdapter adapter = new SchedulerPagerAdapter(workingSchedules, latestTimes, earliestTimes);
    	        adapter.setContext(c);
    	    	
    	        schedulerPager = (ViewPager) findViewById(R.id.schedulerPager);
    	        schedulerPager.setOnPageChangeListener(new OnPageChangeListener() {
    	            public void onPageScrollStateChanged(int state) {
    	            	
    	            }
    	            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    	            public void onPageSelected(int position) {
    	            	schedulerIndexer.setSelection(position);
    	            }
    	        });
    	        
    	        schedulerPager.setAdapter(adapter);
    	        schedulerPager.setCurrentItem(0);
    		}
    		
            if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }

    
    /********************************************************************
     * Task: CourseSchedulerTask
     * Purpose: task to load terms 
    /*******************************************************************/
    private class CourseSchedulerTask extends AsyncTask<DynamicCourses, Void, Boolean> {
    	
        protected Boolean doInBackground(DynamicCourses... scheduler) {  
    		
    		// Load
    		if(scheduler[0].getTermsLoaded()) return true;
    		else return scheduler[0].storeTerms();
        }      

        protected void onPostExecute(Boolean success) {
        	
    		if(success){
    			last_view.add(HOME);
    			
        		// Show
        		setContentView(R.layout.scheduler);
    			
    			// Show
	            populateScheduler();
    		}
    		
            if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }

        
    /********************************************************************
     * Task: ScheduleTask
     * Purpose: task to store schedule
    /*******************************************************************/
    private class ScheduleTask extends AsyncTask<Student, Void, Boolean> {
    	
    	@SuppressWarnings("deprecation") @Override
        protected Boolean doInBackground(Student... student) {  
    		
    		int year = Calendar.getInstance().getTime().getYear() + 1900;
    		int month = Calendar.getInstance().getTime().getMonth();
    		String term = year + "";
    		
    		if (month >= 0 && month <= 2) term += "01";
    		else if (month >= 3 && month <= 5) term += "02";
    		else if (month >= 6 && month <= 8) term += "03";
    		else if (month >= 9 && month <= 11) term += "04";
    		
    		// Load
    		if(student[0].getScheduleLoaded()) return true;
    		else return student[0].storeSchedule(term);
        }      

        protected void onPostExecute(Boolean success) {
        	
    		if(success){
	        
    			// Show
	            populateSchedule();
    		}
    		
            if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    

    /********************************************************************
     * Task: GradeSingleTask
     * Purpose: task to store and display a single course grade
    /*******************************************************************/
    private class GradeSingleTask extends AsyncTask<String, Void, String> {
    	
    	@Override
        protected String doInBackground(String... params) {  
    		
    		
			try {
				
				if(!student.getCurrentGrades().get((Integer.parseInt(params[0]))).getIsLoaded()){
					
					if(student.storeGradeItem((Integer.parseInt(params[0])))) return params[0];
					else return null;
				}
				
				else return params[0];
			}
			catch (Exception e){ e.printStackTrace(); return null;}
			
        }      

    	@Override
        protected void onPostExecute(String param) {
        	
        	if(param != null){
        		
        		last_view.add(0, GRADES);
        		
        		CurrentGrade grade = student.getCurrentGrades().get(Integer.parseInt(param));
        		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        		List<CurrentGradeItem> items = grade.getGradeItems();
        		
        		// Show
        		setContentView(R.layout.single_grade);
       
        		TextView course_title = (TextView) findViewById(R.id.currentGradeName); 		
        		LinearLayout grade_container = (LinearLayout) findViewById(R.id.gradesContainer);
        		course_title.setText(grade.getCourseName());
        		
        		for(CurrentGradeItem item : items){
        			
        			TableLayout current_view = (TableLayout) inflater.inflate(R.layout.grade_item, null);
        			TextView title = (TextView) current_view.findViewById(R.id.title);
        			TextView total = (TextView) current_view.findViewById(R.id.total);
        			TextView possible = (TextView) current_view.findViewById(R.id.possible);
        			
        			title.setText(item.getGradeName());
        			total.setText(item.getScore() + "");
        			possible.setText(item.getPointsPossible() + "");
        			
        			grade_container.addView(current_view);
        		}
        		
        	}
        	
        	if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }

    
    /********************************************************************
     * Task: GradesTask
     * Purpose: task to store grades (current, midterm, final)
    /*******************************************************************/
    private class GradesTask extends AsyncTask<Student, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(Student... student) {  
    		
    		if(student[0].getGradesPage() == 0){
    			
    			if(student[0].getCurrentGradesLoaded()) return true;
    			else return student[0].storeCurrentGrades();
    		}
    		
    		else if(student[0].getGradesPage() == 1){

    			if(student[0].getFinalListLoaded()) return true; 
    			else return student[0].storeFinalTermList();
    		}
    		
    		else {
    			if(student[0].getMidtermListLoaded()) return true;
    			else return student[0].storeMidtermList();
    		}
        }      

        protected void onPostExecute(Boolean success) {
        	
    		if(success){       

    			// Show
    			setContentView(R.layout.grades);
	            populateGrades();
    		}
    		
            if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }

    
    /********************************************************************
     * Task: AccountTask
     * Purpose: task to get current account information
    /*******************************************************************/
    private class AccountTask extends AsyncTask<Student, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(Student... student) {  

    		if(student[0].getAccountLoaded()) return true;
			else return student[0].storeAccount();
    	}      

        protected void onPostExecute(Boolean success) {
        	
    		if(success){       

    			// Show
    			setContentView(R.layout.account);
	            populateAccount();
    		}
    		
            if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }

    
    
    /********************************************************************
     * Task: KUMenuTask
     * Purpose: task to store menu
    /*******************************************************************/
    /*
    private class KUMenuTask extends AsyncTask<KUMenu, Void, Boolean> {
    	
    	@Override
        protected Boolean doInBackground(KUMenu... menu) {  
    		
    		// Load
    		if(menu[0].getIsLoaded()) return true;
    		else return menu[0].storeMenu();
        }      

    	@SuppressWarnings("deprecation")
		@Override
        protected void onPostExecute(Boolean success) {
        	
    		if(success){
	        
    			// Show
    			setContentView(R.layout.menu);
	            populateMenu(Calendar.getInstance().getTime().getDay());
    		}
    		
            if(load_dialog.isShowing()) load_dialog.dismiss();
		}

        @Override
        protected void onPreExecute() { load_dialog.show(); }

        @Override
        protected void onProgressUpdate(Void... values) { }
        
    }
    */
}

class SchedulerPagerAdapter extends PagerAdapter {
	
	// Properties
	List<TimeBlock> monday;
	List<TimeBlock> tuesday;
	List<TimeBlock> wednesday;
	List<TimeBlock> thursday;
	List<TimeBlock> friday;
	LayoutInflater inflater;
	List<List<Course>> workingCourses;
	int[] latestTimes;
	int[] earliestTimes;
	ViewPager schedluerPager;
	Context c;
	//Scheduler c;
	
	// Constants
	int ROWSIZE =  5;
	final String[] COLORS = {"#FFDEAD", "#87CEEB","#8FBC8F", 
			"#F0E68C", "#FFC0CB", "#D8BFD8", "#BFEFFF", "#C1FFC1", "#BCEE68", "#FFEC8B"};
	boolean creation = false;
	
	public void setContext (Context c){
		this.c = c;
		ROWSIZE=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, c.getResources().getDisplayMetrics());
	}
	
	public SchedulerPagerAdapter(List<List<Course>> workingCourses, int[] latestTimes, int[] earliestTimes){
		
		this.workingCourses = workingCourses;
		this.latestTimes = latestTimes;
		this.earliestTimes = earliestTimes;

	}
	
    public int getCount() {
        return this.workingCourses.size();
    }

    public Object instantiateItem(View collection, int position) {

    	int latestTime = this.latestTimes[position];
	    int earliestTime = this.earliestTimes[position]; 
	    
        int resId = R.layout.schedule_view;
        
        inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View view = inflater.inflate(resId, null);
        
        sortCoursesToDay(position);
        
        // Day Containers
	    LinearLayout mondayView = (LinearLayout) view.findViewById(R.id.mondayContainer);
	    LinearLayout tuesdayView = (LinearLayout) view.findViewById(R.id.tuesdayContainer);
	    LinearLayout wednesdayView = (LinearLayout) view.findViewById(R.id.wednesdayContainer);
	    LinearLayout thursdayView = (LinearLayout) view.findViewById(R.id.thursdayContainer);
	    LinearLayout fridayView = (LinearLayout) view.findViewById(R.id.fridayContainer);
	    
	    // Time fields
	    TextView time8am  = (TextView) view.findViewById(R.id.time8am);
	    TextView time9am  = (TextView)  view.findViewById(R.id.time9am);
	    TextView time10am = (TextView) view.findViewById(R.id.time10am);
	    TextView time11am = (TextView) view.findViewById(R.id.time11am);
	    TextView time12pm = (TextView) view.findViewById(R.id.time12pm);
	    TextView time1pm  = (TextView) view.findViewById(R.id.time1pm);
	    TextView time2pm  = (TextView) view.findViewById(R.id.time2pm);
	    TextView time3pm  = (TextView) view.findViewById(R.id.time3pm);
	    TextView time4pm  = (TextView) view.findViewById(R.id.time4pm);
	    TextView time5pm  = (TextView) view.findViewById(R.id.time5pm);
	    TextView time6pm  = (TextView) view.findViewById(R.id.time6pm);
	    TextView time7pm  = (TextView) view.findViewById(R.id.time7pm);
	    TextView time8pm  = (TextView) view.findViewById(R.id.time8pm);
	    TextView time9pm  = (TextView) view.findViewById(R.id.time9pm);
	    
	    
	    // Remove times greater than the latestTime
	    if(latestTime >= 8 && earliestTime <= 8) time8am.setHeight(12 * ROWSIZE);
	    else time8am.setVisibility(View.GONE);
	    if(latestTime >= 9 && earliestTime <=  9) time9am.setHeight(12 * ROWSIZE);
	    else time9am.setVisibility(View.GONE);
	    if(latestTime >= 10 && earliestTime <=  10) time10am.setHeight(12 * ROWSIZE);
	    else time10am.setVisibility(View.GONE);
	    if(latestTime >= 11 && earliestTime <=  11) time11am.setHeight(12 * ROWSIZE);
	    else time11am.setVisibility(View.GONE);
	    if(latestTime >= 12 && earliestTime <=  12) time12pm.setHeight(12 * ROWSIZE);
	    else time12pm.setVisibility(View.GONE);
	    if(latestTime >= 13 && earliestTime <=  13) time1pm.setHeight(12 * ROWSIZE);
	    else time1pm.setVisibility(View.GONE);
	    if(latestTime >= 14 && earliestTime <=  14) time2pm.setHeight(12 * ROWSIZE);
	    else time2pm.setVisibility(View.GONE);
	    if(latestTime >= 15 && earliestTime <=  15) time3pm.setHeight(12 * ROWSIZE);
	    else time3pm.setVisibility(View.GONE);
	    if(latestTime >= 16 && earliestTime <=  16) time4pm.setHeight(12 * ROWSIZE);
	    else time4pm.setVisibility(View.GONE);
	    if(latestTime >= 17 && earliestTime <=  17) time5pm.setHeight(12 * ROWSIZE);
	    else time5pm.setVisibility(View.GONE);
	    if(latestTime >= 18 && earliestTime <=  18) time6pm.setHeight(12 * ROWSIZE);
	    else time6pm.setVisibility(View.GONE);
	    if(latestTime >= 19 && earliestTime <=  19) time7pm.setHeight(12 * ROWSIZE);
	    else time7pm.setVisibility(View.GONE);
	    if(latestTime >= 20 && earliestTime <=  20) time8pm.setHeight(12 * ROWSIZE);
	    else time8pm.setVisibility(View.GONE);
	    if(latestTime >= 21 && earliestTime <=  21) time9pm.setHeight(12 * ROWSIZE);
	    else time9pm.setVisibility(View.GONE);
	    
	    
	    TimeBlock latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Monday
	    for(TimeBlock course : monday) latestBlock = addTimeBlock(mondayView, course, latestBlock);
	    latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Tuesday
	    for(TimeBlock course : tuesday) latestBlock = addTimeBlock(tuesdayView, course, latestBlock);
	    latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Wednesday
	    for(TimeBlock course : wednesday) latestBlock = addTimeBlock(wednesdayView, course, latestBlock);
	    latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Thursday
	    for(TimeBlock course : thursday) latestBlock = addTimeBlock(thursdayView, course, latestBlock);
	    latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Friday
	    for(TimeBlock course : friday) latestBlock = addTimeBlock(fridayView, course, latestBlock);     
        
        ((ViewPager) collection).addView(view, 0);
        
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }

    @Override
    public Parcelable saveState() {
        return null;
    }
    
    /********************************************************************
	 * Method: addTimeBlock
	 * Purpose: adds a time block to the layout, then returns added block
	/*******************************************************************/
	public TimeBlock addTimeBlock(LinearLayout day, TimeBlock course, TimeBlock latestBlock){
		
		// Calculate heights
		int spacerHeight = latestBlock.compareToRowEnd(course) * this.ROWSIZE;
    	int height = course.getRowSpan() * this.ROWSIZE;
    	
    	
    	// Inflate spacer and class
    	TextView spacer = (TextView) this.inflater.inflate(R.layout.course, null);
    	TextView current = (TextView) this.inflater.inflate(R.layout.course, null);
    	
    	
    	// Set course info. Secretly hide additional info in hint.
    	current.setText(Html.fromHtml(course.getCourse().getCourseID() + "</b><br>CRN: " + course.getCourse().getCRN() + "<br>" + course.getCourse().getTime()));
    	current.setHint(Html.fromHtml(course.getCourse().getCourseName() + "</b><br>" + course.getCourse().getInstructor()));
    	current.setBackgroundColor(Color.parseColor(course.getCourse().getColor()));
    	
    	// Set height
    	current.setHeight(height);
    	spacer.setHeight(spacerHeight);	
    	
    	// Listener for clicking of a course
    	current.setOnClickListener(new OnClickListener() { 
    		public void onClick(View v) { 
				
    			TextView current = (TextView) v;
				
    			// Switch hint and text
				CharSequence hint = current.getHint();
				CharSequence text = current.getText();
				current.setText(hint);
				current.setHint(text);
			} 
    	});
    	
    	// Add components
    	day.addView(spacer);
    	day.addView(current);
    	
    	return course;
	}
    
	/********************************************************************
	 * Method: sortCoursesToDay
	 * Purpose: stores courses in order of time insider respective days
	/*******************************************************************/
	private void sortCoursesToDay(int index){
		
		// Days of the week
		this.monday = new ArrayList<TimeBlock>();
		this.tuesday = new ArrayList<TimeBlock>();
		this.wednesday = new ArrayList<TimeBlock>();
		this.thursday = new ArrayList<TimeBlock>();
		this.friday = new ArrayList<TimeBlock>();
		
		for(int i = 0; i < this.workingCourses.get(index).size(); i++){
			
			// Get course and timeblock
			Course current = this.workingCourses.get(index).get(i);
			TimeBlock newBlock = TimeBlock.convertToTimeBlock(current.getTime(), current);
			
			if (newBlock != null){

				// Color
				if(i < COLORS.length) current.setColor(COLORS[i]);
				else current.setColor(COLORS[0]);
			
				// Add to correct block(s)
				if(current.getDays().contains("M")) monday.add(newBlock);
				if(current.getDays().contains("T")) tuesday.add(newBlock);
				if(current.getDays().contains("W")) wednesday.add(newBlock);
				if(current.getDays().contains("R")) thursday.add(newBlock);
				if(current.getDays().contains("F")) friday.add(newBlock);
			}
		}	
		
		// Sort each day
		Collections.sort(this.monday);
		Collections.sort(this.tuesday);
		Collections.sort(this.wednesday);
		Collections.sort(this.thursday);
		Collections.sort(this.friday);
	}
}
