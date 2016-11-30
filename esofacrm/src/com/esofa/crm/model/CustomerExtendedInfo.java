package com.esofa.crm.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.util.DateUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.common.model.Address;
import com.esofa.crm.refdata.model.AppointmentPreference;
import com.esofa.crm.refdata.model.ContactPreference;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerExtendedInfo implements Serializable {
	private static final long serialVersionUID = 2L; 
	
	@Id
	Long id;
	
	@Parent
	private Key<Customer> customer;
	
	/** Contact Information **/
	@Valid
	private Address address = new Address();	
	
	@Email
	private String email;
	
	private Key<ContactPreference> contactPreference;
	
	private Key<AppointmentPreference> appointmentPreference;
	
	private String language;
	
	private String appointmentPreferenceNote;
	
	private String contactNotes;
		
	private String additionalNote;

	private Date customerSince= new Date();
	
	@NotBlank
	private String gender;

	private Date dateOfBirth;
	 

	private boolean consentStorage;
	
	@Deprecated
	private boolean consentContact;
	
	private boolean consentEmail;
	private boolean consentPhone;
	private boolean consentMail;
	
	public enum LanguagePreferences{
		EN("English"),
		ZH("Chinese"),
		FR("French");
		
		LanguagePreferences(String displayValue){
			this.displayValue = displayValue;
		}
		private final String displayValue;
		public String getDisplayValue(){
			return this.displayValue;
		}
		public String toString() {
	        return displayValue;
	    }
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Key<Customer> getCustomer() {
		return customer;
	}


	public void setCustomer(Key<Customer> customer) {
		this.customer = customer;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}

	public String getAddressAsOneLine() {
		
		StringBuilder addrLine = new StringBuilder();
		
		if (address !=null ) {
			
			addrLine.append(address.getLine1()).append(", ");
			if (StringUtils.isNotEmpty(address.getLine2())) {addrLine.append(address.getLine2()).append(", "); }
			if (StringUtils.isNotEmpty(address.getCity())) {addrLine.append(address.getCity()).append(", "); }
			if (StringUtils.isNotEmpty(address.getProvince())) {addrLine.append(address.getProvince()).append(", "); }
			if (StringUtils.isNotEmpty(address.getPostalCode())) {addrLine.append(address.getPostalCode()).append(" "); }
		}
		
		return addrLine.toString();
	}
	
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Key<ContactPreference> getContactPreference() {
		return contactPreference;
	}


	public void setContactPreference(Key<ContactPreference> contactPreference) {
		this.contactPreference = contactPreference;
	}


	public String getContactNotes() {
		return contactNotes;
	}


	public void setContactNotes(String contactNotes) {
		this.contactNotes = contactNotes;
	}


	public String getAdditionalNote() {
		return additionalNote;
	}


	public void setAdditionalNote(String additionalNote) {
		this.additionalNote = additionalNote;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getCustomerSince() {
		return customerSince;
	}

	public void setCustomerSince(Date customerSince) {
		this.customerSince = customerSince;
	}

	public Key<AppointmentPreference> getAppointmentPreference() {
		return appointmentPreference;
	}

	public void setAppointmentPreference(
			Key<AppointmentPreference> appointmentPreference) {
		this.appointmentPreference = appointmentPreference;
	}

	public String getAppointmentPreferenceNote() {
		return appointmentPreferenceNote;
	}

	public void setAppointmentPreferenceNote(String appointmentPreferenceNote) {
		this.appointmentPreferenceNote = appointmentPreferenceNote;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getFormattedDateOfBirth() {
		
		if (dateOfBirth == null) {
			return null;
		}
		return DateUtils.format(dateOfBirth, "dd/MM/yyyy");
	}
	

	public boolean getConsentStorage() {
		return consentStorage;
	}

	public void setConsentStorage(boolean consentStorage) {
		this.consentStorage = consentStorage;
	}

	@Deprecated
	public boolean getConsentContact() {
		return consentContact;
	}

	@Deprecated
	public void setConsentContact(boolean consentContact) {
		this.consentContact = consentContact;
	}


	public boolean getConsentEmail() {
		return consentEmail;
	}

	public void setConsentEmail(boolean consentEmail) {
		this.consentEmail = consentEmail;
	}

	public boolean getConsentPhone() {
		return consentPhone;
	}

	public void setConsentPhone(boolean consentPhone) {
		this.consentPhone = consentPhone;
	}

	public boolean getConsentMail() {
		return consentMail;
	}

	public void setConsentMail(boolean consentMail) {
		this.consentMail = consentMail;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(getEmail());
		sb.append(" " + id);
		sb.append(" " + consentContact);
		sb.append(" "  + consentEmail);
		return sb.toString();
		
	}
	
	// Load and resave entity for Objectify 4.1 migration
	@Deprecated
	public boolean lol() {
		return true;	
	}	
}