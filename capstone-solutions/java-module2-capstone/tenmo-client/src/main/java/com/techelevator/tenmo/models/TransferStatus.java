package com.techelevator.tenmo.models;

public final class TransferStatus {

	public static final String PENDING = "Pending";
	public static final String APPROVED = "Approved";
	public static final String REJECTED = "Rejected";
		
	private TransferStatus() { } //private constructor prevents this class from being instantiated

	public static boolean isValid(String transferStatus) {
		return PENDING.equals(transferStatus) || APPROVED.equals(transferStatus) || REJECTED.equals(transferStatus);
	}
	
}
