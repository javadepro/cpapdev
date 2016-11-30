package com.esofa.crm.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.refdata.model.FundingOption;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerInsuranceInfoType2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L; 
	
	@Id
	Long id;
	
	@Parent
	private Key<Customer> customer;
		
	private Key<FundingOption> fundingOption;

	@NotBlank(message="{customer.insurance.memeberid.empty}")
	private String memberId;
	
	private String caseWorkerNumber;
	
	private String caseWorkerPhone;
	
	private String caseWorkerPhoneExt;

	private String caseWorkerFax;

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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getCaseWorkerNumber() {
		return caseWorkerNumber;
	}

	public void setCaseWorkerNumber(String caseWorkerNumber) {
		this.caseWorkerNumber = caseWorkerNumber;
	}

	public String getCaseWorkerPhone() {
		return caseWorkerPhone;
	}

	public void setCaseWorkerPhone(String caseWorkerPhone) {
		this.caseWorkerPhone = caseWorkerPhone;
	}

	public String getCaseWorkerPhoneExt() {
		return caseWorkerPhoneExt;
	}

	public void setCaseWorkerPhoneExt(String caseWorkerPhoneExt) {
		this.caseWorkerPhoneExt = caseWorkerPhoneExt;
	}

	public String getCaseWorkerFax() {
		return caseWorkerFax;
	}

	public void setCaseWorkerFax(String caseWorkerFax) {
		this.caseWorkerFax = caseWorkerFax;
	}

	public Key<FundingOption> getFundingOption() {
		return fundingOption;
	}

	public void setFundingOption(Key<FundingOption> fundingOption) {
		this.fundingOption = fundingOption;
	}
	
	
	
}