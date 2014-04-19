package ch.hsr.faith.android.app.services;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import ch.hsr.faith.domain.UserAccount;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class AuthenticatedRequest<ResponseType> extends
		SpringAndroidSpiceRequest<ResponseType> {
	private UserAccount credentials;
	private Class<ResponseType> responseType;

	public AuthenticatedRequest(UserAccount credentials, Class<ResponseType> responseType) {
		super((Class<ResponseType>) responseType);
		this.responseType = responseType;
		this.credentials = credentials;
	}

	public ResponseType loadDataFromGetRequest(String url)
			throws Exception {
 
		ResponseEntity<ResponseType> response = getRestTemplate().exchange(url,
				HttpMethod.GET, getRequestEntity(), ((Class<ResponseType>) responseType));
		return response.getBody();
	}
	
	public ResponseType loadDataFromPostRequest(String url, Object body)
			throws Exception {
 
		ResponseEntity<ResponseType> response = getRestTemplate().exchange(url,
				HttpMethod.POST, getRequestEntity(), ((Class<ResponseType>) responseType), body);
		return response.getBody();
	}

	private HttpEntity<Object> getRequestEntity() {

		HttpAuthentication authHeader = new HttpBasicAuthentication(
				credentials.getEmail(), credentials.getPassword());
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(
				requestHeaders);
		return requestEntity;
	}

	@Override
	public ResponseType loadDataFromNetwork() throws Exception {
		// FIXME mvetsch :  This method must be overwritten by the subclass
		// 					I guess this is ugly!
		throw new RuntimeException("This method is not intended to call, must be overwritten.");
	}

}