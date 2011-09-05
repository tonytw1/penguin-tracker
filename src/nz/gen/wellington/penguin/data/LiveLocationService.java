package nz.gen.wellington.penguin.data;

import java.io.InputStream;
import java.util.List;

import nz.gen.wellington.penguin.config.Config;
import nz.gen.wellington.penguin.data.kml.KMLParser;
import nz.gen.wellington.penguin.model.Location;
import nz.gen.wellington.penguin.network.HttpFetcher;
import android.content.Context;

public class LiveLocationService implements LocationService {
	
	public List<Location> getLocations(Context context) {
		HttpFetcher httpFetcher = new HttpFetcher(context);
		InputStream inputStream = httpFetcher.httpFetch(Config.FEED_URL);
		if (inputStream != null) {
			KMLParser parser = new KMLParser();
			return parser.parse(inputStream);
		}
		return null;
	}

}
