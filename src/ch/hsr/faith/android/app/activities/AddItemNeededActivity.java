package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.FurnitureCategoryList;
import ch.hsr.faith.android.app.dto.PieceOfFurnitureList;
import ch.hsr.faith.android.app.services.request.AddItemNeededRequest;
import ch.hsr.faith.android.app.services.request.FurnitureCategoriesGetByParentRequest;
import ch.hsr.faith.android.app.services.request.FurnitureCategoriesRootRequest;
import ch.hsr.faith.android.app.services.request.PieceOfFurnituresGetByCategoryRequest;
import ch.hsr.faith.android.app.services.response.FurnitureCategoryListResponse;
import ch.hsr.faith.android.app.services.response.ItemNeededResponse;
import ch.hsr.faith.android.app.services.response.PieceOfFurnitureListResponse;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.Facility;
import ch.hsr.faith.domain.FurnitureCategory;
import ch.hsr.faith.domain.ItemNeeded;
import ch.hsr.faith.domain.PieceOfFurniture;

import com.octo.android.robospice.persistence.DurationInMillis;

public class AddItemNeededActivity extends BaseActivity {

	private TextView failuresTextView;
	private ListView popupListView;
	private PieceOfFurnituresAdapter popupListAdapter;

	private EditText amountField;
	private EditText descriptionField;
	private TextView selectedPieceOfFurnitureLabel;
	private PieceOfFurniture selectedPieceOfFurniture;
	private Facility facility;

