package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.android.app.dto.FurnitureCategoryList;

public class FurnitureCategoryListResponse extends BaseJSONResponse<FurnitureCategoryList> {

	private FurnitureCategoryList data;

	public FurnitureCategoryList getData() {
		return data;
	}

}
