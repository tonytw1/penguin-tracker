package nz.gen.wellington.penguin.data.tracker;

import java.util.Calendar;
import java.util.Date;

import nz.gen.wellington.penguin.utils.DateTimeHelper;
import android.util.Log;

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
		final Calendar utcCalendar = DateTimeHelper.getUTCCalender();
		final int utcHours = utcCalendar.get(Calendar.HOUR_OF_DAY);
		Log.i(TAG, "Current utc time: " + utcHours + ":" + utcCalendar.get(Calendar.MINUTE));
		if (utcHours >= 8 && utcHours < 12) {
			return true;
		}
		if (utcHours >=18 && utcHours < 21) {
			return true;
		}
		return false;
	}
	
	private Date getNextScheduled() {
		final Calendar utcCalendar = DateTimeHelper.getUTCCalender();
		final int utcHours = utcCalendar.get(Calendar.HOUR_OF_DAY);		
		if (utcHours < 8) {
			utcCalendar.set(Calendar.HOUR_OF_DAY, 8);
			utcCalendar.set(Calendar.MINUTE, 0);
			utcCalendar.set(Calendar.SECOND, 0);
			return utcCalendar.getTime();
		}
		
		if (utcHours < 18) {
			utcCalendar.set(Calendar.HOUR_OF_DAY, 18);
			utcCalendar.set(Calendar.MINUTE, 0);
			utcCalendar.set(Calendar.SECOND, 0);		
			return utcCalendar.getTime();
		}
		
		utcCalendar.set(Calendar.HOUR_OF_DAY, 8);
		utcCalendar.set(Calendar.MINUTE, 0);
		utcCalendar.set(Calendar.SECOND, 0);
		utcCalendar.add(Calendar.DATE, 1);
		return utcCalendar.getTime();		
	}
	
}
