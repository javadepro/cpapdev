package com.esofa.crm.rule;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerAlert;
import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.AlertType;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.CustomerAlertService;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;

public class CreateCustomerAlertAction<S extends Serializable> implements Action<Customer, S>{

	private static final Logger log = Logger
			.getLogger(CreateCustomerAlertAction.class.getName());

	@Autowired
	CustomerService customerService;
	
	@Autowired
	RefdataService refdataService;

	@Autowired
	CustomerAlertService customerAlertService;
	
	public void execute(WorkPackage<Customer, S> workPackage) {
		
		CustomerAlert customerAlert = new CustomerAlert(); 
		ExpressionParser parser = new SpelExpressionParser();
		
		String message = (String)parser.parseExpression(workPackage.getResources(),new TemplateParserContext()).getValue(workPackage);
		
		try{
			
			Date date = EsofaUtils.getDateAfterNDays(workPackage.getDateOffset());
			customerAlert.setAlertDate(date);
		}catch(Exception ex){
			customerAlert.setAlertDate(new Date());
			log.log(Level.WARNING, "had to default to current date", ex);
		}

		customerAlert.setMessage(message);
		customerAlert.setCustomer(workPackage.getTarget());
		customerAlert.setClinician(customerService.getCustomerByKey(workPackage.getTarget()).getClinician());
		
		/** Get Key **/
		String[] ids = workPackage.getAttribute().split("/");
		Key<AlertType> alertType = Key.create(AlertType.class, Long.parseLong(ids[0]));
		Key<AlertSubType> alertSubType = Key.create(alertType, AlertSubType.class, Long.parseLong(ids[1]));
		customerAlert.setAlertSubType(alertSubType);
		
		customerAlertService.saveCustomerAlert(customerAlert);
		log.info("added new alert for Customer");
		
	}
	

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}


	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

	public void setCustomerAlertService(
			CustomerAlertService customerAlertService) {
		this.customerAlertService = customerAlertService;
	}
	
	
}
