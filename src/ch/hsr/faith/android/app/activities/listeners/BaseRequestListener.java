package ch.hsr.faith.android.app.activities.listeners;

import java.util.List;

import ch.hsr.faith.android.app.activities.BaseActivity;
import ch.hsr.faith.android.app.dto.BaseJSONResponse;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public abstract class BaseRequestListener<T extends BaseJSONResponse<R>, R> implements RequestListener<T> {

	protected BaseActivity baseActivity;

	public BaseRequestListener(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
	}

	public void onRequestFailure(SpiceException spiceException) {
		baseActivity.showErrorDialog(spiceException.getMessage());
	}

	public void onRequestSuccess(T result) {
		if (result.getStatus().equals(BaseJSONResponse.STATUS_ERROR)) {
			baseActivity.showErrorDialog(result.getErrorMessage());
		} else if (result.getStatus().equals(BaseJSONResponse.STATUS_FAIL)) {
			handleFailures(result.getFailures());
		} else if (result.getStatus().equals(BaseJSONResponse.STATUS_SUCCESS)) {
			handleSuccess(result.getData());
		} else {
			baseActivity.showErrorDialog("Service returned unknown response status!");
		}
	}

	protected abstract void handleSuccess(R data);

	protected void handleFailures(List<String> failures) {
		// By default, failures are not handled. Override this method to handle!
	}

}
