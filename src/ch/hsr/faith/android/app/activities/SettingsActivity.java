package ch.hsr.faith.android.app.activities;

import org.apache.log4j.Logger;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.services.GeoLocationService;

public class SettingsActivity extends BaseActivity {
	private Logger logger = Logger.getRootLogger();
	private GeoLocationService geoLocationService;
	private boolean gotTheLocation;
	private GeoLocationService.LocationResult locationResult;
	private Editor editor;
	protected SharedPreferences geoLocation;
	protected static final String geoLocationLatitudePreferenceName = "LOCATION_LATITUDE", geoLocationLongitudePreferenceName = "LOCATION_LONGITUDE",
			positionSharedPreference = "FAITH-POSITION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		geoLocation = getSharedPreferences(positionSharedPreference, 0);
		logger.info(geoLocation);
		String latitudePreferencesString = geoLocation.getString(geoLocationLatitudePreferenceName, null);
		String longitudePreferencesString = geoLocation.getString(geoLocationLongitudePreferenceName, null);
		if (latitudePreferencesString != null && longitudePreferencesString != null) {
			View checkBoxSaveGpsData = findViewById(R.id.checkBoxSaveGpsData);
			((CheckBox) checkBoxSaveGpsData).setChecked(true);
		}
	}

	public void onCheckboxSaveGpsLocClicked(View view) {
		boolean checked = ((CheckBox) view).isChecked();
		if (view.getId() == R.id.checkBoxSaveGpsData)
			if (checked) {
				getGeoLocation();

			} else {
				deleteSavedGpsLocation();
			}
	}

	private void getGeoLocation() {
		locationResult = new GeoLocationService.LocationResult() {

			private Location loc;

			@Override
			public void gotLocation(Location location) {
				this.setLoc(location);
				saveGeoLocationOnSharedMemory(location);
			}

			@Override
			public Location getLoc() {
				return loc;
			}

			@Override
			public void setLoc(Location loc) {
				this.loc = loc;
			}
		};
		geoLocationService = new GeoLocationService();
		gotTheLocation = geoLocationService.invokeGettingLocation(this, locationResult);

		if (gotTheLocation == false) {
			Toast t = Toast.makeText(getApplicationContext(), getText(R.string.dialog_alert_gps_disabled).toString(), Toast.LENGTH_LONG);
			t.show();
		} else {
			Toast t = Toast.makeText(getApplicationContext(), "fetching location...", Toast.LENGTH_LONG);
			t.show();
		}

	}

	private void deleteSavedGpsLocation() {

		editor = geoLocation.edit();
		editor.clear();
		editor.apply();
		Toast t = Toast.makeText(getApplicationContext(), "Location sucessfully removed", Toast.LENGTH_LONG);
		t.show();

	}

	private void saveGeoLocationOnSharedMemory(Location loc) {

		try {
			editor = geoLocation.edit();
			editor.putString(geoLocationLatitudePreferenceName, String.valueOf((float) loc.getLatitude()));
			editor.putString(geoLocationLongitudePreferenceName, String.valueOf((float) loc.getLongitude()));
			editor.apply();
			logger.info("Wrote to shared preferences: Longitude -> " + geoLocation.getString(geoLocationLongitudePreferenceName, null) + " and Latitude -> "
					+ geoLocation.getString(geoLocationLatitudePreferenceName, null));
			Toast t = Toast.makeText(getApplicationContext(), "Location sucessfully saved ", Toast.LENGTH_LONG);
			t.show();
		} catch (Exception ex) {
			Toast t = Toast.makeText(getApplicationContext(), "Location could not be saved: ", Toast.LENGTH_LONG);
			t.show();
		}
		return;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (geoLocationService != null)
			geoLocationService.cancelTimer();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
			return rootView;
		}

	}

}
