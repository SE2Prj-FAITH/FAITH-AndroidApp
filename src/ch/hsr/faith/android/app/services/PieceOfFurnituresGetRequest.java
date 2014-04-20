package ch.hsr.faith.android.app.services;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.dto.PieceOfFurnitureListResponse;
import ch.hsr.faith.domain.FurnitureCategory;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class PieceOfFurnituresGetRequest extends SpringAndroidSpiceRequest<PieceOfFurnitureListResponse> {

	private FurnitureCategory furnitureCategory;

	public PieceOfFurnituresGetRequest(FurnitureCategory furnitureCategory) {
		super(PieceOfFurnitureListResponse.class);
		this.furnitureCategory = furnitureCategory;
	}

	@Override
	public PieceOfFurnitureListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(JSONService.getServiceUrl("/piece-of-furnitures/findByCategoryId/" + furnitureCategory.getId()), PieceOfFurnitureListResponse.class);
	}

	public String createCacheKey() {
		return "pieceOfFurnituresByCategory." + furnitureCategory.getId();
	}

}
