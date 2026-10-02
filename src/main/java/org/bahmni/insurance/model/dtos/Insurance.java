package org.bahmni.insurance.model.dtos;

import java.util.List;

public class Insurance {
	
	private List<BenefitBalance> benefitBalance;
    private Contract contract;
    private List<Extension> extension;
	
	public List<BenefitBalance> getBenefitBalance() {
		return benefitBalance;
	}
	
	public void setBenefitBalance(List<BenefitBalance> benefitBalance) {
		this.benefitBalance = benefitBalance;
	}
	
	public Contract getContract() {
		return contract;
	}
	
	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	public List<Extension> getExtension() {
		return extension;
	}
	
	public void setExtension(List<Extension> extension) {
		this.extension = extension;
	}
	 
}
