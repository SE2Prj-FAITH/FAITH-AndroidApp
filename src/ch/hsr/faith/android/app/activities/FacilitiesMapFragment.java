package ch.hsr.faith.android.app.activities;

import org.apache.log4j.Logger;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilitiesMapFragment extends MapFragment {

	private GoogleMap map;

	static final LatLng HSR = new LatLng(47.22332, 8.81728);

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		map = getMap();
		map.addMarker(new MarkerOptions().position(HSR).title("HSR"));

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HSR, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}

	public void updateData() {
		// Toast.makeText(getActivity(), "update", Toast.LENGTH_LONG).show();
		Logger.getRootLogger().info("update");
	}

}
