package ch.hsr.faith.android.app.services.request;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityCategoryListResponse;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FacilityCategoriesGetAllRequest extends SpringAndroidSpiceRequest<FacilityCategoryListResponse> {

	public FacilityCategoriesGetAllRequest() {
		super(FacilityCategoryListResponse.class);
	}

	@Override
	public FacilityCategoryListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/facility-categories/find-all"), FacilityCategoryListResponse.class);
	}

	public String createCacheKey() {
		return "facilityCategoriesFindAll";
	}

}
