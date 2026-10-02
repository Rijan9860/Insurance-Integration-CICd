package org.bahmni.insurance.serviceImpl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.bahmni.insurance.AppProperties;
import org.bahmni.insurance.ImisConstants;
import org.bahmni.insurance.exception.FhirFormatException;
import org.bahmni.insurance.model.ClaimLineItemRequest;
import org.bahmni.insurance.model.ClaimParam;
import org.bahmni.insurance.model.RefundParam;
import org.bahmni.insurance.model.dtos.DiagnosisDto;
import org.bahmni.insurance.model.dtos.InsureeDto;
import org.bahmni.insurance.model.dtos.VisitDto;
import org.bahmni.insurance.model.dtos.ExtensionDto;
import org.bahmni.insurance.model.dtos.InformationDto;
import org.bahmni.insurance.service.AFhirConstructorService;
import org.bahmni.insurance.utils.InsuranceUtils;
import org.bahmni.insurance.validation.FhirInstanceValidator;
import org.bahmni.insurance.web.RequestProcessor;
import org.hl7.fhir.dstu3.model.Claim;
import org.hl7.fhir.dstu3.model.Claim.ItemComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.EligibilityRequest;
import org.hl7.fhir.dstu3.model.EligibilityRequest.EligibilityRequestStatus;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Money;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.Task;
import org.hl7.fhir.dstu3.model.Task.TaskStatus;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

import static org.apache.log4j.Logger.getLogger;

@Component
public class FhirConstructorServiceImpl extends AFhirConstructorService {
	
	private final Logger logger = getLogger(FhirConstructorServiceImpl.class);

	
	@Autowired
	private AppProperties properties;
	
	@Autowired
	private ImisRestClientServiceImpl imisRestClientService;
	
	@Override
	public String getFhirPatient(String name) {
		HttpHeaders headers = createHeaders(properties.openmrsUser, properties.openmrsPassword);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return this.getApiClient()
		        .exchange(properties.openmrsFhirUrl + "?name=" + name, HttpMethod.GET, entity, String.class).getBody();
	}
	
	@Override
	public ResponseEntity<String> createFhirPatient(String patientJson) {
		HttpHeaders headers = createHeaders(properties.openmrsUser, properties.openmrsPassword);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Content-Type", "application/fhir+json;q=1.0, application/json+fhir;q=0.9");
		HttpEntity<String> entity = new HttpEntity<String>(patientJson, headers);
		return this.getApiClient().exchange(properties.openmrsFhirUrl + "/", HttpMethod.POST, entity, String.class);
	}
	
