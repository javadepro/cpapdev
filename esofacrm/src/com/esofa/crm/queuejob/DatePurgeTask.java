package com.esofa.crm.queuejob;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.esofa.crm.service.ConfigService;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;

public class DatePurgeTask extends BasePurgeTask {	
	protected ConfigService configService;

	protected String configNameForDaysToKeep;
	protected String olderThanFieldCondition="snapshotDate <=";
		 	
	@Override
	protected List<? extends Key<?>> getKeysToDelete() {

		int dayLimit =configService.getConfigIntByName(configNameForDaysToKeep);		
		Date olderThan = EsofaUtils.getDateBeforeNDays(dayLimit);
		
		List<? extends Key<?>> keys=null;

		try {
			 keys = ofy().load().type(Class.forName(clazz)).filter(olderThanFieldCondition, olderThan).keys().list();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}	
		return keys;
	}
	
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	
	public void setConfigNameForDaysToKeep(String configNameForDaysToKeep) {
		this.configNameForDaysToKeep = configNameForDaysToKeep;
	}

	public void setOlderThanFieldCondition(String olderThanFieldCondition) {
		this.olderThanFieldCondition = olderThanFieldCondition;
	}

}
