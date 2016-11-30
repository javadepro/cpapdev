package com.esofa.crm.model;

import java.io.Serializable;

import com.esofa.crm.refdata.model.FundingOption;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerInsuranceInfo  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L; 
	
	@Id
	Long id;
	
	@Parent
	private Key<Customer> customer;

	/** Insurance Information 
	 *	Depends on different funding option.. we will have different object attach to this  
	 **/
	private Key<FundingOption> fundingOptionInsurance;
	private Key<FundingOption> fundingOptionGovernment;
	private String insuranceNote;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(Key<Customer> customer) {
		this.customer = customer;
	}

	public Key<FundingOption> getFundingOptionInsurance() {
		return fundingOptionInsurance;
	}

	public void setFundingOptionInsurance(Key<FundingOption> fundingOptionInsurance) {
		this.fundingOptionInsurance = fundingOptionInsurance;
	}

	public Key<FundingOption> getFundingOptionGovernment() {
		return fundingOptionGovernment;
	}

	public void setFundingOptionGovernment(
			Key<FundingOption> fundingOptionGovernment) {
		this.fundingOptionGovernment = fundingOptionGovernment;
	}

	public void setInsuranceNote(String insuranceNote) {
		this.insuranceNote = insuranceNote;
	}
	
	public String getInsuranceNote() {
		return insuranceNote;
	}
	
	
}