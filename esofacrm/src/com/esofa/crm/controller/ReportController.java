package com.esofa.crm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.controller.util.DataDumpForm;
import com.esofa.crm.drive.DataDumpFolder;
import com.esofa.crm.model.report.DataDumpReportReq;
import com.esofa.crm.model.report.DateRangeReportReq;
import com.esofa.crm.model.report.GeneratedReport;
import com.esofa.crm.model.report.ReferralReport;
import com.esofa.crm.model.report.ReferralSearch;
import com.esofa.crm.model.report.SnapshotReportReq;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.reports.DateRangeReportFacade;
import com.esofa.crm.reports.SnapshotReportFacade;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.crm.util.PeriodFilterUtil;
import com.esofa.spring.controller.GaeEnhancedController;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.model.File;

@Controller
@RequestMapping(value = "/report")
@PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_SUPER','ROLE_REPORTS','ROLE_INVENTORY_ADMIN')")
public class ReportController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(ReportController.class
			.getName());

	@Autowired
	private RefdataService refdataService;
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	@Qualifier(value="dateRangeReportFacade")
	private DateRangeReportFacade reportFacade;
	
	@Autowired
	@Qualifier(value="snapshotReportFacade")
	private SnapshotReportFacade snapshotReportFacade;
	
	@Autowired
	private DataDumpFolder reportFolder;
	
	@RequestMapping(value = {"/referral"}, method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView referralReport(@ModelAttribute("referralSearch") ReferralSearch referralSearch){
		ModelAndView mav = new ModelAndView();
		
		List<ReferralReport> results = new ArrayList<ReferralReport>();
		
		if (referralSearch == null) {
			referralSearch = new ReferralSearch();
		} else {
			
			Date d = EsofaUtils.getDateAdjustedByMonth(referralSearch.getNumMths());
			Date fromDate = EsofaUtils.getFirstDateOfMonth(d);
			Date toDate = EsofaUtils.getLastDateOfMonth(d);
		
			results =customerService.getReferrals(fromDate, toDate, referralSearch.getReferralType());
		}
		
		mav.addObject("periodFilter",PeriodFilterUtil.getRollingFilter());
		mav.addObject("referralSearch", referralSearch);
		mav.addObject("referrals",results);
		
		mav.addObject("sleepDoctors", refdataService.getSleepDoctorMap());
		mav.addObject("familyDoctors", refdataService.getFamilyDoctorMap());
		
		mav.setViewName("report-referral-list");
		return mav;
	}	
	
	@RequestMapping(value = {"/range-select"}, method = RequestMethod.GET)
	public ModelAndView dateRangeReport() {
		
		ModelAndView mav = new ModelAndView();
		
		List<String> reportTypes = reportFacade.getSupportedReportNames();
		mav.addObject("reportForm",new DateRangeReportReq());
		mav.addObject("reportTypes",reportTypes);		
		mav.setViewName("report-date-range-form");
		return mav;
	}
	

	@RequestMapping(value="/range-formsubmit", method=RequestMethod.POST,
			produces = "text/csv;")
	@ResponseBody
	public DateRangeReportReq dateRangeReportSubmit(@ModelAttribute("reportForm") DateRangeReportReq dateRangeReportReq, HttpServletResponse response){
			
		return dateRangeReportReq;
	}


	@RequestMapping(value = {"/snapshot-select"}, method = RequestMethod.GET)
	public ModelAndView snapshotReport() {
		
		ModelAndView mav = new ModelAndView();
		
		List<GeneratedReport> reportsAvailable = snapshotReportFacade.getSnapshotReports();
		mav.addObject("reportForm",new SnapshotReportReq());
		mav.addObject("reportsAvailable",reportsAvailable);		
		
		//data dump reports
		mav.addObject("dataDumpFiles",reportFolder.getFolderContents());
		mav.addObject("dataDumpForm",new DataDumpForm());
		mav.setViewName("report-snapshot-form");
		return mav;
	}
	
	@RequestMapping(value="/snapshot-formsubmit", method=RequestMethod.POST,
			produces = "text/csv;")
	@ResponseBody
	public SnapshotReportReq snapshotReportSubmit(@ModelAttribute("reportForm") SnapshotReportReq snapshotReportReq, HttpServletResponse response){
			
		return snapshotReportReq;
	}
	
	@RequestMapping(value = "/datadumpview-formsubmit", method = { RequestMethod.POST })
	public void streamFile(@ModelAttribute("dataDumpForm") DataDumpForm dataDumpForm,  HttpServletResponse response) {

		HttpResponse resp = null;
		String requestedFileId = dataDumpForm.getRequestedFileId();

		try {
			File requestedFile = reportFolder.getFile(requestedFileId);
			resp = reportFolder
					.getDrive()
					.getRequestFactory()
					.buildGetRequest(
							new GenericUrl(requestedFile.getDownloadUrl()))
					.execute();
			InputStream is = resp.getContent();
			response.setContentType(requestedFile.getMimeType());
			response.setHeader("Content-Disposition", "attachment; filename="
					+ requestedFile.getTitle());
			OutputStream os = response.getOutputStream();
			int bytesCopied = IOUtils.copy(is, os);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
			log.severe(ExceptionUtils.getStackTrace(e));
		} finally {
			if (resp != null) {
				try {
					resp.disconnect();
				} catch (IOException e) {
				}
			}
		}
	}
	
	
	@RequestMapping(value = "/dataDump", method = { RequestMethod.GET })
	public ModelAndView viewDataDumpReportFolder() {
		ModelAndView mav = new ModelAndView();
		
		//mav.addObject("supportedReports",supportedDataDump);
		mav.addObject("files", reportFolder.getFolderContents());
		mav.setViewName("report-datadump-view");
		return mav;
	}
	
	@RequestMapping(value = "/generateDataDump", method = { RequestMethod.GET })
	public ModelAndView generateDataDump(@RequestParam (required=true) String reportId) {
		
		String url = "/qj/execute";
		ModelAndView mav = new ModelAndView("redirect:" +url);
		mav.addObject("setName","dataDumpReport");
		mav.addObject("jobName",reportId);
		return mav;
	}
	
	
	@RequestMapping(value="/inventoryCountReport", method=RequestMethod.GET, produces = "text/csv;")
	@ResponseBody
	public DataDumpReportReq inventoryCountReportSubmit(HttpServletResponse response) {
		DataDumpReportReq dataDumpReportReq = new DataDumpReportReq();
		dataDumpReportReq.setReportName("Inventory Count Report");
		return dataDumpReportReq;
	}	
}
