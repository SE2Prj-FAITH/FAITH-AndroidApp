package ch.hsr.faith.android.app.services.request;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.domain.UserAccount;

public class FacilitiesGetRequest extends AuthenticatedRequest<FacilityListResponse> {
	public FacilitiesGetRequest() {
		// Vorübergehender Dummy UserAccount
		super(new UserAccount(), FacilityListResponse.class);
	}

	@Override
	public FacilityListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/facility/findByUsersFacilities"), FacilityListResponse.class);
	}

	public String createCacheKey() {
		return "facilities.all";
	}
}