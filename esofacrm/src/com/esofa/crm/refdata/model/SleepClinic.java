package com.esofa.crm.refdata.model;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.common.model.Address;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SleepClinic implements Serializable{	
	private static final long serialVersionUID = 1L;

	@Id
	Long id;

	@NotBlank
	@Index
	private String name;

	@Email
	private String email;
	
	@NotBlank
	private String phone;
	
	private String phoneExt;
	
	private String fax;
	
	private String contact;

	@Pattern(regexp="\\d{4}|^$", message="ADP Number has to be 4 digits")
	private String adpNumber;
	
	private Address address = new Address();

	private String comment;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getPhoneExt() {
		return phoneExt;
	}

	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getAdpNumber() {
		return adpNumber;
	}

	public void setAdpNumber(String adpNumber) {
		this.adpNumber = adpNumber;
	}

	// Load and resave entity for Objectify 4.1 migration
	@Deprecated
	public boolean lol() {
		return true;	
	}	
}
