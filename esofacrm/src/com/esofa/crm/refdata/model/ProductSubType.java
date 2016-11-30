package com.esofa.crm.refdata.model;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class ProductSubType implements Serializable {

	private static final Logger log = Logger
			.getLogger(ProductSubType.class.getName());
	
	private static final long serialVersionUID = 1L;

	@Id 
	Long id;
	
	@NotBlank
	private String type;

	@Parent
	@NotNull
	private Key<ProductType> parentType;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<ProductType> getParentType() {
		return parentType;
	}

	public void setParentType(Key<ProductType> parentType) {
		log.info("set Value");
		this.parentType = parentType;
	}
}
