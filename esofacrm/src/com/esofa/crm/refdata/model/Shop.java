package com.esofa.crm.refdata.model;


import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.common.model.Address;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Shop implements Comparable<Shop>, Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@NotBlank(message="{name.empty}")
	private String name;
	
	@NotBlank(message="{shortname.empty}")
	private String shortName;
	
	@Email
	private String email;
	
	private String phone;
	
	private String fax;
	
	@NotNull
	@Digits(integer=5, fraction=0)
	@Min(value=1)
	@Index
	private Integer order = 0;
	
	private Address address = new Address();

	private String shopType;

	private String adpVendorNumber;
	
	private String hstNumber;
	
	@Index
	private boolean displayDropDown;
	
	public enum ShopType{
		NORMAL, VIRTUAL;
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
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}
	
	public String getAdpVendorNumber() {
		return adpVendorNumber;
	}

	public void setAdpVendorNumber(String adpVendorNumber) {
		this.adpVendorNumber = adpVendorNumber;
	}

	public String getHstNumber() {
		return hstNumber;
	}

	public void setHstNumber(String hstNumber) {
		this.hstNumber = hstNumber;
	}

	public int compareTo(Shop otherShop) {
		return this.getOrder().compareTo(otherShop.getOrder());
	};
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) {
			return false;
		} else if (obj instanceof Shop == false) {
			return false;
		}
		
		Shop shopObj = (Shop) obj;
		return this.getOrder().equals(shopObj.getOrder());
	}
	
	public boolean getDisplayDropDown() {
		
		return displayDropDown;
	}

	public void setDisplayDropDown(boolean displayDropDown) {
		this.displayDropDown = displayDropDown;
	}
	
	// Load and resave entity for Objectify 4.1 migration
	@Deprecated
	public boolean lol() {
		return true;	
	}	
}
