package com.esofa.crm.messenger.model;

import java.io.Serializable;

import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;

public class WorkPackage<T extends Serializable, S extends Serializable> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2310035758306996188L;
	
	private CrmUser initiator;
	private Key<CrmUser> initiatorKey;
	private Key<T> target;
	private S before;
	private S after;
	private String resources;
	private String attribute;
	private String attribute2;
	private Integer dateOffset;
	
	public WorkPackage(CrmUser initiator, Key<T> target, S before, S after){
		this.initiator = initiator;
		this.target = target;
		this.after = after;
		this.before = before;
		dateOffset  = 0;
	}
	
	public WorkPackage(Key<CrmUser> initiatorKey, Key<T> target, S before, S after){
		this.initiatorKey = initiatorKey;
		this.target = target;
		this.after = after;
		this.before = before;
		dateOffset  = 0;
	}
	
	
	public WorkPackage(){}
	
	public CrmUser getInitiator() {
		return initiator;
	}
	public void setInitiator(CrmUser initiator) {
		this.initiator = initiator;
	}
	
	public Key<CrmUser> getInitiatorKey() {
		return initiatorKey;
	}
	
	public Key<T> getTarget() {
		return target;
	}
	public void setTarget(Key<T> target) {
		this.target = target;
	}
	public Object getBefore() {
		return before;
	}
	public void setBefore(S before) {
		this.before = before;
	}
	public Object getAfter() {
		return after;
	}
	public void setAfter(S after) {
		this.after = after;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	
	public void setDateOffset(Integer dateOffset) {
		this.dateOffset = dateOffset;
	}
	
	public Integer getDateOffset() {
		return dateOffset;
	}
	
}
