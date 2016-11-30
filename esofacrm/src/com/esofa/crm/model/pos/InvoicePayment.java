package com.esofa.crm.model.pos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class InvoicePayment implements Serializable, InvoiceHistoryItem {

	private static final long serialVersionUID = 8763245437683736686L;

	public static final String DESC_FULL_PAYMENT = "Full Payment";
	public static final String PAYMENT="Payment";
	public static final String DESC_PENNY_ROUND = "Penny Round";
	public static final String DESC_ADJUSTMENT="Adjustment";
	
	public static final String DESC_ADP_PYMT ="Adp Payment";
	
	public static final String DESC_ADJ_ADJUSTMENT="Adp Adjustment";

	public static final String DESC_REFUND="Refund";
	public static final String DESC_DEPOSIT="Deposit";
	public static final String DESC_DEPOSIT_REFUND="Deposit Refund";
	public static final String DESC_TRANSFER_TO_PAYMENT = "Transfer to Payment";
	public static final String DESC_TRANSFER_TO_OTHERINCOME = "Transfer to Other Income";
	public static final String DESC_OTHERINCOME="Other Income";
	public static final String DESC_CREDIT="Credit";
	
	public static final String[] includeInDisplay = {DESC_FULL_PAYMENT,DESC_ADJUSTMENT,PAYMENT, DESC_PENNY_ROUND, DESC_DEPOSIT ,DESC_OTHERINCOME, DESC_TRANSFER_TO_PAYMENT };
	public static final String[] adpInDisplay = {DESC_ADP_PYMT,DESC_ADJ_ADJUSTMENT};
			
	private static String ZERO = "0.00";
	private static int SCALE = 2;
	
	@Id
	private Long id;
	
	
	@Index 
	private Key<Invoice> invoiceKey;
	
	@Index
	private Date paymentDate;
	
	@Index
	private Date insertDateTime;
	
	@Index
	private String description;
	
	private String paymentMethod;
	
	private String amt;
	private String balance;
	
	// TODO to be removed. require conversion
	// getUserName will still be available
	@Deprecated	
	private String userName;
	private String userFirstName;
	private String userLastName;
	
	public InvoicePayment() {
	
		amt = ZERO;
		balance = ZERO;
	}
	
	/* (non-Javadoc)
	 * @see com.esofa.crm.model.pos.InvoiceHistoryItem#getId()
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public Key<Invoice> getInvoiceKey() {
		return invoiceKey;
	}

	public void setInvoiceKey(Key<Invoice> invoiceKey) {
		this.invoiceKey = invoiceKey;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public void  setBalanceAsBD(BigDecimal balance) {
		
		this.balance = balance.setScale(2).toString();
	}
	
	public BigDecimal getBalanceAsBD() {
		return new BigDecimal(balance);
	}

	public void setAmtAsBD(BigDecimal amt) {
		this.amt = amt.setScale(SCALE).toString();
	}
	
	public BigDecimal getAmtAsBD() {
		return new BigDecimal(amt);
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
			return name[1];
		}
					
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
	
		if (StringUtils.isEmpty(userLastName) && StringUtils.isNotEmpty(userName)) {
			
			String name[] = userName.split(", ");
			return name[0];
		}
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public Date getInsertDateTime() {
		return insertDateTime;
	}

	public void setInsertDateTime(Date insertDateTime) {
		this.insertDateTime = insertDateTime;
	}
	
	
	public static boolean isDescriptionInList(String desc, String[] descSet) {
		
		boolean result = false;
		
		for (String inSet : descSet) {
			
			if (StringUtils.equalsIgnoreCase(desc, inSet)) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	
	@Deprecated
	public boolean migrate() {
		boolean changed = false;
		
		// split using current name or get it fresh from current info
		// - invoice payment is historical. using existing name and split may be saver
		// - assuming ", " is unique delimiter. if space count != 1 flag it		
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

	@Override
	public String toString() {
		return "InvoicePayment [id=" + id + ", invoiceKey=" + invoiceKey
				+ ", paymentDate=" + paymentDate + ", insertDateTime="
				+ insertDateTime + ", description=" + description
				+ ", paymentMethod=" + paymentMethod + ", amt=" + amt
				+ ", balance=" + balance + ", userName=" + userName
				+ ", userFirstName=" + userFirstName + ", userLastName="
				+ userLastName + "]";
	}	
}