	private String lastFurnitureCategoriesRootRequestCacheKey;
	private String lastFurnitureCategoriesRequestCacheKey;
	private String lastPieceOfFurnituresRequestCacheKey;
	private String saveItemNeededRequestCacheKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item_needed);
	}

	@Override
	protected void onStart() {
		super.onStart();
		failuresTextView = (TextView) findViewById(R.id.AddItemNeededFailures);
		amountField = (EditText) findViewById(R.id.addItemNeededAmount);
		amountField.setText("1");
		descriptionField = (EditText) findViewById(R.id.addItemNeededDescription);
		selectedPieceOfFurnitureLabel = (TextView) findViewById(R.id.AddItemNeededPieceOfFurniture);
		facility = (Facility) getIntent().getExtras().get("facility");
	}

	public void selectPieceOfFurnitureClicked(View view) {
		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.selectPieceOfFurniturePopup);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.select_piece_of_furniture_popup, viewGroup);

		final PopupWindow popup = new PopupWindow(this);
		popup.setContentView(layout);
		popup.setWidth(getWindowManager().getDefaultDisplay().getWidth() - 50);
		popup.setHeight(getWindowManager().getDefaultDisplay().getHeight() - 150);
		popup.setFocusable(true);

		popup.showAtLocation(layout, Gravity.CENTER, 0, 10);

		Button close = (Button) layout.findViewById(R.id.selectPieceOfFurniturePopupCancelButton);
		close.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				popup.dismiss();
			}
		});
		try {
			popupListView = (ListView) layout.findViewById(R.id.selectPieceOfFurniturePopupListView);
			popupListAdapter = new PieceOfFurnituresAdapter(this, R.layout.select_piece_of_furniture_popup_listview_item, new ArrayList<SelectPieceOfFurnitureListItem>());
			popupListView.setAdapter(popupListAdapter);
			popupListView.setOnItemClickListener(new OnSelectPieceOfFurnitureListItemClickedListener(popup));
			loadRootCategories();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveButtonClicked(View view) {
		cleanFailuresView();
		if (isInputValid()) {
			ItemNeeded itemNeeded = new ItemNeeded();
			itemNeeded.setAmount(Integer.parseInt(amountField.getText().toString()));
			itemNeeded.setDescription(descriptionField.getText().toString());
			itemNeeded.setFacility(facility);
			itemNeeded.setPieceOfFurniture(selectedPieceOfFurniture);

			AddItemNeededRequest request = new AddItemNeededRequest(getLoginObject(), itemNeeded);
			saveItemNeededRequestCacheKey = request.createCacheKey();
			spiceManager.execute(request, saveItemNeededRequestCacheKey, 0, new AddItemNeededRequestListener(this));
		}
	}

	private boolean isInputValid() {
		if (selectedPieceOfFurniture == null) {
			failuresTextView.setText(getString(R.string.add_item_needed_error_piece_of_furniture_empty));
			failuresTextView.setVisibility(TextView.VISIBLE);
			return false;
		}
		if ("".equals(amountField.getText().toString())) {
			amountField.setError(getString(R.string.add_item_needed_error_amount_empty));
			return false;
		}
		return true;
	}

	private void cleanFailuresView() {
		failuresTextView.setText("");
		failuresTextView.setVisibility(TextView.INVISIBLE);
	}

	private void loadRootCategories() {
		popupListAdapter.clear();
		FurnitureCategoriesRootRequest request = new FurnitureCategoriesRootRequest();
		lastFurnitureCategoriesRootRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFurnitureCategoriesRootRequestCacheKey, DurationInMillis.ONE_HOUR, new FurnitureCategoriesListRequestListener(this));
	}

	private void loadSubCategory(FurnitureCategory furnitureCategory) {
		popupListAdapter.clear();
		GotoParentCategoryListItem gotoParentListItem = new GotoParentCategoryListItem(furnitureCategory.getParent());
		popupListAdapter.add(gotoParentListItem);
		FurnitureCategoriesGetByParentRequest request = new FurnitureCategoriesGetByParentRequest(furnitureCategory);
		lastFurnitureCategoriesRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFurnitureCategoriesRequestCacheKey, DurationInMillis.ONE_HOUR, new FurnitureCategoriesListRequestListener(this));

		PieceOfFurnituresGetByCategoryRequest requestPieceOfFurnitures = new PieceOfFurnituresGetByCategoryRequest(furnitureCategory);
		lastPieceOfFurnituresRequestCacheKey = requestPieceOfFurnitures.createCacheKey();
		spiceManager.execute(requestPieceOfFurnitures, lastPieceOfFurnituresRequestCacheKey, DurationInMillis.ONE_HOUR, new PieceOfFurnitureListRequestListener(this));
	}

	private void selectPieceOfFurniture(PieceOfFurniture pieceOfFurniture) {
		selectedPieceOfFurniture = pieceOfFurniture;
		selectedPieceOfFurnitureLabel.setText(getString(R.string.add_item_needed_piece_of_furniture_selected) + " "
				+ pieceOfFurniture.getName().getText(LocaleUtil.getCurrentLocale()));
	}

	private class AddItemNeededRequestListener extends BaseRequestListener<ItemNeededResponse, ItemNeeded> {

		public AddItemNeededRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(ItemNeeded ItemNeeded) {
			Toast.makeText(getApplicationContext(), getString(R.string.add_item_needed_successfully_saved), Toast.LENGTH_LONG).show();
//			Intent intent = new Intent(AddItemNeededActivity.this, EditFacilityActivity.class);
//			intent.putExtra("facility", facility);
//			startActivityIfNeeded(intent, 0);
			finish();
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

	private class OnSelectPieceOfFurnitureListItemClickedListener implements OnItemClickListener {
		private PopupWindow popup;

		public OnSelectPieceOfFurnitureListItemClickedListener(PopupWindow popup) {
			this.popup = popup;
		}

		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			SelectPieceOfFurnitureListItem item = (SelectPieceOfFurnitureListItem) popupListView.getItemAtPosition(position);
			if (item instanceof FurnitureCategoryListItem) {
				FurnitureCategoryListItem furnitureCategoryListItem = (FurnitureCategoryListItem) item;
				loadSubCategory(furnitureCategoryListItem.getFurnitureCategory());
			} else if (item instanceof PieceOfFurnitureListItem) {
				PieceOfFurnitureListItem pieceOfFurnitureListItem = (PieceOfFurnitureListItem) item;
				selectPieceOfFurniture(pieceOfFurnitureListItem.getPieceOfFurniture());
				popup.dismiss();
			} else if (item instanceof GotoParentCategoryListItem) {
				GotoParentCategoryListItem gotoParentCategoryListItem = (GotoParentCategoryListItem) item;
				if (gotoParentCategoryListItem.getFurnitureCategory() == null) {
					loadRootCategories();
				} else {
					loadSubCategory(gotoParentCategoryListItem.getFurnitureCategory());
				}
			}
		}
	}

	private class FurnitureCategoriesListRequestListener extends BaseRequestListener<FurnitureCategoryListResponse, FurnitureCategoryList> {
		public FurnitureCategoriesListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(FurnitureCategoryList data) {
			for (FurnitureCategory furnitureCategory : data) {
				popupListAdapter.add(new FurnitureCategoryListItem(furnitureCategory));
			}
			popupListAdapter.notifyDataSetChanged();
		}
	}

	private class PieceOfFurnitureListRequestListener extends BaseRequestListener<PieceOfFurnitureListResponse, PieceOfFurnitureList> {
		public PieceOfFurnitureListRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(PieceOfFurnitureList data) {
			for (PieceOfFurniture pieceOfFurniture : data) {
				popupListAdapter.add(new PieceOfFurnitureListItem(pieceOfFurniture));
			}
			popupListAdapter.notifyDataSetChanged();
		}
	}

	private class PieceOfFurnituresAdapter extends ArrayAdapter<SelectPieceOfFurnitureListItem> {

		public PieceOfFurnituresAdapter(Context context, int textViewResourceId, List<SelectPieceOfFurnitureListItem> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SelectPieceOfFurnitureListItem listItem = getItem(position);
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_piece_of_furniture_popup_listview_item, null);
			ImageView image = (ImageView) convertView.findViewById(R.id.item_icon);
			TextView textView = (TextView) convertView.findViewById(R.id.item_title);
			if (listItem instanceof FurnitureCategoryListItem) {
				FurnitureCategoryListItem furnitureCategoryListItem = (FurnitureCategoryListItem) listItem;
				image.setImageResource(android.R.drawable.ic_menu_more);
				textView.setText(furnitureCategoryListItem.getFurnitureCategory().getName().getText(LocaleUtil.getCurrentLocale()));
			} else if (listItem instanceof PieceOfFurnitureListItem) {
				PieceOfFurnitureListItem pieceOfFurnitureListItem = (PieceOfFurnitureListItem) listItem;
				image.setImageResource(android.R.drawable.ic_menu_add);
				textView.setText(pieceOfFurnitureListItem.getPieceOfFurniture().getName().getText(LocaleUtil.getCurrentLocale()));
			} else if (listItem instanceof GotoParentCategoryListItem) {
				GotoParentCategoryListItem gotoParentCategoryListItem = (GotoParentCategoryListItem) listItem;
				image.setImageResource(R.drawable.ic_menu_top);
				if (gotoParentCategoryListItem.getFurnitureCategory() == null) {
					textView.setText(getString(R.string.select_piece_of_furniture_top_item_root_text));
				} else {
					textView.setText(getString(R.string.select_piece_of_furniture_top_item_text) + " '"
							+ gotoParentCategoryListItem.getFurnitureCategory().getName().getText(LocaleUtil.getCurrentLocale()) + "'");
				}
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			SelectPieceOfFurnitureListItem listItem = super.getItem(position);
			return listItem.getId();
		}
	}

	private interface SelectPieceOfFurnitureListItem {
		public long getId();
	}

	private class FurnitureCategoryListItem implements SelectPieceOfFurnitureListItem {
		private FurnitureCategory furnitureCategory;

		FurnitureCategoryListItem(FurnitureCategory furnitureCategory) {
			this.furnitureCategory = furnitureCategory;
		}

		public long getId() {
			return furnitureCategory.getId();
		}

		public FurnitureCategory getFurnitureCategory() {
			return this.furnitureCategory;
		}
	}

	private class PieceOfFurnitureListItem implements SelectPieceOfFurnitureListItem {
		private PieceOfFurniture pieceOfFurniture;

		PieceOfFurnitureListItem(PieceOfFurniture pieceOfFurniture) {
			this.pieceOfFurniture = pieceOfFurniture;
		}

		public long getId() {
			return pieceOfFurniture.getId();
		}

		public PieceOfFurniture getPieceOfFurniture() {
			return this.pieceOfFurniture;
		}
	}

	private class GotoParentCategoryListItem implements SelectPieceOfFurnitureListItem {
		private FurnitureCategory parentCategory;

		GotoParentCategoryListItem(FurnitureCategory parentCategory) {
			this.parentCategory = parentCategory;
		}

		public long getId() {
			if (parentCategory == null)
				return 0;
			return parentCategory.getId();
		}

		public FurnitureCategory getFurnitureCategory() {
			return this.parentCategory;
		}
	}

}