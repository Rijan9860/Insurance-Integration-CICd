package org.bahmni.insurance.model;
import java.util.Date;

public class ClaimTrackingModel {

	private String claimId;
	private String claimOwner;
	private String claimStatus;
	private String claimDesc;
	private String claimSignature;
	private Date dateProcessed;
	private Date dateAuthorized;
	private Date dateLastModified;
/*	public enum ClaimStatus {
		REJECTED, ENTERED, CHECKED, PROCESSED, VALUATED
	};
	*/
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public String getClaimOwner() {
		return claimOwner;
	}
	public void setClaimOwner(String claimOwner) {
		this.claimOwner = claimOwner;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getClaimDesc() {
		return claimDesc;
	}
	public void setClaimDesc(String claimDesc) {
		this.claimDesc = claimDesc;
	}
	public String getClaimSignature() {
		return claimSignature;
	}
	public void setClaimSignature(String claimSignature) {
		this.claimSignature = claimSignature;
	}
	public Date getDateProcessed() {
		return dateProcessed;
	}
	public void setDateProcessed(Date dateProcessed) {
		this.dateProcessed = dateProcessed;
	}
	public Date getDateAuthorized() {
		return dateAuthorized;
	}
	public void setDateAuthorized(Date dateAuthorized) {
		this.dateAuthorized = dateAuthorized;
	}
	public Date getDateLastModified() {
		return dateLastModified;
	}
	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}	
}