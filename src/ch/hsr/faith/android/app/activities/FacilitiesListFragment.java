package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.FacilityWithDistance;

public class FacilitiesListFragment extends Fragment {

	private FacilitiesTabActivity context;
	private ListView facilitiesListView;
	private FacilitiesAdapter adapter;

	public FacilitiesListFragment(FacilitiesTabActivity context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_facilities_listview_tab, container, false);
		facilitiesListView = (ListView) view.findViewById(R.id.facilities_ListView);
		adapter = new FacilitiesAdapter(context, R.layout.facilities_list_rowlayout, new ArrayList<FacilityWithDistance>());
		facilitiesListView.setAdapter(adapter);
		updateData();
		return view;
	}

	public void updateData() {
		if (adapter != null) {
			adapter.clear();
			adapter.addAll(context.getFacilityList());
			adapter.notifyDataSetChanged();
		}
	}

	private class FacilitiesAdapter extends ArrayAdapter<FacilityWithDistance> {

		public FacilitiesAdapter(Context context, int textViewResourceId, List<FacilityWithDistance> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FacilityWithDistance facility = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.facilities_list_rowlayout, null);
			}
			TextView categoryTextView = (TextView) convertView.findViewById(R.id.facility_rowitem_category_name);
			TextView nameTextView = (TextView) convertView.findViewById(R.id.facility_rowitem_facility_name);
			TextView distanceTextView = (TextView) convertView.findViewById(R.id.facility_rowitem_distance);
			categoryTextView.setText(facility.getFacilityCategory().getName().getText(LocaleUtil.getCurrentLocale()));
			nameTextView.setText(facility.getName());
			distanceTextView.setText(facility.getDistance() + " km");
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			FacilityWithDistance facility = super.getItem(position);
			return facility.getId();
		}
	}

}
