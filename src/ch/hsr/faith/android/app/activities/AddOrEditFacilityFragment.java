package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FacilityCategoryList;
import ch.hsr.faith.android.app.services.request.AddOrUpdateFacilityRequest;
import ch.hsr.faith.android.app.services.request.FacilityCategoriesGetAllRequest;
import ch.hsr.faith.android.app.services.response.FacilityCategoryListResponse;
import ch.hsr.faith.android.app.services.response.FacilityResponse;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.Facility;
import ch.hsr.faith.domain.FacilityCategory;

import com.octo.android.robospice.persistence.DurationInMillis;

public class AddOrEditFacilityFragment extends Fragment {

	private BaseActivity context;
	private TextView failuresTextView;
	
	private String addFacilityRequestCacheKey;
	private String lastFacilityCategoriesGetAllRequestCacheKey;
	private FacilityCategoryAdapter categoryAdapter;

	private EditText nameField;
	private EditText streetField;
	private EditText zipField;
	private EditText cityField;
	private EditText homepageField;
	private EditText phoneField;
	private EditText emailField;
	private Spinner facilityCategorySpinner;
	private Spinner countrySpinner;
	
	private Facility facility;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		facility = (Facility) context.getIntent().getExtras().get(IntentExtras.EXTRA_FACILITY);
		Button button = (Button) context.findViewById(R.id.button_add_or_edit);
		if (facility == null) {
			this.context = (AddFacilityActivity) getActivity();
			button.setText(R.string.label_button_add_facility);
		} else {
			this.context = (EditFacilityActivity) getActivity();
			button.setText(R.string.label_button_edit_facility);
		}
		View view = inflater.inflate(R.layout.fragment_add_or_edit_facility, container, false);
		
