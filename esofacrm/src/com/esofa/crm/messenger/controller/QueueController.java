package com.esofa.crm.messenger.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.messenger.model.WorkPackage;
import com.esofa.crm.rule.Rule;
import com.esofa.crm.rule.RuleEngine;
import com.esofa.crm.rule.RuleEngineUtils;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.RuleEngineService;
import com.esofa.crm.service.UserService;
import com.esofa.gae.datamigration.DataMigrationTask;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/queue")
public class QueueController extends GaeEnhancedController{

	private static final Logger log = Logger.getLogger(QueueController.class
			.getName());

	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	RuleEngineService ruleEngineService;
	
	@Autowired
	RuleEngine ruleEngine;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/process-work-package", method = RequestMethod.POST)
	public ModelAndView processWorkPackage(@ModelAttribute("workpackage") byte[] workpackage) {
		ModelAndView mav = new ModelAndView();

		log.info("Processing work package... ");
		if(workpackage!=null){
			Object wpObj = RuleEngineUtils.toObject(workpackage);
			if(wpObj.getClass().equals(com.esofa.crm.messenger.model.WorkPackage.class)){
				WorkPackage<Serializable,Serializable> workPackage = (WorkPackage<Serializable, Serializable>)wpObj;
				List<Rule> rules = new ArrayList<Rule>(ruleEngineService.getRuleMap().values());
				ruleEngine.setRules(rules);
				
				if (workPackage.getInitiator() == null && workPackage.getInitiatorKey() != null) {
					CrmUser initiator = userService.getCrmUserById(workPackage.getInitiatorKey().getId());
					workPackage.setInitiator(initiator);
				}
				ruleEngine.processAndExecute(workPackage);
			}
			mav.addObject("message","Qed - OK!!");

		}else{
			mav.addObject("message","NOT OK!!");

		}
		mav.setViewName("message");
		
		return mav;

		
	}
	
	@RequestMapping(value = "/print-message", method = RequestMethod.POST)
	public ModelAndView processCustomerNoteRequest(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView();

		
		log.info("print message... "+message);

		mav.setViewName("message");
		return mav;

		
	}	
	@RequestMapping(value = "/process-message-get", method = RequestMethod.GET)
	public ModelAndView processMessageGet(@RequestParam("initial") String initial) {
		log.info("Processing message... ");
		log.info("Initial is... "+initial);
		ModelAndView mav = new ModelAndView();
		mav.addObject("message","Q - OK!!");
		mav.setViewName("message");
		return mav;

	}

	@RequestMapping(value="/mtask")
	public ModelAndView processDataMigrationRequest(@RequestParam(value="entity", required=false) String entity, 
													@RequestParam(value="nextBatch", required=false) String currentBatch,
													@RequestParam(value="realRun", required=false) String realRun) {
		try {
			DataMigrationTask.execute(entity, currentBatch, realRun);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("message","Started task for " + entity + " @ " + currentBatch);
		mav.setViewName("message");
		return mav;
	}
}
