package org.bahmni.insurance.model.dtos;

import java.util.Date;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.ResourceType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class InsureeDto {
	
	private String uuid;
	
	private String familyName;
	
	private String givenName;
	
	private Date birthdate;
	
	private String gender;
	
	private String address;
	
	private String telephone;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public static InsureeDto fromBundleResponse(String bundleJson) {
		System.out.println("Entered into the from bundle response");
		FhirContext ctx = FhirContext.forDstu3();
		IParser parser = ctx.newJsonParser();
		Bundle bundle = parser.parseResource(Bundle.class, bundleJson);
		
		InsureeDto patientDTO = new InsureeDto();
		
		for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
			if (entry.hasResource() && entry.getResource().getResourceType() == ResourceType.Patient) {
				Patient patient = (Patient) entry.getResource();
				System.out.println("Gender---------->" + patient.getGender().getDisplay());

				patientDTO.setUuid(patient.getIdElement().getIdPart());
				
				if (!patient.getName().isEmpty()) {
					HumanName name = patient.getName().get(0);
					patientDTO.setFamilyName(name.getFamily());
					if (!name.getGiven().isEmpty()) {
						patientDTO.setGivenName(name.getGiven().get(0).getValue());
					}
				}
				
				if (patient.hasBirthDate()) {
					patientDTO.setBirthdate(patient.getBirthDate());
				}
				
				if (patient.hasGender()) {
					patientDTO.setGender(patient.getGender().getDisplay());
				}

				
				if (!patient.getAddress().isEmpty()) {
					Address patientAddress = patient.getAddressFirstRep();
					patientDTO.setAddress(patientAddress.getText());
				}
				
				if (!patient.getTelecom().isEmpty()) {
					for (ContactPoint telecom : patient.getTelecom()) {
						if (telecom.hasSystem() && telecom.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
							patientDTO.setTelephone(telecom.getValue());
							break;
						}
					}
				}
				
				// We only process the first patient entry
				break;
			}
		}
		
		return patientDTO;
	}
	
}
