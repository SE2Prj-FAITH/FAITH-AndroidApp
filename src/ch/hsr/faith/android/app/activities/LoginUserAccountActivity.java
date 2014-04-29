package ch.hsr.faith.android.app.activities;

import android.app.Fragment;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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
		EditText emailField = ((EditText) findViewById(R.id.EditTextEmail));
		EditText passwordField = ((EditText) findViewById(R.id.EditTextPassword));

		if(getLoginObject().isAuthenticated()) { 
			emailField.setText(getLoginObject().getEmail());
			passwordField.setText("");
			passwordField.requestFocus();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void LoginButtonClicked(View view) {
		String email = ((EditText) findViewById(R.id.EditTextEmail)).getText().toString();
		String password = ((EditText) findViewById(R.id.EditTextPassword)).getText().toString();

		Login login = new Login(email, password);

		storeCredentialsOnSharedMemory(login);

		LoginUserAccountRequest request = new LoginUserAccountRequest(login);
		spiceManager.execute(request, new LoginUserAccountRequestListener(this));
	}

	private void storeCredentialsOnSharedMemory(Login login) {
		Editor editor = loginData.edit();
		editor.putString(faithLoginEmailPreferenceName, login.getEmail());
		editor.putString(faithLoginPasswordPreferenceName, login.getPassword());
		editor.apply();
	}

	private class LoginUserAccountRequestListener extends BaseRequestListener<LoginUserAccountResponse, String> {

		public LoginUserAccountRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleAuthenticationFailure() {
			super.handleAuthenticationFailure();
			EditText passwordField = ((EditText) findViewById(R.id.EditTextPassword));
			passwordField.setText("");
			passwordField.requestFocus();
		}

		@Override
		protected void handleSuccess(String data) {
			Toast.makeText(getApplicationContext(), getString(R.string.authentication_message_success), Toast.LENGTH_LONG).show();
			finish();
		}
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
