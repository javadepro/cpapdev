package com.esofa.crm.messenger.model;

import java.io.Serializable;

import com.esofa.crm.security.user.model.CrmUser;

public class MessageWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8258805863518374511L;
	
	private Object message;
	private CrmUser crmUser;

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public CrmUser getCrmUser() {
		return crmUser;
	}

	public void setCrmUser(CrmUser crmUser) {
		this.crmUser = crmUser;
	}
	
	
	
}
