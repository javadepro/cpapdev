package com.esofa.crm.queuejob;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerWrapper;
import com.esofa.crm.service.CpapMailerLiteService;
import com.esofa.crm.service.CustomerService;
import com.esofa.gae.queue.QueueableTask;
import com.googlecode.objectify.Key;

/**
 * goes through all the customers and makes sure their mailerlite settings
 * are up to date.
 * @author JHa
 *
 */
public class RefreshMailerLiteTask implements QueueableTask {


	private static final Logger log = Logger
			.getLogger(RefreshMailerLiteTask.class.getName());
	
	private static final String IS_INITIALIZED = "init-done";
	private Iterator<Key<Customer>> iterCustomer;

	private CpapMailerLiteService mailerLiteService;
	private CustomerService customerService;

	protected void initTask(Map<String, String> params) {
		
		params.put(IS_INITIALIZED, IS_INITIALIZED);
		iterCustomer = customerService.getAllCustomerKeys().iterator();		
	}
	
	@Override
	public Map<String, String> executeTask(Map<String, String> params) {
		
		//do stuff for first iteration.
		String initDone = params.get(IS_INITIALIZED);		
		if (StringUtils.isEmpty(initDone)) {
			
			initTask(params);
		}
		
		//actually do the work
		if (iterCustomer == null || !iterCustomer.hasNext()) {
			return null;
		}
		
		Key<Customer> customerKey = iterCustomer.next();
		CustomerWrapper cw = customerService.getCustomerWrapperByKey(customerKey);		
		log.severe("entering " + cw.getCustomer().getFirstname());

		if (cw == null) {
			log.severe("skipping customer where cw=null:" + customerKey);
			return params;
		}
		
		CustomerExtendedInfo cei = cw.getCustomerExtendedInfo();		
		if (cei == null) {
			log.severe("skipping customer where cei=null:" + customerKey);
			return params;
		}
		
		if(cei.getConsentEmail() && StringUtils.isNotEmpty(cei.getEmail())) {
			try {
			mailerLiteService.addUpdateSubscriber(cw);
			} catch (Exception e) {
				log.log(Level.SEVERE, "", e);
			}
			log.severe("updating mailerlite for: " + cei.getEmail());
		}
		
		return params;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public void setMailerLiteService(CpapMailerLiteService mailerLiteService) {
		this.mailerLiteService = mailerLiteService;
	}
	
}
