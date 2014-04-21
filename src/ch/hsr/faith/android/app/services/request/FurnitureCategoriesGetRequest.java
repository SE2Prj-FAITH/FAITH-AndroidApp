package ch.hsr.faith.android.app.services.request;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FurnitureCategoryListResponse;
import ch.hsr.faith.domain.FurnitureCategory;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FurnitureCategoriesGetRequest extends SpringAndroidSpiceRequest<FurnitureCategoryListResponse> {

	private FurnitureCategory parentCategory;

	public FurnitureCategoriesGetRequest(FurnitureCategory parentCategory) {
		super(FurnitureCategoryListResponse.class);
		this.parentCategory = parentCategory;
	}

	@Override
	public FurnitureCategoryListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/furniture-categories/findByParentId/" + parentCategory.getId()), FurnitureCategoryListResponse.class);
	}

	public String createCacheKey() {
		return "furnitureCategoriesByParent." + parentCategory.getId();
	}

}
