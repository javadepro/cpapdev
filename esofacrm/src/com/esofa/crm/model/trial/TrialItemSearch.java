package com.esofa.crm.model.trial;

import java.io.Serializable;

import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;

public class TrialItemSearch  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Key<Shop> location;
	private String trialStatus;
	public Key<Shop> getLocation() {
		return location;
	}
	public void setLocation(Key<Shop> location) {
		this.location = location;
	}
	public String getTrialStatus() {
		return trialStatus;
	}
	public void setTrialStatus(String trialStatus) {
		this.trialStatus = trialStatus;
	}
	
	
	
}
