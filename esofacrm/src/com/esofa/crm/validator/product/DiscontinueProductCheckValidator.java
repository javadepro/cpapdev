package com.esofa.crm.validator.product;

import java.util.Map;

import javax.validation.ConstraintValidatorContext;

import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.validator.ApplicationContextAwareConstraintValidator;

public class DiscontinueProductCheckValidator extends ApplicationContextAwareConstraintValidator<DiscontinueProductCheck,Product> {

	private ProductService productService;
	
	public void initialize(DiscontinueProductCheck constraintAnnotation) {

		productService = (ProductService)applicationContext.getBean("productService");	
	}

	public boolean isValid(Product product, ConstraintValidatorContext context) {
		
		boolean isValid=true;
		
		
		//must have zero inventory before you can disable a product
		if (!product.getIsActive()) {

			
			Map<Shop,Inventory> inventoryMap = productService.getInventoryMapByProductId(product.getId());
			
			for (Inventory inventory : inventoryMap.values() ) {
				
				if (inventory.getQty() != 0) {
					isValid = false;
					break;
				}
			}
			
		}
		 
		return isValid;
	}

}
