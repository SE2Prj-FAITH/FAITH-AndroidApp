package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.android.app.dto.FacilityCategoryList;

public class FacilityCategoryListResponse extends BaseJSONResponse<FacilityCategoryList> {

	private FacilityCategoryList data;

	public FacilityCategoryList getData() {
		return data;
	}

}
