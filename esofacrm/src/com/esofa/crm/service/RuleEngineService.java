package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.esofa.crm.rule.Rule;
import com.googlecode.objectify.Key;

@Service
public class RuleEngineService {
	@Autowired
	private EhCacheCacheManager cacheManager;
	
	/** Rulesssss **/
	@Cacheable(value = "ruleAll")
	public Map<Key<Rule>, Rule> getRuleMap() {
		List<Key<Rule>> keys = ofy().load().type(Rule.class).keys().list();
		return ofy().load().keys(keys);
	}

	@Cacheable(value="rule", key="#id")
	public Rule getRuleById(Long id) {		
		return ofy().load().type(Rule.class).id(id).now();
	}

	@CacheEvict(value = "rule", key="#rule.id")
	public void saveRule(Rule rule) {
		cacheManager.getCache("ruleAll").clear();
		ofy().save().entity(rule).now();	
	}
	
	@CacheEvict(value = "rule", key="#id")
	public void deleteRule(Long id) {
		ofy().delete().type(Rule.class).id(id).now();
		cacheManager.getCache("ruleAll").clear();
	}	
}
