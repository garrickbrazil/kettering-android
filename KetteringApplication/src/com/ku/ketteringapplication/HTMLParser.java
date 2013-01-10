package com.ku.ketteringapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

/********************************************************************
 * Class: HTMLParser
 * Purpose: class to parse HTML and return as a string
/*******************************************************************/
public class HTMLParser {
	
	
	/********************************************************************
	* Method: parse
	* Purpose: parse input stream into a string
	/*******************************************************************/
	public static String parse(InputStream stream) throws IOException{
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		String inputLine;
		String html = "";
		
		// Read entire HTML response
		while ((inputLine = reader.readLine()) != null){ html += inputLine + "\n"; }
		
		return html;

	}
	
	/********************************************************************
	* Method: parse
	* Purpose: parse a http response into a string
	/*******************************************************************/
	public static String parse(HttpResponse response) throws IOException{
		
		
		InputStream stream = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		String inputLine;
		String html = "";
		
		// Read entire HTML response
		while ((inputLine = reader.readLine()) != null){ html += inputLine + "\n"; }
		
		return html;

	}
	
}
