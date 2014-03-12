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

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

/********************************************************************
 * Class: KUMenu
 * Purpose: stores 1 week lunch and dinner menu
/*******************************************************************/
public class KUMenu {

	// Days
	private DailyMenu sunday;
	private DailyMenu monday;
	private DailyMenu tuesday;
	private DailyMenu wednesday;
	private DailyMenu thursday;
	private DailyMenu friday;
	private DailyMenu saturday;
	private boolean isLoaded;

	/********************************************************************
	 * Constructor: Menu
	 * Purpose: create a default menu item
	/*******************************************************************/
	public KUMenu() {
		
		// Blank menus
		this.sunday = new DailyMenu();
		this.monday = new DailyMenu();
		this.tuesday = new DailyMenu();
		this.wednesday = new DailyMenu();
		this.thursday = new DailyMenu();
		this.friday = new DailyMenu();
		this.saturday = new DailyMenu();
		this.isLoaded = false;
	}
	
	/********************************************************************
     * Method: storeMenu
     * Purpose: stores Kettering's weekly menu
    /*******************************************************************/
	public boolean storeMenu(){
		
		try{
			
			Elements tables =  Jsoup.parse(new URL("https://www.kettering.edu/offices-administration/campus-dining/daily-menus"), 3000).getElementsByTag("table");
			
			// Two tables ?
			if (tables.size() >=2){
			
				Elements lunch = tables.get(0).getElementsByTag("tr");
				Elements dinner = tables.get(1).getElementsByTag("tr");
				
				// Food elements
				Elements soups;
				Elements pastaStations;
				Elements grills;
				Elements pizzas;
				Elements delis;
				Elements expos;
				Elements desserts;
				
				String[] dateSplit = lunch.get(0).text().replaceAll("Lunch\\s-\\sWeek\\sof\\s", "").replaceAll(":","").split("\\s[–]\\s");
				
				// Lunch	
				if(lunch.size() == 9 && dateSplit.length == 2){
					
					String[] startSplit = dateSplit[0].split("\\s");
					String[] endSplit = dateSplit[1].split("\\s");
					
					String startMonth = "";
					int startDay = 0;
					String endMonth = "";
					int endDay = 0;
					
					
					if(startSplit.length == 2 && endSplit.length == 2){
					
						startMonth = startSplit[0];
						startDay = Integer.parseInt(startSplit[1]);
						endMonth = endSplit[0];
						endDay = Integer.parseInt(endSplit[1]);
					}
					
					else if(startSplit.length == 2 && endSplit.length == 1){
						startMonth = startSplit[0];
						startDay = Integer.parseInt(startSplit[1]);
						endMonth = startSplit[0];
						endDay = Integer.parseInt(endSplit[0]);
					}
					
					else return false;
					
					// Set dates
					this.sunday.setDate("Sunday, " + startMonth + " " + startDay );
					this.monday.setDate("Monday, " + ((endDay-5 > 0)?(endMonth + " " + (endDay-5)):(startMonth + " " + (startDay + 1))));
					this.tuesday.setDate("Tuesday, " + ((endDay-4 > 0)?(endMonth + " " + (endDay-4)):(startMonth + " " + (startDay + 2))));
					this.wednesday.setDate("Wednesday, " + ((endDay-3 > 0)?(endMonth + " " + (endDay-3)):(startMonth + " " + (startDay + 3))));
					this.thursday.setDate("Thursday, " + ((endDay-2 > 0)?(endMonth + " " + (endDay-2)):(startMonth + " " + (startDay + 4))));
					this.friday.setDate("Friday, " + ((endDay-1 > 0)?(endMonth + " " + (endDay-1)):(startMonth + " " + (startDay + 5))));
					this.saturday.setDate("Saturday, " + (endMonth + " " + endDay ));
					
					
					// Delete first two rows
					lunch.remove(0); lunch.remove(0);
					pastaStations = lunch.get(1).getElementsByTag("td");
					grills = lunch.get(2).getElementsByTag("td"); 
					pizzas = lunch.get(3).getElementsByTag("td");
					delis = lunch.get(4).getElementsByTag("td");
					expos = lunch.get(5).getElementsByTag("td");
					desserts = lunch.get(6).getElementsByTag("td");
					
					// Get food
					soups = lunch.get(0).getElementsByTag("td");
					
					// Correct sizes ?
					if(soups.size() == 8 && pastaStations.size() == 8 && grills.size() == 8 && pizzas.size() == 8 
					   && delis.size() == 8 && expos.size() == 8 && desserts.size() == 8){
						
						// Soup
						soups.remove(0);
						this.sunday.getLunch().setSoup(soups.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getLunch().setSoup(soups.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getLunch().setSoup(soups.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getLunch().setSoup(soups.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getLunch().setSoup(soups.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getLunch().setSoup(soups.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getLunch().setSoup(soups.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Pasta Station
						pastaStations.remove(0);
						this.sunday.getLunch().setPastaStation(pastaStations.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getLunch().setPastaStation(pastaStations.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getLunch().setPastaStation(pastaStations.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getLunch().setPastaStation(pastaStations.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getLunch().setPastaStation(pastaStations.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getLunch().setPastaStation(pastaStations.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getLunch().setPastaStation(pastaStations.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Grills
						grills.remove(0);
						this.sunday.getLunch().setGrill(grills.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getLunch().setGrill(grills.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getLunch().setGrill(grills.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getLunch().setGrill(grills.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getLunch().setGrill(grills.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getLunch().setGrill(grills.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getLunch().setGrill(grills.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Pizza
						pizzas.remove(0);
						this.sunday.getLunch().setPizza(pizzas.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getLunch().setPizza(pizzas.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getLunch().setPizza(pizzas.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getLunch().setPizza(pizzas.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getLunch().setPizza(pizzas.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getLunch().setPizza(pizzas.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getLunch().setPizza(pizzas.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Delis
						delis.remove(0);
						this.sunday.getLunch().setDeli(delis.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getLunch().setDeli(delis.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getLunch().setDeli(delis.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getLunch().setDeli(delis.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getLunch().setDeli(delis.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getLunch().setDeli(delis.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getLunch().setDeli(delis.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Expos
						expos.remove(0);
						this.sunday.getLunch().setExpo(expos.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getLunch().setExpo(expos.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getLunch().setExpo(expos.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getLunch().setExpo(expos.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getLunch().setExpo(expos.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getLunch().setExpo(expos.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getLunch().setExpo(expos.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Desserts
						desserts.remove(0);
						this.sunday.getLunch().setDessert(desserts.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getLunch().setDessert(desserts.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getLunch().setDessert(desserts.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getLunch().setDessert(desserts.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getLunch().setDessert(desserts.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getLunch().setDessert(desserts.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getLunch().setDessert(desserts.get(6).text().replaceAll("[\u00C2]", ""));	
					}
				}
				
				
				// Dinner
				if(dinner.size() == 9){
	
					
					// Delete first two rows
					dinner.remove(0); dinner.remove(0);
					
					// Get food
					soups = dinner.get(0).getElementsByTag("td");
					pastaStations = dinner.get(1).getElementsByTag("td");
					grills = dinner.get(2).getElementsByTag("td");
					pizzas = dinner.get(3).getElementsByTag("td");
					delis = dinner.get(4).getElementsByTag("td");
					expos = dinner.get(5).getElementsByTag("td");
					desserts = dinner.get(6).getElementsByTag("td");
					
					// Correct sizes ?
					if(soups.size() == 8 && pastaStations.size() == 8 && grills.size() == 8 && pizzas.size() == 8 
					   && delis.size() == 8 && expos.size() == 8 && desserts.size() == 8){
	
						// Soup
						soups.remove(0);
						this.sunday.getDinner().setSoup(soups.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getDinner().setSoup(soups.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getDinner().setSoup(soups.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getDinner().setSoup(soups.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getDinner().setSoup(soups.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getDinner().setSoup(soups.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getDinner().setSoup(soups.get(6).text().replaceAll("[\u00C2]", ""));
					
					
						// Pasta Station
						pastaStations.remove(0);
						this.sunday.getDinner().setPastaStation(pastaStations.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getDinner().setPastaStation(pastaStations.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getDinner().setPastaStation(pastaStations.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getDinner().setPastaStation(pastaStations.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getDinner().setPastaStation(pastaStations.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getDinner().setPastaStation(pastaStations.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getDinner().setPastaStation(pastaStations.get(6).text().replaceAll("[\u00C2]", ""));
					
					
						// Grills
						grills.remove(0);
						this.sunday.getDinner().setGrill(grills.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getDinner().setGrill(grills.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getDinner().setGrill(grills.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getDinner().setGrill(grills.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getDinner().setGrill(grills.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getDinner().setGrill(grills.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getDinner().setGrill(grills.get(6).text().replaceAll("[\u00C2]", ""));
					
						
						// Pizza
						pizzas.remove(0);
						this.sunday.getDinner().setPizza(pizzas.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getDinner().setPizza(pizzas.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getDinner().setPizza(pizzas.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getDinner().setPizza(pizzas.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getDinner().setPizza(pizzas.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getDinner().setPizza(pizzas.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getDinner().setPizza(pizzas.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Delis
						delis.remove(0);
						this.sunday.getDinner().setDeli(delis.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getDinner().setDeli(delis.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getDinner().setDeli(delis.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getDinner().setDeli(delis.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getDinner().setDeli(delis.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getDinner().setDeli(delis.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getDinner().setDeli(delis.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Expos
						expos.remove(0);
						this.sunday.getDinner().setExpo(expos.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getDinner().setExpo(expos.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getDinner().setExpo(expos.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getDinner().setExpo(expos.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getDinner().setExpo(expos.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getDinner().setExpo(expos.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getDinner().setExpo(expos.get(6).text().replaceAll("[\u00C2]", ""));
						
						// Desserts
						desserts.remove(0);
						this.sunday.getDinner().setDessert(desserts.get(0).text().replaceAll("[\u00C2]", ""));
						this.monday.getDinner().setDessert(desserts.get(1).text().replaceAll("[\u00C2]", ""));
						this.tuesday.getDinner().setDessert(desserts.get(2).text().replaceAll("[\u00C2]", ""));
						this.wednesday.getDinner().setDessert(desserts.get(3).text().replaceAll("[\u00C2]", ""));
						this.thursday.getDinner().setDessert(desserts.get(4).text().replaceAll("[\u00C2]", ""));
						this.friday.getDinner().setDessert(desserts.get(5).text().replaceAll("[\u00C2]", ""));
						this.saturday.getDinner().setDessert(desserts.get(6).text().replaceAll("[\u00C2]", ""));
					}
				}
			}
			this.isLoaded = true;
			return true;
		}
		catch(Exception e){ e.printStackTrace(); this.isLoaded = false; return false; }
		
	}
	
	
    /********************************************************************
     * Method: getDayOfWeek
     * Purpose: returns appropriate day
    /*******************************************************************/
	public DailyMenu getDayOfWeek(int dayOfWeek){
		
		final int SUNDAY = 0, MONDAY = 1, TUESDAY = 2, WEDNESDAY = 3, THURSDAY = 4, FRIDAY = 5;
	
		switch(dayOfWeek){
	    	case SUNDAY: return this.getSunday(); 
	    	case MONDAY: return this.getMonday(); 
	    	case TUESDAY: return this.getTuesday(); 
	    	case WEDNESDAY: return this.getWednesday(); 
	    	case THURSDAY: return this.getThursday(); 
	    	case FRIDAY: return this.getFriday(); 
	    	default: return this.getSaturday();
		}
	}
	
	/********************************************************************
	 * Accessors: getSunday, getMonday, getTuesday, getWednesday,
	 * 		getThursday, getFriday, getSaturday, getLoaded
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public DailyMenu getSunday() { return this.sunday; }
	public DailyMenu getMonday() { return this.monday; }
	public DailyMenu getTuesday() { return this.tuesday; }
	public DailyMenu getWednesday() { return this.wednesday; }
	public DailyMenu getThursday() { return this.thursday; }
	public DailyMenu getFriday() { return this.friday; }
	public DailyMenu getSaturday() { return this.saturday; }
	public boolean getIsLoaded() { return this.isLoaded; }
	
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		String menuStr = "";
		
		menuStr += "[Sunday]";
		menuStr += "\nLunch:\t";
		menuStr += this.sunday.getLunch().toString();
		menuStr += "\nDinner:\t";
		menuStr += this.sunday.getDinner().toString();
		
		
		menuStr += "\n\n[Monday]";
		menuStr += "\nLunch:\t";
		menuStr += this.monday.getLunch().toString();
		menuStr += "\nDinner:\t";
		menuStr += this.monday.getDinner().toString();
		
		
		menuStr += "\n\n[Tuesday]";
		menuStr += "\nLunch:\t";
		menuStr += this.tuesday.getLunch().toString();
		menuStr += "\nDinner:\t";
		menuStr += this.tuesday.getDinner().toString();
		
		
		menuStr += "\n\n[Wednesday]";
		menuStr += "\nLunch:\t";
		menuStr += this.wednesday.getLunch().toString();
		menuStr += "\nDinner:\t";
		menuStr += this.wednesday.getDinner().toString();
		
		
		menuStr += "\n\n[Thursday]";
		menuStr += "\nLunch:\t";
		menuStr += this.thursday.getLunch().toString();
		menuStr += "\nDinner:\t";
		menuStr += this.thursday.getDinner().toString();
		
		
		menuStr += "\n\n[Friday]";
		menuStr += "\nLunch:\t";
		menuStr += this.friday.getLunch().toString();
		menuStr += "\nDinner:\t";
		menuStr += this.friday.getDinner().toString();
		
		
		menuStr += "\n\n[Saturday]";
		menuStr += "\nLunch:\t";
		menuStr += this.saturday.getLunch().toString();
		menuStr += "\nDinner:\t";
		menuStr += this.saturday.getDinner().toString();
		
		
		return menuStr;
	}
	
}

