package com.esofa.crm.model;

import java.io.Serializable;
import java.util.LinkedList;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class InventoryCostQueue implements Serializable {
	private static final long serialVersionUID = -3300106402530193197L;
	
	@Id
	private Long id;
	
	@Index
	private Key<Product> productKey;
		
	// TODO IMPORTANT: there may be data migration require for this
	// @Embed no longer works on field. it is a marker for classes that are
	// embeddable. also, not sure if it requires to be Embed in the first place.
	// the field is a key (value), not an entity
	// @Embed
	private LinkedList<Key<InventoryCostEntry>> costEntryQueue;

	public InventoryCostQueue() {
		costEntryQueue = new LinkedList<Key<InventoryCostEntry>>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<Product> getProductKey() {
		return productKey;
	}

	public void setProductKey(Key<Product> productKey) {
		this.productKey = productKey;
	}

	public LinkedList<Key<InventoryCostEntry>> getCostEntryQueue() {
		return costEntryQueue;
	}

	public void setCostEntryQueue(LinkedList<Key<InventoryCostEntry>> costEntryQueue) {
		this.costEntryQueue = costEntryQueue;
	}

	public boolean add(Key<InventoryCostEntry> e) {
		return costEntryQueue.add(e);
	}

	
	
	public Key<InventoryCostEntry> peek() {
		return costEntryQueue.peek();
	}

	public Key<InventoryCostEntry> poll() {
		return costEntryQueue.poll();
	}

	

}
