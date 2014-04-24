package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.android.app.dto.FacilityList;

public class FacilityListResponse extends BaseJSONResponse<FacilityList> {

	private FacilityList data;

	public FacilityList getData() {
		return data;
	}

}
