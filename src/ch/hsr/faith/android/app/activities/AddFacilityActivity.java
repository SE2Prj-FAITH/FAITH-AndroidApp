package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FacilityCategoryList;
import ch.hsr.faith.android.app.services.request.AddItemNeededRequest;
import ch.hsr.faith.android.app.services.request.FacilityCategoriesGetAllRequest;
import ch.hsr.faith.android.app.services.response.FacilityCategoryListResponse;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.Facility;
import ch.hsr.faith.domain.FacilityCategory;
import ch.hsr.faith.domain.ItemNeeded;

import com.octo.android.robospice.persistence.DurationInMillis;

public class AddFacilityActivity extends BaseActivity {
	private Facility facility;
	
	private TextView failuresTextView;
	private String lastFacilityCategoriesGetAllRequestCacheKey;
	
	ArrayList<String> listData;
	private Spinner facilityCategorySpinner;
	private FacilityCategoryAdapter categoryAdapter;

	private EditText nameField;
	private EditText streetField;
	private EditText zipField;
	private EditText cityField;
	private EditText homepageField;
	private EditText phoneField;
	private EditText emailField;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_facility);

		facilityCategorySpinner = (Spinner) findViewById(R.id.selectFacilityCategory);
		categoryAdapter = new FacilityCategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
		facilityCategorySpinner.setAdapter(categoryAdapter);
		loadFacilityCategoryList();
		
		Spinner spinner = (Spinner) findViewById(R.id.selectFacilityCountry);
		ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this, R.array.countries_available, android.R.layout.simple_spinner_dropdown_item);
		countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(countryAdapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		failuresTextView = (TextView) findViewById(R.id.addFacilityFailures);
		nameField = (EditText) findViewById(R.id.editFacilityName);
		streetField = (EditText) findViewById(R.id.editFacilityStreet);
		zipField = (EditText) findViewById(R.id.editFacilityZip);
		cityField = (EditText) findViewById(R.id.editFacilityCity);
		homepageField = (EditText) findViewById(R.id.editFacilityHomepage);
		phoneField = (EditText) findViewById(R.id.editFacilityPhone);
		emailField = (EditText) findViewById(R.id.editFacilityEmail);
	}
	
	private void loadFacilityCategoryList() {
		FacilityCategoriesGetAllRequest request = new FacilityCategoriesGetAllRequest();
		lastFacilityCategoriesGetAllRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFacilityCategoriesGetAllRequestCacheKey, DurationInMillis.ONE_MINUTE, new FacilityCategoriesListRequestListener(this));
	}
	
	public void addButtonClicked() {
		
	}
	
	

	public void addButtonClicked(View view) {
		cleanFailuresView();
		if (isInputValid()) {
			ItemNeeded itemNeeded = new ItemNeeded();
			/*itemNeeded.setAmount(Integer.parseInt(amountField.getText().toString()));
			itemNeeded.setDescription(descriptionField.getText().toString());
			itemNeeded.setFacility(facility);
			itemNeeded.setPieceOfFurniture(selectedPieceOfFurniture);

			AddItemNeededRequest request = new AddItemNeededRequest(getLoginObject(), itemNeeded);
			saveItemNeededRequestCacheKey = request.createCacheKey();
			spiceManager.execute(request, saveItemNeededRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new AddItemNeededRequestListener(this));*/
		}
	}

	private boolean isInputValid() {
		/*if (selectedPieceOfFurniture == null) {
			failuresTextView.setText(getString(R.string.add_item_needed_error_piece_of_furniture_empty));
			failuresTextView.setVisibility(TextView.VISIBLE);
			return false;
		}*/
		if ("".equals(nameField.getText().toString())) {
			nameField.setError(getString(R.string.add_item_needed_error_amount_empty));
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
				categoryAdapter.add(facilityCategory.getName().getText(LocaleUtil.getCurrentLocale()));
			}
			categoryAdapter.notifyDataSetChanged();
		}
	}
	
	private class FacilityCategoryAdapter extends ArrayAdapter<String> {

		public FacilityCategoryAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//String facilityCategory = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, null);
			}
			/*TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
			textView.setTextSize(17);
			textView.setText(facilityCategory);*/
			return convertView;
		}
	}
}
