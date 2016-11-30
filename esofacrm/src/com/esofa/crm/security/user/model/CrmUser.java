package com.esofa.crm.security.user.model;

import java.io.Serializable;

import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class CrmUser implements Serializable{
	private static final long serialVersionUID = 1982340234L;

	@Id
	Long id;
	
	private boolean isActive = true;
	private String initial;
	private String firstname;
	private String lastname;
	private String email;
	private String alternateEmail;
	private Key<GrantedAuthorityImpl>[] authorities ;
	private Key<Shop>[] shops;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isActive() {
		return isActive;
	}
	
	public boolean getActive() {
		return isActive();
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getName(){
		return lastname+", "+firstname;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Key<GrantedAuthorityImpl>[] getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Key<GrantedAuthorityImpl>[] authorities) {
		this.authorities = authorities;
	}
	public Key<Shop>[] getShops() {
		return shops;
	}
	public void setShops(Key<Shop>[] shops) {
		this.shops = shops;
	}
	
	public String getAlternateEmail() {
		return alternateEmail;
	}
	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}
	
	
}
