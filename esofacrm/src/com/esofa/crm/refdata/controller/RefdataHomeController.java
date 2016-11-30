package com.esofa.crm.refdata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/refdata")
public class RefdataHomeController {

	@RequestMapping(value = {"","/"}, method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("refdata-landing");
		return mav;
	}
}
