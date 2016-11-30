package com.esofa.crm.queuejob;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.esofa.gae.queue.QueueableTask;
import com.googlecode.objectify.Key;

public abstract class  BasePurgeTask implements QueueableTask {
	private static final Logger log = Logger.getLogger(BasePurgeTask.class.getName());

	protected String clazz;
	
	/**
	 * provide a list of keys to delete based on whatever criteria u wish.
	 * @param ofy
	 * @return
	 */
	abstract protected List<? extends Key<?>> getKeysToDelete();
	
	@Override
	public Map<String, String> executeTask(Map<String, String> params) {		
		log.info("delete old " + clazz + " entries start");
		
		List<? extends Key<?>> keysToDelete = getKeysToDelete();
		int deletedRecords=0;
		try {
			deletedRecords = delete(keysToDelete);
		} catch (ClassNotFoundException e) {
			
			throw new RuntimeException(e);
		}
		
		log.info("delete old " + clazz + "done : deleted " + deletedRecords);		
		return null;
	}	
	
	protected int delete(List<? extends Key<?>> keys) throws ClassNotFoundException {				
		int deletedRecords = keys.size();
		ofy().delete().keys(keys).now();
		return deletedRecords;
	}
	
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}	
}
