package com.esofa.crm.service;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;

import com.esofa.crm.model.CustomerWrapper;
import com.esofa.crm.util.MailUtils;
import com.esofa.mailerlite.BasicSubscriber;
import com.esofa.mailerlite.MailerLiteService;

/**
 * cpapdirect's mapping into mailerlite
 * @author JHa
 *
 */
public class CpapMailerLiteService implements InitializingBean{

	private static final Logger log = Logger
			.getLogger(CpapMailerLiteService.class.getName());
	
	private MailerLiteService mailerLiteService;
	private ConfigService configService;	
	private MailUtils mailUtils;
	

	public void afterPropertiesSet() throws Exception {

		String apiKey = configService.getConfigStringByName("MAILERLITE.API.KEY");
		String listId = configService.getConfigStringByName("MAILERLITE.LIST.ID");
		String subUrl = configService.getConfigStringByName("MAILERLITE.SUB.URL");
		String unsubUrl= configService.getConfigStringByName("MAILERLITE.UNSUB.URL");
		
		mailerLiteService.setApiKey(apiKey);
		mailerLiteService.setListId(listId);
		mailerLiteService.setSubscribeUrl(subUrl);
		mailerLiteService.setUnsubscribeUrl(unsubUrl);
	}
	
	
	public boolean addUpdateSubscriber(CustomerWrapper cw) {

		log.severe("addUpdateSubscriber");
		if (cw != null && cw.getCustomer() != null) {
			log.warning("customer: " + cw.getCustomer().getName());
		}
		
		if (cw!=null && cw.getCustomerExtendedInfo() != null) {
			log.warning("email: " + cw.getCustomerExtendedInfo().getEmail());
			log.warning("consent: " + cw.getCustomerExtendedInfo().getConsentEmail());
		}
		
		//upper level should to validation...but here is the last catch
		if (cw == null || cw.getCustomerExtendedInfo() == null || cw.getCustomer() == null
				|| StringUtils.isEmpty(cw.getCustomerExtendedInfo().getEmail()) || !cw.getCustomerExtendedInfo().getConsentEmail()) {
			return false;
		}
		
		log.severe("pass all checks for " + cw.getCustomerExtendedInfo().getEmail());

		
		BasicSubscriber sub = new BasicSubscriber();
		sub.setFirstName(cw.getCustomer().getName());
		sub.setEmail(cw.getCustomerExtendedInfo().getEmail());
		
		boolean success =  false;
		
		try {
			success = mailerLiteService.addUpdateSubscriber(sub);
		} catch (Exception e) {

			log.severe(ExceptionUtils.getStackTrace(e));
			success=false;
			mailUtils.sendMail("error with adding subscriber", "unable to add:"  + cw.getCustomerExtendedInfo().getEmail());
		}
		
		return success;
	}

	public boolean removeSubscriber(CustomerWrapper cw) {
		
		//upper level should to validation...but here is the last catch
		if (cw == null || cw.getCustomerExtendedInfo() == null 
				|| StringUtils.isEmpty(cw.getCustomerExtendedInfo().getEmail())) {
			return false;
		}
		
		String email = cw.getCustomerExtendedInfo().getEmail();
		return mailerLiteService.removeSubscriber(email);
	}
	
	public void setMailerLiteService(MailerLiteService mailerLiteService) {
		this.mailerLiteService = mailerLiteService;
	}
	
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
}
