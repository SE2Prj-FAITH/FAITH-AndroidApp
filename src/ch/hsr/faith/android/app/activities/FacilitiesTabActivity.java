package ch.hsr.faith.android.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.FacilitiesTabListener;
import ch.hsr.faith.domain.FacilityCategory;

public class FacilitiesTabActivity extends Activity {

	private ActionBar.Tab tabFacilitiesList;
	private ActionBar.Tab tabFacilitiesMap;
	private Fragment facilitiesListFragment;
	private Fragment facilitiesMapFragment;

	private FacilityCategory facilityCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_tabbed);
		facilityCategory = (FacilityCategory) getIntent().getExtras().get("facilityCategory");

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

}
