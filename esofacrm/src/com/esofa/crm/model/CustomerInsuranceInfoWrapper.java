package com.esofa.crm.model;

import javax.validation.Valid;

public class CustomerInsuranceInfoWrapper {

	@Valid
	CustomerInsuranceInfoType1 self;
	
	@Valid
	CustomerInsuranceInfoType1 spouse;
	
	public CustomerInsuranceInfoType1 getSelf() {
		return self;
	}
	public void setSelf(CustomerInsuranceInfoType1 self) {
		this.self = self;
	}
	public CustomerInsuranceInfoType1 getSpouse() {
		return spouse;
	}
	public void setSpouse(CustomerInsuranceInfoType1 spouse) {
		this.spouse = spouse;
	}
	
}
