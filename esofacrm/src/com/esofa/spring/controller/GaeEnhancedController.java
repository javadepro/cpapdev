package com.esofa.spring.controller;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.rule.RuleEngineUtils;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.GrantedAuthorityImpl;
import com.esofa.crm.service.UserService;
import com.esofa.gae.GoogleDatastoreKeyEditor;
import com.esofa.gae.security.CrmAuthentication;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.googlecode.objectify.Key;


@Controller
public class GaeEnhancedController {
	
	@Autowired
	protected UserService userService;
	
	protected boolean isAjax;

	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		
		/** Key Conversion **/
		binder.registerCustomEditor(
				com.google.appengine.api.datastore.Key.class,
				new GoogleDatastoreKeyEditor());
		
		/** Key Conversion **/
		binder.registerCustomEditor(com.googlecode.objectify.Key.class,  new KeyEditor());
		
		/** Date Conversion **/
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
		binder.registerCustomEditor(Date.class, editor);
		
		isAjax = GaeEnhancedController.isAjaxRequest(request);

	}
	
	public static boolean isAjaxRequest(HttpServletRequest request) {
		   return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}
	
	protected CrmUser getCurrentUser(){
		CrmAuthentication auth = (CrmAuthentication)SecurityContextHolder.getContext().getAuthentication();
		return auth.getCrmUser();
	}
	
	protected Key<CrmUser> getCurrentUserKey() {
		
		Key<CrmUser> userKey  = Key.create(CrmUser.class, getCurrentUser().getId());
		return userKey;
	}
	
	protected boolean currentUserHasRole(String role) {
			
		CrmUser user = 	getCurrentUser();
		Map<Key<GrantedAuthorityImpl>, GrantedAuthorityImpl> authMap = userService.getGrantedAuthorityMap();
		
		if (user.getAuthorities() == null) { return false;}
		
		for (Key<GrantedAuthorityImpl> userAuth : user.getAuthorities()) {
			
			GrantedAuthorityImpl auth = authMap.get(userAuth);
			
			if (auth == null) { return false;}
			
			if (StringUtils.equalsIgnoreCase(auth.getRole(), role)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean currentUserHasRole(List<String> rolesRequired) {
		
		for( String r : rolesRequired) {
			
			if (currentUserHasRole(r)) {
				return true;
			}
		}
		
		return false;		
	}
	
	
}
