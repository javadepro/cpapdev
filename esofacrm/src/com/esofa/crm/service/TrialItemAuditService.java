package com.esofa.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.esofa.crm.model.AuditEntryTypeE;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.trial.TrialItem;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;

@Service
public class TrialItemAuditService extends AuditService {


	private RefdataService refdataService;
	private ProductService productService;
	
	public void generateAddEntry(Key<CrmUser> crmUser, TrialItem ti) {
		
		
		String productName = getProductName(ti);
		String location = getShopName(ti);
		AuditEntryTypeE entryType = AuditEntryTypeE.TRIAL_ADD;
		String auditNote = String.format("Adding new  %s with serial %s, to trial pool at %s",productName, ti.getSerialNumber(),location);
		
		save(crmUser, entryType, auditNote, StringUtils.EMPTY);
	}
	
	public void generateDeleteEntry(Key<CrmUser> crmUser, TrialItem ti) {
		
		String productName = getProductName(ti);
		AuditEntryTypeE entryType = AuditEntryTypeE.TRIAL_DELETE;
		String auditNote = String.format("Removed %s with serial %s, from trial pool",productName, ti.getSerialNumber());
		
		save(crmUser, entryType, auditNote, StringUtils.EMPTY);
	}
	
	public void generateUpdateEntry(Key<CrmUser> crmUser, TrialItem ti) {
		
		String productName = getProductName(ti);
		String location = getShopName(ti);
		String status = ti.getTrialStatus();
		String customerName = ti.getCustomerFullName();
		AuditEntryTypeE entryType = AuditEntryTypeE.TRIAL_UPDATE;
		String auditNote = String.format("Updated %s with serial %s at %s. status: %s. customer: %s."
				,productName, ti.getSerialNumber(),location,status, customerName);
		
		save(crmUser, entryType, auditNote, StringUtils.EMPTY);
	}
	
	private String getProductName(TrialItem ti) {
		
		Product p = productService.getProductByKey(ti.getProduct());
		String productName = StringUtils.EMPTY;
		
		if (p != null) {
			productName = p.getName();
		}
		return productName;
	}
	
	private String getShopName(TrialItem ti) {
		
		Shop s = refdataService.getShop(ti.getLocation());
		String shopName = StringUtils.EMPTY;
		
		if (s !=null) {
			shopName = s.getShortName();
		}
		return shopName;
	}
	
	
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
