package com.esofa.crm.model;

import java.io.Serializable;

import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;

public class InventoryTransferSearch  implements Serializable  {
	
	private static final long serialVersionUID = 2506965386893400464L;
	private Key<Shop> shop = null;

	public Key<Shop> getShop() {
		return shop;
	}

	public void setShop(Key<Shop> shop) {
		this.shop = shop;
	}

	
	

}
