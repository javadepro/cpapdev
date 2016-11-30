package com.esofa.gae.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.GrantedAuthorityImpl;
import com.esofa.crm.service.UserService;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;

public class GaeUserDetailsService implements
		AuthenticationUserDetailsService<Authentication> {

	private static final Logger log = Logger
			.getLogger(GaeUserDetailsService.class.getName());

	@Autowired
	UserService userService;


	public UserDetails loadUserDetails(Authentication token)
			throws UsernameNotFoundException {

		User googleUser = (User) token.getPrincipal();
		// ObjectifyGenericDao<GrantedAuthorityImpl> dao = new
		// ObjectifyGenericDao<GrantedAuthorityImpl>(
		// GrantedAuthorityImpl.class);
		// List<GrantedAuthorityImpl> authorities =
		// dao.listByProperty("user",googleUser.getUserId());
		// List<GrantedAuthorityImpl> authorities =
		// userService.getGrantedAuthorityImplByEmail(googleUser.getUserId());
		CrmUser crmUser = userService.getCrmUserByEmail(googleUser.getEmail());
		Key<GrantedAuthorityImpl>[] authsKey = crmUser.getAuthorities();

		Map<Key<GrantedAuthorityImpl>, GrantedAuthorityImpl> authMap = userService
				.getGrantedAuthorityMap();

		List<GrantedAuthorityImpl> authorities = new ArrayList<GrantedAuthorityImpl>();
		if (authsKey != null) {
			for (Key<GrantedAuthorityImpl> auth : authsKey) {
				authorities.add(authMap.get(auth));
			}
		}

		 if (crmUser != null && !crmUser.isActive()) {
				
				throw new UsernameNotFoundException("User " + googleUser.getEmail()
						+ " not authorized to use this.");
			}
		
		if (authorities == null || authorities.size() == 0) {
			throw new UsernameNotFoundException("User "
					+ googleUser.getUserId() + " not found");
		}
		return new org.springframework.security.core.userdetails.User(
				googleUser.getUserId(), "", true, true, true, true, authorities);
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/*
	 * @Override public UserDetails loadUserByUsername(String username) throws
	 * UsernameNotFoundException { // Retuen User Details List<GrantedAuthority>
	 * authorities = new ArrayList<GrantedAuthority>(); authorities.add(new
	 * SimpleGrantedAuthority("ROLE_ADMIN")); return new
	 * User(username,"",true,true,true,true, authorities); }
	 */

}
