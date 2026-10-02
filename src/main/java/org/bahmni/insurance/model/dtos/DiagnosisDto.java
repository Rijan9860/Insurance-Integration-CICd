package org.bahmni.insurance.model.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiagnosisDto {
	
	@SerializedName("icdCode")
	@Expose
	private String icdCode;
	
	@SerializedName("diagnosis")
	@Expose
	private String diagnosis;
	
	@SerializedName("sequence")
	@Expose
	private Integer sequence;
	
	public String getIcdCode() {
		return icdCode;
	}
	
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	
	public String getDiagnosis() {
		return diagnosis;
	}
	
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}

