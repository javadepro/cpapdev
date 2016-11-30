package com.esofa.crm.service;

import java.util.logging.Logger;

import com.esofa.crm.model.AuditEntry;
import com.esofa.crm.model.Config;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerAlert;
import com.esofa.crm.model.CustomerCpapTrialInfo;
import com.esofa.crm.model.CustomerEvent;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerHasSpecialMedicalNote;
import com.esofa.crm.model.CustomerInsuranceInfo;
import com.esofa.crm.model.CustomerInsuranceInfoType1;
import com.esofa.crm.model.CustomerInsuranceInfoType2;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.model.CustomerPaymentInfo;
import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.InventoryCostEntry;
import com.esofa.crm.model.InventoryCostQueue;
import com.esofa.crm.model.InventoryTransfer;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.ProductAlert;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.model.pos.InvoicePayment;
import com.esofa.crm.model.pos.InvoiceSeqNum;
import com.esofa.crm.model.report.ArAgingRE;
import com.esofa.crm.model.report.GeneratedReport;
import com.esofa.crm.model.report.InventoryCostRE;
import com.esofa.crm.model.trial.TrialItem;
import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.AlertType;
import com.esofa.crm.refdata.model.AppointmentPreference;
import com.esofa.crm.refdata.model.Clinician;
import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.model.CpapDiagnosis;
import com.esofa.crm.refdata.model.DentalClinic;
import com.esofa.crm.refdata.model.Dentist;
import com.esofa.crm.refdata.model.DiscountReason;
import com.esofa.crm.refdata.model.EventSubType;
import com.esofa.crm.refdata.model.EventType;
import com.esofa.crm.refdata.model.FamilyDoctor;
import com.esofa.crm.refdata.model.FundingOption;
import com.esofa.crm.refdata.model.InsuranceProvider;
import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.model.PrimaryAdpInfo;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.esofa.crm.refdata.model.Setting;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.model.SleepClinic;
import com.esofa.crm.refdata.model.SleepDoctor;
import com.esofa.crm.rule.Rule;
import com.esofa.crm.security.user.model.AllowedIP;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.CustomerProfileTempAccess;
import com.esofa.crm.security.user.model.GrantedAuthorityImpl;
import com.esofa.crm.security.user.model.UserPasscode;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

public class OfyService {
	private static final Logger log = Logger.getLogger(OfyService.class.getName());
	
	static {
			
		register();
	}

	public static void register() {
		// Config
				factory().register(Config.class);
				
				// Audit
				factory().register(AuditEntry.class);

				// Customer
				factory().register(Customer.class);
				factory().register(CustomerAlert.class);
				factory().register(CustomerEvent.class);
				factory().register(CustomerExtendedInfo.class);
				factory().register(CustomerHasSpecialMedicalNote.class);
				factory().register(CustomerInsuranceInfo.class);
				factory().register(CustomerInsuranceInfoType1.class);
				factory().register(CustomerInsuranceInfoType2.class);
				factory().register(CustomerMedicalInfo.class);
				factory().register(CustomerPaymentInfo.class);
				factory().register(CustomerCpapTrialInfo.class);

				// Product
				factory().register(Inventory.class);
				factory().register(Product.class);
				factory().register(ProductAlert.class);
				factory().register(InventoryTransfer.class);
				factory().register(InventoryCostEntry.class);
				factory().register(InventoryCostQueue.class);

				
				factory().register(AlertSubType.class);
				factory().register(AlertType.class);
				factory().register(Clinician.class);
				factory().register(AppointmentPreference.class);
				factory().register(ContactPreference.class);
				factory().register(DiscountReason.class);
				factory().register(CpapDiagnosis.class);
				factory().register(EventSubType.class);
				factory().register(EventType.class);
				factory().register(FamilyDoctor.class);
				factory().register(FundingOption.class);
				factory().register(InsuranceProvider.class);
				factory().register(Manufacturer.class);
				factory().register(ProductSubType.class);
				factory().register(ProductType.class);
				factory().register(Setting.class);
				factory().register(Shop.class);
				factory().register(SleepClinic.class);
				factory().register(SleepDoctor.class);
				factory().register(Dentist.class);
				factory().register(DentalClinic.class);	
				factory().register(PrimaryAdpInfo.class);	

				// Rule Engine
				factory().register(Rule.class);

				// Security
				factory().register(CrmUser.class);
				factory().register(GrantedAuthorityImpl.class);
				factory().register(AllowedIP.class);
				factory().register(CustomerProfileTempAccess.class);
				factory().register(UserPasscode.class);
				
				// POS
				factory().register(Invoice.class);
				factory().register(InvoiceItem.class);
				factory().register(InvoicePayment.class);
				factory().register(InvoiceSeqNum.class);

				// Trial
				factory().register(TrialItem.class);

				// Reports
				factory().register(GeneratedReport.class);
				factory().register(InventoryCostRE.class);
				factory().register(ArAgingRE.class);	
	}
	
	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}
	

	public static ObjectifyFactory factory() {
		ObjectifyFactory of = ObjectifyService.factory();
		// TODO uncomment this to use the new Embed format. Non-reversible.
		// of = changeSaveFormat(of);
		return of;
	}
	
	public static Closeable begin() {
		Closeable c = ObjectifyService.begin();
		register();
		return c;
	}
	
	// Prep for 4.1->5 migration
	@Deprecated
	private static ObjectifyFactory changeSaveFormat(ObjectifyFactory of) {
		log.info("Setting ObjectifyFactory to use new Embed Format");		
		//of.setSaveWithNewEmbedFormat(true);
		return of;
	}	
	
	
}
