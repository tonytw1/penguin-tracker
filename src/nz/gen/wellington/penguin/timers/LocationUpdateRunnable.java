package nz.gen.wellington.penguin.timers;

import nz.gen.wellington.penguin.data.LocationUpdater;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

public class LocationUpdateRunnable implements Runnable {
	
	private Context context;
	private NotificationManager notificationManager;
    
	private boolean running = false;
	
	public LocationUpdateRunnable(Context context, NotificationManager notificationManager) {
		this.context = context;
		this.notificationManager = notificationManager;
	}
	
	@Override
	public void run() {
		 running = true;
		 while(running) {
			 LocationUpdater locationUpdater = new LocationUpdater();
			 locationUpdater.updateLocations(context, notificationManager);			 
			 announceBatchFinished();
             running = false;
		 }
	}
	
	private void announceBatchFinished() {
		Intent intent = new Intent(LocationUpdateService.BATCH_COMPLETION);
		context.sendBroadcast(intent);
	}

}
