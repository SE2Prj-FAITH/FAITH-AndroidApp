package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.domain.ItemNeeded;

public class ItemNeededResponse extends BaseJSONResponse<ItemNeeded> {

	private ItemNeeded data;

	public ItemNeeded getData() {
		return data;
	}

}
