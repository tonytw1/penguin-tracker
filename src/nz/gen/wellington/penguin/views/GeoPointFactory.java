package nz.gen.wellington.penguin.views;

import com.google.android.maps.GeoPoint;

public class GeoPointFactory {

	public static GeoPoint createGeoPointForLatLong(double longitude, double latitude) {		
		return new GeoPoint((int) Math.round(latitude * 1e6), (int) Math.round(longitude * 1e6));
	}
	
}
