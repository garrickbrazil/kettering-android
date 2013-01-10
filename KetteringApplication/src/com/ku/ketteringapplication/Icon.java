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
