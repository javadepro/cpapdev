package com.esofa.gae.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.esofa.crm.security.user.model.CrmUser;

public class CrmAuthentication extends PreAuthenticatedAuthenticationToken{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678466447059929321L;
	
	public CrmAuthentication(Object aPrincipal, Object aCredentials) {
		super(aPrincipal, aCredentials);
	}
	
	public CrmAuthentication(java.lang.Object aPrincipal, java.lang.Object aCredentials, java.util.Collection<? extends GrantedAuthority> anAuthorities){
		super(aPrincipal, aCredentials, anAuthorities);
	}
	
	public CrmAuthentication(Authentication authentication, CrmUser crmUser){
		super(authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
		
		
		this.crmUser = crmUser;
		
		if (crmUser != null) {
			this.fistname = crmUser.getFirstname();
			this.lastname = crmUser.getLastname();
			this.initial = crmUser.getInitial();
		}
	}
	
	private String id; 
	private String initial;
	private String fistname;
	private String lastname;
	private CrmUser crmUser;
	
	public CrmUser getCrmUser() {
		return crmUser;
	}

	public void setCrmUser(CrmUser crmUser) {
		this.crmUser = crmUser;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getFistname() {
		return fistname;
	}

	public void setFistname(String fistname) {
		this.fistname = fistname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	
	

}
