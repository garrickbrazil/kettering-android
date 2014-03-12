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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconAdapter extends BaseAdapter {
    private Context mContext;
    private Icon[] icons;
    
    public IconAdapter(Context c, Icon[] icons) {
        this.mContext = c;
        this.icons = icons;
    }

    public int getCount() {
        return icons.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	LinearLayout view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        
	    if (convertView == null) view = (LinearLayout) inflater.inflate(R.layout.icon, null);
	    else view = (LinearLayout) convertView;
	
	    ImageView image = (ImageView) view.findViewById(R.id.image);
	    TextView text = (TextView) view.findViewById(R.id.title);
	 
	    text.setText(icons[position].getTitle());
	    image.setImageResource(this.icons[position].getImage());
	    view.setOnClickListener(this.icons[position].getListener());
	    
	    return view;
    }
    
}