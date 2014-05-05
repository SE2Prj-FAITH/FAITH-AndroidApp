package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.ItemNeededListResponse;
import ch.hsr.faith.android.app.util.Login;
import ch.hsr.faith.domain.Facility;

public class ItemsNeededGetByFacilityRequest extends AuthenticatedRequest<ItemNeededListResponse> {

	private Facility facility;

	public ItemsNeededGetByFacilityRequest(Login userLogin, Facility facility) {
		super(userLogin, ItemNeededListResponse.class);
		this.facility = facility;
	}

	@Override
	public ItemNeededListResponse loadDataFromNetwork() throws Exception {
		return loadDataFromGetRequest(JSONService.getServiceUrl("/items-needed/findByFacility/" + facility.getId()));
	}

	public String createCacheKey() {
		return "itemsNeededByFacility." + facility.getId();
	}

}
