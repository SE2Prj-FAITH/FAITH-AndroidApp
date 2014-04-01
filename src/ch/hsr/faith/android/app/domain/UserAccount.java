package ch.hsr.faith.android.app.domain;

import java.io.Serializable;

public class UserAccount implements Serializable {

	private static final long serialVersionUID = 4980954194729732034L;
	
	Long id;
	String userName;
	String email;
	String password;

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
