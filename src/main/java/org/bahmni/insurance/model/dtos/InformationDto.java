package org.bahmni.insurance.model.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InformationDto {
	
	@SerializedName("category")
	@Expose
	private String category;
	
	@SerializedName("sequence")
	@Expose
	private Integer sequence;
	
	@SerializedName("valuestring")
	@Expose
	private String valueString;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	
}