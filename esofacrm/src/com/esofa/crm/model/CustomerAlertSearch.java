package com.esofa.crm.model;

import java.io.Serializable;

import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;

public class CustomerAlertSearch  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer numDays = 14;
	private Long customerId = null;
	private Key<CrmUser> clinician = null;
	private Key<AlertSubType> alertSubType = null;
	
	public Integer getNumDays() {
		return numDays;
	}
	public void setNumDays(Integer numDays) {
		this.numDays = numDays;
	}
	public Key<CrmUser> getClinician() {
		return clinician;
	}
	public void setClinician(Key<CrmUser> clinician) {
		this.clinician = clinician;
	}
	
	public Key<AlertSubType> getAlertSubType() {
		return alertSubType;
	}
	public void setAlertSubType(Key<AlertSubType> alertSubType) {
		this.alertSubType = alertSubType;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
	
}
