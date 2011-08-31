package nz.gen.wellington.penguin.timers;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationUpdateAlarmSetter {

    private static final String TAG = "LocationUpdateAlarmSetter";

    private static final long ONE_MINUTE = 60000;
    private static final long ONE_HOUR = ONE_MINUTE; // TODO Dev mode setting

	public void setHourlyContentUpdateAlarm(Context context) {
	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    PendingIntent pi = makeContentUpdatePendingIntent(context);
	    
	    final long timeInMillis = getNextHourlyAutoSyncTime();
	    Log.i(TAG, "Setting sync alarm for: " + new Date(timeInMillis).toLocaleString());
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, ONE_HOUR, pi);
	}
	
    private PendingIntent makeContentUpdatePendingIntent(Context context) {
            Intent i=new Intent(context, LocationUpdateAlarmReceiver.class);
            PendingIntent pi= PendingIntent.getBroadcast(context, 0, i, 0);
            return pi;
    }
    
    private long getNextHourlyAutoSyncTime() {
        Calendar time = Calendar.getInstance();                                 
        long timeInMillis = time.getTimeInMillis() + ONE_HOUR;          
        return timeInMillis;
    }
    
}
