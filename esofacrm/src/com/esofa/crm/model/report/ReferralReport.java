package com.esofa.crm.model.report;

import java.io.Serializable;

import com.googlecode.objectify.Key;

public class ReferralReport<T> implements Serializable {

	private static final long serialVersionUID = -870121085956921257L;
	
	
	private Key<T> referralKey;
	private int count;
	
	public ReferralReport() {}
	
	
	public ReferralReport(Key<T> referralKey, int count) {
		super();
		this.referralKey = referralKey;
		this.count = count;
	}

	public Key<T> getReferralKey() {
		return referralKey;
	}
	public void setReferralKey(Key<T> referralKey) {
		this.referralKey = referralKey;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}
