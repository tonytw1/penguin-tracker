package nz.gen.wellington.penguin.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import nz.gen.wellington.penguin.utils.DateTimeHelper;

public class Location implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	private Date date;
	private double longitude;
	private double latitude;
	
	public Location(Date date, double longitude, double latitude) {
		this.date = date;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Location() {
	}

	public Date getDate() {
		return date;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.##");
		String latitudeLabel = latitude < 0 ? "S" : "N";
		String longitudeLabel = longitude < 0 ? "W" : "E";		
		return df.format(latitude > 0 ? latitude : latitude * -1)  + latitudeLabel + ", " + 
			df.format(longitude >0 ? longitude : longitude * -1) + longitudeLabel + " at " + DateTimeHelper.format(date, "h:mm a, d MMM");
	}

	public void setDate(Date date) {
		this.date = date;		
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	public String timeAgo() {
		return DateTimeHelper.calculateTimeTaken(date, DateTimeHelper.now()) + " ago";
	}
	
}
