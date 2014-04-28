package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.android.app.util.Login;

public class FacilitiesGetByUserAccountRequest extends AuthenticatedRequest<FacilityListResponse> {
	private Login ownerAccount;
	
	public FacilitiesGetByUserAccountRequest(Login ownerAccount) {
		super(ownerAccount, FacilityListResponse.class);
		this.ownerAccount = ownerAccount;
	}

	@Override
	public FacilityListResponse loadDataFromNetwork() throws Exception {
		return loadDataFromGetRequest(JSONService.getServiceUrl("/facilities/findByUserAccountId/"));
		// TODO: @Dominic removed userid in the url, userid is not stored locally
	}

	public String createCacheKey() {
		return "facilities.byUserAccount";
	}
}