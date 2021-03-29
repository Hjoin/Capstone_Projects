package com.techelevator.VendingMachineItem.DAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.techelevator.vendingmachineclasses.Candy;
import com.techelevator.vendingmachineclasses.Chip;
import com.techelevator.vendingmachineclasses.Drink;
import com.techelevator.vendingmachineclasses.Gum;
import com.techelevator.vendingmachineclasses.MysterySnack;
import com.techelevator.vendingmachineclasses.VendingMachineItem;

public class FileVendingMachineItemDAO implements VendingMachineItemDAO {
	
	private String fileName;
	private int inventoryStartCount; 

	public FileVendingMachineItemDAO(String fileName, int inventoryStartCount) {
		this.fileName = fileName;
		this.inventoryStartCount = inventoryStartCount;
	}
	
	@Override
	public Map<String, List<VendingMachineItem>> getAllVendingMachineItems() {
		Map<String, List<VendingMachineItem>> inventory = new TreeMap<>();
		try {
			File file = new File(fileName);
			Scanner readFile = new Scanner(file);
			while (readFile.hasNext()) { // for every line
				String line = readFile.nextLine();
				String[] lineAr = line.split("\\|");
				VendingMachineItem addMe = null;

				switch (lineAr[3]) {
				case "Chip":
					addMe = new Chip(lineAr[1], lineAr[2]);
					break;
				case "Candy":
					addMe = new Candy(lineAr[1], lineAr[2]);
					break;
				case "Drink":
					addMe = new Drink(lineAr[1], lineAr[2]);
					break;
				case "Gum":
					addMe = new Gum(lineAr[1], lineAr[2]);
					break;
				default:
					addMe = new MysterySnack(lineAr[1], lineAr[2]);
					break;
				}
				// now we want to add 5 of the items to the inventory at the appropriate slot
				addItemToSlot(addMe, lineAr[0],inventory);
			}
		} catch (Exception e) {
			// nothing to do, there's just no inventory
			System.out.println("unable to load inventory: " + e.getMessage());
		}
		return inventory;
	}
	
	private void addItemToSlot(VendingMachineItem item, String slot, Map<String, List<VendingMachineItem>> inventory) {
		List<VendingMachineItem> slotList = new ArrayList<>();
		for (int i = 0; i < inventoryStartCount; i++) {
			slotList.add(item);
		}
		inventory.put(slot, slotList);
	}

}
