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
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/********************************************************************
 * Class: News
 * Purpose: holds Kettering news
/*******************************************************************/
public class News {

	private List<NewsItem> news;
	private boolean isLoaded;
	private int page;
	
	/********************************************************************
	 * Constructor: News
	 * Purpose: create default news object
	/*******************************************************************/
	public News(){
		
		this.news = new ArrayList<NewsItem>();
		this.isLoaded = false;
		this.page = 0;
	}
	
	/********************************************************************
	 * Method: store
	 * Purpose: stores first page of news
	/*******************************************************************/
	public boolean store(){
		
		try{
			
			// Execute
			this.news = new ArrayList<NewsItem>();
			return storeArticles(Jsoup.parse(new URL("https://www.kettering.edu/news/current-news"), 3000).getElementsByClass("news-caption"));
		}
		
		catch(Exception e){ this.news = new ArrayList<NewsItem>(); this.isLoaded = false; return false;}
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
	 * Method: storeArticles
	 * Purpose: internally stores articles into memory
	/*******************************************************************/
	private boolean storeArticles(Elements articles){
		
		try{
			
			for(Element article : articles){
				
				Elements pictureObj = article.getElementsByClass("object"); 
				if(pictureObj.size() > 0 && pictureObj.get(0).getElementsByTag("img").size() > 0 && article.getElementsByTag("h3").size() > 0 && article.getElementsByTag("h3").get(0).getElementsByTag("a").size() > 0 && article.getElementsByTag("h3").get(0).getElementsByTag("a").attr("href") != null){
					
					String link = "https://www.kettering.edu" + article.getElementsByTag("h3").get(0).getElementsByTag("a").attr("href");
					Element img = pictureObj.get(0).getElementsByTag("img").get(0);  img.attr("style", "width:100%; height:100%");
					Bitmap bitmap = loadBitmap(img.attr("src"));
					String imgStr = img.toString();
					String title = article.getElementsByTag("h3").text();
					String info = "";
					
					if (article.getElementsByClass("by").size() > 0 && article.getElementsByClass("date").size() > 0){
						info = article.getElementsByClass("by").get(0).text() + " | " + article.getElementsByClass("date").get(0).text();
					}
					else if (article.getElementsByClass("by").size() > 0){
						info = article.getElementsByClass("by").get(0).text();
					}
					else if (article.getElementsByClass("date").size() > 0){
						info = article.getElementsByClass("date").get(0).text();
					}
					
					// Add
					this.news.add(new NewsItem(title, info, link, imgStr, bitmap));
				}	
			}
			
			this.isLoaded = true;
			return true;
		}
		
		catch(Exception e){ this.news = new ArrayList<NewsItem>(); this.isLoaded = false; return false;}
	}
	
	
	/********************************************************************
	 * Method: storeNextPage
	 * Purpose: stores next page of news
	/*******************************************************************/
	public boolean storeNextPage(){
		
		try{
			// Execute
			return storeArticles(Jsoup.parse(new URL("https://www.kettering.edu/news/current-news?page=" + (++page)), 3000).getElementsByClass("news-caption"));
		}
			
		catch(Exception e){ this.news = new ArrayList<NewsItem>(); this.isLoaded = false; return false; }
	}
	
	
	/********************************************************************
	 * Accessors: getNews
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public List<NewsItem> getNews(){ return this.news; }
	public boolean getIsLoaded(){ return this.isLoaded; }
	
}


/********************************************************************
 * Class: NewsItem
 * Purpose: holds one news article
/******************************************************************/
class NewsItem{
	
	// Properties
	private String link;
	private String title;
	private String headline;
	private String date;
	private String body;
	private String author;
	private String img;
	private String info;
	private boolean isLoaded;
	private String html;
	private Bitmap bitmap;
	
	/********************************************************************
	 * Constructor: NewsItem
	 * Purpose: creates one news article
	/*******************************************************************/
	public NewsItem(String title, String info, String link, String imgStr, Bitmap bitmap){
		
		// Set properties
		this.title = title;
		this.info = info;
		this.link = link;
		this.img = imgStr;
		this.isLoaded = false;
		this.html = "";
		this.bitmap = bitmap;
		
	}
	
	
	/********************************************************************
	 * Accessors: getTitle getHeadline, getDate, getBody, getAuthor, getInfo
	 * 		getLInk, getImg, getHTML, getIsLoaded
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getTitle(){ return this.title; }
	public String getHeadline(){ return this.headline; }
	public String getDate(){ return this.date; }
	public String getBody(){ return this.body; }
	public String getAuthor(){ return this.author; }
	public String getInfo(){ return this.info; }
	public String getLink(){ return this.link; }
	public String getImg(){ return this.img; }
	public String getHTML(){ return this.html; }
	public boolean getIsLoaded(){ return this.isLoaded; }
	public Bitmap getBitmap(){ return this.bitmap; }
	
	/********************************************************************
	 * Mutators: setIsLoaded, setHTML
	 * Purpose: set the corresponding data
	/*******************************************************************/
	public void setIsLoaded(boolean isLoaded){ this.isLoaded = isLoaded; }
	public void setHTML(String html){ this.html = html; }
	
	
	/********************************************************************
	 * Method: toString
	 * Purpose: format object into a string
	/*******************************************************************/
	public String toString(){ 
		
		return this.title + "\n" + this.date + "\n" + this.headline + "\n" + Jsoup.parse(this.body).text();
	}

}

