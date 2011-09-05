package nz.gen.wellington.penguin.data.tracker;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

import nz.gen.wellington.penguin.utils.DateTimeHelper;

// @NZEmperor "To save battery the tracker is only active from 0800 to 1200 and 1800-2100 UTC every day."
public class TrackerScheduleService {
	
	private static final String TAG = "TrackerScheduleService";

	public String getTrackerStatus() {
		if (isCurrentlyScheduledToTransmit()) {
			return "The tracker is scheduled to transmit at this time";
			
		} else {
			final Date nextScheduled = getNextScheduled();
			return "The tracker is next scheduled to transmit in " + DateTimeHelper.calculateTimeTaken(DateTimeHelper.now(), nextScheduled) + " at " + DateTimeHelper.format(nextScheduled, "h:mm a");
		}
	}
	
	public boolean isCurrentlyScheduledToTransmit() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Zulu"));
		final int utcHours = calendar.get(Calendar.HOUR_OF_DAY);
		Log.i(TAG, "Current utc time: " + utcHours + ":" + calendar.get(Calendar.MINUTE));
		if (utcHours >= 8 && utcHours < 12) {
			return true;
		}
		if (utcHours >=18 && utcHours < 21) {
			return true;
		}
		return false;
	}
	
	private Date getNextScheduled() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Zulu"));
		final int utcHours = calendar.get(Calendar.HOUR_OF_DAY);		
		if (utcHours < 8) {
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			return calendar.getTime();
		}
		
		if (utcHours < 18) {
			calendar.set(Calendar.HOUR_OF_DAY, 18);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);		
			return calendar.getTime();
		}
		
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();		
	}
	
}
