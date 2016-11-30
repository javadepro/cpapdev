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
import com.esofa.crm.refdata.model.FamilyDoctor;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/family-doctor")
public class FamilyDoctorController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(FamilyDoctorController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("familyDoctors", refdataService.getFamilyDoctorMap());
		mav.setViewName("refdata-family-doctor-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		try {

			mav.addObject("familyDoctor",
					refdataService.getFamilyDoctorById(id));
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
			mav.setViewName("message");
		}
		mav.setViewName("refdata-family-doctor-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		FamilyDoctor familyDoctor = null;
		if (id == null) {
			familyDoctor = new FamilyDoctor();
		} else {
			try {
				familyDoctor = refdataService.getFamilyDoctorById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");

			}
		}
		mav.addObject("familyDoctor", familyDoctor);
		mav.addObject("countryList", Address.Country.values());
		mav.addObject("provinceList", Address.Province.values());
		mav.setViewName("refdata-family-doctor-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("familyDoctor") @Valid FamilyDoctor familyDoctor,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
		}else{
			refdataService.saveFamilyDoctor(familyDoctor);
			mav.addObject("message", "Saved");
			
		}
		
		mav.addObject("familyDoctor", familyDoctor);
		mav.addObject("countryList", Address.Country.values());
		mav.addObject("provinceList", Address.Province.values());
		mav.setViewName("refdata-family-doctor-form");
		return mav;
	}

}
