package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.esofa.crm.model.Product;
import com.esofa.crm.model.trial.TrialItem;
import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

@Service
public class TrialItemService {
	private static final Logger log = Logger.getLogger(TrialItemService.class
			.getName());

	public Map<Key<TrialItem>, TrialItem> getAll() {
		List<Key<TrialItem>> keys = ofy().load().type(TrialItem.class).keys().list();
		return ofy().load().keys(keys);
	}
	
	public TrialItem getTrialItem(Long trialItemId) {		
		Key<TrialItem> key = Key.create(TrialItem.class,trialItemId);
		return getTrialItem(key);
	}

	public TrialItem getTrialItem(Key<TrialItem> trialItemKey) {
		return ofy().load().key(trialItemKey).now();
	}
	
	public List<TrialItem>  getTrialItems(Key<Shop> shopKey, String trialStatus) {
		Query<TrialItem> q = ofy().load().type(TrialItem.class);
		
		if (shopKey != null) { q= q.filter("location", shopKey);}
		if (StringUtils.isNotEmpty(trialStatus)) { q=q.filter("trialStatus",trialStatus); }
		
		return q.list();
	}

	public List<TrialItem> getTrialItem(Key<Product> productKey, String serial) {
		Query<TrialItem> q = ofy().load().type(TrialItem.class);
		
		q= q.filter("product", productKey);
		q=q.filter("serialNumber",serial); 
		
		return q.list();
	}
	
	public TrialItem saveTrialItem(TrialItem trialItem) {
		trialItem.setLastUpdatedDate(new Date());
		ofy().save().entity(trialItem).now();

		return trialItem;
	}
	
	public void deleteTrialItem(Key<TrialItem> trialItemKey) {
		ofy().delete().key(trialItemKey).now();
	}
}
