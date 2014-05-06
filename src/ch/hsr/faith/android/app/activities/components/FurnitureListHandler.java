package ch.hsr.faith.android.app.activities.components;

import ch.hsr.faith.android.app.activities.BaseActivity;
import ch.hsr.faith.android.app.activities.adapters.PieceOfFurnituresAdapter;
import ch.hsr.faith.android.app.activities.listeners.FurnitureCategoriesListRequestListener;
import ch.hsr.faith.android.app.activities.listeners.PieceOfFurnitureListRequestListener;
import ch.hsr.faith.android.app.services.request.FurnitureCategoriesGetByParentRequest;
import ch.hsr.faith.android.app.services.request.FurnitureCategoriesRootRequest;
import ch.hsr.faith.android.app.services.request.PieceOfFurnituresGetByCategoryRequest;
import ch.hsr.faith.domain.FurnitureCategory;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

public class FurnitureListHandler {

	private BaseActivity context;
	private PieceOfFurnituresAdapter adapter;
	private SpiceManager spiceManager;
	private String lastFurnitureCategoriesRootRequestCacheKey;
	private String lastFurnitureCategoriesRequestCacheKey;
	private String lastPieceOfFurnituresRequestCacheKey;

	public FurnitureListHandler(BaseActivity context, SpiceManager spiceManager, PieceOfFurnituresAdapter adapter) {
		this.context = context;
		this.spiceManager = spiceManager;
		this.adapter = adapter;
	}

	public void loadRootCategories() {
		adapter.clear();
		FurnitureCategoriesRootRequest request = new FurnitureCategoriesRootRequest();
		lastFurnitureCategoriesRootRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFurnitureCategoriesRootRequestCacheKey, DurationInMillis.ONE_HOUR, new FurnitureCategoriesListRequestListener(context, adapter));
	}

	public void loadSubCategory(FurnitureCategory furnitureCategory) {
		adapter.clear();
		GotoParentCategoryListItem gotoParentListItem = new GotoParentCategoryListItem(furnitureCategory.getParent());
		adapter.add(gotoParentListItem);
		FurnitureCategoriesGetByParentRequest request = new FurnitureCategoriesGetByParentRequest(furnitureCategory);
		lastFurnitureCategoriesRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastFurnitureCategoriesRequestCacheKey, DurationInMillis.ONE_HOUR, new FurnitureCategoriesListRequestListener(context, adapter));

		PieceOfFurnituresGetByCategoryRequest requestPieceOfFurnitures = new PieceOfFurnituresGetByCategoryRequest(furnitureCategory);
		lastPieceOfFurnituresRequestCacheKey = requestPieceOfFurnitures.createCacheKey();
		spiceManager.execute(requestPieceOfFurnitures, lastPieceOfFurnituresRequestCacheKey, DurationInMillis.ONE_HOUR, new PieceOfFurnitureListRequestListener(context, adapter));
	}

}
