package com.esofa.crm.model.pos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.esofa.crm.common.model.Address;
import com.esofa.crm.model.CompanyTypeE;
import com.esofa.crm.model.Customer;
import com.esofa.crm.refdata.model.DiscountReason;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.util.MathUtils;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Invoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6643811981011083216L;

	public static int SCALE = 2;

	public Invoice() {
		this(CompanyTypeE.CPAPDIRECT.name());
	}
	
	public Invoice(String company) {

		this.company = company;
		shopAddress = new Address();
		customerAddress = new Address();

		subTotal = MathUtils.ZERO.toString();
		hst = MathUtils.ZERO.toString();
		total = MathUtils.ZERO.toString();
		redemCode = null;
		adpPortion = MathUtils.ZERO.toString();
		clientPortion = null;
		balanceRemaining = MathUtils.ZERO.toString();
		promoCode = MathUtils.ZERO.toString();
		creditItemTotal = MathUtils.ZERO.toString();
		shippingTotal = MathUtils.ZERO.toString();
		
		refInvoiceCredit = MathUtils.ZERO.toString();
		adjInvoiceClientPortion = MathUtils.ZERO.toString();
		passcodeHash=StringUtils.EMPTY;
	}

	public enum InvoiceStatusTypeE {
		VOID, FULL_PAYMENT, PARTIAL_PAYMENT,REFUNDED,TRIAL_ENDED;
	}
	
	public enum InvoiceAdpStatusTypeE {
		PENDING,RECEIVED
	}
	
	public enum InvoiceTypeE {
		STANDARD,TRIAL_REFUND,TRIAL,OTHER, SALES_RETURN
	}
	
	public enum PriceModeTypeE {
		ADP, NON_ADP
	}

	@Id
	private Long id;

	@Index
	private String company;
	@Index
	private String priceMode;
	
	@Index
	private String invoiceNumber;

	@Index
	private String referencedInvoiceNumber;
	
	@Index
	private Date invoiceDate;
	@Index
	private Date insertDateTime;

	// TODO to be removed. require conversion
	// getUserName will still be available
	@Deprecated
	private String userName;
	private String userFirstName;
	private String userLastName;

	private Address shopAddress;
	private String shopPhone;
	private String shopFax;
	private String shopName;
	@Index
	private Key<Shop> shopKey;

	private String adpVendorNumber;
	private String hstNumber;
	private String referenceNumber; // dormant

	private Key<Customer> customerKey;

	// TODO to be removed. require conversion
	// getCustomerName will still be available
	@Deprecated
	private String customerName;
	private String customerLastName;
	private String customerFirstName;

	private Address customerAddress;

	private String customerPhone;
	
	private String healthCardNumber;
	private String benefitCode;
	private String memberId;

	private String subTotal;
	private String hst;
	@Deprecated
	private String redemCode;
	private String total;
	private String adpPortion;
	private String clientPortion;
	private String promoCode;

	@Deprecated
	private String creditItemTotal;

	private String shippingTotal;
	
	private String paymentMethod;
	private String machineWarranty;
	private String maskWarranty;

	@Index
	private String status;
	@Index
	private String adpStatus;
	@Index
	private String invoiceType = StringUtils.EMPTY	;
	
	
	private String balanceRemaining;
	private String adpBalanceRemaining;

	private String remark1;
	private String remark2;
	
	private Key<DiscountReason> discountReason;
	private String discountNote;
	
	private String refInvoiceCredit;
	private String adjInvoiceClientPortion;
	
	private String passcodeHash;
	
	private boolean specialPricing=false;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPriceMode() {
		return priceMode;
	}

	public void setPriceMode(String priceMode) {
		this.priceMode = priceMode;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getRawUserName() {
		
		return userName;
	}
	
	public String getUserName() {
		
		if (StringUtils.isEmpty(userName)) {
		
			return new StringBuffer().append(this.userLastName)
				 .append(", ")
				 .append(this.userFirstName)
				 .toString();
		}
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserFirstName() {
	
		if (StringUtils.isEmpty(userFirstName) && StringUtils.isNotEmpty(userName)) {
			
			
			String name[] = userName.split(", ");
			if (name.length >1) {
				return name[1];
			} else {
				return StringUtils.EMPTY;
			}
			
		}
					
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
	
		if (StringUtils.isEmpty(userLastName) && StringUtils.isNotEmpty(userName)) {
			
			String name[] = userName.split(", ");
			if (name.length > 0) {
				return name[0];
			}
		}
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public Address getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(Address shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getShopPhone() {
		return shopPhone;
	}

	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}

	public String getShopFax() {
		return shopFax;
	}

	public void setShopFax(String shopFax) {
		this.shopFax = shopFax;
	}

	public String getAdpVendorNumber() {
		return adpVendorNumber;
	}

	public void setAdpVendorNumber(String adpVendorNumber) {
		this.adpVendorNumber = adpVendorNumber;
	}

	public String getHstNumber() {
		return hstNumber;
	}

	public void setHstNumber(String hstNumber) {
		this.hstNumber = hstNumber;
	}

	public Key<Customer> getCustomerKey() {
		return customerKey;
	}

	public void setCustomerKey(Key<Customer> customerKey) {
		this.customerKey = customerKey;
	}

	public String getCustomerName() {
	
		if (StringUtils.isEmpty(customerName)) {
			
			if (StringUtils.isEmpty(customerLastName) && StringUtils.isEmpty(customerFirstName)) {
			
				return StringUtils.EMPTY;
				
			} else {
			
			return new StringBuffer().append(this.customerLastName)
									 .append(", ")
									 .append(this.customerFirstName)
									 .toString();
			}
		}
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerLastName() {
	
		if(StringUtils.isEmpty(customerLastName) && StringUtils.isNotEmpty(customerName)) {
			
			return StringUtils.substringAfterLast(customerName, " ");
		}
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerFirstName() {
	
		
		if(StringUtils.isEmpty(customerFirstName) && StringUtils.isNotEmpty(customerName)) {
		
			return StringUtils.substringBeforeLast(customerName, " ");					
		}
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public String getBenefitCode() {
		return benefitCode;
	}

	public void setBenefitCode(String benefitCode) {
		this.benefitCode = benefitCode;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getSubTotalAsBD() {
		return new BigDecimal(subTotal);
	}

	public void setSubTotalAsBD(BigDecimal subTotal) {
		this.subTotal = MathUtils.setBDScale(subTotal).toString();
	}

	public BigDecimal getHstAsBD() {
		return new BigDecimal(hst);
	}

	public void setHstAsBD(BigDecimal hst) {
		this.hst =MathUtils.setBDScale(hst).toString();
	}

	@Deprecated
	public BigDecimal getRedemCodeAsBD() {

		if (redemCode == null) {
			redemCode = MathUtils.ZERO.toString();
		}
		return new BigDecimal(redemCode).setScale(SCALE);
	}

	@Deprecated
	public void setRedemCodeAsBD(BigDecimal redemCode) {

		if (redemCode == null) {
			return;
		}
		this.redemCode = redemCode.setScale(SCALE).toString();
	}

	public BigDecimal getTotalAsBD() {
		return new BigDecimal(total);
	}

	public void setTotalAsBD(BigDecimal total) {
		this.total = total.setScale(SCALE).toString();
	}

	public BigDecimal getAdpPortionAsBD() {
		return new BigDecimal(adpPortion);
	}

	public void setAdpPortionAsBD(BigDecimal adpPortion) {
		this.adpPortion = MathUtils.setBDScale(adpPortion).toString();
	}

	public BigDecimal getClientPortionAsBD() {
		return new BigDecimal(clientPortion);
	}

	public void setClientPortionAsBD(BigDecimal clientPortion) {
		this.clientPortion = clientPortion.toString();
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getHst() {
		return hst;
	}

	public void setHst(String hst) {
		this.hst = hst;
	}

	@Deprecated
	public String getRedemCode() {
		return redemCode;
	}

	@Deprecated
	public void setRedemCode(String redemCode) {
		this.redemCode = redemCode;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getAdpPortion() {
		return adpPortion;
	}

	public void setAdpPortion(String adpPortion) {
		this.adpPortion = adpPortion;
	}

	public String getClientPortion() {
		return clientPortion;
	}

	public void setClientPortion(String clientPortion) {
		this.clientPortion = clientPortion;
	}

	public Address getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(Address customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setMachineWarranty(String machineWarranty) {
		this.machineWarranty = machineWarranty;
	}

	public String getMachineWarranty() {
		return machineWarranty;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getDisplayStatus() {

		String displayStatus = status;
		if (StringUtils.isNotEmpty(status)) {

			displayStatus = StringUtils.replace(status, "_", " ");
			displayStatus = WordUtils.capitalize(displayStatus);
		}

		return displayStatus;

	}

	public void setBalanceRemaining(String balanceRemaining) {
		this.balanceRemaining = balanceRemaining;
	}

	public String getBalanceRemaining() {
		return balanceRemaining;
	}

	public void setBalanceRemainingAsBD(BigDecimal balanceRemaining) {
		this.balanceRemaining = balanceRemaining.setScale(SCALE).toString();
	}

	public BigDecimal getBalanceRemainingAsBD() {

		if (StringUtils.isEmpty(balanceRemaining)) {

			balanceRemaining = MathUtils.ZERO.toString();
		}
		return new BigDecimal(balanceRemaining);
	}

	public void decrementBalanceRemainingAsBD(BigDecimal delta) {

		setBalanceRemainingAsBD(getBalanceRemainingAsBD().subtract(delta));
	}

	public String getAdpBalanceRemaining() {
		return adpBalanceRemaining;
	}

	public void setAdpBalanceRemaining(String adpBalanceRemaining) {
		this.adpBalanceRemaining = adpBalanceRemaining;
	}

	public void setAdpBalanceRemainingAsBD(BigDecimal adpBalanceRemaining) {
		this.adpBalanceRemaining = adpBalanceRemaining.setScale(SCALE)
				.toString();
	}

	public BigDecimal getAdpBalanceRemainingAsBD() {

		if (StringUtils.isEmpty(adpBalanceRemaining)) {

			adpBalanceRemaining = MathUtils.ZERO.toString();
		}
		return new BigDecimal(adpBalanceRemaining);
	}

	public void decrementAdpBalanceRemainingAsBD(BigDecimal delta) {

		setAdpBalanceRemainingAsBD(getAdpBalanceRemainingAsBD().subtract(delta));
	}

	public Date getInsertDateTime() {
		return insertDateTime;
	}

	public void setInsertDateTime(Date insertDateTime) {
		this.insertDateTime = insertDateTime;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public BigDecimal getPromoCodeAsBD() {

		if (StringUtils.isEmpty(promoCode)) {
			promoCode = BigDecimal.ZERO.setScale(2).toString();
		}
		return new BigDecimal(promoCode);
	}

	public void setPromoCodeAsBD(BigDecimal promoCode) {
		this.promoCode = promoCode.toString();
	}

	public String getAdpStatus() {
		return adpStatus;
	}

	public void setAdpStatus(String adpStatus) {
		this.adpStatus = adpStatus;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	
	public String getInvoiceType() {
		return invoiceType;
	}
	
	public void setReferencedInvoiceNumber(String referencedInvoiceNumber) {
		this.referencedInvoiceNumber = referencedInvoiceNumber;
	}
	
	public String getReferencedInvoiceNumber() {
		return referencedInvoiceNumber;
	}
	
	@Deprecated
	public String getCreditItemTotal() {
		return creditItemTotal;
	}

	@Deprecated
	public void setCreditItemTotal(String creditItemTotal) {
		this.creditItemTotal = creditItemTotal;
	}

	@Deprecated
	public BigDecimal getCreditItemTotalAsBD() {

		if (StringUtils.isEmpty(creditItemTotal)) {
			creditItemTotal = MathUtils.ZERO.toString();
		}
		return new BigDecimal(creditItemTotal);
	}

	@Deprecated
	public void setCreditItemTotalAsBD(BigDecimal creditItemTotal) {
		this.creditItemTotal = creditItemTotal.toString();
	}
	
	public String getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(String shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public BigDecimal getShippingTotalAsBD() {

		if (StringUtils.isEmpty(shippingTotal)) {
			shippingTotal = MathUtils.ZERO.toString();
		}
		return new BigDecimal(shippingTotal);
	}

	public void setShippingTotalAsBD(BigDecimal shippingTotal) {
		this.shippingTotal = shippingTotal.toString();
	}
		
	
	public String getRefInvoiceCredit() {
		return refInvoiceCredit;
	}

	public void setRefInvoiceCredit(String refInvoiceCredit) {
		this.refInvoiceCredit = refInvoiceCredit;
	}
	
	public BigDecimal getRefInvoiceCreditAsBD() {

		if (StringUtils.isEmpty(refInvoiceCredit)) {
			refInvoiceCredit = MathUtils.ZERO.toString();
		}
		return new BigDecimal(refInvoiceCredit);
	}

	public void setRefInvoiceCreditAsBD(BigDecimal refInvoiceCredit) {
		this.refInvoiceCredit = refInvoiceCredit.toString();
	}
		

	public String getAdjInvoiceClientPortion() {
		return adjInvoiceClientPortion;
	}

	public void setAdjInvoiceClientPortion(String adjInvoiceClientPortion) {
		this.adjInvoiceClientPortion = adjInvoiceClientPortion;
	}
	
	public BigDecimal getAdjInvoiceClientPortionAsBD() {

		if (StringUtils.isEmpty(adjInvoiceClientPortion)) {
			adjInvoiceClientPortion = MathUtils.ZERO.toString();
		}
		return new BigDecimal(adjInvoiceClientPortion);
	}

	public void setAdjInvoiceClientPortionAsBD(BigDecimal adjInvoiceClientPortion) {
		this.adjInvoiceClientPortion = adjInvoiceClientPortion.toString();
	}
		

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	
	public String getRemark1() {
		return remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	
	public Key<Shop> getShopKey() {
		return shopKey;
	}
	public void setShopKey(Key<Shop> shopKey) {
		this.shopKey = shopKey;
	}

	public void setDiscountNote(String discountNote) {
		this.discountNote = discountNote;
	}
	
	public String getDiscountNote() {
		return discountNote;
	}

	public Key<DiscountReason> getDiscountReason() {
		return discountReason;
	}

	public void setDiscountReason(Key<DiscountReason> discountReason) {
		this.discountReason = discountReason;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}
	
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	
	
	public void setMaskWarranty(String maskWarranty) {
		this.maskWarranty = maskWarranty;
	}
	
	public String getMaskWarranty() {
		return maskWarranty;
	}
	
	
	public String getPasscodeHash() {
		return passcodeHash;
	}

	public void setPasscodeHash(String passcodeHash) {
		this.passcodeHash = passcodeHash;
	}

	
	public void setSpecialPricing(boolean specialPricing) {
		this.specialPricing = specialPricing;
	}
	
	public boolean getSpecialPricing() {
		return specialPricing;
	}
	
	@Deprecated
	public boolean migrate() {
		boolean changed = false;
		
		// split using current name or get it fresh from current info
		// - invoice is historical. using existing name and split may be saver
		// - assuming ", " is unique delimiter. if space count != 1 flag it		
		if( this.customerFirstName == null && 
			this.customerLastName == null && 
			this.customerName != null ) {
			String name[] = this.customerName.split(" ");
			if( name.length == 2 ) {
				this.customerLastName = name[1];
				this.customerFirstName = name[0];
				this.customerName = null;
				changed = true;
			} else {
				// TODO warning
			}
		}		
		
		if( this.userFirstName == null && 
			this.userLastName == null && 
			this.userName != null ) {
			String name[] = this.userName.split(", ");
			if( name.length == 2 ) {				
				this.userLastName = name[0];
				this.userFirstName = name[1];
				this.userName = null;
				changed = true;
			} else {
				// TODO warning
			}
		}				
				
		return changed;
	}

	public String getUserInitial() {
	
		String retVal = StringUtils.EMPTY;
		
		if (StringUtils.isNotEmpty(userFirstName) && StringUtils.isNotEmpty(userLastName)) {
			
			retVal = String.valueOf(userFirstName.charAt(0)) + String.valueOf(userLastName.charAt(0));
		}
		
		return retVal;
	}
	
	@Override
	public String toString() {
		return "Invoice [id=" + id + ", company=" + company + ", priceMode="
				+ priceMode + ", invoiceNumber=" + invoiceNumber
				+ ", referencedInvoiceNumber=" + referencedInvoiceNumber
				+ ", invoiceDate=" + invoiceDate + ", insertDateTime="
				+ insertDateTime + ", userName=" + userName
				+ ", userFirstName=" + userFirstName + ", userLastName="
				+ userLastName + ", shopAddress=" + shopAddress
				+ ", shopPhone=" + shopPhone + ", shopFax=" + shopFax
				+ ", shopName=" + shopName + ", shopKey=" + shopKey
				+ ", adpVendorNumber=" + adpVendorNumber + ", hstNumber="
				+ hstNumber + ", referenceNumber=" + referenceNumber
				+ ", customerKey=" + customerKey + ", customerName="
				+ customerName + ", customerLastName=" + customerLastName
				+ ", customerFirstName=" + customerFirstName
				+ ", customerAddress=" + customerAddress + ", customerPhone="
				+ customerPhone + ", healthCardNumber=" + healthCardNumber
				+ ", benefitCode=" + benefitCode + ", memberId=" + memberId
				+ ", subTotal=" + subTotal + ", hst=" + hst + ", redemCode="
				+ redemCode + ", total=" + total + ", adpPortion=" + adpPortion
				+ ", clientPortion=" + clientPortion + ", promoCode="
				+ promoCode + ", creditItemTotal=" + creditItemTotal
				+ ", shippingTotal=" + shippingTotal + ", paymentMethod="
				+ paymentMethod + ", machineWarranty=" + machineWarranty
				+ ", maskWarranty=" + maskWarranty + ", status=" + status
				+ ", adpStatus=" + adpStatus + ", invoiceType=" + invoiceType
				+ ", balanceRemaining=" + balanceRemaining
				+ ", adpBalanceRemaining=" + adpBalanceRemaining + ", remark1="
				+ remark1 + ", remark2=" + remark2 + ", discountReason="
				+ discountReason + ", discountNote=" + discountNote + "]";
	}
	
	// Load and resave entity for Objectify 4.1 migration
	@Deprecated
	public boolean lol() {
		return true;	
	}
}
