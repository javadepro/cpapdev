package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerEvent;
import com.esofa.crm.refdata.model.EventSubType;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

@Service
public class CustomerEventService{
	private static final Logger log = Logger.getLogger(CustomerEventService.class
			.getName());
	
	/** Event Stuff **/
	public CustomerEvent getCustomerEventByKey(Key<CustomerEvent> key) {
		return ofy().load().key(key).now();
	}

	public List<CustomerEvent> getCustomerEventByCustomerId(Long id) {
		Key<Customer> key = Key.create(Customer.class, id);
		return ofy().load().type(CustomerEvent.class).ancestor(key).order("-date").order("-__key__").list();
	}

	public List<CustomerEvent> getCustomerEventByCustomerKey(Key<Customer> key) {
		return ofy().load().type(CustomerEvent.class).ancestor(key).order("-date").order("-__key__").list();
	}

	public List<CustomerEvent> getCustomerEventByCustomerId(Long id, Key<EventSubType> eventSubType, int lastXDays){
		Key<Customer> key = Key.create(Customer.class, id);
		//get date
		Date date = EsofaUtils.getDateBeforeNDays(lastXDays);
		Query<CustomerEvent> q = ofy().load().type(CustomerEvent.class).ancestor(key).filter("date >=", date );
		if(eventSubType!=null){
			q = q.filter("eventSubType", eventSubType);
		}
		List<CustomerEvent> events = q.order("-date").order("-__key__").list();
		return events;
	}
	
	public void saveCustomerEvent(CustomerEvent customerEvent) {
		ofy().save().entity(customerEvent).now();
	}
	
	public void deleteCustomerEvent(CustomerEvent customerEvent){
		ofy().delete().entity(customerEvent).now();
	}
	
	public void deleteCustomerEventByKey(Key<CustomerEvent> key){
		ofy().delete().key(key).now();
	}
}
