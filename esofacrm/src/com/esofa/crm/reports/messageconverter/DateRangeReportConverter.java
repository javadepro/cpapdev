package com.esofa.crm.reports.messageconverter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.esofa.crm.model.report.DateRangeReportReq;
import com.esofa.crm.reports.DateRangeReportFacade;

public class DateRangeReportConverter extends
		AbstractHttpMessageConverter<DateRangeReportReq> {

	public static final MediaType MEDIA_TYPE = new MediaType("text", "csv",
			Charset.forName("utf-8"));
	
	private DateRangeReportFacade dateRangeReportFacade;

	public DateRangeReportConverter() {
		super(MEDIA_TYPE);
	}

	protected boolean supports(Class<?> clazz) {
		return DateRangeReportReq.class.equals(clazz);
	}

	protected void writeInternal(DateRangeReportReq d, HttpOutputMessage output)
			throws IOException, HttpMessageNotWritableException {
		output.getHeaders().setContentType(MEDIA_TYPE);
		output.getHeaders().set("Content-Disposition",
				"attachment; filename=\"" + d.getReportName() + ".csv\"");
		OutputStream out = output.getBody();
		PrintWriter writer = new PrintWriter(out);

		dateRangeReportFacade.generateReport(d, writer);

	}

	@Override
	protected DateRangeReportReq readInternal(
			Class<? extends DateRangeReportReq> arg0, HttpInputMessage arg1)
			throws IOException, HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setDateRangeReportFacade(
			DateRangeReportFacade dateRangeReportFacade) {
		this.dateRangeReportFacade = dateRangeReportFacade;
	}
	
}