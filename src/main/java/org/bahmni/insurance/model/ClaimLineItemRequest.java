package org.bahmni.insurance.model;

import java.math.BigDecimal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimLineItemRequest {

	@SerializedName("category")
	@Expose
	private String category;

	@SerializedName("quantity")
	@Expose
	private BigDecimal quantity;

	@SerializedName("sequence")
	@Expose
	private Integer sequence;

	@SerializedName("unitPrice")
	@Expose
	private BigDecimal unitPrice;

	@SerializedName("totalClaimed")
	@Expose
	private BigDecimal totalClaimed;

	@SerializedName("status")
	@Expose
	private String status;

	@SerializedName("rejectedReason")
	@Expose
	private String rejectedReason;

	@SerializedName("totalApproved")
	@Expose
	private BigDecimal totalApproved;
	
	@SerializedName("code")
	@Expose
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalClaimed() {
		return totalClaimed;
	}

	public void setTotalClaimed(BigDecimal totalClaimed) {
		this.totalClaimed = totalClaimed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejectedReason() {
		return rejectedReason;
	}

	public void setRejectedReason(String rejectedReason) {
		this.rejectedReason = rejectedReason;
	}

	public BigDecimal getTotalApproved() {
		return totalApproved;
	}

	public void setTotalApproved(BigDecimal totalApproved) {
		this.totalApproved = totalApproved;
	}

}
