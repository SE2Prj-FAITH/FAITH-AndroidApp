package ch.hsr.faith.android.app.activities;

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

	GeoLocationService geoLocationService;
	boolean gotTheLocation;
	GeoLocationService.LocationResult locationResult;

	protected SharedPreferences geoLocation;
	protected String faithGeoLocationLatitudePreferenceName = "LOCATION_LATITUDE";
	protected String faithGeoLocationLongitudePreferenceName= "LOCATION_LONGITUDE";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		geoLocation = getSharedPreferences("FAIHT-GEO-LOCATION", 0);

		String latitudePreferencesString = faithGeoLocationLatitudePreferenceName;
		String longitudePreferencesString = faithGeoLocationLongitudePreferenceName;
		if (latitudePreferencesString != "" && longitudePreferencesString != "") {
			showErrorDialog(latitudePreferencesString + " / " + longitudePreferencesString);
			View checkBoxSaveGpsLoc = findViewById(R.id.checkBoxSaveGpsData);
			((CheckBox) checkBoxSaveGpsLoc).setChecked(true);
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

		Editor editor = geoLocation.edit();
		geoLocation.edit().putString(faithGeoLocationLatitudePreferenceName, "");
		geoLocation.edit().putString(faithGeoLocationLongitudePreferenceName, "");
		editor.apply();

		Toast t = Toast.makeText(getApplicationContext(), "Location sucessfully removed", Toast.LENGTH_LONG);
		t.show();

	}

	private void saveGeoLocationOnSharedMemory(Location loc) {

		try {
			Editor editor = geoLocation.edit();
			geoLocation.edit().putString(faithGeoLocationLatitudePreferenceName, String.valueOf((float) loc.getLatitude()));
			geoLocation.edit().putString(faithGeoLocationLongitudePreferenceName, String.valueOf((float) loc.getLongitude()));
			editor.apply();

			Toast t = Toast.makeText(getApplicationContext(), "Location sucessfully saved", Toast.LENGTH_LONG);
			t.show();
		} catch (Exception ex) {
			Toast t = Toast.makeText(getApplicationContext(), "Location could not be saved: " + ex.getMessage(), Toast.LENGTH_LONG);
			t.show();
		}
		return;
	}

	protected void onPause() {
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
