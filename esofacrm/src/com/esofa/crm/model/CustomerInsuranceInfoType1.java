package com.esofa.crm.model;

import java.io.Serializable;

import com.esofa.crm.refdata.model.FundingOption;
import com.esofa.crm.refdata.model.InsuranceProvider;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerInsuranceInfoType1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	@Id
	Long id;

	@Parent
	private Key<Customer> customer;

	private Key<InsuranceProvider> insuranceProvider;

	private Key<FundingOption> fundingOption;

	private String policyNumber;

	private String certificateNumber;

	private String divisionNumber;

	/*** Insruance Information ***/
	private int headGearReplaceFreq;

	/** Getter and Setter **/
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

	public Key<InsuranceProvider> getInsuranceProvider() {
		return insuranceProvider;
	}

	public void setInsuranceProvider(Key<InsuranceProvider> insuranceProvider) {
		this.insuranceProvider = insuranceProvider;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Key<FundingOption> getFundingOption() {
		return fundingOption;
	}

	public void setFundingOption(Key<FundingOption> fundingOption) {
		this.fundingOption = fundingOption;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getDivisionNumber() {
		return divisionNumber;
	}

	public void setDivisionNumber(String divisionNumber) {
		this.divisionNumber = divisionNumber;
	}
}
