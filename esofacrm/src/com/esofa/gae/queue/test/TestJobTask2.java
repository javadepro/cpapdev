package com.esofa.gae.queue.test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.esofa.gae.queue.QueueableTask;

public class TestJobTask2 implements QueueableTask {
	private static Logger log = Logger.getLogger(TestJobTask2.class.getName());

	private String param1;
	
	@Override
	public Map<String,String> executeTask(Map<String, String> params) {
		log.info("Running task " + TestJobTask2.class.getName());
		log.info("  expecting in params: param1=" + param1);
		log.info("  expecting in params: additionalParam=" + params.get("additionalParam"));
		log.info("  expecting in params: count=" + params.get("count"));
						
		Map<String, String> requeueParams = null;
		int count;
		count = (params.get("count") == null) ? 0 : Integer.valueOf(params.get("count")); 
		if( count < 3 ) {
			requeueParams = new HashMap<String, String>();
			requeueParams.put("count", Integer.toString(count+1));
			requeueParams.put("additionalParam", "a");
		}
						
		return requeueParams;
	}
	
	public void setParam1(String param) {
		this.param1 = param;
	}	
}

