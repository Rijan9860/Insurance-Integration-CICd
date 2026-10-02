package org.bahmni.insurance.model.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtensionDto {
	
	@SerializedName("valueString")
	@Expose
	private String valueString;
	
	@SerializedName("code")
	@Expose
	private String code;

	public String getValueString() {
		return valueString;
	}
	
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

}
