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
