package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.begin;
import static com.esofa.crm.service.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.esofa.crm.model.CompanyTypeE;
import com.esofa.crm.model.Config;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.FundingOption;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.util.Closeable;

@Service
public class ConfigService {	
	private static final Logger log = Logger.getLogger(ConfigService.class
			.getName());

	
	/** Config **/
	@Cacheable(value = "adminConfigAll")
	public Map<Key<Config>, Config> getConfigMap() {
		
		Map<Key<Config>, Config> result =null;
		
		try {
			List<Key<Config>> keys = ofy().load().type(Config.class).keys().list();
			result=  ofy().load().keys(keys);
			
			//for when we are not going through objectify filter
		} catch (java.lang.IllegalStateException ise) {
			
			 Closeable closeable = begin();
			 
			 List<Key<Config>> keys = ofy().load().type(Config.class).keys().list();
				result=  ofy().load().keys(keys);
			 closeable.close();
		}

		return result;
	}

	// @Cacheable(value="refdataConfig", key="#id")
	public Config getConfigById(Long id) {
		return ofy().load().type(Config.class).id(id).now();
	}
	
	public Config getConfigByName(String name) {		
		for (Config c : getConfigMap().values()) {
			
			if (StringUtils.equalsIgnoreCase(c.getName(), name)) {
				
				return c;
			}			
		}
		return null;
	}
	
	public int getConfigIntByName(String name){
		Config config = getConfigByName(name);
		return Integer.parseInt(config.getValue());
	}
	
	public int getConfigIntById(Long id){
		Config config = getConfigById(id);
		return Integer.parseInt(config.getValue());
	}
	
	public String getConfigStringById(Long id){
		Config config = getConfigById(id);
		return config.getValue();
	}
	
	public String getConfigStringByName(String name){
		Config config = getConfigByName(name);
		
		if (config !=null) {
			return config.getValue();
		}
		return null;
	}
	
	public Long getConfigLongByName(String name){
		Config config = getConfigByName(name);
		return Long.parseLong(config.getValue());
	}
	
	public Key<Product> getConfigProductKey(String name) {		
		Config config = getConfigByName(name);
		
		if (config == null || config.getValue() == null) {
			return null;
		}
		
		Long productId = Long.parseLong(config.getValue());
		return Key.create(Product.class,productId);
	}

	@CacheEvict(value = "adminConfigAll", allEntries = true)
	public void saveConfig(Config config) {
		ofy().save().entity(config).now();
	}
	
	public Key<FundingOption> getSpouseOptionKey() {
		return Key.create(FundingOption.class,
				 getConfigIntByName("INSURANCE.SPOUSE.OPTION.KEY"));
	}

	public Key<FundingOption> getSelfOptionKey() {
		return Key.create(FundingOption.class,
				 getConfigIntByName("INSURANCE.SELF.OPTION.KEY"));
	}

	public Long getMaskTypeId() {
		return  getConfigLongByName("PRODUCT.MASK.TYPE.ID");
	}

	public Long getMachineTypeId() {
		return  getConfigLongByName("PRODUCT.MACHINE.TYPE.ID");
	}
	
	public String getBarCodePrefix() {
		return getConfigStringByName("PRODUCT.BARCODE.PREFIX");
	}
	
	@Cacheable(value="invoiceCache")
	public double getHst(String companyName) {
		
		String keyName = new StringBuilder("TAX.HST.").append(companyName).toString();
		
		String value_str = getConfigStringByName(keyName);
		
		return Double.valueOf(value_str);
	}	
	
	@CacheEvict(value = "adminConfigAll", allEntries = true)
	public int incrementBarCodeSequence() {
		// Objectify transaction needs to be idempotent
		// incrementing a sequence isn't idempotent in nature, but guess it is ok
		// to have skipped sequence number...
		// pay attention to run time behaviour if odd things happen
		// if this causes a problem, use transactNew and set retry to 1
	    Integer barCodeSeq = ofy().transact(new Work<Integer>() {		
			@Override
			public Integer run() {
				int barCodeSeq = 0;
				Config c = ofy().load().type(Config.class).filter("name", "PRODUCT.BARCODE.SEQ").first().now();
				
				barCodeSeq =Integer.valueOf(c.getValue());
				barCodeSeq++;
				c.setValue(String.valueOf(barCodeSeq));

				ofy().save().entity(c).now();
				
				return barCodeSeq;
			}
		});
	
		return barCodeSeq;
	}

	
	@Cacheable(value="companyModeCache")
	public String getCompanyMode() {
		
		String companyMode = CompanyTypeE.CPAPDIRECT.toString();
		
		Config c = getConfigByName("COMPANY");
		
		if (c!= null) {
			
			companyMode  = c.getValue();
		}
		
		return companyMode;
	}
	
}
