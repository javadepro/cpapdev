package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.esofa.crm.annotation.customer.CustomerTabIdentifier;
import com.esofa.crm.annotation.customer.CustomerTabNameE;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerCpapTrialInfo;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerHasSpecialMedicalNote;
import com.esofa.crm.model.CustomerInsuranceInfo;
import com.esofa.crm.model.CustomerInsuranceInfoType1;
import com.esofa.crm.model.CustomerInsuranceInfoType2;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.model.CustomerMedicalInfoWrapper;
import com.esofa.crm.model.CustomerPaymentInfo;
import com.esofa.crm.model.CustomerSearch;
import com.esofa.crm.model.CustomerWrapper;
import com.esofa.crm.model.report.ReferralReport;
import com.esofa.crm.refdata.model.FundingOption;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
//import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.cmd.Query;

@Service
public class CustomerService {
	private static final Logger log = Logger.getLogger(CustomerService.class
			.getName());

	private CacheManager cacheManager;

	@Cacheable(value="customerAll")
	public Map<Key<Customer>, Customer> getCustomerAll() {
		List<Key<Customer>> customerKeys = getAllCustomerKeys();
		log.info("Get all customer");
		return ofy().load().keys(customerKeys);
	}
	
	public List<Key<Customer>> getAllCustomerKeys() {
		return  ofy().load().type(Customer.class).keys().list();
	}

	public String getCustomersByQueryCursor(String startCursor, List<Customer> resultsHolder, int pageSize) {
		
		Query<Customer> q = ofy().load().type(Customer.class).limit(pageSize);

		if (StringUtils.isNotEmpty(startCursor)) {
			q.startAt(Cursor.fromWebSafeString(startCursor));
		}
		
		QueryResultIterator<Customer> qri = q.iterator();
		
		for (int i =0; i < pageSize; i++) {
			if (qri.hasNext()) {
				resultsHolder.add(qri.next());
			} else {
				break;
			}
		}

		Cursor c = qri.getCursor();
		if (c==null) { return StringUtils.EMPTY;}
		return c.toWebSafeString();
	}
	// @Cacheable(value="customer", key="id")
	public Customer getCustomerById(Long id) {
		Key<Customer> customerKey = Key.create(Customer.class, id);
		return getCustomerByKey(customerKey);
	}
	
	public Customer getCustomerByKey(Key<Customer> key){
		return ofy().load().key(key).now();	
	}
	
	public Map<Key<Customer>,Customer> getCustomersByKey(List<Key<Customer>> keys) {
		return ofy().load().keys(keys);
	}
	
	// @Cacheable(value="customer", key="id")
	public CustomerExtendedInfo getCustomerExtendedById(Long id) {
			Key<Customer> customerKey = Key.create(Customer.class, id);
			return getCustomerExtendedByKey(customerKey);
	}
	
	public void saveCustomerExtendedInfo(CustomerExtendedInfo customerExtendedInfo){
		ofy().save().entity(customerExtendedInfo).now();
	}
	
	public CustomerExtendedInfo getCustomerExtendedByKey(Key<Customer> key) {
		return ofy().load().type(CustomerExtendedInfo.class).ancestor(key).first().now();
	}

	// @CacheEvict(value="customer", key="#customer.id")
	public Customer saveCustomer(Customer customer) {
		cacheManager.getCache("customerAll").clear();
		customer.setLastUpdated(new Date());
		ofy().save().entity(customer).now();
		log.info("Customer id:" + customer.getId() + " Saved");
		return customer;
	}
	
	//delegate method. keep for readability.
	private Customer customerUpdated(Customer customer) {
		return saveCustomer(customer);
	}
	private Customer customerUpdated(Key<Customer> customerKey) {
		Customer customer = getCustomerByKey(customerKey);
		customerUpdated(customer);
		return customer;
	}
	
	public CustomerWrapper getCustomerWrapperById(Long id){
		CustomerWrapper customerWrapper = new CustomerWrapper();
		customerWrapper.setCustomer(getCustomerById(id));
		customerWrapper.setCustomerExtendedInfo(getCustomerExtendedById(id));
		return customerWrapper;
	}
	
