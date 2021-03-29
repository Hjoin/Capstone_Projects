package com.techelevator.vendingmachineclasses;

public class Drink extends VendingMachineItem {

	public Drink(String name, String price) {
		super(name, price);
		
	}

	@Override
	public String makeNoise() {
		return "Glug Glug, Yum!";
	}
	

}
