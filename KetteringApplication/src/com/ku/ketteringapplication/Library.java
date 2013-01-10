package com.ku.ketteringapplication;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/********************************************************************
 * Class: Library
 * Purpose: an object able to search kettering's library
/*******************************************************************/
public class Library {

	// Properties
	private List<LibraryItem> libraryItems;
	private String search;
	private String type;
	private int page;
	private int maxPage;
	private String action;
	private String endURL;
	
	/********************************************************************
	 * Constructor: Library
	 * Purpose: creates default library object
	/*******************************************************************/
	public Library(){
		
		this.libraryItems = new ArrayList<LibraryItem>();
		this.page = 1;
		this.maxPage = 1;
		this.action = "";
		this.search = "";
		this.type = "";
		this.endURL = "";
	}
	
	
	/********************************************************************
	 * Method: search()
	 * Purpose: search with given parameter
	/*******************************************************************/
	public boolean search(String search, String type){
		
		this.search = search;
		this.page = 1;
		this.maxPage = 1;
		this.action = "";
		this.type = type;
		this.libraryItems = new ArrayList<LibraryItem>();
		
		
		try{

			if(this.type.equalsIgnoreCase("Phrase")) this.endURL = "&srchfield1=GENERAL%5ESUBJECT%5EGENERAL%5E%5Ewords+or+phrase&library=KU&SearchNow=Search&user_id=kuweb";
	    	else if (this.type.equalsIgnoreCase("Author")) this.endURL = "&srchfield1=AU%5EAUTHOR%5EAUTHORS%5EAuthor+Processing%5Eauthor&library=KU&SearchNow=Search&user_id=kuweb";
	    	else if (this.type.equalsIgnoreCase("Title")) this.endURL = "&srchfield1=TI%5ETITLE%5ESERIES%5ETitle+Processing%5Etitle&library=KU&SearchNow=Search&user_id=kuweb";
	    	else if (this.type.equalsIgnoreCase("Subject")) this.endURL = "&srchfield1=SU%5ESUBJECT%5ESUBJECTS%5E%5Esubject&library=KU&SearchNow=Search&user_id=kuweb";
	    	else if (this.type.equalsIgnoreCase("Series")) this.endURL = "&srchfield1=SER%5ESERIES%5ESERIES%5ETitle+Processing%5Eseries&library=KU&SearchNow=Search&user_id=kuweb";
	    	else if (this.type.equalsIgnoreCase("Periodical")) this.endURL = "&srchfield1=PER%5EPERTITLE%5ESERIES%5ETitle Processing%5Eperiodical+title&library=KU&SearchNow=Search&user_id=kuweb";
			
			Document doc = Jsoup.parse(new URL("http://catalog.palnet.info/uhtbin/cgisirsi/x/0/0/57/5?search_type=search&searchdata1=" + search.replaceAll("\\s", "+") + this.endURL), 3000);
			Elements hitlist = doc.getElementsByClass("hit_list_item_info");
			
			
			// Add First Page Items
			for(Element item : hitlist) if(item.getElementsByClass("title").size() > 0 && item.getElementsByClass("publisher").size() > 0 && item.getElementsByClass("author").size() > 0 && item.getElementsByClass("call_number").size() > 0 && item.getElementsByClass("edition").size() > 0 && item.getElementsByClass("holdings_statement").size() > 0 && item.getElementsByClass("publishing_date_label").size() > 0) this.libraryItems.add(new LibraryItem(item));
			
			
			Elements pagination = doc.getElementsByClass("searchsummary_pagination");
			
			// Other pages
			if(pagination.size() > 0 && pagination.get(0).getElementsByTag("a").size() > 2 && doc.getElementsByClass("hit_list_form").size() > 0){
				
				Elements otherPages = pagination.get(0).getElementsByTag("a");
				this.action = doc.getElementsByClass("hit_list_form").get(0).attr("action") + "?";
				
				// Remove first and last
				otherPages.remove(0); otherPages.remove(otherPages.size()-1);
				this.maxPage = Integer.parseInt(otherPages.get(otherPages.size()-1).text());
			}
		
			return true;
		}
		
		catch(Exception e){ this.libraryItems = new ArrayList<LibraryItem>(); return false; }
		
	}
	
	
	/********************************************************************
	 * Method: storeNextPage()
	 * Purpose: store next page of search results
	/*******************************************************************/
	public boolean storeNextPage(){
		
		if (this.page + 1 <= this.maxPage){
			try{
			
				Elements hitlistPage = Jsoup.parse(new URL("http://catalog.palnet.info" + this.action + "firsthit=&lasthit=&form_type=" + "JUMP%5E" + ((this.page*20) + 1)), 6000).getElementsByClass("hit_list_item_info");
				
				this.page++;

				// Add Page Items
				for(Element item : hitlistPage) this.libraryItems.add(new LibraryItem(item));
			
				return true;
			}
			catch(Exception e){ return false; }
		}
		
		else return false;
	}
	
	
	/********************************************************************
	 * Accessors: getLibrary, getPage, getMaxPage
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public List<LibraryItem> getLibrary(){ return this.libraryItems; }
	public int getPage(){ return this.page; }
	public int getMaxPage(){ return this.maxPage; }
	public String getSearchString(){ return this.search; }
	public String getType(){ return this.type; }
	
	
}
