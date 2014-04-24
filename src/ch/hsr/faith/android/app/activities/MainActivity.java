package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.adapters.ExpandableListAdapter;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FurnitureCategoryList;
import ch.hsr.faith.android.app.services.request.FurnitureCategoriesRootRequest;
import ch.hsr.faith.android.app.services.response.FurnitureCategoryListResponse;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.android.app.util.PropertyReader;
import ch.hsr.faith.domain.FurnitureCategory;

import com.octo.android.robospice.persistence.DurationInMillis;

public class MainActivity extends BaseActivity implements
		ActionBar.OnNavigationListener {

	private String lastFurnitureCategoriesRootRequestCacheKey;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	ArrayList<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onStart() {
		super.onStart();
		loadSystemProperties();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(false);

		// Set up the dropdown list navigation in the action bar.
		actionBar
				.setListNavigationCallbacks(
						// Specify a SpinnerAdapter to populate the dropdown
						// list.
						new ArrayAdapter<String>(
								actionBar.getThemedContext(),
								android.R.layout.simple_list_item_1,
								android.R.id.text1,
								new String[] {
									getString(R.string.title_activity_furniture_main),
										getString(R.string.title_activity_facility_main), }),
						this);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		loadFurnitureCategories();

		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);
	}

	/**
	 * folgende Methoden onRestoreInstanceState und onSaveInstanceState könnten
	 * eventuell noch gebraucht werden, sind allerdings bisher ohne funktion in
	 * unserer app
	 */

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its activity in the
		// container view.
		switch (position) {
		case 0:
			return true;
		case 1:
			onFacilitySpinnerClick();
			return true;
		default:
			return true;
		}

	}

	private void onFacilitySpinnerClick() {
		Intent intent = new Intent(this.getBaseContext(),
				FacilityMainActivity.class);
		startActivity(intent);
		//calling finish() on an activity, the method onDestroy() is executed
		finish();
	}

	/**
	 * Laedt alle Hauptkategorien (Furniture-Categories)
	 */
	private void loadFurnitureCategories() {
		MainActivity.this.setProgressBarIndeterminateVisibility(true);
		FurnitureCategoriesRootRequest request = new FurnitureCategoriesRootRequest();
		lastFurnitureCategoriesRootRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request,
				lastFurnitureCategoriesRootRequestCacheKey,
				DurationInMillis.ONE_MINUTE,
				new FurnitureCategoriesListRequestListener(this));
	}

	private class FurnitureCategoriesListRequestListener
			extends
			BaseRequestListener<FurnitureCategoryListResponse, FurnitureCategoryList> {

		public FurnitureCategoriesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FurnitureCategoryList data) {
			for (FurnitureCategory s : data) {
				listDataHeader.add(s.getName().getText(
						LocaleUtil.getCurrentLocale()));
			}
			listAdapter.notifyDataSetChanged();
		}
	}

	private void loadSystemProperties() {
		PropertyReader.initProperties(this);
	}

}
