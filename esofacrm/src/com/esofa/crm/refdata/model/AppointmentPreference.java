package com.esofa.crm.refdata.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class AppointmentPreference implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	@NotBlank
	private String preference;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
	}
}