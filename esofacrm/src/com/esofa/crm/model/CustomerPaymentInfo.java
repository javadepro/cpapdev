package com.esofa.crm.model;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerPaymentInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7899597471930821459L;

	@Id
	Long id;
	
	@Parent
	private Key<Customer> customer;
	
	@CreditCardNumber
	private String creditCardNumber; 
	private String holdersName;
	
	@Pattern(regexp="\\d{2}|^$", message="Year has to be 2 digits")
	private String expYear;
	@Pattern(regexp="0?[1-9]|1[012]|^$", message="Month has to be 01-12")
	private String expMonth;
	private String securityNumber;
	private String cardType;
	
	@Pattern(regexp="0|\\d{0,10}\\.\\d{1,2}|^$",message="Has to be a number in xxx.xx format")
	private String balance = "0.00";
	
	
	private String note;
	
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getHoldersName() {
		return holdersName;
	}
	public void setHoldersName(String holdersName) {
		this.holdersName = holdersName;
	}
	
	public String getExpYear() {
		return expYear;
	}
	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}
	public String getExpMonth() {
		return expMonth;
	}
	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}
	public String getSecurityNumber() {
		return securityNumber;
	}
	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
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
	
	public double getBalanceInDouble(){
		return Double.parseDouble(balance);
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public enum CardType{
		VISA, MASTER, AE;
	};
	
}
