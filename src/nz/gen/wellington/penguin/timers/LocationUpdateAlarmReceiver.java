package nz.gen.wellington.penguin.timers;

import nz.gen.wellington.penguin.config.Config;
import nz.gen.wellington.penguin.network.NetworkStatusService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationUpdateAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkStatusService networkStatusService = new NetworkStatusService(context);
		
		if (Config.areUpdatesEnabled(context)) {
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
