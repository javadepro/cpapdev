package com.esofa.crm.model.pos;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Product;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class InvoiceItem implements Serializable {


	public enum ItemPriceTier {
		TIER_1, TIER_2
	}
	
	private static final long serialVersionUID = 395413101910795321L;
	
	private static String ZERO = "0.00";
	private static int SCALE =2;
	
	@Id
	private Long id;
	
	@Index
	private Key<Invoice> invoiceKey;
	
	private Key<Product> productKey;
	
	private String productName;
	private String productBarcode;
	private String productReference;
	private String productAdpNumber;	
	private String productSerialNumber;
	private String additionalSerial= StringUtils.EMPTY;
	private String description= StringUtils.EMPTY;;
	
	private String lotNumber = StringUtils.EMPTY;

	private boolean hstApplicable;
	
	private int qty;
	private String price;
	private String total;
	
	private String inventoryCost;
	
	@Index
	private int order;
	
	private int qtyReturned;
	private boolean bypassInvAdj;

	private String priceTier;

	public InvoiceItem() {
	
		price = ZERO;
		total = ZERO;
		inventoryCost = ZERO;
		qtyReturned =0;
	}
	
	public InvoiceItem(Key<Product> productKey,String productName,int qty,String price,boolean bypassInvAdj, String total) {
		
		this(productKey,productName,null,null,null,null,null,false,qty,price,bypassInvAdj);
		this.total = total;
	}

	public InvoiceItem(Key<Product> productKey,
			String productName, String productAdpNumber,
			String productSerialNumber, 
			String lotNumber, 
			String additionalSerial, 
			String description,
			boolean hstApplicable, int qty, String price, boolean bypassInvAdj) {
		this();
		this.productKey = productKey;
		this.productName = productName;
		this.productAdpNumber = productAdpNumber;
		this.productSerialNumber = productSerialNumber;
		this.lotNumber = lotNumber;
		this.additionalSerial = additionalSerial;
		this.description = description;
		this.hstApplicable = hstApplicable;
		this.qty = qty;
		this.price = price;
		this.bypassInvAdj = bypassInvAdj;
	}



	public void setTotalAsBD(BigDecimal total) {
		this.total = total.toString();
	}
	
	public BigDecimal getTotalAsBD() {
		return new BigDecimal(total);
	}
	
	public String getTotal() {
		return total;
	}

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

	public Key<Product> getProductKey() {
		return productKey;
	}

	public void setProductKey(Key<Product> productKey) {
		this.productKey = productKey;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductAdpNumber() {
		return productAdpNumber;
	}

	public void setProductAdpNumber(String productAdpNumber) {
		this.productAdpNumber = productAdpNumber;
	}

	public String getProductSerialNumber() {
		return productSerialNumber;
	}

	public void setProductSerialNumber(String productSerialNumber) {
		this.productSerialNumber = productSerialNumber;
	}

	public int getQty() {
		return qty;
	}

	public void addQty(int delta) {
		setQty(qty+delta);
	}
	
	public void setQty(int qty) {
		this.qty = qty;
		adjustTotal(getPriceAsBD(),qty);
	}

	public BigDecimal getPriceAsBD() {
		return new BigDecimal(price);
	}

	public void setPriceAsBD(BigDecimal price) {
		this.price = price.setScale(SCALE,BigDecimal.ROUND_HALF_UP).toString();
		adjustTotal(getPriceAsBD(),getQty());
	}
	
	private void adjustTotal(BigDecimal priceBd, int qty) {
		

		total = priceBd.multiply(new BigDecimal(qty)).setScale(SCALE).toString();
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	public boolean getHstApplicable() {
		return hstApplicable;
	}

	public void setHstApplicable(boolean hstApplicable) {
		this.hstApplicable = hstApplicable;
	}
	

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public void setBypassInvAdj(boolean bypassInvAdj) {
		this.bypassInvAdj = bypassInvAdj;
	}
	public boolean getBypassInvAdj() {
		return bypassInvAdj;
	}

	public void setInventoryCost(String inventoryCost) {
		this.inventoryCost = inventoryCost;
	}
	
	public String getInventoryCost() {
		return inventoryCost;
	}
	
	public BigDecimal getInventoryCostAsBD() {
		return new BigDecimal(inventoryCost);
	}

	public void setInventoryCostAsBD(BigDecimal inventoryCost) {
		this.inventoryCost = inventoryCost.setScale(SCALE,BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	
	public void setAdditionalSerial(String additionalSerial) {
		this.additionalSerial = additionalSerial;
	}
	public String getAdditionalSerial() {
		return additionalSerial;
	}
	
	
	
	public String getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}

	public String getProductReference() {
		return productReference;
	}

	public void setProductReference(String productReference) {
		this.productReference = productReference;
	}

	public int getQtyReturned() {
		return qtyReturned;
	}
	
	public void setQtyReturned(int qtyReturned) {
		this.qtyReturned = qtyReturned;
	}

	public void addQtyReturned(int delta) {
		setQtyReturned(qtyReturned+delta);
	}
	
	public String getPriceTier() {
		
		if (StringUtils.isEmpty(priceTier)) {
			priceTier = InvoiceItem.ItemPriceTier.TIER_1.name();
		}
		return priceTier;
	}
	
	public void setPriceTier(String priceTier) {
		this.priceTier = priceTier;
	}
	
}
