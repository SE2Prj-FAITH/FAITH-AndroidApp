package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.adapters.ExpandableListAdapter;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FurnitureCategoryList;
import ch.hsr.faith.android.app.dto.FurnitureCategoryListResponse;
import ch.hsr.faith.android.app.services.FurnitureCategoriesRootRequest;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.android.app.util.PropertyReader;
import ch.hsr.faith.domain.FurnitureCategory;

import com.octo.android.robospice.persistence.DurationInMillis;

public class MainActivity extends BaseActivity {

	private String lastFurnitureCategoriesRootRequestCacheKey;

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

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		loadFurnitureCategories();

		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);
	}


	/**
	 * Laedt alle Hauptkategorien (Furniture-Categories)
	 */
	private void loadFurnitureCategories() {
		MainActivity.this.setProgressBarIndeterminateVisibility(true);
		FurnitureCategoriesRootRequest request = new FurnitureCategoriesRootRequest();
		lastFurnitureCategoriesRootRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFurnitureCategoriesRootRequestCacheKey, DurationInMillis.ONE_MINUTE, new FurnitureCategoriesListRequestListener(this));
	}

	private class FurnitureCategoriesListRequestListener extends BaseRequestListener<FurnitureCategoryListResponse, FurnitureCategoryList> {

		public FurnitureCategoriesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FurnitureCategoryList data) {
			for (FurnitureCategory s : data) {
				listDataHeader.add(s.getName().getText(LocaleUtil.getCurrentLocale()));
			}
			listAdapter.notifyDataSetChanged();
		}
	}

	private void loadSystemProperties() {
		PropertyReader.initProperties(this);
	}

}
