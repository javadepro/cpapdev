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

import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/manufacturer")
public class ManufacturerController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(ManufacturerController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("manufacturers",
				refdataService.getManufacturerMap());
		mav.setViewName("refdata-manufacturer-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		Manufacturer manufacturer = null;

		try {
			manufacturer = refdataService.getManufacturerById(id);
			mav.addObject("manufacturer", manufacturer);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}

		mav.setViewName("refdata-manufacturer-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
	
		Manufacturer manufacturer = null;
		if (id == null) {
			manufacturer = new Manufacturer();
		} else {
			try {
				manufacturer = refdataService.getManufacturerById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("manufacturer", manufacturer);
		mav.setViewName("refdata-manufacturer-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("manufacturer") @Valid Manufacturer manufacturer,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			
		}else{
			refdataService.saveManufacturer(manufacturer);
		}

		mav.addObject("manufacturer", manufacturer);

		mav.addObject("message", "Saved");
		mav.setViewName("refdata-manufacturer-form");

		return mav;
	}

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}
