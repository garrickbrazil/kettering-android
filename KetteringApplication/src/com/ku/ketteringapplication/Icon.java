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

import android.view.View.OnClickListener;

public class Icon {

	// Properties
	private String title;
	private Integer image;
	private OnClickListener listener;
	
	
	/********************************************************************
	 * Constructor: Icon
	 * Purpose: creates and initializes icon object
	/*******************************************************************/
	public Icon(String title, Integer image, OnClickListener listener){
		
		this.title = title;
		this.image = image;
		this.listener = listener;
	}
	
	
	/********************************************************************
	 * Accessors
	 * Purpose: get the corresponding data
	/*******************************************************************/
	public String getTitle(){ return this.title; }
	public Integer getImage(){ return this.image; }
	public OnClickListener getListener(){ return this.listener; }
	
	
}
