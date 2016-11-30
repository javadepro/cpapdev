package com.esofa.crm.service;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.controller.util.InventoriesWrapper;
import com.esofa.crm.model.AuditEntry;
import com.esofa.crm.model.AuditEntryTypeE;
import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.InventoryTransfer;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.googlecode.objectify.Key;

public class InventoryAuditService extends AuditService {

	private  RefdataService refdataService;
		
	private  ProductService productService;
	
		
	public void generateManualInventoryUpdateEntry(Key<CrmUser> crmUser, Product p, InventoriesWrapper iwBefore, InventoriesWrapper iwAfter, BigDecimal cost) {
		String auditNote = null;
				
		int totalBefore = iwBefore.getInventoryTotal();
		int totalAfter = iwAfter.getInventoryTotal();
		
		auditNote = String.format("<a href=\"/product/view?id=%s\" >%s</a>. beforeTotal: %d. afterTotal: %d.",p.getId(),p.getName(), totalBefore, totalAfter);
		
		AuditEntry ae = new AuditEntry(crmUser,	AuditEntryTypeE.INVENTORY_UPDATE, 
				auditNote, iwAfter.getInventoryChangeComment());
		
		ae.setQty(totalAfter - totalBefore);
		ae.setInventoryCost(cost);
	
		save(ae);
	}
	
	public void generateSaleEntry(Key<CrmUser> crmUser,Key<Product> productKey, Inventory inventory) {
		
		String auditNote = null;
		
		String shopName = refdataService.getShop(inventory.getShop()).getName();
		Product p = productService.getProductByKey(productKey);
		
		auditNote = String.format("<a href=\"/product/view?id=%s\" >%s</a>. sold at %s.",p.getId(),p.getName(), shopName);		
		save(crmUser,AuditEntryTypeE.SALE, auditNote,StringUtils.EMPTY);
	}

	public  void generateAddStock(Key<CrmUser> crmUser,  InventoryTransfer transfer ) {
		
		
 		String toShopName = refdataService.getShop(transfer.getToShop()).getName();
		Product p = productService.getProductByKey(transfer.getProduct());
		AuditEntryTypeE entryType = AuditEntryTypeE.ADD_STOCK;
				
		String auditNote  = String.format("Added %s units of  <a href=\"/product/view?id=%s\" >%s</a> of stock to %s.",transfer.getQty(),p.getId(),p.getName(), toShopName);
			
		save(crmUser,entryType,auditNote,StringUtils.EMPTY);
	}	
	
	public void generateVoidSaleEntry(Key<CrmUser> crmUser, Key<Shop> shopKey, Key<Product> productKey, String invoiceNumber) {
		
		String shopName = refdataService.getShop(shopKey).getName();
		Product p = productService.getProductByKey(productKey);
		AuditEntryTypeE entryType = AuditEntryTypeE.SALE_VOIDED;

		String auditNote = String.format("<a href=\"/pos/invoice-view?invoiceNumber=%s\" >%s</a> was void. Inventory adjusted for %s at %s.",
				invoiceNumber,invoiceNumber,p.getName(),shopName);		
		save(crmUser,entryType,auditNote,StringUtils.EMPTY);
	}
	
	public  void generateInitiateTransfer(Key<CrmUser> crmUser,  InventoryTransfer transfer ) {
				
		String fromShopName = refdataService.getShop(transfer.getFromShop()).getName();
		String toShopName = refdataService.getShop(transfer.getToShop()).getName();
		Product p = productService.getProductByKey(transfer.getProduct());
		AuditEntryTypeE entryType = AuditEntryTypeE.INITIATE_TRANSFER;
				
		String auditNote  = String.format("Transferred %s units of  <a href=\"/product/view?id=%s\" >%s</a>. from: %s. to : %s.",transfer.getQty(),p.getId(),p.getName(), fromShopName, toShopName);		
		save(crmUser,entryType,auditNote,StringUtils.EMPTY);
	}	
	
	public  void generateTransferAccepted(Key<CrmUser> crmUser,  InventoryTransfer transfer ) {

		String toShopName = refdataService.getShop(transfer.getToShop()).getName();
		Product p = productService.getProductByKey(transfer.getProduct());
		AuditEntryTypeE entryType = AuditEntryTypeE.ACCEPT_TRANSFER;
				
		String auditNote = String.format("Accepted %s units of <a href=\"/product/view?id=%s\"  >%s</a> into %s.",transfer.getQty(),p.getId(),p.getName(), toShopName);
		
		save(crmUser,entryType,auditNote,StringUtils.EMPTY);
	}	
	
	public  void generateTransferCancel(Key<CrmUser> crmUser,  InventoryTransfer transfer ) {
		
		String fromShopName = refdataService.getShop(transfer.getFromShop()).getName();
		String toShopName = refdataService.getShop(transfer.getToShop()).getName();
		Product p = productService.getProductByKey(transfer.getProduct());
		AuditEntryTypeE entryType = AuditEntryTypeE.CANCEL_TRANSFER;
				
		String auditNote = String.format("Cancelled %s units of <a href=\"/product/view?id=%s\"  >%s</a>. from: %s. to : %s.",transfer.getQty(),p.getId(),p.getName(),fromShopName, toShopName);
		
		save(crmUser,entryType,auditNote,StringUtils.EMPTY);
	}	
	

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

}
