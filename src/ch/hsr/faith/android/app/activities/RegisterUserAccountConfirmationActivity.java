package ch.hsr.faith.android.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.domain.UserAccount;

public class RegisterUserAccountConfirmationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeruseraccount_confirmation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registeruseraccount, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		UserAccount userAccount = (UserAccount) intent.getExtras().get("registeredUserAccount");
		fillData(userAccount);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void fillData(UserAccount userAccount) {
		TextView idTextView = (TextView) findViewById(R.id.textViewUserAccountUserId);
		TextView userNameTextView = (TextView) findViewById(R.id.textViewUserAccountUserName);
		TextView emailTextView = (TextView) findViewById(R.id.textViewUserAccountEmail);

		idTextView.setText(userAccount.getId().toString());
		userNameTextView.setText(userAccount.getUserName());
		emailTextView.setText(userAccount.getEmail());
	}

}
