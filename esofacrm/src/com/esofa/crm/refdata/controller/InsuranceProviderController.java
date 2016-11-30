package com.esofa.crm.refdata.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.common.model.Address;
import com.esofa.crm.refdata.model.InsuranceProvider;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/insurance-provider")
public class InsuranceProviderController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(InsuranceProviderController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("insuranceProviders",
				refdataService.getInsuranceProviderMap());
		mav.setViewName("refdata-insurance-provider-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		InsuranceProvider insuranceProvider = null;

		try {
			insuranceProvider = refdataService.getInsuranceProviderById(id);
			mav.addObject("insuranceProvider", insuranceProvider);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}

		mav.addObject("countryList", Address.Country.values());
		mav.addObject("provinceList", Address.Province.values());
		mav.setViewName("refdata-insurance-provider-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
	
		InsuranceProvider insuranceProvider = null;
		if (id == null) {
			insuranceProvider = new InsuranceProvider();
		} else {
			try {
				insuranceProvider = refdataService.getInsuranceProviderById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("insuranceProvider", insuranceProvider);
		mav.addObject("countryList", Address.Country.values());
		mav.addObject("provinceList", Address.Province.values());
		mav.setViewName("refdata-insurance-provider-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("insuranceProvider") @Valid InsuranceProvider insuranceProvider,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			
		}else{
			refdataService.saveInsuranceProvider(insuranceProvider);
		}

		mav.addObject("insuranceProvider", insuranceProvider);
		mav.addObject("countryList", Address.Country.values());
		mav.addObject("provinceList", Address.Province.values());

		mav.addObject("message", "Saved");
		mav.setViewName("refdata-insurance-provider-form");

		return mav;
	}

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}
