package org.bahmni.insurance.service;

import java.net.URISyntaxException;

import org.bahmni.insurance.client.ClientHelperFactory;
import org.bahmni.insurance.model.CapValidation;
import org.bahmni.insurance.model.ClaimResponseModel;
import org.bahmni.insurance.model.ClaimTrackingModel;
import org.bahmni.insurance.model.EligibilityResponseModel;
import org.bahmni.insurance.model.dtos.InsureeDto;
import org.bahmni.insurance.model.RefundParam;
import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.ClaimResponse;
import org.hl7.fhir.dstu3.model.EligibilityRequest;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Task;
import org.hl7.fhir.exceptions.FHIRException;
/*import org.openmrs.module.fhir.api.helper.ClientHelper;*/
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import ca.uhn.fhir.parser.DataFormatException;

import java.util.List;

public abstract class AInsuranceClientService {
	/*private ClientHelper clientHelper;

	public ClientHelper getClientHelper(String clientType) {
		clientHelper = ClientHelperFactory.createClient(clientType);
		return clientHelper;
	}
*/
	public abstract ClaimResponseModel submitClaim(Claim claimRequest) throws RestClientException, URISyntaxException;
	
	public abstract ResponseEntity<String> refundClaim(RefundParam refundParams) throws RestClientException, URISyntaxException;
	
	public abstract ResponseEntity<ClaimResponseModel> trackClaim(String claimUuid);
	
	public abstract ResponseEntity<List<CapValidation>> getCapValidation(String insureeId)
	        throws RestClientException, URISyntaxException, FHIRException;
	
	public abstract EligibilityResponseModel getElibilityResponse(EligibilityRequest eligbilityRequest)
			throws RestClientException, URISyntaxException, FHIRException;

	public abstract ClaimResponse getClaimStatus(Task claimStatusRequest);

	public abstract String loginCheck();

//	public abstract EligibilityResponseModel getDummyEligibilityResponse() throws FHIRException;

//	public abstract ClaimTrackingModel getDummyClaimTrack();
//
//	public abstract ClaimResponseModel getDummyClaimResponse(Claim claimRequest);

	public abstract InsureeDto getInsureeInfo(String insureeId);

	public abstract ClaimResponseModel submitClaim(String jsonClaimRequest);
		
}
