package ch.hsr.faith.android.app.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import ch.hsr.faith.android.app.R;

public class AddFacilityActivity extends BaseActivity {

	private AddOrEditFacilityFragment editFacilityInfoFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_facility);
		editFacilityInfoFragment = new AddOrEditFacilityFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, editFacilityInfoFragment);
		fragmentTransaction.commit();
	}

}
