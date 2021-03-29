package com.techelevator.tenmo.models;

public class TransferRequestDTO {

	private Integer userFrom;
	private Integer userTo;
	private int amount;
	private String transferType;
	
	public TransferRequestDTO() {
	}
	
	public TransferRequestDTO(Integer fromUserId, Integer toUserId, int amount, String transferType) {
		validateTransferType(transferType);
		this.userFrom = fromUserId;
		this.userTo = toUserId;
		this.amount = amount;
		this.transferType = transferType;
	}

	public Integer getUserFrom() {
		return userFrom;
	}
	
	public void setUserFrom(Integer userFrom) {
		this.userFrom = userFrom;
	}

	public Integer getUserTo() {
		return userTo;
	}

	public void setUserTo(Integer userTo) {
		this.userTo = userTo;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getTransferType() {
		return transferType;
	}
	
	public void setTransferType(String transferType) {
		validateTransferType(transferType);
		this.transferType = transferType;
	}

	private void validateTransferType(String transferType) {
		if(!TransferType.isValid(transferType)) {
			throw new IllegalArgumentException(transferType+" is not a valid transferType");
		}
	}
}
