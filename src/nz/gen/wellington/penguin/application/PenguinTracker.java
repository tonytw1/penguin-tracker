package nz.gen.wellington.penguin.application;

import nz.gen.wellington.penguin.timers.LocationUpdateAlarmSetter;
import android.app.Application;
import android.util.Log;

public class PenguinTracker extends Application {

    private static final String TAG = "PenguinTracker";
    
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Setting location update alarm");
		LocationUpdateAlarmSetter alarmSetter = new LocationUpdateAlarmSetter();
		alarmSetter.setHourlyContentUpdateAlarm(this.getApplicationContext());
	}
	
}
