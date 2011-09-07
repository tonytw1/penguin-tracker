package nz.gen.wellington.penguin;

import java.util.Collections;
import java.util.List;

import nz.gen.wellington.penguin.config.Config;
import nz.gen.wellington.penguin.data.LocationDAO;
import nz.gen.wellington.penguin.data.tracker.TrackerScheduleService;
import nz.gen.wellington.penguin.model.Location;
import nz.gen.wellington.penguin.views.GeoPointFactory;
import nz.gen.wellington.penguin.views.LocationsItemizedOverlay;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class main extends MapActivity {
		
	List<Overlay> mapOverlays;
	LocationsItemizedOverlay itemizedOverlay;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.main);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        // TODO center on release point
        
        // TODO Needs to happen on a background thread
        populateMapPoints(mapView);
        
        populateTrackerStatus();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Source Feed");
		menu.add(0, 2, 0, "About");
		menu.add(0, 3, 0, "Settings");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		    case 1:
		    	String url = "http://www.nzemperor.com/";  
		    	Intent i = new Intent(Intent.ACTION_VIEW);  
		    	i.setData(Uri.parse(url));  
		    	startActivity(i);  		    	   
		        return true;
		        
		    case 2:
		    	this.startActivity(new Intent(this, about.class));
		    	return true;
		    	
		    case 3:
		    	this.startActivity(new Intent(this, preferences.class));
		    	return true;		   
		 }
		 return false;
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void populateMapPoints(MapView mapView) {
		mapOverlays = mapView.getOverlays();
        Drawable previousMarker = this.getResources().getDrawable(R.drawable.previousmarker);
        previousMarker.setBounds(-10, -10, 10, 10);
        
        Drawable marker = this.getResources().getDrawable(R.drawable.marker);

        itemizedOverlay = new LocationsItemizedOverlay(marker, mapView);
        
        LocationDAO locationService = new LocationDAO();
        List<Location> locations = locationService.getLocations(this.getBaseContext());
        if (locations != null) {
        	GeoPoint lastPoint = plotAllPoints(locations, marker, previousMarker);	        
	        if (lastPoint != null) {
	        	mapView.getController().animateTo(lastPoint);
	        	mapView.getController().setZoom(11);
	        }
	        
        } else {
        	TextView status = (TextView) findViewById(R.id.status);
            status.setText("Tracking data is currently unavailable");
        	status.setTextColor(Color.parseColor(Config.VERY_DARK_RED));
        }
        
        // Mark the release point
		createOverlayForLocation(Config.releasePoint, previousMarker, "Release point");		
        mapOverlays.add(itemizedOverlay);
	}
	
	private GeoPoint plotAllPoints(List<Location> locations, Drawable marker, Drawable previousMarker) {
		GeoPoint lastPoint = null;
		Collections.reverse(locations);
		int pointCount = 0;
		for (Location location : locations) {
			pointCount++;
			
			if (location.getDate().after(Config.releasePoint.getDate())) {
				OverlayItem overlayitem = createOverlayForLocation(location, pointCount == locations.size() ? marker : previousMarker, location.timeAgo());
				lastPoint = overlayitem.getPoint();
			}
		}
		return lastPoint;
	}
	
	
	private OverlayItem createOverlayForLocation(Location location, Drawable marker, String title) {
		GeoPoint point = GeoPointFactory.createGeoPointForLatLong(location.getLongitude(), location.getLatitude());
		final String snippet = location.toString();
		OverlayItem overlayitem = new OverlayItem(point, title, snippet);
		overlayitem.setMarker(marker);
        itemizedOverlay.addOverlay(overlayitem);
		return overlayitem;
	}
	
	private void populateTrackerStatus() {
        TextView status = (TextView) findViewById(R.id.status);
        TrackerScheduleService trackerScheduleService = new TrackerScheduleService();
        status.setText(trackerScheduleService.getTrackerStatus());
        if (trackerScheduleService.isCurrentlyScheduledToTransmit()) {
        	status.setTextColor(Color.parseColor(Config.VERY_DARK_GREEN));
        } else {
        	status.setTextColor(Color.BLACK);
        }
	}

}