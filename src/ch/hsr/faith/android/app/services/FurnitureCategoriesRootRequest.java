package ch.hsr.faith.android.app.services;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.dto.FurnitureCategoryListResponse;
import ch.hsr.faith.android.app.util.PropertyReader;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FurnitureCategoriesRootRequest extends SpringAndroidSpiceRequest<FurnitureCategoryListResponse> {

	public FurnitureCategoriesRootRequest() {
		super(FurnitureCategoryListResponse.class);
	}

	@Override
	public FurnitureCategoryListResponse loadDataFromNetwork() throws RestClientException {
		String url = PropertyReader.getProperty(JSONService.SERVICE_BASE_URL_PROPERTY_KEY) + "/furniture-categories/all-roots";
		return getRestTemplate().getForObject(url, FurnitureCategoryListResponse.class);
	}

	public String createCacheKey() {
		return "furniturecategories.roots";
	}

}
