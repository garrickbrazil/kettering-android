package com.ku.ketteringapplication;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;


public class Map extends MapActivity{
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    MapController controller = mapView.getController();
	    
		GeoPoint point = new GeoPoint((int) (43.012234 * 1E6), (int) (-83.713247 * 1E6));
		controller.animateTo(point);
	    controller.setZoom(18);
	    mapView.invalidate();
		
	}
	
	
}
