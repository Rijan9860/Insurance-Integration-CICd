package org.bahmni.insurance.model;

import java.util.List;

public class EligibilityResponseModel {
	
	private String nhisId;
	
	private String patientId;
	
	private List<EligibilityBalance> eligibilityBalance;
	
	private double coPaymentValue;
	
	private String hospital;
	
	private String district;
	
	private String profilePicture;
	
	public String getNhisId() {
		return nhisId;
	}
	
	public void setNhisId(String nhisId) {
		this.nhisId = nhisId;
	}
	
	public String getPatientId() {
		return patientId;
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	public List<EligibilityBalance> getEligibilityBalance() {
		return eligibilityBalance;
	}
	
	public void setEligibilityBalance(List<EligibilityBalance> eligibilityBalance) {
		this.eligibilityBalance = eligibilityBalance;
	}
	
	public double getCoPaymentValue() {
		return coPaymentValue;
	}
	
	public void setCoPaymentValue(double coPaymentValue) {
		this.coPaymentValue = coPaymentValue;
	}
	
	public String getHospital() {
		return hospital;
	}
	
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	
	public String getDistrict() {
		return district;
	}
	
	public void setDistrict(String district) {
		this.district = district;
	}
	
	public String getProfilePicture() {
		return profilePicture;
	}
	
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	
}
