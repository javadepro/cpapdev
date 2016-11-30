package com.esofa.crm.model;

import java.io.Serializable;
import java.util.Date;

import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.Manufacturer;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class ProductAlert  implements Serializable {	 
	private static final long serialVersionUID = 6827012369493418745L;
	
	@Id
	Long id;
    private String message;
    
    @Index
    private Date alertDate;
    
    @Index
    private Key<AlertSubType> alertSubType;
    
	@Parent
	private Key<Product> product;
    
	@Index
	private Key<Manufacturer> manufacturer;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
	public Key<AlertSubType> getAlertSubType() {
		return alertSubType;
	}
	public void setAlertSubType(Key<AlertSubType> alertSubType) {
		this.alertSubType = alertSubType;
	}
	
	public Key<Product> getProduct() {
		return product;
	}

	public void setProduct(Key<Product> product) {
		this.product = product;
	}
	
	public void setManufacturer(Key<Manufacturer> manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Key<Manufacturer> getManufacturer() {
		return manufacturer;
	}
}
