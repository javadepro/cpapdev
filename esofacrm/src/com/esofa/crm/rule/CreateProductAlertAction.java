package com.esofa.crm.rule;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.ProductAlert;
import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.AlertType;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.ProductAlertService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;

public class CreateProductAlertAction<S extends Serializable> implements Action<Product, S>{

	private static final Logger log = Logger
			.getLogger(CreateProductAlertAction.class.getName());

	@Autowired
	ProductAlertService productAlertService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	RefdataService refdataService;


	public void execute(WorkPackage<Product, S> workPackage) {
		
		ProductAlert alert = new ProductAlert(); 
		ExpressionParser parser = new SpelExpressionParser();
		
		String message = (String)parser.parseExpression(workPackage.getResources(),new TemplateParserContext()).getValue(workPackage);
		
		try{
			
			Date date = EsofaUtils.getDateAfterNDays(workPackage.getDateOffset());
			alert.setAlertDate(date);
		}catch(Exception ex){
			alert.setAlertDate(new Date());
			log.log(Level.WARNING, "had to default to current date", ex);
		}

		alert.setMessage(message);
		alert.setProduct(workPackage.getTarget()); 
		
		Product p = productService.getProductByKey(workPackage.getTarget());
		alert.setManufacturer(p.getManufacturer() );
		
		/** Get Key **/
		String[] ids = workPackage.getAttribute().split("/");
		Key<AlertType> alertType = Key.create(AlertType.class, Long.parseLong(ids[0]));
		Key<AlertSubType> alertSubType = Key.create(alertType, AlertSubType.class, Long.parseLong(ids[1]));
		alert.setAlertSubType(alertSubType);
		
		productAlertService.saveAlert(alert);
		log.info("added new alert for Product");
		
	}
	


	public void setProductAlertService(ProductAlertService productAlertService) {
		this.productAlertService = productAlertService;
	}
	
	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	
}
