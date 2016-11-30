package com.esofa.crm.model;

import java.io.Serializable;

import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.Manufacturer;
import com.googlecode.objectify.Key;

public class ProductAlertSearch  implements Serializable {

	
	private static final long serialVersionUID = -378610680310251062L;
	
	private Integer numDays = 14;
	private Key<Manufacturer> manufacturer = null;
	private Key<AlertSubType> alertSubType = null;
	
	public Integer getNumDays() {
		return numDays;
	}
	public void setNumDays(Integer numDays) {
		this.numDays = numDays;
	}
	
	public Key<AlertSubType> getAlertSubType() {
		return alertSubType;
	}
	public void setAlertSubType(Key<AlertSubType> alertSubType) {
		this.alertSubType = alertSubType;
	}
	

	public Key<Manufacturer> getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(Key<Manufacturer> manufacturer) {
		this.manufacturer = manufacturer;
	}
	
}
