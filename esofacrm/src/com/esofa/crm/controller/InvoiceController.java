package com.esofa.crm.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.controller.invoicevalidator.product.InvoiceProductValidator;
import com.esofa.crm.controller.util.InvoiceAdpBulkForm;
import com.esofa.crm.controller.util.InvoiceControllerUtil;
import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.controller.util.InvoicePaymentWrapper;
import com.esofa.crm.controller.util.InvoicePymtForm;
import com.esofa.crm.controller.util.InvoiceSearchForm;
import com.esofa.crm.controller.util.SalesReturnUtils;
import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.model.AuditEntryTypeE;
import com.esofa.crm.model.CompanyTypeE;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerInsuranceInfo;
import com.esofa.crm.model.CustomerInsuranceInfoType2;
import com.esofa.crm.model.CustomerWrapper;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.Invoice.InvoiceAdpStatusTypeE;
import com.esofa.crm.model.pos.Invoice.InvoiceStatusTypeE;
import com.esofa.crm.model.pos.Invoice.InvoiceTypeE;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.model.pos.InvoicePayment;
import com.esofa.crm.model.util.PdfMapper_4793_67E_201404;
import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.model.FundingOption;
import com.esofa.crm.refdata.model.Setting;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.refdata.service.RefdataService.SettingTypes;
import com.esofa.crm.rule.RuleEngineUtils;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.AuditService;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.service.SalesService;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.crm.util.InvoicePaymentUtils;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.MathUtils;
import com.esofa.crm.util.PeriodFilterUtil;
import com.esofa.crm.util.PhoneUtil;
import com.esofa.crm.util.ProductUtils;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;


@Controller
@RequestMapping(value = "/pos")
public class InvoiceController  extends GaeEnhancedController {

	private static final Logger logger = Logger.getLogger(InvoiceController.class.getName());
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private RefdataService refdataService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private SalesService salesService;
	
	@Autowired
	private AuditService auditService;
	
	@Autowired
	private PdfMapper_4793_67E_201404 pdfMapper;

	@Autowired
	private List<InvoiceProductValidator> invoiceProductValidators;
	
