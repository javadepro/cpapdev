package com.esofa.crm.common.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

//import com.googlecode.objectify.annotation.Embed;

//@Embed
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="{address.line1.empty}")
	public String line1;

	public String line2;

	public String city;
	
	//@NotBlank(message="{address.postal.empty}")
	public String postalCode;

	public String province;

	public String country;

	public enum Province{
		ON,AB,BC,MB,NB,NL,NT,NS,NU,PE,QC,SK,YT;
	};
	
	public enum Country{
		CA("Canada"),US("United States"),HK("Hong Kong");
		Country(String displayValue){
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

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String toString(){
		String address = this.line1.trim().equals("")?"":this.line1+", ";
		address+=this.line2.trim().equals("")?"":this.line2;
		return address+", "+this.city+", "+this.province+", "+this.postalCode+", "+this.country;
	}
}
