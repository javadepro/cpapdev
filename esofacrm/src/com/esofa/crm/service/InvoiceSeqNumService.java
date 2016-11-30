package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.esofa.crm.model.pos.InvoiceSeqNum;
import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;

@Service
public class InvoiceSeqNumService {
	private static final Logger log = Logger.getLogger(InvoiceSeqNumService.class
			.getName());

	public synchronized int getAndIncrementInvoiceSeqNum(Key<Shop> shopKey) {		
		InvoiceSeqNum isn = ofy().load().type(InvoiceSeqNum.class).filter("shopKey", shopKey).first().now();
		final Key<InvoiceSeqNum> isnKey = Key.create(InvoiceSeqNum.class, isn.getId());
		
		// Objectify transaction needs to be idempotent
		// incrementing a sequence isn't idempotent in nature, but guess it is ok
		// to have skipped sequence number...
		// pay attention to run time behaviour if odd things happen
		// if this causes a problem, use transactNew and set retry to 1
		Integer value = ofy().transact(new Work<Integer>() {
			@Override
			public Integer run() {
				int value = 0;
				InvoiceSeqNum isnTxn = ofy().load().key(isnKey).now();				
				value = isnTxn.getAndIncrementValue();
				ofy().save().entity(isnTxn).now();
				return value;
			}
		});
		
		return value;
	}
	
	public boolean exists(Key<Shop> shopKey) {
		try {
			InvoiceSeqNum isn = ofy().load().type(InvoiceSeqNum.class).filter("shopKey", shopKey).first().now();
			
			if (isn == null) {
				return false;
			}
		} catch (Exception e) {
			
			return false;
		}
		
		return true;
	}
	
	public void createNew(Key<Shop> shopKey) {		
		InvoiceSeqNum isn = new InvoiceSeqNum();
		isn.setShopKey(shopKey);
		ofy().save().entity(isn).now();
	}
}
