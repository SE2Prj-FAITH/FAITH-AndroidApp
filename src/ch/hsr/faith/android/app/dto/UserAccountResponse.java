package ch.hsr.faith.android.app.dto;

import ch.hsr.faith.domain.UserAccount;

public class UserAccountResponse extends BaseJSONResponse<UserAccount> {

	private UserAccount data;

	public UserAccount getData() {
		return data;
	}

}