package ch.hsr.faith.android.app.activities.listeners;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import ch.hsr.faith.android.app.R;

public class FacilitiesTabListener implements ActionBar.TabListener {

	private Fragment fragment;

	public FacilitiesTabListener(Fragment fragment) {
		this.fragment = fragment;
	}

	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		fragmentTransaction.replace(R.id.fragment_container, fragment);
	}

	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		fragmentTransaction.remove(fragment);
	}

	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// do nothing
	}
	
}
