package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.ItemNeededResponse;
import ch.hsr.faith.android.app.util.Login;
import ch.hsr.faith.domain.ItemNeeded;

public class AddOrUpdateItemNeededRequest extends AuthenticatedRequest<ItemNeededResponse> {

	private ItemNeeded itemNeeded;

	public AddOrUpdateItemNeededRequest(Login ownerUser, ItemNeeded itemNeeded) {
		super(ownerUser, ItemNeededResponse.class);
		this.itemNeeded = itemNeeded;
	}

	@Override
	public ItemNeededResponse loadDataFromNetwork() throws Exception {
		return loadDataFromPostRequest(JSONService.getServiceUrl("/items-needed/save"), itemNeeded);
	}

	public String createCacheKey() {
		return "addItemNeeded." + this;
	}

}
