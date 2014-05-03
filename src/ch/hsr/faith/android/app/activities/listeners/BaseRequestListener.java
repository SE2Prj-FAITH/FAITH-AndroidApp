package ch.hsr.faith.android.app.activities.listeners;

import java.util.List;

import android.content.Intent;
import android.widget.Toast;
import ch.hsr.faith.android.app.activities.BaseActivity;
import ch.hsr.faith.android.app.activities.LoginUserAccountActivity;
import ch.hsr.faith.android.app.services.response.BaseJSONResponse;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public abstract class BaseRequestListener<T extends BaseJSONResponse<R>, R> implements RequestListener<T> {

	protected BaseActivity activityRequesting;

	public BaseRequestListener(BaseActivity activityRequesting) {
		this.activityRequesting = activityRequesting;
		showRequestProgressDialogOnGUI();
	}

	public void onRequestFailure(SpiceException spiceException) {
		if (isErrorAnAuthenticationFailure(spiceException)) {
			handleAuthenticationFailure();
		} else {
			activityRequesting.showErrorDialog(spiceException.getMessage());
		}
		hideRequestProgressDialogOnGUI();
	}

	public void onRequestSuccess(T result) {
		if (result.getStatus().equals(BaseJSONResponse.STATUS_ERROR)) {
			activityRequesting.showErrorDialog(result.getErrorMessage());
		} else if (result.getStatus().equals(BaseJSONResponse.STATUS_FAIL)) {
			handleFailures(result.getFailures());
		} else if (result.getStatus().equals(BaseJSONResponse.STATUS_SUCCESS)) {
			handleSuccess(result.getData());
		} else {
			activityRequesting.showErrorDialog("Service returned unknown response status!");
		}
		hideRequestProgressDialogOnGUI();
	}

	private boolean isErrorAnAuthenticationFailure(SpiceException spiceException) {
		return spiceException.getCause().getMessage().equals("401 Unauthorized");
	}

	protected abstract void handleSuccess(R data);

	protected void handleFailures(List<String> failures) {
		// By default, failures are not handled. Override this method to handle!
	}
	
	protected void handleAuthenticationFailure() {
		Intent intent = new Intent(activityRequesting, LoginUserAccountActivity.class);
		activityRequesting.startActivity(intent);
		Toast.makeText(activityRequesting.getApplicationContext(), activityRequesting.getString(ch.hsr.faith.android.app.R.string.authentication_message_failure), Toast.LENGTH_LONG).show();
	}

	private void showRequestProgressDialogOnGUI() {
		activityRequesting.showRequestProgressDialog(activityRequesting.getString(ch.hsr.faith.android.app.R.string.request_progress_dialog_loading));
	}

	private void hideRequestProgressDialogOnGUI() {
		activityRequesting.hideRequestProgressDialog();
	}

}
