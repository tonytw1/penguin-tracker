package nz.gen.wellington.penguin.model;

import java.io.Serializable;
import java.util.Date;

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
		// TODO Auto-generated constructor stub
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
		return "Location [longitude=" + longitude + ", latitude=" + latitude + "]";
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
	
}
