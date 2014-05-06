package ch.hsr.faith.android.app.activities.components;

import ch.hsr.faith.domain.PieceOfFurniture;

public class PieceOfFurnitureListItem implements SelectPieceOfFurnitureListItem {
	private PieceOfFurniture pieceOfFurniture;

	public PieceOfFurnitureListItem(PieceOfFurniture pieceOfFurniture) {
		this.pieceOfFurniture = pieceOfFurniture;
	}

	public long getId() {
		return pieceOfFurniture.getId();
	}

	public PieceOfFurniture getPieceOfFurniture() {
		return this.pieceOfFurniture;
	}
}