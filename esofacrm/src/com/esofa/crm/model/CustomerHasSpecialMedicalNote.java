package com.esofa.crm.model;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerHasSpecialMedicalNote  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	Long id; 
	
	@Parent
	private Key<Customer> customer;
	
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
	
	
	
}