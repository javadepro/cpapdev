package com.esofa.crm.security.user.model;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;

@Entity
public class UserPasscode implements Serializable {

	private static final long serialVersionUID = -8113810666304918388L;
	
	@Id
	private Long id;
	
	private Key<CrmUser> crmUser;
	private String passCode;
	
	@Ignore
	private String reTypePassCode;
	
	public Key<CrmUser> getCrmUser() {
		return crmUser;
	}
	public void setCrmUser(Key<CrmUser> crmUser) {
		this.crmUser = crmUser;
	}
	public String getPassCode() {
		return passCode;
	}
	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReTypePassCode() {
		return reTypePassCode;
	}
	public void setReTypePassCode(String reTypePassCode) {
		this.reTypePassCode = reTypePassCode;
	}
}
