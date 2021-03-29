package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;
	private PrintStream err = System.err;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {	
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (Exception e) {
			choice = null;
			// eat the exception, an error message will be displayed below since choice will
			// be null
		}
		if (choice == null) {
			//err.println("\n*** " + userInput + " is not a valid option ***\n");
			out.println("\r\n*** " + userInput + " is not a valid option ***\r\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print("\r\nPlease choose an option >>> ");
		out.flush();
	}

	public int getPositiveInt(String prompt, String errorMsg, int max) {
		int input = 0;
		while (true) {
			try {
				out.println(prompt + " >>> ");
				out.flush();
				input = Integer.parseInt(in.nextLine());
				if (input < 0 || input > max ) {
					throw new Exception(); 
				} else {
					break;
				}
				
			} catch (Exception e) {
				err.println(errorMsg);
				
			}
		}
		return input;
	}

	public String getChoice(String prompt, String errorMsg, Collection<String> validValues) {
		String input = "";
		while (true) {
			out.println(prompt + " >>> ");
			out.flush();
			input = in.nextLine().toUpperCase();
			if (validValues.contains(input)) {
				break;
			} else {
				err.println(errorMsg);
			}
		}
		return input;
		
	}
}
