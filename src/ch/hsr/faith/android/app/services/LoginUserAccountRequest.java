package ch.hsr.faith.android.app.services;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import ch.hsr.faith.android.app.dto.LoginUserAccountResponse;
import ch.hsr.faith.domain.UserAccount;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class LoginUserAccountRequest extends
		SpringAndroidSpiceRequest<LoginUserAccountResponse> {

	private String user;
	private String password;

	public LoginUserAccountRequest(UserAccount user) {
		super(LoginUserAccountResponse.class);
		this.user = user.getEmail();
		this.password = user.getPassword();
	}

	@Override
	public LoginUserAccountResponse loadDataFromNetwork() throws Exception {
		HttpAuthentication authHeader = new HttpBasicAuthentication(user,
				password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

		return getRestTemplate().exchange(
				JSONService.getServiceUrl("/useraccount/login"),
				HttpMethod.GET, requestEntity,
				LoginUserAccountResponse.class).getBody();
	}

	public String createCacheKey() {
		return "LoginUserAccount";
	}

}
