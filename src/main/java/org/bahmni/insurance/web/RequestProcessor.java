package org.bahmni.insurance.web;

import static org.apache.log4j.Logger.getLogger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bahmni.insurance.AppProperties;
import org.bahmni.insurance.ImisConstants;
import org.bahmni.insurance.client.RestTemplateFactory;
import org.bahmni.insurance.dao.FhirResourceDaoServiceImpl;
import org.bahmni.insurance.dao.IFhirResourceDaoService;
import org.bahmni.insurance.model.ClaimParam;
import org.bahmni.insurance.model.ClaimResponseModel;
import org.bahmni.insurance.model.EligibilityResponseModel;
import org.bahmni.insurance.model.FhirResourceModel;
import org.bahmni.insurance.model.VisitSummary;
import org.bahmni.insurance.model.CapValidation;
import org.bahmni.insurance.model.RefundParam;
import org.bahmni.insurance.service.AFhirConstructorService;
import org.bahmni.insurance.service.FInsuranceServiceFactory;
import org.bahmni.insurance.serviceImpl.FhirConstructorServiceImpl;
import org.bahmni.insurance.serviceImpl.OpenmrsOdooServiceImpl;
import org.bahmni.insurance.serviceImpl.BahmniOpenmrsApiClientServiceImpl;
import org.bahmni.insurance.utils.InsuranceUtils;
import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.ClaimResponse;
import org.hl7.fhir.dstu3.model.EligibilityRequest;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;

@RestController
public class RequestProcessor {
	
	private final Logger logger = getLogger(RequestProcessor.class);
	
	private final AFhirConstructorService fhirConstructorService;
	
	private final IFhirResourceDaoService fhirDaoService;
	
	private final FInsuranceServiceFactory insuranceImplFactory;
	
	private final BahmniOpenmrsApiClientServiceImpl bahmniOpenmrsService;
	
	private final IParser FhirParser = FhirContext.forDstu3().newJsonParser();
	
	private final AppProperties properties;
	
