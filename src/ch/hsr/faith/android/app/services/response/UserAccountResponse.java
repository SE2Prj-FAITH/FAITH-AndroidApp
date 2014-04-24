package ch.hsr.faith.android.app.services.response;

import ch.hsr.faith.domain.UserAccount;

public class UserAccountResponse extends BaseJSONResponse<UserAccount> {

	private UserAccount data;

	public UserAccount getData() {
		return data;
	}

}
