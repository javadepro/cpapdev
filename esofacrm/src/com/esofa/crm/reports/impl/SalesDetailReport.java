package com.esofa.crm.reports.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.model.report.DateRangeReportReq;
import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.service.SalesService;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;

public class SalesDetailReport extends BaseCrmReport<DateRangeReportReq> {

	private SalesService salesService;
	private ProductService productService;
	
	@Override
	protected List<Map<String,String>> getData(DateRangeReportReq req) {
		Date fromDate = req.getFromDate();
		Date endDate = req.getEndDate();
		
		List<Invoice> rawResults = salesService.getInvoices(fromDate, endDate,null,true);		
		Map<Key<Invoice>,Invoice> invoiceMap = new HashMap<Key<Invoice>,Invoice>(rawResults.size());
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		
		Map<Key<ProductType>,ProductType> pTypeMap = refdataService.getProductTypeMap();
		Map<Key<ProductSubType>,ProductSubType> psubTypeMap = refdataService.getProductSubTypeMap();
		Map<Key<Manufacturer>,Manufacturer> manuMap = refdataService.getManufacturerMap();
		
		for (Invoice i : rawResults) {
			
			Key<Invoice> iKey = Key.create(Invoice.class,i.getId());
			invoiceMap.put(iKey,i);
		}
		
		//dont need you anymore
		rawResults = null;		
		
		//get invoice items based on invoicekeys.
		Key<Invoice>[] invoiceKeys = (Key<Invoice>[]) Array.newInstance(Key.class, 0);
				invoiceKeys = invoiceMap.keySet().toArray(invoiceKeys);	
				
		List<InvoiceItem> rawItems = new ArrayList<>();
		
		if (invoiceKeys !=null && invoiceKeys.length > 0) {
			rawItems =salesService.getInvoiceItems(invoiceKeys );
		}
		

		//populate all the items.
		for (InvoiceItem ii : rawItems) {
		
			Map<String,String> row = new HashMap<String,String>();
			
			Invoice i= invoiceMap.get(ii.getInvoiceKey());
			
			row.put("Invoice Date",EsofaUtils.getDateFormatted(i.getInvoiceDate()));
			row.put("Invoice Internal Id", i.getId().toString());
			row.put("Invoice Number", i.getInvoiceNumber());
			row.put("Invoice Status", i.getStatus());
			row.put("Invoice Type", i.getInvoiceType());
			row.put("Location", i.getShopName());
			row.put("Clinician Name",i.getUserName());
			row.put("Clinician First Name",i.getUserFirstName());
			row.put("Clinician Last Name",i.getUserLastName());
			
			
			
			populate(row,ii);
			populateProductInfo(row,ii,pTypeMap,psubTypeMap,manuMap);
			resultList.add(row);
		}
		
		return resultList;
	}

	private void populateProductInfo(Map<String,String> row, InvoiceItem ii,
			Map<Key<ProductType>,ProductType> pTypeMap,
			Map<Key<ProductSubType>,ProductSubType> psubTypeMap,
			Map<Key<Manufacturer>,Manufacturer> manuMap) {
		
		Product p = productService.getProductMap().get(ii.getProductKey());
		
		if (p == null) { return;}
		
		row.put("Product Barcode", p.getProductBarCode());
		row.put("Product Reference", p.getReferenceNumber());
		
		if (p.getProductSubType() != null) {
				
			ProductSubType pst = psubTypeMap.get(p.getProductSubType());
			
			if (pst != null) {
			
				row.put("Product SubType",pst.getType());
				ProductType pt = pTypeMap.get(pst.getParentType());
				
				if (pt != null) {
					
					row.put("Product Type",pt.getType());
				}
			}
		}
		
		if (p.getManufacturer() != null) {
			Manufacturer manu = manuMap.get(p.getManufacturer());

			if (manu != null) {
				row.put("Manufacturer",manu.getName());
			}
		}
			
	}
	
	
	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}

