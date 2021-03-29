package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
    
	private Long transferId;
    private String transferType;
    private String transferStatus;
    private User userFrom;
    private User userTo;
    private BigDecimal amount;
    
	public Long getTransferId() {
		return transferId;
	}
	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public String getTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
	public User getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(User userFrom) {
		this.userFrom = userFrom;
	}
	public User getUserTo() {
		return userTo;
	}
	public void setUserTo(User userTo) {
		this.userTo = userTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public boolean isPending() {
		return TransferStatus.PENDING.equals(this.transferStatus);
	}
}
