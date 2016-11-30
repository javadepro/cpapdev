package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerEvent;
import com.esofa.crm.model.CustomerPaymentInfo;
import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.Invoice.InvoiceTypeE;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.model.pos.InvoicePayment;
import com.esofa.crm.refdata.model.EventSubType;
import com.esofa.crm.refdata.model.EventType;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.util.InvoicePaymentUtils;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.MathUtils;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

@Service
public class SalesService {
	private static final Logger log = Logger.getLogger(SalesService.class
			.getName());

	
	private InventoryService inventoryService;
	
	private InventoryAuditService inventoryAuditService;
	private RefdataService refdataService;

	private ConfigService configService;
	
	private CustomerEventService customerEventService;
	
	private CustomerService customerService;
	
	private InvoiceSeqNumService invoiceSeqNumService;
	
	public Invoice getInvoice(Long invoiceId) {		
		Key<Invoice> key = Key.create(Invoice.class,invoiceId);
		return getInvoice(key);
	}
	
	public Invoice getInvoice(String invoiceNumber) {
		return ofy().load().type(Invoice.class).filter("invoiceNumber", invoiceNumber).first().now();
	}
	
	public List<Invoice> getInvoices(List<String> invoiceNumbers) {
		return ofy().load().type(Invoice.class).filter("invoiceNumber in", invoiceNumbers).list();
	}
	
	public Invoice getInvoice(Key<Invoice> invoiceKey) {
		return ofy().load().key(invoiceKey).now();
	}
	
	public List<Invoice> getInvoices( Date from, Date to, Key<Shop> shopKey, boolean ascOrder) {		
		String orderString = "-";
		if (ascOrder) { orderString = ""; }
		
		Query<Invoice> q = ofy().load().type(Invoice.class);
		
		if (shopKey !=null) { 	q= q.filter("shopKey", shopKey); }
		
		q= q.filter("invoiceDate >=", from).filter("invoiceDate <", to).order(orderString + "invoiceDate").order(orderString + "insertDateTime");
		return q.list();
	}
	
	public List<Invoice> getInvoices( Date from, Date to, Invoice.InvoiceAdpStatusTypeE adpStatus) {
		Query<Invoice> q = ofy().load().type(Invoice.class);
		
		if (adpStatus != null) { q.filter("adpStatus = ",adpStatus.toString()); }
		q = q.filter("invoiceType",InvoiceTypeE.STANDARD.toString())
				.filter("invoiceDate >=", from).filter("invoiceDate <", to)
				.order("-invoiceDate").order("-insertDateTime");
		return q.list();
	}
	
		
	public String  getInvoiceForArAging(String startCursor, String queryKey, String queryValue, List<Invoice> resultsHolder, int pageSize) {
		
		Query<Invoice> q = ofy().load().type(Invoice.class).filter(queryKey +" =", queryValue).limit(pageSize);
		
		if (!StringUtils.equalsIgnoreCase(queryKey, "status") ) {
			q.filter("status in", InvoiceUtils.notVoidInvoiceStatus());
		}
		
		if (StringUtils.isNotEmpty(startCursor)) {
			q.startAt(Cursor.fromWebSafeString(startCursor));
		}
		
		QueryResultIterator<Invoice> qri = q.iterator();
		
		for (int i =0; i < pageSize; i++) {
			if (qri.hasNext()) {
				Invoice inv =qri.next();
				resultsHolder.add(inv);
			} else {
				break;
			}
		}
		
		Cursor c = qri.getCursor();
		
		if (c ==null) { return StringUtils.EMPTY; }
		return c.toWebSafeString();
	}
	
	public void updateInvoice(Invoice invoice) {
		ofy().save().entity(invoice).now();
	}

	//direct save, no business logic up front!!!!
	public void saveInvoice(Invoice invoice) {
		ofy().save().entity(invoice).now();		
	}
	
