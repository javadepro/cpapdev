package com.esofa.crm.model;

import java.util.Date;

import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;

// TODO is this class used at all?
public class InventoryOrder {

	private Date orderDate;
	private Key<Product> product;
	private Key<Shop> shipTo;
	private int qty;
	private String status;
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Key<Product> getProduct() {
		return product;
	}
	public void setProduct(Key<Product> product) {
		this.product = product;
	}
	public Key<Shop> getShipTo() {
		return shipTo;
	}
	public void setShipTo(Key<Shop> shipTo) {
		this.shipTo = shipTo;
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
}
