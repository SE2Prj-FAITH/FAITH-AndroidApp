package ch.hsr.faith.android.app.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.Facility;

public class FacilityInfoActivity extends BaseActivity {

	Facility displayedFacility;

	TextView FacilityCategory;
	TextView FacilityName;
	TextView FacilityAddress;
	TextView FacilityCity;
	TextView FacilityWebsite;
	TextView FacilityPhone;
	TextView FacilityMail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facility_info);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onStart() { 
		super.onStart();
		
		displayedFacility = (Facility) getIntent().getExtras().get(IntentExtras.EXTRA_FACILITY);
		this.setTitle(displayedFacility.getName());
		
		FacilityCategory = (TextView) findViewById(R.id.textViewFacilityCategory);
		FacilityName = (TextView) findViewById(R.id.textViewFacilityName);
		FacilityAddress = (TextView) findViewById(R.id.textViewFacilityAddress);
		FacilityCity = (TextView) findViewById(R.id.textViewFacilityCity);
		FacilityWebsite = (TextView) findViewById(R.id.textViewFacilityWebsite);
		FacilityPhone = (TextView) findViewById(R.id.textViewFacilityPhone);
		FacilityMail = (TextView) findViewById(R.id.textViewFacilityMails);

		FacilityCategory.setText(displayedFacility.getFacilityCategory().getName().getText(LocaleUtil.getCurrentLocale()));
		FacilityName.setText(displayedFacility.getName());
		FacilityAddress.setText(displayedFacility.getStreet());
		FacilityCity.setText(displayedFacility.getCity());
		FacilityWebsite.setText(displayedFacility.getHomepage());
		FacilityPhone.setText(displayedFacility.getPhone());
		FacilityMail.setText(displayedFacility.getEmail());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facility_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_facility_info, container, false);
			return rootView;
		}
	}

}
