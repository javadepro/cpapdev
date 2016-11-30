package com.esofa.crm.controller;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.admin.controller.AdminController;
import com.esofa.crm.controller.util.InvoiceSearchForm;
import com.esofa.crm.model.CustomerSearch;
import com.esofa.crm.model.ProductSearch;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.util.PeriodFilterUtil;
import com.esofa.spring.controller.GaeEnhancedController;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.users.UserServiceFactory;

@Controller
@RequestMapping(value = "/")
public class HomeController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(AdminController.class
			.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "home" }, method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();

		/** Get all shops **/
		mav.addObject("shopMap", refdataService.getActiveShopMap());

		/** Customer Search **/
		mav.addObject("customer", new CustomerSearch());

		/** Product Search **/
		mav.addObject("product", new ProductSearch());
		mav.addObject("productTypeMap", refdataService.getFullProductTypeMap());
		
		/** POS Search **/
		mav.addObject("invoiceSearchForm", new InvoiceSearchForm());
		mav.addObject("periodFilter",PeriodFilterUtil.getRollingFilter());

		mav.setViewName("home");
		return mav;

	}
	
	@RequestMapping(value = { "test-queue" }, method = RequestMethod.GET)
	public ModelAndView testQueue() {
		String initial = getCurrentUser().getInitial();
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl("/queue/process-message-get").param("initial",initial).method(Method.GET));
		ModelAndView mav = new ModelAndView();
		mav.addObject("message","Q - OK!!");
		mav.setViewName("message");
		return mav;
	}
	
	@RequestMapping(value={"/bye","bye"},method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		request.getSession(false).invalidate();
		String logoutUrl = UserServiceFactory.getUserService().createLogoutURL("/byebye");
		//String logoutUrl = UserServiceFactory.getUserService().createLogoutURL("http://google.com");
		
		response.sendRedirect(logoutUrl);
	}
	
	
	@RequestMapping(value="/byebye",method = RequestMethod.GET)
	public ModelAndView loggedout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("byebye");
		return mav;
	}
	
	@RequestMapping(value="/access-denied",method = RequestMethod.GET)
	public ModelAndView accessDenied(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("message");
		mav.addObject("message", "Access Denied, Contact Administrator for Access!!");
		return mav;
	}
	/**
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		request.getSession().invalidate();
		String logoutUrl = UserServiceFactory.getUserService().createLogoutURL(
				"/loggedout");
		response.sendRedirect(logoutUrl);
		
	}**/

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}
