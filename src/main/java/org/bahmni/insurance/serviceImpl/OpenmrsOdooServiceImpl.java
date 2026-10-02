package org.bahmni.insurance.serviceImpl;

import java.util.Arrays;

import org.bahmni.insurance.ImisConstants;
import org.bahmni.insurance.AppProperties;
import org.bahmni.insurance.client.RestTemplateFactory;
import org.bahmni.insurance.service.IOpenmrsOdooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Configurable
public class OpenmrsOdooServiceImpl implements IOpenmrsOdooService {

	private AppProperties properties;

	@Autowired
	public OpenmrsOdooServiceImpl(AppProperties properties) {
		this.properties = properties;
	}

	public AppProperties getProperties() {
		return properties;
	}

	public RestTemplate getApiClient() {
		RestTemplateFactory restFactory = new RestTemplateFactory(properties);
		return restFactory.getRestTemplate(ImisConstants.OPENMRS_ODOO);
	}

	@Override
	public String getOrderCost(String patientId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return this.getApiClient()
				.exchange(this.getProperties().openmrsOdooApi + patientId, HttpMethod.GET, entity, String.class)
				.getBody();
	}

}
