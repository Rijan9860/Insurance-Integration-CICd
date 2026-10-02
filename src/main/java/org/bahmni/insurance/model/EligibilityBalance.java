package org.bahmni.insurance.model;

import java.math.BigDecimal;
import java.util.Date;

public class EligibilityBalance {
	
	private BigDecimal benefitBalance;
	
	private BigDecimal usedBalance;
	
	private String code;
	
	private String term;
	
	private String category;
	
	private Date validDate;
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
		
	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public BigDecimal getBenefitBalance() {
		return benefitBalance;
	}
	
	public void setBenefitBalance(BigDecimal benefitBalance) {
		this.benefitBalance = benefitBalance;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getTerm() {
		return term;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}

	public BigDecimal getUsedBalance() {
		return usedBalance;
	}

	public void setUsedBalance(BigDecimal usedBalance) {
		this.usedBalance = usedBalance;
	}
	
}
