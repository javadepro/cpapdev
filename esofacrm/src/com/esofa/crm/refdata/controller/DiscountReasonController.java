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

import com.esofa.crm.refdata.model.DiscountReason;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/discount-reason")
public class DiscountReasonController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(DiscountReasonController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("discountReasons",
				refdataService.getDiscountReasonMap());
		mav.setViewName("refdata-discount-reason-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		DiscountReason discountReason = null;

		try {
			discountReason = refdataService.getDiscountReasonById(id);
			mav.addObject("discountReason", discountReason);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}

		mav.setViewName("refdata-discount-reason-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
	
		DiscountReason discountReason = null;
		if (id == null) {
			discountReason = new DiscountReason();
		} else {
			try {
				discountReason = refdataService.getDiscountReasonById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("discountReason", discountReason);
		mav.setViewName("refdata-discount-reason-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("discountReason") @Valid DiscountReason discountReason,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			
		}else{
			refdataService.saveDiscountReason(discountReason);
		}

		mav.addObject("discountReason", discountReason);

		mav.addObject("message", "Saved");
		mav.setViewName("refdata-discount-reason-form");

		return mav;
	}

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}
