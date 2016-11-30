package com.esofa.crm.controller.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;

public class DailySalesForm {

	@NotNull
	Key<Shop> shop;
	@NotNull
	Date date;
	List<SalesItem> items = new ArrayList<SalesItem>();
	
	public DailySalesForm(){
		for(int i=0; i<5;i++){
			items.add(new SalesItem());
		}
	}
	
	public Key<Shop> getShop() {
		return shop;
	}
	public void setShop(Key<Shop> shop) {
		this.shop = shop;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<SalesItem> getItems() {
		return items;
	}
	public void setItems(List<SalesItem> items) {
		this.items = items;
	}
	
	
}
