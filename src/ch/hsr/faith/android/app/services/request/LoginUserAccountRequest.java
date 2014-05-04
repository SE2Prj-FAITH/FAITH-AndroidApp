package ch.hsr.faith.android.app.services.request;

import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.services.response.LoginUserAccountResponse;
import ch.hsr.faith.android.app.util.Login;

public class LoginUserAccountRequest extends
		AuthenticatedRequest<LoginUserAccountResponse> {

	public LoginUserAccountRequest(Login user) {
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
