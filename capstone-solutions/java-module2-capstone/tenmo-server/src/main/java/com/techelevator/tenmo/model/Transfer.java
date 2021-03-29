package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Transfer {

    private Long transferId;
    private String transferType;
    private String transferStatus;
    private User userFrom;
    private User userTo;
    private BigDecimal amount;

    public static final String TRANSFER_TYPE_REQUEST = "Request";
    public static final String TRANSFER_TYPE_SEND = "Send";
    public static final String TRANSFER_STATUS_PENDING = "Pending";
    public static final String TRANSFER_STATUS_APPROVED = "Approved";
    public static final String TRANSFER_STATUS_REJECTED = "Rejected";
    
    public Transfer(String transferType, User userFrom, User userTo, BigDecimal amount) {
    	this(null, transferType, getInitialStatusForTransferType(transferType), userFrom, userTo, amount);
    }
    
    public Transfer(Long transferId, String transferType, String transferStatus, User userFrom, User userTo, BigDecimal amount) {
    	this.transferId = transferId;
    	this.transferType = transferType;
    	this.transferStatus = transferStatus;
    	this.userFrom = userFrom;
    	this.userTo = userTo;
    	this.amount = amount;
    	validateTransferType();
    	validateTransferStatus();
    }

	public Long getTransferId() {
        return transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }
    
    public User getUserFrom() {
    	return userFrom;
    }
    
    public User getUserTo() {
    	return userTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

	public boolean isApproved() {
		return TRANSFER_STATUS_APPROVED.equals(this.transferStatus);
	}
	
	public boolean isRejected() {
		return TRANSFER_STATUS_REJECTED.equals(this.transferStatus);
	}
	
	public boolean isPending() {
		return TRANSFER_STATUS_PENDING.equals(this.transferStatus);
	}
	
	public boolean isRequestType() {
		return TRANSFER_TYPE_REQUEST.equals(this.transferType);
	}
	
	public boolean isSendType() {
		return TRANSFER_TYPE_SEND.equals(this.transferType);
	}

	public void approve() throws InvalidTransferStatusUpdateException {
		if(isPending()) {
			// only pending transfers can be approved
			transferStatus = TRANSFER_STATUS_APPROVED;
		} else {
			throw new InvalidTransferStatusUpdateException(transferStatus, TRANSFER_STATUS_APPROVED);
		}
	}

	public void reject() {
		if(isPending()) {
			// only pending transfers can be rejected
			transferStatus = TRANSFER_STATUS_REJECTED;
		} else {
			throw new InvalidTransferStatusUpdateException(transferStatus, TRANSFER_STATUS_REJECTED);
		}
	}

    private void validateTransferType() {
    	if(!(TRANSFER_TYPE_REQUEST.equals(transferType) || TRANSFER_TYPE_SEND.equals(transferType))) {
    		throw new IllegalArgumentException(transferType + " is not a valid transferType");
    	}
    }

    private void validateTransferStatus() {
    	if(!(TRANSFER_STATUS_APPROVED.equals(transferStatus) || TRANSFER_STATUS_PENDING.equals(transferStatus) || TRANSFER_STATUS_REJECTED.equals(transferStatus))) {
    		throw new IllegalArgumentException(transferStatus + " is not a valid transferStatus");
    	}
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(transferId, transfer.transferId) &&
                Objects.equals(transferType, transfer.transferType) &&
                transferStatus == transfer.transferStatus &&
                Objects.equals(userFrom, transfer.userFrom) &&
                Objects.equals(userTo, transfer.userTo) &&
                Objects.equals(amount, transfer.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, transferType, transferStatus, userFrom, userTo, amount);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferType=" + transferType +
                ", transferStatus=" + transferStatus +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                ", amount=" + amount +
                '}';
    }
    
	private static String getInitialStatusForTransferType(String transferType) {
		String transferStatus = null;
    	if(Transfer.TRANSFER_TYPE_REQUEST.equals(transferType)) {
    		transferStatus = Transfer.TRANSFER_STATUS_PENDING;
    	} else if(Transfer.TRANSFER_TYPE_SEND.equals(transferType)) {
    		transferStatus = Transfer.TRANSFER_STATUS_APPROVED;
    	}
		return transferStatus;
	}
}
