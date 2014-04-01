package ch.hsr.faith.android.app.dto;

import ch.hsr.faith.android.app.domain.FurnitureCategoryList;

public class FurnitureCategoryListResponse extends BaseJSONResponse<FurnitureCategoryList> {

	private FurnitureCategoryList data;

	public FurnitureCategoryList getData() {
		return data;
	}

}
