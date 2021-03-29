package com.techelevator.vendingmachineclasses;

public class MysterySnack extends VendingMachineItem {

	public MysterySnack(String name, String price) {
		super(name, price);
		
	}

	@Override
	public String makeNoise() {
		return "OMG WHAT IS THIS";
	}
	

}
