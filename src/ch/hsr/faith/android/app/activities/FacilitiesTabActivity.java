package ch.hsr.faith.android.app.activities;

import android.app.ActionBar;
import android.os.Bundle;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.activities.listeners.FacilitiesTabListener;
import ch.hsr.faith.android.app.dto.FacilityWithDistanceList;
import ch.hsr.faith.android.app.services.request.FacilitiesWithDistanceGetByCategoryRequest;
import ch.hsr.faith.android.app.services.request.FacilitiesWithDistanceGetByPieceOfItemsNeededRequest;
import ch.hsr.faith.android.app.services.response.FacilityWithDistanceListResponse;
import ch.hsr.faith.domain.FacilityCategory;
import ch.hsr.faith.domain.PieceOfFurniture;

import com.octo.android.robospice.persistence.DurationInMillis;

public class FacilitiesTabActivity extends BaseActivity {

	private ActionBar.Tab tabFacilitiesList;
	private ActionBar.Tab tabFacilitiesMap;
	private FacilitiesListFragment facilitiesListFragment;
	private FacilitiesMapFragment facilitiesMapFragment;

	private FacilityCategory facilityCategory;
	private PieceOfFurniture pieceOfFurniture;
	private FacilityWithDistanceList facilityList = new FacilityWithDistanceList();

	private String facilitiesGetByCategoryRequestCacheKey;
	private String facilitiesGetByPieceOfItemsNeededRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_tabbed);
		facilityCategory = (FacilityCategory) getIntent().getExtras().get(IntentExtras.EXTRA_FACILITY_CATEGORY);
		pieceOfFurniture = (PieceOfFurniture) getIntent().getExtras().get(IntentExtras.EXTRA_PIECE_OF_FURNITURE);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		tabFacilitiesList = actionBar.newTab().setText(getString(R.string.activity_facilities_tabbed_tab_list));
		tabFacilitiesMap = actionBar.newTab().setText(getString(R.string.activity_facilities_tabbed_tab_map));
		facilitiesListFragment = new FacilitiesListFragment();
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
		// TODO: get current or saved location (now fixed location HSR is
		// used)
		double latitude = 47.22332;
		double longitude = 8.81728;
		if (facilityCategory != null) {
			FacilitiesWithDistanceGetByCategoryRequest request = new FacilitiesWithDistanceGetByCategoryRequest(facilityCategory, latitude, longitude);
			spiceManager.execute(request, facilitiesGetByCategoryRequestCacheKey, DurationInMillis.ONE_MINUTE, new FacilitiesListRequestListener(this));
		} else if (pieceOfFurniture != null) {
			FacilitiesWithDistanceGetByPieceOfItemsNeededRequest request = new FacilitiesWithDistanceGetByPieceOfItemsNeededRequest(pieceOfFurniture, latitude, longitude);
			facilitiesGetByPieceOfItemsNeededRequest = request.createCacheKey();
			spiceManager.execute(request, facilitiesGetByPieceOfItemsNeededRequest, DurationInMillis.ONE_MINUTE, new FacilitiesListRequestListener(this));
		}
	}

	public FacilityWithDistanceList getFacilityList() {
		return facilityList;
	}

	private class FacilitiesListRequestListener extends BaseRequestListener<FacilityWithDistanceListResponse, FacilityWithDistanceList> {

		public FacilitiesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FacilityWithDistanceList data) {
			facilityList = data;
			FacilitiesTabActivity.this.facilitiesListFragment.updateData();
			FacilitiesTabActivity.this.facilitiesMapFragment.updateData();
		}
	}

}
