package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.adapters.ExpandableListAdapter;
import ch.hsr.faith.android.app.domain.FurnitureCategory;
import ch.hsr.faith.android.app.domain.FurnitureCategoryList;
import ch.hsr.faith.android.app.services.FurnitureCategoriesRootRequest;
import ch.hsr.faith.android.app.services.JSONSpiceService;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends Activity {

	protected SpiceManager spiceManager = new SpiceManager(JSONSpiceService.class);
	private TextView testServiceResponseTextView;
	private String lastFurnitureCategoriesRootRequestCacheKey;

	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		testServiceResponseTextView = (TextView) findViewById(R.id.TestServiceResponseView);

		// get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		loadFurnitureCategories();
		
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
	

        // setting list adapter
        expListView.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Laedt alle Hauptkategorien (Furniture-Categories)
	 */
	private void loadFurnitureCategories() {
		MainActivity.this.setProgressBarIndeterminateVisibility(true);
		FurnitureCategoriesRootRequest request = new FurnitureCategoriesRootRequest();
		lastFurnitureCategoriesRootRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFurnitureCategoriesRootRequestCacheKey, DurationInMillis.ONE_MINUTE, new FurnitureCategoriesListRequestListener());
	}

	private class FurnitureCategoriesListRequestListener implements RequestListener<FurnitureCategoryList> {

		public void onRequestFailure(SpiceException spiceException) {
			testServiceResponseTextView.setText("Error loading data: " + spiceException.getLocalizedMessage());
		}

		public void onRequestSuccess(FurnitureCategoryList furnitureCategoryList) {
//			testServiceResponseTextView.setText("\n\nCategories:\n");
//			for (FurnitureCategory furnitureCategory : furnitureCategoryList) {
//				testServiceResponseTextView.append(furnitureCategory.getName() + "\n");
//			}
//			FurnitureCategoryList categories = request.loadDataFromNetwork();
			
	    	for (FurnitureCategory s : furnitureCategoryList){
	    		listDataHeader.add(s.getName());
	    	}
	    	listAdapter.notifyDataSetChanged();
		}

	}
    

}
