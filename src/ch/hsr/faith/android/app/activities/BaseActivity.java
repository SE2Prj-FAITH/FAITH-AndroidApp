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
import ch.hsr.faith.domain.UserAccount;

import com.octo.android.robospice.SpiceManager;

public class BaseActivity extends Activity {

	protected SpiceManager spiceManager = new SpiceManager(JSONService.class);
	protected SharedPreferences loginData;
	protected String faithLoginEmailPreferenceName = "LOGIN_EMAIL";
	protected String faithLoginPasswordPreferenceName = "LOGIN_PASSWORD";

	private ProgressDialog requestProgressDialog;
	
	protected UserAccount getUserAccount() {
		UserAccount authUser = new UserAccount();
		authUser.setEmail(getUserEmail());
		authUser.setPassword(getUserPassword());
		return authUser;
	}

	protected String getUserEmail() {
		return loginData.getString(faithLoginEmailPreferenceName, null);
	}

	protected String getUserPassword() {
		return loginData.getString(faithLoginPasswordPreferenceName, null);
	}
	
	protected boolean isLoggedIn() {
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable throwable) {
				showErrorDialog(throwable.getMessage());
			}
		});
		loginData = getSharedPreferences("FAIHT-LOGIN-DATE", 0);
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
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_about:
			onAboutItemClick(item);
			return true;
		case R.id.action_settings:
			onSettingsItemClick(item);
			return true;
		case R.id.action_registeruseraccount:
			onRegisterItemClick(item);
			return true;
		case R.id.action_loginuseraccount:
			onLoginItemClick(item);
			return true;
		case R.id.action_facilities_management:
			onFacilitiesItemClick(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onLoginItemClick(MenuItem item) {
		Intent intent = new Intent(this.getBaseContext(), LoginUserAccountActivity.class);
		startActivity(intent);
	}

	private void onSettingsItemClick(MenuItem item) {
		Intent intent = new Intent(this.getBaseContext(), SettingsActivity.class);
		startActivity(intent);
	}

	private void onAboutItemClick(MenuItem item) {
		Intent intent = new Intent(this.getBaseContext(), AboutFaithActivity.class);
		startActivity(intent);
	}

	private void onRegisterItemClick(MenuItem mi) {
		Intent intent = new Intent(this.getBaseContext(), RegisterUserAccountActivity.class);
		startActivity(intent);
	}

	private void onFacilitiesItemClick(MenuItem mi) {
		Intent intent = new Intent(this.getBaseContext(), FacilitiesManagementActivity.class);
		startActivity(intent);
	}

	public void showErrorDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog = builder.create();
		dialog.setTitle("Error");
		dialog.setMessage("Error orrured: " + message);
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
