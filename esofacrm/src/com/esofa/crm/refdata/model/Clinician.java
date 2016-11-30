package com.esofa.crm.refdata.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Clinician implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id 
	Long id;
	
	@Email
	private String email;

	@NotBlank
	private String firstname;
	
	@NotBlank
	private String lastname;
	
	private Key<Shop> shop;

	private String comment;
	
	public String getName(){
		return this.firstname +" "+ this.lastname;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Key<Shop> getShop() {
		return shop;
	}

	public void setShop(Key<Shop> shop) {
		this.shop = shop;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}