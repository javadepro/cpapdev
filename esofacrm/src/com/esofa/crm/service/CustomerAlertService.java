package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerAlert;
import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

@Service
public class CustomerAlertService{
	private static final Logger log = Logger.getLogger(CustomerAlertService.class
			.getName());
	
	public List<CustomerAlert> getCustomerAlertByClinician(Key<CrmUser> clinician){
		return ofy().load().type(CustomerAlert.class).filter("clinician", clinician).order("alertDate").list();
	}

	public List<CustomerAlert> getCustomerAlertBefore(Date date){
		return ofy().load().type(CustomerAlert.class).filter("alertDate <=", date).order("alertDate").list();
	}
	
	public List<CustomerAlert> getCustomerAlertbyClinician(Date date, Key<CrmUser> clinician){
		return ofy().load().type(CustomerAlert.class).filter("alertDate <=", date).filter("clinician", clinician).order("alertDate").list();
	}
	
	public List<CustomerAlert> getCustomerAlertByAlertSubType(Date date, Key<AlertSubType> alertSubType){
		return ofy().load().type(CustomerAlert.class).filter("alertDate <=", date).filter("alertSubType", alertSubType).order("alertDate").list();
	}
	
	public List<CustomerAlert> getCustomerAlert(Date date, Key<CrmUser> clinician, Key<AlertSubType> alertSubType){
		return ofy().load().type(CustomerAlert.class).filter("alertDate <=", date).filter("clinician", clinician).filter("alertSubType", alertSubType).order("alertDate").list();
	}	
	
	public List<CustomerAlert> getCustomerAlert(Key<Customer> customer, Key<AlertSubType> alertSubType){
		return ofy().load().type(CustomerAlert.class).filter("alertSubType", alertSubType).filter("customer", customer).order("alertDate").list();
	}
	
	public List<CustomerAlert> getCustomerAlert(Key<Customer> customer){
		return ofy().load().type(CustomerAlert.class).filter("customer", customer).order("alertDate").list();
	}
	
	public CustomerAlert getCustomerAlertByKey(Key<CustomerAlert> key) {
		return ofy().load().key(key).now();
	}
	
	public List<CustomerAlert> getCustomerAlertByCustomerId(Long id, Key<AlertSubType> alertSubType, int numDays) {
		Key<Customer> key = Key.create(Customer.class, id);
		Date date = EsofaUtils.getDateAfterNDays(numDays);
		Query<CustomerAlert> q = ofy().load().type(CustomerAlert.class).ancestor(key).filter("alertDate <=", date);
		if(alertSubType!=null){
			q = q.filter("alertSubType", alertSubType);
		}
		return q.order("alertDate").list();
	}

	//@CacheEvict(value="customerAlert",key="#customerAlert.customer.id")
	public void saveCustomerAlert(CustomerAlert customerAlert) {
		ofy().save().entity(customerAlert).now();
	}
	
	public void deleteCustomerAlert(CustomerAlert customerAlert){
		ofy().delete().entity(customerAlert).now();
	}
	
	/**
	 * 
	 * @param id - CustomerAlert ID
	 * @param customerId - Customer ID (Also used to clear cache)
	 */
	//@CacheEvict(value="customerAlert",key="#customerId")
	public void deleteCustomerAlertByKey(Long id, Long customerId){
		Key<CustomerAlert> key = Key.create(Key.create(Customer.class, customerId), CustomerAlert.class, id);
		
		//cacheManager.getCache("customerAlert").evict(customerKey.getId());
		ofy().delete().key(key).now();
	}
}
