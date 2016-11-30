package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.esofa.crm.controller.util.InventoriesWrapper;
import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.InventoryCostEntry;
import com.esofa.crm.model.InventoryCostQueue;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.util.MathUtils;
import com.googlecode.objectify.Key;


@Service
public class InventoryService {
	private static final Logger log = Logger.getLogger(InventoryService.class
			.getName());

	private ProductService productService;
	
	private Object lockInventoryCost = new Object();
	private Object lockInventory = new Object();
	
	public void saveInventories(List<Inventory> inventories) {
		ofy().save().entities(inventories).now();;
	}

	private void saveInventory(Inventory inventory) {
		ofy().save().entity(inventory).now();
	}

	public Inventory adjustAndSaveInventory(Key<Product> productKey,Key<Shop> shopKey, int qty) {
		Inventory inventory = null;

		synchronized (lockInventory) {

			inventory = productService.getInventory(productKey, shopKey);
			inventory.incrementAndGet(qty);

			saveInventory(inventory);
		}

		return inventory;
	}

	//used for manual inventory adjustment.  
	//adjust for the inventory cost
	public BigDecimal getCostAndManualAdjustInventoryCosts(Product p, InventoriesWrapper iwBefore, InventoriesWrapper iwAfter) {	
		BigDecimal currentInventoryCost = MathUtils.ZERO;
		
		int totalBefore = iwBefore.getInventoryTotal();
		int totalAfter = iwAfter.getInventoryTotal();
		int delta = totalAfter - totalBefore;
		
		if (  delta < 0) {
			
			//this method subtracts the quantity...so need to negate first.
			delta = -delta;
			currentInventoryCost = getCostAndIncrementInventorySold(Key.create(Product.class,p.getId()),delta);
			currentInventoryCost = currentInventoryCost.negate();
		} else if (delta > 0) {
			
			//add it back at default cost			
			InventoryCostEntry costEntry = new InventoryCostEntry(new Date(), Key.create(Product.class,p.getId()), delta, p.getDefaultCost());			
			addInventoryCost(costEntry);			
			currentInventoryCost = MathUtils.toBD(costEntry.getCost()).multiply(new BigDecimal(costEntry.getQty()));
		}
		
		return currentInventoryCost;
	}
	
	
	public  void addInventoryCost(InventoryCostEntry costEntry) {
	
		synchronized (lockInventoryCost) {
			//need to handle case where InventoryCostQueue already exists		
			InventoryCostQueue icq = getInventoryCostQueue(costEntry
					.getProductKey());
			if (icq == null) {
				icq = new InventoryCostQueue();
				icq.setProductKey(costEntry.getProductKey());
			}
			//insert into queue.
			Key<InventoryCostEntry> iceKey = saveInventoryCost(costEntry);
			icq.add(iceKey);
			saveInventoryCostQueue(icq);
		}
	}
	
	private BigDecimal getProductDefaultCost(Key<Product> productKey) {		
		Product p = productService.getProductMap().get(productKey);				
		return MathUtils.toBD(p.getDefaultCost());
	}
	

	public  BigDecimal getCostAndIncrementInventorySold(Key<Product> productKey, int qty) {	
		BigDecimal currentInventoryCost = MathUtils.ZERO;	
							
		synchronized (lockInventoryCost) {
			InventoryCostQueue icq   = getInventoryCostQueue(productKey);
			int remainingQty = qty;
			

			if (icq == null) {  //if no costs, use product default cost
												
				currentInventoryCost = getProductDefaultCost(productKey);
				return currentInventoryCost.multiply(new BigDecimal(remainingQty));
			}
			
			//iterate until all the qty has been accounted for.
			while (remainingQty != 0) {
				
				//peek into the first item on queue. 
				Key<InventoryCostEntry> currentCostEntry =  icq.peek();
				
				if (currentCostEntry == null) {  //no cost entry left. use default cost
				
					currentInventoryCost = currentInventoryCost.add(getProductDefaultCost(productKey));
					return currentInventoryCost.multiply(new BigDecimal(remainingQty));
				}
				
				//there is a cost entry.
				InventoryCostEntry ice = getInventoryCost(currentCostEntry);
				int qtyToMark=remainingQty;
				
				//try to mark all remaining to be used. if not, 
				//then try to mark off the max possible.
				if (remainingQty > ice.getRemainingQty()) {
				
					qtyToMark = ice.getRemainingQty();
				}
				
				remainingQty = remainingQty - qtyToMark;
				
				//increment qty sold.  then also get the inventory cost for this entry.
				//currentInventoryCost = currentInventryCost + (cost * qtyToMark)
				ice.incrementAndGetQtySold(qtyToMark);
				currentInventoryCost = currentInventoryCost.add(MathUtils.toBD(ice.getCost()).multiply(new BigDecimal(qtyToMark)));
				
				//if we already sold everything in this entry. aka original qty = qtySold
				if (ice.getRemainingQty() == 0) {
					ice.setStatus(InventoryCostEntry.Status.AllSold.toString());
					
					icq.poll();	//remove the entry we peeked into
					saveInventoryCostQueue(icq);
				}				
				
				//save changes to the ice entry.				
				saveInventoryCost(ice);				
			}
		}
		return currentInventoryCost;
	}
	
	/**
	 * unsold due to void or other reasons. need to add back into inventory
	 * 1 => decrement sold amt by one.  aka increase inventory by 1
	 */
	public void decrementInventorySold(Key<Shop> shopKey, Key<Product> productKey, int qty) {		
		//add new item to inventory cost queue at default price
		Product p = productService.getProductByKey(productKey);
		InventoryCostEntry costEntry = new  InventoryCostEntry(new Date(),productKey,qty,p.getDefaultCost());
		
		//add inventory back to that location
		adjustAndSaveInventory(productKey, shopKey, qty);
		addInventoryCost(costEntry);
	}
	
	public Key<InventoryCostEntry> saveInventoryCost(InventoryCostEntry costEntry) {
		return ofy().save().entity(costEntry).now();
	}

	public InventoryCostEntry getInventoryCost(Key<InventoryCostEntry> entryKey) {
		return ofy().load().key(entryKey).now();
	}
	
	public List<InventoryCostEntry> getInventoryCost( Date from, Date to) {
		return ofy().load().type(InventoryCostEntry.class).filter("insertDateTime >=", from).filter("insertDateTime <", to).order("insertDateTime").chunk(100).list();
	}	

	@CacheEvict(value="inventoryCostQueueByProductKey", key="#icq.productKey" )
	public void saveInventoryCostQueue(InventoryCostQueue icq) {
		ofy().save().entity(icq);		
	}
	
	
	@Cacheable(value = "inventoryCostQueueByProductKey", key="#productKey")
	public InventoryCostQueue getInventoryCostQueue(Key<Product> productKey) {
		return ofy().load().type(InventoryCostQueue.class).filter("productKey", productKey).first().now();
	}
	
	public InventoryCostQueue getInventoryCostQueue(Long id) {		
		return getInventoryCostQueue(Key.create(Product.class,id));
	}	
	
	public Map<Key<Product>, InventoryCostQueue> getInventoryCostQueueMap() {
		List<InventoryCostQueue> entries = ofy().load().type(InventoryCostQueue.class).list();
		Map<Key<Product>,InventoryCostQueue> icqMap = new HashMap<Key<Product>,InventoryCostQueue>();
		
		for (InventoryCostQueue icq : entries) {
			icqMap.put(icq.getProductKey(), icq);
		}
		return icqMap;
	}
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
