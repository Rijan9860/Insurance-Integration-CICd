package org.bahmni.insurance.model.dtos;

import java.util.List;

public class BenefitBalance {
	
	private Category category;
    private List<Financial> financial;
    
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public List<Financial> getFinancial() {
		return financial;
	}
	public void setFinancial(List<Financial> financial) {
		this.financial = financial;
	}
	
}
