package com.esofa.crm.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.GenericFilterBean;

import com.esofa.crm.service.ConfigService;

public class CompanyModeFilter extends GenericFilterBean {

	private static final Logger logger = Logger
			.getLogger(CompanyModeFilter.class.getName());
	
	private ConfigService configService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		HttpSession session = req.getSession(true);
		String companyMode = configService.getCompanyMode();
		session.setAttribute("companyMode", companyMode);

		chain.doFilter(request, response);
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	
}
