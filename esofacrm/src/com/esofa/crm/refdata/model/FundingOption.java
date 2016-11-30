package com.esofa.crm.refdata.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class FundingOption implements Serializable {
	private static final long serialVersionUID = 2L;

	@Id
	Long id;
	
	@NotBlank
	private String option;
	
	@NotNull
	@Digits(integer=5, fraction=0)
	@Index
	private int fundingDetailsType;
	
	private String description;

	private Float adpPercentage;
	
	private boolean benefitCodeApplicable; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFundingDetailsType() {
		return fundingDetailsType;
	}

	public void setFundingDetailsType(int fundingDetailsType) {
		this.fundingDetailsType = fundingDetailsType;
	}

	
	public Float getAdpPercentage() {
		return adpPercentage;
	}
	
	public BigDecimal getAdpPercentageAsBD() {
		
		if (adpPercentage == null ) {
			adpPercentage = new Float(0.00);
		}
		return new BigDecimal(adpPercentage.toString()).setScale(2,BigDecimal.ROUND_HALF_UP);
	}	

	public void setAdpPercentage(Float adpPercentage) {
		this.adpPercentage = adpPercentage;
	}

	public boolean getBenefitCodeApplicable() {
		return benefitCodeApplicable;
	}

	public void setBenefitCodeApplicable(boolean benefitCodeApplicable) {
		this.benefitCodeApplicable = benefitCodeApplicable;
	}
}
