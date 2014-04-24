package ch.hsr.faith.android.app.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FurnitureCategoryList;
import ch.hsr.faith.android.app.dto.FurnitureCategoryListResponse;
import ch.hsr.faith.android.app.services.FacilitiesGetRequest;
import ch.hsr.faith.android.app.services.FurnitureCategoriesRootRequest;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.FurnitureCategory;

import com.octo.android.robospice.persistence.DurationInMillis;

public class FacilitiesManagementActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities_management);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.facilities_management, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*private void loadFacilityList() {
		FacilitiesGetRequest request = new FacilitiesGetRequest();
		lastFurnitureCategoriesRootRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request,
				lastFurnitureCategoriesRootRequestCacheKey,
				DurationInMillis.ONE_MINUTE,
				new FurnitureCategoriesListRequestListener(this));
	}

	private class FurnitureCategoriesListRequestListener
			extends
			BaseRequestListener<FurnitureCategoryListResponse, FurnitureCategoryList> {

		public FurnitureCategoriesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FurnitureCategoryList data) {
			for (FurnitureCategory s : data) {
				listDataHeader.add(s.getName().getText(
						LocaleUtil.getCurrentLocale()));
			}
			listAdapter.notifyDataSetChanged();
		}
		
		// hier handleAuthenticationFailure() von BaseRequestListener überschreiben um direkt zu Login Activity zu gelangen
	}*/


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_facilities_management, container, false);
			return rootView;
		}
	}

}
