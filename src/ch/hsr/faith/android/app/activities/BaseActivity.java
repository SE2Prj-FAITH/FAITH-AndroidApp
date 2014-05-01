package ch.hsr.faith.android.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.util.Login;

import com.octo.android.robospice.SpiceManager;

public abstract class BaseActivity extends Activity {

	protected SpiceManager spiceManager = new SpiceManager(JSONService.class);
	protected SharedPreferences loginData;
	protected static final String PREFERENCE_NAME_LOGIN_EMAIL = "LOGIN_EMAIL";
	protected static final String PREFERENCE_NAME_LOGIN_PASWORD = "LOGIN_PASSWORD";
	protected static final String PREFERENCE_NAME_LOGIN = "FAITH_LOGIN";

	private Login loginObject;
	private ProgressDialog requestProgressDialog;

	protected Login getLoginObject() {
		if (loginObject == null) {
			String email = loginData.getString(PREFERENCE_NAME_LOGIN_EMAIL, null);
			String password = loginData.getString(PREFERENCE_NAME_LOGIN_PASWORD, null);
			loginObject = new Login(email, password);
		}
		return loginObject;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable throwable) {
				showErrorDialog(throwable.getMessage());
			}
		});
		loginData = getSharedPreferences(PREFERENCE_NAME_LOGIN, 0);
		requestProgressDialog = new ProgressDialog(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			startActivity(AboutFaithActivity.class);
			return true;
		case R.id.action_settings:
			startActivity(SettingsActivity.class);
			return true;
		case R.id.action_registeruseraccount:
			startActivity(RegisterUserAccountActivity.class);
			return true;
		case R.id.action_loginuseraccount:
			startActivity(LoginUserAccountActivity.class);
			return true;
		case R.id.action_facilities_management:
			startActivity(FacilitiesManagementActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void startActivity(Class<?> activityClass) {
		Intent intent = new Intent(this.getBaseContext(), activityClass);
		startActivity(intent);
	}

	public void showErrorDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog = builder.create();
		dialog.setTitle(R.string.error);
		dialog.setMessage(R.string.error_occured_message + message);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.show();
	}

	public void showRequestProgressDialog(String message) {
		requestProgressDialog.setMessage(message);
		requestProgressDialog.setCancelable(false);
		requestProgressDialog.show();
	}

	public void hideRequestProgressDialog() {
		if (requestProgressDialog.isShowing())
			requestProgressDialog.dismiss();
	}

	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}
}
