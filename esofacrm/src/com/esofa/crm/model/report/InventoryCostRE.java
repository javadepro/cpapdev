package com.esofa.crm.model.report;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * inventory cost report entry
 * @author JHa
 *
 */
@Entity
public class InventoryCostRE implements ReportEntry, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1857170457223373216L;
	
	@Id
	private Long id;
	@Index
	private String reportName;
	
	@Index
	private Date snapshotDate;
	@Index
	private String reportId;
	
	private String manufacturerName;
	private String productName;
	private String productBarcode;
	private int totalCount;
	private String avgUnitCost;
	private String totalInventoryCost;
	
	private Set<ShopCount>shopInvCountSet;
	public InventoryCostRE() {
		shopInvCountSet = new HashSet<ShopCount>();
		totalCount =0;
	}
	
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public Date getSnapshotDate() {
		return snapshotDate;
	}
	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductBarcode() {
		return productBarcode;
	}
	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public String getAvgUnitCost() {
		return avgUnitCost;
	}
	public void setAvgUnitCost(String avgUnitCost) {
		this.avgUnitCost = avgUnitCost;
	}
	public String getTotalInventoryCost() {
		return totalInventoryCost;
	}
	public void setTotalInventoryCost(String totalInventoryCost) {
		this.totalInventoryCost = totalInventoryCost;
	}

	public String getReportId() {
		return reportId;
	}
	
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Set<ShopCount> getShopInvCountSet() {
		return shopInvCountSet;
	}

	public void setShopInvCountSet(Set<ShopCount> shopInvCountSet) {
		this.shopInvCountSet = shopInvCountSet;
	}
}
