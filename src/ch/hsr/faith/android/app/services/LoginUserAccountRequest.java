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
		AuthenticatedRequest<LoginUserAccountResponse> {

	public LoginUserAccountRequest(UserAccount user) {
		super(user, LoginUserAccountResponse.class);
	}

	@Override
	public LoginUserAccountResponse loadDataFromNetwork() throws Exception {
		return loadDataFromGetRequest(JSONService.getServiceUrl("/useraccount/login"));
	}

	public String createCacheKey() {
		return "LoginUserAccount";
	}

}
