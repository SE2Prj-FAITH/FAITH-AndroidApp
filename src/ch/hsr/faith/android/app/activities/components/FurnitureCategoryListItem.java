package ch.hsr.faith.android.app.activities.components;

import ch.hsr.faith.domain.FurnitureCategory;

public class FurnitureCategoryListItem implements SelectPieceOfFurnitureListItem {
	private FurnitureCategory furnitureCategory;

	public FurnitureCategoryListItem(FurnitureCategory furnitureCategory) {
		this.furnitureCategory = furnitureCategory;
	}

	public long getId() {
		return furnitureCategory.getId();
	}

	public FurnitureCategory getFurnitureCategory() {
		return this.furnitureCategory;
	}
}