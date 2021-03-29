package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferRequestDTO;
import com.techelevator.tenmo.models.TransferStatus;

public class TransferService {

	private final String BASE_SERVICE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	public TransferService(String baseUrl) {
		this.BASE_SERVICE_URL = baseUrl + "transfers/";
	}
	
	public Transfer createTransfer(TransferRequestDTO transfer, String authToken) {
		HttpEntity<TransferRequestDTO> entity = new HttpEntity<>(transfer, authHeaders(authToken));
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_SERVICE_URL, HttpMethod.POST, entity, Transfer.class);
		return response.getBody();
	}

	public Transfer approvePendingTransfer(Integer transferId, String authToken) {
		TransferStatusUpdateDTO dto = new TransferStatusUpdateDTO(TransferStatus.APPROVED);
		HttpEntity<TransferStatusUpdateDTO> entity = new HttpEntity<>(dto, authHeaders(authToken));
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_SERVICE_URL+"/"+transferId, HttpMethod.PUT, entity, Transfer.class);
		return response.getBody();
	}

	public Transfer rejectPendingTransfer(Integer transferId, String authToken) {
		TransferStatusUpdateDTO dto = new TransferStatusUpdateDTO(TransferStatus.REJECTED);
		HttpEntity<TransferStatusUpdateDTO> entity = new HttpEntity<>(dto, authHeaders(authToken));
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_SERVICE_URL+"/"+transferId, HttpMethod.PUT, entity, Transfer.class);
		return response.getBody();
	}

	public Transfer retrieveTransferDetails(Integer transferId, String authToken) {
		HttpEntity<?> entity = new HttpEntity<>(authHeaders(authToken));
		ResponseEntity<Transfer> response = restTemplate.exchange(BASE_SERVICE_URL+"/"+transferId, HttpMethod.GET, entity, Transfer.class);
		return response.getBody();
	}
	
	private HttpHeaders authHeaders(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		return headers;
	}
	
	private static class TransferStatusUpdateDTO {
		private String transferStatus;
		
		public TransferStatusUpdateDTO(String transferStatus) {
			if(TransferStatus.isValid(transferStatus)) {
				this.transferStatus = transferStatus;
			} else {
				throw new IllegalArgumentException("Invalid transferStatus: "+transferStatus);
			}
		}
		
		@SuppressWarnings("unused")
		public String getTransferStatus() {
			return transferStatus;
		}
	}
}

