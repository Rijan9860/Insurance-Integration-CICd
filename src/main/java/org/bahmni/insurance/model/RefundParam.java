package org.bahmni.insurance.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefundParam {
	
	@SerializedName("claimId")
	@Expose
	private String claimId;
	
	@SerializedName("type")
	@Expose
	private String type;
	
	@SerializedName("codes")
	@Expose
	private List<String> codes;
	
	public String getClaimId() {
		return claimId;
	}
	
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
	
}
