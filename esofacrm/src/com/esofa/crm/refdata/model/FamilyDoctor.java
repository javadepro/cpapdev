package com.esofa.crm.refdata.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.common.model.Address;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class FamilyDoctor implements Serializable, Doctor {
	private static final long serialVersionUID = 2L;

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

	private String clinicName;

	private String physicianBillingNumber;

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

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	public String getPhysicianBillingNumber() {
		return physicianBillingNumber;
	}

	public void setPhysicianBillingNumber(String physicianBillingNumber) {
		this.physicianBillingNumber = physicianBillingNumber;
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

	// Load and resave entity for Objectify 4.1 migration
	@Deprecated
	public boolean lol() {
		return true;	
	}	
}
