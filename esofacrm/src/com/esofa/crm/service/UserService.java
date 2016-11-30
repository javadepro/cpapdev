package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.esofa.crm.model.Customer;
import com.esofa.crm.security.user.model.AllowedIP;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.CustomerProfileTempAccess;
import com.esofa.crm.security.user.model.GrantedAuthorityImpl;
import com.esofa.crm.security.user.model.UserPasscode;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

@Service
public class UserService {
	@Autowired
	private EhCacheCacheManager cacheManager;
	
	/** GrantedAuthorityImpl **/
	@Cacheable(value = "grantedAuthority")
	public Map<Key<GrantedAuthorityImpl>, GrantedAuthorityImpl> getGrantedAuthorityMap() {
		List<Key<GrantedAuthorityImpl>> keys = ofy().load().type(GrantedAuthorityImpl.class).keys().list();
		return ofy().load().keys(keys);
	}
	
	protected GrantedAuthorityImpl getGrantedAuthority(String role) {
		
		Map<Key<GrantedAuthorityImpl>, GrantedAuthorityImpl> auths = getGrantedAuthorityMap();
		
		for ( GrantedAuthorityImpl auth:  auths.values() ) {
			
			if (StringUtils.equalsIgnoreCase(role, auth.getRole())) {
				return auth;
			}
		}
		
		return null;
	}

	@Cacheable(value="grantedAuthority", key="#id")
	public GrantedAuthorityImpl getGrantedAuthorityById(Long id) {
		return ofy().load().type(GrantedAuthorityImpl.class).id(id).now();
	}

	@CacheEvict(value = "grantedAuthority", allEntries=true)
	public void saveGrantedAuthority(GrantedAuthorityImpl grantedAuthorityImpl) {
		ofy().save().entity(grantedAuthorityImpl).now();
	}	

	/** CrmUser **/
	@Cacheable(value = "crmUser")
	public Map<Key<CrmUser>, CrmUser> getCrmUserMap(boolean getActiveOnly) {
		List<Key<CrmUser>> keys = ofy().load().type(CrmUser.class).order("lastname").order("firstname").keys().list();
		
		Map<Key<CrmUser>, CrmUser> crmUsers = ofy().load().keys(keys);
		
		if (getActiveOnly) {

			Map<Key<CrmUser>, CrmUser> crmUsersActive = new LinkedHashMap<Key<CrmUser>, CrmUser>();
			
			for(Key<CrmUser> key: crmUsers.keySet()){
				if(crmUsers.get(key).isActive()){
					crmUsersActive.put(key, crmUsers.get(key));
				}
			}

			crmUsers = crmUsersActive;
		}
		
		return crmUsers;
	}	

	@Cacheable(value="crmUser", key="#id")
	public CrmUser getCrmUserById(Long id) {
		return ofy().load().type(CrmUser.class).id(id).now();
	}
	

	@CacheEvict(value = "crmUser", allEntries=true)
	public void saveCrmUser(CrmUser crmUser) {
		cacheManager.getCache("crmUserAll").clear();
		ofy().save().entity(crmUser).now();
	}
	
	public CrmUser getCrmUserByEmail(String email){
		email = email.trim().toLowerCase();		
		return ofy().load().type(CrmUser.class).filter("email", email).first().now();
	}
	
	@CacheEvict(value="crmUser", allEntries=true)
	public void deleteCrmUserById(Long id) {
		ofy().delete().type(CrmUser.class).id(id).now();
		cacheManager.getCache("crmUserAll").clear();
	}
	
	public Map<Key<CrmUser>,CrmUser> getPosManagers() {
		GrantedAuthorityImpl auth = getGrantedAuthority("ROLE_POS_MGR");
		
		Key<GrantedAuthorityImpl> roleKey = Key.create(GrantedAuthorityImpl.class, auth.getId());
		return getCrmUsersByRole(roleKey);
	}

	public Map<Key<CrmUser>,CrmUser> getInventoryManager() {
		
		GrantedAuthorityImpl auth = getGrantedAuthority("ROLE_INVENTORY_EMAIL");
		Key<GrantedAuthorityImpl> roleKey = Key.create(GrantedAuthorityImpl.class, auth.getId());
		return getCrmUsersByRole(roleKey);		
	}
	
	public Map<Key<CrmUser>,CrmUser> getCrmUsersByRole(Key<GrantedAuthorityImpl> roleKey) {
		List<Key<CrmUser>> keys = ofy().load().type(CrmUser.class).filter("authorities",roleKey).keys().list();
		return ofy().load().keys(keys);
	}	
	
