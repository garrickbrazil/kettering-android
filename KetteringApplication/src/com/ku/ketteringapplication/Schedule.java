package com.ku.ketteringapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/********************************************************************
 * Class: Schedule
 * Purpose: creates and displays a Kettering schedule
/*******************************************************************/
public class Schedule extends Activity{
	
	// Properties
	private List<Course> courses;
	private List<TimeBlock> monday;
	private List<TimeBlock> tuesday;
	private List<TimeBlock> wednesday;
	private List<TimeBlock> thursday;
	private List<TimeBlock> friday;
	private LayoutInflater inflater;
	
	// Constants
	private final int ROWSIZE = 5;
	private final String[] COLORS = {"#FFDEAD", "#87CEEB","#8FBC8F", 
			"#F0E68C", "#FFC0CB", "#D8BFD8", "#BFEFFF", "#C1FFC1", "#BCEE68", "#FFEC8B"};
	
	
	/********************************************************************
	 * Method: onCreate
	 * Purpose: method for when application loads
	/*******************************************************************/
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.schedule);
	    this.inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    Gson gson = new Gson();
	    int latestTime = 8;
	    
	    
	    // Clear courses
	    this.courses = new ArrayList<Course>();

	    // Grab course info
	    Bundle extras = getIntent().getExtras();
	    String[] jsonCourses = extras.getStringArray("jsonCourses");
		
	    for(String jsonCourse : jsonCourses){
	    	
	    	// Recreate course list
	    	Course current = gson.fromJson(jsonCourse, Course.class);
	    	if(current != null){
	    		
	    		// Add
	    		courses.add(current);
	    		
	    		TimeBlock newBlock = TimeBlock.convertToTimeBlock(current.getTime(), current);
		    	
		    	if(newBlock != null){
		    	
		    		// Check for latest time
			    	int hours = newBlock.getEndHours();
			    	if (hours > latestTime) latestTime = hours;
		    	}
	    	}
	    }
	    
	    sortCoursesToDay();
	    
	    // Day Containers
	    LinearLayout mondayView = (LinearLayout) findViewById(R.id.mondayContainer);
	    LinearLayout tuesdayView = (LinearLayout) findViewById(R.id.tuesdayContainer);
	    LinearLayout wednesdayView = (LinearLayout) findViewById(R.id.wednesdayContainer);
	    LinearLayout thursdayView = (LinearLayout) findViewById(R.id.thursdayContainer);
	    LinearLayout fridayView = (LinearLayout) findViewById(R.id.fridayContainer);
	    
	    // Time fields
	    TextView time8am = (TextView) findViewById(R.id.time8am);
	    TextView time9am = (TextView) findViewById(R.id.time9am);
	    TextView time10am = (TextView) findViewById(R.id.time10am);
	    TextView time11am = (TextView) findViewById(R.id.time11am);
	    TextView time12pm = (TextView) findViewById(R.id.time12pm);
	    TextView time1pm = (TextView) findViewById(R.id.time1pm);
	    TextView time2pm = (TextView) findViewById(R.id.time2pm);
	    TextView time3pm = (TextView) findViewById(R.id.time3pm);
	    TextView time4pm = (TextView) findViewById(R.id.time4pm);
	    TextView time5pm = (TextView) findViewById(R.id.time5pm);
	    TextView time6pm = (TextView) findViewById(R.id.time6pm);
	    TextView time7pm = (TextView) findViewById(R.id.time7pm);
	    TextView time8pm = (TextView) findViewById(R.id.time8pm);
	    TextView time9pm = (TextView) findViewById(R.id.time9pm);
	    
	    
	    // Remove times greater than the latestTime
	    if(latestTime > 8) time8am.setHeight(12 * this.ROWSIZE);
	    else time8am.setVisibility(View.GONE);
	    if(latestTime > 9) time9am.setHeight(12 * this.ROWSIZE);
	    else time9am.setVisibility(View.GONE);
	    if(latestTime > 10) time10am.setHeight(12 * this.ROWSIZE);
	    else time10am.setVisibility(View.GONE);
	    if(latestTime > 11) time11am.setHeight(12 * this.ROWSIZE);
	    else time11am.setVisibility(View.GONE);
	    if(latestTime > 12) time12pm.setHeight(12 * this.ROWSIZE);
	    else time12pm.setVisibility(View.GONE);
	    if(latestTime > 13) time1pm.setHeight(12 * this.ROWSIZE);
	    else time1pm.setVisibility(View.GONE);
	    if(latestTime > 14) time2pm.setHeight(12 * this.ROWSIZE);
	    else time2pm.setVisibility(View.GONE);
	    if(latestTime > 15) time3pm.setHeight(12 * this.ROWSIZE);
	    else time3pm.setVisibility(View.GONE);
	    if(latestTime > 16) time4pm.setHeight(12 * this.ROWSIZE);
	    else time4pm.setVisibility(View.GONE);
	    if(latestTime > 17) time5pm.setHeight(12 * this.ROWSIZE);
	    else time5pm.setVisibility(View.GONE);
	    if(latestTime > 18) time6pm.setHeight(12 * this.ROWSIZE);
	    else time6pm.setVisibility(View.GONE);
	    if(latestTime > 19) time7pm.setHeight(12 * this.ROWSIZE);
	    else time7pm.setVisibility(View.GONE);
	    if(latestTime > 20) time8pm.setHeight(12 * this.ROWSIZE);
	    else time8pm.setVisibility(View.GONE);
	    if(latestTime > 21) time9pm.setHeight(12 * this.ROWSIZE);
	    else time9pm.setVisibility(View.GONE);
	    
	    
	    TimeBlock latestBlock = new TimeBlock(8,0,8,0);
	    
	    
	    // Monday
	    for(TimeBlock course : this.monday) latestBlock = addTimeBlock(mondayView, course, latestBlock);
	    latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Tuesday
	    for(TimeBlock course : this.tuesday) latestBlock = addTimeBlock(tuesdayView, course, latestBlock);
	    latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Wednesday
	    for(TimeBlock course : this.wednesday) latestBlock = addTimeBlock(wednesdayView, course, latestBlock);
	    latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Thursday
	    for(TimeBlock course : this.thursday) latestBlock = addTimeBlock(thursdayView, course, latestBlock);
	    latestBlock = new TimeBlock(8,0,8,0);
	    
	    // Friday
	    for(TimeBlock course : this.friday) latestBlock = addTimeBlock(fridayView, course, latestBlock); 
	    
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
    	current.setText(Html.fromHtml(course.getCourse().getCourseID() + "</b><br>" + course.getCourse().getLocation().replaceAll("Academic\\sBuilding", "AB") + "<br>" + course.getCourse().getTime()));
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
	private void sortCoursesToDay(){
		
		// Days of the week
		this.monday = new ArrayList<TimeBlock>();
		this.tuesday = new ArrayList<TimeBlock>();
		this.wednesday = new ArrayList<TimeBlock>();
		this.thursday = new ArrayList<TimeBlock>();
		this.friday = new ArrayList<TimeBlock>();
		
		for(int i = 0; i < this.courses.size(); i++){
			
			// Get course and timeblock
			Course current = this.courses.get(i);
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
	
	/********************************************************************
	 * Method: back
	 * Purpose: exit activity when back is pressed
	/*******************************************************************/
	public void back(View view){
    	super.finish();
    }
}