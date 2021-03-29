package com.techelevator.tenmo.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;

public class TransferStatusUpdateDTO {

	@NotEmpty
	private String transferStatus;

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
	
	@AssertTrue
	public boolean isValidStatus() {
		return Transfer.TRANSFER_STATUS_APPROVED.equals(transferStatus) || Transfer.TRANSFER_STATUS_REJECTED.equals(transferStatus);
	}
}
