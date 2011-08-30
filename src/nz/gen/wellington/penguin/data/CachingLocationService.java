package nz.gen.wellington.penguin.data;

import java.util.List;

import android.content.Context;

import nz.gen.wellington.penguin.model.Location;

public class CachingLocationService implements LocationService {
	
	public List<Location> getLocations(Context context) {
		LocalLocationService localLocationService = new LocalLocationService();		
		
		List<Location> locations = localLocationService.getLocations(context);
		if (locations != null) {
			return locations;
		}
		
		LocationService liveLocationService = new LiveLocationService();
		locations = liveLocationService.getLocations(context);
		if (locations != null) {
			localLocationService.saveLocations(context, locations);
		}
		return locations;
	}
	
}