	public CustomerWrapper getCustomerWrapperByKey(Key<Customer> key){
		CustomerWrapper customerWrapper = new CustomerWrapper();
		customerWrapper.setCustomer(getCustomerByKey(key));
		customerWrapper.setCustomerExtendedInfo(getCustomerExtendedByKey(key));
		return customerWrapper;
	}
	
	public void saveCustomerWrapper(CustomerWrapper customerWrapper){
		saveCustomer(customerWrapper.getCustomer());
		if(customerWrapper.getCustomerExtendedInfo().getCustomer()==null){
			customerWrapper.getCustomerExtendedInfo().setCustomer(Key.create(Customer.class, customerWrapper.getCustomer().getId()));
		}
		saveCustomerExtendedInfo(customerWrapper.getCustomerExtendedInfo());
	}
	
	public CustomerMedicalInfoWrapper getCustomerMedicalInfoWrapper(Key<Customer> key) {		
		Customer c = getCustomerByKey(key);
		CustomerMedicalInfoWrapper cmiw = new CustomerMedicalInfoWrapper(c);
		CustomerMedicalInfo cmi = getCustomerMedicalInfoByKey(key);
		if (cmi == null) { cmi = new CustomerMedicalInfo(c.getKey()); }
		
		CustomerCpapTrialInfo ccti = getCustomerCpapTrialInfoByKey(c.getKey());
		if (ccti == null) {ccti = new CustomerCpapTrialInfo(c.getKey());}
		
		cmiw.setCustomerMedicalInfo(cmi);
		cmiw.setCustomerCpapTrialInfo(ccti);
		
		return cmiw;		
	}
	
