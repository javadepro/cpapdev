package com.esofa.crm.reports.queuejob;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.Invoice.InvoiceAdpStatusTypeE;
import com.esofa.crm.model.pos.Invoice.InvoiceStatusTypeE;
import com.esofa.crm.model.report.ArAgingRE;
import com.esofa.crm.queuejob.AbstractOnlineReportTask;
import com.esofa.crm.service.SalesService;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.MathUtils;

/**
 * 
 * @author JHa
 * @param <T>
 *
 */
public class ArAgingReportTask extends AbstractOnlineReportTask<ArAgingRE> {

	private final String PARAM_PARTIAL="partialDone";
	
	private SalesService salesService;
	private int pageSize=400;
	
	
	@Override
	protected void initTask(Map<String, String> params) {
	
		params.put(PARAM_PARTIAL, Boolean.FALSE.toString());
	}

	/**
	 * snapshot date customer id customer first name customer last name type
	 * (client portion or adp portion) payment amount remaining balance 30 days
	 * 60 days 90 days 120 days 180 days over 180 days
	 */

	@Override
	protected Map<String, String> doTask(Map<String, String> params) {

		String startCursor = params.get(PARAM_CURSOR);
		boolean partialDone = Boolean.valueOf(params.get(PARAM_PARTIAL));
		
		
		
		List<Invoice> invoiceList = new ArrayList<>(pageSize);
		boolean atLeast1 =false;
		String newCursor = StringUtils.EMPTY;
		String queryKey  = StringUtils.EMPTY;;
		String queryValue = StringUtils.EMPTY;;
		
		if (!partialDone) {
			queryKey= "status";
			queryValue= Invoice.InvoiceStatusTypeE.PARTIAL_PAYMENT.toString();
					
		} else {
			queryKey="adpStatus";
			queryValue=Invoice.InvoiceAdpStatusTypeE.PENDING.toString();
		}
		
		newCursor = salesService.getInvoiceForArAging(startCursor, queryKey, queryValue, invoiceList, pageSize);

		for (Invoice i : invoiceList) {

			atLeast1 = true;

			if (!partialDone) {

				ArAgingRE pymtRow = mapPymtRow(i, params);
				if (pymtRow != null) { 
					saveRE(pymtRow);
				}
			} else {

				ArAgingRE adpRow = mapAdpRow(i, params);

				if (adpRow != null) {
					saveRE(adpRow);
				}
			}
		}
		
		if (!atLeast1 || StringUtils.isEmpty(newCursor)) {

			if (!partialDone) {
				
				params.put(PARAM_PARTIAL,Boolean.TRUE.toString());
				newCursor=StringUtils.EMPTY;
			} else {
				
				params.put(PARAM_END,PARAM_END);	
			}
			
		}
		
		params.put(PARAM_CURSOR, newCursor);
		return params;
	}
	

	private ArAgingRE mapPymtRow (Invoice i, Map<String,String> params) {
	

		ArAgingRE row = new ArAgingRE("client");
		
		Date invoiceDate = i.getInvoiceDate();

		if (InvoiceUtils.isInvoiceStatusType(i,InvoiceStatusTypeE.PARTIAL_PAYMENT)) {

			//logic to figure out if we should use clientPortion or Adj client portion for
			//backward compatiability.  if there was reference credit, then adj should be used.
			BigDecimal clientPortionToUse = i.getClientPortionAsBD();
			
			if (!MathUtils.isZero(i.getRefInvoiceCreditAsBD())) {
				
				clientPortionToUse= i.getAdjInvoiceClientPortionAsBD();
			}
			
			setAgingReceivables(row, invoiceDate,
					clientPortionToUse, i.getBalanceRemainingAsBD());

			setCustomerInfo(row, i);
			row.setInvoiceNumber(i.getInvoiceNumber());
			row.setLocation(i.getShopName());
			row.setPreparedByFirstName(i.getUserFirstName());
			row.setInvoiceDate(invoiceDate);
		}
		
		//save only if we actually found info for this customer and for this bucket.
		if (row.getRemainingBalance()!= null && 
				!StringUtils.equalsIgnoreCase(MathUtils.ZERO.toString(),row.getRemainingBalance())) {
			
			setReInfo(row, params);
		} else {
			row = null;
		}
		
		return row;
	}
	private ArAgingRE mapAdpRow (Invoice i , Map<String,String> params) {


		ArAgingRE adpBucket = new ArAgingRE("adp");
		
		Date invoiceDate = i.getInvoiceDate();

		if (InvoiceUtils.isInvoiceAdpStatusType(i,InvoiceAdpStatusTypeE.PENDING)
				|| StringUtils.isEmpty(i.getAdpStatus())) {
			
			setAgingReceivables(adpBucket, invoiceDate,
					i.getAdpPortionAsBD(), i.getAdpBalanceRemainingAsBD());

			setCustomerInfo(adpBucket, i);
			adpBucket.setInvoiceNumber(i.getInvoiceNumber());
			adpBucket.setLocation(i.getShopName());
			adpBucket.setPreparedByFirstName(i.getUserFirstName());
			adpBucket.setInvoiceDate(invoiceDate);

		}
		// need to get client full apyment
		// any any adp outstanding payment!


		if (adpBucket.getRemainingBalance() !=null && 
				!StringUtils.equalsIgnoreCase(MathUtils.ZERO.toString(),adpBucket.getRemainingBalance())) {
			
			setReInfo(adpBucket, params); 
		} else {
			adpBucket=null;
		}
		return adpBucket;
	}


	protected void setAgingReceivables(ArAgingRE entry, Date invoiceDate, BigDecimal originalAmt, BigDecimal remainingBalance  ) {
	
		entry.addAmt(originalAmt);
		entry.addRemainingBalance(remainingBalance);
		
		if (EsofaUtils.isWithinNDays(invoiceDate, 30)) {
			
			entry.addDays30(remainingBalance);
			
		} else if (EsofaUtils.isWithinNDays(invoiceDate, 60)) {
			
			entry.addDays60(remainingBalance);
		} else if (EsofaUtils.isWithinNDays(invoiceDate, 90)) {
			
			entry.addDays90(remainingBalance);
		} else if (EsofaUtils.isWithinNDays(invoiceDate, 180)) {
			
			entry.addDays180(remainingBalance);
		} else {
			
			entry.addDaysOver180(remainingBalance);
		}
			
	}

	protected void setCustomerInfo(ArAgingRE entry,Invoice i ) {
		
		//if customer id does not exist..then no need to set
		//no need to rest the customer id either.
		if (i.getCustomerKey() == null || entry.getCustomerId() != null) { return; }
		
		entry.setCustomerId(i.getCustomerKey().getId());
		entry.setCustomerName(i.getCustomerName());
		entry.setCustomerFirstName(i.getCustomerFirstName());
		entry.setCustomerLastName(i.getCustomerLastName());
	}
		
	@Override
	protected void executeOnComplete(Map<String, String> params) {
	}
	
	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}
	
	
}
