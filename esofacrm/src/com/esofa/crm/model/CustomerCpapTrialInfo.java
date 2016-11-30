package com.esofa.crm.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerCpapTrialInfo implements Serializable {

	private static final long serialVersionUID = -6905332922744858433L;


	@Id
	Long id;
	
	@Parent
	private Key<Customer> customer;
	
	//trial info
	@Pattern(regexp="0|\\d{0,10}\\.?\\d{0,2}|^$",message="Has to be a number in xxx.xx format")
	private String cpapDepositAmt;
	private String cpapReceivedBy;
	
	@Pattern(regexp="0|\\d{0,10}\\.?\\d{0,2}|^$",message="Has to be a number in xxx.xx format")
	private String apapDepositAmt;
	private String appaReceivedBy;
	
	private Date cpapRentalStart;
	private Date cpapRentalEnd;
	
	private Date apapRentalStart;
	private Date apapRentalEnd;
	
	public CustomerCpapTrialInfo() { }
	
	public CustomerCpapTrialInfo(Key<Customer> customer) {
		this.customer = customer;
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
	public String getCpapDepositAmt() {
		return cpapDepositAmt;
	}
	public void setCpapDepositAmt(String cpapDepositAmt) {
		this.cpapDepositAmt = cpapDepositAmt;
	}
	public String getCpapReceivedBy() {
		return cpapReceivedBy;
	}
	public void setCpapReceivedBy(String cpapReceivedBy) {
		this.cpapReceivedBy = cpapReceivedBy;
	}
	public String getApapDepositAmt() {
		return apapDepositAmt;
	}
	public void setApapDepositAmt(String apapDepositAmt) {
		this.apapDepositAmt = apapDepositAmt;
	}
	public String getAppaReceivedBy() {
		return appaReceivedBy;
	}
	public void setAppaReceivedBy(String appaReceivedBy) {
		this.appaReceivedBy = appaReceivedBy;
	}
	public Date getCpapRentalStart() {
		return cpapRentalStart;
	}
	public void setCpapRentalStart(Date cpapRentalStart) {
		this.cpapRentalStart = cpapRentalStart;
	}
	public Date getCpapRentalEnd() {
		return cpapRentalEnd;
	}
	public void setCpapRentalEnd(Date cpapRentalEnd) {
		this.cpapRentalEnd = cpapRentalEnd;
	}
	public Date getApapRentalStart() {
		return apapRentalStart;
	}
	public void setApapRentalStart(Date apapRentalStart) {
		this.apapRentalStart = apapRentalStart;
	}
	public Date getApapRentalEnd() {
		return apapRentalEnd;
	}
	public void setApapRentalEnd(Date apapRentalEnd) {
		this.apapRentalEnd = apapRentalEnd;
	}
	
	
}