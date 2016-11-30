package com.esofa.gae.queue.test;

import java.util.Map;
import java.util.logging.Logger;

import com.esofa.gae.queue.QueueableTask;

public class TestJobTask1 implements QueueableTask {
	private static Logger log = Logger.getLogger(TestJobTask1.class.getName());

	private String param1;
	private String param2;
	
	@Override
	public Map<String,String> executeTask(Map<String, String> params) {
		log.info("Running task " + TestJobTask1.class.getName());
		log.info("  expecting in params: param1=" + param1);
		log.info("  expecting in params: param2=" + param2);
		return null;
	}
	
	public void setParam1(String param) {
		this.param1 = param;
	}
	public void setParam2(String param) {
		this.param2 = param;
	}	
}

