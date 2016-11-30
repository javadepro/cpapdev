package com.esofa.crm.controller.util;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.Product;
import com.googlecode.objectify.Key;

@Component
public class SalesItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4892633259591415893L;
	
	String productName;
	Key<Product> product;
	Key<Customer> customer;
	
	int qty;
	String status = "UNPROCESSED";
	
	public Key<Product> getProduct() {
		return product;
	}
	public void setProduct(Key<Product> product) {
		this.product = product;
	}
	public Key<Customer> getCustomer() {
		return customer;
	}
	public void setCustomer(Key<Customer> customer) {
		this.customer = customer;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	
}
