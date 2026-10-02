package org.bahmni.insurance.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface IApiClientService {
	
	public ResponseEntity<String> sendPostRequest(String requestJson, String url);
	public String sendGetRequest(String url);
	public HttpHeaders getAuthHeaders();

}
