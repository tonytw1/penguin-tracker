package nz.gen.wellington.penguin.timers;

import java.util.List;

import nz.gen.wellington.penguin.R;
import nz.gen.wellington.penguin.main;
import nz.gen.wellington.penguin.data.LiveLocationService;
import nz.gen.wellington.penguin.data.LocalLocationService;
import nz.gen.wellington.penguin.model.Location;
import nz.gen.wellington.penguin.utils.DateTimeHelper;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationUpdateRunnable implements Runnable {

    private static final String TAG = "InternalRunnable";
    
	private Context context;
    private NotificationManager notificationManager;
    
	private boolean running = false;
	
	public LocationUpdateRunnable( Context context, NotificationManager notificationManager) {
		this.context = context;
		this.notificationManager = notificationManager;
	}
	
	@Override
	public void run() {
		 running = true;
		 while(running) {			 
			 LocalLocationService localLocationService = new LocalLocationService();
			 List<Location> existingLocations = localLocationService.getLocations(context);
			 
			 LiveLocationService liveLocationService = new LiveLocationService();
			 List<Location> fetchedLocations = liveLocationService.getLocations(context);
			 if (fetchedLocations != null) {
				 Log.i(TAG, "Replacing cached locations with fetched locations");
				 localLocationService.saveLocations(context, fetchedLocations);
			 }
			 
			 if (existingLocations != null && !existingLocations.isEmpty() && fetchedLocations != null && !fetchedLocations.isEmpty()) {				 
				 checkForAndNotifyOfNewLocationUpdates(existingLocations, fetchedLocations);
			 }
			 
			 announceBatchFinished();
             running = false;
		 }
	}

	private void checkForAndNotifyOfNewLocationUpdates(List<Location> existingLocations, List<Location> fetchedLocations) {
		Location existingLatest = existingLocations.get(0);
		Location newLatest = fetchedLocations.get(0);
		 
		Log.i(TAG, "Comparing most recent locations: " + existingLatest.getDate() + newLatest.getDate());
		if (!existingLatest.equals(newLatest)) {
			Log.i(TAG, "A new location has been found in the latest update");
			sendNotification(newLatest);		 
		 } else {
			 Log.i(TAG, "No new location fixes were found in this update");
		 }
	}
	
	private void announceBatchFinished() {
		Intent intent = new Intent(LocationUpdateService.BATCH_COMPLETION);
		context.sendBroadcast(intent);
	}
	
	private void sendNotification(Location location) {
		int icon = R.drawable.icon;
		CharSequence tickerText = "Location update received";
		Notification notification = new Notification(icon, tickerText, DateTimeHelper.now().getTime());

		final CharSequence contentTitle = "Location update received";
		final CharSequence contentText = location.toString();
		
		Intent notificationIntent = new Intent(context, main.class);		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notificationManager.notify(LocationUpdateService.UPDATE_COMPLETE_NOTIFICATION_ID, notification);
	}
	
}
