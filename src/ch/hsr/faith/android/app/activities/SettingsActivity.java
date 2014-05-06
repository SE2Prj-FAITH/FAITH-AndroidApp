package ch.hsr.faith.android.app.activities;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.services.GeoLocationService;
import ch.hsr.faith.android.app.services.GeoLocationService.LocationResult;

public class SettingsActivity extends BaseActivity {
	private GeoLocationService geoLocationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		this.geoLocationService = new GeoLocationService(getApplicationContext());
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (geoLocationService.getPositionFromSharedPreferences() != null) {
			View checkBoxSaveGpsData = findViewById(R.id.checkBoxSaveGpsData);
			((CheckBox) checkBoxSaveGpsData).setChecked(true);
		}
	}

	public void onCheckboxSaveGpsLocClicked(View view) {
		boolean checkBoxIsChecked = ((CheckBox) view).isChecked();
		if (view.getId() == R.id.checkBoxSaveGpsData) {
			if (checkBoxIsChecked) {
				LocationResult locationResult = new GeoLocationService.LocationResult() {
					@Override
					public void gotLocation(Location location) {
						geoLocationService.saveGeoLocation(location);
					}
				};
				boolean gpsOrNetworkEnabled = geoLocationService.isGpsOrNetworkEnabled(getApplicationContext(), locationResult);

				if (gpsOrNetworkEnabled == false) {
					Toast.makeText(getApplicationContext(), getText(R.string.dialog_alert_gps_disabled).toString(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "fetching location...", Toast.LENGTH_LONG).show();
				}
			} else {
				geoLocationService.deleteSavedGpsLocation();
			}
		}
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
