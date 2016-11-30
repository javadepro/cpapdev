package com.esofa.crm.validator.product.inventory;

import java.util.Map;

import javax.validation.ConstraintValidatorContext;

import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.InventoryTransfer;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.validator.ApplicationContextAwareConstraintValidator;
import com.googlecode.objectify.Key;

/**
 * ensures that the from location has sufficient stock.
 * @author JHa
 *
 */
public class SufficientOriginationStockCheckValidator extends ApplicationContextAwareConstraintValidator<SufficientOriginationStockCheck,InventoryTransfer> {

	private ProductService productService;
	private RefdataService refdataService;
	
	public void initialize(SufficientOriginationStockCheck constraintAnnotation) {

		productService = (ProductService)applicationContext.getBean("productService");
		refdataService = (RefdataService)applicationContext.getBean("refdataService");
	}

	public boolean isValid(InventoryTransfer inventoryTransfer, ConstraintValidatorContext context) {

		
		//skip validation if from shop is null.  aka this means it is a stock item request.
		if (inventoryTransfer.getFromShop() == null) {
			return true;
		}
		
		String errorMsg = null;
		Key<Product> productKey = inventoryTransfer.getProduct();
		
		Map<Shop,Inventory> inventoryMap = productService.getInventoryMapByProductKey(productKey);
		Shop shop =  refdataService.getShop(inventoryTransfer.getFromShop());
		Inventory fromInventory = inventoryMap.get(shop);
		
		if (fromInventory.getQty() < inventoryTransfer.getQty()) {
			return false;
		}
		
		return true;
	}

}
