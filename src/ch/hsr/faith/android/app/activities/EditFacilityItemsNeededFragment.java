package ch.hsr.faith.android.app.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.hsr.faith.android.app.R;

public class EditFacilityItemsNeededFragment extends Fragment {

	private EditFacilityActivity context;

	public EditFacilityItemsNeededFragment() {
		this.context = (EditFacilityActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_facility_itemsneeded, container, false);
		return view;
	}

}
