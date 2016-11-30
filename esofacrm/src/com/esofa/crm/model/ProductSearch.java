package com.esofa.crm.model;

import java.io.Serializable;

import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.model.ProductSubType;
import com.googlecode.objectify.Key;

public class ProductSearch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private Float price;
	
	private Key<ProductSubType> productSubType;
	
	private String productBarCode;
	
	private String adpCatalogNumber;

	private String referenceNumber;
	
	private Key<Manufacturer> manufacturer;
	
	private boolean isActive = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim().toUpperCase();
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Key<ProductSubType> getProductSubType() {
		return productSubType;
	}

	public void setProductSubType(Key<ProductSubType> productSubType) {
		this.productSubType = productSubType;
	}

	public String getProductBarCode() {
		return productBarCode;
	}

	public void setProductBarCode(String productBarCode) {
		this.productBarCode = productBarCode.trim().toUpperCase();
	}

	public String getAdpCatalogNumber() {
		return adpCatalogNumber;
	}

	public void setAdpCatalogNumber(String adpCatalogNumber) {
		this.adpCatalogNumber = adpCatalogNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber.trim().toUpperCase();
	}

	public Key<Manufacturer> getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Key<Manufacturer> manufacturer) {
		this.manufacturer = manufacturer;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public int hasWildCard(){
		int hasWildCard = 0;
		if(this.getName()!=null&&this.getName().contains("*")) hasWildCard++;
		if(this.getProductBarCode()!=null&&this.getProductBarCode().contains("*")) hasWildCard++;
		if(this.getReferenceNumber()!=null&&this.getReferenceNumber().contains("*")) hasWildCard++;
		return hasWildCard;
	}
	public int fieldSet(){
		int fieldSet = 0;
		if(this.getName()!=null&&!getName().equals("")) fieldSet++;
		if(this.getProductBarCode()!=null&&!getProductBarCode().equals("")) fieldSet++;
		if(this.getReferenceNumber()!=null&&!getReferenceNumber().equals("")) fieldSet++;
		return fieldSet;
	}

		
}
