package com.esofa.crm.rule;

import java.io.Serializable;
import java.util.logging.Logger;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerEvent;
import com.esofa.crm.refdata.model.EventSubType;
import com.esofa.crm.refdata.model.EventType;
import com.esofa.crm.service.CustomerEventService;
import com.googlecode.objectify.Key;

@Component
public class CreateCustomerHistoryAction<S extends Serializable> implements Action<Customer, S>{
	private static final Logger log = Logger
			.getLogger(CreateCustomerHistoryAction.class.getName());

	private CustomerEventService customerEventService;
		
	public void setCustomerEventService(
			CustomerEventService customerEventService) {
		this.customerEventService = customerEventService;
	}

	public void execute(WorkPackage<Customer, S> workPackage) {
		CustomerEvent customerEvent = new CustomerEvent(); 
		ExpressionParser parser = new SpelExpressionParser();
		
		String details = (String)parser.parseExpression(workPackage.getResources(),new TemplateParserContext()).getValue(workPackage);
		
		customerEvent.setDetails(details);
		customerEvent.setCustomer(workPackage.getTarget());
		
		/** Get Key **/
		String[] ids = workPackage.getAttribute().split("/");
		Key<EventType> eventType = Key.create(EventType.class, Long.parseLong(ids[0]));
		Key<EventSubType> eventSubType = Key.create(eventType, EventSubType.class, Long.parseLong(ids[1]));
		customerEvent.setEventSubType(eventSubType);
		
		customerEventService.saveCustomerEvent(customerEvent);
		log.info("added new note for Customer");
	}	
}
