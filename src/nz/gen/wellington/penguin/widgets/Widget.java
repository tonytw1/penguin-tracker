package nz.gen.wellington.penguin.widgets;

import java.text.DecimalFormat;
import java.util.List;

import nz.gen.wellington.penguin.R;
import nz.gen.wellington.penguin.main;
import nz.gen.wellington.penguin.data.LocationDAO;
import nz.gen.wellington.penguin.data.LocationService;
import nz.gen.wellington.penguin.model.Location;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
	
	private static final String TAG = "Widget";
	private static final int CIRCUMFERENCE_OF_THE_EARTH_IN_KILOMETERS = 40008;

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
			populateWidget(widgetView, context, locations);
		}
		
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		if (manager != null) {
			manager.updateAppWidget(appWidgetIds, widgetView);
		}
	}
	
	private void populateWidget(RemoteViews widgetView, Context context, List<Location> locations) {
		Location latestFix = locations.get(0);
		widgetView.setTextViewText(R.id.heading, latestFix.timeAgo());
		widgetView.setTextViewText(R.id.snippet, latestFix.toString());
		
		if (locations.size() > 1) {
			Location previousFix = locations.get(1);
			double latitudeDelta = latestFix.getLatitude() - previousFix.getLatitude();
			Log.i(TAG, latestFix.getLatitude() + " - " + previousFix.getLatitude() + " = " + latitudeDelta);
			double kilometerDelta = latitudeDelta * (CIRCUMFERENCE_OF_THE_EARTH_IN_KILOMETERS / 360);
			
			DecimalFormat df = new DecimalFormat("#.#");
			widgetView.setTextViewText(R.id.delta, df.format(kilometerDelta * -1));
			widgetView.setTextColor(R.id.delta, kilometerDelta < 0 ? Color.parseColor("#00aa00") : Color.parseColor("#aa0000"));			
		}
		
		PendingIntent pendingIntent = createShowArticleIntent(context);
		widgetView.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);
	}

	private PendingIntent createShowArticleIntent(Context context) {
		Intent intent = new Intent(context, main.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		return pendingIntent;
	}
	
}
