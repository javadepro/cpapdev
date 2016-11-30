package com.esofa.crm.controller.util;

import java.io.Serializable;

import com.esofa.crm.model.trial.TrialItem;

public class TrialItemForm implements Serializable{

	private static final long serialVersionUID = 2602345411969492009L;
	
	//add, or edit.
	private String mode;
	
	private String actionType;
	
	private String productBarCode;
	private Long customerId;
	private TrialItem trialItem;
	
	private String healthCardNumber;
	
	public TrialItemForm() {
		trialItem = new TrialItem();
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getMode() {
		return mode;
	}
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getProductBarCode() {
		return productBarCode;
	}
	public void setProductBarCode(String productBarCode) {
		this.productBarCode = productBarCode;
	}

	public Long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public TrialItem getTrialItem() {
		return trialItem;
	}
	public void setTrialItem(TrialItem trialItem) {
		this.trialItem = trialItem;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}
	
	public String getHealthCardNumber() {
		return healthCardNumber;
	}

}
