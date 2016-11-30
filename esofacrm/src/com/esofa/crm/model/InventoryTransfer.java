package com.esofa.crm.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.validator.product.inventory.SufficientOriginationStockCheck;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
@SufficientOriginationStockCheck
public class InventoryTransfer  implements Serializable {
	private static final long serialVersionUID = 5862251337755025005L;
	
	public enum TransferStatusE {
		CANCELED
	}
	
	@Id
	private Long id;
	
	@Index
	private Date initDate;
	
	@Index
	private Key<Product> product;
	
	@Index
	private Key<Shop> fromShop;
	
	@Index
	@NotNull
	private Key<Shop> toShop;
	
	private Key<CrmUser> user;
	
	@Min(value=1)
	private int qty;
	
	//only used for add stock. dont need any other time.
	@Ignore
	private Float cost;
	
	private String status;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
	public Key<Product> getProduct() {
		return product;
	}
	public void setProduct(Key<Product> product) {
		this.product = product;
	}
	public Key<Shop> getFromShop() {
		return fromShop;
	}
	public void setFromShop(Key<Shop> fromShop) {
		this.fromShop = fromShop;
	}
	public Key<Shop> getToShop() {
		return toShop;
	}
	public void setToShop(Key<Shop> toShop) {
		this.toShop = toShop;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Float getCost() {
		return cost;
	}
	
	public void setCost(Float cost) {
		this.cost = cost;
	}
	
	
	public Key<CrmUser> getUser() {
		return user;
	}
	public void setUser(Key<CrmUser> user) {
		this.user = user;
	}
}
