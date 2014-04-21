package ch.hsr.faith.android.app.services.response;

import java.util.List;

public abstract class BaseJSONResponse<R> {

	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAIL = "fail";
	public static final String STATUS_ERROR = "error";

	private String status;
	private String errorMessage;
	private List<String> failures;

	public String getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public List<String> getFailures() {
		return failures;
	}

	public abstract R getData();

}
