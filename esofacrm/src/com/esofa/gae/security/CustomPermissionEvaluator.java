package com.esofa.gae.security;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.esofa.crm.model.Customer;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.CustomerProfileTempAccess;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.UserService;
import com.googlecode.objectify.Key;

public class CustomPermissionEvaluator implements PermissionEvaluator{
	
	private static final Logger log = Logger.getLogger(CustomPermissionEvaluator.class
			.getName());
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	RefdataService refdataService; 
	
	@Autowired
	UserService userService;
	

	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		return false;
	}


	public boolean hasPermission(Authentication authentication, Serializable targetId,
			String targetType, Object permission) {
		
		// check if clinician allow to read customer profile
		if(targetType.toLowerCase().startsWith("customer")){
			Customer customer = customerService.getCustomerById((Long)targetId);
			CrmUser crmUser = ((CrmAuthentication)authentication).getCrmUser();
			
			// Check if clinician can access the shop or not?!
			long targetShopId;
			
			
			if(customer!=null&&customer.getClinician()!=null){
				if(customer.getClinician().getId()==crmUser.getId())
					//targetShopId = customer.getPreferredLocation().getId();
					return true;
			}
				
			
			/**
			 * 
			if(customer!=null&&customer.getPreferredLocation()!=null)
				targetShopId = customer.getPreferredLocation().getId();
			else
				return false;
			Key<Shop>[] shops = crmUser.getShops();
			
			for(Key<Shop> shop : shops){
				if(shop.getId()==targetShopId){
					return true;
				}
			}
			 */
			
			// Check temp rule
			Key<CrmUser> crmUserKey = Key.create(CrmUser.class, crmUser.getId());
			List<CustomerProfileTempAccess> tempAccesses = userService.getCustomerProfileTempAccessByCrmUserKey(crmUserKey);
			for(CustomerProfileTempAccess tempAccess : tempAccesses){
				log.info("Proccessing Temp Rule :"
						+"rule customer ID "+tempAccess.getCustomer().getId()+"\n"
						+ "customer Id"+customer.getId());
				if(tempAccess.getCustomer().getId()==customer.getId()){
					return true;
				}
			}
		}
		return false;
	}

}
