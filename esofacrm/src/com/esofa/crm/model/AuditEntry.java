package com.esofa.crm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class AuditEntry implements Serializable {

	private static final long serialVersionUID = 119794226480444983L;

	@Id
	private Long id;
	
	@Index
	private Date entryDate;
	
	@Index
	private Key<CrmUser> user;
	
	@Index
	private AuditEntryTypeE entryType;
	private String auditDetails;
	private String userMessage;
	
	private int qty;
	private String inventoryCost;
	
	public AuditEntry() {}
	
	
	public AuditEntry(Key<CrmUser> user, AuditEntryTypeE entryType,
			String auditDetails, String userMessage) {
		super();
		this.entryDate = new Date();
		this.user = user;
		this.entryType = entryType;
		this.auditDetails = auditDetails;
		this.userMessage = userMessage;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public Key<CrmUser> getUser() {
		return user;
	}
	public void setUser(Key<CrmUser> user) {
		this.user = user;
	}
	public AuditEntryTypeE getEntryType() {
		return entryType;
	}
	public void setEntryType(AuditEntryTypeE entryType) {
		this.entryType = entryType;
	}
	public String getAuditDetails() {
		return auditDetails;
	}
	public void setAuditDetails(String auditDetails) {
		this.auditDetails = auditDetails;
	}
	
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
	public String getUserMessage() {
		return userMessage;
	}
	
	public void setInventoryCost(String inventoryCost) {
		this.inventoryCost = inventoryCost;
	}
	
	public String getInventoryCost() {
		return inventoryCost;
	}
	
	public void setInventoryCost(BigDecimal inventoryCost){
				
		this.inventoryCost =inventoryCost.toString(); 
	}
	
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public int getQty() {
		return qty;
	}
	
}
