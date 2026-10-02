package org.bahmni.insurance.client;

import static org.apache.log4j.Logger.getLogger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.bahmni.insurance.AppProperties;
import org.bahmni.insurance.ImisConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactory {

	private AppProperties properties;

	public RestTemplateFactory(AppProperties prop) {
		properties = prop;
	}

	RestTemplate restTemplate = new RestTemplate();

	private final Logger logger = getLogger(RestTemplateFactory.class);

	public RestTemplate getRestTemplate(int clientType) {

		HttpComponentsClientHttpRequestFactory requestFactory = getClientHttpRequestFactory(clientType);

		//requestFactory.setConnectTimeout(3000);
		//requestFactory.setReadTimeout(3000);

		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}

	private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(int clientType) {

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(getHtpClient(clientType));
		return clientHttpRequestFactory;
	}

	private HttpClient getHtpClient(int clientType) {
		Collection<Header> defaultHeaders = new ArrayList<Header>();
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

		try {
			if (clientType == ImisConstants.OPENIMIS_FHIR) {
				Header header = new BasicHeader("Authorization",
						"Basic " + Base64.encodeBase64(properties.imisUser.getBytes("UTF-8")));
				defaultHeaders.add(header);
				
				credentialsProvider.setCredentials(AuthScope.ANY,
						new UsernamePasswordCredentials(properties.imisUser, properties.imisPassword));
			} else if (clientType == ImisConstants.OPENMRS_FHIR) {
				Header header = new BasicHeader("Authorization",
						"Basic " + Base64.encodeBase64(properties.imisUser.getBytes("UTF-8")));
				defaultHeaders.add(header);
				credentialsProvider.setCredentials(AuthScope.ANY,
						new UsernamePasswordCredentials(properties.openmrsUser, properties.openmrsPassword));
			} else if (clientType == ImisConstants.OPENMRS_ODOO) {
				Header header = new BasicHeader("Authorization",
						"Basic " + Base64.encodeBase64(properties.imisUser.getBytes("UTF-8")));
				defaultHeaders.add(header);
				credentialsProvider.setCredentials(AuthScope.ANY,
						new UsernamePasswordCredentials(properties.imisConnectUser, properties.imisPassword));

			} else if (clientType == 100) {
				Header headers = new BasicHeader("Accept",
						"application/fhir+xml;q=1.0, application/fhir+json;q=1.0, application/xml+fhir;q=0.9, application/json+fhir;q=0.9");
				defaultHeaders.add(headers);

			}

		} catch (UnsupportedEncodingException e) {
			logger.debug(e);
		}

		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider)
				.setDefaultHeaders(defaultHeaders).build(); // .setSSLContext(context)

		return httpClient;
	}

}