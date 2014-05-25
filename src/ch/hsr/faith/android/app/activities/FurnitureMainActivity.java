package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.adapters.PieceOfFurnituresAdapter;
import ch.hsr.faith.android.app.activities.components.FurnitureCategoryListItem;
import ch.hsr.faith.android.app.activities.components.FurnitureListHandler;
import ch.hsr.faith.android.app.activities.components.GotoParentCategoryListItem;
import ch.hsr.faith.android.app.activities.components.PieceOfFurnitureListItem;
import ch.hsr.faith.android.app.activities.components.SelectPieceOfFurnitureListItem;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.logging.Log4JConfigurator;
import ch.hsr.faith.android.app.util.PropertyReader;
import ch.hsr.faith.domain.PieceOfFurniture;

public class FurnitureMainActivity extends BaseActivity implements ActionBar.OnNavigationListener {

	private ListView listView;
	private PieceOfFurnituresAdapter popupListAdapter;
	private FurnitureListHandler furnitureListHandler;

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onStart() {
		super.onStart();
		loadSystemProperties();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_furniture_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
				getString(R.string.title_activity_furniture_main), getString(R.string.title_activity_facility_main), }), this);

		listView = (ListView) findViewById(R.id.furnitureMainListView);
		popupListAdapter = new PieceOfFurnituresAdapter(this, R.layout.select_piece_of_furniture_popup_listview_item, new ArrayList<SelectPieceOfFurnitureListItem>());
		listView.setAdapter(popupListAdapter);
		listView.setOnItemClickListener(new OnSelectPieceOfFurnitureListItemClickedListener());
		furnitureListHandler = new FurnitureListHandler(this, spiceManager, popupListAdapter);
		furnitureListHandler.loadRootCategories();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
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
			onFacilitySpinnerClick();
			return true;
		default:
			return true;
		}
	}

	private void onFacilitySpinnerClick() {
		Intent intent = new Intent(this.getBaseContext(), FacilityMainActivity.class);
		startActivity(intent);
		finish();
	}

	private class OnSelectPieceOfFurnitureListItemClickedListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			SelectPieceOfFurnitureListItem item = (SelectPieceOfFurnitureListItem) listView.getItemAtPosition(position);
			if (item instanceof FurnitureCategoryListItem) {
				FurnitureCategoryListItem furnitureCategoryListItem = (FurnitureCategoryListItem) item;
				furnitureListHandler.loadSubCategory(furnitureCategoryListItem.getFurnitureCategory());
			} else if (item instanceof PieceOfFurnitureListItem) {
				PieceOfFurnitureListItem pieceOfFurnitureListItem = (PieceOfFurnitureListItem) item;
				openFacilitiesList(pieceOfFurnitureListItem.getPieceOfFurniture());
			} else if (item instanceof GotoParentCategoryListItem) {
				GotoParentCategoryListItem gotoParentCategoryListItem = (GotoParentCategoryListItem) item;
				if (gotoParentCategoryListItem.getFurnitureCategory() == null) {
					furnitureListHandler.loadRootCategories();
				} else {
					furnitureListHandler.loadSubCategory(gotoParentCategoryListItem.getFurnitureCategory());
				}
			}
		}

		private void openFacilitiesList(PieceOfFurniture pieceOfFurniture) {
			Intent intent = new Intent(FurnitureMainActivity.this, FacilitiesTabActivity.class);
			intent.putExtra(IntentExtras.EXTRA_PIECE_OF_FURNITURE, pieceOfFurniture);
			startActivity(intent);
		}
	}

	private void loadSystemProperties() {
		PropertyReader.initProperties(this);
		Log4JConfigurator.configure();
	}

}
