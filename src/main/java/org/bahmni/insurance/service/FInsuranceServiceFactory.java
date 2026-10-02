package org.bahmni.insurance.service;

import org.bahmni.insurance.AppProperties;
import org.bahmni.insurance.ImisConstants;
import org.bahmni.insurance.serviceImpl.ImisRestClientServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FInsuranceServiceFactory {
	
	public AInsuranceClientService getInsuranceServiceImpl(int type, AppProperties properties) {
		if (ImisConstants.OPENIMIS_FHIR == type) {
			return new ImisRestClientServiceImpl(properties);
		} else {
			return new ImisRestClientServiceImpl(properties); // TODO: if further any other insurance is to be integrated
		}
		// new ImisRestClientServiceImpl(properties);
	}

}
