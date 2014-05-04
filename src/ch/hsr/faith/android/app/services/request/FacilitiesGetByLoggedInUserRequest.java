package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.android.app.util.Login;

public class FacilitiesGetByLoggedInUserRequest extends AuthenticatedRequest<FacilityListResponse> {
	public FacilitiesGetByLoggedInUserRequest(Login ownerUser) {
		super(ownerUser, FacilityListResponse.class);
	}

	@Override
	public FacilityListResponse loadDataFromNetwork() throws Exception {
		return loadDataFromGetRequest(JSONService.getServiceUrl("/facilities/findUsersFacilities"));
	}

	public String createCacheKey() {
		return "facilities.byUserAccount";
	}
}