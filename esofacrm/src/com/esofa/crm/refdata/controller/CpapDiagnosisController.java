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

import com.esofa.crm.refdata.model.CpapDiagnosis;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/cpap-diagnosis")
public class CpapDiagnosisController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(CpapDiagnosisController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("cpapDiagnosiss",
				refdataService.getCpapDiagnosisMap());
		mav.setViewName("refdata-cpap-diagnosis-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		CpapDiagnosis cpapDiagnosis = null;

		try {
			cpapDiagnosis = refdataService.getCpapDiagnosisById(id);
			mav.addObject("cpapDiagnosis", cpapDiagnosis);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}

		mav.setViewName("refdata-cpap-diagnosis-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
	
		CpapDiagnosis cpapDiagnosis = null;
		if (id == null) {
			cpapDiagnosis = new CpapDiagnosis();
		} else {
			try {
				cpapDiagnosis = refdataService.getCpapDiagnosisById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("cpapDiagnosis", cpapDiagnosis);
		mav.setViewName("refdata-cpap-diagnosis-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("cpapDiagnosis") @Valid CpapDiagnosis cpapDiagnosis,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			
		}else{
			refdataService.saveCpapDiagnosis(cpapDiagnosis);
		}

		mav.addObject("cpapDiagnosis", cpapDiagnosis);

		mav.addObject("message", "Saved");
		mav.setViewName("refdata-cpap-diagnosis-form");

		return mav;
	}

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}
