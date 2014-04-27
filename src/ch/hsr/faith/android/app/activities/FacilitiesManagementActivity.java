package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FacilityList;
import ch.hsr.faith.android.app.services.request.FacilitiesGetByUserAccountRequest;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.domain.Facility;

import com.octo.android.robospice.persistence.DurationInMillis;

public class FacilitiesManagementActivity extends BaseActivity {
	private String lastFacilitiesGetByCategoryRequestCacheKey;

	ArrayList<String> listData;
	private FacilityAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_management);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		loadFacilityList();
	}
	
	private void loadFacilityList() {
		FacilitiesGetByUserAccountRequest request = new FacilitiesGetByUserAccountRequest(getUserAccount());
		lastFacilitiesGetByCategoryRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request,
				lastFacilitiesGetByCategoryRequestCacheKey,
				DurationInMillis.ONE_MINUTE,
				new FacilitiesListRequestListener(this));
	}

	private class FacilitiesListRequestListener extends BaseRequestListener<FacilityListResponse, FacilityList> {

		public FacilitiesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FacilityList data) {
			for (Facility facility : data) {
				adapter.add(facility);
			}
			adapter.notifyDataSetChanged();
		}
		
		@Override
		protected void handleAuthenticationFailure() {
			Intent intent = new Intent(FacilitiesManagementActivity.this.getBaseContext(), LoginUserAccountActivity.class);
			startActivity(intent);
		}
	}
	
	private class FacilityAdapter extends ArrayAdapter<Facility> {

		public FacilityAdapter(Context context, int textViewResourceId, List<Facility> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Facility facility = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, null);
			}
			TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
			textView.setText(facility.getName());
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			Facility facility = super.getItem(position);
			return facility.getId();
		}
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_facilities_management, container, false);
			return rootView;
		}
	}

}
