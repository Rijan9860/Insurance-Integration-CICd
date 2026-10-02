package org.bahmni.insurance.model.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EligibilityResponseDto {
	
	private String resourceType;
    private List<Insurance> insurance;
    private List<Extension> extension;
    
	public String getResourceType() {
		return resourceType;
	}
	
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	public List<Insurance> getInsurance() {
		return insurance;
	}
	
	public void setInsurance(List<Insurance> insurance) {
		this.insurance = insurance;
	}
    
    
	
}

