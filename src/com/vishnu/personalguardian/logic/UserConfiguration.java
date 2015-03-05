package com.vishnu.personalguardian.logic;

import java.io.Serializable;

public class UserConfiguration implements Serializable {
	/**
	 * This class contains all user configuration settings including password and guardian details
	 */
	private static final long serialVersionUID = 1L;
	private String password;
	private String guardianName;
	private String guardianPhone;
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGuardianName() {
		return guardianName;
	}
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	public String getGuardianPhone() {
		return guardianPhone;
	}
	public void setGuardianPhone(String guardianPhone) {
		this.guardianPhone = guardianPhone;
	}
}
