package com.esofa.crm.controller;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerAlert;
import com.esofa.crm.model.CustomerAlertSearch;
import com.esofa.crm.model.ProductAlert;
import com.esofa.crm.model.ProductAlertSearch;
import com.esofa.crm.refdata.model.AlertCategoryE;
import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.CustomerAlertService;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.ProductAlertService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.crm.util.PeriodFilterUtil;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/alert")
public class AlertController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(AlertController.class
			.getName());
	
	@Autowired
	CustomerAlertService customerAlertService;
	
	@Autowired
	ProductAlertService productAlertService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private RefdataService refdataService;
	
	@Autowired
	private CustomerService customerService;	
	
	@RequestMapping(value = { "/customer", "/customer/" ,"/customer/list"}, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listAllCustomerAlert(@ModelAttribute("customerAlertSearch") CustomerAlertSearch customerAlertSearch) {
			
		ModelAndView mav = new ModelAndView();
		
		Date date = EsofaUtils.getDateAfterNDays(customerAlertSearch.getNumDays());
		
		List<CustomerAlert> alerts = new ArrayList<CustomerAlert>(1);
		
		//clinician only
		if(customerAlertSearch.getClinician()!=null && customerAlertSearch.getAlertSubType() == null){
			alerts = customerAlertService.getCustomerAlertbyClinician(date, customerAlertSearch.getClinician());
					
		//alertSubType only
		} else if (customerAlertSearch.getClinician()==null && customerAlertSearch.getAlertSubType() != null){
			alerts = customerAlertService.getCustomerAlertByAlertSubType(date, customerAlertSearch.getAlertSubType());
				
		//both clinician and alertSubType
		} else if (customerAlertSearch.getClinician()!=null && customerAlertSearch.getAlertSubType() != null){
			alerts = customerAlertService.getCustomerAlert(date, customerAlertSearch.getClinician(),customerAlertSearch.getAlertSubType());
			
		}else{
			alerts = customerAlertService.getCustomerAlertBefore(date);
		}
		
		List<Key<Customer>> customerKeys = new ArrayList<Key<Customer>>(alerts.size());
		
		for (CustomerAlert ca : alerts) {
			customerKeys.add(ca.getCustomer());
		}
		
		mav.addObject("customerAlertSearch", customerAlertSearch);
		mav.addObject("alerts", alerts);
		prepareCommonAlertMAV(mav,AlertCategoryE.CustomerAlert);
		mav.addObject("clinicians", userService.getCrmUserMap(true));
		mav.addObject("customers",customerService.getCustomersByKey(customerKeys));

		mav.setViewName("alert-list");
		return mav;
	}
	
	@RequestMapping(value="/customer/dismiss",method = RequestMethod.GET)
	public ModelAndView customerAlertDismiss(@RequestParam(required = true) Long customerId,
			@RequestParam(required = false) Key<AlertSubType> alertSubType,
			@RequestParam(required = true) Long alertId,
			@RequestParam(required = true) int numDays
			)throws Exception{
		if(alertId!=null&&alertId!=0&&customerId!=null&&customerId!=0){
			customerAlertService.deleteCustomerAlertByKey(alertId, customerId);
		}
		CustomerAlertSearch customerAlertSearch = new CustomerAlertSearch();
		customerAlertSearch.setNumDays(numDays);
		customerAlertSearch.setCustomerId(customerId);
		customerAlertSearch.setAlertSubType(alertSubType);
		return listAllCustomerAlert(customerAlertSearch);
	}
	
	
	@RequestMapping(value = "/form-customer", method = RequestMethod.GET)
	public ModelAndView formCustomer(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		CustomerAlert alert = null;
		if (id == null) {
			alert = new CustomerAlert();
		} else {
			alert = ofy().load().type(CustomerAlert.class).id(id).now();
		}
		mav.addObject("alert", alert);
		mav.setViewName("alert-form-customer");
		return mav;
	}

	@RequestMapping(value = "/formsubmit-customer", method = RequestMethod.POST)
	public ModelAndView formCustomerSubmit(
			@ModelAttribute @Valid CustomerAlert alert, BindingResult result) {
		ModelAndView mav = new ModelAndView();

		System.out.println("Customer Alert");

		if (result.hasErrors()) {
			mav.addObject("alert", alert);
		} else {
			ofy().save().entity(alert).now();
		}
		mav.setViewName("message");
		mav.addObject("message", "customer alert saved");
		return mav;
	}

	@RequestMapping(value = "/form-product", method = RequestMethod.GET)
	public ModelAndView formProduct(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		ProductAlert alert = null;
		if (id == null) {
			alert = new ProductAlert();
		} else {
			alert = ofy().load().type(ProductAlert.class).id(id).now();
		}
		mav.addObject("alert", alert);
		mav.setViewName("alert-form-product");
		return mav;
	}

	@RequestMapping(value = "/formsubmit-product", method = RequestMethod.POST)
	public ModelAndView formProductSubmit(
			@ModelAttribute @Valid ProductAlert alert, BindingResult result) {
		ModelAndView mav = new ModelAndView();

		System.out.println("Product Alert");

		if (result.hasErrors()) {
			mav.addObject("alert", alert);
		} else {
			ofy().save().entity(alert).now();
		}
		mav.setViewName("message");
		mav.addObject("message", "product alert saved");
		return mav;
	}

	/** NEW STYLE **/
	@RequestMapping(value = "/tabview", method = RequestMethod.GET)
	public ModelAndView tabview(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("alert-tabview");
		return mav;
	}
	
	@RequestMapping(value = "/tabview/contact-alerts", method = RequestMethod.GET)
	public ModelAndView tabviewContact(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("alert-tabview-contact");
		return mav;
	}
	
	@RequestMapping(value = "/tabview/appointment-alerts", method = RequestMethod.GET)
	public ModelAndView tabviewAppointment(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("alert-tabview-appointment");
		return mav;
	}
	
	@RequestMapping(value = "/tabview/payment-alerts", method = RequestMethod.GET)
	public ModelAndView tabviewPayment(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("alert-tabview-payment");
		return mav;
	}
	
	/** Product Alert **/
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = {"/product","/product/","/product/list"}, method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView productAlertList(@ModelAttribute("productAlertSearch") ProductAlertSearch productAlertSearch){
		ModelAndView mav = new ModelAndView();
		
		if (productAlertSearch == null) {
			productAlertSearch = new ProductAlertSearch();
		}
		Date date = EsofaUtils.getDateAfterNDays( productAlertSearch.getNumDays());
		
		List<ProductAlert> alerts;
		
		Map<Key<Manufacturer>, Manufacturer> manufacturerMap = refdataService.getManufacturerMap();
		
		//manufacturer only
		if(productAlertSearch.getManufacturer()!=null && productAlertSearch.getAlertSubType() == null){
			alerts = productAlertService.getAlertbyManufacturer(date, productAlertSearch.getManufacturer());
					
		//alertSubType only
		} else if (productAlertSearch.getManufacturer()==null && productAlertSearch.getAlertSubType() != null){
				alerts = productAlertService.getAlertByAlertSubType(date, productAlertSearch.getAlertSubType());
				
		//both clinician and alertSubType
		} else if (productAlertSearch.getManufacturer()!=null && productAlertSearch.getAlertSubType() != null){
			alerts = productAlertService.getAlert(date, productAlertSearch.getManufacturer(),productAlertSearch.getAlertSubType());
			
		}else{
			alerts = productAlertService.getAlertNextNDay(date);
		}
		
		mav.addObject("productAlertSearch", productAlertSearch);
		mav.addObject("alerts", alerts);
		mav.addObject("manufacturers",manufacturerMap);
		prepareCommonAlertMAV(mav,AlertCategoryE.ProductAlert);
		mav.addObject("productMap",productService.getProductMap());
		mav.setViewName("alert-product-list");
		return mav;
	}	
	
	
	@RequestMapping(value="/product/dismiss",method = RequestMethod.GET)
	public ModelAndView productAlertDismiss(@RequestParam(required = true) Long productId,
			@RequestParam(required = false) Key<AlertSubType> alertSubType,
			@RequestParam(required = true) Long alertId,
			@RequestParam(required = true) int numDays
			)throws Exception{
		if(alertId!=null&&alertId!=0&&productId!=null&&productId!=0){
			productAlertService.deleteAlertByKey(alertId, productId);
		}
		ProductAlertSearch productAlertSearch = new ProductAlertSearch();
		productAlertSearch.setNumDays(numDays);
		productAlertSearch.setAlertSubType(alertSubType);
		return productAlertList(productAlertSearch);
	}
	
	private ModelAndView prepareCommonAlertMAV(ModelAndView mav, AlertCategoryE alertCategory) {
		
		mav.addObject("alertType", refdataService.getAlertTypeMap());
		mav.addObject("alertTypeMap", refdataService.getFullAlertTypeMap(alertCategory));
		mav.addObject("alertSubType", refdataService.getAlertSubTypeMap());
		mav.addObject("periodFilter", PeriodFilterUtil.getPeriodFilter());
		return mav;
	}
}
