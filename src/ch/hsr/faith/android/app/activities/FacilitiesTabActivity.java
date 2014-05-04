package ch.hsr.faith.android.app.activities;

import android.app.ActionBar;
import android.os.Bundle;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.activities.listeners.FacilitiesTabListener;
import ch.hsr.faith.android.app.dto.FacilityList;
import ch.hsr.faith.android.app.services.request.FacilitiesGetByCategoryRequest;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.domain.FacilityCategory;

import com.octo.android.robospice.persistence.DurationInMillis;

public class FacilitiesTabActivity extends BaseActivity {

	private ActionBar.Tab tabFacilitiesList;
	private ActionBar.Tab tabFacilitiesMap;
	private FacilitiesListFragment facilitiesListFragment;
	private FacilitiesMapFragment facilitiesMapFragment;

	private FacilityCategory facilityCategory;
	private FacilityList facilityList = new FacilityList();
	protected static final String EXTRA_FACILITY_CATEGORY = "ch.hsr.faith.android.app.activities.EXTRA_FACILITY_CATEGORY";

	private String facilitiesGetByCategoryRequestCacheKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_tabbed);
		facilityCategory = (FacilityCategory) getIntent().getExtras().get(EXTRA_FACILITY_CATEGORY);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		tabFacilitiesList = actionBar.newTab().setText(getString(R.string.activity_facilities_tabbed_tab_list));
		tabFacilitiesMap = actionBar.newTab().setText(getString(R.string.activity_facilities_tabbed_tab_map));
		facilitiesListFragment = new FacilitiesListFragment(this);
		facilitiesMapFragment = new FacilitiesMapFragment();

		tabFacilitiesList.setTabListener(new FacilitiesTabListener(facilitiesListFragment));
		tabFacilitiesMap.setTabListener(new FacilitiesTabListener(facilitiesMapFragment));

		actionBar.addTab(tabFacilitiesList);
		actionBar.addTab(tabFacilitiesMap);
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadFacilities();
	}

	private void loadFacilities() {
		if (facilityCategory != null) {
			FacilitiesGetByCategoryRequest request = new FacilitiesGetByCategoryRequest(facilityCategory);
			facilitiesGetByCategoryRequestCacheKey = request.createCacheKey();
			spiceManager.execute(request, facilitiesGetByCategoryRequestCacheKey, DurationInMillis.ONE_MINUTE, new FacilitiesListRequestListener(this));
		}
	}

	public FacilityList getFacilityList() {
		return facilityList;
	}
	
	private class FacilitiesListRequestListener extends BaseRequestListener<FacilityListResponse, FacilityList> {

		public FacilitiesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FacilityList data) {
			facilityList = data;
			FacilitiesTabActivity.this.facilitiesListFragment.updateData();
			FacilitiesTabActivity.this.facilitiesMapFragment.updateData();
		}
	}

}
