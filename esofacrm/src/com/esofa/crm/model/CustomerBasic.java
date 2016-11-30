package com.esofa.crm.model;

import java.io.Serializable;


public class CustomerBasic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -645353533892389971L;
	private Long id;
	private String value;
	private String fullHealthCardNumber;
	
	
	
	public CustomerBasic(Long id, String value,String fullHealthCardNumber) {
		super();
		this.id = id;
		this.value = value;
		this.fullHealthCardNumber = fullHealthCardNumber;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFullHealthCardNumber() {
		return fullHealthCardNumber;
	}
	public void setFullHealthCardNumber(String fullHealthCardNumber) {
		this.fullHealthCardNumber = fullHealthCardNumber;
	}
	
	
	
	
}
