package ch.hsr.faith.android.app.util;

public class Login {
	
	String email, password;
	String sessionId;
	
	public Login(String email, String password) { 
		this.email = email;
		this.password = password;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
	
	public boolean isAuthenticated() { 
		return email != null && password != null;
	}

}
