package nz.gen.wellington.penguin;

import java.util.List;

import nz.gen.wellington.penguin.data.CachingLocationService;
import nz.gen.wellington.penguin.model.Location;
import nz.gen.wellington.penguin.utils.DateTimeHelper;
import nz.gen.wellington.penguin.views.GeoPointFactory;
import nz.gen.wellington.penguin.views.LocationsItemizedOverlay;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
	Drawable drawable;
	LocationsItemizedOverlay itemizedOverlay;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.main);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        populateMapPoints(mapView);        
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Refresh");
		menu.add(0, 2, 0, "About");
		menu.add(0, 3, 0, "Settings");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		    case 1:
		        return true;
		    case 2:
		    	Intent intent = new Intent(this, about.class);
		    	this.startActivity(intent);
		        return true;
		    case 3:
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
        drawable = this.getResources().getDrawable(R.drawable.marker);
        itemizedOverlay = new LocationsItemizedOverlay(drawable, mapView);
        
        CachingLocationService locationService = new CachingLocationService();
        List<Location> locations = locationService.getLocations(this.getBaseContext());
        if (locations != null) {
        	GeoPoint lastPoint = null;
        	if (!locations.isEmpty()) {
        		Location location = locations.get(0);
        		Log.i(TAG, "Adding map point for: " + location);
	        	OverlayItem overlayitem = createOverlayForLocation(location);
	        	lastPoint = overlayitem.getPoint();
			}
	        
	        if (lastPoint != null) {
	        	mapView.getController().animateTo(lastPoint);
	        	mapView.getController().setZoom(7);
	        }	        
	        mapOverlays.add(itemizedOverlay);

        } else {
        	Toast.makeText(this.getApplicationContext(), "Tracking information is not currently available", Toast.LENGTH_LONG);        	
        }        
	}
	
	private OverlayItem createOverlayForLocation(Location location) {
		GeoPoint point = GeoPointFactory.createGeoPointForLatLong(location.getLongitude(), location.getLatitude());
		final String title = DateTimeHelper.calculateTimeTaken(location.getDate(), DateTimeHelper.now()) + " ago";		
		final String snippet = location.getLatitude() + ", " + location.getLongitude() + " at " + DateTimeHelper.format(location.getDate(), "HH:mm, dd MMM yyyy");		
		OverlayItem overlayitem = new OverlayItem(point, title, snippet);
		itemizedOverlay.addOverlay(overlayitem);
		return overlayitem;
	}
	
}