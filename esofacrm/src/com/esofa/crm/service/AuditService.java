package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.esofa.crm.model.AuditEntry;
import com.esofa.crm.model.AuditEntryTypeE;
import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

@Service
public class AuditService {
	private static final Logger log = Logger.getLogger(AuditService.class.getName());

	/**
	 * save audit entry
	 * @param auditEntry
	 */
	public void save(AuditEntry auditEntry) {
		ofy().save().entity(auditEntry).now();
	}
	
	public void save(Key<CrmUser> crmUser, AuditEntryTypeE entryType, String auditNote, String userMessage) {	
		AuditEntry ae = new AuditEntry(crmUser, entryType, auditNote,userMessage);
		save(ae);
	}
		
	public int delete(Date olderThanDate) {
		List<Key<AuditEntry>> keys = ofy().load().type(AuditEntry.class).filter("entryDate <=", olderThanDate).keys().list();
		int deletedRecords = keys.size();		
		ofy().delete().keys(keys).now();		
		return deletedRecords;
	}
	
	public List<AuditEntry> getAll(AuditEntryTypeE entryType, Key<CrmUser> crmUser, Date entryDate) {
		Query<AuditEntry> query = ofy().load().type(AuditEntry.class);
		
		if (entryType != null) { query = query.filter("entryType",entryType); }		
		if (crmUser != null) { query = query.filter("user",crmUser); }		
		if (entryDate != null) { query =query.filter("entryDate >=",entryDate); }

		return query.order("-entryDate").list();	
	}	
	

	public List<AuditEntry> getAll(List<String> entryTypes, Date fromDate, Date toDate) {
		Query<AuditEntry> query = ofy().load().type(AuditEntry.class);
		
		if (fromDate != null) { query =query.filter("entryDate >=",fromDate); }
		if (toDate != null) { query =query.filter("entryDate <=",toDate); }
		if (entryTypes != null) { query = query.filter("entryType in",entryTypes); }		

		return query.order("entryDate").list();	
	}
}