	private HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			
			private static final long serialVersionUID = 1L;
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}
	
	@Override
	public Claim constructFhirClaimRequest(ClaimParam claimParam) throws IOException {
		
		Claim claimReq = new Claim();
			
		// claim number
		List<Identifier> identifierList = new ArrayList<>();
		
		String claimCode = claimParam.getClaimId();

		if (claimCode != null && !claimCode.trim().isEmpty() && !"false".equalsIgnoreCase(claimCode)) {

		    Identifier identifier2 = new Identifier();

		    CodeableConcept codeableConcept2 = new CodeableConcept();
		    Coding code2 = new Coding();

		    code2.setSystem(ImisConstants.FHIR_VALUESET_SYSTEM);
		    code2.setCode(ImisConstants.FHIR_CODE_FOR_IMIS_CLAIM_CODE_TYPE);

		    codeableConcept2.addCoding(code2);

		    identifier2.setType(codeableConcept2);
		    identifier2.setUse(IdentifierUse.USUAL);
		    identifier2.setValue(claimCode);

		    identifierList.add(identifier2);
		}
		
		if (!identifierList.isEmpty()) {
		    claimReq.setIdentifier(identifierList);
		}
		
		// Insuree patient
		Reference patientReference = new Reference();
		
		//Fetch insuree uuid from openimis
		InsureeDto insureeDto = imisRestClientService.getInsureeInfo(claimParam.getInsureeId());
		logger.info("Insurance Dto : " + insureeDto);
		System.out.println("Insurance Dto----->" + insureeDto);

		
		if (insureeDto == null || insureeDto.getUuid() == null || insureeDto.getUuid().isEmpty()) {
			throw new FhirFormatException("Invalid Insuree ID. Insuree information not found");
		}
		
		patientReference.setReference("Patient/" + insureeDto.getUuid());
		claimReq.setPatient(patientReference);
		
		
		
		// BillablePeriod
		Period period = new Period();
		//		VisitSummary visitDetails = bahmniApiService.getVisitDetail(claimParam.getVisitUUID());
		
		VisitDto visitDetails = claimParam.getVisit();
		
		if (visitDetails == null || visitDetails.getStartDate() == null) {
			throw new FhirFormatException("Invalid visit details. Start and end date can't be empty or null.");
		}
		
		period.setStart(new Date(visitDetails.getStartDate()));
		period.setEnd(new Date(visitDetails.getEndDate()));
		claimReq.setBillablePeriod(period);
		claimReq.setCreated(new Date());
		
		//Map Visit Type
		CodeableConcept typeValue = new CodeableConcept();
		if (visitDetails == null || visitDetails.getVisitType() == null || visitDetails.getVisitType().isEmpty()) {
			throw new FhirFormatException("Invalid visit details. Visit Type can't be empty or null.");
		}
		typeValue.setText(visitDetails.getVisitType());
		claimReq.setType(typeValue);
		
//		if (claimParam.getDiagnosis() == null || claimParam.getDiagnosis() == null || claimParam.getDiagnosis().isEmpty()) {
//			throw new FhirFormatException("Invalid Claim payload. No diagnosis is present in the claim.");
//		}
		
		//Map NMC Number
		CodeableConcept nmcNumberVal = new CodeableConcept();
		if (claimParam.getNmc() == null || claimParam.getNmc().isEmpty()) {
//			throw new FhirFormatException("Invalid NMC. NMC number not present.");
			nmcNumberVal.setText("");			
		}
		else {
			nmcNumberVal.setText(claimParam.getNmc());
		}
		//claimReq
		
//		if (claimParam.getDiagnosis() == null || claimParam.getDiagnosis().isEmpty()) {
//			throw new FhirFormatException("Invalid Claim payload. No diagnosis is present in the claim.");
//		}
		
		// Map Diagnosis 
		int counter = 0;
		
		if (claimParam.getDiagnosis() == null || claimParam.getDiagnosis().isEmpty()) {
				Claim.DiagnosisComponent diagnosisComponent = new Claim.DiagnosisComponent();
				CodeableConcept concept = new CodeableConcept();
				Coding code = new Coding(); 
				code.setCode(""); //1A00
				concept.addCoding(code);
				diagnosisComponent.setDiagnosis(concept);
				diagnosisComponent.setSequence(1);
				
				CodeableConcept conceptType = new CodeableConcept();
				conceptType.setText("icd_0"); //TODO: remove hardcoded
				diagnosisComponent.addType(conceptType);
				
				//Add diagnosis from claim param
				claimReq.addDiagnosis(diagnosisComponent);
			}
			
		else {
			for (DiagnosisDto diagnosisDto : claimParam.getDiagnosis()) {
				Claim.DiagnosisComponent diagnosisComponent = new Claim.DiagnosisComponent();
				CodeableConcept concept = new CodeableConcept();
				Coding code = new Coding();
				code.setCode(diagnosisDto.getIcdCode());
				concept.addCoding(code);
				diagnosisComponent.setDiagnosis(concept);
				
				diagnosisComponent.setSequence(1);
				
				CodeableConcept conceptType = new CodeableConcept();
				conceptType.setText("icd_"+counter); //TODO: remove hardcoded
				diagnosisComponent.addType(conceptType);
				
				//Add diagnosis from claim param
				claimReq.addDiagnosis(diagnosisComponent);
				counter++;
			}
			
		}
		
//		for (DiagnosisDto diagnosisDto : claimParam.getDiagnosis()) {
//			Claim.DiagnosisComponent diagnosisComponent = new Claim.DiagnosisComponent();
//			CodeableConcept concept = new CodeableConcept();
//			Coding code = new Coding();
//			code.setCode(diagnosisDto.getIcdCode());
//			concept.addCoding(code);
//			diagnosisComponent.setDiagnosis(concept);
//			
//			diagnosisComponent.setSequence(1);

//			diagnosisComponent.setSequence(diagnosisDto.getSequence());
		
//			CodeableConcept conceptType = new CodeableConcept();
//			conceptType.setText("icd_0"); //TODO: remove hardcoded
//			diagnosisComponent.addType(conceptType);
//			
//			//Add diagnosis from claim param
//			claimReq.addDiagnosis(diagnosisComponent);
//		}
//		
		if (claimParam.getItem() == null || claimParam.getItem().isEmpty()) {
			throw new FhirFormatException("Empty items. No items to claim.");
		}
		
		// Items/services for claims
		List<ItemComponent> listItemComponent = populateClaimableItems(claimParam.getItem());
		claimReq.setItem(listItemComponent);
		
		// "enterer"
		Reference entererReference = new Reference();
		entererReference.setReference("Practitioner/" + properties.openImisEntererId);
		claimReq.setEnterer(entererReference);
		
		// "Facility"
		Reference facilityReference = new Reference();
		facilityReference.setReference("Location/" + properties.openImisHFCode);
		claimReq.setFacility(facilityReference);
		claimReq.setId(claimParam.getClaimId());
		
		Money total = new Money();
		total.setValue(claimParam.getTotal());
		claimReq.setTotal(total);
		
		// Critical Illness
		if (claimParam.getExtension() != null && !claimParam.getExtension().isEmpty()) {
			System.out.println("Critical Illness");
			for (ExtensionDto extDto : claimParam.getExtension()) {
				Extension extension = new Extension();
				extension.setUrl(ImisConstants.FHIR_PRODUCT);
		        extension.setValue(new StringType(extDto.getCode()));
				claimReq.addExtension(extension);
			}
		}
		else {
			System.out.println("Critical Illness Code is Empty");
		}
				
		return claimReq;
	}
	
	@Override
	public String addCustomFieldsClaim(String claimRequest, ClaimParam claimParam)
	        throws JsonParseException, JsonMappingException, IOException {
		// Parse JSON string to ObjectNode
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode jsonNode = objectMapper.readValue(claimRequest, ObjectNode.class);
		
		// Add custom fields in JSON
//		if (claimParam.getNmc() == null || claimParam.getNmc().isEmpty()) {
//			throw new FhirFormatException("Invalid NMC. NMC number not present.");
//		}
		jsonNode.put("nmc", claimParam.getNmc());
		
		if (claimParam.getCareType() == null || claimParam.getCareType().isEmpty()) {
			throw new FhirFormatException("Care Type is invalid. Caretype is empty or null");
		}
		jsonNode.put("careType", claimParam.getCareType());
		
		// Claim Explanation
	    if (claimParam.getInformation() != null && !claimParam.getInformation().isEmpty()) {

	        ArrayNode informationArray = objectMapper.createArrayNode();

	        for (InformationDto infoDto : claimParam.getInformation()) {

	            ObjectNode informationNode = objectMapper.createObjectNode();

	            // category
	            ObjectNode categoryNode = objectMapper.createObjectNode();
	            categoryNode.put("text", infoDto.getCategory());

	            informationNode.set("category", categoryNode);

	            // sequence
	            if (infoDto.getSequence() != null) {
	                informationNode.put("sequence", infoDto.getSequence());
	            }

	            // valueString
	            if (infoDto.getValueString() != null) {
	                informationNode.put("valueString", infoDto.getValueString());
	            }

	            informationArray.add(informationNode);
	        }

	        jsonNode.set("information", informationArray);
	    }
		
		return objectMapper.writeValueAsString(jsonNode);
		
	}
	
	private List<ItemComponent> populateClaimableItems(List<ClaimLineItemRequest> listItem) {
		List<ItemComponent> listItemComponent = new ArrayList<>();
		for (ClaimLineItemRequest claimItem : listItem) {
			ItemComponent itemComponent = new ItemComponent();
			itemComponent.setSequence(claimItem.getSequence());
			
			CodeableConcept codeConceptCategory = new CodeableConcept();
			codeConceptCategory.setText(claimItem.getCategory());
			itemComponent.setCategory(codeConceptCategory);
			
			SimpleQuantity simpleQuantity = new SimpleQuantity();
			simpleQuantity.setValue(claimItem.getQuantity());
			itemComponent.setQuantity(simpleQuantity);
			
			CodeableConcept codeConceptService = new CodeableConcept();
			codeConceptService.setText(claimItem.getCode());
			itemComponent.setService(codeConceptService);
			
			Money value = new Money();
			value.setValue(claimItem.getUnitPrice());
			itemComponent.setUnitPrice(value);
			listItemComponent.add(itemComponent);
		}
		return listItemComponent;
	}
	
	@Override
	public EligibilityRequest constructFhirEligibilityRequest(String insuranceID) throws IOException {
		
		EligibilityRequest eligibilityRequest = new EligibilityRequest();
		
		List<Identifier> identifierList = new ArrayList<>();
		Identifier identifier = new Identifier();
		identifier.setSystem("SenderID");
		identifier.setValue(insuranceID);
		identifierList.add(identifier);
		eligibilityRequest.setIdentifier(identifierList);
		
		eligibilityRequest.setStatus(EligibilityRequestStatus.ACTIVE);
		
		Reference patientReference = new Reference();
		patientReference.setReference("Patient/" + insuranceID);
		eligibilityRequest.setPatient(patientReference);
		
		Reference referenceOrg = new Reference();
		referenceOrg.setReference("Organization/1");
		eligibilityRequest.setOrganization(referenceOrg);
		
		Reference referenceInsurer = new Reference();
		referenceInsurer.setReference("Organization/2");
		eligibilityRequest.setInsurer(referenceInsurer);
		
		return eligibilityRequest;
	}
	
	@Override
	public Task constructFhirClaimTrackRequest(String insuranceID) throws IOException {
		Task claimTracking = new Task();
		
		List<Identifier> identifierList = new ArrayList<>();
		Identifier identifier = new Identifier();
		identifier.setSystem("SenderID");
		identifier.setValue(insuranceID);
		identifierList.add(identifier);
		claimTracking.setIdentifier(identifierList);
		
		claimTracking.setStatus(TaskStatus.READY);
		
		Reference referenceOrg = new Reference();
		referenceOrg.setReference("Organization/1");
		claimTracking.setOwner(referenceOrg);
		
		return claimTracking;
	}
	
	@Override
	public boolean validateRequest(String eligibilityRequestValidation) throws IOException {
		FhirContext ctx = FhirContext.forDstu3();
		
		FhirValidator validator = ctx.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator();
		validator.registerValidatorModule(instanceValidator);
		instanceValidator.setAnyExtensionsAllowed(true);
		
		ValidationResult result = validator.validateWithResult(eligibilityRequestValidation);
		
		if (!result.isSuccessful()) {
			String errorMsg = "";
			for (SingleValidationMessage next : result.getMessages()) {
				errorMsg = next.getSeverity() + " - " + next.getLocationString() + " - " + next.getMessage();
			}
			
			throw new FhirFormatException(errorMsg);
		}
		
		return result.isSuccessful();
	}

	
}