	@RequestMapping(value="/invoice-search", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView invoiceSearch(InvoiceSearchForm invoiceSearchForm) {
		
		ModelAndView mav = new ModelAndView();
		List<Invoice> invoiceResults =  new ArrayList<Invoice>();
		
		if (invoiceSearchForm != null) {
			
			if (StringUtils.isNotEmpty(invoiceSearchForm.getInvoiceNumber())) {
				Invoice i = salesService.getInvoice(StringUtils.upperCase(invoiceSearchForm.getInvoiceNumber()));
				
				if (i != null) {
					invoiceResults.add(i);
				}
			} else {

				Date d = EsofaUtils.getDateAdjustedByMonth(invoiceSearchForm.getNumMths());
				Date fromDate = EsofaUtils.getFirstDateOfMonth(d);
				Date toDate = EsofaUtils.getLastDateOfMonth(d);
				
				invoiceResults = salesService.getInvoices(fromDate,toDate,null,false);
			}
		}
		
		mav.addObject("periodFilter",PeriodFilterUtil.getRollingFilter());
		mav.addObject("invoiceResults", invoiceResults);
		mav.setViewName("pos-invoice-search");
		return mav;
	}
	
	@RequestMapping(value="/invoice-adpbulk-form", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView invoiceAdpBulk() {
		
		ModelAndView mav = new ModelAndView();
		
		InvoiceAdpBulkForm invoiceAdpBulkForm = new InvoiceAdpBulkForm();
		prepareInvoiceAdpBulkMAV(mav, invoiceAdpBulkForm);
		invoiceAdpBulkForm.setFromNumMths(PeriodFilterUtil.getFirstMonthOfYear());
		return mav;
	}

	private void prepareInvoiceAdpBulkMAV(ModelAndView mav, InvoiceAdpBulkForm invoiceAdpBulkForm ) {
		
		mav.addObject("invoiceAdpBulkForm",invoiceAdpBulkForm);
		mav.addObject("adpStatusList",InvoiceAdpStatusTypeE.values());
		mav.addObject("periodFilter",PeriodFilterUtil.getRollingFilter());
		mav.setViewName("pos-invoice-adpbulk-form");
	}
	
	@RequestMapping(value="/invoice-adpbulk-formsubmit", method= {RequestMethod.POST})
	public ModelAndView invoiceAdpBulkFormSubmit(@ModelAttribute("invoiceAdpBulkForm")InvoiceAdpBulkForm invoiceAdpBulkForm, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		List<Invoice> invoiceResults =  new ArrayList<Invoice>();
		
		Date fromDate = EsofaUtils.getFirstDateOfMonth(EsofaUtils.getDateAdjustedByMonth(invoiceAdpBulkForm.getFromNumMths()));
		Date toDate = EsofaUtils.getLastDateOfMonth(EsofaUtils.getDateAdjustedByMonth(invoiceAdpBulkForm.getToNumMths()));
		InvoiceAdpStatusTypeE adpStatus = null;
						
		if (!StringUtils.isEmpty(invoiceAdpBulkForm.getAdpStatus())) {
			adpStatus =InvoiceAdpStatusTypeE.valueOf(invoiceAdpBulkForm.getAdpStatus());
		}
		
		if (invoiceAdpBulkForm != null && StringUtils.equalsIgnoreCase(invoiceAdpBulkForm.getActionType(), "search")) {
			
			//reset the selection
			invoiceAdpBulkForm.initInvoiceSelected();
			
		
		} else if (invoiceAdpBulkForm != null && StringUtils.equalsIgnoreCase(invoiceAdpBulkForm.getActionType(), "Apply Full Payment")) {
			
			Date paymentDate = invoiceAdpBulkForm.getProcessedDate();
			if (paymentDate == null) {
				result.addError(new FieldError("invoiceAdpBulkForm", "processedDate", "date must be provided"));
			} else {
				
				//for each invoiceNumber.
				List<String> invoiceNumbers = new ArrayList<String>();
				for ( String i : invoiceAdpBulkForm.getInvoiceSelected()) {
					
					if (StringUtils.isNotEmpty(i)) {
						invoiceNumbers.add(i);
					}								
				}
				List<Invoice> invoices = salesService.getInvoices(invoiceNumbers);
				StringBuilder skippedInvoices= new StringBuilder();
				
				for (Invoice i : invoices) {
					
					if (InvoiceUtils.isInvoiceAdpStatusType(i, InvoiceAdpStatusTypeE.PENDING)
							|| StringUtils.isEmpty(i.getAdpStatus())) {
						
						
						
						InvoicePymtForm invoicePymtForm = new InvoicePymtForm();
						invoicePymtForm.setPaymentAmount(i.getAdpBalanceRemainingAsBD());
						invoicePymtForm.setDescription(InvoicePayment.DESC_ADP_PYMT);
						
						//save actual payment
						InvoicePayment invoicePayment = InvoicePaymentUtils.generateInvoicePayment(invoicePymtForm,
																								   Key.create(Invoice.class,i.getId()), 
																								   i.getAdpBalanceRemainingAsBD(), 
																								   getCurrentUser().getFirstname(),
																									   getCurrentUser().getLastname());
						
							invoicePayment.setPaymentDate(paymentDate);
							salesService.saveInvoicePayment( i, invoicePayment);				
					} else {
						skippedInvoices.append(" " + i.getInvoiceNumber());
					}
				}
				if (skippedInvoices.length() > 0) {
					result.addError(new FieldError("invoiceAdpBulkForm","","the following invoices were not processed: " + skippedInvoices.toString()));
				} else {
					mav.addObject("message", "Saved");
				}
			}
		}
		
		invoiceResults = salesService.getInvoices(fromDate,toDate,adpStatus);
		invoiceAdpBulkForm.initInvoiceSelected(invoiceResults.size());
			
		prepareInvoiceAdpBulkMAV(mav,invoiceAdpBulkForm);		
		mav.addObject("invoiceResults",invoiceResults);
		return mav;
	}
		
	
	
	@RequestMapping(value="/invoice-view", method=RequestMethod.GET)
	public ModelAndView invoiceView(@RequestParam(required = true) String invoiceNumber, @RequestParam(required=false) boolean fwdFromSuccessfulSave) {
		
		ModelAndView mav = new ModelAndView();
		
		//include successfully save msg if internal forward
		if (fwdFromSuccessfulSave) {
			mav.addObject("message", "Saved");
		}
		
		Invoice invoice = salesService.getInvoice(invoiceNumber);
		Key<Invoice> invoiceKey = InvoiceUtils.getInvoiceKey(invoice.getId());
		List<InvoiceItem> invoiceItems = salesService.getInvoiceItems(invoiceKey);
		
		prepareInvoicePaymentMAV(mav,invoice,false);
		
		mav.setViewName("pos-invoice-view");
		mav.addObject(invoice);
		
		if (invoice.getCustomerKey() != null) {
			mav.addObject("customerId",invoice.getCustomerKey().getId());
		}
		
		if (StringUtils.isEmpty(invoice.getMaskWarranty())) {
			invoice.setMaskWarranty(refdataService.getSettingByType(RefdataService.SettingTypes.MaskWarranty, "2 months and up to 2 exchanges").getValue());
		}
		
		mav.addObject("invoiceItems",invoiceItems);
		mav.addObject("paymentMethods",refdataService.getAllSettingsByType(RefdataService.SettingTypes.PaymentMethod));
		mav.addObject("discountReasons", refdataService.getDiscountReasonMap());
		mav.addObject("allReturned", InvoiceControllerUtil.allReturned(invoiceItems));
		return mav;

	}

	@RequestMapping(value = "/generateGovPdf", method = {RequestMethod.GET})
	public void streamAdpForm(
			@RequestParam(required = true) String invoiceNumber,
			HttpServletRequest request,
			HttpServletResponse response) {		
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=" + "AdpForm-"+invoiceNumber + ".pdf");
			OutputStream os = response.getOutputStream();
			
			pdfMapper.createPdf(os, invoiceNumber);					

			os.flush();
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}	
	
	@RequestMapping(value = "/invoice-form", method = RequestMethod.GET)
	public ModelAndView invoiceForm(@RequestParam(required = false) Long customerId,
			@RequestParam(required = false) String invoiceType) {
		
		ModelAndView mav = new ModelAndView();
		String companyMode = configService.getCompanyMode();
		InvoiceForm invoiceForm = new InvoiceForm();	
		Customer c=null;		
		
		//set defaults
		invoiceForm.setCustomerId(customerId);	
		prepareBasicInvoice(invoiceForm.getInvoice(),null,invoiceType);
		invoiceForm.getInvoice().setInvoiceDate(new Date());
		invoiceForm.getInvoice().setUserFirstName(getCurrentUser().getFirstname());
		invoiceForm.getInvoice().setUserLastName(getCurrentUser().getLastname());
		invoiceForm.setQty(1);
		
		if(InvoiceUtils.isSleepMed(companyMode)) {
			invoiceForm.getInvoice().setInvoiceType(Invoice.InvoiceTypeE.STANDARD.name());
			invoiceForm.getInvoice().setCompany(CompanyTypeE.SLEEPMED.name());
		}
		
		c = prepareInvoiceCustomerInfo(invoiceForm,customerId);
		prepareInvoiceShopInfo(invoiceForm);
		prepareInvoiceMAV(mav,invoiceForm, c);
		
		mav.setViewName("pos-invoiceform");
		return mav;
	}
	
	@RequestMapping(value = "/invoice-formsubmit", method = RequestMethod.POST)
	public ModelAndView invoiceFormSubmit(@ModelAttribute("invoiceForm") @Valid InvoiceForm invoiceForm, BindingResult result) {
				
		ModelAndView mav = new ModelAndView();
		
		String actionType = invoiceForm.getActionType();
		Customer c=null;
		Long customerId = invoiceForm.getCustomerId();
		Key<Customer> customerKey = null;
		
		if (customerId !=null) {
			customerKey = Key.create(Customer.class, customerId);
		}
		
		//hardcode since we remove price mode
		invoiceForm.getInvoice().setPriceMode(Invoice.PriceModeTypeE.ADP.name());
		
		//common validation
		InvoiceControllerUtil.validateHeaderInfo(invoiceForm, result);
		InvoiceControllerUtil.setPreparedByUser(invoiceForm,result,refdataService);
		
		
		//actionType = add
		if (!result.hasErrors()) {
			if ("add".equalsIgnoreCase(actionType)) {
				
	
				addProduct(invoiceForm, result);
	
				calculateSubTotal(invoiceForm,result);
				calculateTotals(invoiceForm,result);
				
				if (!result.hasErrors()) {
					InvoiceControllerUtil.resetInputBoxes(invoiceForm);
				}
			} else if (StringUtils.startsWith(actionType, "delete_")) {
				
				int itemToDelete = Integer.valueOf(StringUtils.substringAfter(actionType, "delete_"));
				invoiceForm.getInvoiceItems().remove(itemToDelete);
				InvoiceControllerUtil.calcAndSetMaxAdpAmt(invoiceForm,null,productService,refdataService);
	
				
				//recalculate subtotal;
				calculateSubTotal(invoiceForm,result);
				calculateTotals(invoiceForm,result);
			} else if ("calculateTotals".equalsIgnoreCase(actionType)
					|| "update".equalsIgnoreCase(actionType)
					|| "updateMgrPasscode".equalsIgnoreCase(actionType)) {
				
				if (customerId != null) {
					invoiceForm.getInvoice().setCustomerKey(customerKey);
				}
				
				handleMgrPasscode(invoiceForm,result);
				calculateSubTotal(invoiceForm,result);
				calculateTotals(invoiceForm,result);
			} else if ("save".equalsIgnoreCase(actionType)) {
							
				if (customerId != null) {
					invoiceForm.getInvoice().setCustomerKey(customerKey);
				}
				
				InvoiceControllerUtil.validateCpapInfoBlock(invoiceForm,result);			
				InvoiceControllerUtil.validateTrialInfo(invoiceForm, result, productService,configService);
				InvoiceControllerUtil.passMaskCheck(invoiceForm, result, productService);
				InvoiceControllerUtil.validateItemsforInvoiceType(invoiceForm, result, configService);
				Invoice refInvoice = InvoiceControllerUtil.validateReferencedInvoice(invoiceForm, result, salesService);
				
				//if sales invoice, then reference
				List<InvoiceItem> salesReturnItemsToAdjust = null;				
				if (InvoiceUtils.isInvoiceType(invoiceForm.getInvoice(), InvoiceTypeE.SALES_RETURN)) {
										
					Key<Invoice> refInvoiceKey = Key.create(Invoice.class,refInvoice.getId());
					salesReturnItemsToAdjust =SalesReturnUtils.adjOriginalForSalesReturn(invoiceForm.getInvoiceItems(),
						refInvoiceKey,salesService,productService,result);
				}
	
				
				handleMgrPasscode(invoiceForm,result);
				calculateSubTotal(invoiceForm,result);
				calculateTotals(invoiceForm,result);
				
				if (!result.hasErrors()) {
				
					
					//will only do stuff if type trial or trial_re.
					InvoiceControllerUtil.adjustTrialInvoice(refInvoice, salesService);		
					salesService.saveInvoice(getCurrentUserKey(),invoiceForm.getInvoice(),invoiceForm.getInvoiceItems(),invoiceForm.getLocation());
									
					if (InvoiceUtils.isInvoiceType(invoiceForm.getInvoice(), InvoiceTypeE.SALES_RETURN) ) {
						
						salesService.saveInvoiceItem(salesReturnItemsToAdjust);
					}
					
					//for customer history entry
					if (customerId != null) {
						
						submitToRuleEngine(customerKey,invoiceForm.getInvoice(),null);
					}
					
					return prepareRedirectToView(invoiceForm.getInvoice().getInvoiceNumber());
				}
			}
		}
		//else update
		
		mav.setViewName("pos-invoiceform");		
		c= prepareInvoiceCustomerInfo(invoiceForm,customerId);
		prepareInvoiceShopInfo(invoiceForm);
		prepareInvoiceMAV(mav,invoiceForm,c);	
		
		return mav;
		
	}

	@RequestMapping(value = "/invoice-void-form", method = RequestMethod.GET)
	public ModelAndView invoiceVoid(@RequestParam(required=true) String invoiceNumber) {
		
		ModelAndView mav = new ModelAndView();
		
		Invoice invoice = InvoiceControllerUtil.getInvoiceWithChecks(invoiceNumber, mav, salesService);
		boolean hasError= (invoice == null);
		
		if (hasError) {
			mav.setViewName("pos-invoice-msg");
		}
		mav.addObject(invoice);
		mav.setViewName("pos-invoice-void-form");
		return mav;
	}
	
	@PreAuthorize("hasRole('ROLE_POS_ADMIN')")
	@RequestMapping(value = "/invoice-void-formsubmit", method = RequestMethod.POST)
	public ModelAndView invoiceVoidFormSubmit(@ModelAttribute("invoice") Invoice invoiceFromSubmit, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		
		Invoice invoice = salesService.getInvoice(invoiceFromSubmit.getInvoiceNumber());
		
		if (invoice == null) {
			//error
			result.addError(new ObjectError("invoiceNumber", "Error.  Unable to find invoice"));
		} else if (StringUtils.equalsIgnoreCase(Invoice.InvoiceStatusTypeE.VOID.toString(), invoice.getStatus())) {
			result.addError(new ObjectError("invoiceNumber", "Invoice has already been void."));			
		} else {
			
			//handle invoice
			invoice.setStatus(Invoice.InvoiceStatusTypeE.VOID.toString());
			salesService.updateInvoice(invoice);
			
			//revert any referenced invoices
			Invoice referencedInvoice = salesService.getInvoice(invoice.getReferencedInvoiceNumber());
			
			if (InvoiceUtils.isInvoiceType(referencedInvoice, Invoice.InvoiceTypeE.TRIAL)) {
			
				referencedInvoice.setStatus(InvoiceStatusTypeE.FULL_PAYMENT.name());
				salesService.updateInvoice(referencedInvoice);
			
			}
			
			//if current invoice is a SALES RETURN. then must update invoices referenced.
			if (InvoiceUtils.isInvoiceType(invoice, Invoice.InvoiceTypeE.SALES_RETURN)) {
			
				List<InvoiceItem> currentItems = salesService.getInvoiceItems(Key.create(Invoice.class,invoice.getId()));
				Key<Invoice> referencedInvoiceKey= Key.create(Invoice.class,referencedInvoice.getId());
				SalesReturnUtils.handleVoid(currentItems, referencedInvoiceKey, salesService,productService);
			}
						
			//set note
			String auditNote  = String.format("Invoice  <a href=\"/pos/invoice-view?invoiceNumber=%s\" >%s</a> has been void.",invoice.getInvoiceNumber(),invoice.getInvoiceNumber());			
			auditService.save(getCurrentUserKey(),AuditEntryTypeE.VOID_INVOICE,auditNote,StringUtils.EMPTY);		
			salesService.adjForVoidedInvoice(getCurrentUserKey(),invoice);	
			submitToRuleEngine(invoice.getCustomerKey(),invoice,null);
			
			mav.addObject("message", String.format("The invoice,  %s, has been void.",invoice.getInvoiceNumber()));			
		}
		 
		mav.addObject(invoice);
		mav.setViewName("pos-invoice-msg");
		return mav;
	}
	
	@RequestMapping(value = "/invoice-edit", method = RequestMethod.GET)
	public ModelAndView invoiceEditForm(@RequestParam(required=true) String invoiceNumber) {
		
		
		ModelAndView mav = new ModelAndView();
		Invoice i = salesService.getInvoice(invoiceNumber);
				
		mav.setViewName("pos-edit-form");
		mav.addObject("invoice",i);

		mav.addObject("paymentMethods",refdataService.getAllSettingsByType(RefdataService.SettingTypes.PaymentMethod));
		mav.addObject("machineWarranties",refdataService.getAllSettingsByType(RefdataService.SettingTypes.MachineWarranty));
		mav.addObject("clinicianUsers", refdataService.getClinicianMap(true).values());
		return mav;
	}
	

	@RequestMapping(value = "/invoice-edit-formsubmit", method = RequestMethod.POST)
	public ModelAndView invoiceEditFormSubmit (@ModelAttribute("Invoice") Invoice invoice, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();


		Invoice i = salesService.getInvoice(invoice.getId());
		
		if (i == null) {
			result.addError(new FieldError("error","invoiceNumber","invoice not found."));
		} else if (StringUtils.equalsIgnoreCase(i.getStatus(), Invoice.InvoiceStatusTypeE.VOID.toString())) {
			result.addError(new FieldError("error","invoiceNumber","cannot update a void invoice."));
		}
		
		if (StringUtils.isNotEmpty(invoice.getUserName())) {
			i.setUserFirstName(invoice.getUserFirstName());
			i.setUserLastName(invoice.getUserLastName());
			i.setUserName(StringUtils.EMPTY);
		}		
		i.setMachineWarranty(invoice.getMachineWarranty());
		i.setPaymentMethod(invoice.getPaymentMethod());
		i.setDiscountNote(invoice.getDiscountNote());
		
		salesService.saveInvoice(i);
		mav.addObject("message", "Update successful. Please close this window and hit the blue refresh button at the bottom of the Invoice screen");	
		mav.setViewName("pos-invoice-msg");
		return mav;
	}
	
	
	@RequestMapping(value = "/invoice-trial-refund-form", method = RequestMethod.GET)
	public ModelAndView trialRefundForm(@RequestParam(required = true) String invoiceNumber) {
		
		ModelAndView mav = new ModelAndView();
		Invoice trialInvoice = InvoiceControllerUtil.getInvoiceWithChecks(invoiceNumber, mav, salesService);

		//additional check
		if (!InvoiceUtils.isInvoiceType(trialInvoice, InvoiceTypeE.TRIAL)) {
			
			mav.addObject("warning", "Error.  This is not a trial invoice.");
		}
		
		if (trialInvoice == null) {
			//if null, then there was an error
			mav.setViewName("pos-invoice-msg");
			return mav;
		}

		Long customerId = (trialInvoice.getCustomerKey() == null) ? null: trialInvoice.getCustomerKey().getId();		
		mav = invoiceForm(customerId,null);
		
		InvoiceForm invoiceForm = (InvoiceForm) mav.getModel().get("invoiceForm");
		invoiceForm.getInvoice().setReferencedInvoiceNumber(trialInvoice.getInvoiceNumber());
		
		//set defaults
		invoiceForm.setCustomerId(customerId);	
		prepareBasicInvoice(invoiceForm.getInvoice(),null,Invoice.InvoiceTypeE.TRIAL_REFUND.toString());

		//refund deposit
		Product refundProduct=productService.getProductMap().get(configService.getConfigProductKey("PRODUCT.TRIALREFUND.ID"));
		BigDecimal refundAmt = trialInvoice.getTotalAsBD().negate();
		InvoiceItem refundItem =InvoiceControllerUtil.getInvoiceItemFrom(invoiceForm.getInvoice(),refundProduct,null,1,refundAmt,null);
		refundItem.setBypassInvAdj(true);
		invoiceForm.getInvoiceItems().add(refundItem);		
				
		invoiceForm.getInvoice().setPriceMode(trialInvoice.getPriceMode());
		invoiceForm.setActionType("update");
		return invoiceFormSubmit(invoiceForm, new BeanPropertyBindingResult(invoiceForm, "invoiceForm") );

	}
		
	@RequestMapping(value = "/invoice-trial2purchase-form", method = RequestMethod.GET)
	public ModelAndView trial2Purchase(@RequestParam(required = true) String invoiceNumber) {
		
		ModelAndView mav = new ModelAndView();
		Invoice trialInvoice = InvoiceControllerUtil.getInvoiceWithChecks(invoiceNumber, mav, salesService);
		
		//additional check
		if (!InvoiceUtils.isInvoiceType(trialInvoice, InvoiceTypeE.TRIAL)) {
			
			mav.addObject("warning", "Error.  This is not a trial invoice.");
		}
		
		//bad invoice number
		if (trialInvoice == null) {
			
			mav.setViewName("pos-invoice-msg");
			return mav;
		}				
		
		Long customerId = (trialInvoice.getCustomerKey() == null) ? null: trialInvoice.getCustomerKey().getId();		
		mav = invoiceForm(customerId,null);
		
		
		//in trial to purchase
		//	- add credit
		//  - add products
		// - set referenced InvoiceNumber
		InvoiceForm invoiceForm = (InvoiceForm) mav.getModel().get("invoiceForm");
		invoiceForm.getInvoice().setReferencedInvoiceNumber(trialInvoice.getInvoiceNumber());		

		//set defaults
		invoiceForm.setCustomerId(customerId);	
		prepareBasicInvoice(invoiceForm.getInvoice(),null,Invoice.InvoiceTypeE.STANDARD.toString());
		

		
		//copy products
		List<InvoiceItem> trialItems = salesService.getInvoiceItems(Key.create(Invoice.class,trialInvoice.getId()));
		List<InvoiceItem> copyOfItems = InvoiceControllerUtil.copyAllRegularProducts(trialItems, productService);
		invoiceForm.getInvoiceItems().addAll(copyOfItems);
		
		//credits for C2P
		//only care if the client paid a deposit on the trial invoice.  
		//else there is no carry forward credit.
		if (trialInvoice.getTotalAsBD().signum() >0) {

			Product p = ProductUtils.getProductByBarcode("TRIALDEPOSIT", productService.getProductMap());
			InvoiceItem i = InvoiceControllerUtil.findInvoiceItemFrom(trialItems,p);
			
			if (i != null) {
				invoiceForm.getInvoice().setRefInvoiceCreditAsBD(i.getTotalAsBD());
			}
		}		
		
		invoiceForm.setCtp(true);
		invoiceForm.getInvoice().setPriceMode(trialInvoice.getPriceMode());
		invoiceForm.setActionType("update");
		return invoiceFormSubmit(invoiceForm, new BeanPropertyBindingResult(invoiceForm, "invoiceForm") );
	}
	
	@RequestMapping(value = "/invoice-sales-return", method = RequestMethod.GET)
	public ModelAndView salesReturnForm(@RequestParam(required =true) String invoiceNumber) {
		
		ModelAndView mav = new ModelAndView();
		Invoice originalInvoice = InvoiceControllerUtil.getInvoiceWithChecks(invoiceNumber, mav, salesService);
		Key<Invoice> invoiceKey = InvoiceUtils.getInvoiceKey(originalInvoice.getId());
		List<InvoiceItem> originalItems = salesService.getInvoiceItems(invoiceKey);
		
		if (!(InvoiceUtils.isInvoiceType(originalInvoice, InvoiceTypeE.STANDARD)
				|| InvoiceUtils.isInvoiceType(originalInvoice, InvoiceTypeE.OTHER))) {
			
			mav.addObject("warning", "Sales return only applies for invoices of type STANDARD or OTHER.");
		}
		
		if (InvoiceControllerUtil.allReturned(originalItems)) {
			mav.addObject("warning", "All items from this invoice has been returned.");		
		}
		
		//bad invoice number
		if (originalInvoice == null) {
			
			mav.setViewName("pos-invoice-msg");
			return mav;
		}			
		
		Long customerId = (originalInvoice.getCustomerKey() == null) ? null: originalInvoice.getCustomerKey().getId();		
		mav = invoiceForm(customerId,null);
		InvoiceForm invoiceForm = (InvoiceForm) mav.getModel().get("invoiceForm");
		invoiceForm.getInvoice().setReferencedInvoiceNumber(originalInvoice.getInvoiceNumber());

		invoiceForm.getInvoice().setInvoiceType(InvoiceTypeE.SALES_RETURN.toString());
		invoiceForm.getInvoice().setPriceMode(originalInvoice.getPriceMode());
		invoiceForm.getInvoice().setStatus(Invoice.InvoiceStatusTypeE.FULL_PAYMENT.toString());
		invoiceForm.setActionType("update");
		return invoiceFormSubmit(invoiceForm, new BeanPropertyBindingResult(invoiceForm, "invoiceForm") );

	}
	
	@RequestMapping(value = "/invoice-pymt-formsubmit-v", method = RequestMethod.POST)
	public ModelAndView invoicePaymentFormSubmitV(@ModelAttribute("invoicePymtForm") @Valid InvoicePymtForm invoicePymtForm, BindingResult result) {
		
		
		if (!result.hasErrors()) {
			
			invoicePaymentFormSubmitGeneric(invoicePymtForm, result);
		}
		
		
		if (result.hasErrors()) {
			
			ModelAndView mav = invoiceView(invoicePymtForm.getInvoiceNumber(), false);
			
			mav.addObject(invoicePymtForm);
			return mav;
			
		}
		return prepareRedirectToView(invoicePymtForm.getInvoiceNumber());			
	}
	
	@RequestMapping(value = "/invoice-pymt-formsubmit-p", method = RequestMethod.POST)
	public ModelAndView invoicePaymentFormSubmitP(@ModelAttribute("invoicePymtForm") @Valid InvoicePymtForm invoicePymtForm, BindingResult result) {
		
		if (!result.hasErrors()) {
			
			invoicePaymentFormSubmitGeneric(invoicePymtForm, result);
		}
		
		if (result.hasErrors()) {
			
			ModelAndView mav = invoiceView(invoicePymtForm.getInvoiceNumber(), false);
			mav.addObject(invoicePymtForm);
			return mav;
			
		}
		
		return prepareRedirect("/pos/invoice-pymt",invoicePymtForm.getInvoiceNumber());		
	}	
	
	private void invoicePaymentFormSubmitGeneric(
			InvoicePymtForm invoicePymtForm, BindingResult result) {

		Invoice invoice = salesService.getInvoice(invoicePymtForm
				.getInvoiceNumber());
		Key<Invoice> invoiceKey = Key.create(Invoice.class,invoice.getId());
	
		boolean applyPennyRound = false;
		BigDecimal originalRemainingBal = invoice.getBalanceRemainingAsBD().setScale(MathUtils.NUM_SCALE);
		BigDecimal potentialRemainingBal = invoice.getBalanceRemainingAsBD().setScale(MathUtils.NUM_SCALE);
		BigDecimal paymentAmt = invoicePymtForm.getPaymentAmount().setScale(MathUtils.NUM_SCALE);
	
		//round first if it is cash balance.
		if (StringUtils.equalsIgnoreCase("Cash", invoicePymtForm.getPaymentMethod())) {
			
			//check to see if valid cash amount
			if (!MathUtils.acceptableCashAmt(paymentAmt)) {
			
				result.addError(new FieldError("invoicePymtForm",
						"paymentAmount",
						"cash payments must end in 0 or 5 cents"));
				return;
			}
			
			//if cash payment results in full payment, then we should pay the pennyRounded amount
			//if it is less, then there is no need for penny round.
			if (invoicePymtForm.getPaymentAmount().compareTo(MathUtils.pennyRound(potentialRemainingBal)) == 0) {

				applyPennyRound = true;
				potentialRemainingBal = MathUtils.pennyRound(potentialRemainingBal).setScale(MathUtils.NUM_SCALE);
			}
			
		}
			
		
		// if the payment amount is not valid, then do not save!
		if (paymentAmt == null) {
			
			result.addError(new FieldError("invoicePymtForm",
					"paymentAmount",
					"payment amount must be provided"));
			return;
		} else {

			//check if zero
			if (paymentAmt.equals(BigDecimal.ZERO.setScale(MathUtils.NUM_SCALE))) {
				result.addError(new FieldError("invoicePymtForm",
						"paymentAmount",
						"payment amount cannot be zero"));
				return;
			}
			
			//if payment amount is greater than remaining balance.
			if (paymentAmt.compareTo(potentialRemainingBal) > 0) {

				result.addError(new FieldError("invoicePymtForm",
						"paymentAmount",
						"payment amount is more than remaining balance"));
				return;
			}

			
		}

		//appying penny round, which means it is full payment. let's adjust the penny rounding
		//and then apply the payment amount
		if (applyPennyRound) {
			
			BigDecimal pennyRound = originalRemainingBal.subtract(potentialRemainingBal);
			
			//if the original amount and payment amount end up being zero.. basically there was no penny round.
			if (!pennyRound.equals(BigDecimal.ZERO.setScale(MathUtils.NUM_SCALE))) {
				
				//borrow the form to generate invoice for penny rounding.
				invoicePymtForm.setPaymentAmount(pennyRound);						

				InvoicePayment pennyRoundPayment = InvoicePaymentUtils.generateInvoicePayment(invoicePymtForm,
																							  invoiceKey,
																							  invoice.getBalanceRemainingAsBD(),
																							  getCurrentUser().getFirstname(),
																							  getCurrentUser().getLastname());
				pennyRoundPayment.setDescription(InvoicePayment.DESC_PENNY_ROUND);	
				pennyRoundPayment.setAmtAsBD(pennyRound);
				salesService.saveInvoicePayment( invoice, pennyRoundPayment);		
				
				//after done, make sure payment amt is back into the form
				invoicePymtForm.setPaymentAmount(paymentAmt);
			}
		}
		
		//save actual payment
		InvoicePayment invoicePayment = InvoicePaymentUtils.generateInvoicePayment(invoicePymtForm,
																				   invoiceKey,
																				   invoice.getBalanceRemainingAsBD(),
																				   getCurrentUser().getFirstname(),
																				   getCurrentUser().getLastname());
		
		salesService.saveInvoicePayment( invoice, invoicePayment);
		
		//customer event
		if (invoice.getCustomerKey() != null) {
			
			submitToRuleEngine(invoice.getCustomerKey(), invoice, invoicePayment);
			
		}
	}
	
	@RequestMapping(value = "/invoice-pymt", method = RequestMethod.GET)
	public ModelAndView invoicePaymentForm(@RequestParam(required=true) String invoiceNumber) {
		
		ModelAndView mav = new ModelAndView();
		Invoice invoice = salesService.getInvoice(invoiceNumber);
		Key<Invoice> invoiceKey = Key.create(Invoice.class,invoice.getId());
		List<InvoicePayment> invoicePayments = salesService.getInvoicePayments(invoiceKey);
		
		prepareInvoicePaymentMAV(mav,invoice,false);
		mav.addObject(invoice);
		mav.addObject("invoicePayments", invoicePayments);
		mav.setViewName("pos-invoice-pymt-list");
		return mav;
	}
	
	@RequestMapping(value = "/pymt-edit", method = RequestMethod.GET)
	public ModelAndView paymentEditForm(@RequestParam(required=true) Long id) {
		
		
		ModelAndView mav = new ModelAndView();
		InvoicePayment ip = salesService.getInvoicePayment(Key.create(InvoicePayment.class,id));
		
		mav.addObject("paymentMethods",refdataService.getAllSettingsByType(RefdataService.SettingTypes.PaymentMethod));
		mav.addObject("invoicePayment",ip);
		mav.setViewName("pos-pymt-edit-form");
		return mav;
	}
	

	@RequestMapping(value = "/pymt-formsubmit", method = RequestMethod.POST)
	public ModelAndView paymentEditFormSubmit (@ModelAttribute("invoicePayment") @Valid InvoicePayment invoicePayment, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();

		InvoicePayment ip = salesService.getInvoicePayment(Key.create(InvoicePayment.class,invoicePayment.getId()));
		
		ip.setPaymentMethod(invoicePayment.getPaymentMethod());
		salesService.saveInvoicePayment(ip);
		mav.addObject("message", "Update successful. Please close this window and refresh the Invoice Payment screen");	
		mav.setViewName("pos-pymt-msg");
		return mav;
	}
	

	
	@RequestMapping(value = "/invoice-adp-pymt", method = RequestMethod.GET)
	public ModelAndView invoiceAdpPaymentForm(@RequestParam(required=true) String invoiceNumber) {
		
		ModelAndView mav = new ModelAndView();
		Invoice invoice = salesService.getInvoice(invoiceNumber);
		Key<Invoice> invoiceKey = Key.create(Invoice.class,invoice.getId());
		List<InvoicePayment> invoicePayments = salesService.getInvoiceAdpPayment(invoiceKey);
		
		prepareInvoicePaymentMAV(mav,invoice,true);
		mav.addObject(invoice);
		mav.addObject("invoicePayments", invoicePayments);
		mav.setViewName("pos-invoice-adp-pymt-list");
		return mav;
	}
	
	@RequestMapping(value = "/invoice-adp-pymt-formsubmit", method = RequestMethod.POST)
	public ModelAndView invoiceAdpPaymentFormSubmit(InvoicePymtForm invoicePymtForm, BindingResult result) {

		Key<CrmUser> crmUser = getCurrentUserKey();
		Invoice invoice = salesService.getInvoice(invoicePymtForm.getInvoiceNumber());
	
		BigDecimal originalRemainingBal = invoice.getAdpBalanceRemainingAsBD().setScale(MathUtils.NUM_SCALE);
		BigDecimal potentialRemainingBal = invoice.getAdpBalanceRemainingAsBD().setScale(MathUtils.NUM_SCALE);
		BigDecimal paymentAmt = invoicePymtForm.getPaymentAmount().setScale(MathUtils.NUM_SCALE);
	
		
		// if the payment amount is not valid, then do not save!
		if (paymentAmt == null) {
			
			result.addError(new FieldError("invoicePymtForm","paymentAmount","payment amount must be provided"));
		} else {

			//check if zero
			if (paymentAmt.equals(BigDecimal.ZERO.setScale(MathUtils.NUM_SCALE))) {
				result.addError(new FieldError("invoicePymtForm",
						"paymentAmount",
						"payment amount cannot be zero"));
			}
			
			//if payment amount is greater than remaining balance.
			if (paymentAmt.compareTo(potentialRemainingBal) > 0) {

				result.addError(new FieldError("invoicePymtForm",
						"paymentAmount",
						"payment amount is more than remaining balance"));
			}

		}

		if (result.hasErrors()) {
			
			ModelAndView mav = invoiceAdpPaymentForm(invoice.getInvoiceNumber());
			mav.addObject(invoicePymtForm);
			return mav;			
		}
		
		
		//save actual payment
		InvoicePayment invoicePayment = InvoicePaymentUtils.generateInvoicePayment(invoicePymtForm,
																				   Key.create(Invoice.class,invoice.getId()), 
																				   invoice.getAdpBalanceRemainingAsBD(), 
																				   getCurrentUser().getFirstname(),
																				   getCurrentUser().getLastname());
		
		salesService.saveInvoicePayment( invoice, invoicePayment);				
		return prepareRedirect("/pos/invoice-adp-pymt",invoicePymtForm.getInvoiceNumber());		
	}
	
	
	protected ModelAndView prepareRedirectToView(String invoiceNumber) {
				
		return prepareRedirect("/pos/invoice-view",invoiceNumber);
	}
	
	protected ModelAndView prepareRedirect (String viewUrl, String invoiceNumber) {
		
		
		ModelAndView mav = new ModelAndView("redirect:" +viewUrl);
		mav.addObject("invoiceNumber",invoiceNumber);
		mav.addObject("fwdFromSuccessfulSave",true);
		return mav;
	}
	
	protected Customer prepareInvoiceCustomerInfo(InvoiceForm invoiceForm, Long customerId) {
	
		CustomerWrapper customerWrapper = null;
		Customer customer=null;
		CustomerInsuranceInfo insuranceInfo = null;
		Invoice i = invoiceForm.getInvoice();
		 ContactPreference pref = null;
		
		
		if (customerId != null) {
			customerWrapper = customerService.getCustomerWrapperById(customerId);
			customer = customerWrapper.getCustomer();
			 insuranceInfo =  customerService.getCustomerInsuranceInfoByKey(customer.getKey());
			 CustomerExtendedInfo extInfo = customerWrapper.getCustomerExtendedInfo();
			
					 
			if (extInfo != null && extInfo.getContactPreference() != null) {
				  pref = refdataService.getContactPreferenceById(extInfo.getContactPreference().getId());				
			}
		}	
		

		//dormant code
//		if (InvoiceUtils.isSleepMed(invoiceForm.getInvoice())) {
//			i.setBenefitCode(StringUtils.EMPTY);
//		}
		
		if (customerWrapper == null ) {
			return null;
		}
	
		if (customer != null) {
			i.setCustomerKey(customer.getKey());
			i.setCustomerFirstName(customer.getFirstname());
			i.setCustomerLastName(customer.getLastname());
			i.setHealthCardNumber(customer.getFormattedHealthCardNumber());		
			i.setCustomerAddress(customerWrapper.getCustomerExtendedInfo().getAddress());
			
			if (invoiceForm.getLocation() == null) {
				invoiceForm.setLocation(customer.getPreferredLocation());
			}
			
			//phone number logic
			String phone = PhoneUtil.getCustomerPhone(customer, pref);
			if (StringUtils.isNotEmpty(phone)) {
				i.setCustomerPhone(phone);
			}
		}
		
		
		if (insuranceInfo != null && insuranceInfo.getFundingOptionGovernment() != null) {
			
			CustomerInsuranceInfoType2 ciType2 
				= customerService.getCustomerInsuranceInfoType2ByKey(customer.getKey(), 
					insuranceInfo.getFundingOptionGovernment());

			Map<Key<FundingOption>,FundingOption> foMap =refdataService.getFundingOptionMap();
			i.setBenefitCode(foMap.get(insuranceInfo.getFundingOptionGovernment()).getOption());
			
			if (ciType2 != null) {
				
				i.setMemberId(ciType2.getMemberId());
			}
		}
		
		return customer;
	}
	
	
	protected void prepareInvoiceShopInfo(InvoiceForm invoiceForm) {
		
		if (invoiceForm.getLocation() == null) {
			return;
		}
		
		Shop s = refdataService.getShop(invoiceForm.getLocation());
		
		if (s != null ) {
			Invoice i = invoiceForm.getInvoice();
			
			i.setShopAddress(s.getAddress());
			i.setShopFax(s.getFax());
			i.setShopPhone(s.getPhone());
			i.setShopName(s.getShortName());
			i.setShopKey(invoiceForm.getLocation());
			i.setAdpVendorNumber(s.getAdpVendorNumber());				
			i.setHstNumber(s.getHstNumber());
		}
	}
	
	
	protected void addProduct(InvoiceForm invoiceForm, BindingResult result) {
		
		String barCode = invoiceForm.getBarcode();
		
		if (StringUtils.isEmpty(barCode)) {
			
			result.addError(new FieldError("invoiceForm","productBarcode","barcode is required if you wish to add a product"));
			return;
		}		
		
		Product p = productService.getProductByBarcode(barCode);
		
		//check if product exists
		if (p == null) {

			result.addError(new FieldError("invoiceForm","productBarcode","no product found for this barcode"));
			return;
		}
		
		//check for permission to use this barcode
		Setting productPerm= refdataService.getSettingByType(SettingTypes.BarcodePermissions,p.getProductBarCode());
		if (productPerm != null) {
			
			List<String> rolesRequired= new ArrayList<String>(Arrays.asList(productPerm.getValue().split(",")));;
			if (!currentUserHasRole(rolesRequired) ) {
				
				result.addError(new FieldError("invoiceForm", "product","User not authorized to use this barcode"));
				return;
			}
		}
		
		//assert: there is product
		
		for(InvoiceProductValidator ipv : invoiceProductValidators) {
			
			if (!ipv.validateProduct(invoiceForm, p, result)) {
				return;
			}
		}		
		
		//assert: passed all validators, so valid product to add.
		InvoiceControllerUtil.calcAndSetMaxAdpAmt(invoiceForm,p,productService,refdataService);
		
		if(invoiceForm.getHasATier1() && invoiceForm.getHasATier2() && !MathUtils.isZero(invoiceForm.getMaxAdpAmtBeforeBenefitDiscount())) {
			
			result.addError(new FieldError("invocieForm","specialPricing","Cannot mix Tier1 and Tier 2 pricing when the invoice has an adp portion.  Please separate invoices."));
			return;
		}
		
		//assert: passed all validators, so valid product to add.
		BigDecimal servicePrice = (invoiceForm.getServicePrice() ==  null) ? null : MathUtils.toBD(invoiceForm.getServicePrice());
		InvoiceItem i = InvoiceControllerUtil.getInvoiceItemFrom(invoiceForm.getInvoice(),p, 
				invoiceForm.getSerial(), invoiceForm.getQty(), servicePrice, invoiceForm.getPriceTier());
		
		if (StringUtils.isNotEmpty(invoiceForm.getLotNumber())) {
			i.setLotNumber(invoiceForm.getLotNumber());
		}
		if (StringUtils.isNotEmpty(invoiceForm.getAdditionalSerial())) {
			i.setAdditionalSerial(invoiceForm.getAdditionalSerial());
		}
		
		i.setBypassInvAdj(invoiceForm.getBypassInvAdj());
		invoiceForm.getInvoiceItems().add(i);
	}
	

	protected void calculateSubTotal(InvoiceForm invoiceForm, BindingResult result) {
				
		List<InvoiceItem> items = invoiceForm.getInvoiceItems();
		BigDecimal tmpSubTotal = MathUtils.ZERO;
		
		
		for (InvoiceItem i : items) {
			
			Product p = productService.getProductByKey(i.getProductKey());			
			
			
			//use invoiceItem's price.			
			Float priceToUse = null;
			
			//if nonproduct, then get price from invoiceForm.
			if (InvoiceUtils.isProductType(p, InvoiceUtils.ProductTypeCheckE.isNonProduct)
					|| InvoiceUtils.isInvoiceType(invoiceForm.getInvoice(), Invoice.InvoiceTypeE.SALES_RETURN)) {

				//keep existing value
				priceToUse = i.getPriceAsBD().floatValue();			
				
			} else {
				//if actual product, figure out what the price is again to ensure no changes.			
				priceToUse = Float.valueOf(InvoiceUtils.getPrice(i.getPriceTier(), p).toString());
			}
			
			if (priceToUse == null) { priceToUse = Float.valueOf(0);}
			i.setPriceAsBD(new BigDecimal(priceToUse));
						
			tmpSubTotal = tmpSubTotal.add(i.getTotalAsBD());
			
			if (p.getHstApplicable()) {
				invoiceForm.incrementHstSubTotal(i.getTotalAsBD());
				i.setHstApplicable(p.getHstApplicable());
			}			
			
		}					

		invoiceForm.getInvoice().setSubTotalAsBD(tmpSubTotal);
		//add shipping back to hstSubTotal
		invoiceForm.incrementHstSubTotal(invoiceForm.getInvoice().getShippingTotalAsBD());				
	}
	
	
	protected void calculateTotals(InvoiceForm invoiceForm, BindingResult result) {
		
		Invoice i = invoiceForm.getInvoice();
		BigDecimal hstPercentage = BigDecimal.valueOf(configService.getHst(i.getCompany())).setScale(MathUtils.NUM_SCALE);
		BigDecimal adpPercentage = refdataService.getFundingAdp(invoiceForm.getInvoice().getBenefitCode());
			
		BigDecimal calc_hst = invoiceForm.getHstSubTotal().multiply(hstPercentage).setScale(MathUtils.NUM_SCALE,RoundingMode.HALF_UP);
		i.setHstAsBD(calc_hst);			
		
		//total = subtotal + shipping + hst
		i.setTotalAsBD(i.getSubTotalAsBD().add(i.getShippingTotalAsBD()).
				add(i.getHstAsBD()).setScale(MathUtils.NUM_SCALE,RoundingMode.HALF_UP));		
		
		if (InvoiceUtils.allowAdpPortion(i)) {
			
			//by default the tmpAdpAmt is the total * adp percentage.
			BigDecimal maxAdpAmt = invoiceForm.getMaxAdpAmtBeforeBenefitDiscount().multiply(adpPercentage);						

			i.setAdpPortionAsBD(maxAdpAmt);
			i.setAdpStatus(Invoice.InvoiceAdpStatusTypeE.PENDING.toString());
		} else {
			i.setAdpPortionAsBD(BigDecimal.ZERO);
		}
		
		i.setClientPortionAsBD(i.getTotalAsBD().subtract(i.getAdpPortionAsBD())
				.subtract(i.getPromoCodeAsBD()).setScale(MathUtils.NUM_SCALE));
				
		//if it is cash, then we have to do penny round. else dont need to do this
		if (StringUtils.equalsIgnoreCase("Cash",i.getPaymentMethod())) {
			
			i.setClientPortionAsBD(MathUtils.pennyRound(i.getClientPortionAsBD()));
		}
		
		//if C2P, then need to account for any credit that got carried over
		//if nothing carried over, then refInvoiceCredit is zero.
		//adjustInvoiceClientPortion is needed because we dont want the credit to show on the invoice
		//but it is still calculated and used to facilitate staff usage.
		i.setAdjInvoiceClientPortionAsBD(i.getClientPortionAsBD().subtract(i.getRefInvoiceCreditAsBD()));		
	
		//if the client portion or adjusted client portion is less than 0, then must be full payment as it means it is a refund.
		if ((i.getClientPortionAsBD().signum() <= 0 || i.getAdjInvoiceClientPortionAsBD().signum() <= 0) 				
				&& !InvoiceUtils.isFullPayment(i)) {
			
			result.addError(new FieldError("invoiceForm", "paymentMethod", "if the client or adjusted client portion less than zero, then full payment must be selected "));
			return;
		} 
		
		//adjInvoiceClientPortion = clientPortion - refInvoiceCredit;
		//balanceRemaining = adjInvoiceClientPortion
		
		//partial payment means that there is outstanding balance.
		if (StringUtils.equalsIgnoreCase(Invoice.InvoiceStatusTypeE.PARTIAL_PAYMENT.toString(), i.getStatus())) {
			
			i.setBalanceRemaining(i.getAdjInvoiceClientPortion());
		}
		
		i.setAdpBalanceRemainingAsBD(i.getAdpPortionAsBD());		
		//redem/promo code note check
		InvoiceControllerUtil.validateDiscountNote(invoiceForm, result);		

	}
	
	protected void handleMgrPasscode (InvoiceForm invoiceForm, BindingResult result) {
		
		Invoice invoice = invoiceForm.getInvoice();
		
		if (InvoiceUtils.isInvoiceType(invoice, Invoice.InvoiceTypeE.SALES_RETURN)) {
			
			invoiceForm.setReqMgrPasscode(true);
		} else { return;}

		if (StringUtils.isNotEmpty(invoiceForm.getPosMgrPasscode().getPassCode())) {
			
			if(userService.matchUserPasscode(invoiceForm.getPosMgrPasscode())) {
				
				String passCodeHash = userService.generateUserPasscodeHash(invoice.getCustomerKey(), invoiceForm.getPosMgrPasscode().getCrmUser());
				invoice.setPasscodeHash(passCodeHash);
				return;
				
			} else {
				result.addError(new FieldError("invoiceForm","passCode","manager passcode is not valid"));
				return;
			}
			
			//always validate the passcode hash
		} else if (StringUtils.isNotEmpty(invoice.getPasscodeHash())) {
			
			String actualHash = invoice.getPasscodeHash();
			boolean passcodeMatch = userService.matchUserPasscodeHash(actualHash,invoice.getCustomerKey(), invoiceForm.getPosMgrPasscode().getCrmUser());
			
			if (!passcodeMatch) {
				
				result.addError(new FieldError("invoiceForm","passCode","manager passcode has been tampered with.  please start a new invoice"));
				return;
			}				
		}
		
		if (StringUtils.isEmpty(invoice.getPasscodeHash())) {
			
			result.addError(new FieldError("invoiceForm","passCode","manager passcode is required!"));
			return;
		}
	}
	
	protected ModelAndView prepareInvoiceMAV(ModelAndView mav, InvoiceForm invoiceForm, Customer c) {

		if (c != null) {
			Key<Customer> customerKey = c.getKey();
			CustomerInsuranceInfo cii = customerService.getCustomerInsuranceInfoByKey(customerKey);
			mav.addObject("customerInsuranceInfo", cii);
		}		
		
		mav.addObject("customer", c);
		mav.addObject(invoiceForm);
		
		mav.addObject("discountReasons", refdataService.getDiscountReasonMap());
		mav.addObject("benefitCodes",refdataService.getBenefitCodeList());
		mav.addObject("shops", refdataService.getShopMap());
		mav.addObject("priceMode",Invoice.PriceModeTypeE.values());
		
		if (InvoiceUtils.isSleepMed(invoiceForm.getInvoice())) {
			mav.addObject("priceTierList",InvoiceUtils.getSleepMedPriceTierList());	
		} else {
			mav.addObject("priceTierList",InvoiceItem.ItemPriceTier.values());
		}
		mav.addObject("paymentMethods",refdataService.getAllSettingsByType(RefdataService.SettingTypes.PaymentMethod));
		mav.addObject("machineWarranties",refdataService.getAllSettingsByType(RefdataService.SettingTypes.MachineWarranty));
		mav.addObject("maskWarranties",refdataService.getAllSettingsByType(RefdataService.SettingTypes.MaskWarranty));
		mav.addObject("invoiceTypes",InvoiceUtils.getDefaultInvoiceTypeList());
		mav.addObject("clinicians", refdataService.getClinicianMap());
		mav.addObject("posMgrs",userService.getPosManagers());
		return mav;
	}
	
	protected ModelAndView prepareInvoicePaymentMAV(ModelAndView mav, Invoice invoice,boolean isAdpPayment) {
		
		InvoicePymtForm invoicePymtForm = new InvoicePymtForm();
		invoicePymtForm.setInvoiceNumber(invoice.getInvoiceNumber());
		mav.addObject("invoicePymtForm",invoicePymtForm);
		mav.addObject("cashRemainingBal",MathUtils.pennyRound(invoice.getBalanceRemainingAsBD()).toString());
		mav.addObject("paymentMethods",refdataService.getAllSettingsByType(RefdataService.SettingTypes.PaymentMethod));
		
		SettingTypes pymtDescSettingsKey = null;
		
		if (isAdpPayment) {
			pymtDescSettingsKey = RefdataService.SettingTypes.AdpPaymentDescription;
			
			if (currentUserHasRole("ROLE_POS_ADMIN")) {
				pymtDescSettingsKey = RefdataService.SettingTypes.AdminAdpPaymentDescription;
			}
			
		} else {
			pymtDescSettingsKey = RefdataService.SettingTypes.PaymentDescription;
			
			if (currentUserHasRole("ROLE_POS_ADMIN")) {
				pymtDescSettingsKey = RefdataService.SettingTypes.AdminPaymentDescription;
			}
		}

		mav.addObject("paymentDescriptions",refdataService.getAllSettingsByType(pymtDescSettingsKey));
		
		return mav;
	}

	protected void prepareBasicInvoice(Invoice invoice,  Date d,  String invoiceType) {
		
				
		String finalInvoiceType = StringUtils.EMPTY;
		Date invoiceDate = (d == null) ? new Date() : d;
		
		//if input provided, make sure it is valid invoiceTypeE.
		//otherwise use standard as default
		if (StringUtils.isNotEmpty(invoiceType)) { 
			
			finalInvoiceType = Invoice.InvoiceTypeE.valueOf(invoiceType).toString();
		}
		
		invoice.setInvoiceDate(invoiceDate);
		invoice.setUserFirstName(getCurrentUser().getFirstname());
		invoice.setUserLastName(getCurrentUser().getLastname());
		invoice.setInvoiceType(finalInvoiceType);
		invoice.setCompany(CompanyTypeE.CPAPDIRECT.toString());		
	}
	
	private void submitToRuleEngine(Key<Customer> customerKey, Invoice invoice, InvoicePayment invoicePayment) {
		
		if (customerKey != null) {
			
			InvoicePaymentWrapper ipw = new InvoicePaymentWrapper();
			ipw.setInvoice(invoice);
			ipw.setInvoicePayment(invoicePayment);
			
			WorkPackage<Customer, InvoicePaymentWrapper> wp = new WorkPackage<Customer, InvoicePaymentWrapper>(
					getCurrentUser(), customerKey, null,ipw);

			RuleEngineUtils.pushWorkPackageIntoQueue( wp);
		}
		
	}

	public void setInvoiceProductValidators(
			List<InvoiceProductValidator> invoiceProductValidators) {
		this.invoiceProductValidators = invoiceProductValidators;
	}
}
