package com.esofa.crm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.AllowedIP;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.CustomerProfileTempAccess;
import com.esofa.crm.security.user.model.GrantedAuthorityImpl;
import com.esofa.crm.security.user.model.UserPasscode;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.UserService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/user")
public class UserController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(ProductController.class
			.getName());

	@Autowired private UserService userService;
	@Autowired private RefdataService refdataService;
	@Autowired private CustomerService customerService; 
	
	
	@RequestMapping(value = "/role/view", method = RequestMethod.GET)
	public ModelAndView viewRole(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("authority",userService.getGrantedAuthorityById(id));
		
		mav.setViewName("user-role-view");
		return mav;
	}
	
	@RequestMapping(value = {"/role/list","/role/","/role"}, method = RequestMethod.GET)
	public ModelAndView listRole() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("authorities",userService.getGrantedAuthorityMap());
		mav.setViewName("user-role-list");

		return mav;
	}
	
	@RequestMapping(value = "/role/form", method = RequestMethod.GET)
	public ModelAndView formRole(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		if(id==null||id==0){
			mav.addObject("authority",new GrantedAuthorityImpl());

		}else{
		mav.addObject("authority",userService.getGrantedAuthorityById(id));
		}
		mav.setViewName("user-role-form");

		return mav;
	}
	
	@RequestMapping(value = "/role/formsubmit", method = RequestMethod.POST)
	public ModelAndView formsubmitRole(@ModelAttribute("user") @Valid GrantedAuthorityImpl authority,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
		}else{
			userService.saveGrantedAuthority(authority);
			mav.addObject("message","Saved");
		}
		mav.addObject("authority",authority);
		mav.setViewName("user-role-form");
		return mav;
	}
	
	@RequestMapping(value = "/ip/view", method = RequestMethod.GET)
	public ModelAndView viewIP(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("allowedIP",userService.getAllowedIPById(id));
		
		mav.setViewName("user-ip-view");
		return mav;
	}
	
	@RequestMapping(value = {"/ip/list","/ip/","/ip"}, method = RequestMethod.GET)
	public ModelAndView listAllowedIP() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("allowedIPs",userService.getAllowedIPMap());
		mav.setViewName("user-ip-list");

		return mav;
	}
	
	@RequestMapping(value = "/ip/form", method = RequestMethod.GET)
	public ModelAndView formAllowedIP(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		if(id==null||id==0){
			mav.addObject("allowedIP", new AllowedIP());
		}else{
			mav.addObject("allowedIP",userService.getAllowedIPById(id));
		}
		mav.setViewName("user-ip-form");

		return mav;
	}
	
	@RequestMapping(value = "/ip/delete", method = RequestMethod.GET)
	public ModelAndView deleteAllowedIPAccess(@RequestParam(required = true) Long id) {
		
		userService.deleteAllowedIPById(id);
		ModelAndView mav = listAllowedIP();

		mav.addObject("message","Allowed IP  Deleted");
		
		return mav;
	}
	
	@RequestMapping(value = "/ip/formsubmit", method = {RequestMethod.POST})
	public ModelAndView formsubmitAllowedIP(@ModelAttribute("allowedIP") @Valid AllowedIP allowedIP,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			userService.saveAllowedIP(allowedIP);
			mav.addObject("message","Saved");
		}
		
		mav.addObject("allowedIP",allowedIP);
		mav.setViewName("user-ip-form");
		return mav;
	}
	
	/** Temp Access Controller **/
	@RequestMapping(value = {"/temp-access/list","/temp-access/","/temp-access"}, method = RequestMethod.GET)
	public ModelAndView listCustomerProfileTempAccess() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("tempAccesses",userService.getCustomerProfileTempAccessMap());
		mav.addObject("customers", customerService.getCustomerAll());
		mav.addObject("clinicians", refdataService.getClinicianMap());
		mav.setViewName("user-temp-access-list");

		return mav;
	}
	
	@RequestMapping(value = "/temp-access/form", method = RequestMethod.GET)
	public ModelAndView formCustomerProfileTempAccess(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		if(id==null||id==0){
			mav.addObject("tempAccess", new CustomerProfileTempAccess());
		}else{
			mav.addObject("tempAccess",userService.getCustomerProfileTempAccessById(id));
		}
		mav.addObject("customers", customerService.getCustomerAll());
		mav.addObject("clinicians", refdataService.getClinicianMap());
		mav.setViewName("user-temp-access-form");

		return mav;
	}
	
	@RequestMapping(value = "/temp-access/view", method = RequestMethod.GET)
	public ModelAndView viewCustomerProfileTempAccess(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("tempAccess",userService.getCustomerProfileTempAccessById(id));
		mav.addObject("customers", customerService.getCustomerAll());
		mav.addObject("clinicians", refdataService.getClinicianMap());
		mav.setViewName("user-temp-access-view");

		return mav;
	}
	
	@RequestMapping(value = "/temp-access/delete", method = RequestMethod.GET)
	public ModelAndView deleteCustomerProfileTempAccess(@RequestParam(required = true) Long id) {
		
		userService.deleteCustomerProfileTempAccessById(id);
		ModelAndView mav = listCustomerProfileTempAccess();

		mav.addObject("message","Temp Access Rule Deleted");

		return mav;
	}
	
	@RequestMapping(value = "/temp-access/formsubmit", method = {RequestMethod.POST})
	public ModelAndView formsubmitCustomerProfileTempAccess(@ModelAttribute("customerProfileTempAccess") @Valid CustomerProfileTempAccess customerProfileTempAccess,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			userService.saveCustomerProfileTempAccess(customerProfileTempAccess);
			mav.addObject("message","Saved");
		}
		
		mav.addObject("tempAccess",customerProfileTempAccess);
		mav.addObject("customers", customerService.getCustomerAll());
		mav.addObject("clinicians", refdataService.getClinicianMap());
		mav.setViewName("user-temp-access-form");
		return mav;
	}
	
	
	
	@RequestMapping(value = "/crmuser/view", method = RequestMethod.GET)
	public ModelAndView viewCrmUser(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("crmUser",userService.getCrmUserById(id));
		mav.addObject("authorities", userService.getGrantedAuthorityMap());
		mav.addObject("shops", refdataService.getShopMap());

		mav.setViewName("user-crmuser-view");
		return mav;
	}
	
	@RequestMapping(value = {"/crmuser/list","/crmuser/","/crmuser"}, method = RequestMethod.GET)
	public ModelAndView listCrmUser(@RequestParam(required=false) boolean getActiveOnly) {
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("crmUsers",userService.getCrmUserMap(getActiveOnly));
		mav.setViewName("user-crmuser-list");

		return mav;
	}
	
	@RequestMapping(value = "/crmuser/form", method = RequestMethod.GET)
	public ModelAndView formCrmUser(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		if(id==null||id==0){
			mav.addObject("crmUser", new CrmUser());
		}else{
			mav.addObject("crmUser",userService.getCrmUserById(id));
		}
		mav.addObject("authorities", userService.getGrantedAuthorityMap());
		mav.addObject("shops", refdataService.getActiveShopMap());

		mav.setViewName("user-crmuser-form");

		return mav;
	}
	
	@RequestMapping(value = "/crmuser/delete", method = RequestMethod.GET)
	public ModelAndView deleteCrmUser(@RequestParam(required = true) Long id) {
		
		userService.deleteCrmUserById(id);
		ModelAndView mav = listCrmUser(true);

		mav.addObject("message","CRM User Deleted");

		return mav;
	}
	
	@RequestMapping(value = "/crmuser/inactive", method = RequestMethod.GET)
	public ModelAndView deactivateCrmUser(@RequestParam(required = true) Long id) {
		
		CrmUser user = userService.getCrmUserById(id);
		user.setActive(false);
		userService.saveCrmUser(user);
		ModelAndView mav = listCrmUser(true);

		mav.addObject("message","CRM User is now inactive");

		return mav;
	}
	
	
	
	@RequestMapping(value = "/crmuser/formsubmit", method = {RequestMethod.POST})
	public ModelAndView formsubmitCrmUser(@ModelAttribute("crmUser") @Valid CrmUser crmUser,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			userService.saveCrmUser(crmUser);
			mav.addObject("message","Saved");
		}
		mav.addObject("crmUser",crmUser);
		mav.addObject("authorities", userService.getGrantedAuthorityMap());
		mav.addObject("shops", refdataService.getActiveShopMap());

		mav.setViewName("user-crmuser-form");
		return mav;
	}
	
	@RequestMapping(value="/passcode/form",method=RequestMethod.GET)
	public ModelAndView formPasscode() {
	
		ModelAndView mav = new ModelAndView();
		CrmUser crmUser = getCurrentUser();
		mav.addObject("crmUser",crmUser);
		mav.addObject("userPasscode",new UserPasscode());
		mav.setViewName("user-passcode-form");
		return mav;
	}
	
	@RequestMapping(value = "/passcode/formsubmit", method = {RequestMethod.POST})
	public ModelAndView formsubmitPasscode(@ModelAttribute("userPasscode") UserPasscode userPasscode,
			BindingResult result) {

		ModelAndView mav = formPasscode();
		
		if (userPasscode != null) {
			if (!StringUtils.equals(userPasscode.getPassCode(), userPasscode.getReTypePassCode()) ) {
				result.addError(new FieldError("userPasscode", "userPasscode", "passcode and retype passcode do not match"));
		
			}
		}
		
		if(result.hasErrors()){
				//no clue why i have to manually do it for this one.
				List<String> errors = new ArrayList<String>();
				
				for (ObjectError err : result.getAllErrors()) {
					errors.add(err.getDefaultMessage());
				}
				mav.addObject("errors",errors);
			
		}else{
			userPasscode.setCrmUser(getCurrentUserKey());
			userService.saveUserPasscode(userPasscode);
			mav.addObject("message","Saved");
		}
		
		return mav;
	}
	
}
