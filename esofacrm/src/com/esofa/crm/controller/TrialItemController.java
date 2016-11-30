package com.esofa.crm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.controller.util.TrialItemForm;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerBasic;
import com.esofa.crm.model.CustomerSearch;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.trial.TrialItem;
import com.esofa.crm.model.trial.TrialItemSearch;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.service.TrialItemAuditService;
import com.esofa.crm.service.TrialItemService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/pos/trial")
public class TrialItemController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(TrialItemController.class
			.getName());

	@Autowired
	private RefdataService refdataService;
	@Autowired
	private TrialItemService trialItemService;
	
	@Autowired
	private TrialItemAuditService trialItemAuditService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	

	@RequestMapping(value = { "/","/list"}, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listAll(@ModelAttribute("trialItemSearch") TrialItemSearch trialItemSearch) {
			
		ModelAndView mav = new ModelAndView();
		
		//check shopkey
		List<TrialItem> trialItems=null;
		
		trialItems = trialItemService.getTrialItems(trialItemSearch.getLocation(),trialItemSearch.getTrialStatus());		
			
		List<Key<Customer>> customerKeys = new ArrayList<Key<Customer>>(trialItems.size());
		
		for (TrialItem ti : trialItems) {
			
			if (ti.getCustomerKey() != null) {
				customerKeys.add(ti.getCustomerKey());
			}
		}
					
		mav.addObject("customers",customerService.getCustomersByKey(customerKeys));
		mav.addObject("trialItems", trialItems);
		prepareTrialMAV(mav,false);
		mav.setViewName("trial-list");
		return mav;
	}
	

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView formAdd() {
		ModelAndView mav = new ModelAndView();
		TrialItemForm ti = new TrialItemForm();
		
		ti.setMode("add");
		prepareTrialMAV(mav,true);
		mav.setViewName("trial-form");
		mav.addObject("trialItemForm",ti);
		return mav;
	}
	


	
	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView addFormSubmit(
			@ModelAttribute("trialItemForm") @Valid TrialItemForm trialItemForm,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		 if (StringUtils.equalsIgnoreCase(trialItemForm.getActionType(), "Save" )) {
			
			TrialItem ti = trialItemForm.getTrialItem();
			
			//serial number is not null
			if (StringUtils.isEmpty(ti.getSerialNumber())) {
				
				result.addError(new FieldError("trialItemForm","serialNumber","serial number is required"));
			}
			
			if(ti.getProduct() == null) {
				
				result.addError(new FieldError("trialItemForm","product","product must be set"));
			}
			
			if (ti.getProduct() !=null) {
				List<TrialItem> items = trialItemService.getTrialItem(ti.getProduct(), ti.getSerialNumber());
				if (items != null && items.size() >0) {
					
					TrialItem alreadyExistsItem = items.get(0);
					
					if ((ti.getId() == null || !ti.getId().equals(alreadyExistsItem.getId()))) {
					
						result.addError(new FieldError("trialItemForm","serialNumber","there is already a trial item with this product and serial number."));
					}
				}
			}
			

			if (trialItemForm.getCustomerId()  != null) {
				
				Customer c= customerService.getCustomerById(trialItemForm.getCustomerId());
				if (c == null) {
					
					result.addError(new FieldError("trialItemForm","customerFullName","customer does not exist"));
				} else {
					ti.setCustomerFullName(c.getName());
					ti.setCustomerKey(Key.create(Customer.class,c.getId()));;
				}
			}
			
			String trialStatus = trialItemForm.getTrialItem().getTrialStatus();
			//if not available, then customer info must be set
			//if avail, then customer info must be not set			
			if (!StringUtils.equalsIgnoreCase(trialStatus, TrialItem.TrialStatusE.AVAILABLE.toString())
					&& ti.getCustomerKey() == null) {
				
				result.addError(new FieldError("trialItemForm","trialStatus","if status is not available, then customer info must be set."));
			} else if (StringUtils.equalsIgnoreCase(trialStatus, TrialItem.TrialStatusE.AVAILABLE.toString())
				&& ti.getCustomerKey() !=null) {
				
					result.addError(new FieldError("trialItemForm","trialStatus","if status is available, then remove the customer."));
				}
			
			
			if (!result.hasErrors()) {
			
				
				if (ti.getId() == null) {

					trialItemAuditService.generateAddEntry(getCurrentUserKey(), ti);
				} else {
					trialItemAuditService.generateUpdateEntry(getCurrentUserKey(), ti);
				}				
				
				ti = trialItemService.saveTrialItem(ti);
				return prepareRedirectToView(ti.getId());
			}
			
		 } else if (StringUtils.equalsIgnoreCase(trialItemForm.getActionType(),"deleteCustomer")) {
			 trialItemForm.setHealthCardNumber(StringUtils.EMPTY);
			 trialItemForm.setCustomerId(null);
			 trialItemForm.getTrialItem().setCustomerFullName(StringUtils.EMPTY);
			 trialItemForm.getTrialItem().setCustomerKey(null);
			 
		} else if (StringUtils.equalsIgnoreCase(trialItemForm.getActionType(), "refresh")) {
			
			if(StringUtils.isNotEmpty(trialItemForm.getProductBarCode())) {
				Product p = productService.getProductByBarcode(trialItemForm.getProductBarCode());
				
				if (p == null) {
					
					result.addError(new FieldError("trialItemForm","productBarCode","could not find product with that barcode"));								
				} else {
				
					Key<Product> productKey = Key.create(Product.class, p.getId());			
					trialItemForm.getTrialItem().setProduct(productKey);
				}
			} else {
				trialItemForm.getTrialItem().setProduct(null);
			}
		} 
		prepareTrialMAV(mav,true);
		mav.addObject("trialItemForm",trialItemForm);
		mav.setViewName("trial-form");
		return mav;
	}
	
	protected ModelAndView prepareRedirectToView(Long id) {
		
		String viewUrl = "/pos/trial/view";
		ModelAndView mav = new ModelAndView("redirect:" +viewUrl);
		mav.addObject("id",id);
		mav.addObject("fwdFromSuccessfulSave",true);
		return  mav;
	}
	

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = true) Long id, @RequestParam(required=false) boolean fwdFromSuccessfulSave) {
		ModelAndView mav = new ModelAndView();

		//include successfully save msg if internal forward
		if (fwdFromSuccessfulSave) {
			mav.addObject("message", "Saved");
		}
		
		TrialItem trialItem = trialItemService.getTrialItem(id);
	
		if (trialItem.getCustomerKey() != null) {
			
			mav.addObject("customer",customerService.getCustomerByKey(trialItem.getCustomerKey()));
		}
		mav.addObject("trialItem", trialItem);
		prepareTrialMAV(mav,false);

		mav.setViewName("trial-view");
		return mav;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();

		TrialItemForm trialItemForm = new TrialItemForm();
		TrialItem trialItem = trialItemService.getTrialItem(id);
		
		if (trialItem.getCustomerKey() != null) {
			Customer c = customerService.getCustomerByKey(trialItem.getCustomerKey());
			trialItemForm.setCustomerId(c.getId());
			trialItemForm.setHealthCardNumber(c.getFullHealthCardNumber());
			mav.addObject("customer",c);
		}
		
		trialItemForm.setMode("edit");
		trialItemForm.setTrialItem(trialItem);
		
		prepareTrialMAV(mav,true);
		mav.setViewName("trial-form");
		mav.addObject("trialItemForm",trialItemForm);
		return mav;
	}
	
	@RequestMapping(value = "/delete-form", method = RequestMethod.GET)
	public ModelAndView deleteForm(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();

		TrialItem trialItem = trialItemService.getTrialItem(id);
		
		mav.addObject("trialItem",trialItem);
		mav.setViewName("trial-delete-form");
		return mav;
	}
	
	@RequestMapping(value = "/delete-formsubmit", method = RequestMethod.POST)
	public ModelAndView deleteFormSubmit(
			@ModelAttribute("trialItem") TrialItem trialItem,
			BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		TrialItem origTrialItem=null;
		
		if (trialItem == null && trialItem.getId() == null) {
		
			result.addError(new ObjectError("id", "Error.  Unable to load Trial/Rental item"));
		} else if ((origTrialItem = trialItemService.getTrialItem(trialItem.getId())) == null) {
			
			result.addError(new ObjectError("id", "Error.  Unable to find Trial/Rental item"));
		} else {
			
			
			Key<TrialItem> trialItemKey = Key.create(TrialItem.class,trialItem.getId());
			trialItemAuditService.generateDeleteEntry(getCurrentUserKey(), origTrialItem);
			trialItemService.deleteTrialItem(trialItemKey);					
			mav.addObject("message", String.format("The trial item with serial %s, has been deleted.",origTrialItem.getSerialNumber()));
		}
	 
		mav.setViewName("trial-delete-msg");
		return mav;
	}

	/**
	 * ajax call for customer lookup.
	 * @param term
	 * @param response
	 * @return a json response.
	 */
	@RequestMapping(value ="/customerSearch", method=RequestMethod.GET)  
	public @ResponseBody List<CustomerBasic> getCustomers(@RequestParam(required = true) String term, HttpServletResponse response){

		List<CustomerBasic> customers = new ArrayList<CustomerBasic>();
		CustomerSearch customerSearch = new CustomerSearch();
		customerSearch.setFirstname(term + "*");

		Map<Key<Customer>, Customer> resultMap = customerService
				.search(customerSearch);

		for (Customer c : resultMap.values()) {
			customers.add(new CustomerBasic(c.getId(), c.getName(),c.getFullHealthCardNumber()));
		}

		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();

		MediaType jsonMimeType = MediaType.APPLICATION_JSON;

		try {
			jsonConverter.write(customers, jsonMimeType,
					new ServletServerHttpResponse(response));
		} catch (Exception e) {
			log.log(Level.SEVERE, "unable to get customers ", e);
		}

		return null;

	}
	
	
	//prepare ref data needed for product detail view.
	private void prepareTrialMAV(ModelAndView mav, boolean activeShopOnly) {
		
		if (activeShopOnly) {
			mav.addObject("shops",refdataService.getActiveShopMap());
 		} else {
 			mav.addObject("shops",refdataService.getShopMap());	
 		}
		
		mav.addObject("productMap",productService.getProductMap());
		mav.addObject("trialStatuses", getTrialStatusList());		
	}
		
	public static List<String>getTrialStatusList () {
		
		List<String> trialStatuses  = new ArrayList<String>();		
		
		trialStatuses.add(TrialItem.TrialStatusE.AVAILABLE.toString());
		trialStatuses.add(TrialItem.TrialStatusE.ON_TRIAL.toString());
		trialStatuses.add(TrialItem.TrialStatusE.RENTED.toString());
		return trialStatuses;
	}
	
	
	public void setTrialItemAuditService(
			TrialItemAuditService trialItemAuditService) {
		this.trialItemAuditService = trialItemAuditService;
	}
	
	public void setTrialItemService(TrialItemService trialItemService) {
		this.trialItemService = trialItemService;
	}
	
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
