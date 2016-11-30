package com.esofa.crm.refdata.model;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.Key;


@Entity
public class PrimaryAdpInfo implements Comparable<PrimaryAdpInfo> , Serializable{

	private static final long serialVersionUID = -7996391255216247775L;
	
	@Id
	Long id;
	
	@NotNull
	private Key<Shop> shopKey;
	
	@NotBlank
	@Index
	private String adpNumber;

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

	public String getAdpNumber() {
		return adpNumber;
	}

	public void setAdpNumber(String adpNumber) {
		this.adpNumber = adpNumber;
	}

	@Override
	public int compareTo(PrimaryAdpInfo o) {
		return this.getId().compareTo(o.getId());		
	}

	
	

}
