package ch.hsr.faith.android.app.services;

import ch.hsr.faith.android.app.dto.UserAccountResponse;
import ch.hsr.faith.android.app.util.PropertyReader;
import ch.hsr.faith.domain.UserAccount;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class RegisterUserAccountRequest extends SpringAndroidSpiceRequest<UserAccountResponse> {

	private UserAccount userAccount;

	public RegisterUserAccountRequest(UserAccount userAccount) {
		super(UserAccountResponse.class);
		this.userAccount = userAccount;
	}

	@Override
	public UserAccountResponse loadDataFromNetwork() throws Exception {
		String url = PropertyReader.getProperty(JSONService.SERVICE_BASE_URL_PROPERTY_KEY) + "/useraccount/register";
		return getRestTemplate().postForObject(url, userAccount, UserAccountResponse.class);
	}

	public String createCacheKey() {
		return "registerUserAccount";
	}
	
}
