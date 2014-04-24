package ch.hsr.faith.android.app.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import ch.hsr.faith.android.app.R;

public class FacilityMainActivity extends BaseActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facility_main);

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
										getString(R.string.title_activity_facility_main),
										getString(R.string.title_activity_furniture_main), }),
						this);

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
			onFurnitureSpinnerClick();
			return true;
		default:
			return true;
		}

	}

	private void onFurnitureSpinnerClick() {
		Intent intent = new Intent(this.getBaseContext(),
				MainActivity.class);
		startActivity(intent);
		//calling finish() on an activity, the method onDestroy() is executed
		finish();
	}

	

}
