package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityResponse;
import ch.hsr.faith.android.app.util.Login;
import ch.hsr.faith.domain.Facility;

public class AddOrUpdateFacilityRequest extends AuthenticatedRequest<FacilityResponse> {

	private Facility facility;

	public AddOrUpdateFacilityRequest(Login ownerUser, Facility facility) {
		super(ownerUser, FacilityResponse.class);
		this.facility = facility;
	}

	@Override
	public FacilityResponse loadDataFromNetwork() throws Exception {
		return loadDataFromPostRequest(JSONService.getServiceUrl("/facilities/add"), facility);
	}

	public String createCacheKey() {
		return "addFacility." + this;
	}

}
