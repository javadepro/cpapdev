package com.esofa.crm.controller.invoicevalidator.product;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Setting;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.refdata.service.RefdataService.SettingTypes;


//commenting out now.
public class PosMgrRequiredValidator extends AbstractInvoiceProductValidator
		implements InvoiceProductValidator {

	private RefdataService refDataService;
	
	@Override
	protected boolean doValidate(InvoiceForm invoiceForm, Product p,
			BindingResult result) {

//		List<Setting> settings = refDataService.getAllSettingsByType(//some setting type);
//		
//		for (Setting s: settings) {
//			
//			String barcode = p.getProductBarCode();
//			String settingBarcode = s.getName();
//					
//			if (StringUtils.equalsIgnoreCase(barcode, settingBarcode)) {
//			
//				invoiceForm.setReqMgrPasscode(true);
//				break;
//			}
//			
//		}
//		
		return true;
	}
	
	public void setRefDataService(RefdataService refDataService) {
		this.refDataService = refDataService;
	}

}
