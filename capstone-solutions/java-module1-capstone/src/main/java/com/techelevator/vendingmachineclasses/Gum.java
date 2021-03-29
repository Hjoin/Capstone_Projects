package com.techelevator.vendingmachineclasses;

public class Gum extends VendingMachineItem {

	public Gum(String name, String price) {
		super(name, price);
		
	}

	@Override
	public String makeNoise() {
		return "Chew Chew, Yum!";
	}
	

}
