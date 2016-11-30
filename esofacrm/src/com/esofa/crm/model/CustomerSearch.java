package com.esofa.crm.model;

import java.io.Serializable;

import javax.validation.constraints.Digits;

public class CustomerSearch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	private String firstname;

	private String lastname;

	@Digits(integer=10,fraction=0)
	private String healthCardNumber;

	private boolean isActive = true;

	private String phoneHome;
	private String phoneMobile;
	private String phoneOffice;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname.trim().toUpperCase();
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname.toUpperCase();;
	}

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber.trim();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPhoneHome() {
		return phoneHome;
	}

	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome.trim();
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile.trim();
	}

	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice.trim();
	}
	
	public int hasWildCard(){
		int hasWildCard = 0;
		if(this.firstname!=null&&this.firstname.contains("*")) hasWildCard++;
		if(this.lastname!=null&&this.lastname.contains("*")) hasWildCard++;
		if(this.phoneHome!=null&&this.phoneHome.contains("*")) hasWildCard++;
		if(this.phoneMobile!=null&&this.phoneMobile.contains("*")) hasWildCard++;
		if(this.phoneOffice!=null&&this.phoneOffice.contains("*")) hasWildCard++;
		if(this.healthCardNumber!=null&&this.healthCardNumber.contains("*")) hasWildCard++;
		return hasWildCard;
	}
	public int fieldSet(){
		int fieldSet = 0;
		if(this.firstname!=null&&!getFirstname().equals("")) fieldSet++;
		if(this.lastname!=null&&!getLastname().equals("")) fieldSet++;
		if(this.phoneHome!=null&&!getPhoneHome().equals("")) fieldSet++;
		if(this.phoneMobile!=null&&!getPhoneMobile().equals("")) fieldSet++;
		if(this.phoneOffice!=null&&!getPhoneOffice().equals("")) fieldSet++;
		if(this.healthCardNumber!=null&&!getHealthCardNumber().equals("")) fieldSet++;
		return fieldSet;
	}

}
