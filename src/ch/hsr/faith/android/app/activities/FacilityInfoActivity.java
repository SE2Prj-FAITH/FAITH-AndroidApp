package ch.hsr.faith.android.app.activities;

import android.app.ActionBar;
import android.os.Bundle;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.activities.listeners.FacilitiesTabListener;
import ch.hsr.faith.android.app.dto.ItemNeededList;
import ch.hsr.faith.android.app.services.request.ItemsNeededGetByFacilityRequest;
import ch.hsr.faith.android.app.services.response.ItemNeededListResponse;
import ch.hsr.faith.domain.Facility;

import com.octo.android.robospice.persistence.DurationInMillis;

public class FacilityInfoActivity extends BaseActivity {

	private ActionBar.Tab tabInfo;
	private ActionBar.Tab tabItemsNeeded;

	private FacilityInfoMainFragment facilityInfoMainFragment;
	private FacilityInfoItemsNeededFragment facilityInfoItemsNeededFragment;

	private Facility displayedFacility;
	private ItemNeededList itemsNeededList;

	private String itemsNeededGetByFacilityRequestCacheKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facility_info);
		displayedFacility = (Facility) getIntent().getExtras().get(IntentExtras.EXTRA_FACILITY);
		this.setTitle(displayedFacility.getName());

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		tabInfo = actionBar.newTab().setText(getString(R.string.activity_facility_info_tab_main));
		tabItemsNeeded = actionBar.newTab().setText(getString(R.string.activity_facility_info_tab_items_needed));
		facilityInfoMainFragment = new FacilityInfoMainFragment();
		facilityInfoItemsNeededFragment = new FacilityInfoItemsNeededFragment();

		tabInfo.setTabListener(new FacilitiesTabListener(facilityInfoMainFragment));
		tabItemsNeeded.setTabListener(new FacilitiesTabListener(facilityInfoItemsNeededFragment));

		actionBar.addTab(tabInfo);
		actionBar.addTab(tabItemsNeeded);
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadItemsNeeded();
	}

	private void loadItemsNeeded() {
		if (displayedFacility != null) {
			ItemsNeededGetByFacilityRequest request = new ItemsNeededGetByFacilityRequest(getLoginObject(), displayedFacility);
			itemsNeededGetByFacilityRequestCacheKey = request.createCacheKey();
			spiceManager.execute(request, itemsNeededGetByFacilityRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new ItemNeededListRequestListener(this));
		}
	}

	public Facility getFacility() {
		return displayedFacility;
	}

	public ItemNeededList getItemsNeeded() {
		return itemsNeededList;
	}

	private class ItemNeededListRequestListener extends BaseRequestListener<ItemNeededListResponse, ItemNeededList> {
		public ItemNeededListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(ItemNeededList data) {
			itemsNeededList = data;
			facilityInfoMainFragment.updateData();
			facilityInfoItemsNeededFragment.updateData();
		}
	}

}
