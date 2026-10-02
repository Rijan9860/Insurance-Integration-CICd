package org.bahmni.insurance.web;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.bahmni.insurance.AppProperties;
import org.bahmni.insurance.client.RestTemplateFactory;
import org.bahmni.insurance.dao.FhirResourceDaoServiceImpl;
import org.bahmni.insurance.model.EligibilityResponseModel;
import org.bahmni.insurance.model.Insurance;
import org.bahmni.insurance.service.FInsuranceServiceFactory;
import org.bahmni.insurance.serviceImpl.FhirConstructorServiceImpl;
import org.bahmni.insurance.serviceImpl.OpenmrsOdooServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InsuranceController {

	private final FInsuranceServiceFactory insuranceImplFactory;
	private final AppProperties properties;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@Autowired
	public InsuranceController(FhirConstructorServiceImpl fhirConstructorServiceImpl,
			OpenmrsOdooServiceImpl openmrsOdooServiceImpl, FhirResourceDaoServiceImpl fhirServiceImpl,
			FInsuranceServiceFactory insuranceImplFactory, RestTemplateFactory restFactory, AppProperties props) {
		this.insuranceImplFactory = insuranceImplFactory;
		this.properties = props;

		// this.restFactory = restFactory;
	}

	@RequestMapping(value = "/add-info", method = RequestMethod.GET)
	public String addInfo(Insurance insurance) {
		return "add-info";
	}

//	@RequestMapping(value = "/add-info", method = RequestMethod.POST)
//	public String showWelcomePage(@ModelAttribute @Valid Insurance insurance, BindingResult bindingResult, Model model,
//			@RequestParam String nhisNumber, @RequestParam Boolean isMember) {
//		if (bindingResult.hasErrors()) {
//			System.out.println("BINDING RESULT ERROR");
//			model.addAttribute("error", bindingResult.getAllErrors());
//			return "error-page";
//		} else {
//			EligibilityResponseModel eligibilityResponse = insuranceImplFactory.getInsuranceServiceImpl(100, properties)
//					.getDummyEligibilityResponse();
//			// String nhisId = eligibilityResponse.getNhisId();
//			String patientId = eligibilityResponse.getPatientId();
//			String status = eligibilityResponse.getStatus();
//			BigDecimal benefitBalance = eligibilityResponse.getEligibilityBalance().get(0).getBenefitBalance();
//			String code = eligibilityResponse.getEligibilityBalance().get(0).getCode();
//			String term = eligibilityResponse.getEligibilityBalance().get(0).getTerm();
//
//			model.addAttribute("patientId", patientId);
//			model.addAttribute("status", status);
//			model.addAttribute("benefitBalance", benefitBalance);
//			model.addAttribute("code", code);
//			model.addAttribute("term", term);
//			model.addAttribute("insurance", insurance);
//			model.addAttribute("nhisNumber", insurance.getNhisNumber());
//			model.addAttribute("isMember", isMember);
//
//			return "index";
//
//		}
//	}

}
