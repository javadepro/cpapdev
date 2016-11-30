package com.esofa.crm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.gmr.web.multipart.GMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.admin.controller.util.UploaderForm;
import com.esofa.crm.annotation.customer.CustomerTabNameE;
import com.esofa.crm.common.model.Address;
import com.esofa.crm.controller.util.CustomerControllerUtil;
import com.esofa.crm.controller.util.EventSearchForm;
import com.esofa.crm.drive.CustomerFolder;
import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.CompanyTypeE;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerAlert;
import com.esofa.crm.model.CustomerAlertSearch;
import com.esofa.crm.model.CustomerEvent;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerInsuranceInfo;
import com.esofa.crm.model.CustomerInsuranceInfoType1;
import com.esofa.crm.model.CustomerInsuranceInfoType2;
import com.esofa.crm.model.CustomerInsuranceInfoWrapper;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.model.CustomerMedicalInfoWrapper;
import com.esofa.crm.model.CustomerPaymentInfo;
import com.esofa.crm.model.CustomerSearch;
import com.esofa.crm.model.CustomerWrapper;
import com.esofa.crm.model.util.CustomerPdfMapperProvider;
import com.esofa.crm.model.util.PdfMapper;
import com.esofa.crm.refdata.model.AlertCategoryE;
import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.AlertType;
import com.esofa.crm.refdata.model.EventSubType;
import com.esofa.crm.refdata.model.FundingOption;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.rule.RuleEngineUtils;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.service.CpapMailerLiteService;
import com.esofa.crm.service.CustomerAlertService;
import com.esofa.crm.service.CustomerEventService;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.util.PeriodFilterUtil;
import com.esofa.gae.security.CrmAuthentication;
import com.esofa.spring.controller.GaeEnhancedController;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.model.File;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/customer")
public class CustomerController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(CustomerController.class
			.getName());

	@Autowired
	private RefdataService refdataService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerAlertService customerAlertService;

	@Autowired
	private CustomerEventService customerEventService;

	@Autowired
	private ConfigService configService;
	
	@Autowired
	private CpapMailerLiteService mailerLiteService;
	
	@Autowired
	private CustomerPdfMapperProvider pdfMapperProvider;

	@RequestMapping(value = "/onepager/new", method = RequestMethod.GET)
	public ModelAndView onepagerNew() {
		ModelAndView mav = new ModelAndView();

		/** New Guy **/
		CustomerWrapper customerWrapper = new CustomerWrapper();
		Customer customer = new Customer();
		CustomerExtendedInfo customerExtendedInfo = new CustomerExtendedInfo();
		customerWrapper.setCustomer(customer);
		customerWrapper.setCustomerExtendedInfo(customerExtendedInfo);

		setupBasicInfoForMAV(mav,true);
		mav.addObject("clinicians", refdataService.getClinicianMap(true));
		mav.addObject("customerWrapper", customerWrapper);
		mav.setViewName("customer-onepager-add");
		return mav;
	}

	@RequestMapping(value = "/onepager/new-save", method = RequestMethod.POST)
	public ModelAndView onepagerNewSave(
			@ModelAttribute("customerWrapper") @Valid CustomerWrapper customerWrapper,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		CustomerControllerUtil.verifyEmailConsent(customerWrapper, result);
		
		if (result.hasErrors()) {
			/** Go back to the same page if error **/

			mav.setViewName("customer-onepager-add");
			/** Options **/
			setupBasicInfoForMAV(mav,true);

			/** Command Obj **/
			mav.addObject("customerWrapper", customerWrapper);
		} else {
			boolean isNewCustomer = customerWrapper.getCustomer().getId() == null
					|| customerWrapper.getCustomer().getId() == 0;
			customerService.saveCustomerWrapper(customerWrapper);

			mailerLiteService.addUpdateSubscriber(customerWrapper);
			mav.setViewName("redirect:/customer/onepageredit?id="
					+ customerWrapper.getCustomer().getId());
		}
		return mav;
	}

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		Map<Key<Customer>, Customer> customers = customerService
				.getCustomerAll();
		mav.addObject("customers", customers);
		/** REFDATA **/
		mav.addObject("clinicians", refdataService.getClinicianMap());

		mav.setViewName("customer-list");
		return mav;
	}

	@RequestMapping(value = "/onepageredit", method = RequestMethod.GET)
	public ModelAndView onepagerEdit(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();
		CustomerWrapper customerWrapper = customerService
				.getCustomerWrapperById(id);
		if (customerWrapper == null) {
			// Go to error page
		}
		Key<Customer> customerKey = Key.create(Customer.class, id);

		/** Get customer insurance info **/
		Map<Key<FundingOption>, FundingOption> fundingOptionMap = refdataService
				.getFundingOptionMap();

		CustomerInsuranceInfo customerInsuranceInfo = customerService
				.getCustomerInsuranceInfoByKey(customerKey);
		int fundingOptionTypeInsurance = 0;
		int fundingOptionTypeGovernment = 0;
		// If not exist
		if (customerInsuranceInfo == null) {
			customerInsuranceInfo = new CustomerInsuranceInfo();
			customerInsuranceInfo.setCustomer(customerKey);
		} else {
			fundingOptionTypeGovernment = fundingOptionMap.get(
					customerInsuranceInfo.getFundingOptionGovernment())
					.getFundingDetailsType();
			fundingOptionTypeInsurance = fundingOptionMap.get(
					customerInsuranceInfo.getFundingOptionInsurance())
					.getFundingDetailsType();

			/** Funding Option Details **/
			/** Insurance **/
			if (fundingOptionTypeInsurance == 1) {
				CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSelfOptionKey());
				if (customerInsuranceInfoType1 == null) {
					customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType1);
			} else if (fundingOptionTypeInsurance == 2) {
				// Spouse Insurance
				CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSpouseOptionKey());
				if (customerInsuranceInfoType1S == null) {
					customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1S
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1S
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType1S);

			} else if (fundingOptionTypeInsurance == 3) {

				CustomerInsuranceInfoWrapper customerInsuranceInfoWrapper = new CustomerInsuranceInfoWrapper();

				CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSelfOptionKey());
				// self
				if (customerInsuranceInfoType1 == null) {
					customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1.setFundingOption(configService
							.getSelfOptionKey());
				}

				// Spouse Insurance
				CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSpouseOptionKey());
				if (customerInsuranceInfoType1S == null) {
					customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1S
							.setCustomer(customerInsuranceInfo.getCustomer());

					customerInsuranceInfoType1S.setFundingOption(configService
							.getSpouseOptionKey());
				}
				customerInsuranceInfoWrapper
						.setSelf(customerInsuranceInfoType1);
				customerInsuranceInfoWrapper
						.setSpouse(customerInsuranceInfoType1S);
				mav.addObject("wrapper", customerInsuranceInfoWrapper);

			}
			if (fundingOptionTypeGovernment > 9) {
				CustomerInsuranceInfoType2 customerInsuranceInfoType2 = customerService
						.getCustomerInsuranceInfoType2ByKey(
								customerInsuranceInfo.getCustomer(),
								customerInsuranceInfo
										.getFundingOptionGovernment());
				if (customerInsuranceInfoType2 == null) {
					customerInsuranceInfoType2 = new CustomerInsuranceInfoType2();
					customerInsuranceInfoType2
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType2
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionGovernment());
				}
				mav.addObject("customerInsuranceInfoDetailsGovernment",
						customerInsuranceInfoType2);
			}
		}
		/** Get customer medical info **/
		CustomerMedicalInfo customerMedicalInfo = customerService
				.getCustomerMedicalInfoByKey(customerKey);
		if (customerMedicalInfo == null) {
			customerMedicalInfo = new CustomerMedicalInfo();
			customerMedicalInfo.setCustomer(customerKey);
		}

		mav.addObject("customerWrapper", customerWrapper);

		mav.addObject("customerInsuranceInfo", customerInsuranceInfo);
		mav.addObject("customerMedicalInfo", customerMedicalInfo);

		/** Options **/
		mav.addObject("customerId", id);
		mav.addObject("cpapDiagnosis", refdataService.getCpapDiagnosisMap());

		setupBasicInfoForMAV(mav,true);
		setupMedicalInfoForMAV(mav);
		setupMachineMaskForMAV(mav);
		setupFundingForMAV(mav);

		mav.addObject("products", productService.getProductMap());

		mav.setViewName("customer-onepager-edit");
		return mav;
	}

	@RequestMapping(value = "/basic-form-save", method = RequestMethod.POST)
	public ModelAndView basicFormSave(
			@ModelAttribute("customerWrapper") @Valid CustomerWrapper customerWrapper,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		CustomerControllerUtil.verifyEmailConsent(customerWrapper, result);
		
		if (result.hasErrors()) {
			/** Go back to the same page if error **/

		} else {
			
			CustomerWrapper cwBefore = customerService.getCustomerWrapperByKey(customerWrapper.getCustomer().getKey());
			
			customerService.saveCustomerWrapper(customerWrapper);
			
			// Upon successful saving
			// Create workpackage
			WorkPackage<Customer, CustomerWrapper> wp = new WorkPackage<Customer, CustomerWrapper>(
					getCurrentUser(), customerWrapper.getCustomer().getKey(),
					cwBefore, customerWrapper);

			RuleEngineUtils.pushWorkPackageIntoQueue( wp);
			
			mav.addObject("message", "Customer Information Saved");
		}

		/** Options **/
		setupBasicInfoForMAV(mav,true);
		/** Command Obj **/
		mav.addObject("customerWrapper", customerWrapper);

		mav.setViewName("customer-basic-form");

		return mav;
	}

	@RequestMapping(value = "/insurance-option-form-save", method = RequestMethod.POST)
	public ModelAndView insuranceOptionFormSave(
			@ModelAttribute("customer") @Valid CustomerInsuranceInfo customerInsuranceInfo,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		Map<Key<FundingOption>, FundingOption> fundingOptionMap = refdataService
				.getFundingOptionMap();

		if (result.hasErrors()) {
			/** Go back to the same page if error **/

		} else {
			customerService.saveCustomerInsuranceInfo(customerInsuranceInfo);
			mav.addObject("messageInsuranceOption",
					"Customer Insurance Option Saved");
		}

		int fundingOptionTypeGovernment = 0, fundingOptionTypeInsurance = 0;

		try {
			fundingOptionTypeGovernment = fundingOptionMap.get(
					customerInsuranceInfo.getFundingOptionGovernment())
					.getFundingDetailsType();
		} catch (Exception ex) {
			log.fine(ex.toString());
		}
		try {
			fundingOptionTypeInsurance = fundingOptionMap.get(
					customerInsuranceInfo.getFundingOptionInsurance())
					.getFundingDetailsType();
		} catch (Exception ex) {
			log.fine(ex.toString());
		}
		log.info("fundingOptionTypeInsurance:" + fundingOptionTypeInsurance);
		/** Funding Option Details **/
		/** Insurance **/
		if (fundingOptionTypeInsurance == 1) {
			CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
					.getCustomerInsuranceInfoType1ByKey(
							customerInsuranceInfo.getCustomer(),
							configService.getSelfOptionKey());
			if (customerInsuranceInfoType1 == null) {
				log.info("creating new info 1");
				customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
				customerInsuranceInfoType1.setCustomer(customerInsuranceInfo
						.getCustomer());
				customerInsuranceInfoType1
						.setFundingOption(customerInsuranceInfo
								.getFundingOptionInsurance());
			}
			mav.addObject("customerInsuranceInfoDetails",
					customerInsuranceInfoType1);
		} else if (fundingOptionTypeInsurance == 2) {
			// Spouse Insurance
			CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
					.getCustomerInsuranceInfoType1ByKey(
							customerInsuranceInfo.getCustomer(),
							configService.getSpouseOptionKey());
			if (customerInsuranceInfoType1S == null) {
				log.info("creating new info 2");
				customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
				customerInsuranceInfoType1S.setCustomer(customerInsuranceInfo
						.getCustomer());
				customerInsuranceInfoType1S
						.setFundingOption(customerInsuranceInfo
								.getFundingOptionInsurance());
			}
			mav.addObject("customerInsuranceInfoDetails",
					customerInsuranceInfoType1S);

		} else if (fundingOptionTypeInsurance == 3) {

			CustomerInsuranceInfoWrapper customerInsuranceInfoWrapper = new CustomerInsuranceInfoWrapper();

			CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
					.getCustomerInsuranceInfoType1ByKey(
							customerInsuranceInfo.getCustomer(),
							configService.getSelfOptionKey());
			if (customerInsuranceInfoType1 == null) {
				log.info("creating new info 3 self");
				customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
				customerInsuranceInfoType1.setCustomer(customerInsuranceInfo
						.getCustomer());
				// Hard code the key
				customerInsuranceInfoType1.setFundingOption(configService
						.getSelfOptionKey());
			}

			// Spouse Insurance
			CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
					.getCustomerInsuranceInfoType1ByKey(
							customerInsuranceInfo.getCustomer(),
							configService.getSpouseOptionKey());
			if (customerInsuranceInfoType1S == null) {
				log.info("creating new info 3 spouse");
				customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
				customerInsuranceInfoType1S.setCustomer(customerInsuranceInfo
						.getCustomer());
				customerInsuranceInfoType1S.setFundingOption(configService
						.getSpouseOptionKey());
			}
			customerInsuranceInfoWrapper.setSelf(customerInsuranceInfoType1);
			customerInsuranceInfoWrapper.setSpouse(customerInsuranceInfoType1S);
			mav.addObject("wrapper", customerInsuranceInfoWrapper);

		}
		// Non none option
		if (fundingOptionTypeGovernment > 9) {
			CustomerInsuranceInfoType2 customerInsuranceInfoType2 = customerService
					.getCustomerInsuranceInfoType2ByKey(
							customerInsuranceInfo.getCustomer(),
							customerInsuranceInfo.getFundingOptionGovernment());
			if (customerInsuranceInfoType2 == null) {
				customerInsuranceInfoType2 = new CustomerInsuranceInfoType2();
				customerInsuranceInfoType2.setCustomer(customerInsuranceInfo
						.getCustomer());
				customerInsuranceInfoType2
						.setFundingOption(customerInsuranceInfo
								.getFundingOptionGovernment());
			}
			mav.addObject("customerInsuranceInfoDetailsGovernment",
					customerInsuranceInfoType2);
		}

		/** Options **/
		setupFundingForMAV(mav);

		/** Command Obj **/
		mav.addObject("customerInsuranceInfo", customerInsuranceInfo);

		mav.setViewName("customer-insurance-form");

		return mav;
	}

	@RequestMapping(value = "/insurance-type1-form-save", method = RequestMethod.POST)
	public ModelAndView insuranceType1FormSave(
			@ModelAttribute("customerInsuranceInfoDetails") @Valid CustomerInsuranceInfoType1 customerInsuranceInfoDetails,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {

		} else {
			customerService
					.saveCustomerInsuranceInfoType1(customerInsuranceInfoDetails);
			mav.addObject("message", "Insurance Information Saved");
		}

		/** Options **/
		mav.addObject("insuranceProviders",
				refdataService.getInsuranceProviderMap());

		/** Command Obj **/
		mav.addObject("customerInsuranceInfoDetails",
				customerInsuranceInfoDetails);

		mav.setViewName("customer-insurance-type1-form");

		return mav;
	}

	@RequestMapping(value = "/insurance-type2-form-save", method = RequestMethod.POST)
	public ModelAndView insuranceType2FormSave(
			@ModelAttribute("customerInsuranceInfoDetails") @Valid CustomerInsuranceInfoType1 customerInsuranceInfoDetails,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {

		} else {
			customerService
					.saveCustomerInsuranceInfoType1(customerInsuranceInfoDetails);
			mav.addObject("message", "Insurance Information Saved");
		}

		/** Options **/
		mav.addObject("insuranceProviders",
				refdataService.getInsuranceProviderMap());

		/** Command Obj **/
		mav.addObject("customerInsuranceInfoDetails",
				customerInsuranceInfoDetails);

		mav.setViewName("customer-insurance-type2-form");

		return mav;
	}

	@RequestMapping(value = "/insurance-type3-form-save", method = RequestMethod.POST)
	public ModelAndView insuranceType3FormSave(
			@ModelAttribute("wrapper") @Valid CustomerInsuranceInfoWrapper wrapper,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {

		} else {
			customerService.saveCustomerInsuranceInfoType1(wrapper.getSelf());
			customerService.saveCustomerInsuranceInfoType1(wrapper.getSpouse());
			mav.addObject("message", "Insurance Information Saved");
		}

		/** Options **/
		mav.addObject("insuranceProviders",
				refdataService.getInsuranceProviderMap());

		/** Command Obj **/
		mav.addObject("wrapper", wrapper);

		mav.setViewName("customer-insurance-type3-form");

		return mav;
	}

	@RequestMapping(value = "/insurance-type4-form-save", method = RequestMethod.POST)
	public ModelAndView insuranceType4FormSave(
			@ModelAttribute("customerInsuranceInfoDetailsGovernment") @Valid CustomerInsuranceInfoType2 customerInsuranceInfoDetailsGovernment,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {

		} else {
			customerService
					.saveCustomerInsuranceInfoType2(customerInsuranceInfoDetailsGovernment);
			mav.addObject("message", "Insurance Information Saved");
		}

		/** Options **/
		mav.addObject("insuranceProviders",
				refdataService.getInsuranceProviderMap());

		/** Command Obj **/
		mav.addObject("customerInsuranceInfoDetailsGovernment",
				customerInsuranceInfoDetailsGovernment);

		mav.setViewName("customer-insurance-type4-form");

		return mav;
	}

	@RequestMapping(value = "/medical-form-save", method = RequestMethod.POST)
	public ModelAndView medicalFormSave(
			@ModelAttribute("customerMedicalInfo") @Valid CustomerMedicalInfo customerMedicalInfo,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		//if sleep doctor is not filled in, that's not nice
		if ( StringUtils.equalsIgnoreCase(CompanyTypeE.CPAPDIRECT.name(), configService.getCompanyMode()) && 
				customerMedicalInfo.getSleepDoctor() == null ) {
			result.addError(new FieldError("customerMedicalInfo", "sleepDoctor", "Sleep Doctor is required."));
		}
		
		if (result.hasErrors()) {
			/** Go back to the same page if error **/

		} else {

			CustomerMedicalInfo customerMedicalInfoBefore = customerService
					.getCustomerMedicalInfoByKey(customerMedicalInfo
							.getCustomer());

			// for backward compatibility? make sure there is a customer info
			// before to avoid
			// errors in workpackage and rule engine.
			if (customerMedicalInfoBefore == null) {
				customerMedicalInfoBefore = new CustomerMedicalInfo();
				customerMedicalInfoBefore.setCustomer(customerMedicalInfo
						.getCustomer());
			}

			try {
				customerService.saveCustomerMedicalInfo(customerMedicalInfo,
						CustomerTabNameE.MEDICAL);
			} catch (IllegalArgumentException e) {

				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {

				throw new RuntimeException(e);
			}
			// Upon successful saving
			// Create workpackage
			WorkPackage<Customer, CustomerMedicalInfo> wp = new WorkPackage<Customer, CustomerMedicalInfo>(
					getCurrentUser(), customerMedicalInfo.getCustomer(),
					customerMedicalInfoBefore, customerMedicalInfo);

			RuleEngineUtils.pushWorkPackageIntoQueue( wp);

			mav.addObject("message", "Customer Medical Information Saved");
		}

		/** Options **/
		mav.addObject("cpapDiagnosis", refdataService.getCpapDiagnosisMap());
		mav.addObject("clinicians", refdataService.getClinicianMap());
		mav.addObject("products", productService.getProductMap());

		setupMedicalInfoForMAV(mav);
		setupMachineMaskForMAV(mav);

		/** Command Obj **/
		mav.addObject("customerMedicalInfo", customerMedicalInfo);

		mav.setViewName("customer-medical-form");

		return mav;
	}

	@RequestMapping(value = "/cpap-form-save", method = RequestMethod.POST)
	public ModelAndView cpapFormSave(
			@ModelAttribute("customerMedicalInfoWrapper") @Valid CustomerMedicalInfoWrapper customerMedicalInfoWrapper,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		if (!result.hasErrors()) {

			try {
				customerService.saveCustomerMedicalInfoWrapper(
						customerMedicalInfoWrapper, CustomerTabNameE.CPAP);
			} catch (IllegalArgumentException e) {

				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {

				throw new RuntimeException(e);
			}
			Key<Customer> customerKey = customerMedicalInfoWrapper
					.getCustomerMedicalInfo().getCustomer();

			// prepare work package.
			CustomerMedicalInfo customerMedicalInfoBefore = customerService
					.getCustomerMedicalInfoByKey(customerKey);

			// for backward compatibility? make sure there is a customer info
			// before to avoid
			// errors in workpackage and rule engine.
			if (customerMedicalInfoBefore == null) {
				customerMedicalInfoBefore = new CustomerMedicalInfo();
				customerMedicalInfoBefore.setCustomer(customerKey);
			}

			CustomerMedicalInfo customerMedicalInfoAfter = customerMedicalInfoWrapper
					.getCustomerMedicalInfo();

			// Upon successful saving
			// Create workpackage
			WorkPackage<Customer, CustomerMedicalInfo> wp = new WorkPackage<Customer, CustomerMedicalInfo>(
					getCurrentUser(), customerMedicalInfoBefore.getCustomer(),
					customerMedicalInfoBefore, customerMedicalInfoAfter);

			RuleEngineUtils.pushWorkPackageIntoQueue( wp);

			mav.addObject("message", "Customer Cpap Trial Information Saved");
		}

		/** Options **/
		mav.addObject("products", productService.getProductMap());
		setupMachineMaskForMAV(mav);

		mav.addObject("customerMedicalInfoWrapper", customerMedicalInfoWrapper);
		mav.setViewName("customer-cpap-form");
		return mav;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_CLINICIAN') or hasPermission(#id, 'customer', 'admin')")
	@RequestMapping(value = "/tabedit", method = RequestMethod.GET)
	public ModelAndView tabEdit(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();
		CustomerWrapper customerWrapper = customerService
				.getCustomerWrapperById(id);
		if (customerWrapper == null) { // Go to error page
		}

		mav.addObject("customerWrapper", customerWrapper);
		setupBasicInfoForMAV(mav,true);
		mav.setViewName("customer-tabedit");

		return mav;
	}

	@RequestMapping(value = "/tabview/form-insurance", method = RequestMethod.GET)
	public ModelAndView tabeditFormInsurance(
			@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, id);

		/** Get customer insurance info **/
		Map<Key<FundingOption>, FundingOption> fundingOptionMap = refdataService
				.getFundingOptionMap();

		CustomerInsuranceInfo customerInsuranceInfo = customerService
				.getCustomerInsuranceInfoByKey(customerKey);
		// If not exist
		if (customerInsuranceInfo == null) {
			customerInsuranceInfo = new CustomerInsuranceInfo();
			customerInsuranceInfo.setCustomer(customerKey);
		} else {
			int fundingOptionTypeGovernment = 0, fundingOptionTypeInsurance = 0;

			try {
				fundingOptionTypeGovernment = fundingOptionMap.get(
						customerInsuranceInfo.getFundingOptionGovernment())
						.getFundingDetailsType();
			} catch (Exception ex) {
				log.fine("not able to get gov insurance details"
						+ ex.toString());
			}
			try {
				fundingOptionTypeInsurance = fundingOptionMap.get(
						customerInsuranceInfo.getFundingOptionInsurance())
						.getFundingDetailsType();
			} catch (Exception ex) {
				log.fine("not able to get personal insurance details"
						+ ex.toString());
			}
			/** Funding Option Details **/
			/** Insurance **/
			/** Self **/
			log.info("fundingOptionTypeGovernment:"
					+ fundingOptionTypeGovernment);
			log.info("fundingOptionTypeInsurance:" + fundingOptionTypeInsurance);
			if (fundingOptionTypeInsurance == 1) {
				CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSelfOptionKey());
				if (customerInsuranceInfoType1 == null) {
					log.info("create new insurance 1");
					customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType1);

				/** Spouse **/
			} else if (fundingOptionTypeInsurance == 2) {

				// Spouse Insurance
				CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSpouseOptionKey());
				if (customerInsuranceInfoType1S == null) {
					log.info("create new insurance type 1 spouse");

					customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1S
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1S
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType1S);

			} else if (fundingOptionTypeInsurance == 3) {

				CustomerInsuranceInfoWrapper customerInsuranceInfoWrapper = new CustomerInsuranceInfoWrapper();

				CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSelfOptionKey());
				// self
				if (customerInsuranceInfoType1 == null) {
					customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1.setFundingOption(configService
							.getSelfOptionKey());
				}

				// Spouse Insurance
				CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSpouseOptionKey());
				if (customerInsuranceInfoType1S == null) {
					customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1S
							.setCustomer(customerInsuranceInfo.getCustomer());

					customerInsuranceInfoType1S.setFundingOption(configService
							.getSpouseOptionKey());
				}
				customerInsuranceInfoWrapper
						.setSelf(customerInsuranceInfoType1);
				customerInsuranceInfoWrapper
						.setSpouse(customerInsuranceInfoType1S);
				mav.addObject("wrapper", customerInsuranceInfoWrapper);

			}
			if (fundingOptionTypeGovernment > 9) {
				CustomerInsuranceInfoType2 customerInsuranceInfoType2 = customerService
						.getCustomerInsuranceInfoType2ByKey(
								customerInsuranceInfo.getCustomer(),
								customerInsuranceInfo
										.getFundingOptionGovernment());
				if (customerInsuranceInfoType2 == null) {
					customerInsuranceInfoType2 = new CustomerInsuranceInfoType2();
					customerInsuranceInfoType2
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType2
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionGovernment());
				}
				mav.addObject("customerInsuranceInfoDetailsGovernment",
						customerInsuranceInfoType2);
			}
		}

		mav.addObject("customerInsuranceInfo", customerInsuranceInfo);

		setupFundingForMAV(mav);
		mav.setViewName("customer-insurance-form");

		return mav;
	}

	@RequestMapping(value = "/tabview/form-medical", method = RequestMethod.GET)
	public ModelAndView tabviewFormMedical(
			@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, id);

		/** Get customer medical info **/
		CustomerMedicalInfo customerMedicalInfo = customerService
				.getCustomerMedicalInfoByKey(customerKey);
		if (customerMedicalInfo == null) {
			customerMedicalInfo = new CustomerMedicalInfo();
			customerMedicalInfo.setCustomer(customerKey);
		}

		mav.addObject("customerMedicalInfo", customerMedicalInfo);

		/** Options **/
		mav.addObject("cpapDiagnosis", refdataService.getCpapDiagnosisMap());
		mav.addObject("clinicians", refdataService.getClinicianMap());

		setupMedicalInfoForMAV(mav);
		setupMachineMaskForMAV(mav);

		mav.addObject("products", productService.getProductMap());

		mav.setViewName("customer-medical-form");
		return mav;
	}

	@RequestMapping(value = "/tabview/form-cpap", method = RequestMethod.GET)
	public ModelAndView tabviewFormCpap(
			@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, customerId);

		/** Get customer medical info **/
		CustomerMedicalInfoWrapper cmiw = customerService
				.getCustomerMedicalInfoWrapper(customerKey);

		mav.addObject("customerMedicalInfoWrapper", cmiw);

		/** Options **/

		setupMachineMaskForMAV(mav);
		mav.addObject("products", productService.getProductMap());

		mav.setViewName("customer-cpap-form");
		return mav;
	}

	@RequestMapping(value = { "/onepagerview", "view" }, method = RequestMethod.GET)
	public ModelAndView onepagerView(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("customerWrapper",
				customerService.getCustomerWrapperById(id));
		mav.addObject("customerMedicalInfo",
				customerService.getCustomerMedicalInfoById(id));
		CustomerInsuranceInfo customerInsuranceInfo = customerService
				.getCustomerInsuranceInfoById(id);
		mav.addObject("customerInsuranceInfo", customerInsuranceInfo);
		Map<Key<FundingOption>, FundingOption> fundingOptionMap = refdataService
				.getFundingOptionMap();

		Key<Customer> customerKey = Key.create(Customer.class, id);

		int fundingOptionTypeInsurance = 0;
		int fundingOptionTypeGovernment = 0;
		// If not exist
		if (customerInsuranceInfo == null) {
			customerInsuranceInfo = new CustomerInsuranceInfo();
			customerInsuranceInfo.setCustomer(customerKey);
		} else {
			fundingOptionTypeGovernment = fundingOptionMap.get(
					customerInsuranceInfo.getFundingOptionGovernment())
					.getFundingDetailsType();
			fundingOptionTypeInsurance = fundingOptionMap.get(
					customerInsuranceInfo.getFundingOptionInsurance())
					.getFundingDetailsType();

			/** Funding Option Details **/
			/** Insurance **/
			if (fundingOptionTypeInsurance == 1) {
				CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSelfOptionKey());
				if (customerInsuranceInfoType1 == null) {
					customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType1);
			} else if (fundingOptionTypeInsurance == 2) {
				// Spouse Insurance
				CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSpouseOptionKey());
				if (customerInsuranceInfoType1S == null) {
					customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1S
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1S
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType1S);

			} else if (fundingOptionTypeInsurance == 3) {

				CustomerInsuranceInfoWrapper customerInsuranceInfoWrapper = new CustomerInsuranceInfoWrapper();

				CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSelfOptionKey());
				if (customerInsuranceInfoType1 == null) {
					customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1
							.setCustomer(customerInsuranceInfo.getCustomer());
					// Hard code the key
					Key<FundingOption> option = configService
							.getSelfOptionKey();
					customerInsuranceInfoType1.setFundingOption(option);
				}

				// Spouse Insurance
				CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSpouseOptionKey());
				if (customerInsuranceInfoType1S == null) {
					customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1S
							.setCustomer(customerInsuranceInfo.getCustomer());
					// Hard code the key
					Key<FundingOption> option = configService
							.getSpouseOptionKey();
					customerInsuranceInfoType1S.setFundingOption(option);
				}
				customerInsuranceInfoWrapper
						.setSelf(customerInsuranceInfoType1);
				customerInsuranceInfoWrapper
						.setSpouse(customerInsuranceInfoType1S);
				mav.addObject("wrapper", customerInsuranceInfoWrapper);

			}
			if (fundingOptionTypeGovernment == 4) {
				CustomerInsuranceInfoType2 customerInsuranceInfoType2 = customerService
						.getCustomerInsuranceInfoType2ByKey(
								customerInsuranceInfo.getCustomer(),
								customerInsuranceInfo
										.getFundingOptionGovernment());
				if (customerInsuranceInfoType2 == null) {
					customerInsuranceInfoType2 = new CustomerInsuranceInfoType2();
					customerInsuranceInfoType2
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType2
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionGovernment());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType2);
			}
		}

		mav.addObject("customerId", id);
		mav.addObject("cpapDiagnosis", refdataService.getCpapDiagnosisMap());

		setupBasicInfoForMAV(mav,false);
		setupMedicalInfoForMAV(mav);
		setupMachineMaskForMAV(mav);

		mav.addObject("fundingOptions", refdataService.getFundingOptionMap());
		mav.addObject("products", productService.getProductMap());
		mav.addObject("insuranceProviders",
				refdataService.getInsuranceProviderMap());

		return mav;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_CLINICIAN') or hasPermission(#id, 'customer', 'view')")
	@RequestMapping(value = { "/tabview", "view" }, method = RequestMethod.GET)
	public ModelAndView tabview(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("customerWrapper",
				customerService.getCustomerWrapperById(id));

		mav.addObject("hasSpecialMedicalNote", customerService
				.customerHasSpecialMedicalNote(Key.create(Customer.class, id)));

		setupBasicInfoForMAV(mav,false);
		mav.setViewName("customer-tabview");
		return mav;
	}

	@RequestMapping(value = "/tabview/view-medical", method = RequestMethod.GET)
	public ModelAndView viewMedical(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, id);

		/** Get customer medical info **/
		CustomerMedicalInfo customerMedicalInfo = customerService
				.getCustomerMedicalInfoByKey(customerKey);
		if (customerMedicalInfo == null) {
			customerMedicalInfo = new CustomerMedicalInfo();
			customerMedicalInfo.setCustomer(customerKey);
		}

		mav.addObject("customerMedicalInfo", customerMedicalInfo);

		/** Options **/
		mav.addObject("cpapDiagnosis", refdataService.getCpapDiagnosisMap());
		mav.addObject("clinicians", refdataService.getClinicianMap());

		setupMedicalInfoForMAV(mav);
		mav.addObject("products", productService.getProductMap());

		mav.addObject("yesNo", CustomerControllerUtil.yesNo());
		mav.setViewName("customer-medical-view");
		return mav;
	}

	@RequestMapping(value = "/tabview/view-cpap", method = RequestMethod.GET)
	public ModelAndView viewCpapInfo(
			@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, customerId);

		/** Get customer medical info **/
		CustomerMedicalInfoWrapper cmiw = customerService
				.getCustomerMedicalInfoWrapper(customerKey);

		setupMachineMaskForMAV(mav);
		mav.addObject("products", productService.getProductMap());
		mav.addObject("customerMedicalInfoWrapper", cmiw);

		mav.setViewName("customer-cpap-view");
		return mav;
	}

	@RequestMapping(value = "/tabview/view-insurance", method = RequestMethod.GET)
	public ModelAndView viewInsurance(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, id);

		/** Get customer insurance info **/
		Map<Key<FundingOption>, FundingOption> fundingOptionMap = refdataService
				.getFundingOptionMap();

		CustomerInsuranceInfo customerInsuranceInfo = customerService
				.getCustomerInsuranceInfoByKey(customerKey);

		int fundingOptionTypeGovernment = 0, fundingOptionTypeInsurance = 0;

		// If not exist
		if (customerInsuranceInfo == null) {
			customerInsuranceInfo = new CustomerInsuranceInfo();
			customerInsuranceInfo.setCustomer(customerKey);
		} else {

			try {
				fundingOptionTypeGovernment = fundingOptionMap.get(
						customerInsuranceInfo.getFundingOptionGovernment())
						.getFundingDetailsType();
			} catch (Exception ex) {
			}
			try {
				fundingOptionTypeInsurance = fundingOptionMap.get(
						customerInsuranceInfo.getFundingOptionInsurance())
						.getFundingDetailsType();
			} catch (Exception ex) {

			}

			/** Funding Option Details **/
			/** Insurance **/
			if (fundingOptionTypeInsurance == 1) {
				CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSelfOptionKey());
				if (customerInsuranceInfoType1 == null) {
					customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType1);
			} else if (fundingOptionTypeInsurance == 2) {
				// Spouse Insurance
				CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSpouseOptionKey());
				if (customerInsuranceInfoType1S == null) {
					customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1S
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType1S
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}
				mav.addObject("customerInsuranceInfoDetails",
						customerInsuranceInfoType1S);

			} else if (fundingOptionTypeInsurance == 3) {

				CustomerInsuranceInfoWrapper customerInsuranceInfoWrapper = new CustomerInsuranceInfoWrapper();

				CustomerInsuranceInfoType1 customerInsuranceInfoType1 = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSelfOptionKey());
				if (customerInsuranceInfoType1 == null) {
					customerInsuranceInfoType1 = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1
							.setCustomer(customerInsuranceInfo.getCustomer());
					// Hard code the key
					Key<FundingOption> option = configService
							.getSelfOptionKey();
					customerInsuranceInfoType1
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionInsurance());
				}

				// Spouse Insurance
				CustomerInsuranceInfoType1 customerInsuranceInfoType1S = customerService
						.getCustomerInsuranceInfoType1ByKey(
								customerInsuranceInfo.getCustomer(),
								configService.getSpouseOptionKey());
				if (customerInsuranceInfoType1S == null) {
					customerInsuranceInfoType1S = new CustomerInsuranceInfoType1();
					customerInsuranceInfoType1S
							.setCustomer(customerInsuranceInfo.getCustomer());
					// Hard code the key
					Key<FundingOption> option = configService
							.getSpouseOptionKey();
					customerInsuranceInfoType1S.setFundingOption(option);
				}
				customerInsuranceInfoWrapper
						.setSelf(customerInsuranceInfoType1);
				customerInsuranceInfoWrapper
						.setSpouse(customerInsuranceInfoType1S);
				mav.addObject("wrapper", customerInsuranceInfoWrapper);

			}
			if (fundingOptionTypeGovernment > 9) {
				CustomerInsuranceInfoType2 customerInsuranceInfoType2 = customerService
						.getCustomerInsuranceInfoType2ByKey(
								customerInsuranceInfo.getCustomer(),
								customerInsuranceInfo
										.getFundingOptionGovernment());
				if (customerInsuranceInfoType2 == null) {
					customerInsuranceInfoType2 = new CustomerInsuranceInfoType2();
					customerInsuranceInfoType2
							.setCustomer(customerInsuranceInfo.getCustomer());
					customerInsuranceInfoType2
							.setFundingOption(customerInsuranceInfo
									.getFundingOptionGovernment());
				}
				mav.addObject("customerInsuranceInfoDetailsGovernment",
						customerInsuranceInfoType2);
			}
		}

		mav.addObject("customerInsuranceInfo", customerInsuranceInfo);

		mav.addObject("insuranceProviders",
				refdataService.getInsuranceProviderMap());
		mav.addObject("fundingOptions", refdataService.getFundingOptionMap());

		mav.setViewName("customer-insurance-view");
		return mav;
	}

	@RequestMapping(value = "/tabview/view-event", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView viewHistory(
			@RequestParam(required = true) Long customerId,
			@RequestParam(required = false) Key<EventSubType> eventSubType,
			@RequestParam(required = false, defaultValue = "7") int lastXDays) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("customerId", customerId);
		mav.addObject("customer", customerService.getCustomerById(customerId));
		List<CustomerEvent> events = customerEventService
				.getCustomerEventByCustomerId(customerId, eventSubType,
						lastXDays);
		mav.addObject("events", events);
		mav.addObject("eventTypeMap", refdataService.getFullEventTypeMap());
		mav.addObject("eventType", refdataService.getEventTypeMap());
		mav.addObject("eventSubTypeMap", refdataService.getEventSubTypeMap());

		EventSearchForm eventSearchForm = new EventSearchForm();
		eventSearchForm.setEventSubType(eventSubType);
		eventSearchForm.setLastXDays(lastXDays);

		mav.addObject("eventSearchForm", eventSearchForm);
		mav.addObject("periodFilter", PeriodFilterUtil.getPeriodFilter());

		mav.setViewName("customer-event-view");

		return mav;
	}

	@RequestMapping(value = "/tabview/view-file", method = { RequestMethod.GET })
	public ModelAndView viewFiles(@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("customerId", customerId);

		CustomerFolder customerFolder = new CustomerFolder(customerId,
				configService);
		mav.addObject("files", customerFolder.getFolderContents());
		mav.addObject("uploadItem", new UploaderForm());

		mav.setViewName("customer-file-view");
		return mav;
	}

	@RequestMapping(value = "/files/view-file", method = { RequestMethod.GET })
	public void streamFile(@RequestParam(required = true) Long customerId,
			@RequestParam(required = true) String requestedFileId,
			HttpServletRequest request, HttpServletResponse response) {

		HttpResponse resp = null;

		CustomerFolder customerFolder = new CustomerFolder(customerId,
				configService);
		try {
			File requestedFile = customerFolder.getFile(requestedFileId);
			resp = customerFolder
					.getDrive()
					.getRequestFactory()
					.buildGetRequest(
							new GenericUrl(requestedFile.getDownloadUrl()))
					.execute();
			InputStream is = resp.getContent();
			response.setContentType(requestedFile.getMimeType());
			response.setHeader("Content-Disposition", "attachment; filename="
					+ requestedFile.getTitle());
			OutputStream os = response.getOutputStream();
			int bytesCopied = IOUtils.copy(is, os);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (resp != null) {
				try {
					resp.disconnect();
				} catch (IOException e) {
				}
			}
		}
	}

	@RequestMapping(value = "/files/delete-file", method = { RequestMethod.GET })
	public ModelAndView deleteFile(
			@RequestParam(required = true) Long customerId,
			@RequestParam(required = true) String requestedFileId) {
		ModelAndView mav = new ModelAndView();

		CustomerFolder customerFolder = new CustomerFolder(customerId,
				configService);
		try {
			customerFolder.deleteFile(requestedFileId);
		} catch (IOException e) {
			log.log(Level.WARNING, "Attempt to delete " + requestedFileId
					+ " failed.", e);
		}

		mav = viewFiles(customerId);
		return mav;
	}

	@RequestMapping(value = "/files/customerFileUpload", method = { RequestMethod.POST })
	public ModelAndView uploaderFormSubmit(
			@RequestParam(required = true) Long customerId,
			UploaderForm uploaderItem) {
		ModelAndView mav = new ModelAndView();

		GMultipartFile uploadFile = uploaderItem.getFileData();
		CustomerFolder customerFolder = new CustomerFolder(customerId,
				configService);

		if (uploadFile != null && uploadFile.getSize() > 0) {
			try {
				String fileId = customerFolder.addOrUpdateFile(uploadFile);
			} catch (IOException e) {
				log.log(Level.WARNING,
						"Attempt to add/update "
								+ uploadFile.getOriginalFilename() + " failed.",
						e);
			}
		} else {
			log.log(Level.WARNING, "uploadFile was null or empty");
		}

		mav = viewFiles(customerId);
		return mav;
	}

	@RequestMapping(value = "/tabview/view-alert", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView viewAlert(
			@RequestParam(required = true) Long customerId,
			@RequestParam(required = false) Key<AlertSubType> alertSubType,
			@RequestParam(required = false, defaultValue = "7") int numDays) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("customerId", customerId);

		List<CustomerAlert> alerts = customerAlertService
				.getCustomerAlertByCustomerId(customerId, alertSubType, numDays);
		mav.addObject("alerts", alerts);
		mav.addObject("alertType", refdataService.getAlertTypeMap());
		mav.addObject("alertSubType", refdataService.getAlertSubTypeMap());
		mav.addObject("alertTypeMap", refdataService
				.getFullAlertTypeMap(AlertCategoryE.CustomerAlert));
		CustomerAlertSearch customerAlertSearch = new CustomerAlertSearch();
		customerAlertSearch.setCustomerId(customerId);
		customerAlertSearch.setAlertSubType(alertSubType);
		customerAlertSearch.setNumDays(numDays);
		mav.addObject("customerAlertSearch", customerAlertSearch);
		mav.addObject("periodFilter", PeriodFilterUtil.getPeriodFilterFuture());

		mav.setViewName("customer-alert-view");
		return mav;
	}

	/** Alert Stuff **/
	@RequestMapping(value = "/alert/form", method = RequestMethod.GET)
	public ModelAndView alertform(@RequestParam(required = false) Long id,
			@RequestParam(required = false) Long customerId) {
		ModelAndView mav = new ModelAndView();

		CustomerAlert alert = null;

		if (id == null || id == 0) {
			alert = new CustomerAlert();
			alert.setCustomer(Key.create(Customer.class, customerId));
			Customer customer = customerService.getCustomerById(customerId);
			// note: can keep this as default, but user can assign to other users
			alert.setClinician(customer.getClinician());
		} else {
			Key<CustomerAlert> key = Key.create(Key.create(Customer.class, customerId), CustomerAlert.class, id);
			alert = customerAlertService.getCustomerAlertByKey(key);
		}

		mav.addObject("alert", alert);
		mav.addObject("alertTypeMap", refdataService
				.getFullAlertTypeMap(AlertCategoryE.CustomerAlert)); 
		mav.addObject("clinicianMap", userService.getCrmUserMap(true));
		mav.setViewName("customer-alert-form");
		return mav;
	}

	@RequestMapping(value = "/alert/formsubmit", method = RequestMethod.POST)
	public ModelAndView alertFormSubmit(
			@ModelAttribute("alert") @Valid CustomerAlert alert,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		/** Manual check **/ 
		if (alert.getAlertDate() == null) {
			result.addError(new FieldError("alert", "alertDate",
					"Alert Date cannot be empty"));
		}
		if (result.hasErrors()) {
			mav.addObject("alert", alert);
			mav.addObject("alertTypeMap", refdataService
					.getFullAlertTypeMap(AlertCategoryE.CustomerAlert));
			mav.setViewName("customer-alert-form");
		} else {
			customerAlertService.saveCustomerAlert(alert);
			mav.setViewName("message");
			mav.addObject("message", "Saved");
		}

		return mav;

	}

	@RequestMapping(value = "/alert/dismiss", method = RequestMethod.GET)
	public ModelAndView alertDismiss(@RequestParam(required = false) Long id,
			@RequestParam(required = false) Long customerId,
			@RequestParam(required = false) Key<AlertSubType> alertSubType,
			@RequestParam(required = false, defaultValue = "7") int numDays) {
		ModelAndView mav = new ModelAndView();

		// CustomerAlert alert = null;
		if (id == null || id == 0 || customerId == null || customerId == 0) {
			mav.addObject("message", "Something is wrong!!");
		} else {
			customerAlertService.deleteCustomerAlertByKey(id, customerId);
		}

		mav = viewAlert(customerId, alertSubType, numDays);
		return mav;
	}

	/**
	 * @RequestMapping(value = "/alert/addnotedismiss", method =
	 *                       RequestMethod.GET) public ModelAndView
	 *                       addNoteAndAlertDismiss(
	 * @RequestParam(required = false) Long id,
	 * @RequestParam(required = false) Long customerId) { ModelAndView mav = new
	 *                        ModelAndView();
	 * 
	 *                        CustomerAlert alert = null; if (id == null || id
	 *                        == 0 || customerId == null || customerId == 0) {
	 *                        mav.addObject("message", "Something is wrong!!");
	 *                        } else {
	 * 
	 *                        customerService.deleteCustomerAlertByKey(id,
	 *                        customerId); }
	 * 
	 *                        mav.addObject("customerId", customerId);
	 *                        mav.addObject("alerts",
	 *                        customerService.getCustomerAlertByCustomerId
	 *                        (customerId)); mav.addObject("alertType",
	 *                        refdataService.getAlertTypeMap());
	 *                        mav.addObject("alertSubType",
	 *                        refdataService.getAlertSubTypeMap());
	 *                        mav.setViewName("customer-alert-view");
	 * 
	 *                        return mav; }
	 **/
	@RequestMapping(value = { "/search/form", "/search/", "/search" }, method = RequestMethod.GET)
	public ModelAndView searchForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("customer", new CustomerSearch());
		mav.setViewName("customer-search");
		return mav;
	}

	@RequestMapping(value = "/search/formsubmit", method = RequestMethod.POST)
	public ModelAndView searchFormSubmit(
			@ModelAttribute("customer") CustomerSearch customer,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		Map<?, ?> customers = customerService.search(customer);
		mav.addObject("clinicians", refdataService.getClinicianMap());
		mav.addObject("customer", customer);
		mav.addObject("customers", customers);
		mav.setViewName("customer-search-result");
		return mav;
	}

	/** Event Stuff **/
	@RequestMapping(value = "/event/form", method = RequestMethod.GET)
	public ModelAndView eventform(@RequestParam(required = false) Long id,
			@RequestParam(required = false) Long customerId) {
		ModelAndView mav = new ModelAndView();

		CustomerEvent event = null;

		if (id == null || id == 0) {
			event = new CustomerEvent();
			event.setCustomer(Key.create(Customer.class, customerId));

		} else {
			Key<CustomerEvent> key = Key.create(Key.create(Customer.class, customerId), CustomerEvent.class, id);
			event = customerEventService.getCustomerEventByKey(key);
		}

		mav.addObject("event", event);
		mav.addObject("eventTypeMap", refdataService.getFullEventTypeMap());
		mav.setViewName("customer-event-form");
		return mav;
	}

	@RequestMapping(value = "/event/formsubmit", method = RequestMethod.POST)
	public ModelAndView eventFormSubmit(
			@ModelAttribute("event") @Valid CustomerEvent event,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();

		/** Manual check **/
		if (event.getDate() == null) {
			result.addError(new FieldError("event", "eventDate",
					"Event Date cannot be empty"));
		}
		if (result.hasErrors()) {
			mav.addObject("event", event);
			mav.addObject("eventTypeMap", refdataService.getFullEventTypeMap());
			mav.setViewName("customer-event-form");
		} else {
			// Add initial
			CrmUser crmUser = ((CrmAuthentication) SecurityContextHolder
					.getContext().getAuthentication()).getCrmUser();
			event.setDetails(crmUser.getInitial() + " - " + event.getDetails());
			customerEventService.saveCustomerEvent(event);

			if (event.getEventSubType().getId() == 610000) {

				Key<Customer> customerKey = event.getCustomer();
				Customer customer = customerService
						.getCustomerByKey(customerKey);

				/** Create customer alert **/
				CustomerAlert alert = new CustomerAlert();
				alert.setClinician(customer.getClinician());
				alert.setCustomer(customerKey);
				alert.setCreatedBy("SYSTEM");
				alert.setMessage("1 Week appointment after customer setup");
				alert.setAlertSubType(Key.create(Key.create(AlertType.class, 700001), AlertSubType.class, 710009));
				Calendar c = Calendar.getInstance();
				c.setTime(event.getDate());
				c.add(Calendar.DATE, 7);
				alert.setAlertDate(c.getTime());

				customerAlertService.saveCustomerAlert(alert);

				CustomerAlert alert2 = new CustomerAlert();
				alert2.setClinician(customer.getClinician());
				alert2.setCustomer(customerKey);
				alert2.setCreatedBy("SYSTEM");
				alert2.setMessage("1 month appointment after customer setup");
				alert2.setAlertSubType(Key.create(Key.create(AlertType.class, 700001),
						AlertSubType.class, 710010));
				c = Calendar.getInstance();
				c.setTime(event.getDate());
				c.add(Calendar.MONTH, 1);
				alert2.setAlertDate(c.getTime());

				customerAlertService.saveCustomerAlert(alert2);

			}

			mav.setViewName("message");
			mav.addObject("message", "Saved");
		}

		return mav;

	}

	@RequestMapping(value = "/event/dismiss", method = RequestMethod.GET)
	public ModelAndView eventDismiss(@RequestParam(required = false) Long id,
			@RequestParam(required = false) Long customerId,
			@RequestParam(required = false) Key<EventSubType> eventSubType,
			@RequestParam(required = false, defaultValue = "7") int lastXDays) {
		ModelAndView mav = new ModelAndView();

		CustomerEvent event = null;
		if (id == null || id == 0 || customerId == null || customerId == 0) {
			mav.addObject("message", "Something is wrong!!");
		} else {
			Key<CustomerEvent> key = Key.create(Key.create(Customer.class, customerId), CustomerEvent.class, id);
			customerEventService.deleteCustomerEventByKey(key);
		}

		mav = viewHistory(customerId, eventSubType, lastXDays);

		return mav;
	}

	@RequestMapping(value = "/event/formForAlertDismiss", method = RequestMethod.GET)
	public ModelAndView eventformForAlertDismiss(
			@RequestParam(required = false) Long alertId,
			@RequestParam(required = false) Long customerId) {

		ModelAndView mav = new ModelAndView();
		CustomerEvent event = new CustomerEvent();
		event = new CustomerEvent();
		event.setCustomer(Key.create(Customer.class, customerId));
		mav.addObject("event", event);
		mav.addObject("alertId", alertId);
		mav.addObject("eventTypeMap", refdataService.getFullEventTypeMap());
		mav.setViewName("customer-event-form-for-dismiss");
		return mav;
	}

	@RequestMapping(value = "/event/formForAlertDismissSubmit", method = RequestMethod.POST)
	public ModelAndView eventformForAlertDismissSubmit(
			@ModelAttribute("event") @Valid CustomerEvent event,
			@ModelAttribute("alertId") Long alertId, BindingResult result) {

		ModelAndView mav = new ModelAndView();
		/** Manual check **/
		if (event.getDate() == null) {
			result.addError(new FieldError("event", "eventDate",
					"Event Date cannot be empty"));
		}
		if (result.hasErrors()) {
			mav.addObject("event", event);
			mav.addObject("alertId", alertId);
			mav.addObject("eventTypeMap", refdataService.getFullEventTypeMap());
			mav.setViewName("customer-event-form-for-dismiss");
		} else {
			CrmUser crmUser = ((CrmAuthentication) SecurityContextHolder
					.getContext().getAuthentication()).getCrmUser();
			event.setDetails(crmUser.getInitial() + " - " + event.getDetails());
			customerEventService.saveCustomerEvent(event);
			log.info("Deleting alert...ID:" + alertId);
			customerAlertService.deleteCustomerAlertByKey(alertId, event
					.getCustomer().getId());
			mav.setViewName("message");
			mav.addObject("message", "Saved");
		}

		return mav;
	}

	// Payment Info Stuff
	@RequestMapping(value = "/tabview/view-payment", method = RequestMethod.GET)
	public ModelAndView viewPayment(
			@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, customerId);

		CustomerPaymentInfo customerPaymentInfo = customerService
				.getCustomerPaymentInfoByKey(customerKey);
		if (customerPaymentInfo == null) {
			customerPaymentInfo = new CustomerPaymentInfo();
		}
		mav.addObject("customerPaymentInfo", customerPaymentInfo);
		mav.addObject("customerId", customerId);
		mav.setViewName("customer-payment-view");
		return mav;
	}

	@RequestMapping(value = "/tabview/tabview-payment", method = RequestMethod.GET)
	public ModelAndView tabviewPayment(
			@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, customerId);

		CustomerPaymentInfo customerPaymentInfo = customerService
				.getCustomerPaymentInfoByKey(customerKey);

		if (customerPaymentInfo == null) {
			customerPaymentInfo = new CustomerPaymentInfo();
		}
		mav.addObject("customerPaymentInfo", customerPaymentInfo);
		mav.addObject("customerId", customerId);
		mav.setViewName("customer-payment-view");

		return mav;
	}

	@RequestMapping(value = "/tabview/form-payment", method = RequestMethod.GET)
	public ModelAndView tabviewFormPayment(
			@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, customerId);

		CustomerPaymentInfo customerPaymentInfo = customerService
				.getCustomerPaymentInfoByKey(customerKey);
		if (customerPaymentInfo == null) {
			customerPaymentInfo = new CustomerPaymentInfo();
			customerPaymentInfo.setCustomer(customerKey);
		}
		mav.addObject("cardType", CustomerPaymentInfo.CardType.class.getEnumConstants());
		mav.addObject("customerPaymentInfo", customerPaymentInfo);
		mav.setViewName("customer-payment-form");
		return mav;
	}

	@RequestMapping(value = "/payment-form-save", method = RequestMethod.POST)
	public ModelAndView paymentFormSave(
			@ModelAttribute("customerPaymentInfo") @Valid CustomerPaymentInfo customerPaymentInfo,
			BindingResult result) {

		ModelAndView mav = new ModelAndView();

		if (result.hasErrors()) {
			/** Go back to the same page if error **/

		} else {
			CustomerPaymentInfo customerPaymentInfoBefore = customerService
					.getCustomerPaymentInfoById(customerPaymentInfo
							.getCustomer().getId());
			customerService.saveCustomerPaymentInfo(customerPaymentInfo);

			if (customerPaymentInfo.getBalance() != null
					&& !customerPaymentInfo.getBalance().trim().equals("")
					&& Double.parseDouble(customerPaymentInfo.getBalance()) > 0) {
				WorkPackage<Customer, CustomerPaymentInfo> wp = new WorkPackage<Customer, CustomerPaymentInfo>(
						getCurrentUser(), customerPaymentInfo.getCustomer(),
						null, customerPaymentInfo);
				wp.setBefore(customerPaymentInfoBefore);
				RuleEngineUtils.pushWorkPackageIntoQueue( wp);

			}
			mav.addObject("message", "Customer Payment Information Saved");
		}
		mav.addObject("cardType", CustomerPaymentInfo.CardType.values());
		mav.setViewName("customer-payment-form");

		return mav;
	}

	/** Label Stuff **/
	@RequestMapping(value = "/label/addressInfo", method = RequestMethod.GET)
	public ModelAndView labelAddrInfo(
			@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		CustomerWrapper cw = customerService.getCustomerWrapperById(customerId);

		mav.addObject("customerWrapper", cw);
		mav.setViewName("customer-label-addressInfo-view");
		return mav;
	}

	@RequestMapping(value = "/label/cpapInfo", method = RequestMethod.GET)
	public ModelAndView labelCpapInfo(
			@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		Key<Customer> customerKey = Key.create(Customer.class, customerId);
		CustomerMedicalInfo cmi = customerService
				.getCustomerMedicalInfoByKey(customerKey);

		mav.addObject("products", productService.getProductMap());
		mav.addObject("customerMedicalInfo", cmi);
		mav.setViewName("customer-label-cpapInfo-view");
		return mav;
	}

	@RequestMapping(value = "/label/patientInfo", method = RequestMethod.GET)
	public ModelAndView labelPatientInfo(
			@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();

		CustomerWrapper cw = customerService.getCustomerWrapperById(customerId);
		Key<Customer> customerKey = cw.getCustomer().getKey();
		CustomerMedicalInfo cmi = customerService
				.getCustomerMedicalInfoByKey(customerKey);

		mav.addObject("customerWrapper", cw);
		mav.addObject("customerMedicalInfo", cmi);
		mav.addObject("clinics", refdataService.getSleepClinicMap());
		mav.addObject("sleepDoctors", refdataService.getSleepDoctorMap());
		mav.setViewName("customer-label-patientInfo-view");
		return mav;
	}
	
	/************************* Email ************************************/
	
	@RequestMapping(value = "/subMail", method = RequestMethod.GET)
	public ModelAndView subscribeToEmails(@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("customer-mailerlite-msg");
		
		CustomerWrapper cw = customerService.getCustomerWrapperById(customerId);
		
		if (validateCustomerForMailerLite(cw, mav,true)) {
			mailerLiteService.addUpdateSubscriber(cw);
			mav.addObject("message", String.format("The customer is subscribed"));
			mav.addObject("mailerLiteDone",true);
		}
		return mav;
	}
	
	@RequestMapping(value = "/unsubMail", method = RequestMethod.GET)
	public ModelAndView unsubscribeToEmails(@RequestParam(required = true) Long customerId) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("customer-mailerlite-msg");
		
		CustomerWrapper cw = customerService.getCustomerWrapperById(customerId);
		cw.getCustomerExtendedInfo().setConsentEmail(false);
		customerService.saveCustomerExtendedInfo(cw.getCustomerExtendedInfo());
		
		if (validateCustomerForMailerLite(cw, mav,false)) {
			mailerLiteService.removeSubscriber(cw);
			mav.addObject("message", String.format("The customer is unsubscribed"));	
			mav.addObject("mailerLiteDone",true);
		}
		return mav;
	}
	
	private boolean validateCustomerForMailerLite(CustomerWrapper cw, ModelAndView mav, boolean checkEmailConsent) {
		
		if (cw == null) {
			
			mav.addObject("message", String.format("The customer is not subscribed!  customer not found"));
			return false;
		} else 	if (cw.getCustomerExtendedInfo() != null && StringUtils.isEmpty(cw.getCustomerExtendedInfo().getEmail())) {
			
			mav.addObject("message", String.format("The customer is not subscribed!  email is empty"));
			return false;
		} else if (checkEmailConsent && !cw.getCustomerExtendedInfo().getConsentEmail()) {
			mav.addObject("message", String.format("The customer is not subscribed!  email consent is required"));
			return false;
		}
		
		return true;
	}
	
	/************************************** pdf ************************************/
	@RequestMapping(value = "/generatePdf", method = {RequestMethod.GET})
	public void streamAdpForm(
			@RequestParam(required = true) String pdfName,
			@RequestParam(required = true)  Long customerId,
			HttpServletRequest request,
			HttpServletResponse response) {		
		try {
			
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=" + pdfName + ".pdf");
			OutputStream os = response.getOutputStream();
			
			PdfMapper pdfMapper = pdfMapperProvider.getMapper(pdfName);
			pdfMapper.createPdf(os, customerId);					

			os.flush();
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}	
	
	

	/************************* setter ************************************/

	
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	private void setupBasicInfoForMAV(ModelAndView mav, boolean activeShopOnly) {

		mav.addObject("clinicians", refdataService.getClinicianMap());
		
		mav.addObject("genders", Customer.Gender.values());
		mav.addObject("contactPreferences",
				refdataService.getContactPreferenceMap());
		mav.addObject("appointmentPreferences",
				refdataService.getAppointmentPreferenceMap());
		mav.addObject("countries", Address.Country.values());
		mav.addObject("provinces", Address.Province.values());
		mav.addObject("languagePreferences",
				CustomerExtendedInfo.LanguagePreferences.values());
		
		
		if(activeShopOnly) {
			mav.addObject("shops", refdataService.getActiveShopMap());
		} else {
			mav.addObject("shops", refdataService.getShopMap());
		}
	}

	private void setupMedicalInfoForMAV(ModelAndView mav) {

		mav.addObject("clinics", refdataService.getSleepClinicMap());
		mav.addObject("familyDoctors", refdataService.getFamilyDoctorMap());
		mav.addObject("sleepDoctors", refdataService.getSleepDoctorMap());
		mav.addObject("dentists", refdataService.getDentistMap());
		mav.addObject("dentalClinics", refdataService.getDentalClinicMap());
	}

	private void setupMachineMaskForMAV(ModelAndView mav) {

		List<ProductSubType> subtypes = refdataService
				.getProductSubTypeByProductId(configService.getMachineTypeId());

		List<Key<ProductSubType>> subtypeKeys = new ArrayList<Key<ProductSubType>>();
		for (ProductSubType type : subtypes) {
			subtypeKeys.add(Key.create(type.getParentType(), ProductSubType.class, type.getId()));
		}
		mav.addObject("machines",
				productService.getProductBySubtypeList(subtypeKeys));

		subtypes = refdataService.getProductSubTypeByProductId(configService
				.getMaskTypeId());

		subtypeKeys = new ArrayList<Key<ProductSubType>>();
		for (ProductSubType type : subtypes) {
			subtypeKeys.add(Key.create(type.getParentType(), ProductSubType.class, type.getId()));
		}

		mav.addObject("masks",
				productService.getProductBySubtypeList(subtypeKeys));
	}

	private void setupFundingForMAV(ModelAndView mav) {

		mav.addObject("fundingOptions", refdataService.getFundingOptionMap());
		mav.addObject("governmentFundingOptions",
				refdataService.getFundingOptionMap("Government"));
		mav.addObject("insuranceFundingOptions",
				refdataService.getFundingOptionMap("Insurance"));
		mav.addObject("insuranceProviders",
				refdataService.getInsuranceProviderMap());
	}
	
	public void setMailerLiteService(CpapMailerLiteService mailerLiteService) {
		this.mailerLiteService = mailerLiteService;
	}
}
