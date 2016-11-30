package com.esofa.crm.model.report;

import java.io.Serializable;

public class ReferralSearch implements Serializable {

	private static final long serialVersionUID = -6460192609418042957L;
	private int numMths;
	private String referralType;
	public int getNumMths() {
		return numMths;
	}
	public void setNumMths(int numMths) {
		this.numMths = numMths;
	}
	public String getReferralType() {
		return referralType;
	}
	public void setReferralType(String referralType) {
		this.referralType = referralType;
	}
	
	
}
