package com.esofa.crm.model;

import java.io.Serializable;

import javax.validation.Valid;

public class CustomerWrapper implements Serializable  {

	private static final long serialVersionUID = 3424205903204158825L;
	
	@Valid
	Customer customer;
	@Valid
	CustomerExtendedInfo customerExtendedInfo;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public CustomerExtendedInfo getCustomerExtendedInfo() {
		return customerExtendedInfo;
	}
	public void setCustomerExtendedInfo(CustomerExtendedInfo customerExtendedInfo) {
		this.customerExtendedInfo = customerExtendedInfo;
	}
	
	
	
}
