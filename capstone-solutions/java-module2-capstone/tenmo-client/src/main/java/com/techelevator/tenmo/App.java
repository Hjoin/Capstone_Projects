package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferRequestDTO;
import com.techelevator.tenmo.models.TransferType;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String APPROVE_REJECT_MENU_OPTION_APPROVE = "Approve";
	private static final String APPROVE_REJECT_MENU_OPTION_REJECT = "Reject";
	private static final String APPROVE_REJECT_MENU_OPTION_CANCEL = "Don't approve or reject";
	private static final String[] APPROVE_REJECT_MENU_OPTIONS = { APPROVE_REJECT_MENU_OPTION_APPROVE, APPROVE_REJECT_MENU_OPTION_REJECT, APPROVE_REJECT_MENU_OPTION_CANCEL};
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private UserService userService;
    private TransferService transferService;

    public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), 
						  new AuthenticationService(API_BASE_URL),
					  	  new AccountService(API_BASE_URL),
					  	  new UserService(API_BASE_URL),
					  	  new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, 
    		   AuthenticationService authenticationService, 
    		   AccountService accountService, 
    		   UserService userService,
    		   TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.userService = userService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			try {
				String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
				if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
					viewCurrentBalance();
				} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
					viewTransferHistory();
				} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
					viewPendingRequests();
				} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
					sendBucks();
				} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
					requestBucks();
				} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
					login();
				} else {
					// the only other option on the main menu is to exit
					exitProgram();
				}
			} catch(RestClientResponseException e) {
				displayError("Unable to complete the requested operation.\n\n"+e.getResponseBodyAsString());
			} catch(RestClientException e) {
				displayError("A communication error occurred\n\n"+e.getMessage());
			} catch(Exception e) {
				displayError("An unanticipated error occurred.\n\n"+e.getMessage()+"\n\nShutting down...");
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		BigDecimal balance = accountService.getBalance(currentUser.getToken());
		System.out.println("Your current account balance is: " + balance);
	}

	private void viewTransferHistory() {
		displayAllTransfers();
		Integer selectedTransferId = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
		if(selectedTransferId != 0) {
			Transfer transfer = transferService.retrieveTransferDetails(selectedTransferId, currentUser.getToken());
			displayTransferDetails(transfer);
		} else {
			System.out.println("Canceling...");
		}
	}

	private void viewPendingRequests() {
		displayPendingRequestTransfers();
		Integer selectedTransferId = console.getUserInputInteger("Please enter transfer ID to approve/reject (0 to cancel)");
		if(selectedTransferId != 0) {
			String choice = (String)console.getChoiceFromOptions(APPROVE_REJECT_MENU_OPTIONS);
			if(APPROVE_REJECT_MENU_OPTION_APPROVE.equals(choice)) {
				Transfer transfer = transferService.approvePendingTransfer(selectedTransferId, currentUser.getToken());
				System.out.println("Approved transfer of "+transfer.getAmount()+" TE Bucks to "+transfer.getUserTo().getUsername());
			} else if(APPROVE_REJECT_MENU_OPTION_REJECT.equals(choice)) {
				Transfer transfer = transferService.rejectPendingTransfer(selectedTransferId, currentUser.getToken());
				System.out.println("Rejected transfer of "+transfer.getAmount()+" TE Bucks to "+transfer.getUserTo().getUsername());
			}
		} else {
			System.out.println("Canceling...");
		}
	}

	private void sendBucks() {
		displayUsers();
		Integer toUserId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
		if(toUserId != 0) {
			int amount = console.getUserInputInteger("Enter amount");
			Integer fromUserId = currentUser.getUser().getId();
			TransferRequestDTO transfer = new TransferRequestDTO(fromUserId, toUserId, amount, TransferType.SEND);
			transferService.createTransfer(transfer, currentUser.getToken());
			System.out.println(amount+" TE Bucks were sent to user "+toUserId);
		} else {
			System.out.println("Canceling transfer...");
		}
	}

	private void requestBucks() {
		displayUsers();
		Integer fromUserId = console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");
		if(fromUserId != 0) {
			int amount = console.getUserInputInteger("Enter amount");
			Integer toUserId = currentUser.getUser().getId();
			TransferRequestDTO transfer = new TransferRequestDTO(fromUserId, toUserId, amount, TransferType.REQUEST);
			transferService.createTransfer(transfer, currentUser.getToken());
			System.out.println(amount+" TE Bucks were requested from user "+fromUserId);
		} else {
			System.out.println("Canceling transfer...");
		}
	}

	private void displayUsers() {
		User[] users = userService.retrieveAllUsers(currentUser.getToken());
		displayUsersHeader();
		for(User u : users) {
			System.out.printf("%-10s%s%n",u.getId(),u.getUsername());
		}
		printHR(10);
		System.out.println();
	}

	private void displayUsersHeader() {
		printHR(20);
		System.out.println("Users");
		System.out.printf("%-10s%s%n","ID","Name");
		printHR(20);
	}
	
	private void displayPendingRequestTransfers() {
		Transfer[] transfers = getPendingTransfersFromCurrentUser();
		displayPendingTransfersHeader();
		for(Transfer t : transfers) {
			System.out.printf("%-10s%-23s%s%n",t.getTransferId(),t.getUserTo().getUsername(),t.getAmount());
		}
		printHR(10);
		System.out.println();
	}

	private void displayPendingTransfersHeader() {
		printHR(45);
		System.out.println("Pending Transfers");
		System.out.printf("%-10s%-23s%s%n","ID","To","Amount");
		printHR(45);
	}

	private Transfer[] getPendingTransfersFromCurrentUser() {
		ArrayList<Transfer> pendingRequestedTransfers = new ArrayList<>();
		Transfer[] allTransfers = accountService.retrieveAllTransfers(currentUser.getToken());
		for(Transfer transfer : allTransfers) {
			if(transfer.isPending() && isCurrentUser(transfer.getUserFrom())) {
				pendingRequestedTransfers.add(transfer);
			}
		}
		return pendingRequestedTransfers.toArray(new Transfer[0]);
	}
	
	private void displayAllTransfers() {
		Transfer[] transfers = accountService.retrieveAllTransfers(currentUser.getToken());
		displayAllTransfersHeader();
		for(Transfer t : transfers) {
			String toFromString = buildToFromString(t);
			System.out.printf("%-10s%-23s%s%n",t.getTransferId(),toFromString,t.getAmount());
		}
		printHR(10);
		System.out.println();
	}
	
	private String buildToFromString(Transfer transfer) {
		String toFromString = null;
		if(isCurrentUser(transfer.getUserFrom())) {
			toFromString = "To: "+transfer.getUserTo().getUsername();
		} else {
			toFromString = "From: "+transfer.getUserFrom().getUsername();
		}
		return toFromString;
	}

	private void displayAllTransfersHeader() {
		printHR(45);
		System.out.println("Transfers");
		System.out.printf("%-10s%-23s%s%n","ID","From/To","Amount");
		printHR(45);
	}

	private void displayTransferDetails(Transfer transfer) {
		printHR(20);
		System.out.println("Transfer Details");
		printHR(20);
		System.out.println("Id: "+transfer.getTransferId());
		System.out.println("From: "+transfer.getUserFrom().getUsername());
		System.out.println("To: "+transfer.getUserTo().getUsername());
		System.out.println("Type: "+transfer.getTransferType());
		System.out.println("Status: "+transfer.getTransferStatus());
		System.out.println("Amount: "+transfer.getAmount());
	}
	
	private boolean isCurrentUser(User user) {
		return user.getUsername().equals(currentUser.getUser().getUsername());
	}
	
	private void printHR(int length) {
		for(int i=0; i<length; i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private void displayError(String message) {
		System.out.println();
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("ERROR:");
		System.out.println();
		System.out.println(message);
		System.out.println();
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println();
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
