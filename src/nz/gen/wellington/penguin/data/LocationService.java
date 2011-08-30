package nz.gen.wellington.penguin.data;

import java.util.List;

import android.content.Context;

import nz.gen.wellington.penguin.model.Location;

public interface LocationService {

	public List<Location> getLocations(Context context);

}
