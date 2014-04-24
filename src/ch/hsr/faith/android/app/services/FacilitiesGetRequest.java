package ch.hsr.faith.android.app.services;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.dto.FacilityListResponse;
import ch.hsr.faith.domain.UserAccount;

public class FacilitiesGetRequest extends AuthenticatedRequest<FacilityListResponse> {
	private UserAccount ownerAccount;
	
	public FacilitiesGetRequest(UserAccount ownerAccount) {
		super(new UserAccount(), FacilityListResponse.class);
		this.ownerAccount = ownerAccount;
	}

	@Override
	public FacilityListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/facilities/findByUserAccountId/" + ownerAccount.getId()), FacilityListResponse.class);
	}

	public String createCacheKey() {
		return "facilities.all";
	}
}