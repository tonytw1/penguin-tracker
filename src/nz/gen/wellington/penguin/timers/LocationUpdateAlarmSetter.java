package nz.gen.wellington.penguin.timers;

import java.util.Calendar;
import java.util.Date;

import nz.gen.wellington.penguin.config.Config;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationUpdateAlarmSetter {

    private static final String TAG = "LocationUpdateAlarmSetter";

    private static final long INTERVAL = Config.CACHE_TTL * 1000;

	public void setHourlyContentUpdateAlarm(Context context) {
	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    PendingIntent pi = makeContentUpdatePendingIntent(context);
	    
	    final long timeInMillis = getNextAutoSyncTime();
	    Log.i(TAG, "Setting sync alarm for: " + new Date(timeInMillis).toLocaleString());
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, INTERVAL, pi);
	}
	
    private PendingIntent makeContentUpdatePendingIntent(Context context) {
            Intent i=new Intent(context, LocationUpdateAlarmReceiver.class);
            PendingIntent pi= PendingIntent.getBroadcast(context, 0, i, 0);
            return pi;
    }
    
    private long getNextAutoSyncTime() {
        Calendar time = Calendar.getInstance();                                 
        long timeInMillis = time.getTimeInMillis() + INTERVAL;          
        return timeInMillis;
    }
    
}
