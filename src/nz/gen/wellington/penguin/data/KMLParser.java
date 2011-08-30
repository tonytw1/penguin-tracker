package nz.gen.wellington.penguin.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nz.gen.wellington.penguin.model.Location;

import org.xml.sax.SAXException;

import android.util.Log;

public class KMLParser {

	private static final String TAG = "KMLParser";

	public List<Location> parse(InputStream inputStream) {
		try {
			KMLHandler handler = new KMLHandler();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(inputStream, handler);
			inputStream.close();
			return handler.getPoints();
			
		} catch (SAXException e) {
			Log.e(TAG, "SAXException while parsing content xml: " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException while parsing content xml: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "ParserConfigurationException while parsing content xml: " + e.getMessage());
		}
		return null;
	}

}
