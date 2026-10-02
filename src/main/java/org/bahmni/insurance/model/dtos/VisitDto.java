package org.bahmni.insurance.model.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitDto {
	
	@SerializedName("visitUUID")
	@Expose
	private String visitUUID;
	
	@SerializedName("startDate")
	@Expose
	private Long startDate;
	
	@SerializedName("endDate")
	@Expose
	private Long endDate;
	
	@SerializedName("visitType")
	@Expose
	private String visitType;
	
	public String getVisitUUID() {
		return visitUUID;
	}
	
	public void setVisitUUID(String visitUUID) {
		this.visitUUID = visitUUID;
	}
	
	public Long getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	
	public Long getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	
	public String getVisitType() {
		return visitType;
	}
	
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	
}
