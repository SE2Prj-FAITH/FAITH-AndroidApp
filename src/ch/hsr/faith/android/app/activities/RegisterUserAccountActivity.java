package ch.hsr.faith.android.app.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.domain.UserAccount;
import ch.hsr.faith.android.app.services.UserAccountService;

public class RegisterUserAccountActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeruseraccount);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registeruseraccount, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void registerButtonClicked(View view) {
		String userName = ((EditText) findViewById(R.id.editUserAccountUserName)).getText().toString();
		String email = ((EditText) findViewById(R.id.editUserAccountEmail)).getText().toString();
		String password = ((EditText) findViewById(R.id.editUserAccountPasswort)).getText().toString();

		UserAccount user = new UserAccount();
		user.setUserName(userName);
		user.setEmail(email);
		user.setPassword(password);

		UserAccountService service = new UserAccountService();
		service.login(user);
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