		facilityCategorySpinner = (Spinner) view.findViewById(R.id.select_facility_category);
		categoryAdapter = new FacilityCategoryAdapter(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<FacilityCategory>());
		facilityCategorySpinner.setAdapter(categoryAdapter);
		loadFacilityCategoryList();

		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		failuresTextView = (TextView) context.findViewById(R.id.add_facility_failures);
		nameField = (EditText) context.findViewById(R.id.edit_facility_name);
		streetField = (EditText) context.findViewById(R.id.edit_facility_street);
		zipField = (EditText) context.findViewById(R.id.edit_facility_zip);
		cityField = (EditText) context.findViewById(R.id.edit_facility_city);
		homepageField = (EditText) context.findViewById(R.id.edit_facility_homepage);
		phoneField = (EditText) context.findViewById(R.id.edit_facility_phone);
		emailField = (EditText) context.findViewById(R.id.edit_facility_email);
		countrySpinner = (Spinner) context.findViewById(R.id.select_facility_country);
		if (context instanceof EditFacilityActivity)
			initGuiForEditFacility();
	}
	
	private void initGuiForEditFacility() {
		nameField.setText(facility.getName());
		streetField.setText(facility.getStreet());
		zipField.setText(facility.getZip());
		cityField.setText(facility.getCity());
		homepageField.setText(facility.getHomepage());
		phoneField.setText(facility.getPhone());
		emailField.setText(facility.getEmail());
		facilityCategorySpinner.setSelection(categoryAdapter.getPosition(facility.getFacilityCategory()));
		ArrayAdapter<String> countryAdapter = (ArrayAdapter<String>) countrySpinner.getAdapter();
		countrySpinner.setSelection(countryAdapter.getPosition(facility.getCountry()));
	}

	public void updateData() {
	}
	
	private void loadFacilityCategoryList() {
		FacilityCategoriesGetAllRequest request = new FacilityCategoriesGetAllRequest();
		lastFacilityCategoriesGetAllRequestCacheKey = request.createCacheKey();
		context.spiceManager.execute(request, lastFacilityCategoriesGetAllRequestCacheKey, DurationInMillis.ONE_MINUTE, new FacilityCategoriesListRequestListener(context));
	}

	public void buttonClicked(View view) {
		cleanFailuresView();
		if (isInputValid()) {
			Facility facility = new Facility();
			facility.setName(nameField.getText().toString());
			facility.setStreet(streetField.getText().toString());
			facility.setZip(zipField.getText().toString());
			facility.setCity(cityField.getText().toString());
			facility.setHomepage(homepageField.getText().toString());
			facility.setPhone(phoneField.getText().toString());
			facility.setEmail(emailField.getText().toString());
			facility.setFacilityCategory((FacilityCategory) facilityCategorySpinner.getSelectedItem());
			facility.setCountry(countrySpinner.getSelectedItem().toString());
			facility.setLevel(0);
			
			AddOrUpdateFacilityRequest request = new AddOrUpdateFacilityRequest(context.getLoginObject(), facility);
			addFacilityRequestCacheKey = request.createCacheKey();
			context.spiceManager.execute(request, addFacilityRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new FacilityRequestListener(context));
		}
	}

	private boolean isInputValid() {
		if ("".equals(nameField.getText().toString())) {
			nameField.setError(getString(R.string.add_facility_error_name_empty));
			return false;
		}
		if ("".equals(streetField.getText().toString())) {
			nameField.setError(getString(R.string.add_facility_error_street_empty));
			return false;
		}
		if ("".equals(zipField.getText().toString())) {
			nameField.setError(getString(R.string.add_facility_error_zip_empty));
			return false;
		}
		if ("".equals(cityField.getText().toString())) {
			nameField.setError(getString(R.string.add_facility_error_city_empty));
			return false;
		}
		if ("".equals(emailField.getText().toString())) {
			nameField.setError(getString(R.string.add_facility_error_email_empty));
			return false;
		}
		if (countrySpinner.getSelectedItem() == null) {
			failuresTextView.setText(getString(R.string.add_facility_error_no_country_selected));
			failuresTextView.setVisibility(TextView.VISIBLE);
			return false;
		}
		if (facilityCategorySpinner.getSelectedItem() == null) {
			failuresTextView.setText(getString(R.string.add_facility_error_no_category_selected));
			failuresTextView.setVisibility(TextView.VISIBLE);
			return false;
		}
		return true;
	}

	private void cleanFailuresView() {
		failuresTextView.setText("");
		failuresTextView.setVisibility(TextView.INVISIBLE);
	}
	
	private class FacilityCategoriesListRequestListener extends BaseRequestListener<FacilityCategoryListResponse, FacilityCategoryList> {
		public FacilityCategoriesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FacilityCategoryList data) {
			logger.info("List of FacilityCategories successfully loaded");
			categoryAdapter.clear();
			for (FacilityCategory facilityCategory : data) {
				categoryAdapter.add(facilityCategory);
			}
			categoryAdapter.notifyDataSetChanged();
		}
	}
	
	private class FacilityCategoryAdapter extends ArrayAdapter<FacilityCategory> {

		public FacilityCategoryAdapter(Context context, int textViewResourceId, List<FacilityCategory> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FacilityCategory facilityCategory = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, null);
			}
			TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
			textView.setTextSize(18);
			textView.setText(facilityCategory.getName().getText(LocaleUtil.getCurrentLocale()));
			return convertView;
		}
	}
	
	private class FacilityRequestListener extends BaseRequestListener<FacilityResponse, Facility> {

		public FacilityRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(Facility facility) {
			Toast.makeText(context.getApplicationContext(), getString(R.string.add_facility_successfully_saved), Toast.LENGTH_LONG).show();
			context.finish();
		}

		@Override
		protected void handleFailures(List<String> failures) {
			String failureText = new String();
			for (String string : failures) {
				failureText = failureText + string + "\n";
			}
			failuresTextView.setText(failureText);
			failuresTextView.setVisibility(TextView.VISIBLE);
		}
	}

}
