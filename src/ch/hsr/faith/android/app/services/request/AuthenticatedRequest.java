package ch.hsr.faith.android.app.services.request;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import ch.hsr.faith.domain.UserAccount;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public abstract class AuthenticatedRequest<ResponseType> extends SpringAndroidSpiceRequest<ResponseType> {
	private UserAccount credentials;
	private Class<ResponseType> responseType;

	public AuthenticatedRequest(UserAccount credentials, Class<ResponseType> responseType) {
		super((Class<ResponseType>) responseType);
		this.responseType = responseType;
		this.credentials = credentials;
	}

	public ResponseType loadDataFromGetRequest(String url) throws Exception {
		ResponseEntity<ResponseType> response = getRestTemplate().exchange(url, HttpMethod.GET, getRequestEntity(), ((Class<ResponseType>) responseType));
		return response.getBody();
	}

	public ResponseType loadDataFromPostRequest(String url, Object body) throws Exception {
		ResponseEntity<ResponseType> response = getRestTemplate().exchange(url, HttpMethod.POST, getRequestEntity(body), ((Class<ResponseType>) responseType));
		return response.getBody();
	}

	private HttpEntity<Object> getRequestEntity() {
		return getRequestEntity(null);
	}

	private HttpEntity<Object> getRequestEntity(Object body) {
		HttpAuthentication authHeader = new HttpBasicAuthentication(credentials.getEmail(), credentials.getPassword());
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, requestHeaders);
		return requestEntity;
	}
}