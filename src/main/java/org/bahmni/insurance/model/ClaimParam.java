package org.bahmni.insurance.model;

import java.math.BigDecimal;
import java.util.List;

import org.bahmni.insurance.model.dtos.DiagnosisDto;
import org.bahmni.insurance.model.dtos.VisitDto;
import org.bahmni.insurance.model.dtos.ExtensionDto;
import org.bahmni.insurance.model.dtos.InformationDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimParam {
	
	@SerializedName("patientUUID")
	@Expose
	private String patientUUID;
	
	@SerializedName("claimId")
	@Expose
	private String claimId;
	
	@SerializedName("insureeId")
	@Expose
	private String insureeId;
	
	@SerializedName("nmc")
	@Expose
	private String nmc;
	
	@SerializedName("careType")
	@Expose
	private String careType;
	
	@SerializedName("visit")
	@Expose
	private VisitDto visit;
	
	@SerializedName("diagnosis")
	@Expose
	private List<DiagnosisDto> diagnosis;
	
	@SerializedName("item")
	@Expose
	private List<ClaimLineItemRequest> item;
	
	@SerializedName("total")
	@Expose
	private BigDecimal total;
	
	@SerializedName("extension")
	@Expose
	private List<ExtensionDto> extension;
	
	@SerializedName("information")
	@Expose
	private List<InformationDto> information;
	
	public String getNmc() {
		return nmc;
	}
	
	public void setNmc(String nmc) {
		this.nmc = nmc;
	}
	
	public String getCareType() {
		return careType;
	}
	
	public void setCareType(String careType) {
		this.careType = careType;
	}
	
	public String getPatientUUID() {
		return patientUUID;
	}
	
	public void setPatientUUID(String patientUUID) {
		this.patientUUID = patientUUID;
	}
	
	public String getClaimId() {
		return claimId;
	}
	
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	
	public String getInsureeId() {
		return insureeId;
	}
	
	public void setInsureeId(String insureeId) {
		this.insureeId = insureeId;
	}
	
	public List<ClaimLineItemRequest> getItem() {
		return item;
	}
	
	public void setItem(List<ClaimLineItemRequest> item) {
		this.item = item;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public VisitDto getVisit() {
		return visit;
	}
	
	public void setVisit(VisitDto visit) {
		this.visit = visit;
	}
	
	public List<DiagnosisDto> getDiagnosis() {
		return diagnosis;
	}
	
	public void setDiagnosis(List<DiagnosisDto> diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<ExtensionDto> getExtension() {
		return extension;
	}

	public void setExtension(List<ExtensionDto> extension) {
		this.extension = extension;
	}

	public List<InformationDto> getInformation() {
		return information;
	}

	public void setInformation(List<InformationDto> information) {
		this.information = information;
	}
	
}
