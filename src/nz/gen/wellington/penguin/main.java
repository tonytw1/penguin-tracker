package nz.gen.wellington.penguin;

import java.util.Collections;
import java.util.List;

import nz.gen.wellington.penguin.data.LocationDAO;
import nz.gen.wellington.penguin.model.Location;
import nz.gen.wellington.penguin.timers.LocationUpdateService;
import nz.gen.wellington.penguin.views.GeoPointFactory;
import nz.gen.wellington.penguin.views.LocationsItemizedOverlay;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class main extends MapActivity {
	
	private static final String TAG = "main";
	
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
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		
		notificationManager.cancel(LocationUpdateService.UPDATE_COMPLETE_NOTIFICATION_ID);
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
	        	mapView.getController().setZoom(7);
	        }	        
	        mapOverlays.add(itemizedOverlay);

        } else {
        	Toast.makeText(this.getApplicationContext(), "Tracking information is not currently available", Toast.LENGTH_LONG);        	
        }        
	}
	
	private GeoPoint plotAllPoints(List<Location> locations, Drawable marker, Drawable previousMarker) {
		GeoPoint lastPoint = null;
		Collections.reverse(locations);
		int pointCount = 0;
		for (Location location : locations) {
			pointCount++;
			Log.i(TAG, "Adding map point for: " + location + pointCount + ", " + locations.size());
			OverlayItem overlayitem = createOverlayForLocation(location, pointCount == locations.size() ? marker : previousMarker);
			lastPoint = overlayitem.getPoint();
		}
		return lastPoint;
	}
	
	
	private OverlayItem createOverlayForLocation(Location location, Drawable marker) {
		GeoPoint point = GeoPointFactory.createGeoPointForLatLong(location.getLongitude(), location.getLatitude());
		final String title = location.timeAgo();	
		final String snippet = location.toString();		
		OverlayItem overlayitem = new OverlayItem(point, title, snippet);
		overlayitem.setMarker(marker);
        itemizedOverlay.addOverlay(overlayitem);
		return overlayitem;
	}
	
}