package ch.hsr.faith.android.app.services.request;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.domain.UserAccount;


public class FacilitiesGetByLoggedInUserRequest extends AuthenticatedRequest<FacilityListResponse> {
	// Vorübergehender Dummy UserAccount
	static UserAccount authUser;
	
	public FacilitiesGetByLoggedInUserRequest() {
		super(getUser(), FacilityListResponse.class);
	}
	
	private static UserAccount getUser() {
		UserAccount authUser = new UserAccount();
		authUser.setEmail("all@facilities.owner");
		authUser.setPassword("1234");
		authUser.setId(2421L);
		return authUser;
	}

	@Override
	public FacilityListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/facilities/findUsersFacilities"), FacilityListResponse.class);
	}

	public String createCacheKey() {
		return "facilities.byUserAccount";
	}
}