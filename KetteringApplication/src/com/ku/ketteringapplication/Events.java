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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/********************************************************************
 * Class: Events
 * Purpose: holds event objects
/*******************************************************************/
public class Events {
	
	private List<EventDay> events;
	private int page;
	private boolean isLoaded;
	final Pattern descPattern = Pattern.compile("(.*)(<div class=\"field-items\">\\s*<div class=\"field-item even field-item-1\">)(.*)(</div>\\s*</div>)(.*)", Pattern.DOTALL);
	
	public Events(){
			
		this.events = new ArrayList<EventDay>();
		this.isLoaded = false;
		this.page = 0;
	}
	
	/********************************************************************
	 * Method: store
	 * Purpose: stores first page into memory
	/*******************************************************************/
	public boolean store(){
		
		try{
			this.events = new ArrayList<EventDay>();
			return this.storeEvents(Jsoup.parse(new URL("http://kettering.edu/events"), 3000).getElementsByClass("event-caption"));
		}
		
		catch(Exception e) {this.events = new ArrayList<EventDay>(); this.isLoaded = false; return false; }
	}
	
	/********************************************************************
	 * Method storeNextPage
	 * Purpose: stores next page of events
	/*******************************************************************/
	public boolean storeNextPage(){
		
		try{
			
			// Execute
			return this.storeEvents(Jsoup.parse(new URL("http://kettering.edu/events?page=" + (++this.page)), 6000).getElementsByClass("event-caption"));
		}
		
		catch(Exception e) {this.events = new ArrayList<EventDay>(); this.isLoaded = false; return false; }
	}
	
	
	public static Bitmap loadBitmap(String url) {
		
		String urldisplay = url;
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) { e.printStackTrace(); }
        return mIcon11;
	}
	
	
	/********************************************************************
	 * Method: storeEvents
	 * Purpose: takes in an list of links and stores them into memory
	/*******************************************************************/
	private boolean storeEvents(Elements events){
		
		try{
			
			if(events.size() > 0){
				EventDay currentDay;
				
				if(this.events.size() > 0)  currentDay = this.events.get(this.events.size()-1);
				else currentDay =  new EventDay();
				
				for(Element event : events){
					
					//Pattern p = Pattern.compile("(^.+)(,\\s)(.*)(\\s)([0-9]+)(,\\s)([0-9][0-9][0-9][0-9])(.*)");
					final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
					
					Event currentEvent = new Event();
					Elements pictureObj = event.getElementsByClass("object"); 
					
					if(pictureObj.size() > 0 && pictureObj.get(0).getElementsByTag("img").size() > 0 && event.getElementsByTag("h3").size() > 0 && event.getElementsByTag("h3").get(0).children().size() > 0 && event.getElementsByTag("h3").get(0).childNode(0).attr("href") != null && event.getElementsByClass("info").size() > 0 && event.getElementsByClass("month").size() > 0 && event.getElementsByClass("day").size() > 0 && event.getElementsByClass("dow").size() > 0){
						
						Element img = pictureObj.get(0).getElementsByTag("img").get(0); img.attr("style", "width:100%; height:100%");
						String imgStr = img.toString();
						String title = event.getElementsByTag("h3").get(0).text();
						String linkStr = "https://www.kettering.edu" + event.getElementsByTag("h3").get(0).child(0).attr("href");
						String info = event.getElementsByClass("info").get(0).text();
						String monthStr = event.getElementsByClass("month").get(0).text();
						String dayStr = event.getElementsByClass("day").get(0).text();
						String dayOfWeek = event.getElementsByClass("dow").get(0).text();
						boolean isValidImage = !img.attr("src").equals("/sites/all/themes/kettering/images/events/default_event_icon.jpg");
						
						Bitmap image;
						
						if(isValidImage){
							image = loadBitmap(img.attr("src"));
							currentEvent.setBitmap(image);
						}
						int month = 0;
						
						for (int i = 0; i < months.length; i++)  if(monthStr.equalsIgnoreCase(months[i])){ month = i; break; }
						int dayOfMonth = Integer.parseInt(dayStr);
						
						currentEvent.setImg(imgStr);
						currentEvent.setSummary(title);
						currentEvent.setLink(linkStr);
						currentEvent.setInfo(info);
						currentEvent.setIsValidImage(isValidImage);
						
						Date currentDate = new Date(month, dayOfMonth, 0, dayOfWeek);
						
						if(currentDay.getDate() != null && currentDay.getDate().compareTo(currentDate) != 0){
							
							if(this.events.size() <= 0 || this.events.get(this.events.size() -1).getDate().compareTo(currentDay.getDate()) != 0) this.events.add(currentDay);
							currentDay = new EventDay();
							currentDay.setDate(currentDate);
							currentDay.getEvents().add(currentEvent);
						}
						
						else { 
						
							currentDay.setDate(currentDate); 
							currentDay.getEvents().add(currentEvent); 
						}
					}
				}
				
				this.events.add(currentDay);
			}
			
			else { this.isLoaded = false; return false; }
			this.isLoaded = true; 
			return true;
		}
		
		catch(Exception e) {this.events = new ArrayList<EventDay>(); this.isLoaded = false; return false; }		
	}

	/* Old Stuff!
	 if(event.children().size() > 0 &&  linkStr != null){
						
						Document doc = Jsoup.parse(new URL("http://kettering.edu" + linkStr), 3000);
						Elements summary = doc.select("div.title.inside");
						Elements description = doc.select("div.content.clearfix");
						Elements date = doc.getElementsByClass("date-display-single");
						
						if(summary.size() > 0 && date.size() > 0){
									
							currentEvent.setSummary(summary.get(0).text());							
							
							if(description.size() > 0){ 
								String desc = description.get(0).toString();
										
								Matcher m = descPattern.matcher(desc);
				        		
				        		while(m.find()){

				        			desc = m.group(1) +  m.group(3) + m.group(5);
				        			m = descPattern.matcher(desc);
				        		}
								
				        		currentEvent.setDescription(desc);
							}
							
							else currentEvent.setDescription("No description available.");
							
							// Regular expression
							Matcher m = p.matcher(date.get(0).text());
							
							if (m.find()){		
								try{  
									if(m.groupCount() >= 7){
										
										String dayOfWeek = m.group(1);
										int month = 0;
										
										for (int i = 0; i < months.length; i++) if(m.group(3).equalsIgnoreCase(months[i])){ month = i; break; }
										int dayOfMonth = Integer.parseInt(m.group(5));
										int year = Integer.parseInt(m.group(7));
										
										Date currentDate = new Date(month, dayOfMonth, year, dayOfWeek);
										
										if(currentDay.getDate() != null && currentDay.getDate().compareTo(currentDate) != 0){
											
											if(this.events.size() <= 0 || this.events.get(this.events.size() -1).getDate().compareTo(currentDay.getDate()) != 0) this.events.add(currentDay);
											currentDay = new EventDay();
											currentDay.setDate(currentDate);
											currentDay.getEvents().add(currentEvent);
										}
										else { 
										
											currentDay.setDate(currentDate); 
											currentDay.getEvents().add(currentEvent); 
										}
									}

	 */
	
	
	/********************************************************************
	 * Accessors: getEvents, getIsLoaded
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public List<EventDay> getEvents(){ return this.events; }
	public boolean getIsLoaded(){ return this.isLoaded; }
	
	
	/********************************************************************
	 * Method: getEvents
	 * Purpose: gets events based off parameters
	/*******************************************************************/
	public List<EventDay> getEvents(int amount, Date start){
		
		List<EventDay> eventList = new ArrayList<EventDay>();
		int index;
		
		// Find starting point
		for(index = 0; index < this.events.size(); index++) if(this.events.get(index).getDate().compareTo(start) <= 0) break;
		
		
		// Out of range
		if(index >= this.events.size()){
			if(index-amount < 0) eventList = this.events.subList(0, index);
			else eventList = this.events.subList(index-amount, index);
			Collections.reverse(eventList);
			return eventList;
		}
		
		
		// Equal
		if(this.events.get(index).getDate().compareTo(start) == 0) index++; 
		
		// Get latest
		if(index-amount < 0) eventList = this.events.subList(0, index);
		else eventList = this.events.subList(index-amount, index);
		
		Collections.reverse(eventList);
		return eventList;
		
	}
}


