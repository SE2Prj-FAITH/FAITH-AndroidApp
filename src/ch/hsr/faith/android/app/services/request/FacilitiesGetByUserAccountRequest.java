package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.domain.UserAccount;

public class FacilitiesGetByUserAccountRequest extends AuthenticatedRequest<FacilityListResponse> {
	private UserAccount ownerAccount;
	
	public FacilitiesGetByUserAccountRequest(UserAccount ownerAccount) {
		super(ownerAccount, FacilityListResponse.class);
		this.ownerAccount = ownerAccount;
	}

	@Override
	public FacilityListResponse loadDataFromNetwork() throws Exception {
		return loadDataFromGetRequest(JSONService.getServiceUrl("/facilities/findByUserAccountId/" + ownerAccount.getId()));
	}

	public String createCacheKey() {
		return "facilities.byUserAccount";
	}
}