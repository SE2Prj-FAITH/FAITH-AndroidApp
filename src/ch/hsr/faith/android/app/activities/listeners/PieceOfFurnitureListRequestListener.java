package ch.hsr.faith.android.app.activities.listeners;

import ch.hsr.faith.android.app.activities.BaseActivity;
import ch.hsr.faith.android.app.activities.adapters.PieceOfFurnituresAdapter;
import ch.hsr.faith.android.app.activities.components.PieceOfFurnitureListItem;
import ch.hsr.faith.android.app.dto.PieceOfFurnitureList;
import ch.hsr.faith.android.app.services.response.PieceOfFurnitureListResponse;
import ch.hsr.faith.domain.PieceOfFurniture;

public class PieceOfFurnitureListRequestListener extends BaseRequestListener<PieceOfFurnitureListResponse, PieceOfFurnitureList> {
	private PieceOfFurnituresAdapter adapter;

	public PieceOfFurnitureListRequestListener(BaseActivity baseActivity, PieceOfFurnituresAdapter adapter) {
		super(baseActivity);
		this.adapter = adapter;
	}

	@Override
	protected void handleSuccess(PieceOfFurnitureList data) {
		for (PieceOfFurniture pieceOfFurniture : data) {
			adapter.add(new PieceOfFurnitureListItem(pieceOfFurniture));
		}
		adapter.notifyDataSetChanged();
	}
}