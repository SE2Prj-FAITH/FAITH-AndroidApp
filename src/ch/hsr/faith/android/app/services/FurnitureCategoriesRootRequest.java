package ch.hsr.faith.android.app.services;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.domain.FurnitureCategoryList;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FurnitureCategoriesRootRequest extends SpringAndroidSpiceRequest<FurnitureCategoryList> {

	public FurnitureCategoriesRootRequest() {
		super(FurnitureCategoryList.class);
	}

	@Override
	public FurnitureCategoryList loadDataFromNetwork()  {
		String url = "http://sinv-56078.edu.hsr.ch/faith-rest-application/furniture-categories/all-roots";
		FurnitureCategoryList result;
		try {
			result =  getRestTemplate().getForObject(url, FurnitureCategoryList.class);			
		} catch (RestClientException e) {
			System.err.println("communication error");
			result = new FurnitureCategoryList();
		}
		return result;
	}

	public String createCacheKey() {
		return "furniturecategories.roots";
	}

}
