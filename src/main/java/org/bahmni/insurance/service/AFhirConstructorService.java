package org.bahmni.insurance.service;

import java.io.IOException;

import org.bahmni.insurance.ImisConstants;
import org.bahmni.insurance.client.RestTemplateFactory;
import org.bahmni.insurance.model.ClaimParam;
import org.bahmni.insurance.model.RefundParam;
import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.EligibilityRequest;
import org.hl7.fhir.dstu3.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public abstract class AFhirConstructorService {

	@Autowired
	RestTemplateFactory restFactory;

	public RestTemplate getApiClient() {
		return restFactory.getRestTemplate(ImisConstants.OPENMRS_FHIR);
	}

	public abstract Claim constructFhirClaimRequest(ClaimParam claimParamObj) throws IOException;
	
	public abstract EligibilityRequest constructFhirEligibilityRequest(String insuranceID) throws IOException;

	public abstract String getFhirPatient(String patientId);

	public abstract Task constructFhirClaimTrackRequest(String insuranceID) throws IOException;

	public abstract ResponseEntity<String> createFhirPatient(String patientId);

	public abstract boolean validateRequest(String fhirJson) throws IOException;

	public abstract String addCustomFieldsClaim(String claimRequest, ClaimParam claimParam)
	        throws JsonParseException, JsonMappingException, IOException;

}
