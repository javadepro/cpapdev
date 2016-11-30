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
import com.esofa.crm.refdata.model.Dentist;
import com.esofa.crm.refdata.model.FamilyDoctor;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/dentist")
public class DentistController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(DentistController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("dentists", refdataService.getDentistMap());
		mav.setViewName("refdata-dentist-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		try {

			mav.addObject("dentist",
					refdataService.getDentistById(id));
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
			mav.setViewName("message");
		}
		mav.setViewName("refdata-dentist-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		Dentist dentist = null;
		if (id == null) {
			dentist = new Dentist();
		} else {
			try {
				dentist = refdataService.getDentistById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");

			}
		}
		mav.addObject("dentist", dentist);
		mav.addObject("countryList", Address.Country.values());
		mav.addObject("provinceList", Address.Province.values());
		mav.setViewName("refdata-dentist-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("dentist") @Valid Dentist dentist,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
		}else{
			refdataService.saveDentist(dentist);
			mav.addObject("message", "Saved");
			
		}
		
		mav.addObject("dentist", dentist);
		mav.addObject("countryList", Address.Country.values());
		mav.addObject("provinceList", Address.Province.values());
		mav.setViewName("refdata-dentist-form");
		return mav;
	}

}
