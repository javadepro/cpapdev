package com.esofa.crm.controller.util;

import java.util.ArrayList;
import java.util.List;

import com.esofa.crm.model.InventoryTransfer;

public class InventoryTransferForm {
	
	private List<InventoryTransfer> transfer = new ArrayList<InventoryTransfer>();
	
	public InventoryTransferForm(){
		for(int i=0; i<5;i++){
			transfer.add(new InventoryTransfer());
		}
	}

	public List<InventoryTransfer> getTransfer() {
		return transfer;
	}

	public void setTransfer(List<InventoryTransfer> transfer) {
		this.transfer = transfer;
	}
	
	
}
