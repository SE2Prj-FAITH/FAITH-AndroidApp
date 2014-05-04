package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FacilityList;
import ch.hsr.faith.android.app.services.request.FacilitiesGetByLoggedInUserRequest;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.domain.Facility;

import com.octo.android.robospice.persistence.DurationInMillis;

public class FacilitiesManagementActivity extends BaseActivity {
	Logger logger = Logger.getRootLogger();
	private String lastFacilitiesGetByCategoryRequestCacheKey;

	ArrayList<String> listData;
	private ListView facilityListView;
	private FacilityAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_management);

		facilityListView = (ListView) findViewById(R.id.facilitiesManagement_ListView);
		adapter = new FacilityAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<Facility>());
		facilityListView.setAdapter(adapter);
		facilityListView.setOnItemClickListener(new OnFacilityClickedListener());
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadFacilityList();
	}

	private void loadFacilityList() {
		FacilitiesGetByLoggedInUserRequest request = new FacilitiesGetByLoggedInUserRequest(getLoginObject());
		lastFacilitiesGetByCategoryRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFacilitiesGetByCategoryRequestCacheKey, DurationInMillis.ONE_MINUTE, new FacilitiesListRequestListener(this));
	}

	private class FacilitiesListRequestListener extends BaseRequestListener<FacilityListResponse, FacilityList> {

		public FacilitiesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FacilityList data) {
			logger.debug("success part");
			for (Facility facility : data) {
				adapter.add(facility);
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void handleAuthenticationFailure() {
			Intent intent = new Intent(baseActivity, LoginUserAccountActivity.class);
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

	private class OnFacilityClickedListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			Facility facility = (Facility) facilityListView.getItemAtPosition(position);
			openFacilitySettings(facility);
		}

		private void openFacilitySettings(Facility facility) {
			Intent intent = new Intent(FacilitiesManagementActivity.this, EditFacilityActivity.class);
			intent.putExtra("facility", facility);
			startActivity(intent);
		}
	}
}
