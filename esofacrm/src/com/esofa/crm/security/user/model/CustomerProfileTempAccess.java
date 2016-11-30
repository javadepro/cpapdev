package com.esofa.crm.security.user.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.esofa.crm.model.Customer;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class CustomerProfileTempAccess implements Serializable{
	private static final long serialVersionUID = 8252653620094137857L;
	
	public CustomerProfileTempAccess(){
		expiration = getDateAfterNDays(5);
	}

	@Id
	Long id;
	
	@NotNull
	Date expiration;
	
	Key<CrmUser> grantedBy;
	
	@NotNull
	Key<CrmUser> crmUser;
	
	@NotNull
	Key<Customer> customer;
	String note;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public Key<CrmUser> getGrantedBy() {
		return grantedBy;
	}
	public void setGrantedBy(Key<CrmUser> grantedBy) {
		this.grantedBy = grantedBy;
	}
	public Key<CrmUser> getCrmUser() {
		return crmUser;
	}
	public void setCrmUser(Key<CrmUser> crmUser) {
		this.crmUser = crmUser;
	}
	public Key<Customer> getCustomer() {
		return customer;
	}
	public void setCustomer(Key<Customer> customer) {
		this.customer = customer;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	private Date getDateAfterNDays(int day){
		Calendar c = Calendar.getInstance();
		//c.setTime(date);
		c.add(Calendar.DATE, day);
		return c.getTime();
	}
}
