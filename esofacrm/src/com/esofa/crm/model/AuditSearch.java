package com.esofa.crm.model;

import java.io.Serializable;

import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;

public class AuditSearch implements Serializable {

	private static final long serialVersionUID = -9161101439886263427L;
	

	private Integer numDays = 14;
	private Key<CrmUser> user;
	private String entryType;
	
	
	public String getEntryType() {
		return entryType;
	}
	
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	
	public Integer getNumDays() {
		return numDays;
	}
	public void setNumDays(Integer numDays) {
		this.numDays = numDays;
	}

	public Key<CrmUser> getUser() {
		return user;
	}
	
	public void setUser(Key<CrmUser> user) {
		this.user = user;
	}
	

}
