package com.ku.ketteringapplication;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/********************************************************************
 * Class: Schedule
 * Purpose: creates and displays a Kettering schedule
/*******************************************************************/
public class Scheduler extends Activity{
	
	ViewPager schedulerPager;
	Spinner schedulerIndexer;
	
	/********************************************************************
	 * Method: onCreate
	 * Purpose: method for when application loads
	/*******************************************************************/
	@SuppressWarnings("deprecation") 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.scheduler_view_main);	    
	    
	    Gson gson = new Gson();
	    
	    // Clear courses
	    List<String> earliestTimes = new ArrayList<String>();
	    List<String> latestTimes = new ArrayList<String>();
	    
	    int latestTime;
	    int earliestTime;
	    
	    // Grab course info
	    Bundle extras = getIntent().getExtras();
	    String[] jsonCourses = extras.getStringArray("jsonCourses");
		int[] courseIndexes = extras.getIntArray("courseIndexes");

		int currentCount = 0;
		int setIndexer = 0;
		
		latestTime = 8;
		earliestTime = 8;
		
		List<List<Course>> workingSchedules = new ArrayList<List<Course>>();
		List<Course> courseSet = new ArrayList<Course>();
		
		// Example index {3, 5, 3}
		for(int i = 0; i < jsonCourses.length; i++){
	    		
			
			if (currentCount >= courseIndexes[setIndexer]){
			
				// Add
				workingSchedules.add(courseSet);
				latestTimes.add(latestTime + "");
				earliestTimes.add(earliestTime + "");
				
				// Reset
				currentCount = 0;
				setIndexer++;
				latestTime = 8;
				earliestTime = 8;
				courseSet = new ArrayList<Course>();
			}
			
			currentCount++;
			
			String jsonCourse = jsonCourses[i];
			
	    	// Recreate course list
	    	Course current = gson.fromJson(jsonCourse, Course.class);
	    	
	    	if(current != null){
	    		
	    		// Add
	    		courseSet.add(current);
	    		
	    		TimeBlock newBlock = TimeBlock.convertToTimeBlock(current);
		    	
		    	if(newBlock != null){
		    	
		    		// Check for latest time
			    	int hoursEnd = newBlock.getEnd().getHours();
			    	if (hoursEnd > latestTime) latestTime = hoursEnd;
			    	
			    	// Check for earliest time
			    	//int hoursEarly = newBlock.getStart().getHours();
			    	//if (hoursEarly < earliestTime) earliestTime = hoursEarly;
		    	}
	    	}
	    }
		
		if (currentCount >= courseIndexes[setIndexer]){
			
			
			// Add
			workingSchedules.add(courseSet);
			latestTimes.add(latestTime + "");
			earliestTimes.add(earliestTime + "");
			
			// Reset
			currentCount = 0;
			setIndexer++;
			latestTime = 8;
			earliestTime = 21;
			courseSet = new ArrayList<Course>();
		}
	    
		for(List<Course> currentSet : workingSchedules){
			
			for(Course course : currentSet){
				//System.out.println(course);
			}
		}
		
		this.schedulerIndexer = (Spinner) findViewById(R.id.scheduleIndex);
        
        schedulerIndexer.setOnItemSelectedListener(new OnItemSelectedListener() {
           	@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
           		schedulerPager.setCurrentItem(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

        });
        
        List<String> indexerList = new ArrayList<String>();
        
        
        for(int i = 1; i <= workingSchedules.size(); i++){
        	
        	indexerList.add(i + "/" + workingSchedules.size());
        }
        
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, indexerList);
        schedulerIndexer.setAdapter(spinnerAdapter);
		
		System.out.println("BEFORE");
	    SchedulerPagerAdapter adapter = new SchedulerPagerAdapter(workingSchedules, latestTimes, earliestTimes);
	    System.out.println("HERE!");
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
	
	/********************************************************************
	 * Method: back
	 * Purpose: exit activity when back is pressed
	/*******************************************************************/
	public void back(View view){
    	super.finish();
    }
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
	List<String> latestTimes;
	List<String> earliestTimes;
	ViewPager schedluerPager;
	Scheduler c;
	
	// Constants
	final int ROWSIZE = 5;
	final String[] COLORS = {"#FFDEAD", "#87CEEB","#8FBC8F", 
			"#F0E68C", "#FFC0CB", "#D8BFD8", "#BFEFFF", "#C1FFC1", "#BCEE68", "#FFEC8B"};
	boolean creation = false;
	
	public void setContext (ViewPager schedulerPager, Scheduler scheduler){
		this.schedluerPager = schedulerPager;
		this.c = scheduler;
	}
	
	public SchedulerPagerAdapter(List<List<Course>> workingCourses, List<String> latestTimes, List<String> earliestTimes){
		
		this.workingCourses = workingCourses;
		this.latestTimes = latestTimes;
		this.earliestTimes = earliestTimes;

	}
	
    public int getCount() {
        return this.workingCourses.size();
    }

    public Object instantiateItem(View collection, int position) {

    	int latestTime = Integer.parseInt(this.latestTimes.get(position));
	    int earliestTime = Integer.parseInt(this.earliestTimes.get(position)); 
	    
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
	    
	    
	    TimeBlock latestBlock = new TimeBlock(Time.valueOf(earliestTime + ":00:00"),Time.valueOf(earliestTime + ":00:00"));
	    
	    // Monday
	    for(TimeBlock course : monday) latestBlock = addTimeBlock(mondayView, course, latestBlock);
	    latestBlock = new TimeBlock(Time.valueOf(earliestTime + ":00:00"),Time.valueOf(earliestTime + ":00:00"));
	    
	    // Tuesday
	    for(TimeBlock course : tuesday) latestBlock = addTimeBlock(tuesdayView, course, latestBlock);
	    latestBlock = new TimeBlock(Time.valueOf(earliestTime + ":00:00"),Time.valueOf(earliestTime + ":00:00"));
	    
	    // Wednesday
	    for(TimeBlock course : wednesday) latestBlock = addTimeBlock(wednesdayView, course, latestBlock);
	    latestBlock = new TimeBlock(Time.valueOf(earliestTime + ":00:00"),Time.valueOf(earliestTime + ":00:00"));
	    
	    // Thursday
	    for(TimeBlock course : thursday) latestBlock = addTimeBlock(thursdayView, course, latestBlock);
	    latestBlock = new TimeBlock(Time.valueOf(earliestTime + ":00:00"),Time.valueOf(earliestTime + ":00:00"));
	    
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
			TimeBlock newBlock = TimeBlock.convertToTimeBlock(current);
			
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

