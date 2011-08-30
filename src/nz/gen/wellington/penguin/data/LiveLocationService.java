package nz.gen.wellington.penguin.data;

import java.io.InputStream;
import java.util.List;

import nz.gen.wellington.penguin.model.Location;
import nz.gen.wellington.penguin.network.HttpFetcher;
import android.content.Context;

public class LiveLocationService implements LocationService {
		
	private static String FEED_URL = "http://www.nzemperor.com/rest/Position.kml";
	
	public List<Location> getLocations(Context context) {
		HttpFetcher httpFetcher = new HttpFetcher(context);
		InputStream inputStream = httpFetcher.httpFetch(FEED_URL);
		KMLParser parser = new KMLParser();
		return parser.parse(inputStream);
	}

}
