package nz.gen.wellington.penguin.data;

import java.util.ArrayList;
import java.util.List;

import nz.gen.wellington.penguin.model.Location;
import nz.gen.wellington.penguin.utils.DateTimeHelper;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;

import android.util.Log;

public class KMLHandler extends HandlerBase {

	private static final String TAG = "KMLHandler";

	private Location currentPoint = null;
	private StringBuilder currentField;
	private List<Location> points;
	
	public List<Location> getPoints() {
		Log.d(TAG, "Returning: " + points);
		return points;
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		points = new ArrayList<Location>();		
	}
	
	@Override
	public void startElement(String name, AttributeList attributes) throws SAXException {		
		super.startElement(name, attributes);
		if (name.equals("Placemark")) {
			final boolean isNormalPoint = attributes.getValue("id") == null;
			if (isNormalPoint) {
				Log.d(TAG, "Starting point: " + name);
				currentPoint = new Location();
			}
		}
		
		if (name.equals("when")) {
			currentField = new StringBuilder();
		}
		
		if (name.equals("coordinates")) {
			currentField = new StringBuilder();
		}
	}

	@Override
	public void endElement(String name) throws SAXException {
		super.endElement(name);
		if (name.equals("when") && currentPoint != null) {
			currentPoint.setDate(DateTimeHelper.parseDate(currentField.toString()));
			currentField = null;
		}
		
		if (name.equals("coordinates") && currentPoint != null) {
			final String coordinates = currentField.toString();
			currentPoint.setLongitude(Double.parseDouble(coordinates.split(",")[0]));
			currentPoint.setLatitude(Double.parseDouble(coordinates.split(",")[1]));
			currentField = null;
		}
		
		if (name.equals("Placemark") && currentPoint != null) {
			Log.d(TAG, "Closing point");
			points.add(new Location(currentPoint.getDate(), currentPoint.getLongitude(), currentPoint.getLatitude()));
			currentPoint = null;
		}		
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (currentField != null) {
			for (int i = start; i < start + length; i++) {
				currentField.append(ch[i]);
			}
		}
	}

}
