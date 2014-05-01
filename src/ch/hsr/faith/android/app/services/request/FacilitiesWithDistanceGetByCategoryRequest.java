package ch.hsr.faith.android.app.services.request;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityWithDistanceListResponse;
import ch.hsr.faith.domain.FacilityCategory;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FacilitiesWithDistanceGetByCategoryRequest extends SpringAndroidSpiceRequest<FacilityWithDistanceListResponse> {

	private FacilityCategory category;
	private double longitude;
	private double latitude;

	public FacilitiesWithDistanceGetByCategoryRequest(FacilityCategory category, double latitude, double longitude) {
		super(FacilityWithDistanceListResponse.class);
		this.category = category;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public FacilityWithDistanceListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(
				JSONService.getServiceUrl("/facilities/findByCategoryId/" + category.getId() + "?latitude=" + latitude + "&longitude=" + longitude + "&"),
				FacilityWithDistanceListResponse.class);
	}

	public String createCacheKey() {
		return "facilitiesByCategory." + category.getId();
	}

}
