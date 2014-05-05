package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.ItemNeeded;

public class EditFacilityItemsNeededFragment extends Fragment {

	private EditFacilityActivity context;
	private ListView itemsNeededListView;
	private ItemNeededAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = (EditFacilityActivity) getActivity();
		View view = inflater.inflate(R.layout.fragment_edit_facility_itemsneeded, container, false);
		itemsNeededListView = (ListView) view.findViewById(R.id.itemsNeeded_ListView);
		adapter = new ItemNeededAdapter(context, android.R.layout.simple_list_item_1, new ArrayList<ItemNeeded>());
		itemsNeededListView.setAdapter(adapter);
		itemsNeededListView.setOnItemClickListener(new OnItemNeededClickedListener());
		updateData();
		return view;
	}

	public void updateData() {
		if (adapter != null) {
			adapter.clear();
			adapter.addAll(context.getItemNeededList());
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
				convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, null);
			}
			TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
			textView.setTextSize(17);
			textView.setText(itemNeeded.getAmount() + " x " + itemNeeded.getPieceOfFurniture().getName().getText(LocaleUtil.getCurrentLocale()));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			ItemNeeded itemNeeded = super.getItem(position);
			return itemNeeded.getId();
		}
	}

	private class OnItemNeededClickedListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			ItemNeeded itemNeeded = (ItemNeeded) itemsNeededListView.getItemAtPosition(position);
			openItemNeededSettings(itemNeeded);
		}

		private void openItemNeededSettings(ItemNeeded itemNeeded) {
			Intent intent = new Intent(context, AddOrEditItemNeededActivity.class);
			intent.putExtra(IntentExtras.EXTRA_FACILITY, itemNeeded.getFacility());
			intent.putExtra(IntentExtras.EXTRA_ITEM_NEEDED, itemNeeded);
			startActivity(intent);
		}
	}

}
