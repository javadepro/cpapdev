package com.esofa.gae.security;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import com.esofa.crm.security.user.model.AllowedIP;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.GrantedAuthorityImpl;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class GaeAuthenticationFilter extends GenericFilterBean {

	private static final Logger log = Logger
			.getLogger(GaeAuthenticationFilter.class.getName());

	@Autowired
	com.esofa.crm.service.UserService userService;

	private AuthenticationDetailsSource ads = new WebAuthenticationDetailsSource();
	private AuthenticationManager authenticationManager;
	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	public com.esofa.crm.service.UserService getUserService() {
		return userService;
	}

	public void setUserService(com.esofa.crm.service.UserService userService) {
		this.userService = userService;
	}


	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		//log.info("filter");

		HttpServletRequest req = (HttpServletRequest) request;

		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		if (authentication == null) {
			User googleUser = UserServiceFactory.getUserService()
					.getCurrentUser();

			if (googleUser != null) {
				logger.debug("Currently logged on to GAE as user " + googleUser);
				logger.debug("Authenticating to Spring Security");
				// User has returned after authenticating via GAE. Need to
				// authenticate through Spring Security.
				PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
						googleUser, null);
				token.setDetails(ads.buildDetails(request));

				try {
					authentication = authenticationManager.authenticate(token);
					CrmAuthentication auth = enrichUser(googleUser.getEmail(),
							authentication);

					/** see if this guy is admin **/
					boolean isAdmin = false;
					boolean inWhiteList = false;
					@SuppressWarnings("unchecked")
					Collection<GrantedAuthorityImpl> authorities = (Collection<GrantedAuthorityImpl>) authentication
							.getAuthorities();
					for (GrantedAuthorityImpl authority : authorities) {
						if (authority.getAuthority().equals("ROLE_ADMIN")
								|| authority.getAuthority()
										.equals("ROLE_SUPER")
								|| authority.getAuthority().equals(
										"ROLE_REMOTE")) {
							isAdmin = true;
							break;
						}
					}
					/** If not admin, lets check IP **/
					if (!isAdmin) {
						String currentIP = req.getRemoteAddr();
						Collection<AllowedIP> ips = userService
								.getAllowedIPMap().values();
						for (AllowedIP ip : ips) {
							log.info("IP comparing:" + ip.getIpAddress()
									+ " current:" + currentIP);
							if (StringUtils
									.equalsIgnoreCase(currentIP,
											ip.getIpAddress())) {
								inWhiteList = true;
								break;
							}
						}
						// Not in admin, not in whitelist.. then bye bye..
						if (!inWhiteList) {
							HttpSession session = req.getSession(true);
							session.invalidate();
							throw new UsernameNotFoundException("User "
									+ googleUser.getEmail() + " IP:"
									+ currentIP + " is not allowed");
						}
					}
					// Set the auth if everything is ok
					SecurityContextHolder.getContext().setAuthentication(auth);

				} catch (AuthenticationException e) {
					HttpSession session = req.getSession(true);
					session.invalidate();
					failureHandler.onAuthenticationFailure(
							(HttpServletRequest) request,
							(HttpServletResponse) response, e);

					return;
				}
			}
		}

		chain.doFilter(request, response);
	}

	private CrmAuthentication enrichUser(String email,
			Authentication authentication) {
		CrmUser crmUser = userService.getCrmUserByEmail(email);
		CrmAuthentication crmAuthentication = new CrmAuthentication(
				authentication, crmUser);
		return crmAuthentication;
	}
}