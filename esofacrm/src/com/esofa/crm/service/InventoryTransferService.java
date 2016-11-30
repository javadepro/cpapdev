package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.InventoryCostEntry;
import com.esofa.crm.model.InventoryTransfer;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.rule.RuleEngineUtils;
import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.cmd.Query;

public class InventoryTransferService {
	private static final Logger log = Logger.getLogger(InventoryTransferService.class
			.getName());
	
	private InventoryService inventoryService;
	private InventoryAuditService inventoryAuditService;
	
	public List<InventoryTransfer> getInventoryTransferRequests(Key<Shop> shop){
		Query<InventoryTransfer> q = ofy().load().type(InventoryTransfer.class);
		
		if (shop != null) { q = q.filter("toShop", shop);}
				
		return q.order("-initDate").list();
	}
	
	/**
	 * get the sum of all the product quantities locked up in transfers.
	 * @return
	 */
	public Map<Long,Integer> getInventoryTransferQty() {		
		List<InventoryTransfer> transfers = ofy().load().type(InventoryTransfer.class).list();
		
		Map<Long,Integer> productTransferQtyMap = new HashMap<Long,Integer>(transfers.size());
		
		for (InventoryTransfer item: transfers) {
			
			int qty = item.getQty();
			
			if (productTransferQtyMap.containsKey(item.getProduct().getId())) {
				
				qty = qty + productTransferQtyMap.get(item.getProduct().getId());				
			} 
			
			productTransferQtyMap.put(item.getProduct().getId(), qty);
		}
		
		return productTransferQtyMap;		
	}
	
	public int getInventoryTransferQty(Long productId) {		
		return getInventoryTransferQty(Key.create(Product.class, productId));		
	}
	
	/**
	 * for a given product, get all the quantity that is 'pending' transfer
	 * @param productKey
	 * @return
	 */
	public int getInventoryTransferQty(Key<Product> productKey) {		
		List<InventoryTransfer> transfers = ofy().load().type(InventoryTransfer.class).filter("product", productKey).list();
		int sumQty=0;
		
		for (InventoryTransfer item : transfers) {			
			sumQty =sumQty + item.getQty();
		}
		
		return sumQty;
	}
	
	public void saveInventoryTransferRequest(Key<CrmUser> crmUser, InventoryTransfer inventoryTransfer) {
		ofy().save().entity(inventoryTransfer).now();
				
		Key<Product> productKey =inventoryTransfer.getProduct();
		Key<Shop> shopKey = inventoryTransfer.getFromShop();
		int qty = -inventoryTransfer.getQty();
		
		inventoryAuditService.generateInitiateTransfer(crmUser, inventoryTransfer);
		inventoryService.adjustAndSaveInventory(productKey, shopKey, qty);		
	}
	
	public void saveInventoryAddStockRequest(Key<CrmUser> crmUser, InventoryTransfer inventoryTransfer) {
		
		InventoryCostEntry costEntry = new InventoryCostEntry(inventoryTransfer.getInitDate(),
				inventoryTransfer.getProduct(), inventoryTransfer.getQty(), inventoryTransfer.getCost());
		
		Key<Product> productKey =inventoryTransfer.getProduct();
		Key<Shop> shopKey = inventoryTransfer.getToShop();
		int qty = inventoryTransfer.getQty();
		
		inventoryService.adjustAndSaveInventory(productKey, shopKey, qty);
		inventoryService.addInventoryCost(costEntry);
		inventoryAuditService.generateAddStock(crmUser, inventoryTransfer);		
	}
	
	
	public void delete(Key<InventoryTransfer> key) {
		ofy().delete().key(key).now();
	}
	
	public void delete(Long id) {		
		Key<InventoryTransfer> transferKey  = Key.create(InventoryTransfer.class,id);
		delete(transferKey);
	}
	
	public InventoryTransfer get(Long id) {
		Key<InventoryTransfer> transferKey  = Key.create(InventoryTransfer.class,id);
		InventoryTransfer it=null;
		try {
			it= ofy().load().key(transferKey).now();
		} catch (NotFoundException e) {

			log.severe("item not found");
			log.severe(ExceptionUtils.getStackTrace(e));
		}
		
		return it;
	}
	
	public boolean acceptTransfer(Key<CrmUser> crmUser,Long transferId) {
		
		return inventoryAcceptCancelHelper(crmUser,transferId, false);
	}
	
	public boolean cancelTransfer(Key<CrmUser> crmUser, Long transferId) {
	
		return inventoryAcceptCancelHelper(crmUser,transferId, true);
	}
	
	//responsible for updating the inventory and deleting the transfer request
	private boolean inventoryAcceptCancelHelper(Key<CrmUser> crmUser,Long transferId, boolean reverseTxn) {
		
		InventoryTransfer transfer = get(transferId);		
		if (transfer == null) { return false;}
		
		adjustInventoryBasedOnTransfer(transfer, reverseTxn);		
		delete(transferId);
		
		if (!reverseTxn) {

			inventoryAuditService.generateTransferAccepted(crmUser, transfer);
		} else {

			inventoryAuditService.generateTransferCancel(crmUser, transfer);
			
			//set status on transfer object for rule engine.
			transfer.setStatus(InventoryTransfer.TransferStatusE.CANCELED.toString());
			WorkPackage<InventoryTransfer, InventoryTransfer> payload 
				= new WorkPackage<InventoryTransfer, InventoryTransfer>(crmUser, 
						Key.create(InventoryTransfer.class,transfer.getId()), 
					transfer,transfer);
			RuleEngineUtils.pushWorkPackageIntoQueue(payload);
		}
		
		return true;
	}
		
	private void adjustInventoryBasedOnTransfer(InventoryTransfer transfer, boolean reverseTxn) {		

		Key<Product> productKey =transfer.getProduct();
		int fromQty = transfer.getQty();
		Key<Shop> shopKey =null;
		
		if (reverseTxn) {
			shopKey= transfer.getFromShop();
			
		} else {
			
			shopKey = transfer.getToShop();			
		}

		inventoryService.adjustAndSaveInventory(productKey, shopKey, fromQty);		
	}
	
	public void setInventoryAuditService(
			InventoryAuditService inventoryAuditService) {
		this.inventoryAuditService = inventoryAuditService;
	}
	
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}	
}
