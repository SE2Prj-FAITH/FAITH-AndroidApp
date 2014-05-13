package ch.hsr.faith.android.app.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.activities.listeners.FacilitiesTabListener;
import ch.hsr.faith.android.app.dto.ItemNeededList;
import ch.hsr.faith.android.app.services.request.ItemsNeededGetByFacilityRequest;
import ch.hsr.faith.android.app.services.response.ItemNeededListResponse;
import ch.hsr.faith.domain.Facility;

import com.octo.android.robospice.persistence.DurationInMillis;

public class EditFacilityActivity extends BaseActivity {

	private ActionBar.Tab tabFacilityInfo;
	private ActionBar.Tab tabFacilityItemsNeeded;
	private AddOrEditFacilityFragment editFacilityInfoFragment;
	private EditFacilityItemsNeededFragment editFacilityItemsNeededFragment;

	private Facility facility;
	private ItemNeededList itemNeededList;

	private String itemsNeededGetByFacilityRequestCacheKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_tabbed);
		facility = (Facility) getIntent().getExtras().get(IntentExtras.EXTRA_FACILITY);
		this.setTitle(facility.getName());

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		tabFacilityInfo = actionBar.newTab().setText(getString(R.string.edit_facility_tab_info));
		tabFacilityItemsNeeded = actionBar.newTab().setText(getString(R.string.edit_facility_tab_inventory));
		editFacilityInfoFragment = new AddOrEditFacilityFragment();
		editFacilityItemsNeededFragment = new EditFacilityItemsNeededFragment();

		tabFacilityInfo.setTabListener(new FacilitiesTabListener(editFacilityInfoFragment));
		tabFacilityItemsNeeded.setTabListener(new FacilitiesTabListener(editFacilityItemsNeededFragment));

		actionBar.addTab(tabFacilityInfo);
		actionBar.addTab(tabFacilityItemsNeeded);
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadItemsNeeded();
	}

	public void addItemNeededClicked(View view) {
		Intent intent = new Intent(EditFacilityActivity.this, AddOrEditItemNeededActivity.class);
		intent.putExtra(IntentExtras.EXTRA_FACILITY, facility);
		startActivity(intent);
	}

	private void loadItemsNeeded() {
		if (facility != null) {
			ItemsNeededGetByFacilityRequest request = new ItemsNeededGetByFacilityRequest(getLoginObject(), facility);
			itemsNeededGetByFacilityRequestCacheKey = request.createCacheKey();
			spiceManager.execute(request, itemsNeededGetByFacilityRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new ItemNeededListRequestListener(this));
		}
	}

	public ItemNeededList getItemNeededList() {
		return itemNeededList;
	}

	private class ItemNeededListRequestListener extends BaseRequestListener<ItemNeededListResponse, ItemNeededList> {
		public ItemNeededListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(ItemNeededList data) {
			itemNeededList = data;
			EditFacilityActivity.this.editFacilityInfoFragment.updateData();
			EditFacilityActivity.this.editFacilityItemsNeededFragment.updateData();
		}
	}

}
