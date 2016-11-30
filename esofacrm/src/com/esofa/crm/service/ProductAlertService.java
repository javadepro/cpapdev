package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.esofa.crm.model.Product;
import com.esofa.crm.model.ProductAlert;
import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.Manufacturer;
import com.googlecode.objectify.Key;

@Service
public class ProductAlertService {
	private static final Logger log = Logger.getLogger(ProductAlertService.class
			.getName());
	
	public List<ProductAlert> getAlertNextNDay(Date d){
		List<ProductAlert> keys =  ofy().load().type(ProductAlert.class).filter("alertDate <=", d).order("-alertDate").list();
		return keys;
	}	

	public void deleteAlertByKey(Long id, Long productId){
		Key<ProductAlert> key = Key.create(Key.create(Product.class, productId), ProductAlert.class, id);	
		ofy().delete().key(key).now();
	}
	
	public List<ProductAlert> getAlertbyManufacturer(Date date, Key<Manufacturer> manufacturer){
		return ofy().load().type(ProductAlert.class).filter("alertDate <=", date).filter("manufacturer", manufacturer).order("-alertDate").list();
	}
	
	public List<ProductAlert> getAlertByAlertSubType(Date date, Key<AlertSubType> alertSubType){
		return ofy().load().type(ProductAlert.class).filter("alertDate <=", date).filter("alertSubType", alertSubType).order("-alertDate").list();
	}
	
	public List<ProductAlert> getAlert(Date date, Key<Manufacturer> manufacturer, Key<AlertSubType> alertSubType){
		return ofy().load().type(ProductAlert.class).filter("alertDate <=", date).filter("manufacturer", manufacturer).filter("alertSubType", alertSubType).order("-alertDate").list();
	}
	
	public void saveAlert(ProductAlert alert) {
		ofy().save().entity(alert).notify();
	}
	
	public void deleteAlert(ProductAlert alert){
		ofy().delete().entity(alert).now();
	}
}
