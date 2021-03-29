package com.techelevator.vendingmachineclasses;

public class Chip extends VendingMachineItem {

	public Chip(String name, String price) {
		super(name, price);
		
	}

	@Override
	public String makeNoise() {
		return "Crunch Crunch, Yum!";
	}
	

}
