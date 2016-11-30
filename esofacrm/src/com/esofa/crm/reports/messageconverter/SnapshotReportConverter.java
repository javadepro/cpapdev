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

import com.esofa.crm.model.report.SnapshotReportReq;
import com.esofa.crm.reports.SnapshotReportFacade;

public class SnapshotReportConverter extends
		AbstractHttpMessageConverter<SnapshotReportReq> {

	public static final MediaType MEDIA_TYPE = new MediaType("text", "csv",
			Charset.forName("utf-8"));

	private SnapshotReportFacade snapshotReportFacade;

	public SnapshotReportConverter() {
		super(MEDIA_TYPE);
	}

	protected boolean supports(Class<?> clazz) {
		return SnapshotReportReq.class.equals(clazz);
	}

	protected void writeInternal(SnapshotReportReq d, HttpOutputMessage output)
			throws IOException, HttpMessageNotWritableException {
		output.getHeaders().setContentType(MEDIA_TYPE);
		output.getHeaders().set("Content-Disposition",
				"attachment; filename=\"" + d.getReportId() + ".csv\"");
		OutputStream out = output.getBody();
		PrintWriter writer = new PrintWriter(out);

		snapshotReportFacade.generateReport(d, writer);
	}

	@Override
	protected SnapshotReportReq readInternal(
			Class<? extends SnapshotReportReq> arg0, HttpInputMessage arg1)
			throws IOException, HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void setSnapshotReportFacade(
			SnapshotReportFacade snapshotReportFacade) {
		this.snapshotReportFacade = snapshotReportFacade;
	}
	
}