/********************************************************************
 * Class: EventDay
 * Purpose: holds JSON event objects
/*******************************************************************/
class EventDay{
	
	private Date date;
	private List<Event> events;
	
	/********************************************************************
	 * Class: EventDay
	 * Purpose: creates JSON event object
	/*******************************************************************/
	public EventDay(){
		
		this.events = new ArrayList<Event>();
	}	
	
	/********************************************************************
	 * Accessors: getEvents, getDate
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public List<Event> getEvents() { return this.events; }
	public Date getDate() { return this.date; }
	public void setDate(Date date) { this.date = date; }
	
}

/********************************************************************
 * Class: Event
 * Purpose: holds JSON event object
/*******************************************************************/
class Event{
	
	// Properties
	private String summary;
	private String uid;
	private String description;
	private String location;
	private String duration;
	private String contact;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String email;
	private String link;
	private boolean isLoaded;
	private String html;
	private String info;
	private String img;
	private boolean isValidImage;	
	private Bitmap bitmap;
	
	public Event(){
	
		this.bitmap = null;
		this.summary = "";
		this.uid = "";
		this.description = "";
		this.location = "";
		this.duration = "";
		this.contact = "";
		this.startDate = "";
		this.startTime = "";
		this.endDate = "";
		this.endTime = "";
		this.email = "";
		this.html = "";
		this.link = "";
		this.info = "";
		this.img = "";
		this.isLoaded = false;
		this.isValidImage = false;
	}

	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){ 
		String eventStr = "";
		
		if(this.summary!=null) eventStr += "Summary: " +  this.summary + " ";
		if(this.startDate!=null) eventStr += "Start date: " + this.startDate;
		
		return eventStr;
	}
	
	
	/********************************************************************
	 * Accessors: getSummary, getUid, getDescription, getLocation, getDuration
	 * 		getContact, getStartDate, getEndDate, getStartTime, getEndTime, getEmail
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getSummary() { return this.summary; }
	public String getUid() { return this.uid; }
	public String getDescription() { return this.description; }
	public String getLocation() { return this.location; }
	public String getDuration() { return this.duration; }
	public String getContact() { return this.contact; }
	public String getStartDate() { return this.startDate; }
	public String getEndDate() { return this.endDate; }
	public String getStartTime() { return this.startTime; }
	public String getEndTime() { return this.endTime; }
	public String getEmail() { return this.email; }
	public String getHTML() { return this.html; }
	public boolean getIsLoaded() { return this.isLoaded; }
	public String getLink() { return this.link; }
	public String getInfo() { return this.info; }
	public String getImg() { return this.img; }
	public boolean getIsValidImg() { return this.isValidImage; }
	public Bitmap getBitmap() { return this.bitmap; }
	
	
	/********************************************************************
	 * Mutators: setSummary, setUid, setDescription, setLocation, setDuration
	 * 		setContact, setStartDate, setEndDate, setStartTime, setEndTime, setEmail
	 * Purpose: set the corresponding data
	/*******************************************************************/
	public void setSummary(String summary) { this.summary = summary; }
	public void setUid(String uid) { this.uid = uid; }
	public void setDescription(String description) { this.description = description; }
	public void setLocation(String location) { this.location = location; }
	public void setDuration(String duration) { this.duration = duration; }
	public void setContact(String contact) { this.contact = contact; }
	public void setStartDate(String startDate) { this.startDate = startDate; }
	public void setEndDate(String endDate) { this.endDate = endDate; }
	public void setStartTime(String startTime) { this.startTime = startTime; }
	public void setEndTime(String endTime) { this.endTime = endTime; }
	public void setEmail(String email) { this.email = email; }
	public void setIsLoaded(boolean isLoaded){ this.isLoaded = isLoaded; }
	public void setHTML(String html){ this.html = html; }
	public void setLink(String link){ this.link = link; }
	public void setInfo(String info){ this.info = info; }
	public void setImg(String img) { this.img = img; }
	public void setIsValidImage(boolean isValidImage){ this.isValidImage = isValidImage; }
	public void setBitmap(Bitmap image) { this.bitmap = image; }
	
}

