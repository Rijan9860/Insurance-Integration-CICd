package org.bahmni.insurance.validation;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;

public class FhirInstanceValidator
implements IValidatorModule{

	@Override
	public void validateResource(IValidationContext<IBaseResource> theCtx) {
		// TODO Auto-generated method stub
		
	}

	public void setAnyExtensionsAllowed(boolean b) {
		// TODO Auto-generated method stub
		
	}

	

}
