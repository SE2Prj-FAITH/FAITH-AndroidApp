package ch.hsr.faith.android.app.activities;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.dto.LoginUserAccountResponse;
import ch.hsr.faith.android.app.dto.UserAccountResponse;
import ch.hsr.faith.android.app.services.LoginUserAccountRequest;
import ch.hsr.faith.domain.UserAccount;

public class LoginUserAccountActivity extends BaseActivity {

	private TextView failuresTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_user_account);
		

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
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

		UserAccount user = new UserAccount();
		user.setEmail(email);
		user.setPassword(password);

		
		LoginUserAccountRequest request = new LoginUserAccountRequest(user);
		spiceManager.execute(request, new LoginUserAccountRequestListener(this));
	}

	private class LoginUserAccountRequestListener extends BaseRequestListener<LoginUserAccountResponse, String> {

		public LoginUserAccountRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}


		@Override
		protected void handleFailures(List<String> failures) {
			String failureText = new String();
			for (String string : failures) {
				failureText = failureText + string + "\n";
			}
			failuresTextView.setText(failureText);
			failuresTextView.setVisibility(TextView.VISIBLE);
		}

		@Override
		protected void handleSuccess(String data) {
			Intent intent = new Intent(baseActivity, MainActivity.class);
			intent.putExtra("LogedInUserAccount", data);
			startActivity(intent);
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
