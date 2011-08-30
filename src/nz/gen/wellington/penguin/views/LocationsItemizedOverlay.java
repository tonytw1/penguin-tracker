package nz.gen.wellington.penguin.views;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class LocationsItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {
			
	public LocationsItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
	}

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
}
