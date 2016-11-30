package com.esofa.crm.model.trial;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class TrialItem implements Serializable {

	private static final long serialVersionUID = 6783571407729780263L;

	public enum TrialStatusE {
		ON_TRIAL, RENTED, AVAILABLE
	}
	

	@Id
	private Long id;
	@Index
	private Key<Shop> location;
	private Date lastUpdatedDate;
	
	//what is borrowed
	@Index
	private String serialNumber;
	@Index
	private Key<Product> product;	
	
	//who borrowed it
	private Key<Customer> customerKey;
	private String customerFullName = StringUtils.EMPTY;
	
	@Index
	private String trialStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Key<Shop> getLocation() {
		return location;
	}
	public void setLocation(Key<Shop> location) {
		this.location = location;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Key<Product> getProduct() {
		return product;
	}
	public void setProduct(Key<Product> product) {
		this.product = product;
	}
	
	public Key<Customer> getCustomerKey() {
		return customerKey;
	}
	public void setCustomerKey(Key<Customer> customerKey) {
		this.customerKey = customerKey;
	}
	public String getCustomerFullName() {
		return customerFullName;
	}
	public void setCustomerFullName(String customerFullName) {
		this.customerFullName = customerFullName;
	}
	public String getTrialStatus() {
		return trialStatus;
	}
	public void setTrialStatus(String trialStatus) {
		this.trialStatus = trialStatus;
	}
	
	
}
