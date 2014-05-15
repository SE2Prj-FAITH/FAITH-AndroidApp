package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import android.content.Intent;
import android.os.Bundle;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.domain.FacilityWithDistance;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilitiesMapFragment extends MapFragment {

	private GoogleMap map;
	private FacilitiesTabActivity context;
	private List<FacilityWithDistance> listOfFacilities;
	private Map<Marker, FacilityWithDistance> allMarkersMap;
	static final LatLng HSR = new LatLng(47.22332, 8.81728);
	private double currentLatitude;
	private double currentLongitude;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		this.context = (FacilitiesTabActivity) getActivity();
		super.onActivityCreated(savedInstanceState);
		map = getMap();
		listOfFacilities = new ArrayList<FacilityWithDistance>();
		listOfFacilities.addAll(context.getFacilityList());
		allMarkersMap = new HashMap<Marker, FacilityWithDistance>();

		LatLng pos = new LatLng(currentLatitude, currentLongitude);
		Marker homeMarker = map.addMarker(new MarkerOptions().position(pos).title("HOME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
		allMarkersMap.put(homeMarker, null);
		
		for (FacilityWithDistance fac : listOfFacilities) {
			Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(fac.getGpsLatitude(), fac.getGpsLongitude())).title(fac.getName()));
			allMarkersMap.put(marker, fac);
		}
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(9), 2000, null);

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker arg0) {
				FacilityWithDistance facility = allMarkersMap.get(arg0);
				if (facility != null){
					Intent facilityInfo = new Intent(context.getBaseContext(), FacilityInfoActivity.class);
					facilityInfo.putExtra(IntentExtras.EXTRA_FACILITY, facility);
					startActivity(facilityInfo);
				}
			}
		});

	}

	public void updateData() {
		// Toast.makeText(getActivity(), "update", Toast.LENGTH_LONG).show();
		Logger.getRootLogger().info("update");

	}

	public void setCurrentLocation(double latitude, double longitude) {
		this.currentLatitude = latitude;
		this.currentLongitude = longitude;
	}

}
