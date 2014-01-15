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