package com.techelevator.vendingmachineclasses;

import java.math.BigDecimal;

public abstract class VendingMachineItem {
	private String name;
	private BigDecimal price;
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public VendingMachineItem(String name, String price) {
		this.name = name;
		try {
			this.price = new BigDecimal(price);
		}
		catch(Exception e) {
			this.price = BigDecimal.ZERO; //if we can't parse the price it's free;
		}
	}
	
	public abstract String makeNoise();
	


}
