package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.domain.FacilityWithDistance;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilitiesMapFragment extends MapFragment {

	private GoogleMap map;
	private FacilitiesTabActivity context;
	private List<FacilityWithDistance> listOfFacilities;
	private Map<Marker, FacilityWithDistance> allMarkersMap;

	private double lastLatitude = 0;
	private double lastLongitude = 0;

	public FacilitiesMapFragment() {
		listOfFacilities = new ArrayList<FacilityWithDistance>();
		allMarkersMap = new HashMap<Marker, FacilityWithDistance>();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.context = (FacilitiesTabActivity) getActivity();
		map = getMap();
		map.setOnInfoWindowClickListener(new OnMapInfoClickListener());
		map.setOnMapLoadedCallback(new MapLoadedListener());
		updateData();
	}

	public void updateData() {
		if (map != null) {
			map.clear();
			allMarkersMap.clear();
			addCurrentLocationToMap(lastLatitude, lastLongitude);
			updateFacilitiesOnMap();
		}
	}

	private void updateFacilitiesOnMap() {
		listOfFacilities.clear();
		listOfFacilities.addAll(context.getFacilityList());
		if (map != null) {
			for (FacilityWithDistance fac : listOfFacilities) {
				Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(fac.getGpsLatitude(), fac.getGpsLongitude())).title(fac.getName()));
				allMarkersMap.put(marker, fac);
			}
		}
	}

	public void setCurrentLocation(double latitude, double longitude) {
		lastLatitude = latitude;
		lastLongitude = longitude;
		addCurrentLocationToMap(latitude, longitude);
	}

	private void addCurrentLocationToMap(double latitude, double longitude) {
		if (map != null && latitude > 0 && longitude > 0) {
			LatLng pos = new LatLng(latitude, longitude);
			Marker homeMarker = map.addMarker(new MarkerOptions().position(pos).title("HOME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
			allMarkersMap.put(homeMarker, null);
		}
	}

	private class OnMapInfoClickListener implements OnInfoWindowClickListener {
		public void onInfoWindowClick(Marker arg0) {
			FacilityWithDistance facility = allMarkersMap.get(arg0);
			if (facility != null) {
				Intent facilityInfo = new Intent(context.getBaseContext(), FacilityInfoActivity.class);
				facilityInfo.putExtra(IntentExtras.EXTRA_FACILITY, facility);
				startActivity(facilityInfo);
			}
		}
	}

	private class MapLoadedListener implements GoogleMap.OnMapLoadedCallback {
		public void onMapLoaded() {
			LatLngBounds.Builder zoomBuilder = new LatLngBounds.Builder();
			for (Marker marker : allMarkersMap.keySet()) {
				zoomBuilder.include(marker.getPosition());
			}
			LatLngBounds zoomBounds = zoomBuilder.build();
			map.animateCamera(CameraUpdateFactory.newLatLngBounds(zoomBounds, 100));
		}
	}

}
