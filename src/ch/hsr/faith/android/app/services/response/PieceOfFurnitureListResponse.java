package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.android.app.dto.PieceOfFurnitureList;

public class PieceOfFurnitureListResponse extends BaseJSONResponse<PieceOfFurnitureList> {

	private PieceOfFurnitureList data;

	public PieceOfFurnitureList getData() {
		return data;
	}

}
