package com.esofa.crm.validator.product;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Product;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.validator.ApplicationContextAwareConstraintValidator;

public class ProductBarCodeCheckValidator extends ApplicationContextAwareConstraintValidator<ProductBarCodeCheck,Product> {

	private ProductService productService;
	
	public void initialize(ProductBarCodeCheck constraintAnnotation) {

		productService = (ProductService)applicationContext.getBean("productService");	
	}

	public boolean isValid(Product product, ConstraintValidatorContext context) {
		
		String errorMsg = null;
		
		//if the barcode is null, then generate barcode must be selected
		if (StringUtils.isEmpty(product.getProductBarCode()) && product.getGenerateBarCode() == false) {
			
			errorMsg = "{product.barcode.blank}";
			return false;
		}
		
		//brand new product, check that the barcode is unique.
		if (product.getId() == null && productService.doesBarCodeExist(product.getProductBarCode())) {
			
			errorMsg = "{product.barcode.notUnqiue}";
		}
		
		//existing product
		if (product.getId() != null) {
			
			String existingBarCodeForProduct = productService.getProductById(product.getId()).getProductBarCode();
			
			//if it is not the same as existing bar code, and it is already exists, that means another product is already
			//using this updated bar code.
			if (!StringUtils.equalsIgnoreCase(product.getProductBarCode(),existingBarCodeForProduct )
					&& productService.doesBarCodeExist(product.getProductBarCode())) {
				
				errorMsg = "{product.barcode.exists}";
			}			
		}
		
		
		 if(errorMsg != null) {
			 context.disableDefaultConstraintViolation();
			 context.buildConstraintViolationWithTemplate( errorMsg  ).addConstraintViolation();
	        }
		
		 //if error message is null, then must be valid.
		return errorMsg == null;
	}

}
