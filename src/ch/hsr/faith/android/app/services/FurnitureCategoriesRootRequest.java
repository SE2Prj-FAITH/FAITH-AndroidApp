package ch.hsr.faith.android.app.services;

import ch.hsr.faith.android.app.domain.FurnitureCategoryList;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FurnitureCategoriesRootRequest extends SpringAndroidSpiceRequest<FurnitureCategoryList> {

	public FurnitureCategoriesRootRequest() {
		super(FurnitureCategoryList.class);
	}

	@Override
	public FurnitureCategoryList loadDataFromNetwork() throws Exception {
		String url = "http://sinv-56078.edu.hsr.ch/faith-rest-application/furniture-categories/all-roots";
		return getRestTemplate().getForObject(url, FurnitureCategoryList.class);
	}

	public String createCacheKey() {
		return "furniturecategories.roots";
	}

}
