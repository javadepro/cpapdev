package com.esofa.crm.rule;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.esofa.crm.annotation.customer.CustomerTabNameE;
import com.esofa.crm.controller.util.SalesItem;
import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.ProductService;
import com.googlecode.objectify.Key;

public class UpdateMedicalInfoAction<S extends Serializable> implements Action<Customer, S>{

	private static final Logger log = Logger
			.getLogger(CreateCustomerAlertAction.class.getName());

	@Autowired
	CustomerService customerService;
	
	@Autowired
	RefdataService refdataService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ConfigService configService;
	

	public void execute(WorkPackage<Customer, S> workPackage) {

		CustomerMedicalInfo customerMedicalInfo = customerService.getCustomerMedicalInfoByKey(workPackage.getTarget());
		
		//check if it is mask/machine
		Key<Product> productKey = ((SalesItem)workPackage.getAfter()).getProduct();
		Product product = productService.getProductByKey(productKey);
		Long productType = product.getProductSubType().getParent().getId();
		int productTypeInt = productType.intValue();
		log.info("productType to process: "+productTypeInt);
		if(productTypeInt==configService.getConfigIntByName("PRODUCT.MASK.TYPE.ID")){
			customerMedicalInfo.setCurrentMask(productKey);
			customerMedicalInfo.setMaskPurchaseDate(new Date());
			log.info("updating Mask info");
		}else if(productTypeInt==configService.getConfigIntByName("PRODUCT.MACHINE.TYPE.ID")){
			customerMedicalInfo.setCurrentCpapMachine(productKey);
			customerMedicalInfo.setCpapPurchaseDate(new Date());
			log.info("updating CPAP Machine info");
		}else{
			return;
		}
		//we are dealing with mask fields, so we pretend as if we are saving from the cpap tab.
		try {
		customerService.saveCustomerMedicalInfo(customerMedicalInfo, CustomerTabNameE.CPAP);
		} catch (IllegalArgumentException e) {
			
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {

			throw new RuntimeException(e);
		}

	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	} 
	
	

}
