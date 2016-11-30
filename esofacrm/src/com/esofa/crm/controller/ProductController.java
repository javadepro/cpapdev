package com.esofa.crm.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.controller.util.InventoriesWrapper;
import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.InventoryTransfer;
import com.esofa.crm.model.InventoryTransferSearch;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.ProductSearch;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.rule.RuleEngineUtils;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.InventoryAuditService;
import com.esofa.crm.service.InventoryService;
import com.esofa.crm.service.InventoryTransferService;
import com.esofa.crm.service.ProductService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/product")
public class ProductController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(ProductController.class
			.getName());

	@Autowired
	private RefdataService refdataService;
	@Autowired
	private ProductService productService;
	
	@Autowired
	@Qualifier("inventoryAuditService")
	private InventoryAuditService inventoryAuditService;
	@Autowired
	private InventoryTransferService inventoryTransferService;

	@Autowired
	private InventoryService inventoryService;
	
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Long shopId) {
		ModelAndView mav = new ModelAndView();

		//mav.addObject("productMap", productService.getProductMapWithInventory());
		Map<Product, Map<Key<Shop>, Inventory>> productMap = new HashMap<>();
		 productService.getProductMapWithInventory(null,productMap,500000);
		mav.addObject("productMap",productMap);
		mav.addObject("inventoryTransferQtyMap", inventoryTransferService.getInventoryTransferQty());
		
		// Display 1 shop or All?
		if (shopId != null && shopId != 0) {
			Map<Key<Shop>, Shop> shopMap = new TreeMap<Key<Shop>, Shop>();
			shopMap.put(Key.create(Shop.class, shopId),
					refdataService.getShopById(shopId));
			mav.addObject("shopMap", shopMap);
			mav.addObject("shopId", shopId);

		} else {
			mav.addObject("shopMap", refdataService.getActiveShopMap());
		}

		mav.addObject("productSubTypeMap",
				refdataService.getProductSubTypeMap());
		
		mav.addObject("manufacturers", refdataService.getManufacturerMap());

		if (isAjax)
			mav.setViewName("product-list-ajax");
		else
			mav.setViewName("product-list");
		return mav;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView formAdd() {
		ModelAndView mav = new ModelAndView();

		/** Ref data **/
		prepareProductDetailMAV(mav);

		mav.addObject("product", new Product());
		mav.setViewName("product-form-add");

		return mav;
	}
	
	@RequestMapping(value = "/add-formsubmit", method = RequestMethod.POST)
	public ModelAndView productAddFormSubmit(
			@ModelAttribute("product") @Valid Product product,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("product-form-add");

		} else {
			
			if (product.getGenerateBarCode()) {
				product.setProductBarCode(productService.generateBarCode());
			}
			
			productService.saveProduct(product);
			mav.addObject("message", "saved");
			mav.setViewName("redirect:/product/form?id="+product.getId());
		}

		mav.addObject("product", product);

		/** Ref data **/
		prepareProductDetailMAV(mav);
		return mav;
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();

		Map<Shop, Inventory> inventoryMap = productService
				.getInventoryMapByProductId(id);
	
		// To show inventories
		mav.addObject("inventories", buildInventoryWrappers(inventoryMap));
		mav.addObject("shops", buildShopList(inventoryMap) );
		// Product Info
		mav.addObject("product", productService.getProductById(id));

		/** Ref data **/
		prepareProductDetailMAV(mav);

		mav.setViewName("product-form-wrapper");
		return mav;
	}


	
	@RequestMapping(value = "product-formsubmit", method = RequestMethod.POST)
	public ModelAndView productFormSubmit(
			@ModelAttribute("product") @Valid Product product,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
 
		//build before inventories wrapper + shop
		Product beforeProduct = productService.getProductById(product.getId());
		Map<Shop, Inventory> beforeInventoryMap = productService.getInventoryMapByProductId(product.getId());
		InventoriesWrapper beforeInventoriesWrapper = buildInventoryWrappers(beforeInventoryMap);
		beforeInventoriesWrapper.setProductLevelThreshold(beforeProduct.getThresholdQty());

		//end build Before inventorywrapper
		
		if (result.hasErrors()) {

		} else {
			productService.saveProduct(product);
			mav.addObject("message", "saved");

			//build after inventoriesWrapper
			Map<Shop, Inventory> inventoryMap = productService.getInventoryMapByProductId(product.getId());

			// To show inventories
			InventoriesWrapper inventoriesWrapper = buildInventoryWrappers(inventoryMap);
			inventoriesWrapper.setProductLevelThreshold(product.getThresholdQty());
			
			//apply rule if necessary
			// Create workpackage
			WorkPackage<Product, InventoriesWrapper> wp = new WorkPackage<Product, InventoriesWrapper>(
					getCurrentUser(), inventoriesWrapper.getInventories().get(0).getProduct(), beforeInventoriesWrapper, inventoriesWrapper);

			RuleEngineUtils.pushWorkPackageIntoQueue( wp);			
		}

		mav.addObject("product", product);
		prepareProductDetailMAV(mav);
		mav.setViewName("product-form");

		return mav;
	}

	
	@RequestMapping(value = "/inventory-formsubmit", method = RequestMethod.POST)
	public ModelAndView inventoryFormSubmit(
			@ModelAttribute("inventoriesWrapper") @Valid InventoriesWrapper inventoriesWrapper,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		if (result.hasErrors()) {
			//no clue why i have to manually do it for this one.
			List<String> errors = new ArrayList<String>();
			
			for (ObjectError err : result.getAllErrors()) {
				errors.add(err.getDefaultMessage());
			}
			mav.addObject("errors",errors);
		} else {

			//obtain before inventory info
			 
			Key<Product> productKey =inventoriesWrapper.getInventories().get(0).getProduct();
			Long productId = productKey.getId();
			Map<Shop, Inventory> inventoryMap = productService.getInventoryMapByProductId(productId);
			InventoriesWrapper iwBefore = buildInventoryWrappers(inventoryMap);
			
			Key<CrmUser> user  = Key.create(CrmUser.class, getCurrentUser().getId());
			
			Product p = productService.getProductByKey(productKey);
			//save inventor entries.
			
			inventoryService.saveInventories(inventoriesWrapper.getInventories());
			BigDecimal inventoryCostForAdj = inventoryService.getCostAndManualAdjustInventoryCosts(p, iwBefore, inventoriesWrapper);
			
			//add audit entry
			inventoryAuditService.generateManualInventoryUpdateEntry(user, p, iwBefore, inventoriesWrapper,inventoryCostForAdj);
			
			//update wrapper to have productThreshold for ruleEngine
			inventoriesWrapper.setProductLevelThreshold(p.getThresholdQty());
			
			//apply rule if necessary
			// Create workpackage
			WorkPackage<Product, InventoriesWrapper> wp = new WorkPackage<Product, InventoriesWrapper>(
					getCurrentUser(), productKey, inventoriesWrapper, inventoriesWrapper);

			RuleEngineUtils.pushWorkPackageIntoQueue( wp);
			mav.addObject("message", "saved");
		}
		
		Map<Key<Shop>, Shop> shopMap = refdataService.getActiveShopMap();
		List<Shop> shops = new ArrayList<Shop>();

		List<Inventory> inventories = inventoriesWrapper.getInventories();

		// Get shop by inventory order
		for (Inventory inventory : inventories) {
			shops.add(shopMap.get(inventory.getShop()));
		}

		// To show inventories
		mav.addObject("inventories", inventoriesWrapper);
		mav.addObject("shops", shops);

		mav.setViewName("product-inventory-form");

		return mav;
	}

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		Map<Shop, Inventory> inventoryMap = productService
				.getInventoryMapByProductId(id);


		// To show inventories
		mav.addObject("inventories", buildInventoryWrappers(inventoryMap));
		mav.addObject("shops", buildShopList(inventoryMap));
		// Product Info
		mav.addObject("product", productService.getProductById(id));

		/** Ref data **/
		prepareProductDetailMAV(mav);

		mav.setViewName("product-view");
		return mav;
	}

	
	/** Product Search **/
	@RequestMapping(value = { "/search/form", "/search/", "/search" }, method = RequestMethod.GET)
	public ModelAndView searchForm() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("productTypeMap", refdataService.getFullProductTypeMap());
		mav.addObject("product", new ProductSearch());
		mav.setViewName("product-search");
		return mav;
	}

	@RequestMapping(value = "/search/formsubmit", method = RequestMethod.POST)
	public ModelAndView searchFormSubmit(
			@ModelAttribute("product") ProductSearch product,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		// ProductSearch Object
		mav.addObject("product", product);

		// Search Result
		mav.addObject("productMap", productService.search(product));

		/** Refdata **/
		mav.addObject("productTypeMap", refdataService.getFullProductTypeMap());
		mav.addObject("productType", refdataService.getProductTypeMap());
		mav.addObject("inventoryTransferQtyMap", inventoryTransferService.getInventoryTransferQty());

		mav.addObject("shopMap", refdataService.getActiveShopMap());
		mav.addObject("productSubTypeMap",
				refdataService.getProductSubTypeMap());
		mav.addObject("manufacturers", refdataService.getManufacturerMap());

		mav.setViewName("product-search-result");
		return mav;
	}
	
	/** Inventory Management **/
	@RequestMapping(value = { "/inventory/transfer-form"}, method = RequestMethod.GET)
	public ModelAndView inventoryTransferForm(@RequestParam(required = true) Long productId) {

		ModelAndView mav = new ModelAndView();
		InventoryTransfer transfer = new InventoryTransfer();		
		
		transfer.setProduct(Key.create(Product.class,productId));		
		transfer.setFromShop(refdataService.getWarehouseShop()); 
				
		prepareInventoryTransferMAV(mav,transfer);		
		mav.setViewName("inventory-transfer-form");
		return mav;
	}
	
	@RequestMapping(value = { "/inventory/transfer-formsubmit"}, method = RequestMethod.POST)
	public ModelAndView inventoryTransferFormSubmit(@ModelAttribute("inventoryTransferForm") @Valid InventoryTransfer transfer,
			BindingResult result) {

		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			
			prepareInventoryTransferMAV(mav,transfer);	
			mav.setViewName("inventory-transfer-form");
		} else {
			
			transfer.setInitDate(new Date());			
			transfer.setUser(getCurrentUserKey());
			inventoryTransferService.saveInventoryTransferRequest(getCurrentUserKey(),transfer);

			mav.setViewName("message");
			mav.addObject("message", "Saved");
		}
				
		return mav;
	}

	@RequestMapping(value = { "/inventory/transfer-accept"}, method = RequestMethod.GET)
	public ModelAndView inventoryTransferFormAccept(@RequestParam(required = true) Long transferId,
			@RequestParam(required=false) Long shopId) {
		
		boolean success=false;
		success= inventoryTransferService.acceptTransfer(getCurrentUserKey(),transferId);				
		return inventoryTransferActionReloadHelper(success,"Accepted",shopId);
	}

	@RequestMapping(value = { "/inventory/transfer-cancel"}, method = RequestMethod.GET)
	public ModelAndView inventoryTransferFormCancel(@RequestParam(required = true) Long transferId,
			@RequestParam(required=false) Long shopId) {
			
		boolean success=false;
		success = inventoryTransferService.cancelTransfer(getCurrentUserKey(),transferId);	
		return inventoryTransferActionReloadHelper(success,"Cancelled",shopId);
	}
	
	/**
	 * helper for accept and cancel inventory transfer actions.
	 * @param success
	 * @param successMsg
	 * @param shop
	 * @return
	 */
	public ModelAndView inventoryTransferActionReloadHelper(boolean success, String successMsg, Long shopId) { 
		
		InventoryTransferSearch its = new InventoryTransferSearch();
		
		if (shopId != null) {
			
			its.setShop(Key.create(Shop.class,shopId));
		}
		
		ModelAndView mav = inventoryTransferView(its);
		
		if (success ) {
			
			mav.addObject("message", successMsg);
		} else {
			
			mav.addObject("message","transfer entry already been changed by someone else");
		}
	
		return mav;
	}
	
	
	
	@RequestMapping(value = { "/inventory/addstock-form"}, method = RequestMethod.GET)
	public ModelAndView inventoryStockForm(@RequestParam(required = true) Long productId) {

		ModelAndView mav = new ModelAndView();
		InventoryTransfer transfer = new InventoryTransfer();
		Key<Product> productKey = Key.create(Product.class,productId);
		
		transfer.setProduct(productKey);
		transfer.setToShop(refdataService.getWarehouseShop());
		
		//get default inventory cost
		Product p = productService.getProductMap().get(productKey);
		transfer.setCost(p.getDefaultCost());
		
		prepareInventoryTransferMAV(mav, transfer);		
		mav.setViewName("inventory-addstock-form");
		return mav;
	}


	@RequestMapping(value = { "/inventory/addstock-formsubmit"}, method = RequestMethod.POST)
	public ModelAndView inventoryStockFormSubmit(@ModelAttribute("inventoryTransferForm") @Valid InventoryTransfer transfer,
			BindingResult result) {

		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			
			prepareInventoryTransferMAV(mav,transfer);	
			mav.setViewName("inventory-addstock-form");
		} else {
			
			transfer.setInitDate(new Date());
			transfer.setUser(getCurrentUserKey());
			
			inventoryTransferService.saveInventoryAddStockRequest(getCurrentUserKey(),transfer);

			mav.setViewName("message");
			mav.addObject("message", "Saved. Please search again to see the updated counts.");
		}
				
		return mav;
	}
	
	@RequestMapping(value = { "/inventory/transfer-view"}, method = RequestMethod.GET)
	public ModelAndView inventoryTransferView(@ModelAttribute("inventoryTransferSearch")
		InventoryTransferSearch inventoryTransferSearch) {
		
		ModelAndView mav = new ModelAndView();
		
		List<InventoryTransfer> transferRequests = 
				inventoryTransferService.getInventoryTransferRequests(inventoryTransferSearch.getShop());
		
		mav.addObject("transferRequests",transferRequests);
		mav.addObject("shopsAll", refdataService.getShopMap());
		mav.addObject("shops", refdataService.getActiveShopMap());
		mav.addObject("productMap",productService.getProductMap());
		mav.addObject("inventoryTransferSearch",inventoryTransferSearch);
		mav.addObject("users", userService.getCrmUserMap(true));
		mav.setViewName("inventory-transfer-view");
		return mav;
	}
		
	@SuppressWarnings("unchecked")
	public static <T extends Comparable> List<T> asSortedList(
			Collection<T> collection) {
		T[] array = collection.toArray((T[]) new Comparable[collection.size()]);
		Arrays.sort(array);
		return Arrays.asList(array);
	}
	
	//prepare ref data needed for product detail view.
	private void prepareProductDetailMAV(ModelAndView mav) {
		
		
		mav.addObject("productTypeMap", refdataService.getFullProductTypeMap());
		mav.addObject("productSubTypeMap", refdataService.getProductSubTypeMap());	
		mav.addObject("productTypeOnlyMap",refdataService.getProductTypeMap());
		mav.addObject("manufacturers", refdataService.getManufacturerMap());
	}
	
	private void prepareInventoryTransferMAV(ModelAndView mav, InventoryTransfer transfer) {

		mav.addObject("inventoryTransferForm", transfer);		
		mav.addObject("shops", refdataService.getActiveShopMap());
		mav.addObject("productMap",productService.getProductMap());
		mav.addObject("users",userService.getCrmUserMap(true));
	}
	
	
	private InventoriesWrapper buildInventoryWrappers(Map<Shop,Inventory> inventoryMap) {
		
		Set<Shop> shopKey = inventoryMap.keySet();

		List<Inventory> inventories = new ArrayList<Inventory>();
		List<Shop> shops = new ArrayList<Shop>();

		for (Shop shop : shopKey) {
			inventories.add(inventoryMap.get(shop));
		}

		return new InventoriesWrapper(inventories);
	}
	
	private List<Shop> buildShopList(Map<Shop,Inventory> inventoryMap) {
		Set<Shop> shopKey = inventoryMap.keySet();
		List<Shop> shops = new ArrayList<Shop>();

		for (Shop shop : shopKey) {
			shops.add(shop);
		}

		return shops;
	}
		
}
