package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.User;

public class UserService {
	private final String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	public UserService(String baseUrl) {
		this.BASE_URL = baseUrl;
	}

	public User[] retrieveAllUsers(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity entity = new HttpEntity(headers);
		ResponseEntity<User[]> response = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, entity, User[].class);
		return response.getBody();
	}
}
