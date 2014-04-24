package ch.hsr.faith.android.app.services.request;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityListResponse;
import ch.hsr.faith.domain.FacilityCategory;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FacilitiesGetByCategoryRequest extends SpringAndroidSpiceRequest<FacilityListResponse> {

	private FacilityCategory category;

	public FacilitiesGetByCategoryRequest(FacilityCategory category) {
		super(FacilityListResponse.class);
		this.category = category;
	}

	@Override
	public FacilityListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/facilities/findByCategoryId/" + category.getId()), FacilityListResponse.class);
	}

	public String createCacheKey() {
		return "facilitiesByCategory." + category.getId();
	}

}
