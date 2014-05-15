package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.ItemNeededListResponse;
import ch.hsr.faith.domain.Facility;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ItemsNeededGetByFacilityRequest extends SpringAndroidSpiceRequest<ItemNeededListResponse> {

	private Facility facility;

	public ItemsNeededGetByFacilityRequest(Facility facility) {
		super(ItemNeededListResponse.class);
		this.facility = facility;
	}

	@Override
	public ItemNeededListResponse loadDataFromNetwork() throws Exception {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/items-needed/findByFacility/" + facility.getId()), ItemNeededListResponse.class);
	}

	public String createCacheKey() {
		return "itemsNeededByFacility." + facility.getId();
	}

}
