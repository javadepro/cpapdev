package com.esofa.crm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.model.AuditEntry;
import com.esofa.crm.model.AuditEntryTypeE;
import com.esofa.crm.model.AuditSearch;
import com.esofa.crm.service.AuditService;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.service.UserService;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.crm.util.PeriodFilterUtil;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/audit")
public class AuditController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(AuditController.class
			.getName());
	
	@Autowired
	private AuditService auditService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ConfigService configService;
	
	private static List<String> auditEntryTypes;
	
	static {
		
		auditEntryTypes = new ArrayList<String>();
		for(AuditEntryTypeE type : AuditEntryTypeE.values()) {
			
			auditEntryTypes.add(type.name());
		}
	
	}
	
	
	@RequestMapping(value = {  "", "/", "list"}, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listAuditEntries(@ModelAttribute("auditSearch") AuditSearch auditSearch) {
			
		ModelAndView mav = new ModelAndView();
		
		Date date = EsofaUtils.getDateBeforeNDays(auditSearch.getNumDays());
		AuditEntryTypeE type = null;
		
		try {
			type = AuditEntryTypeE.valueOf(auditSearch.getEntryType());
		} catch (IllegalArgumentException iae) {
			//let it be null
			type = null;
		} catch (NullPointerException npe) {
			type = null;
		}
		
		List<AuditEntry> auditEntries = auditService.getAll(type,auditSearch.getUser(), date);
		
		mav.addObject("auditSearch", auditSearch);
		mav.addObject("crmusers", userService.getCrmUserMap(true));
		mav.addObject("periodFilter", PeriodFilterUtil.getPeriodFilter6Mths());
		mav.addObject("auditEntries",auditEntries);
		mav.addObject("auditEntryTypes",auditEntryTypes);
		mav.setViewName("audit-list");
				
		return mav;
	}
	
	
	@RequestMapping(value = {"delete-old","/delete-old"}, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView  deleteOld() {
	
		log.info("delete old audit entries start");
		int dayLimit =configService.getConfigIntByName("AUDIT.KEEP.DAYS");
		
		Date olderThan = EsofaUtils.getDateBeforeNDays(dayLimit);
		auditService.delete(olderThan);
		log.info("delete old audit entries done :" + olderThan);
		
		return listAuditEntries(new AuditSearch());
	}
}
