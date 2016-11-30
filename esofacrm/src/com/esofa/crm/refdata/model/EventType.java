package com.esofa.crm.refdata.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class EventType implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@NotBlank
	@Index
	private String type;

	private String color = "#000000";
	
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "EventType [id=" + id + ", type=" + type + ", color=" + color
				+ "]";
	}
}
