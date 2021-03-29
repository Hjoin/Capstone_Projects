package com.techelevator.VendingMachineItem.DAO;

import java.util.List;
import java.util.Map;

import com.techelevator.vendingmachineclasses.VendingMachineItem;

public interface VendingMachineItemDAO {
	 Map<String, List<VendingMachineItem>> getAllVendingMachineItems();
	
	 //as a user buys an item, we update the file
	 
	 //after a transaction is finished, we update the file

}
