package nz.gen.wellington.penguin.timers;

import nz.gen.wellington.penguin.config.Config;
import nz.gen.wellington.penguin.network.NetworkStatusService;
import nz.gen.wellington.penguin.utils.DateTimeHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationUpdateAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = "LocationUpdateAlarmReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkStatusService networkStatusService = new NetworkStatusService(context);
		
		if (Config.areUpdatesEnabled(context)) {
			Log.i(TAG, "Location update triggered at: " + DateTimeHelper.format(DateTimeHelper.now(), "hh:mm a"));			
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
