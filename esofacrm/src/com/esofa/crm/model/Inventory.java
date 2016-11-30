package com.esofa.crm.model;

import java.io.Serializable;

import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Inventory  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@Parent
	@Index
	private Key<Product> product; 
	@Index
	private Key<Shop> shop;
	
	private int qty;
	
	private int threshold;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<Product> getProduct() {
		return product;
	}

	public void setProduct(Key<Product> product) {
		this.product = product;
	}

	public Key<Shop> getShop() {
		return shop;
	}

	public void setShop(Key<Shop> shop) {
		this.shop = shop;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	public int incrementAndGet(int increment) {
		qty = qty + increment;
		return qty;
	}
	
}