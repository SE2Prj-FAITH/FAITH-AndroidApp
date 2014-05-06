package ch.hsr.faith.android.app.services.request;

import org.springframework.web.client.RestClientException;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.FacilityWithDistanceListResponse;
import ch.hsr.faith.domain.PieceOfFurniture;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class FacilitiesWithDistanceGetByPieceOfItemsNeededRequest extends SpringAndroidSpiceRequest<FacilityWithDistanceListResponse> {

	private PieceOfFurniture pieceOfFurniture;
	private double longitude;
	private double latitude;

	public FacilitiesWithDistanceGetByPieceOfItemsNeededRequest(PieceOfFurniture pieceOfFurniture, double latitude, double longitude) {
		super(FacilityWithDistanceListResponse.class);
		this.pieceOfFurniture = pieceOfFurniture;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public FacilityWithDistanceListResponse loadDataFromNetwork() throws RestClientException {
		return getRestTemplate().getForObject(
				JSONService.getServiceUrl("/facilities/findByPieceOfFurnitureNeededWithDistanceFrom/" + pieceOfFurniture.getId() + "?latitude=" + latitude + "&longitude="
						+ longitude + "&"), FacilityWithDistanceListResponse.class);
	}

	public String createCacheKey() {
		return "facilitiesByPieceOfItemsNeeded." + pieceOfFurniture.getId();
	}

}
