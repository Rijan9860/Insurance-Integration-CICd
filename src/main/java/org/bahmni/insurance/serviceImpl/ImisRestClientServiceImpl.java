package org.bahmni.insurance.serviceImpl;

import static org.apache.log4j.Logger.getLogger;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.bahmni.insurance.AppProperties;
import org.bahmni.insurance.ImisConstants;
import org.bahmni.insurance.client.RestTemplateFactory;
import org.bahmni.insurance.model.ClaimLineItemResponse;
import org.bahmni.insurance.model.ClaimResponseModel;
import org.bahmni.insurance.model.ClaimTrackingModel;
import org.bahmni.insurance.model.EligibilityBalance;
import org.bahmni.insurance.model.EligibilityResponseModel;
import org.bahmni.insurance.model.RefundParam;
import org.bahmni.insurance.model.CapValidation;
import org.bahmni.insurance.model.dtos.EligibilityResponseDto;
import org.bahmni.insurance.model.dtos.InsureeDto;
import org.bahmni.insurance.service.AInsuranceClientService;
import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.ClaimResponse;
import org.hl7.fhir.dstu3.model.ClaimResponse.AdjudicationComponent;
import org.hl7.fhir.dstu3.model.ClaimResponse.ItemComponent;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.EligibilityRequest;
import org.hl7.fhir.dstu3.model.EligibilityResponse;
import org.hl7.fhir.dstu3.model.EligibilityResponse.BenefitComponent;
import org.hl7.fhir.dstu3.model.EligibilityResponse.BenefitsComponent;
import org.hl7.fhir.dstu3.model.EligibilityResponse.InsuranceComponent;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Task;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.exceptions.FHIRException;
/*
import org.openmrs.module.fhir.api.client.ClientHttpEntity;
import org.openmrs.module.fhir.api.helper.ClientHelper;*/
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component
public class ImisRestClientServiceImpl extends AInsuranceClientService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	private final IParser FhirParser = FhirContext.forDstu3().newJsonParser();
	
	private final org.apache.log4j.Logger logger = getLogger(ImisRestClientServiceImpl.class);
	
	private AppProperties properties;
	
	public ImisRestClientServiceImpl(AppProperties prop) {
		properties = prop;
		// restTemplate = getRestClient();
	}
	
	public RestTemplate getRestClient() {
		RestTemplateFactory restFactory = new RestTemplateFactory(properties);
		return restFactory.getRestTemplate(ImisConstants.OPENIMIS_FHIR);
	}
	
	private HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			
			private static final long serialVersionUID = 1L;
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
				set("remote-user", properties.openImisRemoteUser);
			}
		};
	}
	
	private ResponseEntity<String> sendPostRequest(String requestJson, String url) {
		
		HttpHeaders headers = createHeaders(properties.imisUser, properties.imisPassword);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Content-Type", "application/json");
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		
	}
	
	private ResponseEntity<String> sendGetRequest(String url) {
		HttpHeaders headers = createHeaders(properties.imisUser, properties.imisPassword);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	}
	
	@Override
	public ClaimResponseModel submitClaim(Claim claimRequest) {
		String jsonClaimRequest = FhirParser.encodeResourceToString(claimRequest);
		ResponseEntity<String> responseObject = sendPostRequest(jsonClaimRequest, properties.openImisFhirApiClaim);
		ClaimResponse claimResponse = (ClaimResponse) FhirParser.parseResource(responseObject.getBody());
		System.out.println("ClaimResponse : " + FhirParser.encodeResourceToString(claimResponse));
		return populateClaimRespModel(claimResponse);
	}
	
	@Override
	public ClaimResponseModel submitClaim(String jsonClaimRequest) {
		ResponseEntity<String> responseObject = sendPostRequest(jsonClaimRequest, properties.openImisFhirApiClaim);
		System.out.println(responseObject.getBody());
		logger.info(responseObject.getBody());
		ClaimResponse claimResponse = (ClaimResponse) FhirParser.parseResource(responseObject.getBody());
		System.out.println("ClaimResponse : " + FhirParser.encodeResourceToString(claimResponse));
		return populateClaimRespModel(claimResponse);
	}
	
	@Override
	public ResponseEntity<String> refundClaim(RefundParam refundParams) {
		JsonObject json = new JsonObject();
		Gson gson = new Gson();
		
		json.addProperty("claim_code", refundParams.getClaimId());
		json.addProperty("type", refundParams.getType());
		json.add("codes", gson.toJsonTree(refundParams.getCodes())); //Convert the Java List<string> into a json array
		
		String jsonRefundRequest = json.toString();
		System.out.println("Json Refund Request:" + jsonRefundRequest);
		try {
			ResponseEntity<String> refundResponse = sendPostRequest(jsonRefundRequest, properties.openImisFhirApiRefund);
			System.out.println("Refund Response:" + refundResponse.getBody());
			return refundResponse;
		}
		catch (RestClientException e){
			System.out.println("Error Calling API:" + e.getMessage());
			return null;
		}
	}
		
	@Override
	public EligibilityResponseModel getElibilityResponse(EligibilityRequest eligbilityRequest)
	        throws RestClientException, URISyntaxException, FHIRException {
		String jsonEligRequest = FhirParser.encodeResourceToString(eligbilityRequest);
		ResponseEntity<String> responseObject = sendPostRequest(jsonEligRequest,
		    properties.imisUrl + "/EligibilityRequest/");
		logger.info(responseObject);
		System.out.println("Response Object---->" + responseObject);
		EligibilityResponseDto eligibilityResponseDto = null;
		try {
			// Parse the JSON response body into an object of EligibilityResponseDto class
			ObjectMapper objectMapper = new ObjectMapper();
			eligibilityResponseDto = objectMapper.readValue(responseObject.getBody(), EligibilityResponseDto.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		EligibilityResponse eligibilityResponse = (EligibilityResponse) FhirParser.parseResource(EligibilityResponse.class,
		    responseObject.getBody());		
		return populateEligibilityRespModel(eligibilityResponse, eligibilityResponseDto);
	}
	
	@Override
	public ClaimResponse getClaimStatus(Task claimStatusRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//	@Override
	//	public ClaimResponseModel getDummyClaimResponse(Claim claimRequest) {
	//		String claimResponseBody = sendGetRequest(properties.dummyClaimResponseUrl);
	//		ClaimResponse dummyClaimResponse = (ClaimResponse) FhirParser.parseResource(claimResponseBody);
	//		String jsonClaimRequest = FhirParser.encodeResourceToString(claimRequest);
	//		logger.debug("jsonClaimRequest ==> " + jsonClaimRequest);
	//		return populateClaimRespModel(dummyClaimResponse);
	//		
	//	}	
	private ClaimResponseModel populateClaimRespModel(ClaimResponse claimResponse) {
		ClaimResponseModel clmRespModel = new ClaimResponseModel();
		
		clmRespModel.setClaimStatus(claimResponse.getOutcome().getText());
		clmRespModel.setClaimId(claimResponse.getId());
		clmRespModel.setClaimUUID(claimResponse.getIdentifier().get(0).getValue());
		
		// Extract MR identifier and store as generatedClaimCode
	    for (Identifier identifier : claimResponse.getIdentifier()) {
	        if (identifier.hasType()) {
	            for (Coding coding : identifier.getType().getCoding()) {
	                if ("MR".equals(coding.getCode())) {
	                    clmRespModel.setGeneratedClaimCode(identifier.getValue());
	                    break;
	                }
	            }
	        }
	    }
		
		if (ImisConstants.CLAIM_OUTCOME.REJECTED.getOutCome().equals(claimResponse.getOutcome().getText())) {
			clmRespModel.setApprovedTotal(claimResponse.getTotalBenefit().getValue());
			clmRespModel.setDateProcessed(claimResponse.getPayment().getDate());
		} else if (ImisConstants.CLAIM_OUTCOME.VALUATED.getOutCome().equals(claimResponse.getOutcome().getText())) {
			clmRespModel.setDateProcessed(claimResponse.getPayment().getDate());
		}
		
		List<ClaimLineItemResponse> claimLineItems = new ArrayList<>();
		for (ItemComponent responseItem : claimResponse.getItem()) {
			ClaimLineItemResponse claimItem = new ClaimLineItemResponse();
			claimItem.setSequence(responseItem.getSequenceLinkIdElement().getValue());
			
			for (AdjudicationComponent adj : responseItem.getAdjudication()) {
				if (ImisConstants.CLAIM_ADJ_CATEGORY.GENERAL.equals(adj.getCategory().getText())) {
					claimItem.setStatus(adj.getReason().getText());
					claimItem.setQuantityApproved(adj.getValue());
					if (ImisConstants.CLAIM_ITEM_STATUS.PASSED.equalsIgnoreCase(adj.getReason().getText())) {
						claimItem.setTotalApproved(adj.getAmount().getValue());
					}
				}
				if (ImisConstants.CLAIM_ADJ_CATEGORY.REJECTED_REASON.equals(adj.getCategory().getText())) {
					claimItem.setRejectedReason(ImisConstants.ERROR_CODE_TO_TEXT_MAP
					        .get(Integer.parseInt(adj.getReason().getCoding().get(0).getCode())));
				}
			}
			claimItem.setSequence(responseItem.getSequenceLinkId());
			claimLineItems.add(claimItem);
			
		}
		
		if (ImisConstants.CLAIM_OUTCOME.VALUATED.getOutCome().equals(claimResponse.getOutcome().getText())
		        && claimResponse.getTotalBenefit().getValue() != null) {
			clmRespModel.setApprovedTotal(claimResponse.getTotalBenefit().getValue());
		} else {
			clmRespModel.setApprovedTotal(BigDecimal.ZERO);
		}
		
		clmRespModel.setClaimLineItems(claimLineItems);
		return clmRespModel;
	}
	
	//	@Override
	//	public EligibilityResponseModel getDummyEligibilityResponse() throws FHIRException {
	//		String eligibilityResponseBody = sendGetRequest(properties.dummyEligibiltyResponseUrl);
	//		EligibilityResponse dummyEligibiltyResponse = (EligibilityResponse) FhirParser
	//		        .parseResource(eligibilityResponseBody);
	//		return populateEligibilityRespModel(dummyEligibiltyResponse);
	//	}
	
	private EligibilityResponseModel populateEligibilityRespModel(EligibilityResponse eligibilityResponse,
	        EligibilityResponseDto eligibilityResponseDto) throws FHIRException {
		logger.info(eligibilityResponse);
		EligibilityResponseModel eligRespModel = new EligibilityResponseModel();
		eligRespModel.setNhisId(eligibilityResponse.getId());
		eligRespModel.setPatientId(eligibilityResponse.getId());
		
		List<EligibilityBalance> eligibilityBalance = new ArrayList<>();
		for (InsuranceComponent responseItem : eligibilityResponse.getInsurance()) {
			List<BenefitsComponent> benefitsBalance = responseItem.getBenefitBalance();
			
			if (benefitsBalance != null && benefitsBalance.size() >= 1) {
				for (BenefitsComponent benefitsComponent: benefitsBalance) {
					EligibilityBalance eligBalance = new EligibilityBalance();
					
					//Map Category
					eligBalance.setCategory(benefitsComponent.getCategory().getText());
					
					//Map validity Period
					// Response text:: "Contract/KIDDS/2026-02-12 00:00:00"
					String[] split_code = responseItem.getContract().getReference().split("/");
					
					//Extract the code part
					String code = split_code[1];
					
					eligBalance.setCode(code);
									
					String[] parts = responseItem.getContract().getReference().split("[/\\s]");
					
					// Extract the date part
					String dateString = parts[2];
					
					// Parse the date string into a Date object
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date date = null;
					try {
						date = dateFormat.parse(dateString);
					}
					catch (ParseException e) {
						e.printStackTrace();
					}
					eligBalance.setValidDate(date);				
					
					List<Coding> codings = benefitsComponent.getTerm().getCoding();
					
					//Map Benefit Code
					if (codings != null && codings.size() >= 1) {
						eligBalance.setCode(codings.get(0).getCode());
					}
					
					List<BenefitComponent> benefitComponent = benefitsComponent.getFinancial();
					
					if (benefitComponent != null && benefitComponent.size() >= 1) {
						BenefitComponent benefitComp = benefitComponent.get(0);
						
						List<Coding> benefitCompCodings = benefitComp.getType().getCoding();
						
						//Map Benefit Term
						if (benefitCompCodings != null && benefitCompCodings.size() >= 1) {
							eligBalance.setTerm(benefitCompCodings.get(0).getCode());
						}
						
						//Map Benefit Balance
						eligBalance.setBenefitBalance(benefitComp.getAllowedMoney().getValue());
						
						//Map Used Balance
						eligBalance.setUsedBalance(benefitComp.getUsedMoney().getValue());
					}
					eligibilityBalance.add(eligBalance);
				}	
			}
			
			
			//Parse copayment: Currently present inside first object of extension
			//			List<Extension> extensions = responseItem.getExtension();
			//						for (Extension extension : extensions) {
			//							String url = extension.getUrl();
			//							// Get the URL of the extension
			//							if ("https://hib.gov.np/fhir/FHIE+extension+Copayment".equals(url)
			//							        || "http://hib.gov.np/fhir/FHIE+extension+Copayment".equals(url)) {
			//								
			//								if (extension.getValue() instanceof DecimalType) {
			//									DecimalType decimalValue = (DecimalType) extension.getValue();
			//									// Access the decimal value
			//									BigDecimal decimal = decimalValue.getValue();
			//									eligRespModel.setCoPaymentValue(decimal.doubleValue());
			//									System.out.println("Decimal Value: " + decimal);
			//								} else if (extension.getValue() instanceof StringType) {
			//									StringType stringValue = (StringType) extension.getValue();
			//									// Access the string value
			//									String value = stringValue.getValue();
			//									System.out.println("String Value: " + value);
			//									eligRespModel.setCoPaymentValue(0);
			//								}				
			//								
			//							}
			//							
			//						}
			
			if (eligibilityResponseDto != null && !eligibilityResponseDto.getInsurance().isEmpty()
			        && !eligibilityResponseDto.getInsurance().get(0).getExtension().isEmpty()) {
				for (org.bahmni.insurance.model.dtos.Extension extension : eligibilityResponseDto.getInsurance().get(0)
				        .getExtension()) {
					String url = extension.getUrl();
					if ("https://hib.gov.np/fhir/FHIE+extension+Copayment".equals(url)
					        || "http://hib.gov.np/fhir/FHIE+extension+Copayment".equals(url)) {
						eligRespModel.setCoPaymentValue(extension.getValueDecimal());
						
					} else {
						eligRespModel.setCoPaymentValue(0);
					}
				}
			} else {
				eligRespModel.setCoPaymentValue(0);
			}
			
			//Map profilePicture, Hospital and District
			List<Extension> extensions = eligibilityResponse.getExtension();
			for (Extension extension : extensions) {
				String url = extension.getUrl();
				StringType stringValue = (StringType) extension.getValue();
				
				// Get the URL of the extension
				if ("http://hib.gov.np/fhir/FHIE+extension+Profile+Photo+Url".equals(url)
				        || "https://hib.gov.np/fhir/FHIE+extension+Profile+Photo+Url".equals(url)) {			
					if (extension.getValue() instanceof StringType) {
						eligRespModel.setProfilePicture(stringValue.getValue());
					}		
				} else if ("http://hib.gov.np/fhir/FHIE+extension+Profile+FSP".equals(url)
				        || "https://hib.gov.np/fhir/FHIE+extension+Profile+FSP".equals(url)) {
					
					if (extension.getValue() instanceof StringType) {
						eligRespModel.setHospital(stringValue.getValue());
					}		
				} else if ("http://hib.gov.np/fhir/FHIE+extension+Profile+District".equals(url)
				        || "https://hib.gov.np/fhir/FHIE+extension+Profile+District".equals(url)) {
					
					if (extension.getValue() instanceof StringType) {
						eligRespModel.setDistrict(stringValue.getValue());
					}		
				}
				
			}
			
			// nhisId
			// patientId
			// Balance
			// CoPayment
		}
		
		eligRespModel.setEligibilityBalance(eligibilityBalance);
		return eligRespModel;
	}
	
	//	@Override
	//	public ClaimTrackingModel getDummyClaimTrack() {
	//		String claimTrackingSample = sendGetRequest(properties.dummyClaimTrackUrl);
	//		Task dummyClaimTrack = (Task) FhirParser.parseResource(claimTrackingSample);
	//		return populateClaimTrackModel(dummyClaimTrack);
	//	}
	
	private ClaimTrackingModel populateClaimTrackModel(Task task) {
		ClaimTrackingModel clmTrackModel = new ClaimTrackingModel();
		clmTrackModel.setClaimId(task.getId());
		clmTrackModel.setClaimOwner(task.getOwner().getDisplay());
		clmTrackModel.setClaimStatus(task.getStatus().toString());
		clmTrackModel.setClaimDesc(task.getDescription());
		clmTrackModel.setClaimSignature(task.getRelevantHistory().get(0).getDisplay());
		clmTrackModel.setDateProcessed(task.getExecutionPeriod().getStart());
		clmTrackModel.setDateAuthorized(task.getAuthoredOn());
		clmTrackModel.setDateLastModified(task.getLastModified());
		return clmTrackModel;
	}
	
	@Override
	public InsureeDto getInsureeInfo(String insureeId) {
	    String response = this.sendGetRequest(this.properties.imisUrl + "Patient/?identifier=" + insureeId).getBody();
	    System.out.println("Original Response ----------> " + response);

	    // Fix incorrect gender casing (case-insensitive)
	    response = response.replaceAll("(?i)\"gender\"\\s*:\\s*\"male\"", "\"gender\":\"male\"")
	                       .replaceAll("(?i)\"gender\"\\s*:\\s*\"female\"", "\"gender\":\"female\"")
	                       .replaceAll("(?i)\"gender\"\\s*:\\s*\"other\"", "\"gender\":\"other\"");

	    System.out.println("Fixed Response ----------> " + response);

	    // Parse to DTO
	    InsureeDto insureeDto = InsureeDto.fromBundleResponse(response);
	    return insureeDto;
	}

	
	@Override
	public String loginCheck() {
		return sendGetRequest(properties.imisUrl).getBody();
	}
	
	@Override
	public ResponseEntity<ClaimResponseModel> trackClaim(String claimUuid) {
		ResponseEntity<String> claimResponseBody = this
		        .sendGetRequest(this.properties.imisUrl + "ClaimResponse/" + claimUuid);
		
		if (claimResponseBody.getStatusCode().is2xxSuccessful()) {
			ClaimResponse claimResponse = (ClaimResponse) FhirParser.parseResource(claimResponseBody.getBody());
			ClaimResponseModel claimResponseModel = populateClaimRespModel(claimResponse);
			return new ResponseEntity<>(claimResponseModel, HttpStatus.OK);
		} else if (claimResponseBody.getStatusCode().is5xxServerError()) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	//Cap Validation
	@Override
	public ResponseEntity<List<CapValidation>> getCapValidation(String insureeId)
	        throws RestClientException, URISyntaxException, FHIRException {

	    System.out.println("Overriding Cap Validation Function");
	    System.out.println("Insuree Id: " + insureeId);

	    // Send GET request and get JSON response string
	    String response = this.sendGetRequest(this.properties.imisUrl + "/cap-validation?CHFID=" + insureeId).getBody();
	    System.out.println("Cap Validation Response ----------> " + response);

	    // Parse JSON into List<CapValidation>
	    List<CapValidation> capValidationList = parseCapValidationJson(response);

	    // Optionally set NHIS ID for all records
	    for (CapValidation capValidation : capValidationList) {
	        capValidation.setNhisId(insureeId);
	    }

	    return new ResponseEntity<>(capValidationList, HttpStatus.OK);
	}

	
	public List<CapValidation> parseCapValidationJson(String bundleJson) {
	    List<CapValidation> capValidations = new ArrayList<>();

	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode root = mapper.readTree(bundleJson);
	        System.out.println("Json Root---->" + root);

	        // If your JSON is a pure array, use root directly
	        JsonNode entries = root.isArray() ? root : root.path("entries");
	        System.out.println("Json Entries---->" + entries);

	        if (entries.isArray()) {
	            for (JsonNode entry : entries) {
	                CapValidation capValidation = new CapValidation();
	                capValidation.setCode(entry.path("Code").asText(null));
	                capValidation.setName(entry.path("Name").asText(null));
	                capValidation.setItemServ(entry.path("itemserv").asText(null));
	                capValidation.setCapQtyPeroid(entry.path("CapQtyPeriod").isNull() ? null : entry.path("CapQtyPeriod").asInt());
	                capValidation.setCapQrstPeroid(entry.path("CapQrstPeriod").isNull() ? null : entry.path("CapQrstPeriod").asInt());
	                capValidation.setQtyUsed(entry.path("QtyUsed").isNull() ? null : (float) entry.path("QtyUsed").asDouble());
	                capValidation.setQtyRemain(entry.path("QtyRemain").isNull() ? null : (float) entry.path("QtyRemain").asDouble());
	                capValidations.add(capValidation);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return capValidations;
	}
	
}
