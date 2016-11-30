package com.esofa.crm.rule;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.InventoryTransfer;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.service.UserService;
import com.esofa.crm.util.MailUtils;
import com.googlecode.objectify.Key;

public class CancelInventoryTransferAction<S extends Serializable> implements Action<InventoryTransfer, S>{

	private static final Logger log = Logger
			.getLogger(CancelInventoryTransferAction.class.getName());

	private final String subject = "Inventory Transfer Cancelled Notification ";
	
	private MailUtils mailUtils;
	private UserService userService;
	private ConfigService configService;
	private ProductService productService;
	private RefdataService refdataService;
	
	public void execute(WorkPackage<InventoryTransfer, S> workPackage) {
		
		
		try{
			
			Map<Key<CrmUser>, CrmUser> invManagers = userService.getInventoryManager();			
			InventoryTransfer before = (InventoryTransfer) workPackage.getBefore();
			CrmUser initiator = workPackage.getInitiator();
			Product p = productService.getProductMap().get(before.getProduct());		
			Map<Key<Shop>,Shop> shopMap = refdataService.getShopMap();
			String env = configService.getConfigStringByName("ENV");
			
			StringBuilder sb = new StringBuilder();
			sb.append("Inventory Transfer");
			sb.append(" canceled by :" + initiator.getName()).append(".");
			sb.append(" from: ").append(shopMap.get(before.getFromShop()).getShortName());
			sb.append(" to: ").append(shopMap.get(before.getToShop()).getShortName());
			sb.append(" product: ").append( p.getName()).append(".");
			sb.append(" barcode: ").append(p.getProductBarCode()).append(".");
			sb.append(" refNum: ").append( p.getReferenceNumber()).append(".");
			sb.append(" qty: ").append(before.getQty()).append(".");
			
			String msgBody = sb.toString();
			
			for (CrmUser mgr :invManagers.values()) { 
				
				String toAddress = mgr.getEmail();
				
				if (StringUtils.isNotEmpty(mgr.getAlternateEmail())) {
					toAddress = mgr.getAlternateEmail();
				}
				mailUtils.sendMail(toAddress, subject + env, msgBody);
				log.warning("CancelInventoryTransferAction sending notification for " + toAddress);

			}
			
		}catch(Exception ex){

			log.warning (ExceptionUtils.getStackTrace(ex));
		}
		
	}
	
	public void setMailUtils(MailUtils mailUtils) {
		this.mailUtils = mailUtils;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
	
}
