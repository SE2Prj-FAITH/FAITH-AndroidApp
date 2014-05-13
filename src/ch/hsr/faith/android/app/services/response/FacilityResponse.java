package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.domain.Facility;

public class FacilityResponse extends BaseJSONResponse<Facility> {

	private Facility data;

	public Facility getData() {
		return data;
	}

}