/********************************************************************
 * Class: Date
 * Purpose: holds date information
/*******************************************************************/
class Date{
	
	private int year;
	private int month;
	private int day;
	private String dayOfWeek;
	
	/********************************************************************
	 * Constructor: Date
	 * Purpose: create date object without a day of week
	/*******************************************************************/
	public Date(int month, int day, int year){
		
		this.year = year;
		this.month = month;
		this.day = day;
		this.dayOfWeek = "";
	}
	
	/********************************************************************
	 * Constructor: Date
	 * Purpose: create date object with a day of week
	/*******************************************************************/
	public Date(int month, int day, int year, String dayOfWeek){
		
		this.year = year;
		this.month = month;
		this.day = day;
		this.dayOfWeek = dayOfWeek;
	}
	
	
	/********************************************************************
	 * Accessors: getYear, getMonth, getDay, getDayOfWeek
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public int getYear(){ return this.year; }
	public int getMonth(){ return this.month; }
	public int getDay(){ return this.day; }
	public String getDayOfWeek(){ return this.dayOfWeek; }
	
	/********************************************************************
	 * Method: compareTo
	 * Purpose: format object into a string
	/*******************************************************************/
	public int compareTo(Date compareDate){
		
		if(this.year > compareDate.getYear()) return 1;
		if(this.year < compareDate.getYear()) return -1;
		
		if(this.month > compareDate.getMonth()) return 1;
		if(this.month < compareDate.getMonth()) return -1;
		
		if(this.day > compareDate.getDay()) return 1;
		if(this.day < compareDate.getDay()) return -1;
		
		return 0;
	}
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		
		return ((this.month < months.length && this.month >= 0)?months[this.month]:"Unknown") + " " + this.day;
	}
	
}
