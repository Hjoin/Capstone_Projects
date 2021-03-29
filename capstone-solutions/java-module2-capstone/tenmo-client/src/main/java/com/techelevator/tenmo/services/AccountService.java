package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferRequestDTO;
import com.techelevator.tenmo.models.TransferRequestDTO;

public class AccountService {
	
	private final String BASE_SERVICE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	public AccountService(String baseUrl) {
		this.BASE_SERVICE_URL = baseUrl + "account/";
	}

	public BigDecimal getBalance(String authToken) {
		HttpEntity<?> entity = new HttpEntity<>(authHeaders(authToken));
		ResponseEntity<BigDecimal> response = restTemplate.exchange(BASE_SERVICE_URL + "balance", HttpMethod.GET, entity, BigDecimal.class);
		return response.getBody();
	}

	public Transfer[] retrieveAllTransfers(String authToken) {
		HttpEntity<?> entity = new HttpEntity<>(authHeaders(authToken));
		ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_SERVICE_URL+"transfers", HttpMethod.GET, entity, Transfer[].class);
		return response.getBody();
	}
	
	private HttpHeaders authHeaders(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		return headers;
	}
}
