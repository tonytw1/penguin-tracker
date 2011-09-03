package nz.gen.wellington.penguin.timers;

import nz.gen.wellington.penguin.network.NetworkStatusService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocationUpdateAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkStatusService networkStatusService = new NetworkStatusService(context);
				
		PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (prefs != null && prefs.getBoolean("sync", true)) {
			if (networkStatusService.isBackgroundDataAvailable()) {
				Intent serviceIntent = new Intent(context, LocationUpdateService.class);
				serviceIntent.setAction("RUN");
				context.startService(serviceIntent);
			}
		}
		LocationUpdateAlarmSetter alarmSetter = new LocationUpdateAlarmSetter();
		alarmSetter.setHourlyContentUpdateAlarm(context);
	}

}
