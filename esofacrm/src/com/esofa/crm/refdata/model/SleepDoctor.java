package com.esofa.crm.refdata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SleepDoctor implements Serializable, Doctor {
	private static final long serialVersionUID = 2L;

	@Id
	Long id;

	@NotBlank(message = "{firstname.empty}")
	@Index
	private String firstName;

	@NotBlank(message = "{lastname.empty}")
	@Index
	private String lastName;

	private String email;

	private String phone;
	
	private String ext;
	
	private String fax;

	@Pattern(regexp="^0\\d{4}$|[^0]{1}[\\w]*|^$", message="{sleepdoctor.hib.format}")
	private String hibNumber;
	
	private List<Key<SleepClinic>> clinics = new ArrayList<Key<SleepClinic>>();
	
	private String comment;
	
	/** Helper methods **/
	public String getName(){
		return this.lastName+", "+this.firstName;
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

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getHibNumber() {
		return hibNumber;
	}

	public void setHibNumber(String hibNumber) {
		this.hibNumber = hibNumber;
	}

	public List<Key<SleepClinic>> getClinics() {
		return clinics;
	}

	public void setClinics(List<Key<SleepClinic>> clinics) {
		this.clinics = clinics;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
