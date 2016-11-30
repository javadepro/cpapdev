package com.esofa.crm.model.pos;

import java.io.Serializable;

import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class InvoiceSeqNum  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -101260221626795623L;

	@Id
	private Long id;
		
	@Index
	private Key<Shop> shopKey;
	
	private int value=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<Shop> getShopKey() {
		return shopKey;
	}

	public void setShopKey(Key<Shop> shopKey) {
		this.shopKey = shopKey;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public int getAndIncrementValue() {
		
		int original = value;
		
		synchronized (InvoiceSeqNum.class) {
			original = value;
			value++;
		}
		
		return original;
	}
	
	
}
