package ch.hsr.faith.android.app.services;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import ch.hsr.faith.android.app.activities.BaseActivity;

public class GeoLocationService extends BaseActivity {

	private Timer timer1;
	private LocationManager locationManager;
	protected LocationResult locationResult;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private Editor editor;
	private Logger logger = Logger.getRootLogger();
	private SharedPreferences geoLocation;
	public static final String GEO_LOCATION_LATITUDE = "LOCATION_LATITUDE", GEO_LOCATION_LONGITUDE = "LOCATION_LONGITUDE", GEO_LOCATION = "FAITH-POSITION";

	public GeoLocationService(Context context) {
		super();
		geoLocation = context.getSharedPreferences(GEO_LOCATION, MODE_PRIVATE);
	}

	public Location getPositionFromSharedPreferences() {

		String latitudePreferencesString = geoLocation.getString(GEO_LOCATION_LATITUDE, null);
		String longitudePreferencesString = geoLocation.getString(GEO_LOCATION_LONGITUDE, null);

		if (latitudePreferencesString == null || longitudePreferencesString == null) {
			return null;
		} else {
			double latitude = Double.parseDouble(latitudePreferencesString);
			double longitude = Double.parseDouble(longitudePreferencesString);

			Location location = new Location("LocationFromPreference");
			location.setLatitude(latitude);
			location.setLongitude(longitude);

			return location;
		}
	}

	public boolean saveGeoLocationOnSharedMemory(Location loc) {

		try {
			editor = geoLocation.edit();
			editor.putString(GEO_LOCATION_LATITUDE, String.valueOf((float) loc.getLatitude()));
			editor.putString(GEO_LOCATION_LONGITUDE, String.valueOf((float) loc.getLongitude()));
			editor.apply();
			logger.info("Wrote to shared preferences: Longitude -> " + geoLocation.getString(GEO_LOCATION_LONGITUDE, null) + " and Latitude -> "
					+ geoLocation.getString(GEO_LOCATION_LATITUDE, null));
			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	public boolean deleteSavedGpsLocation() {

		try {
			editor = geoLocation.edit();
			editor.clear();
			editor.apply();
			return true;

		} catch (Exception e) {
			return false;
		}

	}

	public boolean isGpsOrNetworkEnabled(Context context, LocationResult result) {
		locationResult = result;
		if (locationManager == null)
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled)
			return false;

		if (gps_enabled)
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		if (network_enabled)
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		timer1 = new Timer();
		timer1.schedule(new GetLastLocation(), 2000);
		return true;
	}

	public void cancelTimer() {
		timer1.cancel();
		locationManager.removeUpdates(locationListenerGps);
		locationManager.removeUpdates(locationListenerNetwork);
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location);
			locationManager.removeUpdates(this);
			locationManager.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location);
			locationManager.removeUpdates(this);
			locationManager.removeUpdates(locationListenerGps);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onProviderDisabled(String provider) {

		}
	};

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			locationManager.removeUpdates(locationListenerGps);
			locationManager.removeUpdates(locationListenerNetwork);

			Location networkLocation = null, gpsLocation = null;
			if (gps_enabled)
				gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (network_enabled)
				networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (gpsLocation != null && networkLocation != null) {
				if (gpsLocation.getTime() > networkLocation.getTime())
					locationResult.gotLocation(gpsLocation);
				else
					locationResult.gotLocation(networkLocation);
				return;
			}

			if (gpsLocation != null) {
				locationResult.gotLocation(gpsLocation);
				return;
			}
			if (networkLocation != null) {
				locationResult.gotLocation(networkLocation);
				return;
			}
			locationResult.gotLocation(null);
		}
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}
}
