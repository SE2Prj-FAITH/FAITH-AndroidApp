package ch.hsr.faith.android.app.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditFacilityInfoFragment extends Fragment {

	private EditFacilityActivity context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = (EditFacilityActivity) getActivity();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void updateData() {
	}

}
