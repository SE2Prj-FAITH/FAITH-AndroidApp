package ch.hsr.faith.android.app.activities;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.listeners.BaseRequestListener;
import ch.hsr.faith.android.app.services.request.RegisterUserAccountRequest;
import ch.hsr.faith.android.app.services.response.UserAccountResponse;
import ch.hsr.faith.domain.UserAccount;

public class RegisterUserAccountActivity extends BaseActivity {

	private TextView failuresTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeruseraccount);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		failuresTextView = (TextView) findViewById(R.id.RegisterUserFailures);
	}

	public void registerButtonClicked(View view) {
		String email = ((EditText) findViewById(R.id.editUserAccountEmail)).getText().toString();
		String password = ((EditText) findViewById(R.id.editUserAccountPasswort)).getText().toString();

		UserAccount user = new UserAccount();
		user.setEmail(email);
		user.setPassword(password);

		failuresTextView.setText("");
		failuresTextView.setVisibility(TextView.INVISIBLE);

		RegisterUserAccountRequest request = new RegisterUserAccountRequest(user);
		spiceManager.execute(request, new RegisterUserAccountRequestListener(this));
	}

	private class RegisterUserAccountRequestListener extends BaseRequestListener<UserAccountResponse, UserAccount> {

		public RegisterUserAccountRequestListener(BaseActivity baseActivity) {
			super(baseActivity);
		}

		@Override
		protected void handleSuccess(UserAccount userAccount) {
			Intent intent = new Intent(baseActivity, RegisterUserAccountConfirmationActivity.class);
			intent.putExtra("registeredUserAccount", userAccount);
			startActivity(intent);
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
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_registeruseraccount, container, false);
			return rootView;
		}
	}

}