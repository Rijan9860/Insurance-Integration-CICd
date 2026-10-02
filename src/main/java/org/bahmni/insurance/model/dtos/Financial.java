package org.bahmni.insurance.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Financial {
	
	private AllowedMoney allowedMoney;
    private UsedMoney usedMoney;
    
    @JsonProperty("type")
    private Type type;
    
	public AllowedMoney getAllowedMoney() {
		return allowedMoney;
	}
	public void setAllowedMoney(AllowedMoney allowedMoney) {
		this.allowedMoney = allowedMoney;
	}
	public UsedMoney getUsedMoney() {
		return usedMoney;
	}
	public void setUsedMoney(UsedMoney usedMoney) {
		this.usedMoney = usedMoney;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
