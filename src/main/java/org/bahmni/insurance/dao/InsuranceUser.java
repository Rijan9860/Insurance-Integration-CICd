package org.bahmni.insurance.dao;


import org.bahmni.insurance.model.Insurance;
import org.springframework.stereotype.Repository;

@Repository
public class InsuranceUser {
	  public boolean findDetailById(String nhisNumber) {
		  Insurance insurance = new Insurance();
		return insurance.getNhisNumber().equals(nhisNumber);
	        
	    }

}
