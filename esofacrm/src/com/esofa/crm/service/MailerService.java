package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.esofa.crm.model.MailTemplate;
import com.googlecode.objectify.Key;

@Service
public class MailerService {
	private static final Logger log = Logger.getLogger(MailerService.class
			.getName());
	
	@Autowired
	private CacheManager cacheManager;
	
	public Map<Key<MailTemplate>, MailTemplate> getMailTemplateMap() {
		List<Key<MailTemplate>> keys = ofy().load().type(MailTemplate.class).keys().list();
		return ofy().load().keys(keys);
	}
	
	public MailTemplate getgetCustomerByKeyById(Long id) {
		Key<MailTemplate> mailTemplateKey = Key.create(MailTemplate.class, id);
		return getMailTemplateByKey(mailTemplateKey);
	}
	
	public MailTemplate getMailTemplateByKey(Key<MailTemplate> key){
		return ofy().load().key(key).now();
	}
	
	public void saveMailTemplate(MailTemplate mailTemplate) {
		ofy().save().entity(mailTemplate).now();
	}	
}
