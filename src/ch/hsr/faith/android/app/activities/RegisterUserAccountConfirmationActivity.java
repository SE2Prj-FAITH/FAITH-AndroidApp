package ch.hsr.faith.android.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.domain.UserAccount;

public class RegisterUserAccountConfirmationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeruseraccount_confirmation);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		UserAccount userAccount = (UserAccount) intent.getExtras().get("registeredUserAccount");
		fillData(userAccount);
	}

	private void fillData(UserAccount userAccount) {
		TextView idTextView = (TextView) findViewById(R.id.textViewUserAccountUserId);
		TextView emailTextView = (TextView) findViewById(R.id.textViewUserAccountEmail);

		idTextView.setText(userAccount.getId().toString());
		emailTextView.setText(userAccount.getEmail());
	}

}
