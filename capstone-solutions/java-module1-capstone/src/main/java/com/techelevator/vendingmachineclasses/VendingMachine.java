package com.techelevator.vendingmachineclasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.techelevator.VendingMachineItem.DAO.VendingMachineItemDAO;
import com.techelevator.VendingMachineItem.DAO.FileVendingMachineItemDAO;

public class VendingMachine {

	private String inventoryFileName = "VendingMachine.txt";
	public final static int INVENTORY_START_COUNT = 5;

	//These are static so that it's easy to change the message in one place AND so that my unit tests can use them
	public final static String SOLD_OUT_MSG = "SOLD OUT";
	public final static String NOT_ENOUGH_MONEY_MSG = "Not enough $. Please make a deposit to proceed";
	private static final String LOG_FILE_NAME = "Log.txt";
	private static final String GIVE_CHANGE = "GIVE CHANGE";
	private Map<Integer,String> changeValues = new LinkedHashMap(); //LinkedHashMap so that they stay in teh order we put them in!

	private BigDecimal balance;
	private Map<String, List<VendingMachineItem>> inventory = new TreeMap<>();
	private BufferedWriter auditFile;
	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
	
	private VendingMachineItemDAO vmiDAO = new FileVendingMachineItemDAO(inventoryFileName,INVENTORY_START_COUNT);

	public VendingMachine() {
		loadInventory();		
		balance = BigDecimal.ZERO;		
		clearAuditLog(); 
		initChangeValues();		
	}

	public String getBalance() {
		return balance.toString();
	}

	private void loadInventory() {
		inventory = vmiDAO.getAllVendingMachineItems();
	}

	

	public Collection<String> getSlotList() { // collection is SUPER generic
		return inventory.keySet();
	}

	public VendingMachineItem getItemAtSlot(String slot) {
		if (isValidSlot(slot) && inventory.get(slot).size() > 0) {
			return inventory.get(slot).get(0); // there's no setters on the item so i don't care if the user has a
												// reference to it
		}
		return null;
	}

	// returns true if the slot (not case sensitive) is valid, false otherwise
	private boolean isValidSlot(String slot) {
		return inventory.containsKey(slot.toUpperCase());
	}

	public void deposit(int amount) {
		balance = balance.add(new BigDecimal(amount));
		updateAuditLog("FEED MONEY", amount);
	}



	public boolean hasMoney() {
		return balance.compareTo(BigDecimal.ZERO) > 0;
	}

	private boolean canPurchase(String slotChoice) {
		// return notSoldOut && hasEnoughMoney
		return inventory.get(slotChoice).size() > 0
				&& balance.compareTo(inventory.get(slotChoice).get(0).getPrice()) > 0;
	}

	private String getCantPurchaseMessage(String slotChoice) {
		if (inventory.get(slotChoice).size() == 0)
			return SOLD_OUT_MSG;
		else
			return NOT_ENOUGH_MONEY_MSG;
	}

	public String makePurchase(String slotChoice) {
		slotChoice = slotChoice.toUpperCase();
	
		if (canPurchase(slotChoice)) { // if it's allowed
			VendingMachineItem purchaseItem = inventory.get(slotChoice).get(0);
			
			// update the balance
			balance = balance.subtract(purchaseItem.getPrice());
			//log it
			updateAuditLog(slotChoice+" "+purchaseItem.getName(), purchaseItem.getPrice().doubleValue());
			
			// remove the item and return the noise it makes
			return inventory.get(slotChoice).remove(0).makeNoise();
		} else {
			return getCantPurchaseMessage(slotChoice);
		}
		
	}

	public String returnChange() {
			
		//original balance for logging
		double orig_Balance = balance.doubleValue();		
		
		StringBuilder msg = new StringBuilder ("Your change is ");
		
		int changeInPennies = balance.multiply(new BigDecimal(100)).intValue();
		//so we'll go through and add the dollars, then the quarters, then the dimes, then the nickels
		for(Integer value : changeValues.keySet()) {
			changeInPennies  = appendForAmount(value, changeValues.get(value),changeInPennies, msg);
		}			
			
		// we've given all the change so the balance should be zero
		balance = BigDecimal.ZERO;
		
		//log it AFTER so that the balance is zero
		updateAuditLog(GIVE_CHANGE,orig_Balance);
		
		return msg.toString();
	}
	
	private int appendForAmount(Integer value, String valueText, int currentAmount, StringBuilder msg) {
		
		int count = currentAmount / value;
		if (count > 0) {
			msg.append(count);
			if (count>1)
				valueText+="s"; //if there's 1 its dollar, if there's 2 its dollars
			msg.append(" "+valueText+" ");
		}
		return currentAmount % value;
	}
		

	/**
	 * 
	 */
	private void clearAuditLog() {
		//clear the log from previous runs by open for overwrite an then close
		try (BufferedWriter auditFile = new BufferedWriter(new FileWriter(LOG_FILE_NAME, false) )){
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param amount
	 */
	private void updateAuditLog(String activity, double amount) {
		try (BufferedWriter auditFile = new BufferedWriter(new FileWriter(LOG_FILE_NAME, true) )){
			
			Date now = new Date(System.currentTimeMillis());
			String output = String.format("%-20s %-20s $%-5.2f $%-5.2f\n", dateFormatter.format(now), activity, amount,
					balance);
			auditFile.append(output);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void initChangeValues() {
		changeValues.put(100,"dollar");
		changeValues.put(25,"quarter");
		changeValues.put(10,"dime");
		changeValues.put(5,"nickel");
	}


}
