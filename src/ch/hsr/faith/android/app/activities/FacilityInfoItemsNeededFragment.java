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
import ch.hsr.faith.domain.ItemNeeded;

public class FacilityInfoItemsNeededFragment extends Fragment {

	private FacilityInfoActivity context;
	private ListView itemsNeededListView;
	private ItemNeededAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = (FacilityInfoActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_facility_info_items_needed, container, false);
		itemsNeededListView = (ListView) view.findViewById(R.id.itemsNeeded_ListView);
		adapter = new ItemNeededAdapter(context, R.layout.facility_info_item_needed_rowlayout, new ArrayList<ItemNeeded>());
		itemsNeededListView.setAdapter(adapter);
		updateData();
		return view;
	}

	public void updateData() {
		if (adapter != null) {
			adapter.clear();
			adapter.addAll(context.getItemsNeeded());
			adapter.notifyDataSetChanged();
		}
	}

	private class ItemNeededAdapter extends ArrayAdapter<ItemNeeded> {
		public ItemNeededAdapter(Context context, int textViewResourceId, List<ItemNeeded> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ItemNeeded itemNeeded = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.facility_info_item_needed_rowlayout, null);
			}
			TextView textViewTitle = (TextView) convertView.findViewById(R.id.itemNeeded_amount_and_name_text);
			TextView textViewDescription = (TextView) convertView.findViewById(R.id.itemNeeded_description);
			textViewTitle.setText(itemNeeded.getAmount() + " x " + itemNeeded.getPieceOfFurniture().getName().getText(LocaleUtil.getCurrentLocale()));
			textViewDescription.setText(itemNeeded.getDescription());
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			ItemNeeded itemNeeded = super.getItem(position);
			return itemNeeded.getId();
		}
	}

}
