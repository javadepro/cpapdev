package com.esofa.crm.rule;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;

import com.esofa.crm.messenger.model.WorkPackage;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class RuleEngineUtils {

	public static final String DEFAULT_QUEUE = "message-queue";
	public static final String WORK_PACKAGE_PROCESSOR_URL = "/queue/process-work-package";
	
	public static byte[] toByte(Serializable obj){
		return Base64.encodeBase64(SerializationUtils.serialize(obj));
	}
	

	public static Object toObject(byte[] bytes){
		return SerializationUtils.deserialize(Base64.decodeBase64(bytes));
	}

	public static void pushWorkPackageIntoQueue(WorkPackage payload){
		
		pushWorkPackageIntoQueue(DEFAULT_QUEUE, WORK_PACKAGE_PROCESSOR_URL, payload);
	}
	
	public static void pushWorkPackageIntoQueue(String queueName, String processorUrl, WorkPackage payload){
		byte[] wpInByte = toByte(payload);
		// Push it into queue for further processing
		Queue queue = QueueFactory.getQueue(queueName);
		queue.add(withUrl(processorUrl).param(
				"workpackage", wpInByte).method(Method.POST));
	}
}