	//initial save for an invoice.
	public void saveInvoice (Key<CrmUser> crmUser,Invoice invoice,  List<InvoiceItem> invoiceItems, Key<Shop> shopKey) {
			
		boolean isSleepMed=InvoiceUtils.isSleepMed(invoice);
		
		invoice.setInvoiceNumber(generateInvoiceNumber(shopKey,isSleepMed));
		
		if (MathUtils.ZERO.equals(invoice.getAdpBalanceRemainingAsBD())) {
			invoice.setAdpStatus(Invoice.InvoiceAdpStatusTypeE.RECEIVED.toString());
		}
		
		if(StringUtils.isNotEmpty(invoice.getRawUserName())) {
			
			invoice.setUserFirstName(invoice.getUserFirstName());
			invoice.setUserLastName(invoice.getUserLastName());
			invoice.setUserName(StringUtils.EMPTY);
			
		}
		
		invoice.setInsertDateTime(new Date());
		Key<Invoice> invoiceKey = ofy().save().entity(invoice).now();		
		saveToActivityHistory(crmUser,invoice);
		
		//associate the invoiceItems to the saved invoice key.
		//also adjust the inventory, inventory cost and add audit entry
		for (InvoiceItem i: invoiceItems) {

		
			Key<Product> productKey = i.getProductKey();
			i.setInvoiceKey(invoiceKey);
			
			try {	
				//if not bypass, then set inventory cost
				if (!i.getBypassInvAdj()) {
					
					BigDecimal cost = inventoryService.getCostAndIncrementInventorySold(productKey, i.getQty());
					i.setInventoryCostAsBD(cost);
				}
				
				saveInvoiceItem(i);
				
				//if bypass flag is set, then skip
				if (i.getBypassInvAdj()) { continue; }						
				
				Inventory inventory =inventoryService.adjustAndSaveInventory(productKey, shopKey, -i.getQty());
				inventoryAuditService.generateSaleEntry(crmUser, productKey, inventory);
				
			} catch (Exception e) {
				log.severe("error saving item: " + productKey + ". invoiceKey: " + invoiceKey);
			}
		}
		
		//if it is full payment, then auto generate the save payment record.		
		
		//move the money from trialInvoice back to sale bucket
		if (StringUtils.isNotEmpty(invoice.getReferencedInvoiceNumber())) {
			
			Invoice referencedInvoice = getInvoice(invoice.getReferencedInvoiceNumber());
			
			//if this is a standard and prev is a trial, then this is a trial2purchase
			if (InvoiceUtils.isInvoiceType(referencedInvoice, InvoiceTypeE.TRIAL)
					&& InvoiceUtils.isInvoiceType(invoice, InvoiceTypeE.STANDARD)) {
				
				//take credit amount and subtract from deposit bucket.  add to sales bucket
				InvoicePayment reverseDeposit = InvoicePaymentUtils.createForTrialDepositRefund(referencedInvoice,invoice);
				
				//move those funds into Payment
				BigDecimal moveBucketAmt = referencedInvoice.getAdjInvoiceClientPortionAsBD();
				InvoicePayment moveBucketPayment = InvoicePaymentUtils.createForTrial2Purchase(referencedInvoice,invoice, moveBucketAmt, true);
				moveBucketPayment.setBalanceAsBD(invoice.getAdjInvoiceClientPortionAsBD());
				
				//no need to save delta because if full payment, it is already saved from above.
				//if not full, then no need to do this anyways
				//no need to save 
				saveInvoicePayment(reverseDeposit,moveBucketPayment);
				
				//if this is a trial refund and prev is a trial, then it is trial refund.
			} else if (InvoiceUtils.isInvoiceType(referencedInvoice, InvoiceTypeE.TRIAL)
					&& InvoiceUtils.isInvoiceType(invoice, InvoiceTypeE.TRIAL_REFUND)) {
				
				//remove total from deposit.
				InvoicePayment reverseDeposit = InvoicePaymentUtils.createForTrialDepositRefund(referencedInvoice,invoice);
				
				//move those funds to OTHER.
				BigDecimal moveBucketAmt = referencedInvoice.getAdjInvoiceClientPortionAsBD();
				InvoicePayment moveToOtherIncome = InvoicePaymentUtils.createForOtherIncome(referencedInvoice, invoice, moveBucketAmt,true);
				
				//final amount in OTHER
				BigDecimal finalAmt = invoice.getAdjInvoiceClientPortionAsBD();
				InvoicePayment finalDelta = InvoicePaymentUtils.createForOtherIncome(referencedInvoice,invoice,finalAmt,false);
				
				saveInvoicePayment(reverseDeposit,moveToOtherIncome,finalDelta);				
			}
		}
		
		//do not do for trial refund as special logic applies.
		if (StringUtils.equalsIgnoreCase(Invoice.InvoiceStatusTypeE.FULL_PAYMENT.toString(), invoice.getStatus())
				&& !InvoiceUtils.isInvoiceType(invoice, InvoiceTypeE.TRIAL_REFUND))
				{
			
			InvoicePayment invoicePayment = InvoicePaymentUtils.createFromInvoice(invoice, invoiceKey );
			String invoiceDescription = InvoicePayment.DESC_FULL_PAYMENT;
			
			if (InvoiceUtils.isInvoiceType(invoice, Invoice.InvoiceTypeE.TRIAL)) {
				
				invoiceDescription = InvoicePayment.DESC_DEPOSIT;
			} else if (InvoiceUtils.isInvoiceType(invoice, Invoice.InvoiceTypeE.OTHER)) {
				invoiceDescription = InvoicePayment.DESC_OTHERINCOME;
			}
			
			invoicePayment.setDescription(invoiceDescription);		
			saveInvoicePayment(invoice,invoicePayment);						 
		}		
	}
	
