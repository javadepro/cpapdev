package com.esofa.gae.queue;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.util.MailUtils;
import com.esofa.spring.controller.GaeEnhancedController;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/**
 * QueueJobManager starts all queue jobs as defined by the jobList map.
 */
@Controller
@RequestMapping(value = "/qj")
public class QueueJobManager extends GaeEnhancedController {
	private static final String QUEUE_NAME = "data-migration-queue"; // reuse an old queue...
	private static final String QUEUE_URL = "/qj/execute";
	
	private static final Logger log = Logger.getLogger(QueueJobManager.class.getName());
		
	private Map<String, Map<String, QueueableTask>> jobList;
	
	private MailUtils mailUtils;
	private String alertToAddress;
	
	/**
	 * Start all the jobs as defined in jobList.
	 * 
	 * @return dummy ModelAndView
	 */
	@RequestMapping(value="/start")
	public ModelAndView startJobs(@RequestParam(value="setName", required=true) String setName, HttpServletRequest request ) {
		log.info("Starting all queue jobs.");
	
		ModelAndView mav = new ModelAndView();
		
		Map<String, QueueableTask> jobSet = jobList.get(setName);
		if( jobSet != null ) {
			for(String jobName : jobSet.keySet()) {
				
				Map<String, String> jobParams = populateJobParams(request);
				queueJob(setName, jobName,jobParams);
			}
			mav.addObject("message","Started all queue jobs in set " + setName);
		} else {
			String msg = "Cannot find set " + setName;
			mav.addObject("message", msg);
			log.log(Level.WARNING, msg);
		}
		
		mav.setViewName("message");
		return mav;
	}

	/**
	 * Execute the task. If the execution returns with a non-null value, it will requeue the task.
	 * All the key-value pair in the returned map will be available as request parameters for the
	 * next invocation of the task.
	 *  
	 * @param setName of the set of QueueableTask to be executed
	 * @param jobName of the QueueableTask to be executed
	 * @param request Request containing the rest of the parameters needed by the QueueableTask
	 * @return dummy ModelAndView
	 */
	@RequestMapping(value="/execute")
	public ModelAndView executeJob(@RequestParam(value="setName", required=true) String setName,
								   @RequestParam(value="jobName", required=true) String jobName,
								   HttpServletRequest request) {
		log.info("Start execute job " + jobName);
		
		QueueableTask task;
		ModelAndView mav = new ModelAndView();
		
		Map<String, QueueableTask> jobSet = jobList.get(setName);
		// quite unlikely...
		if( jobSet == null ) {
			String msg = "Cannot find set " + setName;
			log.log(Level.WARNING, msg);
			mav.addObject("message", msg);
			mav.setViewName("message");
			
			return mav;
		}
		task = jobSet.get(jobName);
		
		Map<String, String> jobParams = populateJobParams(request);
				
		Map<String,String> next = null;
		
		try {
			next = task.executeTask(jobParams);		
		} catch(Exception e) {
			String msg = "Execution failure - " + setName + ":" + jobName;
			log.log(Level.SEVERE, msg, e);
			
			failJobNotification(setName, jobName, jobParams, e);


			mav.addObject("message", msg);
			mav.setViewName("message");
			
			return mav;			
		}
		
		// requeue if necessary
		String msg;
		
		if( next != null ) {
			// not the first time queueing this job so the info should be ok
			queueJob(setName, jobName, next);
			msg = "Requeued job " + setName + ":" + jobName;
		} else {
			msg = "Job " + setName + ":" + jobName + " completed.";
		}
		log.info(msg);
		mav.addObject("message", msg);
		mav.setViewName("message");
		
		return mav;
	}
			
	private void queueJob(String setName, String jobName) {
		queueJob(setName, jobName, null);
	}
	
	private void queueJob(String setName, 
						  String jobName, 
						  Map<String,String> additionalParams) {	
		TaskOptions task = withUrl(QUEUE_URL);
		task.method(Method.POST);
		
		task.param("setName", setName);
		task.param("jobName", jobName);
		
		if( additionalParams != null ) {
			for( String keyObj : additionalParams.keySet() ) { 
				
				//dont add setName and jobName again.
				if (StringUtils.equalsIgnoreCase(keyObj, "setName")
						|| StringUtils.equalsIgnoreCase(keyObj, "jobName") ) {
					continue;
				}
				
				task.param(keyObj, additionalParams.get(keyObj));
			}
		}
		QueueFactory.getQueue(QUEUE_NAME).add(task);
		log.info("Queued " + setName + ":" + jobName);
	}
	
	
	private Map<String, String> populateJobParams(HttpServletRequest request ) {
		Map<String, String> jobParams = new HashMap<String, String>(10);
		for( Object keyObj : request.getParameterMap().keySet() ) {
			String key = (String)keyObj;
			jobParams.put(key, request.getParameter(key));
		}
		
		return jobParams;
	}
	
	private void failJobNotification(String setName, String jobName, Map<String,String> params, Throwable cause) {

		StringBuffer msgBody = new StringBuffer(500);
		msgBody.append("Queue job ").append(setName).append(":").append(jobName).append(" has failed with the following details:").append(System.lineSeparator());
		msgBody.append("JobParameters:").append(System.lineSeparator());
		for( String key: params.keySet() ) {
			msgBody.append(key).append(" - ").append(params.get(key)).append(System.lineSeparator());
		}
		msgBody.append("Exception:").append(System.lineSeparator());
		msgBody.append(cause.toString()).append(System.lineSeparator());		
		msgBody.append(ExceptionUtils.getStackTrace(cause));
				    
		String subject = "Queue job failure - " + setName + ":" + jobName;
		
		mailUtils.sendMail(alertToAddress, subject, msgBody.toString());
	}
	
	public void setJobList(Map<String, Map<String, QueueableTask>> jobList) {
		this.jobList = jobList;
	}

	public void setAlertToAddress(String alertToAddress) {
		this.alertToAddress = alertToAddress;
	}	
	
	public void setMailUtils(MailUtils mailUtils) {
		this.mailUtils = mailUtils;
	}
}
