package ch.hsr.faith.android.app.activities.listeners;

import java.util.List;
import ch.hsr.faith.android.app.R;
import android.widget.Toast;
import ch.hsr.faith.android.app.activities.BaseActivity;
import ch.hsr.faith.android.app.services.response.BaseJSONResponse;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public abstract class BaseRequestListener<T extends BaseJSONResponse<S>, S> implements RequestListener<T> {

	protected BaseActivity baseActivity;

	public BaseRequestListener(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
		showRequestProgressDialogOnGUI();
	}

	public void onRequestFailure(SpiceException spiceException) {
		if (isErrorAnAuthenticationFailure(spiceException)) {
			handleAuthenticationFailure();
		} else {
			baseActivity.showErrorDialog(spiceException.getMessage());
		}
		hideRequestProgressDialogOnGUI();
	}

	public void onRequestSuccess(T result) {
		if (result.getStatus().equals(BaseJSONResponse.STATUS_ERROR)) {
			baseActivity.showErrorDialog(result.getErrorMessage());
		} else if (result.getStatus().equals(BaseJSONResponse.STATUS_FAIL)) {
			handleFailures(result.getFailures());
		} else if (result.getStatus().equals(BaseJSONResponse.STATUS_SUCCESS)) {
			handleSuccess(result.getData());
		} else {
			baseActivity.showErrorDialog(baseActivity.getText(R.string.service_return_status_unknown).toString());
		}
		hideRequestProgressDialogOnGUI();
	}

	private boolean isErrorAnAuthenticationFailure(SpiceException spiceException) {
		return spiceException.getCause().getMessage().equals("401 Unauthorized");
	}

	protected abstract void handleSuccess(S data);

	protected void handleFailures(List<String> failures) {
		// By default, failures are not handled. Override this method to handle!
	}

	protected void handleAuthenticationFailure() {
		// By default, if there is a login failure, app shows a text 'invalid
		// credentials'. Override this method to handle more specific!
		Toast.makeText(baseActivity.getApplicationContext(), baseActivity.getString(ch.hsr.faith.android.app.R.string.authentication_message_failure), Toast.LENGTH_LONG).show();
	}

	private void showRequestProgressDialogOnGUI() {
		baseActivity.showRequestProgressDialog(baseActivity.getString(ch.hsr.faith.android.app.R.string.request_progress_dialog_loading));
	}

	private void hideRequestProgressDialogOnGUI() {
		baseActivity.hideRequestProgressDialog();
	}

}
