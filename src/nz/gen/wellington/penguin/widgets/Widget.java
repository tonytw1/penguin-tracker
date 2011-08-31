package nz.gen.wellington.penguin.widgets;

import java.util.List;

import nz.gen.wellington.penguin.R;
import nz.gen.wellington.penguin.main;
import nz.gen.wellington.penguin.data.CachingLocationService;
import nz.gen.wellington.penguin.data.LocationService;
import nz.gen.wellington.penguin.model.Location;
import nz.gen.wellington.penguin.utils.DateTimeHelper;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		LocationService cachingLocationService = new CachingLocationService();
		cachingLocationService.getLocations(context);	
		refresh(context, cachingLocationService.getLocations(context), appWidgetIds);
	}
	
	private void refresh(Context context, List<Location> locations, int[] appWidgetIds) {
		RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
		if (locations != null && !locations.isEmpty()) {
			populateArticle(widgetView, context, locations.get(0));
		}
		
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		if (manager != null) {
			manager.updateAppWidget(appWidgetIds, widgetView);
		}
	}
	
	private void populateArticle(RemoteViews widgetView, Context context, Location location) {
		widgetView.setTextViewText(R.id.heading, DateTimeHelper.calculateTimeTaken(location.getDate(), DateTimeHelper.now()));
		final String snippet = location.getLatitude() + ", " + location.getLongitude() + " at " + DateTimeHelper.format(location.getDate(), "HH:mm, dd MMM yyyy");
		widgetView.setTextViewText(R.id.snippet, snippet);
		
		PendingIntent pendingIntent = createShowArticleIntent(context);
		widgetView.setOnClickPendingIntent(R.id.heading, pendingIntent);
	}

	private PendingIntent createShowArticleIntent(Context context) {
		Intent intent = new Intent(context, main.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		return pendingIntent;
	}
	
}
