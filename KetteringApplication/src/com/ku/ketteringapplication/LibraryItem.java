package com.ku.ketteringapplication;

import org.jsoup.nodes.Element;

/********************************************************************
 * Class: LibraryItem
 * Purpose: stores a single book's information
/*******************************************************************/
public class LibraryItem {

	private String title;
	private String author;
	private String callNumber;
	private String publisher;
	private String edition;
	private String pubDate;
	private String holdings;
	
	
	public LibraryItem(Element item){
		
		this.title = "";
		this.author = "";
		this.callNumber = "";
		this.publisher = "";
		this.edition = "";
		this.pubDate = "";
		this.holdings = "";
		
		
		if(item.getElementsByClass("title").size() > 0) this.title = item.getElementsByClass("title").get(0).text();
		if(item.getElementsByClass("publisher").size() > 0) this.publisher = item.getElementsByClass("publisher").get(0).text();
		if(item.getElementsByClass("author").size() > 0) this.author = item.getElementsByClass("author").get(0).text();
		if(item.getElementsByClass("call_number").size() > 0) this.callNumber = item.getElementsByClass("call_number").get(0).text();
		if(item.getElementsByClass("edition").size() > 0) this.edition = item.getElementsByClass("edition").get(0).text();
		if(item.getElementsByClass("holdings_statement").size() > 0) this.holdings = item.getElementsByClass("holdings_statement").get(0).text();
		if(item.getElementsByClass("publishing_date_label").size() > 0) this.pubDate = item.getElementsByClass("publishing_date_label").get(0).nextElementSibling().text();
	}
	
	/********************************************************************
	 * Accessors: getTitle, getAuthor, getCallNumber, getPublisher,
	 * 		getEdition, getPubDate, getHoldings
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getTitle(){ return this.title; }
	public String getAuthor(){ return this.author; }
	public String getCallNumber(){ return this.callNumber; }
	public String getPublisher(){ return this.publisher; }
	public String getEdition(){ return this.edition; }
	public String getPubDate(){ return this.pubDate; }
	public String getHoldings(){ return this.holdings; }
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){
		String libStr = "[" + this.title + "]";

		if(!this.author.equals("")) libStr += "\nAuthor:\t" + this.author;
		if(!this.callNumber.equals("")) libStr += "\nCall Number:\t" + this.callNumber;
		if(!this.publisher.equals("")) libStr += "\nPublisher:\t" + this.publisher;
		if(!this.edition.equals("")) libStr += "\nEdition:\t" + this.edition;
		if(!this.pubDate.equals("")) libStr += "\nDate published:\t" + this.pubDate;
		if(!this.holdings.equals("")) libStr += "\nHoldings: \t" + this.holdings;
		
		return libStr;
	}
	
}
