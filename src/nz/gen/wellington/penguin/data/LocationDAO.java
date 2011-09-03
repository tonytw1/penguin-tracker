package nz.gen.wellington.penguin.data;

import java.util.List;

import nz.gen.wellington.penguin.model.Location;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocationDAO implements LocationService {
	
	public List<Location> getLocations(Context context) {
		List<Location> locations = null;
		
		LocalLocationService localLocationService = new LocalLocationService();				
		if (localLocationService.isLocallyCached(context) && localLocationService.isCurrent(context)) {
			locations = localLocationService.getLocations(context);
			if (locations != null) {
				return locations;
			}
		}
		
		
		PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (prefs != null && prefs.getBoolean("sync", true)) {
			LocationUpdater locationUpdater = new LocationUpdater();
			locations = locationUpdater.updateLocations(context, (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));		
			if (locations != null) {
				return locations;
			}
		}
		
		if (localLocationService.isLocallyCached(context)) {
			return localLocationService.getLocations(context);
		}
		
		return null;
	}
	
}
