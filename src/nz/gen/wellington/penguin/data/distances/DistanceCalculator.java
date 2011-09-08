package nz.gen.wellington.penguin.data.distances;

import nz.gen.wellington.penguin.model.Location;

public class DistanceCalculator {

	private static final int CIRCUMFERENCE_OF_THE_EARTH_IN_KILOMETERS = 40008;
	
	public double getSoutherlyDistanceBetween(Location latestFix, Location previousFix) {
		double latitudeDelta = latestFix.getLatitude() - previousFix.getLatitude();
		double kilometerDelta = latitudeDelta * (CIRCUMFERENCE_OF_THE_EARTH_IN_KILOMETERS / 360);
		return kilometerDelta;
	}
	
}
