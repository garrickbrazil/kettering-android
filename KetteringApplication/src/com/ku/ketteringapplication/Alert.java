package com.ku.ketteringapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Alert {

	 public static void show(Context c, String message){
		    AlertDialog.Builder alert = new AlertDialog.Builder(c);
		    
		    alert.setTitle("Alert");
		    alert.setMessage(message);
		    
		    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) { /* Nothing */ }
		    });
	        
		    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
	        });
	        
	        alert.show();
	    }
}
