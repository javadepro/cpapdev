package com.esofa.crm.model;

import javax.validation.Valid;

public class CustomerMedicalInfoWrapper {

	
	@Valid
	private CustomerMedicalInfo customerMedicalInfo;
	
	@Valid
	private CustomerCpapTrialInfo customerCpapTrialInfo;

	public CustomerMedicalInfoWrapper() {

		customerMedicalInfo = new CustomerMedicalInfo();
		customerCpapTrialInfo = new CustomerCpapTrialInfo();
	}
	
	public CustomerMedicalInfoWrapper(Customer customer) {

		this();		
		customerMedicalInfo.setCustomer(customer.getKey());
		customerCpapTrialInfo.setCustomer(customer.getKey());
	}
	
	public CustomerMedicalInfo getCustomerMedicalInfo() {
		return customerMedicalInfo;
	}

	public void setCustomerMedicalInfo(CustomerMedicalInfo customerMedicalInfo) {
		this.customerMedicalInfo = customerMedicalInfo;
	}

	public CustomerCpapTrialInfo getCustomerCpapTrialInfo() {
		return customerCpapTrialInfo;
	}

	public void setCustomerCpapTrialInfo(CustomerCpapTrialInfo customerCpapTrialInfo) {
		this.customerCpapTrialInfo = customerCpapTrialInfo;
	}
	
	
	
	
}
