package com.esofa.crm.model;

import java.io.Serializable;
import java.util.Date;

import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerAlert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id 
	Long id;
    
	@Parent
	private Key<Customer> customer;
	
	private String message;
    private String createdBy;
    
    @Index
    private Date alertDate;
  
    @Index
    private Key<AlertSubType> alertSubType;
    
    // Note: not restricted to clinician. Any CrmUser can be assigned
    @Index
    private Key<CrmUser> clinician;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
	public Key<AlertSubType> getAlertSubType() {
		return alertSubType;
	}
	public void setAlertSubType(Key<AlertSubType> alertSubType) {
		this.alertSubType = alertSubType;
	}
	
	public Key<CrmUser> getClinician() {
		return clinician;
	}
	public void setClinician(Key<CrmUser> clinician) {
		this.clinician = clinician;
	}

	public Key<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(Key<Customer> customer) {
		this.customer = customer;
	}	
}
