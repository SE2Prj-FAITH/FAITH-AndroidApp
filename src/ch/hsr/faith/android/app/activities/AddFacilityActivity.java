package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FacilityCategoryList;
import ch.hsr.faith.android.app.services.request.FacilityCategoriesGetAllRequest;
import ch.hsr.faith.android.app.services.response.FacilityCategoryListResponse;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.FacilityCategory;

import com.octo.android.robospice.persistence.DurationInMillis;

public class AddFacilityActivity extends BaseActivity {
	private String lastFacilityCategoriesGetAllRequestCacheKey;

	ArrayList<String> listData;
	private Spinner facilityCategorySpinner;
	private FacilityCategoryAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_facility);
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}*/

		facilityCategorySpinner = (Spinner) findViewById(R.id.selectFacilityCategory);
		adapter = new FacilityCategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<FacilityCategory>());
		facilityCategorySpinner.setAdapter(adapter);
		loadFacilityCategoryList();
	}
	
	private void loadFacilityCategoryList() {
		FacilityCategoriesGetAllRequest request = new FacilityCategoriesGetAllRequest();
		lastFacilityCategoriesGetAllRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFacilityCategoriesGetAllRequestCacheKey, DurationInMillis.ONE_MINUTE, new FacilityCategoriesListRequestListener(this));
	}
	
	private class FacilityCategoriesListRequestListener extends BaseRequestListener<FacilityCategoryListResponse, FacilityCategoryList> {
		public FacilityCategoriesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FacilityCategoryList data) {
			logger.debug("List of FacilityCategories successfully loaded");
			adapter.clear();
			for (FacilityCategory facilityCategory : data) {
				adapter.add(facilityCategory);
			}
			adapter.notifyDataSetChanged();
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
			textView.setTextSize(17);
			textView.setText(facilityCategory.getName().getText(LocaleUtil.getCurrentLocale()));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			FacilityCategory category = super.getItem(position);
			return category.getId();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 *
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_facility, container, false);
			return rootView;
		}
	}*/
}
