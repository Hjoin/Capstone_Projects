package com.techelevator.vendingmachineclasses;

public class Candy extends VendingMachineItem {

	public Candy(String name, String price) {
		super(name, price);
		
	}

	@Override
	public String makeNoise() {
		return "Munch munch, Yum!";
	}
	

}
