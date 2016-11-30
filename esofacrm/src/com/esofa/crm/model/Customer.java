package com.esofa.crm.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	Long id;

	@Index
	@NotBlank(message = "{customer.firstname.empty}")
	private String firstname;

	@Index
	@NotBlank(message = "{customer.lastname.empty}")
	private String lastname;

	@Index
	@Pattern(regexp = "\\d{4}\\s\\d{3}\\s\\d{3}|^$", message = "{customer.healthcard.number.pattern.notmatch}")
	private String healthCardNumber;

	@Pattern(regexp = "\\w{2}|\\w{1}|^$", message = "{customer.healthcard.version.pattern.notmatch}")
	private String healthCardVersion;

	@Index
	@NotBlank(message = "{customer.phonehome.empty}")
	private String phoneHome;

	@Index
	private String phoneOffice;

	private String phoneOfficeExt;

	@Index
	private String phoneMobile;

	private Key<CrmUser> clinician;

	private Key<Shop> preferredLocation;

	private Date lastUpdated;
	
	@Index
	private boolean isActive = true;

	/** GENDER ENUM **/
	public enum Gender {
		MALE, FEMALE, NEUTRAL;
	};

	/** Helper **/
	public String getName() {
		return this.firstname + " " + this.lastname;
	}

	/** Getter Setter **/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname.toUpperCase();
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname.toUpperCase();
	}

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public String getHealthCardVersion() {
		return healthCardVersion;
	}

	public void setHealthCardVersion(String healthCardVersion) {
		this.healthCardVersion = healthCardVersion;
	}

	public String getFullHealthCardNumber() {
		return healthCardNumber + " " + healthCardVersion;
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPhoneHome() {
		return phoneHome;
	}

	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}

	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	public String getPhoneOfficeExt() {
		return phoneOfficeExt;
	}

	public void setPhoneOfficeExt(String phoneOfficeExt) {
		this.phoneOfficeExt = phoneOfficeExt;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	@JsonIgnore
	public Key<CrmUser> getClinician() {
		return clinician;
	}

	public void setClinician(Key<CrmUser> clinician) {
		this.clinician = clinician;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/** Helper method **/
	@JsonIgnore
	public Key<Customer> getKey() {
		if (this.id != null && this.id != 0) {
			return Key.create(Customer.class, this.id);
		}
		return null;
	}

	public Key<Shop> getPreferredLocation() {
		return preferredLocation;
	}

	public void setPreferredLocation(Key<Shop> preferredLocation) {
		this.preferredLocation = preferredLocation;
	}
	
	public String getFormattedHealthCardNumber() {
		
		String retValue = healthCardNumber + " " + healthCardVersion;		
		retValue = StringUtils.replace(retValue, " " , "-");
		
		return retValue;
	}
	
	public String getFormattedLastUpdated() {
		if(lastUpdated == null) {
			return "-";			
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(this.lastUpdated);
		}		
	}
}
