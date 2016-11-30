package com.esofa.gae.datamigration;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import com.esofa.gae.datamigration.DataMigrationProcessor.Action;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.ObjectifyService;

/**
 * DataMigrationTask is responsible for batching and queueing of the calls to process 
 * each entity type using the DataMigrationProcessor. 
 */
public class DataMigrationTask {
	private static final Logger log = Logger.getLogger(DataMigrationTask.class.getName());
	
	private static final String QUEUE_NAME = "data-migration-queue";
	private static final String TASK_URL = "/queue/mtask";
	
	// TODO order matters. probably have better way to structure the data but good enough for now. 
	private static final String[][] items = {		
		{"com.esofa.crm.model.Config","toString",Action.CHANGE.toString()},

		// Audit
		{"com.esofa.crm.model.AuditEntry","toString",Action.CHANGE.toString()},

		// Customer
		{"com.esofa.crm.model.Customer","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerAlert","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerEvent","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerExtendedInfo","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerHasSpecialMedicalNote","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerInsuranceInfo","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerInsuranceInfoType1","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerInsuranceInfoType2","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerMedicalInfo","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerPaymentInfo","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.CustomerCpapTrialInfo","toString",Action.CHANGE.toString()},

		// Product
		{"com.esofa.crm.model.Inventory","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.Product","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.ProductAlert","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.InventoryTransfer","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.InventoryCostEntry","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.InventoryCostQueue","toString",Action.CHANGE.toString()},

		{"com.esofa.crm.refdata.model.AlertSubType","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.AlertType","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.Clinician","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.AppointmentPreference","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.ContactPreference","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.DiscountReason","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.CpapDiagnosis","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.EventSubType","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.EventType","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.FamilyDoctor","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.FundingOption","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.InsuranceProvider","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.Manufacturer","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.ProductSubType","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.ProductType","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.Setting","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.Shop","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.SleepClinic","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.SleepDoctor","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.Dentist","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.refdata.model.DentalClinic","toString",Action.CHANGE.toString()},	
		{"com.esofa.crm.refdata.model.PrimaryAdpInfo","toString",Action.CHANGE.toString()},	




		// Rule Engine
		{"com.esofa.crm.rule.Rule","toString",Action.CHANGE.toString()},

		// Security
		{"com.esofa.crm.security.user.model.CrmUser","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.security.user.model.GrantedAuthorityImpl","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.security.user.model.AllowedIP","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.security.user.model.CustomerProfileTempAccess","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.security.user.model.UserPasscode","toString",Action.CHANGE.toString()},

		// POS
		{"com.esofa.crm.model.pos.Invoice","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.pos.InvoiceItem","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.pos.InvoicePayment","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.pos.InvoiceSeqNum","toString",Action.CHANGE.toString()},

		// Trial
		{"com.esofa.crm.model.trial.TrialItem","toString",Action.CHANGE.toString()},

		// Reports
		{"com.esofa.crm.model.report.GeneratedReport","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.report.InventoryCostRE","toString",Action.CHANGE.toString()},
		{"com.esofa.crm.model.report.ArAgingRE","toString",Action.CHANGE.toString()},	
											};
	
	static {
		for( int i = 0; i < items.length; i++ ) {
			String className = items[i][0];
			try {
				Class<?> clazz = Class.forName(className);
				ObjectifyService.register(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public static void execute(String currentEntity, String currentBatch, String realRun) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
		if( currentEntity == null ) {
			currentEntity = items[0][0];
			log.info("Kick start migration task for " + currentEntity + ".");
			requeue(currentEntity, null, realRun);
			return;
		}
		
		log.info("Executing task for " + currentEntity + " @ " + currentBatch);

		Class<?> clazz = Class.forName(currentEntity);
		Method callback = clazz.getMethod(getMethodName(currentEntity));
		Action action = getAction(currentEntity);
		DataMigrationProcessor<?> processor = 
				new DataMigrationProcessor(clazz, callback, action); 
		
		// TODO if filters are needed/useful then add it later on
		// processor.addFilter(xxx,xxx);
		
		String nextBatch;
		if( realRun != null && realRun.equalsIgnoreCase("sure") ) {
			nextBatch = processor.run(currentBatch);
		} else {
			nextBatch = processor.mock(currentBatch);
		}

		if( nextBatch != null ) {
			requeue(currentEntity, nextBatch, realRun);
			return;
		} else {
			log.info("Task completed for " + currentEntity);
		}
		
		// if there are more entities to deal with start the next entity
		String nextEntity = getNextEntity(currentEntity);
		if( nextEntity != null ) {
			requeue(nextEntity, null, realRun);
			return;				
		}
	}
	
	private static String getMethodName(String entity) {
		for( int i = 0; i < items.length; i++ ) {
			if( items[i][0].equalsIgnoreCase(entity) ) {
				return items[i][1];
			}
		}
		return null;
	}
	
	private static Action getAction(String entity) {
		for( int i = 0; i < items.length; i++ ) {
			if( items[i][0].equalsIgnoreCase(entity) ) {
				return Action.valueOf(items[i][2]);
			}
		}
		return null;		
	}
	
	private static String getNextEntity(String entity) {
		for( int i = 0; i < items.length - 1; i++ ) {
			if( items[i][0].equalsIgnoreCase(entity) ) {
				return items[i+1][0];
			}			
		}
		return null;
	}
	
	private static void requeue(String entity, String cursor, String realRun) {
		log.info("Queuing task for " + entity + " @ " + cursor);
		TaskOptions task = withUrl(TASK_URL);
		if( entity != null ) task = task.param("entity", entity);
		if( cursor != null ) task = task.param("nextBatch", cursor);
		if( realRun != null ) task = task.param("realRun", realRun);
		QueueFactory.getQueue(QUEUE_NAME).add(task);
	}
}
