package com.esofa.crm.model.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.esofa.crm.util.MathUtils;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


/**
 * account receivable aging report entry
 * @author JHa
 *
 */
@Entity
public class ArAgingRE implements Serializable, ReportEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4897368219380908532L;
	
	@Id
	private Long id;
	@Index
	private String reportName;
	@Index
	private Date snapshotDate;
	@Index
	private String reportId;
	private String invoiceNumber;

	private String location;
	private String preparedByFirstName;
	
	private Date invoiceDate;

	private Long customerId;
	
	@Deprecated
	private String customerName;
	private String customerFirstName;
	private String customerLastName;
	private String portionType;
	private String amt;
	private String remainingBalance;
	private String days30;
	private String days60;
	private String days90;
	private String days120;
	private String days180;
	private String daysOver180;
	
	public ArAgingRE() {
	}
	
	public ArAgingRE(String portionType) {
	
		this.portionType = portionType;
	}
	
	
	/* (non-Javadoc)
	 * @see com.esofa.crm.model.report.ReportEntry#getReportName()
	 */
	public String getReportName() {
		return reportName;
	}
	/* (non-Javadoc)
	 * @see com.esofa.crm.model.report.ReportEntry#setReportName(java.lang.String)
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	/* (non-Javadoc)
	 * @see com.esofa.crm.model.report.ReportEntry#getSnapshotDate()
	 */
	public Date getSnapshotDate() {
		return snapshotDate;
	}
	/* (non-Javadoc)
	 * @see com.esofa.crm.model.report.ReportEntry#setSnapshotDate(java.util.Date)
	 */
	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}
	/* (non-Javadoc)
	 * @see com.esofa.crm.model.report.ReportEntry#getReportId()
	 */
	public String getReportId() {
		return reportId;
	}
	/* (non-Javadoc)
	 * @see com.esofa.crm.model.report.ReportEntry#setReportId(java.lang.String)
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
public void setCustomerName(String customerName) {
	this.customerName = customerName;
}

public String getCustomerName() {
	return customerName;
}
public String getCustomerFirstName() {
	return customerFirstName;
}
public void setCustomerFirstName(String customerFirstName) {
	this.customerFirstName = customerFirstName;
}
public String getCustomerLastName() {
	return customerLastName;
}

public void setCustomerLastName(String customerLastName) {
	this.customerLastName = customerLastName;
}

	public String getPortionType() {
		return portionType;
	}
	public void setPortionType(String portionType) {
		this.portionType = portionType;
	}
	public String getAmt() {
		
		if (amt == null) { 
			amt = MathUtils.ZERO.toString(); 
		}
		return amt;
	}
	
	public void addAmt(BigDecimal amt) {
		if (this.amt == null) { 
			this.amt = MathUtils.ZERO.toString(); 
		}
		
		this.amt = MathUtils.setBDScale((new BigDecimal(this.amt)).add(amt)).toString();
	}
	
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getRemainingBalance() {
		
		if (this.remainingBalance == null) { 
			this.remainingBalance = MathUtils.ZERO.toString(); 
		}
		return remainingBalance;
	}
	
	public void addRemainingBalance(BigDecimal amt) {
		if (remainingBalance == null) { 
			remainingBalance = MathUtils.ZERO.toString(); 
		}
		
		remainingBalance= MathUtils.setBDScale((new BigDecimal(remainingBalance)).add(amt)).toString();
	}
	
	
	public void setRemainingBalance(String remainingBalance) {
		this.remainingBalance = remainingBalance;
	}
	public String getDays30() {
		if (days30 == null) { 
			days30 = MathUtils.ZERO.toString(); 
		}
		return days30;
	}
	
	public void addDays30(BigDecimal amt) {
		if (days30 == null) { 
			days30 = MathUtils.ZERO.toString(); 
		}
		
		days30 = MathUtils.setBDScale((new BigDecimal(days30)).add(amt)).toString();
	}
	
	
	public void setDays30(String days30) {
		this.days30 = days30;
	}
	public String getDays60() {
		
		if (days60 == null) { 
			days60 = MathUtils.ZERO.toString(); 
		}
		return days60;
	}
	
	public void addDays60(BigDecimal amt) {
		if (days60 == null) { 
			days60 = MathUtils.ZERO.toString(); 
		}
		
		days60 = MathUtils.setBDScale((new BigDecimal(days60)).add(amt)).toString();
	}
	
	public void setDays60(String days60) {
		this.days60 = days60;
	}
	public String getDays90() {
		
		if (days90 == null) { 
			days90 = MathUtils.ZERO.toString(); 
		}
		return days90;
	}
	
	public void addDays90(BigDecimal amt) {
		if (days90 == null) { 
			days90 = MathUtils.ZERO.toString(); 
		}
		
		days90 = MathUtils.setBDScale((new BigDecimal(days90)).add(amt)).toString();
	}
	
	public void setDays90(String days90) {
		this.days90 = days90;
	}
	public String getDays120() {
		
		if (days120 == null) { 
			days120 = MathUtils.ZERO.toString(); 
		}
		return days120;
	}
	public void setDays120(String days120) {
		this.days120 = days120;
	}
	public String getDays180() {
		
		if (days180 == null) { 
			days180 = MathUtils.ZERO.toString(); 
		}
		return days180;
	}
	
	public void addDays180(BigDecimal amt) {
		if (days180 == null) { 
			days180 = MathUtils.ZERO.toString(); 
		}
		
		days180 = MathUtils.setBDScale((new BigDecimal(days180)).add(amt)).toString();
	}
	
	public void setDays180(String days180) {
		this.days180 = days180;
	}
	public String getDaysOver180() {
		
		if (daysOver180 == null) { 
			daysOver180 = MathUtils.ZERO.toString(); 
		}
		return daysOver180;
	}
	
	public void addDaysOver180(BigDecimal amt) {
		if (daysOver180 == null) { 
			daysOver180 = MathUtils.ZERO.toString(); 
		}
		
		daysOver180 = MathUtils.setBDScale((new BigDecimal(daysOver180)).add(amt)).toString();
	}
	
	public void setDaysOver180(String daysOver180) {
		this.daysOver180 = daysOver180;
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPreparedByFirstName() {
		return preparedByFirstName;
	}

	public void setPreparedByFirstName(String preparedByFirstName) {
		this.preparedByFirstName = preparedByFirstName;
	}

	@Deprecated
	public boolean migrate() {
		boolean changed = false;
		
		// split using current name or get it fresh from current info
		// - invoice is historical. using existing name and split may be saver
		// - assuming ", " is unique delimiter. if space count != 1 flag it		
		if( this.customerFirstName == null && 
			this.customerLastName == null && 
			this.customerName != null ) {
			String name[] = this.customerName.split(", ");
			if( name.length == 2 ) {
				this.customerLastName = name[0];
				this.customerFirstName = name[1];
				this.customerName = null;
				changed = true;
			} else {
				// TODO warning
			}
		}		
		
		return changed;
	}

	@Override
	public String toString() {
		return "ArAgingRE [id=" + id + ", reportName=" + reportName
				+ ", snapshotDate=" + snapshotDate + ", reportId=" + reportId
				+ ", invoiceNumber=" + invoiceNumber + ", customerId="
				+ customerId + ", customerName=" + customerName
				+ ", customerFirstName=" + customerFirstName
				+ ", customerLastName=" + customerLastName + ", portionType="
				+ portionType + ", amt=" + amt + ", remainingBalance="
				+ remainingBalance + ", days30=" + days30 + ", days60="
				+ days60 + ", days90=" + days90 + ", days120=" + days120
				+ ", days180=" + days180 + ", daysOver180=" + daysOver180 + "]";
	}		
}
