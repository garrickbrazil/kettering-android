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
