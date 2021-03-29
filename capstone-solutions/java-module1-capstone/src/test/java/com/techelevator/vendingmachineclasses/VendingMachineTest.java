package com.techelevator.vendingmachineclasses;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VendingMachineTest {
	private VendingMachine vm;

	@Before
	public void setup() {
		vm = new VendingMachine();
	}

	@Test
	public void testInit() {
		try {
			// I googled 'how to test private fields' to see how to do this ;-)
			Field f = VendingMachine.class.getDeclaredField("inventory");
			f.setAccessible(true); // let me have access to this private field

			// get teh private field for vm and cast it to the right thing
			Map<String, List<VendingMachineItem>> inventory = (Map<String, List<VendingMachineItem>>) f.get(vm);

			Assert.assertTrue("No inventory!", inventory.size() > 0);
			;

			// now map that has some keys and the correct starting number of inventory items
			for (String key : inventory.keySet()) {

				Assert.assertEquals(VendingMachine.INVENTORY_START_COUNT, inventory.get(key).size());
			}
		} catch (Exception e) {
			Assert.fail("where's my inventory?");
			e.printStackTrace();
		}
	}

	@Test
	public void soldOutReturnsNull() {
		Field f;
		try {
			f = VendingMachine.class.getDeclaredField("inventory");
			f.setAccessible(true); // let me have access to this private field

			// get teh private field for vm and cast it to the right thing
			Map<String, List<VendingMachineItem>> inventory = (Map<String, List<VendingMachineItem>>) f.get(vm);

			for (String key : inventory.keySet()) {
				// remove all the items
				List<VendingMachineItem> itemList = inventory.get(key);
				while (itemList.size() > 0) {
					itemList.remove(0);
				}
				Assert.assertEquals("getItemAtSlot returns null for empty list", null, vm.getItemAtSlot(key));
			}

		} catch (Exception e) {
			Assert.fail("where's my inventory?");
			e.printStackTrace();
		}
	}

	@Test
	public void testDeposit() {
		Assert.assertFalse("Has $ before deposit", vm.hasMoney());
		vm.deposit(5);
		Assert.assertTrue("Should have $ after deposit", vm.hasMoney());
	}

	@Test
	public void testCantPurchaseSoldOut() {
		String slot = "A1";
		vm.deposit(20);
		for (int i = 0; i < 5; i++) {
			vm.makePurchase("A1");
		}
		// get the balance
		double currentBalance = Double.parseDouble(vm.getBalance());

		// this should fail
		String result = vm.makePurchase(slot);
		Assert.assertEquals(VendingMachine.SOLD_OUT_MSG, result);

		// and the balance shouldnt have chagned
		Assert.assertEquals(currentBalance, Double.parseDouble(vm.getBalance()), .001);
	}

	@Test
	public void testCantPurchaseNotEnoughMoney() {
		String slot = "A1";
		
		//the default is balance of 0

		// this should fail
		String result = vm.makePurchase(slot);
		Assert.assertEquals(VendingMachine.NOT_ENOUGH_MONEY_MSG, result);

		// and the balance shouldnt have chagned
		Assert.assertEquals("0", vm.getBalance());
	}

	@Test
	public void testPurchase() {

		String slot = "a1"; // this test found a bug!
		vm.deposit(20);

		// get the balance
		BigDecimal balance = new BigDecimal(20); //we just deposited this above
		
		String result = vm.makePurchase(slot);
		
		Field f;
		try {
			f = VendingMachine.class.getDeclaredField("inventory");
			f.setAccessible(true); // let me have access to this private field
			
			Map<String, List<VendingMachineItem>> inventory = (Map<String, List<VendingMachineItem>>) f.get(vm);
			
			//make sure that the noise that came back was correct
			Assert.assertEquals(inventory.get(slot.toUpperCase()).get(0).makeNoise(), result);
			balance = balance.subtract( inventory.get(slot.toUpperCase()).get(0).getPrice());
			Assert.assertEquals("Balance is wrong!",balance.toString(), vm.getBalance());
	    }
		catch (Exception e) {
			Assert.fail();//uh oh
		}
	}
}