	@Autowired
	public RequestProcessor(FhirConstructorServiceImpl fhirConstructorServiceImpl,
	    OpenmrsOdooServiceImpl openmrsOdooServiceImpl, FhirResourceDaoServiceImpl fhirServiceImpl,
	    FInsuranceServiceFactory insuranceImplFactory, RestTemplateFactory restFactory, AppProperties props,
	    BahmniOpenmrsApiClientServiceImpl bahmniOpenmrsService) {
		this.fhirConstructorService = fhirConstructorServiceImpl;
		this.fhirDaoService = fhirServiceImpl;
		this.insuranceImplFactory = insuranceImplFactory;
		this.properties = props;
		this.bahmniOpenmrsService = bahmniOpenmrsService;
	}
	
	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
		return new ResponseEntity<Object>("Access denied ", new HttpHeaders(), HttpStatus.FORBIDDEN);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/capvalidation/{InsureeId}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<List<CapValidation>> getCapValidation(
	        HttpServletResponse response,
	        @PathVariable("InsureeId") String InsureeId)
	        throws RestClientException, URISyntaxException, FHIRException {
	    System.out.println("*****Cap Validation*****");
	    return insuranceImplFactory
	            .getInsuranceServiceImpl(ImisConstants.OPENIMIS_FHIR, properties)
	            .getCapValidation(InsureeId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/check/eligibility/{InsureeId}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<EligibilityResponseModel> getEligibilityResponse(HttpServletResponse response,
	        @PathVariable("InsureeId") String InsureeId)
	        throws DataFormatException, IOException, RestClientException, FHIRException, URISyntaxException {
		System.out.println("******Eligibilty Response******");
		logger.debug("eligibityResponse");
		EligibilityRequest eligReq = fhirConstructorService.constructFhirEligibilityRequest(InsureeId);
		System.out.println("Eligibility Request---->" + eligReq);
		System.out.println("FhirParser EncodeResourseToString:"+ FhirParser.encodeResourceToString(eligReq));
		fhirConstructorService.validateRequest(FhirParser.encodeResourceToString(eligReq));
		//		EligibilityResponseModel eligibilityResponse = insuranceImplFactory
		//				.getInsuranceServiceImpl(ImisConstants.OPENIMIS_FHIR, properties).getDummyEligibilityResponse();
		
		EligibilityResponseModel eligibilityResponse = insuranceImplFactory
		        .getInsuranceServiceImpl(ImisConstants.OPENIMIS_FHIR, properties).getElibilityResponse(eligReq);
		System.out.println("Eligibility Response Model:"+ eligibilityResponse);
		eligibilityResponse.setNhisId(InsureeId);
		return new ResponseEntity<>(eligibilityResponse, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/submit/claim", produces = "application/json")
	@ResponseBody
	public ClaimResponseModel submitClaim(HttpServletResponse response, @RequestBody ClaimParam claimParams)
	        throws RestClientException, URISyntaxException, DataFormatException, IOException {
		System.out.println("Claim Params---->" + claimParams);
		logger.debug("submitClaim : ");
		Claim claimRequest = fhirConstructorService.constructFhirClaimRequest(claimParams);
		logger.error("claimRequest : " + FhirParser.encodeResourceToString(claimRequest));
		String claimReqStr = FhirParser.encodeResourceToString(claimRequest);
		if (properties.saveClaimResource) {
			fhirDaoService.insertFhirResource(claimReqStr, ImisConstants.FHIR_RESOURCE_TYPE.CLAIM.getValue());
		}
		fhirConstructorService.validateRequest(claimReqStr);
		
		
		//Adds newer fields like NMC, careType, claim explanation not present FHIR resources
		String claimReqUpdatedStr = fhirConstructorService.addCustomFieldsClaim(claimReqStr, claimParams);
		
		System.out.println("Claim Request Updated String----->" + claimReqUpdatedStr);
		
		ClaimResponseModel claimResponseModel = insuranceImplFactory
		        .getInsuranceServiceImpl(ImisConstants.OPENIMIS_FHIR, properties).submitClaim(claimReqUpdatedStr);
		logger.debug("claimResponseModel : " + InsuranceUtils.mapToJson(claimResponseModel));
		
		return claimResponseModel;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/refund/claim", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> refundClaim(HttpServletResponse response, @RequestBody RefundParam refundParams) 
			throws RestClientException, URISyntaxException, DataFormatException, IOException {
		System.out.println("Refund Params---->"+ refundParams);
		
		ResponseEntity<String> refundResponse = insuranceImplFactory.
				getInsuranceServiceImpl(ImisConstants.OPENIMIS_FHIR, properties).refundClaim(refundParams);
		return refundResponse;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "get/claim/response/{claimUUID}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<ClaimResponseModel> getClaimResponse(HttpServletResponse response,
	        @PathVariable("claimUUID") String claimUUID) throws IOException {
		return insuranceImplFactory.getInsuranceServiceImpl(ImisConstants.OPENIMIS_FHIR, properties).trackClaim(claimUUID);
	}
	
	@RequestMapping(path = "/openIMIS/login")
	@ResponseBody
	public String checkLogin(HttpServletResponse response) throws RestClientException, URISyntaxException {
		logger.debug("checkLogin");
		return insuranceImplFactory.getInsuranceServiceImpl(ImisConstants.OPENIMIS_FHIR, properties).loginCheck();
	}
	
	@RequestMapping(path = "/get/fhir/claims")
	@ResponseBody
	public List<FhirResourceModel> getFhirClaim() {
		return fhirDaoService.findAll();
	}
	
	@RequestMapping(path = "/get/fhir/claim/id")
	@ResponseBody
	public List<String> getFhirClaimId() {
		return fhirDaoService.getClaimId();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/patient/{name}", produces = "application/json")
	@ResponseBody
	public String retrievePatientByName(HttpServletResponse response, @PathVariable("name") String name) {
		logger.debug("retreivePatient : ");
		return fhirConstructorService.getFhirPatient(name);
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/patient", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> generatePatient(HttpServletResponse response, @RequestBody String personJson) {
		logger.debug("generatePatient : ");
		return fhirConstructorService.createFhirPatient(personJson);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/visit/{visitUUID}", produces = "application/json")
	@ResponseBody
	public VisitSummary getVisitDetails(HttpServletResponse response, @PathVariable("visitUUID") String visitUUID) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("getVisitDetails : ");
		return bahmniOpenmrsService.getVisitDetail(visitUUID);

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/request/authenticate", produces = "application/json")
	@ResponseBody
	public void authenticateOpenIMIS(HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("Authenticated : ");

	}
	
}
