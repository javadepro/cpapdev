package com.esofa.crm.security.user.model;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class GrantedAuthorityImpl implements GrantedAuthority, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	
	private String role;
	
	private String description;
	
	public GrantedAuthorityImpl(){}
	
	public GrantedAuthorityImpl(String role){
		this.role = role;
	}
	
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.role;
	}
	
	public void setAuthority(String authority){
		this.role = authority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
