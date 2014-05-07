package ch.hsr.faith.android.app.activities.listeners;

import ch.hsr.faith.android.app.activities.BaseActivity;
import ch.hsr.faith.android.app.activities.adapters.PieceOfFurnituresAdapter;
import ch.hsr.faith.android.app.activities.components.FurnitureCategoryListItem;
import ch.hsr.faith.android.app.dto.FurnitureCategoryList;
import ch.hsr.faith.android.app.services.response.FurnitureCategoryListResponse;
import ch.hsr.faith.domain.FurnitureCategory;

public class FurnitureCategoriesListRequestListener extends BaseRequestListener<FurnitureCategoryListResponse, FurnitureCategoryList> {
	private PieceOfFurnituresAdapter adapter;

	public FurnitureCategoriesListRequestListener(BaseActivity baseActivity, PieceOfFurnituresAdapter adapter) {
		super(baseActivity);
		this.adapter = adapter;
	}

	@Override
	protected void handleSuccess(FurnitureCategoryList data) {
		for (FurnitureCategory furnitureCategory : data) {
			adapter.add(new FurnitureCategoryListItem(furnitureCategory));
		}
		adapter.notifyDataSetChanged();
	}
}