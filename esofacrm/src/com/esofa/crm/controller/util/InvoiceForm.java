package com.esofa.crm.controller.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.model.pos.InvoicePayment;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.UserPasscode;
import com.esofa.crm.util.MathUtils;
import com.googlecode.objectify.Key;

public class InvoiceForm  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4094980601074153870L;

	private String actionType;
	
	private Long customerId;
	
	private Invoice invoice;
	private Key<Shop> location;
	
	//items
	private String barcode;
	private String serial;
	private String lotNumber;
	private Float servicePrice;
	private String additionalSerial;
	private boolean bypassInvAdj;
	
	private boolean ctp;
	private int qty;
	
	private int itemTotal;
	
	private List<InvoiceItem> invoiceItems;
	private List<InvoicePayment> payments;
	
	private BigDecimal hstSubTotal;
	private BigDecimal redemCodeApplicableSubTotal;

	private BigDecimal maxAdpAmtBeforeBenefitDiscount;
	
	private Key<CrmUser> preparedByUserKey;
	
	private boolean reqMgrPasscode;	
	private UserPasscode posMgrPasscode;
	
	private String priceTier;
	
	private boolean hasATier1=false;
	private boolean hasATier2=false;

	
	public InvoiceForm() {
	
		invoice= new Invoice();

		invoiceItems = new ArrayList<InvoiceItem>();
		payments = new ArrayList<InvoicePayment>();
		posMgrPasscode = new UserPasscode();

	}

	
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public void setServicePrice(Float servicePrice) {
		this.servicePrice = servicePrice;
	}
	
	public Float getServicePrice() {
		return servicePrice;
	}
	
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getItemTotal() {
		return itemTotal;
	}

	public void setItemTotal(int itemTotal) {
		this.itemTotal = itemTotal;
	}

	public Key<Shop> getLocation() {
		return location;
	}

	public void setLocation(Key<Shop> location) {
		this.location = location;
	}
	
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public Long getCustomerId() {
		return customerId;
	}

	
	public List<InvoiceItem> getInvoiceItems() {
		return invoiceItems;
	}
	public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}
	public List<InvoicePayment> getPayments() {
		return payments;
	}
	public void setPayments(List<InvoicePayment> payments) {
		this.payments = payments;
	}

	public BigDecimal getHstSubTotal() {
		
		if (hstSubTotal == null) {
			hstSubTotal = BigDecimal.ZERO;
		}
		return hstSubTotal;
	}
	
	public void setHstSubTotal(BigDecimal hstSubTotal) {
		this.hstSubTotal = MathUtils.setBDScale(hstSubTotal);
	}
	
	public void incrementHstSubTotal(BigDecimal delta) {
		
		setHstSubTotal(getHstSubTotal().add(delta));
	}
	
	public BigDecimal getRedemCodeApplicableSubTotal() {
		
		if (redemCodeApplicableSubTotal == null) {
			redemCodeApplicableSubTotal = BigDecimal.ZERO;
		}
		return redemCodeApplicableSubTotal;
	}

	public void setRedemCodeApplicableSubTotal(BigDecimal redemCodeApplicableSubTotal) {
		this.redemCodeApplicableSubTotal = redemCodeApplicableSubTotal;
	}
	
	public void incrementRedemCodeApplicableSubTotal(BigDecimal delta) {
		
		setRedemCodeApplicableSubTotal(getRedemCodeApplicableSubTotal().add(delta));
	}
	
	public void setMaxAdpAmtBeforeBenefitDiscount(
			BigDecimal maxAdpAmtBeforeBenefitDiscount) {
		this.maxAdpAmtBeforeBenefitDiscount = maxAdpAmtBeforeBenefitDiscount;
	}
	
	public BigDecimal getMaxAdpAmtBeforeBenefitDiscount() {
		
		if (maxAdpAmtBeforeBenefitDiscount == null) {
			maxAdpAmtBeforeBenefitDiscount = BigDecimal.ZERO;
		}
		return maxAdpAmtBeforeBenefitDiscount;
	}
	
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	
	public String getLotNumber() {
		return lotNumber;
	}

	public String getAdditionalSerial() {
		return additionalSerial;
	}
	
	public void setAdditionalSerial(String additionalSerial) {
		this.additionalSerial = additionalSerial;
	}
	
	public void setPreparedByUserKey(Key<CrmUser> preparedByUserKey) {
		this.preparedByUserKey = preparedByUserKey;
	}
	public Key<CrmUser> getPreparedByUserKey() {
		return preparedByUserKey;
	}
	
	public UserPasscode getPosMgrPasscode() {
		return posMgrPasscode;
	}
	public void setPosMgrPasscode(UserPasscode posMgrPasscode) {
		this.posMgrPasscode = posMgrPasscode;
	}

	public boolean getReqMgrPasscode() {
		return reqMgrPasscode;
	}

	public void setReqMgrPasscode(boolean reqMgrPasscode) {
		this.reqMgrPasscode = reqMgrPasscode;
	}
	
	public boolean getBypassInvAdj() {
		return bypassInvAdj;
	}
	
	public void setBypassInvAdj(boolean bypassInvAdj) {
		this.bypassInvAdj = bypassInvAdj;
	}
	
	public void setCtp(boolean ctp) {
		this.ctp = ctp;
	}
	public boolean getCtp() {
		return ctp;
	}
	
	public void setPriceTier(String priceTier) {
		this.priceTier = priceTier;
	}
	
	public String getPriceTier() {
		return priceTier;
	}

	public boolean getHasATier1() {
		return hasATier1;
	}

	public void setHasATier1(boolean hasATier1) {
		this.hasATier1 = hasATier1;
	}

	public boolean getHasATier2() {
		return hasATier2;
	}

	public void setHasATier2(boolean hasATier2) {
		this.hasATier2 = hasATier2;
	}
	
	
}
