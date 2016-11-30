package com.esofa.crm.rule;

import java.io.Serializable;
import java.util.logging.Logger;

import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerWrapper;
import com.esofa.crm.service.CpapMailerLiteService;

public class AddSubscriberAction<S extends Serializable> implements
		Action<Customer, S> {

	private static final Logger log = Logger
			.getLogger(AddSubscriberAction.class.getName());

	CpapMailerLiteService mailerLiteService;

	public void execute(WorkPackage<Customer, S> workPackage) {

		CustomerWrapper cw = (CustomerWrapper) workPackage.getAfter();
		log.severe((new Boolean((cw == null))).toString());
		mailerLiteService.addUpdateSubscriber(cw);

	}

	public void setMailerLiteService(CpapMailerLiteService mailerLiteService) {
		this.mailerLiteService = mailerLiteService;
	}

}
