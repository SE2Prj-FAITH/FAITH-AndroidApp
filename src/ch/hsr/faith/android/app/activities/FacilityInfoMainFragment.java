package ch.hsr.faith.android.app.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.Facility;

public class FacilityInfoMainFragment extends Fragment {

	private FacilityInfoActivity context;

	private TextView facilityCategory;
	private TextView facilityName;
	private TextView facilityAddress;
	private TextView facilityCity;
	private TextView facilityWebsite;
	private TextView facilityPhone;
	private TextView facilityMail;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = (FacilityInfoActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_facility_info, container, false);

		facilityCategory = (TextView) view.findViewById(R.id.textViewFacilityCategory);
		facilityName = (TextView) view.findViewById(R.id.textViewFacilityName);
		facilityAddress = (TextView) view.findViewById(R.id.textViewFacilityAddress);
		facilityCity = (TextView) view.findViewById(R.id.textViewFacilityCity);
		facilityWebsite = (TextView) view.findViewById(R.id.textViewFacilityWebsite);
		facilityPhone = (TextView) view.findViewById(R.id.textViewFacilityPhone);
		facilityMail = (TextView) view.findViewById(R.id.textViewFacilityMails);

		updateData();
		return view;
	}

	public void updateData() {
		Facility displayedFacility = context.getFacility();

		facilityCategory.setText(displayedFacility.getFacilityCategory().getName().getText(LocaleUtil.getCurrentLocale()));
		facilityName.setText(displayedFacility.getName());
		facilityAddress.setText(displayedFacility.getStreet());
		facilityCity.setText(displayedFacility.getCity());
		facilityWebsite.setText(displayedFacility.getHomepage());
		facilityPhone.setText(displayedFacility.getPhone());
		facilityMail.setText(displayedFacility.getEmail());
	}

}
