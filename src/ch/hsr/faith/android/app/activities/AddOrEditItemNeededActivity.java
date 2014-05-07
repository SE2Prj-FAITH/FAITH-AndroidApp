package ch.hsr.faith.android.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.adapters.PieceOfFurnituresAdapter;
import ch.hsr.faith.android.app.activities.components.FurnitureCategoryListItem;
import ch.hsr.faith.android.app.activities.components.FurnitureListHandler;
import ch.hsr.faith.android.app.activities.components.GotoParentCategoryListItem;
import ch.hsr.faith.android.app.activities.components.PieceOfFurnitureListItem;
import ch.hsr.faith.android.app.activities.components.SelectPieceOfFurnitureListItem;
import ch.hsr.faith.android.app.activities.constants.IntentExtras;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.services.request.AddOrUpdateItemNeededRequest;
import ch.hsr.faith.android.app.services.request.ItemNeededDeleteRequest;
import ch.hsr.faith.android.app.services.response.ItemNeededResponse;
import ch.hsr.faith.android.app.services.response.StringResponse;
import ch.hsr.faith.android.app.util.LocaleUtil;
import ch.hsr.faith.domain.Facility;
import ch.hsr.faith.domain.ItemNeeded;
import ch.hsr.faith.domain.PieceOfFurniture;

import com.octo.android.robospice.persistence.DurationInMillis;

public class AddOrEditItemNeededActivity extends BaseActivity {

	private TextView failuresTextView;
	private ListView popupListView;
	private PieceOfFurnituresAdapter popupListAdapter;
	private FurnitureListHandler furnitureListHandler;

	private EditText amountField;
	private EditText descriptionField;
	private TextView selectedPieceOfFurnitureLabel;
	private Button deleteButton;
	private PieceOfFurniture selectedPieceOfFurniture;
	private Facility facility;
	private ItemNeeded itemNeeded;

	private String saveItemNeededRequestCacheKey;
	private String deleteItemNeededRequestCacheKey;

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
		deleteButton = (Button) findViewById(R.id.buttonAddItemNeededDelete);
		deleteButton.setVisibility(View.INVISIBLE);
		facility = (Facility) getIntent().getExtras().get(IntentExtras.EXTRA_FACILITY);
		itemNeeded = (ItemNeeded) getIntent().getExtras().get(IntentExtras.EXTRA_ITEM_NEEDED);
		if (itemNeeded != null && itemNeeded.getId() > 0) {
			initGuiForEditItemNeeded();
		}
	}

	private void initGuiForEditItemNeeded() {
		amountField.setText(itemNeeded.getAmount().toString());
		descriptionField.setText(itemNeeded.getDescription());
		selectPieceOfFurniture(itemNeeded.getPieceOfFurniture());
		deleteButton.setVisibility(View.VISIBLE);
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
			furnitureListHandler = new FurnitureListHandler(this, spiceManager, popupListAdapter);
			furnitureListHandler.loadRootCategories();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveButtonClicked(View view) {
		cleanFailuresView();
		if (isInputValid()) {
			if (itemNeeded == null)
				itemNeeded = new ItemNeeded();
			itemNeeded.setAmount(Integer.parseInt(amountField.getText().toString()));
			itemNeeded.setDescription(descriptionField.getText().toString());
			itemNeeded.setFacility(facility);
			itemNeeded.setPieceOfFurniture(selectedPieceOfFurniture);

			AddOrUpdateItemNeededRequest request = new AddOrUpdateItemNeededRequest(getLoginObject(), itemNeeded);
			saveItemNeededRequestCacheKey = request.createCacheKey();
			spiceManager.execute(request, saveItemNeededRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new AddOrEditItemNeededRequestListener(this));
		}
	}

	public void deleteButtonClicked(View view) {
		ItemNeededDeleteRequest request = new ItemNeededDeleteRequest(getLoginObject(), itemNeeded);
		deleteItemNeededRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, deleteItemNeededRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new DeleteItemNeededRequestListener(this));
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

	private void selectPieceOfFurniture(PieceOfFurniture pieceOfFurniture) {
		selectedPieceOfFurniture = pieceOfFurniture;
		selectedPieceOfFurnitureLabel.setText(getString(R.string.add_item_needed_piece_of_furniture_selected) + " "
				+ pieceOfFurniture.getName().getText(LocaleUtil.getCurrentLocale()));
	}

	private class AddOrEditItemNeededRequestListener extends BaseRequestListener<ItemNeededResponse, ItemNeeded> {

		public AddOrEditItemNeededRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(ItemNeeded ItemNeeded) {
			Toast.makeText(getApplicationContext(), getString(R.string.add_item_needed_successfully_saved), Toast.LENGTH_LONG).show();
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

	private class DeleteItemNeededRequestListener extends BaseRequestListener<StringResponse, String> {

		public DeleteItemNeededRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(String string) {
			Toast.makeText(getApplicationContext(), getString(R.string.add_item_needed_successfully_deleted), Toast.LENGTH_LONG).show();
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
				furnitureListHandler.loadSubCategory(furnitureCategoryListItem.getFurnitureCategory());
			} else if (item instanceof PieceOfFurnitureListItem) {
				PieceOfFurnitureListItem pieceOfFurnitureListItem = (PieceOfFurnitureListItem) item;
				selectPieceOfFurniture(pieceOfFurnitureListItem.getPieceOfFurniture());
				popup.dismiss();
			} else if (item instanceof GotoParentCategoryListItem) {
				GotoParentCategoryListItem gotoParentCategoryListItem = (GotoParentCategoryListItem) item;
				if (gotoParentCategoryListItem.getFurnitureCategory() == null) {
					furnitureListHandler.loadRootCategories();
				} else {
					furnitureListHandler.loadSubCategory(gotoParentCategoryListItem.getFurnitureCategory());
				}
			}
		}
	}
}