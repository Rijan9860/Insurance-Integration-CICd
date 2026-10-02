package org.bahmni.insurance.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.transaction.NotSupportedException;

import org.bahmni.insurance.utils.InsuranceUtils;/*
import org.openmrs.module.fhir.api.client.ClientHttpEntity;
import org.openmrs.module.fhir.api.client.ClientHttpRequestInterceptor;
import org.openmrs.module.fhir.api.helper.ClientHelper;*/
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import com.google.gson.Gson;

public class RestClientHelper{
}

/*
public class RestClientHelper implements ClientHelper {
	private final Gson defaultJsonParser = InsuranceUtils.createDefaultGson();

	@Override
	public ClientHttpEntity retrieveRequest(String url) throws URISyntaxException {
		return new ClientHttpEntity(HttpMethod.GET, new URI(url));
	}

	@Override
	public ClientHttpEntity createRequest(String url, Object object) throws URISyntaxException {
		return new ClientHttpEntity<String>(convertToFormattedData(object), HttpMethod.POST, new URI(url));
	}

	@Override
	public ClientHttpEntity deleteRequest(String url, String uuid) throws URISyntaxException {
		url += "/" + uuid;
		return new ClientHttpEntity<String>(uuid, HttpMethod.DELETE, new URI(url));
	}

	@Override
	public ClientHttpEntity updateRequest(String url, Object object) throws URISyntaxException {
		return new ClientHttpEntity<String>(convertToFormattedData(object), HttpMethod.POST, new URI(url));
	}

	@Override
	public Class resolveClassByCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientHttpRequestInterceptor> getCustomInterceptors(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HttpMessageConverter<?>> getCustomMessageConverter() {
		return Arrays.asList(new HttpMessageConverter<?>[] { new StringHttpMessageConverter() });

	}

	@Override
	public boolean compareResourceObjects(String category, Object from, Object dest) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object convertToObject(String formattedData, Class<?> clazz) {
		return defaultJsonParser.fromJson(formattedData, clazz);
	}

	@Override
	public String convertToFormattedData(Object object) {
		return defaultJsonParser.toJson(object);
		
		 * if (RestResource.class.isAssignableFrom(object.getClass())) { return
		 * restConverter.convertToJson((RestResource) object); } else if
		 * (SimpleObject.class.isAssignableFrom(object.getClass())) { return
		 * simpleConverter.convertToJson((SimpleObject) object); } else { throw new
		 * UnsupportedOperationException(getNotSupportedClassMsg(object.getClass().
		 * getCanonicalName())); }
		 }

	@Override
	public Object convertToOpenMrsObject(Object object, String category) throws NotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
*/
