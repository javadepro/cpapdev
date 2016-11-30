package com.esofa.crm.refdata.model;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.common.model.Address;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class Dentist implements Serializable, Doctor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	@Id
	Long id;

	@NotBlank(message="{firstname.empty}")
	@Index
	private String firstName;
	
	@NotBlank(message="{lastname.empty}")
	@Index
	private String lastName;

	@Email(message="{email.format}")
	private String email;

	private String phone;

	private String fax;

	private Address address = new Address();
	
	private String comment;

	/** Helper methods **/
	public String getName() {
		return this.lastName + ", " + this.firstName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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


	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
