package ch.hsr.faith.android.app.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.FacilitiesTabListener;
import ch.hsr.faith.domain.Facility;

public class EditFacilityActivity extends BaseActivity {

	private ActionBar.Tab tabFacilityInfo;
	private ActionBar.Tab tabFacilityItemsNeeded;
	private EditFacilityInfoFragment editFacilityInfoFragment;
	private EditFacilityItemsNeededFragment editFacilityItemsNeededFragment;

	private Facility facility;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_tabbed);
		facility = (Facility) getIntent().getExtras().get("facility");
		this.setTitle(facility.getName());

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		tabFacilityInfo = actionBar.newTab().setText(getString(R.string.edit_facility_tab_info));
		tabFacilityItemsNeeded = actionBar.newTab().setText(getString(R.string.edit_facility_tab_inventory));
		editFacilityInfoFragment = new EditFacilityInfoFragment(this);
		editFacilityItemsNeededFragment = new EditFacilityItemsNeededFragment(this);

		tabFacilityInfo.setTabListener(new FacilitiesTabListener(editFacilityInfoFragment));
		tabFacilityItemsNeeded.setTabListener(new FacilitiesTabListener(editFacilityItemsNeededFragment));

		actionBar.addTab(tabFacilityInfo);
		actionBar.addTab(tabFacilityItemsNeeded);
	}
	
	public void addItemNeededClicked(View view) {
		startActivity(AddItemNeededActivity.class);
	}

}
