package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.android.app.dto.ItemNeededList;

public class ItemNeededListResponse extends BaseJSONResponse<ItemNeededList> {

	private ItemNeededList data;

	public ItemNeededList getData() {
		return data;
	}

}
