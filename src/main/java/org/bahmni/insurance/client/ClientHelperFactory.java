package org.bahmni.insurance.client;

import org.bahmni.insurance.ImisConstants;/*
import org.openmrs.module.fhir.api.helper.ClientHelper;
import org.openmrs.module.fhir.api.helper.FHIRClientHelper;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHelperFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientHelperFactory.class);

/*	public static ClientHelper createClient(final String clientType) {
		switch (clientType) {
		case ImisConstants.REST_CLIENT:
			return new RestClientHelper();
		case ImisConstants.FHIR_CLIENT:
			return new FHIRClientHelper();
		default:
			LOGGER.warn(String.format("Unrecognized clientType: %s. The REST Client will be used.", clientType));
			return new FHIRClientHelper();
		}
	}*/
}
