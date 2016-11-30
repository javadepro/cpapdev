package com.esofa.gae.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.GrantedAuthorityImpl;
import com.esofa.crm.service.UserService;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;

public class GaeAuthenticationProvider extends
		PreAuthenticatedAuthenticationProvider {

	private static final Logger log = Logger
			.getLogger(GaeAuthenticationProvider.class.getName());

	@Autowired
	UserService userService;

	public Authentication authenticate(Authentication authentication) {
		User googleUser = (User) authentication.getPrincipal();

		log.info("User " + googleUser.getEmail() + "("
				+ googleUser.getNickname() + ") trying to get authorization");

		List<GrantedAuthorityImpl> authorities = new ArrayList<GrantedAuthorityImpl>();

		CrmUser crmUser = userService.getCrmUserByEmail(googleUser.getEmail());

		if (googleUser.getEmail().trim().equals("cpapdirect.app@gmail.com")
				|| googleUser.getEmail().trim().equals("jackson.ha@gmail.com")) {
			authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
			authorities.add(new GrantedAuthorityImpl("ROLE_SUPER"));
			authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
			if(crmUser == null){
				crmUser = new CrmUser();
				crmUser.setEmail(googleUser.getEmail().trim());
				crmUser.setFirstname("");
				crmUser.setLastname("");
				crmUser.setInitial(googleUser.getNickname());
				userService.saveCrmUser(crmUser);
			}
		}

		if (crmUser != null && crmUser.isActive()) {

			Key<GrantedAuthorityImpl>[] authsKey = crmUser.getAuthorities();
			Map<Key<GrantedAuthorityImpl>, GrantedAuthorityImpl> authMap = userService
					.getGrantedAuthorityMap();

			if (authsKey != null) {
				for (Key<GrantedAuthorityImpl> auth : authsKey) {
					if(authMap.get(auth)!=null){
						//Make sure it is not null
						authorities.add(authMap.get(auth));
					}
				}
			}
		} else if (crmUser != null && !crmUser.isActive()) {
			
			throw new UsernameNotFoundException("User " + googleUser.getEmail()
					+ " not authorized to use this.");
		}

		// If not found in DB... check hardcoded role
		if (authorities == null || authorities.size() == 0) {

			throw new UsernameNotFoundException("User " + googleUser.getEmail()
					+ " not found");
		}
		// hard code for now

		return new PreAuthenticatedAuthenticationToken(googleUser,
				authentication.getCredentials(), authorities);

		// return super.authenticate(authentication);

	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @Override protected void additionalAuthenticationChecks(UserDetails
	 *           userDetails, UsernamePasswordAuthenticationToken token) throws
	 *           AuthenticationException { // TODO Auto-generated method stub
	 * 
	 *           }
	 * @Override protected UserDetails retrieveUser(String username,
	 *           UsernamePasswordAuthenticationToken token) throws
	 *           AuthenticationException {
	 * 
	 *           UserDetails loadedUser;
	 * 
	 *           log.info("User " + username + " trying to get autherization" );
	 * 
	 *           try { loadedUser =
	 *           this.getUserDetailsService().loadUserByUsername(username); }
	 *           catch (UsernameNotFoundException notFound) { throw notFound; }
	 *           catch (Exception repositoryProblem) { throw new
	 *           AuthenticationServiceException(repositoryProblem.getMessage(),
	 *           repositoryProblem); }
	 * 
	 *           if (loadedUser == null) { throw new
	 *           AuthenticationServiceException(
	 *           "UserDetailsService returned null, which is an interface contract violation"
	 *           ); } return loadedUser;
	 * 
	 *           }
	 **/

}
