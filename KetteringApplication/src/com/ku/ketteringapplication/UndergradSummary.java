package com.ku.ketteringapplication;

import org.jsoup.select.Elements;

/********************************************************************
 * Class: UndergradSummary
 * Purpose: information obtained from final grades (GPA, credits, etc)
/*******************************************************************/
public class UndergradSummary {
	
	// Properties
	private UndergradSummaryDetail currentTerm;
	private UndergradSummaryDetail cumulative;
	private UndergradSummaryDetail transfer;
	private UndergradSummaryDetail overall;
	
	
	/********************************************************************
	 * Constructor: UndergradSummary
	 * Purpose: create a default undergrad summary object
	/*******************************************************************/
	public UndergradSummary(){
		
		// Initialize
		this.currentTerm = new UndergradSummaryDetail();
		this.cumulative = new UndergradSummaryDetail();
		this.transfer = new UndergradSummaryDetail();
		this.overall = new UndergradSummaryDetail();
	}

	/********************************************************************
	 * Constructor: UndergradSummary
	 * Purpose: create undergrad summary object with parameters
	/*******************************************************************/
	public UndergradSummary(Elements elements){
		
		if (elements.size() == 5){
			
			elements.remove(0);
			
			// Get elements
			Elements currentTerm = elements.get(0).getElementsByTag("td");
			Elements cumulative = elements.get(1).getElementsByTag("td");
			Elements transfer = elements.get(2).getElementsByTag("td");
			Elements overall = elements.get(3).getElementsByTag("td");
			
			// Correct size ?
			if (currentTerm.size() == 5 && cumulative.size() == 5 && transfer.size() == 5 && overall.size() == 5){
				
				// Current Term
				try { this.currentTerm = new UndergradSummaryDetail(Double.parseDouble(currentTerm.get(0).text()), Double.parseDouble(currentTerm.get(1).text()), Double.parseDouble(currentTerm.get(2).text()), Double.parseDouble(currentTerm.get(3).text()), Double.parseDouble(currentTerm.get(4).text())); }
				catch(Exception e){ this.currentTerm = new UndergradSummaryDetail(0,0,0,0,0); }
			
				// Cumulative
				try { this.cumulative = new UndergradSummaryDetail(Double.parseDouble(cumulative.get(0).text()), Double.parseDouble(cumulative.get(1).text()), Double.parseDouble(cumulative.get(2).text()), Double.parseDouble(cumulative.get(3).text()), Double.parseDouble(cumulative.get(4).text())); }
				catch(Exception e) { this.cumulative = new UndergradSummaryDetail(0,0,0,0,0); }
				
				// Transfer
				try { this.transfer = new UndergradSummaryDetail(Double.parseDouble(transfer.get(0).text()), Double.parseDouble(transfer.get(1).text()), Double.parseDouble(transfer.get(2).text()), Double.parseDouble(transfer.get(3).text()), Double.parseDouble(transfer.get(4).text())); }
				catch(Exception e) { this.transfer = new UndergradSummaryDetail(0,0,0,0,0); }
				
				// Overall
				try { this.overall = new UndergradSummaryDetail(Double.parseDouble(overall.get(0).text()), Double.parseDouble(overall.get(1).text()), Double.parseDouble(overall.get(2).text()), Double.parseDouble(overall.get(3).text()), Double.parseDouble(overall.get(4).text())); }
				catch(Exception e){ this.overall = new UndergradSummaryDetail(0,0,0,0,0); }
			
			}
			
		}
		
	}
	
	
	/********************************************************************
	 * Accessors: getCurrentTerm, getCumulative, getTransfer, getOverall
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public UndergradSummaryDetail getCurrentTerm(){ return this.currentTerm; }
	public UndergradSummaryDetail getCumulative(){ return this.cumulative; }
	public UndergradSummaryDetail getTransfer(){ return this.transfer; }
	public UndergradSummaryDetail getOverall(){ return this.overall; }
	
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		return "Current term: " + this.currentTerm + "\nCumulative: " + this.cumulative + "\nTransfer: " + this.transfer + "\nOverall: " + this.overall;
	}
	
}


/********************************************************************
 * Class: UndergradSummaryDetail
 * Purpose: details of an undergrad summary
/*******************************************************************/
class UndergradSummaryDetail{
	
	// Properties
	private double attempted;
	private double earned;
	private double GPAHours;
	private double qualityPoints;
	private double GPA;
	
	
	/********************************************************************
	 * Constructor: UndergradSummaryDetail
	 * Purpose: create default undergrad summary detail
	/*******************************************************************/
	public UndergradSummaryDetail(){
		
		// Initialize
		this.attempted = 0;
		this.earned = 0;
		this.GPAHours = 0;
		this.qualityPoints = 0;
		this.GPA = 0;
	}
	
	
	/********************************************************************
	 * Constructor: UndergradSummaryDetail
	 * Purpose: create undergrad summary detail with parameters
	/*******************************************************************/
	public UndergradSummaryDetail(double attempted, double earned, double GPAHours, double qualityPoints, double GPA){
		
		// Set properties
		this.attempted = attempted;
		this.earned = earned;
		this.GPAHours = GPAHours;
		this.qualityPoints = qualityPoints;
		this.GPA = GPA;
	}
	
	
	/********************************************************************
	 * Accessors: getAttempted, getEarned, getGPAHours, getQualityPoints, getGPA
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public double getAttempted(){ return this.attempted; }
	public double getEarned(){ return this.earned; }
	public double getGPAHours(){ return this.GPAHours; }
	public double getQualityPoints(){ return this.qualityPoints; }
	public double getGPA(){ return this.GPA; }
	
	
	
	/********************************************************************
	 * Mutators: setAttempted, setEarned, setGPAHours, setQualityPoints, setGPA
	 * Purpose: set the corresponding data
	/*******************************************************************/
	public void setAttempted(double attempted){ this.attempted = attempted; }
	public void setEarned(double earned){ this.earned = earned; }
	public void setGPAHours(double GPAHours){ this.GPAHours = GPAHours; }
	public void setQualityPoints(double qualityPoints){ this.qualityPoints = qualityPoints; }
	public void setGPA(double GPA){ this.GPA = GPA; }
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		
		return "Attempted: " + this.attempted + " Earned: " + this.earned + " GPA Hours " + this.GPAHours + " Quality Points: " + this.qualityPoints + " GPA: " + this.GPA;
	}
	
	
}