package com.esofa.crm.refdata.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class EventSubType implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@NotBlank
	private String type;
	
	@Parent
	@NotNull
	private Key<EventType> parentType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Key<EventType> getParentType() {
		return parentType;
	}

	public void setParentType(Key<EventType> parentType) {
		this.parentType = parentType;
	}
	
	@Override
	public String toString() {
		return "EventSubType [id=" + id + ", type=" + type + ", parentType="
				+ parentType + "]";
	}
}
