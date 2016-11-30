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

import com.esofa.crm.refdata.model.Clinician;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/clinician")
public class ClinicianController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(ClinicianController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("clinicians", refdataService.getClinicianMap());

		// Refdata
		mav.addObject("shops", refdataService.getActiveShopMap());

		mav.setViewName("refdata-clinician-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		Clinician clinician = null;

		try {
			//clinician = refdataService.getClinicianById(id);
			mav.addObject("clinician", clinician);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}
		mav.addObject("shops", refdataService.getActiveShopMap());

		mav.setViewName("refdata-clinician-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();

		Clinician clinician = null;

		if (id == null) {
			clinician = new Clinician();
		} else {
			try {
				//clinician = refdataService.getClinicianById(id);
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);

			}
		}
		mav.addObject("shops", refdataService.getActiveShopMap());
		mav.addObject("clinician", clinician);
		mav.setViewName("refdata-clinician-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("clinician") @Valid Clinician clinician,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {

		} else {

			mav.addObject("message", "Saved");
			//refdataService.saveClinician(clinician);
		}
		// refdata
		mav.addObject("clinician", clinician);
		mav.addObject("shops", refdataService.getActiveShopMap());

		mav.setViewName("refdata-clinician-form");
		return mav;
	}

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}
