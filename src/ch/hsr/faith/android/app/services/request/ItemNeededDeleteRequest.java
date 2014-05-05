package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.StringResponse;
import ch.hsr.faith.android.app.util.Login;
import ch.hsr.faith.domain.ItemNeeded;

public class ItemNeededDeleteRequest extends AuthenticatedRequest<StringResponse> {

	private ItemNeeded itemNeeded;

	public ItemNeededDeleteRequest(Login userLogin, ItemNeeded itemNeeded) {
		super(userLogin, StringResponse.class);
		this.itemNeeded = itemNeeded;
	}

	@Override
	public StringResponse loadDataFromNetwork() throws Exception {
		return loadDataFromGetRequest(JSONService.getServiceUrl("/items-needed/delete/" + itemNeeded.getId()));
	}

	public String createCacheKey() {
		return "itemNeededDelete." + itemNeeded.getId();
	}

}
