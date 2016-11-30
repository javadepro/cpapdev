package com.esofa.crm.refdata.controller;

import java.util.Map;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.refdata.model.FundingOption;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/refdata/funding-option")
public class FundingOptionController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(FundingOptionController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("fundingOptions", refdataService.getFundingOptionMap());
		mav.setViewName("refdata-funding-option-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();

		try {
			mav.addObject("fundingOption",
					refdataService.getFundingOptionById(id));
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
			mav.setViewName("message");
		}
		mav.setViewName("refdata-funding-option-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();

		FundingOption fundingOption = null;
		if (id == null) {
			fundingOption = new FundingOption();
		} else {
			try {
				fundingOption = refdataService.getFundingOptionById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("fundingOption", fundingOption);
		mav.setViewName("refdata-funding-option-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("fundingOption") @Valid FundingOption fundingOption,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.addObject("fundingOption", fundingOption);

		} else {
			refdataService.saveFundingOption(fundingOption);
			mav.addObject("message", "Saved");
		}
		mav.setViewName("refdata-funding-option-form");

		return mav;
	}

	private ObjectError uniqueCheck(FundingOption fundingOption) {
		Map<Key<FundingOption>, FundingOption> fundingOptionMap = refdataService
				.getFundingOptionMap();
		for (FundingOption fo : fundingOptionMap.values()) {
			if (fo.getId().equals(fundingOption.getId())) {
				continue;
			} else {
				if (fo.getOption().equalsIgnoreCase(fundingOption.getOption())) {
					return new FieldError("fundingOption", "option",
							"Funding Option name has to be unique");
				}
			}
		}
		return null;
	}

}
