package com.esofa.crm.model.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.esofa.crm.common.model.Address;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerInsuranceInfo;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.model.FundingOption;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.model.SleepClinic;
import com.esofa.crm.refdata.model.SleepDoctor;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.service.SalesService;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.PhoneUtil;
import com.googlecode.objectify.Key;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;

@Component
public class PdfMapper_4793_67E_201404 extends PdfMapper<Object> {

	private CustomerService customerService;
	private ProductService productService;
	private SalesService salesService;
	private RefdataService refdataService;
	
	private boolean invoiceMode;
		
	private enum DeviceType {CPAP, BPAP, APAP, UNKNOWN};
	
	public PdfMapper_4793_67E_201404() {
		formPath = "WEB-INF/misc/4793-67E-201404.pdf";
	}
	
	
	@Override
	//itemId is invoiceNumber
	public void mapFields(AcroFields fields, Object itemId) throws IOException, DocumentException { 
		
		
		Invoice invoice = null;
		Shop s = null;
		Key<Customer> customerKey =null;
		
		if (invoiceMode) {
			String str_itemId = (String) itemId;
			invoice =salesService.getInvoice(str_itemId);
			s = refdataService.getShop(invoice.getShopKey()); 
			customerKey = invoice.getCustomerKey();
		} else {
		
			Long long_itemId = (Long) itemId;
			customerKey =Key.create(Customer.class,long_itemId.longValue());
		}		
		
		Customer customer = customerService.getCustomerByKey(customerKey);		
		CustomerExtendedInfo custExtInfo = customerService.getCustomerExtendedByKey(customerKey);		
		CustomerInsuranceInfo custInsuranceInfo = customerService.getCustomerInsuranceInfoByKey(customerKey);
		CustomerMedicalInfo custMedInfo = customerService.getCustomerMedicalInfoByKey(customerKey);	
		
		if (!invoiceMode && customer.getPreferredLocation() != null) {
			
			s = refdataService.getShop(customer.getPreferredLocation());
		}
		
		FundingOption fundingOption = null;
		if( custInsuranceInfo != null ) {
			fundingOption = refdataService.getFundingOptionMap().get(custInsuranceInfo.getFundingOptionGovernment());
		}
				
		SleepDoctor sleepDoctor = null;
		SleepClinic sleepClinic = null;
		
		if( custMedInfo != null ) {
			sleepDoctor = refdataService.getSleepDoctorMap().get(custMedInfo.getSleepDoctor());
			sleepClinic = refdataService.getSleepClinicMap().get(custMedInfo.getClinic());
		}
		
		// don't tell me invoice and customer can be null... we have a bigger problem if so...
		CrmUser clinician = refdataService.getClinicianMap().get(customer.getClinician());
		
		
		DeviceType deviceType = null;
		Product currProduct = null;
		InvoiceItem machineSold = null;
		if (invoiceMode) {
			Key<Invoice> invoiceKey = InvoiceUtils.getInvoiceKey(invoice.getId());
			List<InvoiceItem> invoiceItems = salesService.getInvoiceItems(invoiceKey);
			
			// use first item in invoice that is of Product type "Machine - Main Unit" and there is always at least 1 such entry
			// note: currently there is no BPAP subtype		

			ProductSubType type = null;
			for( InvoiceItem invoiceItem : invoiceItems ) {
				Product invoiceItemProduct = productService.getProductByKey(invoiceItem.getProductKey());
				ProductSubType invoiceItemSubType = refdataService.getProductSubTypeByKey(invoiceItemProduct.getProductSubType());
				ProductType invoiceItemType = refdataService.getProductTypeByKey(invoiceItemSubType.getParentType());
				if( isMachine(invoiceItemType.getType()) ) {
					machineSold = invoiceItem;
					currProduct = invoiceItemProduct;
					type = invoiceItemSubType;
					break;
				}
			}

			if (type != null) { deviceType = getDeviceType(type.getType()); }
		}
		
		
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//		String signDate = sdf.format(new Date());
		
		
		// Field mappings - listed in order as appeared in pdf form
		
		// Section 1 - Applicant's Biographical Information
		fields.setField("lastName", customer.getLastname());
		fields.setField("firstName", customer.getFirstname());
		// middleInitial - NA
		if( customer.getHealthCardNumber() != null ) {
			fields.setField("HealthNumber", customer.getHealthCardNumber().replaceAll(" ", ""));
		}
		fields.setField("Version", customer.getHealthCardVersion());
		//fields.setField("dateOfBirth", sdf.format(custExtInfo.getDateOfBirth()));
		if( custExtInfo != null ) {			
			if( custExtInfo.getGender().equalsIgnoreCase("male") ) {
				fields.setField("gender", "m");	
			} else {
				fields.setField("gender", "f");
			}
			
		}
		
		// name of long-term care home - NA
		// address
		if( custExtInfo != null && custExtInfo.getAddress() != null ) {
			fields.setField("streetName", custExtInfo.getAddress().getLine1());
			fields.setField("city", custExtInfo.getAddress().getCity());
			fields.setField("Residence postal code", custExtInfo.getAddress().getPostalCode());
		} 
		
		ContactPreference pref =null;
		if (custExtInfo != null && custExtInfo.getContactPreference() != null) {
			Key<ContactPreference> prefKey = custExtInfo.getContactPreference();			
			pref =refdataService.getContactPreferenceById(prefKey.getId());
		}
		
		String homeTelephone = PhoneUtil.getCustomerPhone(customer, pref);
		if (StringUtils.isNotEmpty(homeTelephone)) {
			fields.setField("homeTelephone", homeTelephone.replaceAll("-", " "));
		}
		if( customer.getPhoneOffice() != null ) {
			fields.setField("businessTelephone", customer.getPhoneOffice().replaceAll("-", " "));			
		}
		fields.setField("Ext", customer.getPhoneOfficeExt());
		
		// Confirmation of Benefits
		if( fundingOption != null ) {
			String option = fundingOption.getOption();
			if( option != null ) {
				if( option.equalsIgnoreCase("owp") ) {
					fields.setField("soass", "y");
					fields.setField("owp", "y");
				} else if( option.equalsIgnoreCase("odsp") ) {
					fields.setField("soass", "y");
					fields.setField("owp", "odsp");
				} else if( option.equalsIgnoreCase("acsd") ) {
					fields.setField("soass", "y");
					fields.setField("owp", "acsd");			
				} else if( option.equalsIgnoreCase("regular funding") ) {
					fields.setField("soass", "n");
				} else {
					// ignore other funding options
				}				
			}
		}
		
		fields.setField("wsib", "n");
		fields.setField("vac", "n");
		fields.setField("ltcResident", "n");
		fields.setField("chronic", "n");
		
		// Section 2 - Devices and Eligibility
		if (invoiceMode) {
			if( deviceType == DeviceType.CPAP ) {
				fields.setField("appDeviceReq.0", "1");
			} else if( deviceType == DeviceType.APAP ) {
				fields.setField("appDeviceReq.2", "1");
			}
		}
		
		fields.setField("fullName2", customer.getLastname() + ", " + customer.getFirstname());
		if( customer.getHealthCardNumber() != null ) {
			fields.setField("HealthNumber2", customer.getHealthCardNumber().replaceAll(" ",""));
		}
		fields.setField("Version2", customer.getHealthCardVersion());				
		
		// Section 2a - Positive Airway Pressure System
		if (invoiceMode) {
			if( deviceType == DeviceType.CPAP ) {
				fields.setField("deviceSys", "1");
			} else if( deviceType == DeviceType.APAP ) {
				fields.setField("deviceSys", "2");
			}
		}
		// Reason for Application - NA
		// Replacement due to - NA
		
		// Confirmation of Applicant's Eligibility for a Positive Airway Pressure System
		fields.setField("hearingAidConfirm", "1");
		fields.setField("FmsConfirm", "1");
		
		if (invoiceMode) {
			if( deviceType == DeviceType.APAP ) {
				fields.setField("apapDevice", "1");
				fields.setField("apapDevice0", "1");
			} else if (deviceType == DeviceType.BPAP) {
	
				 fields.setField("bbapDevice", "1");
				 fields.setField("bbapDevice2", "1");
				 fields.setField("bbapDevice3", "1");
			}
		}
				
		// Section 2b - Compressors
		// NA
		
		// Section 2c - Suction Devices
		// NA
		
		// Section 2d - Apnea/Cardiorespiratory Monitors
		// NA
		
		// Section 2e - Airway Clearance Devices
		// NA
		
		// Section 2f - Tracheostomy Equipment
		// NA
		
		// Section 3 - Client Consent and Signature
		// Applicant/Agent - NA		
		//fields.setField("dateOfSignature", signDate);
		
		// Section 4 - Signatures
		// Physician
		fields.setField("1", "s"); // Physician
		if( sleepDoctor != null ) {
			fields.setField("phyLastName", sleepDoctor.getLastName());
			fields.setField("phyFirstName", sleepDoctor.getFirstName());
			fields.setField("OHIBNO", StringUtils.leftPad(sleepDoctor.getHibNumber(), 6, "0"));
		}
		if( sleepClinic != null ) {
			if( sleepClinic.getPhone() != null ) {
				fields.setField("phyBussTelephone", sleepClinic.getPhone().replace("-"," "));
			}
			fields.setField("phyExt", sleepClinic.getPhoneExt());
		}		 
		//fields.setField("dateOfSignature2", signDate);
		
		// Clinic
		if( sleepClinic != null ) {
			fields.setField("clinicName", sleepClinic.getName());
			fields.setField("adpClinicNO", sleepClinic.getAdpNumber());
			if( sleepClinic.getPhone() != null ) {
				fields.setField("clinicBussTelephone", sleepClinic.getPhone().replace("-", " "));
			}
			fields.setField("clinicExt", sleepClinic.getPhoneExt());
		}
		
		// Vendor
		fields.setField("vendorBusName", "CPAP Direct Ltd.");
		if( clinician != null ) {
			fields.setField("vendorLastName", clinician.getLastname());
			fields.setField("vendorFirstName", clinician.getFirstname());			
		}
		
		if (s != null) {
			
			Address vendorAddr = refdataService.getAddressForPrimaryAdpShop(s.getAdpVendorNumber());
			
			if (vendorAddr == null) {
				vendorAddr = s.getAddress();
			}
			fields.setField("adpvenAuRegNo2", s.getAdpVendorNumber());
			fields.setField("vendorLocation", vendorAddr.toString());
			fields.setField("vendorBussTelephone", s.getPhone().replace("-"," "));
		}
		
		//fields.setField("dateOfSignature3", signDate);
		if (invoiceMode) {
			fields.setField("invoiceNo", invoice.getInvoiceNumber());
			// Equipment... x6
			if (currProduct != null) {
				fields.setField("adpDevCode.0.0", currProduct.getAdpCatalogNumber());
				fields.setField("makeDescription.0.0", currProduct.getName());
			}
			
			if (machineSold != null) {
				fields.setField("serialNo.0.0", machineSold.getProductSerialNumber());
			}
			
			fields.setField("adpPortion.0.0", invoice.getAdpPortion());
		}
		//commenting out due to the fact this includes other products
//		BigDecimal totalClientPortion = invoice.getClientPortionAsBD();
//		if( invoice.getCreditItemTotalAsBD() != null ) {
//			totalClientPortion = totalClientPortion.add(invoice.getCreditItemTotalAsBD().abs());
//		}
//		fields.setField("clientPortion.0.0", totalClientPortion.toPlainString());

		// Proof of Delivery
		//fields.setField("dateOfSignature4", signDate);
		
		// Pages Submitted
		fields.setField("pageAttached.0", "1");
	}


	private boolean isMachine(String productTypeName) {
		return ("Machine - Main Unit".equalsIgnoreCase(productTypeName) || // 500001
				"Package".equalsIgnoreCase(productTypeName));			   // 500000
	}

	private DeviceType getDeviceType(String typeName) {
		if( typeName == null ) {
			return DeviceType.UNKNOWN;
		} else if( typeName.equalsIgnoreCase("CPAP")) {
			return DeviceType.CPAP;			

		} else if( typeName.equalsIgnoreCase("VPAP") || typeName.equalsIgnoreCase("VPAP Auto")) {
			return DeviceType.BPAP;
		} else if( typeName.equalsIgnoreCase("APAP")) {
			return DeviceType.APAP;
		} else {
			return DeviceType.UNKNOWN;
		}		
	}
	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
		
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
	
	public void setInvoiceMode(boolean invoiceMode) {
		this.invoiceMode = invoiceMode;
	}
	
}