package ch.hsr.faith.android.app.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.services.request.LoginUserAccountRequest;
import ch.hsr.faith.android.app.services.response.LoginUserAccountResponse;
import ch.hsr.faith.android.app.util.Login;

public class LoginUserAccountActivity extends BaseActivity {
	EditText emailField;
	EditText passwordField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_user_account);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		emailField = ((EditText) findViewById(R.id.EditTextEmail));
		passwordField = ((EditText) findViewById(R.id.EditTextPassword));

		if (getLoginObject().isAuthenticated()) {
			emailField.setText(getLoginObject().getEmail());
			passwordField.setText("");
			passwordField.requestFocus();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void LoginButtonClicked(View view) {
		if (!isInputValid()) {
			return;
		}

		Login login = new Login(emailField.getText().toString(), passwordField.getText().toString());

		storeCredentialsOnSharedMemory(login);

		LoginUserAccountRequest request = new LoginUserAccountRequest(login);
		spiceManager.execute(request, new LoginUserAccountRequestListener(this));
	}

	public void RegisterButtonClicked(View view) {
		Intent intent = new Intent(this, RegisterUserAccountActivity.class);
		startActivity(intent);
	}

	public void CancelButtonClicked(View view) {
		Intent intent = new Intent(this, FurnitureMainActivity.class);
		startActivity(intent);
	}

	private void storeCredentialsOnSharedMemory(Login login) {
		Editor editor = loginData.edit();
		editor.putString(PREFERENCE_NAME_LOGIN_EMAIL, login.getEmail());
		editor.putString(PREFERENCE_NAME_LOGIN_PASWORD, login.getPassword());
		editor.apply();
		clearLoginObject();
	}

	private void removePasswordFromSharedPreferences() {
		Editor editor = loginData.edit();
		editor.remove(PREFERENCE_NAME_LOGIN_PASWORD);
		editor.apply();
		clearLoginObject();
	}

	private class LoginUserAccountRequestListener extends BaseRequestListener<LoginUserAccountResponse, String> {

		public LoginUserAccountRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleAuthenticationFailure() {
			EditText passwordField = ((EditText) findViewById(R.id.EditTextPassword));
			passwordField.setText("");
			passwordField.requestFocus();

			removePasswordFromSharedPreferences();
		}

		@Override
		protected void handleSuccess(String data) {
			Toast.makeText(getApplicationContext(), getString(R.string.authentication_message_success), Toast.LENGTH_LONG).show();
			finish();
		}
	}

	private boolean isInputValid() {
		CharSequence email = emailField.getText();
		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			emailField.setError(getString(R.string.user_input_validation_message_invalid_email_address));
			return false;
		}
		if (passwordField.getText().length() < 4) {
			passwordField.setError(getString(R.string.user_input_validation_message_invalid_password));
			return false;
		}
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login_user_account, container, false);
			return rootView;
		}
	}

}
