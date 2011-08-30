package nz.gen.wellington.penguin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class DateTimeHelper {
	
	private static final String TAG = "DateTimeHelper";
	
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String ZULU_DATA_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String ZULU_TIME_ZONE = "Zulu";
	
	public static Date parseDate(String dateString) {		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		if (dateString.endsWith("Z")) {
			dateFormat = new SimpleDateFormat(ZULU_DATA_TIME_FORMAT);
			dateFormat.setTimeZone(java.util.TimeZone.getTimeZone(ZULU_TIME_ZONE));
		}	
		 
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			Log.e(TAG, "Failed to parse date '" + dateString +  "': " + e.getMessage());
		}
		return null;
	}

	public static Date now() {
	    Calendar cal = Calendar.getInstance();
	    return cal.getTime();
	}

	public static Date yesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -24);
		return cal.getTime();
	}		

	public static String format(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static String calculateTimeTaken(Date startTime, Date now) {
		int seconds = durationInSecords(startTime, now);
		
		StringBuilder output = new StringBuilder();
		int hours = (seconds / (60 * 60));	
		if (hours > 0) {
			output.append(hours + " " + Plurals.getPrural("hour", hours));
			return output.toString();
		}
				
		int minutes = (seconds / 60);
		if (minutes > 0) {
			output.append(minutes + " " + Plurals.getPrural("minute", minutes));
			return output.toString();
		}
		
		int remainer = (seconds % 60);
		output.append(" " + remainer + " " + Plurals.getPrural("second", remainer));		
		return output.toString();
	}
	
	public static int durationInSecords(Date startTime, Date now) {
		long mills = now.getTime() - startTime.getTime();
		int seconds = new Long(mills / 1000).intValue();
		return seconds;
	}
	
}