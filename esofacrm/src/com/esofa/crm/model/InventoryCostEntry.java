package com.esofa.crm.model;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class InventoryCostEntry implements Serializable {
	private static final long serialVersionUID = -5273093591726705637L;
	
	public enum Status {
		
		Active, Current, AllSold
	}
	
	@Id
	private Long id;
	
	@Index
	private Date insertDateTime;
	
	@Index
	private Key<Product> productKey;
	
	private int qty;
	
	private int qtySold;
	
	private Float cost;

	@Index
	private String status;
	
	public InventoryCostEntry() {
		
		cost =0.0f;
	
	}
	
	public InventoryCostEntry(Date insertDateTime, Key<Product> productKey,
			int qty, Float cost) {
		super();
		this.insertDateTime = insertDateTime;
		this.productKey = productKey;
		this.qty = qty;
		this.cost = cost;
		status = Status.Active.toString();
	}

	public Date getInsertDateTime() {
		return insertDateTime;
	}

	public void setInsertDateTime(Date insertDateTime) {
		this.insertDateTime = insertDateTime;
	}

	public Key<Product> getProductKey() {
		return productKey;
	}

	public void setProductKey(Key<Product> productKey) {
		this.productKey = productKey;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getQtySold() {
		return qtySold;
	}

	public void setQtySold(int qtySold) {
		this.qtySold = qtySold;
	}
	
	public synchronized int getRemainingQty() {
		return qty - qtySold;
	}
	
	public synchronized int incrementAndGetQtySold(int delta) {
		qtySold = qtySold + delta;
		return qtySold;
	}

	public Float getCost() {
		
		if (cost == null) { cost = 0.0f;}
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "InventoryCostEntry [id=" + id + ", insertDateTime="
				+ insertDateTime + ", productKey=" + productKey + ", qty="
				+ qty + ", qtySold=" + qtySold + ", cost=" + cost + ", status="
				+ status + "]";
	}
	

}
