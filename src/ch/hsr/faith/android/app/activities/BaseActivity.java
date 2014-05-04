package ch.hsr.faith.android.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.constants.MenuItems;
import ch.hsr.faith.android.app.services.JSONService;
import ch.hsr.faith.android.app.util.Login;

import com.octo.android.robospice.SpiceManager;

public abstract class BaseActivity extends Activity {

	protected SpiceManager spiceManager = new SpiceManager(JSONService.class);
	protected SharedPreferences loginData;
	protected static final String PREFERENCE_NAME_LOGIN_EMAIL = "LOGIN_EMAIL";
	protected static final String PREFERENCE_NAME_LOGIN_PASWORD = "LOGIN_PASSWORD";
	protected static final String PREFERENCE_NAME_LOGIN = "FAITH_LOGIN";

	private static Login loginObject;
	private ProgressDialog requestProgressDialog;

	protected Login getLoginObject() {
		if (BaseActivity.loginObject == null) {
			String email = loginData.getString(PREFERENCE_NAME_LOGIN_EMAIL, null);
			String password = loginData.getString(PREFERENCE_NAME_LOGIN_PASWORD, null);
			BaseActivity.loginObject = new Login(email, password);
		}
		return BaseActivity.loginObject;
	}
	
	protected void clearLoginObject() {
		BaseActivity.loginObject = null;
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

	/**
	 * Creates MenuItem's dynamically, each time the users click's the menu
	 * button.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (getLoginObject().isAuthenticated()) {
			menu.add(0, MenuItems.MENU_LOGIN_LOGOUT, Menu.NONE, R.string.title_activity_logout_user_account);
		} else {
			menu.add(0, MenuItems.MENU_LOGIN_LOGOUT, Menu.NONE, R.string.title_activity_login_user_account);
		}
		menu.add(0, MenuItems.MENU_REGISTER, Menu.NONE, R.string.activity_register_user);
		if (getLoginObject().isAuthenticated()) {
			menu.add(0, MenuItems.MENU_FACILITIES_MANAGEMENT, Menu.NONE, R.string.action_facilities_management);
		}
		menu.add(0, MenuItems.MENU_SETTINGS, Menu.NONE, R.string.action_settings);
		menu.add(0, MenuItems.MENU_ABOUT, Menu.NONE, R.string.about_application);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MenuItems.MENU_LOGIN_LOGOUT:
			menuLoginLogoutClicked();
			return true;
		case MenuItems.MENU_REGISTER:
			startActivity(RegisterUserAccountActivity.class);
			return true;
		case MenuItems.MENU_FACILITIES_MANAGEMENT:
			startActivity(FacilitiesManagementActivity.class);
			return true;
		case MenuItems.MENU_SETTINGS:
			startActivity(SettingsActivity.class);
			return true;
		case MenuItems.MENU_ABOUT:
			startActivity(AboutFaithActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void startActivity(Class<?> activityClass) {
		Intent intent = new Intent(this.getBaseContext(), activityClass);
		startActivity(intent);
	}

	private void menuLoginLogoutClicked() {
		if (getLoginObject().isAuthenticated()) {
			Editor editor = loginData.edit();
			editor.remove(PREFERENCE_NAME_LOGIN_PASWORD);
			editor.apply();
			clearLoginObject();
			Toast.makeText(getApplicationContext(), getString(R.string.authentication_message_logout), Toast.LENGTH_LONG).show();
			startActivity(MainActivity.class);
		} else {
			startActivity(LoginUserAccountActivity.class);
		}
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
