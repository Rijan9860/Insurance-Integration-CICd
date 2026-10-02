package org.bahmni.insurance.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Insurance {
	private Boolean isMember;

	@NotEmpty(message = "Name is required field")
	@NotNull
	@Size(min=3, max=10)
	private String nhisNumber;
	
	public Insurance() {}
	
	public Insurance(String nhisNumber, Boolean isMember) {
	this.nhisNumber=nhisNumber;
	this.isMember=isMember;
	}
	public String getNhisNumber() {
		return nhisNumber;
	}
	public void setNhisNumber(String nhisNumber) {
		this.nhisNumber = nhisNumber;
	}
	public Boolean getIsMember() {
		return isMember;
	}
	public void setIsMember(Boolean isMember) {
		this.isMember = isMember;
	}
	@Override
    public String toString() {
        return "NHIS Number" + nhisNumber + ", isMember=" + isMember + "]";
    }
	

}
