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
import android.os.PowerManager;
import android.util.Log;

public class LocationUpdateRunnable implements Runnable {

    private static final String TAG = "InternalRunnable";
    
	private Context context;
    private NotificationManager notificationManager;
    private PowerManager.WakeLock wl;

	private boolean running = false;
	
	public LocationUpdateRunnable(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}
	
	@Override
	public void run() {
		 createWakeLock();
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
				 
				 Location existingLatest = existingLocations.get(0);
				 Location newLatest = fetchedLocations.get(0);
				 
				 Log.i(TAG, "Comparing most recent locations: " + existingLatest.getDate() + newLatest.getDate());
				 if (!existingLatest.equals(newLatest)) {
					 Log.i(TAG, "A new location has been found in the latest update");
					 sendNotification();                     
				 }				 
			 }
			 
			 announceBatchFinished();
             running = false;
		 }
     
		 Log.i(TAG, "Content update has completed - releasing wake lock");
		 releaseWakeLock();           
	}

	private void announceBatchFinished() {
		Intent intent = new Intent(LocationUpdateService.BATCH_COMPLETION);
		context.sendBroadcast(intent);
	}
	
	private void sendNotification() {
		int icon = R.drawable.icon;
		CharSequence tickerText = "Content update complete";
		Notification notification = new Notification(icon, tickerText, DateTimeHelper.now().getTime());

		final CharSequence contentTitle = "Content update complete";
		final CharSequence contentText = "Fetched update";
		final String fullReport = "A new location fix has been found in the latest update";

		Intent notificationIntent = new Intent(context, main.class);
		notificationIntent.putExtra("report", fullReport);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notificationManager.notify(LocationUpdateService.UPDATE_COMPLETE_NOTIFICATION_ID, notification);
	}
	
	private void createWakeLock() {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		wl.acquire();
	}

	private void releaseWakeLock() {
		if (wl != null) {
			wl.release();
		}
	}
	
}
