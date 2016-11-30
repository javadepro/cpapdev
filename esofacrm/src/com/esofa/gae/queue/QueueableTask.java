package com.esofa.gae.queue;

import java.util.Map;

/**
 * QueueableTask Tasks to be called by QueueJobManager. 
 */
public interface QueueableTask {	
	/**
	 * Actual processing of the task.
	 * If the job is broken down into smaller chunks, returning a non-null value allows
	 * the job to requeue itself, with the parameters returned to be used in the next 
	 * execution.
	 * Make sure there is a termination condition.
	 * 
	 * @param params
	 * @return a map of parameters to be used for next execution, or null if the job is completed
	 */
	public Map<String,String> executeTask(Map<String, String> params);		
}