	public void saveInvoiceItem(List<InvoiceItem> items) {
		ofy().save().entity(items).now();
	}
	
	public void saveInvoiceItem(InvoiceItem invoiceItem) {
		ofy().save().entity(invoiceItem).now();
	}	
	
	public List<InvoiceItem> getInvoiceItems(Key<Invoice> ... invoiceKey ) {
		return ofy().load().type(InvoiceItem.class).filter("invoiceKey in",invoiceKey).order("invoiceKey").order("order").list();
	}
	private void saveToActivityHistory(Key<CrmUser> crmUser,Invoice invoice) {
		
		CustomerEvent customerEvent = new CustomerEvent();
		
		if (invoice.getCustomerKey() == null) { return;}
		
		customerEvent.setCustomer(invoice.getCustomerKey());
		customerEvent.setCreater(invoice.getUserName());
		customerEvent.setDate(invoice.getInsertDateTime());
		
		Key<EventType> eventType = Key.create(EventType.class, new Long(600008));
		Key<EventSubType> eventSubType = Key.create(eventType, EventSubType.class, new Long(610040));
		
		customerEvent.setEventSubType(eventSubType);

		String details = String.format("Invoice <a href=\"/pos/invoice-view?invoiceNumber=%s\" >%s</a> was generated.",
				invoice.getInvoiceNumber(),invoice.getInvoiceNumber());
		
		customerEvent.setDetails(details);
		customerEventService.saveCustomerEvent(customerEvent);
	}
	
	public void adjForVoidedInvoice (Key<CrmUser> crmUser,Invoice invoice) {
		
		//for each invoice detail
		//adjust the inventory
		//add audit inventory
		Key<Shop> shopKey = invoice.getShopKey();

		//backward compat
		if(shopKey == null) {
			shopKey= refdataService.getShopKey(invoice.getShopName());
		}
		
		List<InvoiceItem> items = getInvoiceItems(InvoiceUtils.getInvoiceKey(invoice.getId()));
		for (InvoiceItem item : items) {
		
			if (!item.getBypassInvAdj()) {
				
				inventoryService.decrementInventorySold(shopKey ,item.getProductKey(), item.getQty());
				inventoryAuditService.generateVoidSaleEntry(crmUser,shopKey,item.getProductKey(),invoice.getInvoiceNumber());
			}
		}
	}
	
	public String generateInvoiceNumber(Key<Shop> shopKey, boolean isSleepMed) {
		
		Shop s =refdataService.getShop(shopKey);
		int seq = invoiceSeqNumService.getAndIncrementInvoiceSeqNum(shopKey);		
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%06d", seq)).append(s.getShortName());

		return sb.toString();
	}
		
	public List<InvoicePayment> getInvoicePayments(Key<Invoice> invoiceKey) {
		List<InvoicePayment> results = ofy().load().type(InvoicePayment.class).filter("invoiceKey",invoiceKey)
				.filter("description in",Arrays.asList(InvoicePayment.includeInDisplay))
				.order("insertDateTime").list();
		return results;
	}
	
	public List<InvoicePayment> getInvoiceAdpPayment(Key<Invoice> invoiceKey) {
		List<InvoicePayment> results = ofy().load().type(InvoicePayment.class).filter("invoiceKey",invoiceKey)
				.filter("description in",Arrays.asList(InvoicePayment.adpInDisplay))
				.order("insertDateTime").list();
		
		return results;
	}
	