	/** User IP Address **/
	@Cacheable(value = "allowedIPAll")
	public Map<Key<AllowedIP>, AllowedIP> getAllowedIPMap() {
		List<Key<AllowedIP>> keys = ofy().load().type(AllowedIP.class).keys().list();
		return ofy().load().keys(keys);
	}
	
	@Cacheable(value="allowedIP", key="#id")
	public AllowedIP getAllowedIPById(Long id) {
		return ofy().load().type(AllowedIP.class).id(id).now();
	}
	
	@CacheEvict(value = "allowedIP", key="#allowedIP.id")
	public void saveAllowedIP(AllowedIP allowedIP) {
		cacheManager.getCache("allowedIPAll").clear();
		ofy().save().entity(allowedIP).now();
	}
	@CacheEvict(value="allowedIP", key="#id")
	public void deleteAllowedIPById(Long id) {
		ofy().delete().type(AllowedIP.class).id(id).now();
		cacheManager.getCache("allowedIPAll").clear();
	}
	
	/** Customer Profile Temp Access Address **/
	@Cacheable(value = "customerProfileTempAccess")
	public Map<Key<CustomerProfileTempAccess>, CustomerProfileTempAccess> getCustomerProfileTempAccessMap() {
		List<Key<CustomerProfileTempAccess>> keys = ofy().load().type(CustomerProfileTempAccess.class).keys().list();
		return ofy().load().keys(keys);
	}
	
	@Cacheable(value="customerProfileTempAccess", key="#id")
	public CustomerProfileTempAccess getCustomerProfileTempAccessById(Long id) {
		return ofy().load().type(CustomerProfileTempAccess.class).id(id).now();
	}
	
	@Cacheable(value="customerProfileTempAccess", key="#crmUserKey.id")
	public List<CustomerProfileTempAccess> getCustomerProfileTempAccessByCrmUserKey(Key<CrmUser> crmUserKey) {
		return ofy().load().type(CustomerProfileTempAccess.class).filter("crmUser", crmUserKey).list();
	}
	
	@CacheEvict(value = "customerProfileTempAccess",allEntries=true)
	public void saveCustomerProfileTempAccess(CustomerProfileTempAccess customerProfileTempAccess) {
		cacheManager.getCache("customerProfileTempAccessAll").clear();
		ofy().save().entity(customerProfileTempAccess).now();
	}
	
	@CacheEvict(value = "customerProfileTempAccess",allEntries=true)
	public void deleteCustomerProfileTempAccessById(Long id) {
		ofy().delete().type(CustomerProfileTempAccess.class).id(id).now();
		cacheManager.getCache("customerProfileTempAccessAll").clear();
	}
	
	public String generateUserPasscodeHash( Key<Customer> customerKey, Key<CrmUser> crmUserKey ) {
		
		UserPasscode userPasscode = getUserPasscode(crmUserKey);
		if(userPasscode == null) {
			throw new IllegalArgumentException("not able to find crmUser's passcode");
		}
		
		String str_userPasscode = userPasscode.getPassCode();
		String str_customerKey = StringUtils.EMPTY;
		
		if (customerKey != null) { 
			str_customerKey = String.valueOf(customerKey.getId());
		}		
		
		String digest = DigestUtils.md5Hex(str_userPasscode + str_customerKey);
		return digest;
	}
	
	public boolean matchUserPasscodeHash(String actualHash, Key<Customer> customerKey, Key<CrmUser> crmUserKey) {
		
		String expectedHash = generateUserPasscodeHash(customerKey, crmUserKey);
		
		if (StringUtils.equalsIgnoreCase(expectedHash, actualHash)) {
			return true;
		}
		
		return false;
	}
	
	public boolean matchUserPasscode(UserPasscode userPasscode) {
		
		UserPasscode up = getUserPasscode(userPasscode.getCrmUser());
		if (up == null) { return false;}		
		if (StringUtils.isEmpty(up.getPassCode())) { return false;}
		
		if (StringUtils.equalsIgnoreCase(userPasscode.getPassCode(), up.getPassCode())) {
			return true;
		}
		
		return false;
	}
	
	public void saveUserPasscode(UserPasscode userPasscode) {
		//if existing, then replace and update
		UserPasscode up = getUserPasscode(userPasscode.getCrmUser());
		if (up != null) {
			String newPasscode = userPasscode.getPassCode();
			userPasscode = up;
			userPasscode.setPassCode(newPasscode);
		}
		ofy().save().entities(userPasscode);
	}
	
	private UserPasscode getUserPasscode(Key<CrmUser> crmUserKey) {
		UserPasscode up =null;
		
		try {
			up = ofy().load().type(UserPasscode.class).filter("crmUser", crmUserKey).first().now();
		} catch (NotFoundException e) {
			up = null;
		}
		return up;
	}
 
	public void setCacheManager(EhCacheCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}	
}
