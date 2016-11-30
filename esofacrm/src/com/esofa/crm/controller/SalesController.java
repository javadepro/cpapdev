package com.esofa.crm.controller;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.controller.util.DailySalesForm;
import com.esofa.crm.controller.util.SalesItem;
import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.rule.RuleEngineUtils;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.InventoryService;
import com.esofa.crm.service.ProductService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

//@Controller
//@RequestMapping(value = "/pos2")
public class SalesController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(SalesController.class
			.getName());

	@Autowired
	RefdataService refdataService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ProductService productService;

	@Autowired
	private InventoryService inventoryService;
	
	@RequestMapping(value = { "", "/", "form" }, method = RequestMethod.GET)
	public ModelAndView salesForm() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("pos-salesform");
		mav.addObject("salesForm", new DailySalesForm());
		mav.addObject("shops", refdataService.getShopMap());
		mav.addObject("customers", customerService.getCustomerAll());
		mav.addObject("products", productService.getProductMap());

		return mav;
	}

	@RequestMapping(value = "formsubmit", method = RequestMethod.POST)
	public ModelAndView salesFormSubmit(
			@ModelAttribute("salesForm") @Valid DailySalesForm salesForm,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		Map<Key<Customer>,Customer> customerMap = customerService.getCustomerAll();
		mav.setViewName("pos-salesform");
		mav.addObject("salesForm", salesForm);
		mav.addObject("shops", refdataService.getShopMap());
		mav.addObject("customers", customerMap);
		mav.addObject("products", productService.getProductMap());
		if (result.hasErrors()) {
			mav.addObject("message", "ERROR");
			return mav;
		}

		Key<Shop> shop = salesForm.getShop();
		Date date = salesForm.getDate();
		for (SalesItem item : salesForm.getItems()) {
			if (item.getCustomer() == null && item.getProduct() == null
					&& item.getQty() == 0) {
				continue;
			} else if (item.getCustomer() == null || item.getProduct() == null) {
				log.info("Product " + item.getProduct() + " OR customer "
						+ item.getCustomer() + " is empty");
				item.setStatus("CANNOT PROCESS: PRODUCT/CUSTOMER IS NOT VALID");
			} else {
				if (item.getQty() <= 0) {
					item.setStatus("CANNOT PROCESS: QTY CANNOT BE SMALLER THAN 0");
					continue;
				}
				/** FIX Inventory **/
				log.info("Reduce Product " + item.getProduct()
						+ " Inventory of Shop " + shop + " by");
				Product p = productService.getProductByKey(item.getProduct());
				Inventory inventory = productService.getShopInventoryByProductKey(item.getProduct(), shop );
				
				if(inventory==null){
					item.setStatus("CANNOT PROCESS: INVENTORY INFORMATION IS NOT AVAILABLE");
					break;
				}
				// Check if we have enough inventory to subtract
				if (inventory.getQty() < item.getQty()) {
					item.setStatus("CANNOT PROCESS: QTY REQUESTED < CURRENT INVENTORY IN SHOP");
					break;
				}

				inventory.setQty(inventory.getQty() - item.getQty());

				//inventoryService.saveInventory(inventory);
				
				/** Create Event **/
				/**
				 * CustomerEvent event = new CustomerEvent();
				event.setCreater("SYSTEM");
				event.setCustomer(item.getCustomer());
				event.setDetails("Purchased product " + p.getName()
						+ "  on " + date + "");
				event.setDate(date);

				event.setEventSubType(new Key<EventSubType>(new Key<EventType>(
						EventType.class, 600008), EventSubType.class, 610040));

				customerService.saveCustomerEvent(event);
				
				log.info("Create event");
				 */
				item.setProductName(productService.getProductByKey(item.getProduct()).getName());
				WorkPackage<Customer, SalesItem> wp = new WorkPackage<Customer, SalesItem>();
				CrmUser crmUser = new CrmUser();
				crmUser.setInitial("SYSTEM");
				wp.setInitiator(crmUser);
				wp.setAfter(item);
				wp.setTarget(item.getCustomer());
				RuleEngineUtils.pushWorkPackageIntoQueue( wp);


				/** Add Alert **/
				/**CustomerAlert alert = new CustomerAlert();
				alert.setClinician(customerMap.get(item.getCustomer()).getClinician());
				alert.setCustomer(item.getCustomer());
				alert.setCreatedBy("SYSTEM");
				alert.setMessage("6 Month alert after equiptment purchase");
				alert.setAlertSubType(new Key<AlertSubType>(new Key<AlertType>(
						AlertType.class, 700002), AlertSubType.class, 710004));
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.MONTH, 6);
				alert.setAlertDate(c.getTime());
				
				customerService.saveCustomerAlert(alert);**/
				
				item.setStatus("PROCESSED");

			}
		}

		mav.addObject("message", "processed");
		return mav;

	}
	
	

}