	public List<InvoicePayment> getInvoicePayments(List<String> descriptionList, Date fromDate, Date endDate) {
		Query<InvoicePayment> query = ofy().load().type(InvoicePayment.class);		
		
		if (fromDate != null) { query =query.filter("paymentDate >=",fromDate); }
		if (endDate != null) { query =query.filter("paymentDate <=",endDate); }
		if (descriptionList != null) { query = query.filter("description in",descriptionList); }	
		
		return query.list();	
	}
	
	public InvoicePayment getInvoicePayment(Key<InvoicePayment> invoicePaymentKey) {
		return ofy().load().key(invoicePaymentKey).now();
	}
	
	public void saveInvoicePayment (Invoice invoice,InvoicePayment invoicePayment) {
		boolean isNormalPayment = InvoicePayment.isDescriptionInList(invoicePayment.getDescription(), InvoicePayment.includeInDisplay);
		
		saveInvoicePayment(invoicePayment);
		adjustInvoice(invoice,invoicePayment,isNormalPayment);
		updateInvoice(invoice);
		
		//adjust customer payment info.
		if (isNormalPayment) {
			adjustCustomerPaymentInfo(invoice,invoicePayment);
		}
	}
	
	public void saveInvoicePayment(InvoicePayment ... invoicePayment) {
		ofy().save().entities(invoicePayment).now();
	}
	
	private void adjustInvoice( Invoice invoice, InvoicePayment invoicePayment, boolean isNormalPayment) {
		
		if (isNormalPayment) {
			//check if payment status needs to be updated
			//update remaining balance
			invoice.setBalanceRemaining(invoicePayment.getBalance());
			
			if (invoice.getBalanceRemainingAsBD().equals(BigDecimal.ZERO.setScale(Invoice.SCALE))) {
				invoice.setStatus(Invoice.InvoiceStatusTypeE.FULL_PAYMENT.toString());
			}
			
		} else {
			
			invoice.setAdpBalanceRemaining(invoicePayment.getBalance());
			if (invoice.getAdpBalanceRemainingAsBD().equals(BigDecimal.ZERO.setScale(Invoice.SCALE))) {
				invoice.setAdpStatus(Invoice.InvoiceAdpStatusTypeE.RECEIVED.toString());
			} else {
				invoice.setAdpStatus(Invoice.InvoiceAdpStatusTypeE.PENDING.toString());
			}
			
		}
	}
	
	private void adjustCustomerPaymentInfo( Invoice invoice, InvoicePayment invoicePayment) {

		Key<Customer> customerKey = invoice.getCustomerKey();		
		if (customerKey == null) { return; }
		
		//if full payment, no need to adjust
		if (StringUtils.equalsIgnoreCase(invoice.getStatus(), Invoice.InvoiceStatusTypeE.FULL_PAYMENT.toString())) {
			return;
		}
		
		CustomerPaymentInfo cpi = customerService.getCustomerPaymentInfoByKey(customerKey);
		
		//create it.
		if (cpi == null) {
			
			cpi = new CustomerPaymentInfo();
			cpi.setCustomer(customerKey);
		}
		
		String bal_str = cpi.getBalance();
		
		if (StringUtils.isEmpty(bal_str)) {
			bal_str = "0.00";
		}
		
		BigDecimal bal_bd = new BigDecimal(bal_str).setScale(Invoice.SCALE);
		
		//save invoice case
		if (invoicePayment == null) {
										
			bal_str = bal_bd.add(invoice.getBalanceRemainingAsBD()).toString();				
			
		} else {
		
		//partial payment case
			bal_str = bal_bd.subtract(invoicePayment.getAmtAsBD()).setScale(Invoice.SCALE).toString();
		}
		
		cpi.setBalance(bal_str);
		customerService.saveCustomerPaymentInfo(cpi);		
	}

	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	public void setInventoryAuditService(InventoryAuditService inventoryAuditService) {
		this.inventoryAuditService = inventoryAuditService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	public void setCustomerEventService(CustomerEventService customerEventService) {
		this.customerEventService = customerEventService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void setInvoiceSeqNumService(
			InvoiceSeqNumService invoiceSeqNumService) {
		this.invoiceSeqNumService = invoiceSeqNumService;
	}
}