	public void saveCustomerMedicalInfoWrapper(CustomerMedicalInfoWrapper customerMedicalInfoWrapper, CustomerTabNameE savingFromTab) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		saveCustomerMedicalInfo(customerMedicalInfoWrapper.getCustomerMedicalInfo(),savingFromTab);
		saveCustomerCpapTrialInfo(customerMedicalInfoWrapper.getCustomerCpapTrialInfo());
		// customer updated timestamp is updated within the specific method
	}
		
	/** Insurance Related methods **/
	public CustomerInsuranceInfo getCustomerInsuranceInfoById(Long id) {
		Key<Customer> customerKey = Key.create(Customer.class, id);
		return getCustomerInsuranceInfoByKey(customerKey);
	}

	public CustomerInsuranceInfo getCustomerInsuranceInfoByKey(Key<Customer> key) {
		return ofy().load().type(CustomerInsuranceInfo.class).ancestor(key).first().now();
	}

	public void saveCustomerInsuranceInfo(
			CustomerInsuranceInfo customerInsuranceInfo) {
		ofy().save().entity(customerInsuranceInfo);
		customerUpdated(customerInsuranceInfo.getCustomer());
		log.info("Customer Insurance info for id: "
				+ customerInsuranceInfo.getCustomer() + " Saved");
	}

	/** Insurance Details **/
	public CustomerInsuranceInfoType1 getCustomerInsuranceInfoType1ById(
			Long id, Key<FundingOption> fundingOption) {
		Key<Customer> customerKey = Key.create(Customer.class, id);
		return getCustomerInsuranceInfoType1ByKey(customerKey, fundingOption);
	}

	public CustomerInsuranceInfoType1 getCustomerInsuranceInfoType1ByKey(
			Key<Customer> key, Key<FundingOption> fundingOption) {
		return ofy().load().type(CustomerInsuranceInfoType1.class).ancestor(key)
				.filter("fundingOption", fundingOption).first().now();
	}

	public void saveCustomerInsuranceInfoType1(
			CustomerInsuranceInfoType1 customerInsuranceInfo) {
		ofy().save().entity(customerInsuranceInfo).now();
		customerUpdated(customerInsuranceInfo.getCustomer());
		log.info("Customer Insurance info for id: "
				+ customerInsuranceInfo.getCustomer() + " Saved");
	}

	public void saveCustomerInsuranceInfoType1(
			List<CustomerInsuranceInfoType1> customerInsuranceInfo) {
		ofy().save().entity(customerInsuranceInfo).now();
		if( !customerInsuranceInfo.isEmpty() ) {
			customerUpdated(customerInsuranceInfo.get(0).getCustomer());	
		}		
		// log.info("Customer Insurance info for id: "+customerInsuranceInfo.getCustomer()+" Saved");
	}

	public CustomerInsuranceInfoType2 getCustomerInsuranceInfoType2ById(
			Long id, Key<FundingOption> fundingOption) {
		Key<Customer> customerKey = Key.create(Customer.class, id);
		return getCustomerInsuranceInfoType2ByKey(customerKey, fundingOption);
	}

	public CustomerInsuranceInfoType2 getCustomerInsuranceInfoType2ByKey(
			Key<Customer> key, Key<FundingOption> fundingOption) {
		return ofy().load().type(CustomerInsuranceInfoType2.class).ancestor(key)
				.filter("fundingOption", fundingOption).first().now();
	}

	public void saveCustomerInsuranceInfoType2(
			CustomerInsuranceInfoType2 customerInsuranceInfo) {
		ofy().save().entity(customerInsuranceInfo).now();
		customerUpdated(customerInsuranceInfo.getCustomer());
		log.info("Customer Insurance info for id: "
				+ customerInsuranceInfo.getCustomer() + " Saved");
	}

	/** Medical Related methods **/
	public CustomerMedicalInfo getCustomerMedicalInfoById(Long id) {
		Key<Customer> customerKey = Key.create(Customer.class, id);
		return getCustomerMedicalInfoByKey(customerKey);
	}

	public CustomerMedicalInfo getCustomerMedicalInfoByKey(Key<Customer> key) {
		return ofy().load().type(CustomerMedicalInfo.class).ancestor(key).first().now();
	}

	public Map<Key<Customer>,CustomerMedicalInfo> getCustomerMedicalInfoByKey(Set<Key<Customer>> keys) {		
		Map<Key<Customer>,CustomerMedicalInfo> cmiMap = new HashMap<Key<Customer>,CustomerMedicalInfo>();
		
		for (Key<Customer> ckey : keys) {
			 CustomerMedicalInfo cmi  = ofy().load().type(CustomerMedicalInfo.class).ancestor(ckey).first().now();
			 
			 if (cmi != null) {
				 cmiMap.put(ckey, cmi);
			 }
		}

		return cmiMap;
	}
	
	public void saveCustomerMedicalInfo(CustomerMedicalInfo customerMedicalInfo, CustomerTabNameE savingFromTab) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//determine if it is from cpap or medical tab.  
		Key<Customer> customerKey = customerMedicalInfo.getCustomer();
		CustomerMedicalInfo cmiBefore = getCustomerMedicalInfoByKey(customerKey);
		if (cmiBefore == null) { cmiBefore = new CustomerMedicalInfo(customerKey); }
		
		//special rule for referral date. 
		//if referral date is not populated, and referred by is selected, then set to current date.
		if (customerMedicalInfo.getReferredDate() == null 
				&& (StringUtils.equalsIgnoreCase("Family Doctor", customerMedicalInfo.getReferredBy())
						||StringUtils.equalsIgnoreCase("Sleep Doctor", customerMedicalInfo.getReferredBy()))) {
			
			customerMedicalInfo.setReferredDate(new Date());
		}
		
		processMedicalInfoTabOnly(cmiBefore, customerMedicalInfo, savingFromTab);
		
		ofy().save().entity(customerMedicalInfo).now();
		customerUpdated(customerKey);
		
		if(customerMedicalInfo.getSpecialMedicalNote()!=null&&!customerMedicalInfo.getSpecialMedicalNote().trim().equals("")){
			addCustomerSpecialMedicalNote(customerMedicalInfo.getCustomer());
		}else{
			removeCustomerSpecialMedicalNote(customerMedicalInfo.getCustomer());
		}
		
		log.info("Customer Medical info for id: " + customerMedicalInfo.getCustomer() + " Saved");
	}

	private void  processMedicalInfoTabOnly(CustomerMedicalInfo before, CustomerMedicalInfo after, CustomerTabNameE tabName) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	Field[] fields = after.getClass().getDeclaredFields();
    		        	
        for (Field f : fields) {        	
        	CustomerTabIdentifier annotation = f.getAnnotation(CustomerTabIdentifier.class);
        	
        	//we replace the old value when.
        	//annotation is present and not the same name.
        	//we assume that the form only passes in fields marked for it.
        	if (annotation != null  && !tabName.equals(annotation.usedIn())) { 
        		String getterName = new StringBuilder("get").append(StringUtils.capitalize(f.getName())).toString();
           		Object v = before.getClass().getMethod(getterName).invoke(before);
            		
           		if (v != null) { 
           			BeanUtils.copyProperty(after, f.getName(),v);
           		}
           	}
        }    
	}	
	
	public Boolean customerHasSpecialMedicalNote(Key<Customer> key){
		CustomerHasSpecialMedicalNote hasSpecialMedicalNote = getCustomerSpecialMedicalNote(key);
		if( hasSpecialMedicalNote!=null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	
	public CustomerHasSpecialMedicalNote getCustomerSpecialMedicalNote(Key<Customer> key){
		return ofy().load().type(CustomerHasSpecialMedicalNote.class).ancestor(key).first().now();
	}
	
	public void removeCustomerSpecialMedicalNote(Key<Customer> key){
		CustomerHasSpecialMedicalNote note = getCustomerSpecialMedicalNote(key);
		if(note!=null){
			ofy().delete().entity(note).now();
			customerUpdated(note.getCustomer());
		}
	}
	
	public void addCustomerSpecialMedicalNote(Key<Customer> key){
		CustomerHasSpecialMedicalNote hasSpecialMedicalNote = new CustomerHasSpecialMedicalNote();
		hasSpecialMedicalNote.setCustomer(key);
		ofy().save().entity(hasSpecialMedicalNote).now();
		customerUpdated(hasSpecialMedicalNote.getCustomer());
	}
	
	public CustomerCpapTrialInfo getCustomerCpapTrialInfoById(Long id) {
		Key<Customer> customerKey = Key.create(Customer.class, id);
		return getCustomerCpapTrialInfoByKey(customerKey);
	}

	public CustomerCpapTrialInfo getCustomerCpapTrialInfoByKey(Key<Customer> key) {
		return ofy().load().type(CustomerCpapTrialInfo.class).ancestor(key).first().now();
	}

	public void saveCustomerCpapTrialInfo(CustomerCpapTrialInfo customerCpapTrialInfo) {
		ofy().save().entity(customerCpapTrialInfo).now();
		if( customerCpapTrialInfo != null ) {
			customerUpdated(customerCpapTrialInfo.getCustomer());
		}
		log.info("Customer Cpap Trial info for id: " + customerCpapTrialInfo.getCustomer() + " Saved");
	}

	/** Lazy Search **/
	public  Map<Key<Customer>,Customer> search(String substring){
		substring = substring.toUpperCase();
		Map<Key<Customer>,Customer> customers = this.getCustomerAll();
		for (Iterator<Map.Entry<Key<Customer>, Customer>> i = customers.entrySet()
				.iterator(); i.hasNext();) {
			Entry<Key<Customer>, Customer> entry = i.next();
			if (!entry.getValue().getLastname().contains(substring)&&!entry.getValue().getFirstname().contains(substring)) {
				i.remove();
			}
		}
		return customers;
	}
		
	/** Search **/
	public Map<Key<Customer>,Customer> search(CustomerSearch customerSearch){
		int wildCard = customerSearch.hasWildCard();
		int fieldSet = customerSearch.fieldSet();
		if(wildCard==0){
			return queryNoWildCard(customerSearch);
		}else if(wildCard==1&&fieldSet==1){
			return queryOneWildCard(customerSearch);
		}else if(wildCard==fieldSet){
			Map<Key<Customer>,Customer> result = queryOneWildCard(customerSearch);
			return filterResult(result, customerSearch);	
		}else{
			Map<Key<Customer>,Customer> result = querySkipWildCard(customerSearch);
			return filterResult(result, customerSearch);	
		}
	}

	/**
	 * GAE will only allow one in eq
	 */
	private Map<Key<Customer>, Customer> queryNoWildCard(
			CustomerSearch exampleObj) {
		Query<Customer> q = ofy().load().type(Customer.class);

		boolean foundIneq = false;

		// Add all non-null properties to query filter
		for (Field field : CustomerSearch.class.getDeclaredFields()) {
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Ignore.class)
					//|| (field.getType().isAnnotationPresent(Embed.class))
					|| (field.getType().isArray())
					|| (Collection.class.isAssignableFrom(field.getType()))
					|| ((field.getModifiers() & BAD_MODIFIERS) != 0))
				continue;

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(exampleObj);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (value != null) {
				if (value instanceof String) {
					if (value.toString().trim().equals("")) {
						continue;
					}
				}
				q = q.filter(field.getName(), value);
			}
		}
		List<Key<Customer>> keys = q.keys().list();
		return ofy().load().keys(keys);
	}

	private Map<Key<Customer>, Customer> queryOneWildCard(
			CustomerSearch exampleObj) {
		Query<Customer> q = ofy().load().type(Customer.class);

		boolean foundIneq = false;

		// Add all non-null properties to query filter
		for (Field field : CustomerSearch.class.getDeclaredFields()) {
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Ignore.class)
					//|| (field.getType().isAnnotationPresent(Embed.class))
					|| (field.getType().isArray())
					|| (Collection.class.isAssignableFrom(field.getType()))
					|| ((field.getModifiers() & BAD_MODIFIERS) != 0))
				continue;

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(exampleObj);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (value != null) {
				if (value instanceof String) {
					if(foundIneq){
						continue;
					}
					if (!value.toString().trim().equals("")&&value.toString().trim().contains("*")){
								String searchString = value.toString().trim().replace("*","");
								q.filter(field.getName()+" >=",searchString).filter(field.getName()+" <",searchString+"\uFFFD"); 
								foundIneq = true;	
					}
					continue;
				}
				q = q.filter(field.getName(), value);
			}
		}
		List<Key<Customer>> keys = q.keys().list();
		return ofy().load().keys(keys);
	}
	
	/**
	 * GAE will only allow one in eq
	 */
	private Map<Key<Customer>, Customer> querySkipWildCard(
			CustomerSearch exampleObj) {
		Query<Customer> q = ofy().load().type(Customer.class);

		boolean foundIneq = false;

		// Add all non-null properties to query filter
		for (Field field : CustomerSearch.class.getDeclaredFields()) {
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Ignore.class)
					//|| (field.getType().isAnnotationPresent(Embed.class))
					|| (field.getType().isArray())
					|| (Collection.class.isAssignableFrom(field.getType()))
					|| ((field.getModifiers() & BAD_MODIFIERS) != 0))
				continue;

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(exampleObj);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (value != null) {
				if (value instanceof String) {
					if (value.toString().trim().equals("")||value.toString().trim().endsWith("*")) {
						continue;
					}
				}
				q = q.filter(field.getName(), value);
			}
		}
		List<Key<Customer>> keys = q.keys().list();
		return ofy().load().keys(keys);
	}

	private Map<Key<Customer>, Customer> filterResult(
			Map<Key<Customer>, Customer> result, CustomerSearch customerSearch) {

		String searchString = customerSearch.getLastname().replace("*", "");
		for (Iterator<Map.Entry<Key<Customer>, Customer>> i = result.entrySet()
				.iterator(); i.hasNext();) {
			Entry<Key<Customer>, Customer> entry = i.next();
			if (!entry.getValue().getLastname().startsWith(searchString)) {
				i.remove();
			}
		}
		searchString = customerSearch.getFirstname().replace("*", "");
		for (Iterator<Map.Entry<Key<Customer>, Customer>> i = result.entrySet()
				.iterator(); i.hasNext();) {
			Entry<Key<Customer>, Customer> entry = i.next();
			if (!entry.getValue().getFirstname().startsWith(searchString)) {
				i.remove();
			}
		}
		searchString = customerSearch.getHealthCardNumber().replace("*", "");
		for (Iterator<Map.Entry<Key<Customer>, Customer>> i = result.entrySet()
				.iterator(); i.hasNext();) {
			Entry<Key<Customer>, Customer> entry = i.next();
			if (!entry.getValue().getHealthCardNumber()
					.startsWith(searchString)) {
				i.remove();
			}
		}
		searchString = customerSearch.getPhoneHome().replace("*", "");
		for (Iterator<Map.Entry<Key<Customer>, Customer>> i = result.entrySet()
				.iterator(); i.hasNext();) {
			Entry<Key<Customer>, Customer> entry = i.next();
			if (!entry.getValue().getPhoneHome().startsWith(searchString)) {
				i.remove();
			}
		}
		searchString = customerSearch.getPhoneOffice().replace("*", "");
		for (Iterator<Map.Entry<Key<Customer>, Customer>> i = result.entrySet()
				.iterator(); i.hasNext();) {
			Entry<Key<Customer>, Customer> entry = i.next();
			if (!entry.getValue().getPhoneOffice().startsWith(searchString)) {
				i.remove();
			}
		}
		searchString = customerSearch.getPhoneMobile().replace("*", "");
		for (Iterator<Map.Entry<Key<Customer>, Customer>> i = result.entrySet()
				.iterator(); i.hasNext();) {
			Entry<Key<Customer>, Customer> entry = i.next();
			if (!entry.getValue().getPhoneMobile().startsWith(searchString)) {
				i.remove();
			}
		}
		return result;
	}
	
	/** Payment Related methods **/
	public CustomerPaymentInfo getCustomerPaymentInfoById(Long id) {
		Key<Customer> customerKey = Key.create(Customer.class, id);
		return getCustomerPaymentInfoByKey(customerKey);
	}

	public CustomerPaymentInfo getCustomerPaymentInfoByKey(Key<Customer> key) {
		return ofy().load().type(CustomerPaymentInfo.class).ancestor(key).first().now();
	}

	public void saveCustomerPaymentInfo(CustomerPaymentInfo customerPaymentInfo) {
		ofy().save().entity(customerPaymentInfo).now();
	}
	
	public List<ReferralReport> getReferrals(Date from, Date to, String referralType) {		
		List<CustomerMedicalInfo> rawResults;		
		Map<Key,Integer> groupByMap;
		 
		rawResults = ofy().load().type(CustomerMedicalInfo.class).filter("referredDate >=", from).filter("referredDate <", to).filter("referredBy", referralType).list();
		
		groupByMap = new LinkedHashMap<Key,Integer>();
		
		for (CustomerMedicalInfo cmi : rawResults) {
			
			Key doctorKey=null;
			
			if (StringUtils.equalsIgnoreCase("Family Doctor", referralType)) {
				
				doctorKey = cmi.getFamilyDoctor();
			} else if (StringUtils.equalsIgnoreCase("Sleep Doctor", referralType)) {
				
				doctorKey = cmi.getSleepDoctor();
			}
			
			//group by doctor key
			if (!groupByMap.containsKey(doctorKey)) {
				
				groupByMap.put(doctorKey, 1);
			} else {

				Integer count = groupByMap.get(doctorKey);
				count +=1;
				groupByMap.put(doctorKey, count);					
			}
			
		}
			
		List<ReferralReport> results = new ArrayList<ReferralReport>();		
		
		for (Entry<Key,Integer> entry : groupByMap.entrySet()) {	
			results.add(new ReferralReport(entry.getKey(), entry.getValue()));
		}
		
		return results;
	}


	public List<CustomerMedicalInfo> getMedicalInfoByRefrralDate(Date from, Date to) {		
		List<CustomerMedicalInfo> rawResults;				
		rawResults = ofy().load().type(CustomerMedicalInfo.class).filter("referredDate >=", from).filter("referredDate <", to).list();		
		return rawResults;
	}
	
	static final int BAD_MODIFIERS = Modifier.FINAL | Modifier.STATIC
			| Modifier.TRANSIENT;

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}
