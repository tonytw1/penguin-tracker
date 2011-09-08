package nz.gen.wellington.penguin.widgets;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import nz.gen.wellington.penguin.R;
import nz.gen.wellington.penguin.main;
import nz.gen.wellington.penguin.config.Config;
import nz.gen.wellington.penguin.data.LocationDAO;
import nz.gen.wellington.penguin.data.LocationService;
import nz.gen.wellington.penguin.data.distances.DistanceCalculator;
import nz.gen.wellington.penguin.data.tracker.TrackerScheduleService;
import nz.gen.wellington.penguin.model.Location;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		LocationService cachingLocationService = new LocationDAO();
		cachingLocationService.getLocations(context);	
		refresh(context, cachingLocationService.getLocations(context), appWidgetIds);
	}
	
	private void refresh(Context context, List<Location> locations, int[] appWidgetIds) {
		RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
		if (locations != null && !locations.isEmpty()) {
			Location latestFix = locations.get(0);
			Location previousFix = extractPreviousFix(locations, latestFix);

			populateWidget(widgetView, context, latestFix, previousFix);
		}
		
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		if (manager != null) {
			manager.updateAppWidget(appWidgetIds, widgetView);
		}
	}
	
	private void populateWidget(RemoteViews widgetView, Context context, Location latestFix, Location previousFix) {
		if (latestFix == null) {
			return;
		}
		widgetView.setTextViewText(R.id.heading, latestFix.timeAgo());
		widgetView.setTextViewText(R.id.snippet, latestFix.toString());
		
		if (previousFix == null) {
			return;
		}
		
		final double kilometerDelta = new DistanceCalculator().getSoutherlyDistanceBetween(latestFix, previousFix);
			
		DecimalFormat df = new DecimalFormat("0.0");
		widgetView.setTextViewText(R.id.delta, df.format(kilometerDelta * -1));
		widgetView.setTextColor(R.id.delta, kilometerDelta < 0 ? Color.parseColor(Config.DARK_GREEN) : Color.parseColor(Config.DARK_RED));			
				
		PendingIntent pendingIntent = createShowArticleIntent(context);
		widgetView.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);
	}
	
	private Location extractPreviousFix(List<Location> locations, Location latestFix) {
		// As all updates for a given transmission window tend to turn up in the same update,
		// we should use the last fix, in the previous cluster as the reference.
		// This will give a more meaningful delta of say, 12km in 7 hours rather then say 400m in 5 minutes.		
		Location previousFix = null;
		TrackerScheduleService trackerScheduleService = new TrackerScheduleService();
		Iterator<Location> iterator = locations.iterator();
		while (previousFix == null && iterator.hasNext()) {
			Location olderLocation = iterator.next();			
			if (!trackerScheduleService.timesAreWithinTheSameTransmissionWindow(latestFix.getDate(), olderLocation.getDate())) {
				previousFix = olderLocation;
			}
		}		
		return previousFix;
	}
	
	private PendingIntent createShowArticleIntent(Context context) {
		Intent intent = new Intent(context, main.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		return pendingIntent;
	}
	
}
