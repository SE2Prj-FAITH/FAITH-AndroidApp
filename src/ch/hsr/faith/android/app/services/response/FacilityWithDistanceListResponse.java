package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.android.app.dto.FacilityWithDistanceList;

public class FacilityWithDistanceListResponse extends BaseJSONResponse<FacilityWithDistanceList> {

	private FacilityWithDistanceList data;

	public FacilityWithDistanceList getData() {
		return data;
	}

}
