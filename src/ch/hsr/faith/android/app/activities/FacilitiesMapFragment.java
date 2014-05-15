package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import android.os.Bundle;
import ch.hsr.faith.domain.FacilityWithDistance;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilitiesMapFragment extends MapFragment {

	private GoogleMap map;
	private FacilitiesTabActivity context;
	private List<FacilityWithDistance> listOfFacilities;

	static final LatLng HSR = new LatLng(47.22332, 8.81728);

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.context = (FacilitiesTabActivity) getActivity();
		map = getMap();
		listOfFacilities = new ArrayList<FacilityWithDistance>();
		listOfFacilities.addAll(context.getFacilityList());
		updateData();

		map.addMarker(new MarkerOptions().position(HSR).title("HSR"));
		map.addMarker(new MarkerOptions()
	    .position(HSR)
	    .title("HSR")
	    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
		
		//getting null for latlong from db - correction tomorrow
		map.addMarker(new MarkerOptions().position(new LatLng(47.5226, 7.61898)).title("Wohngruppen für Behinderte Kinder"));
		map.addMarker(new MarkerOptions().position(new LatLng(46.8769, 9.53978)).title("Therapiehaus Fürstenwald"));
		map.addMarker(new MarkerOptions().position(new LatLng(47.1679, 9.51575)).title("Heilpädagogisches Zentrum des Fürstentums Liechtenstein"));
		map.addMarker(new MarkerOptions().position(new LatLng(47.1842, 8.53543)).title("ZUWEBE"));
		map.addMarker(new MarkerOptions().position(new LatLng(47.1988, 7.53868)).title("Stiftung Schulheim für körperbehinderte Kinder"));
		// for (FacilityWithDistance fac : listOfFacilities) {
		// map.addMarker(new MarkerOptions().position(new
		// LatLng(fac.getGpsLatitude(),
		// fac.getGpsLongitude())).title(fac.getName()));
		// }

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HSR, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(9), 2000, null);

//		map.setOnMarkerClickListener(new OnMarkerClickListener() {
//			//correction tomorrow
//			public boolean onMarkerClick(Marker arg0) {
//				
////				Intent facilityInfo = new Intent(context.getBaseContext(), FacilityInfoActivity.class);
////				// hier muss noch implementiert werden, das die angeklickte
////				// facility deren profil aufgerufen wird, und nicht wie jetzt
////				// eine wilkürliche
////				facilityInfo.putExtra(IntentExtras.EXTRA_FACILITY, listOfFacilities.get(0));
////				startActivity(facilityInfo);
//				return false;
//			}
//		});

	}

	public void updateData() {
		// Toast.makeText(getActivity(), "update", Toast.LENGTH_LONG).show();
		Logger.getRootLogger().info("update");

	}

}
