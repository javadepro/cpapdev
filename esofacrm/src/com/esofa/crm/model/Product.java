package com.esofa.crm.model;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.validator.product.DiscontinueProductCheck;
import com.esofa.crm.validator.product.ProductBarCodeCheck;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
@ProductBarCodeCheck
@DiscontinueProductCheck
@Index
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@NotBlank(message="{product.name.blank}")
	@Index
	private String name;
	
	private String description;
	
	@Digits(integer=12,fraction=2,message="product.price.formaterror")
	private Float price;
	
	@Digits(integer=12,fraction=2,message="product.price.formaterror")
	private Float priceNonAdp;
	
	//	@Digits(integer=12,fraction=2,message="product.price.formaterror")
	private Float priceSM;
	
	// Minimum qty - below this qty will triggers alert
	private int thresholdQty = 0;
	
	private String comment;
	
	private Key<ProductSubType> productSubType;
	
	@Index
	private String productBarCode;
	
	private String adpCatalogNumber;

	private String referenceNumber;
	
	private Key<Manufacturer> manufacturer;
	
	@Index
	private boolean isActive;
	
	private boolean hstApplicable;
	
	private Float defaultCost;
	
	@Ignore
	private transient boolean generateBarCode;
	
	public Product() {
		 isActive = true;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim().toUpperCase();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public int getThresholdQty() {
		return thresholdQty;
	}

	public void setThresholdQty(int thresholdQty) {
		this.thresholdQty = thresholdQty;
	}

	public Float getPrice() {
		
		if (price == null) { price = new Float(0); }
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public void setPriceNonAdp(Float priceNonAdp) {
		this.priceNonAdp = priceNonAdp;
	}
	
	public Float getPriceNonAdp() {
		
		if (priceNonAdp == null) { priceNonAdp = new Float(0); }
		return priceNonAdp;
	}
	
		public void setPriceSM(Float priceSM) {
		
		if (priceSM == null) { priceSM = new Float(0); }
		this.priceSM = priceSM;
	}
	
	public Float getPriceSM() {
		return priceSM;
	}
	
	public Key<ProductSubType> getProductSubType() {
		return productSubType;
	}

	public void setProductSubType(Key<ProductSubType> productSubType) {
		this.productSubType = productSubType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
		this.referenceNumber = referenceNumber;
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
	
	public void setGenerateBarCode(boolean generateBarCode) {
		this.generateBarCode = generateBarCode;
	}
	
	public boolean getGenerateBarCode() {
		return generateBarCode;
	}
	
	public boolean getHstApplicable() {
		return hstApplicable;
	}

	public void setHstApplicable(boolean hstApplicable) {
		this.hstApplicable = hstApplicable;
	}
			
	public void setDefaultCost(Float defaultCost) {
		this.defaultCost = defaultCost;
	}
	
	public Float getDefaultCost() {
		
		if (defaultCost == null) { defaultCost = new Float(0); }
		return defaultCost;
	}
}
