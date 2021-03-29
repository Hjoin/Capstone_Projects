package com.techelevator;

import com.techelevator.vendingmachineclasses.VendingMachine;
import com.techelevator.vendingmachineclasses.VendingMachineItem;
import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT };
	private static final String PURCHASE_MENU_OPTION_FEED = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_PURCHASE = "Purchase Item";
	private static final String PURCHASE_MENU_OPTION_DONE = "Complete Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED, PURCHASE_MENU_OPTION_PURCHASE,
			PURCHASE_MENU_OPTION_DONE };

	private static final String PURCHASE_MENU_FEED_PROMPT = "Enter amount to deposit (0 to cancel): ";
	private static final String PURCHASE_MENU_FEED_ERROR = "You can only deposit whole dollar amounts up to $20.";
	private static final int PURCHASE_MENU_FEED_MAX = 20;
	private static final String PURCHASE_MENU_PURCHASE_PROMPT = "Please select an item to purchase: ";
	private static final String PURCHASE_MENU_PURCHASE_ERROR = "Invalid entry. Please enter a valid slot: ";
	private static final String EXIT_MESSAGE = "Thanks for your business";

	private Menu menu;
	private VendingMachine vendo = new VendingMachine(); // loads inventory in constructor

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				DisplayPurchaseItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				DisplayPurchaseMenu();
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.out.println(EXIT_MESSAGE);
				break;
			}
		}
	}

	private void DisplayPurchaseMenu() {
		while (true) {

			printBalance();

			String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

			if (choice.equals(PURCHASE_MENU_OPTION_FEED)) {
				vendo.deposit(menu.getPositiveInt(PURCHASE_MENU_FEED_PROMPT, PURCHASE_MENU_FEED_ERROR,
						PURCHASE_MENU_FEED_MAX));
			} else if (choice.equals(PURCHASE_MENU_OPTION_PURCHASE)) {
				// based on the user story, we should boot them here if the balance is zero
				if (vendo.hasMoney()) {
					DisplayPurchaseItems(); // show the items again
					String slotChoice = menu.getChoice(PURCHASE_MENU_PURCHASE_PROMPT, PURCHASE_MENU_PURCHASE_ERROR,
							vendo.getSlotList());
					System.out.println(vendo.makePurchase(slotChoice));
				} else {
					System.err.println(VendingMachine.NOT_ENOUGH_MONEY_MSG);
				}

			} else if (choice.equals(PURCHASE_MENU_OPTION_DONE)) {
				printBalance();
				System.out.println(vendo.returnChange());
				printBalance();
				break;
			}
		}

	}

	/**
	 * 
	 */
	private void printBalance() {
		System.out.println("\nYour balance is $" + vendo.getBalance());
	}

	private void DisplayPurchaseItems() {
		// i am figuring this out here because i think it's ui
		System.out.format("%-5s %-20s %-5s\n", "Slot", "Item", "Price");
		for (String slot : vendo.getSlotList()) {
			VendingMachineItem item = vendo.getItemAtSlot(slot);

			if (item != null) {
				System.out.format("%-5s %-20s %-5.2f\n", slot, item.getName(), item.getPrice());
			} else {
				System.out.format("%-5s %s\n", slot, "SOLD OUT");
			}

		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
