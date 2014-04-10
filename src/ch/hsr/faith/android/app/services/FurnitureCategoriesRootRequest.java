package ch.hsr.faith.android.app.services;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.dto.FurnitureCategoryListResponse;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FurnitureCategoriesRootRequest extends SpringAndroidSpiceRequest<FurnitureCategoryListResponse> {

	public FurnitureCategoriesRootRequest() {
		super(FurnitureCategoryListResponse.class);
	}

	@Override
	public FurnitureCategoryListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/furniture-categories/all-roots"), FurnitureCategoryListResponse.class);
	}

	public String createCacheKey() {
		return "furniturecategories.roots";
	}

}
