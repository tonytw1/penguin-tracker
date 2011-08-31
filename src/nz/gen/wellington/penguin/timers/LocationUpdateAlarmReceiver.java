package nz.gen.wellington.penguin.timers;

import java.util.Date;

import nz.gen.wellington.penguin.R;
import nz.gen.wellington.penguin.main;
import nz.gen.wellington.penguin.network.NetworkStatusService;
import nz.gen.wellington.penguin.utils.DateTimeHelper;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationUpdateAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		 NetworkStatusService networkStatusService = new NetworkStatusService(context);
         if (networkStatusService.isBackgroundDataAvailable()) {
        	 Intent serviceIntent = new Intent(context, LocationUpdateService.class);
        	 serviceIntent.setAction("RUN");
        	 sendNotification(context);
        	 context.startService(serviceIntent);
         }
	}

	private void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        int icon = R.drawable.icon;
        CharSequence tickerText = "Content update starting";                    
        Date now = DateTimeHelper.now();
        
        Notification notification = new Notification(icon, tickerText, now.getTime());
        
        CharSequence contentTitle = "Content update starting";
        CharSequence contentText = "Autosync update started";
        
        Intent notificationIntent = new Intent(context, main.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notificationManager.notify(LocationUpdateService.UPDATE_COMPLETE_NOTIFICATION_ID, notification);
	}
	
}
