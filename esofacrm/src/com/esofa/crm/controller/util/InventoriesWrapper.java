package com.esofa.crm.controller.util;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import com.esofa.crm.model.Inventory;

public class InventoriesWrapper implements Serializable {

	private static final long serialVersionUID = -4871515492317932020L;

	
	@Valid
	private List<Inventory> inventories;
	private Integer productLevelThreshold;						//for rule engine

	@NotBlank(message="{product.inventoryComment.blank}")
	private String inventoryChangeComment;
	
	public InventoriesWrapper(List<Inventory> inventories){
		this.inventories = inventories;
	}
	
	public InventoriesWrapper(){
		
	}
	
	public List<Inventory> getInventories() {
		return inventories;
	}

	public void setInventories(List<Inventory> inventories) {
		this.inventories = inventories;
	}
	
	public void setProductLevelThreshold(Integer productLevelThreshold) {
		this.productLevelThreshold = productLevelThreshold;
	}
	
	public Integer getProductLevelThreshold() {
		return productLevelThreshold;
	}
	
	//helper function in wrapper for rule engine
	public int getInventoryTotal() {
		
		int total=0;
		
		for (Inventory i : inventories) {
			total = total + i.getQty();
		}
		
		return total;
	}
	

	public String getInventoryChangeComment() {
		return inventoryChangeComment;
	}
	
	public void setInventoryChangeComment(String inventoryChangeComment) {
		this.inventoryChangeComment = inventoryChangeComment;
	}
	
}
