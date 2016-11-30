package com.esofa.crm.refdata.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class DiscountReason implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	Long id;

	@NotBlank
	private String reason;
	
	@Index
	private int displayOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}
}
