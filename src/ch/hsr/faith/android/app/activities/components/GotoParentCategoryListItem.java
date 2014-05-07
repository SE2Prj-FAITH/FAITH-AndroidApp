package ch.hsr.faith.android.app.activities.components;

import ch.hsr.faith.domain.FurnitureCategory;

public class GotoParentCategoryListItem implements SelectPieceOfFurnitureListItem {
	private FurnitureCategory parentCategory;

	public GotoParentCategoryListItem(FurnitureCategory parentCategory) {
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