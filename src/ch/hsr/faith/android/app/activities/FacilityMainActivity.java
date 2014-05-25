package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
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
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FacilityCategoryList;
import ch.hsr.faith.android.app.services.request.FacilityCategoriesGetAllRequest;
import ch.hsr.faith.android.app.services.response.FacilityCategoryListResponse;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.FacilityCategory;

import com.octo.android.robospice.persistence.DurationInMillis;

public class FacilityMainActivity extends BaseActivity implements ActionBar.OnNavigationListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private String lastFacilityCategoriesRootRequestCacheKey;

	private ListView facilityCategoriesListView;
	private FacilityCategoryAdapter adapter;
	protected static final String EXTRA_FACILITY_CATEGORY = "ch.hsr.faith.android.app.activities.EXTRA_FACILITY_CATEGORY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facility_main);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
				getString(R.string.title_activity_facility_main), getString(R.string.title_activity_furniture_main), }), this);
		facilityCategoriesListView = (ListView) findViewById(R.id.facilityMain_ListView);
		adapter = new FacilityCategoryAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<FacilityCategory>());
		facilityCategoriesListView.setAdapter(adapter);
		facilityCategoriesListView.setOnItemClickListener(new OnFacilityCategoryClickedListener());
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	public boolean onNavigationItemSelected(int position, long id) {
		switch (position) {
		case 0:
			return true;
		case 1:
			onFurnitureSpinnerClick();
			return true;
		default:
			return true;
		}

	}

	private void onFurnitureSpinnerClick() {
		Intent intent = new Intent(this.getBaseContext(), FurnitureMainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadFacilityCategories();
	}

	private void loadFacilityCategories() {
		FacilityCategoriesGetAllRequest request = new FacilityCategoriesGetAllRequest();
		lastFacilityCategoriesRootRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFacilityCategoriesRootRequestCacheKey, DurationInMillis.ONE_MINUTE, new FacilityCategoriesListRequestListener(this));
	}

	private class FacilityCategoriesListRequestListener extends BaseRequestListener<FacilityCategoryListResponse, FacilityCategoryList> {
		public FacilityCategoriesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FacilityCategoryList data) {
			logger.debug("List of FacilityCategories successfully loaded");
			adapter.clear();
			for (FacilityCategory facilityCategory : data) {
				adapter.add(facilityCategory);
			}
			adapter.notifyDataSetChanged();
		}
	}

	private class FacilityCategoryAdapter extends ArrayAdapter<FacilityCategory> {

		public FacilityCategoryAdapter(Context context, int textViewResourceId, List<FacilityCategory> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FacilityCategory facilityCategory = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, null);
			}
			TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
			textView.setTextSize(17);
			textView.setText(facilityCategory.getName().getText(LocaleUtil.getCurrentLocale()));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			FacilityCategory category = super.getItem(position);
			return category.getId();
		}
	}

	private class OnFacilityCategoryClickedListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			FacilityCategory facilityCategory = (FacilityCategory) facilityCategoriesListView.getItemAtPosition(position);
			openFacilitiesList(facilityCategory);
		}

		private void openFacilitiesList(FacilityCategory facilityCategory) {
			Intent intent = new Intent(FacilityMainActivity.this, FacilitiesTabActivity.class);
			intent.putExtra(IntentExtras.EXTRA_FACILITY_CATEGORY, facilityCategory);
			startActivity(intent);
		}
	}

}
