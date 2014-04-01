package ch.hsr.faith.android.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import ch.hsr.faith.android.app.services.JSONService;

import com.octo.android.robospice.SpiceManager;

public class BaseActivity extends Activity {

	protected SpiceManager spiceManager = new SpiceManager(JSONService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable throwable) {
				showErrorDialog(throwable.getMessage());
			}
		});
	}

	public void showErrorDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog = builder.create();
		dialog.setTitle("Error");
		dialog.setMessage("Error orrured: " + message);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.show();
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
