package nz.gen.wellington.penguin.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {

	public static String FEED_URL = "http://www.nzemperor.com/rest/Position.kml";
	public static final int CACHE_TTL = 60 * 30;
	
	public static boolean areUpdatesEnabled(Context context) {
		PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		final boolean updatesAreEnabled = prefs != null && prefs.getBoolean("sync", true);
		return updatesAreEnabled;
	}
	
}
