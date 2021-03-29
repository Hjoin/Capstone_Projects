package com.techelevator.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.view.Menu;

public class MenuTest {

	private ByteArrayOutputStream output;

	@Before
	public void setup() {
		output = new ByteArrayOutputStream();
	}

	@Test
	public void displays_a_list_of_menu_options_and_prompts_user_to_make_a_choice() {
		Object[] options = new Object[] { new Integer(3), "Blind", "Mice" };
		Menu menu = getMenuForTesting();

		menu.getChoiceFromOptions(options);

		String expected = "\r\n" + "1) " + options[0].toString() + "\r\n" + "2) " + options[1].toString() + "\r\n" + "3) "
				+ options[2].toString() + "\r\n\r\n" + "Please choose an option >>> ";
		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void returns_object_corresponding_to_user_choice() {
		Integer expected = new Integer(456);
		Integer[] options = new Integer[] { new Integer(123), expected, new Integer(789) };
		Menu menu = getMenuForTestingWithUserInput("2\r\n");

		Integer result = (Integer) menu.getChoiceFromOptions(options);

		Assert.assertEquals(expected, result);
	}

	@Test
	public void redisplays_menu_if_user_does_not_choose_valid_option() {
		Object[] options = new Object[] { "Larry", "Curly", "Moe" };
		Menu menu = getMenuForTestingWithUserInput("4\r\n1\r\n");

		menu.getChoiceFromOptions(options);

		String menuDisplay = "\r\n" + "1) " + options[0].toString() + "\r\n" + "2) " + options[1].toString() + "\r\n" + "3) "
				+ options[2].toString() + "\r\n\r\n" + "Please choose an option >>> ";

		String expected = menuDisplay + "\r\n*** 4 is not a valid option ***\r\n\r\n" + menuDisplay;

		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void redisplays_menu_if_user_chooses_option_less_than_1() {
		Object[] options = new Object[] { "Larry", "Curly", "Moe" };
		Menu menu = getMenuForTestingWithUserInput("0\r\n1\r\n");

		menu.getChoiceFromOptions(options);

		String menuDisplay = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() + System.lineSeparator() + "3) "
				+ options[2].toString() + System.lineSeparator()+System.lineSeparator() + "Please choose an option >>> ";

		String expected = menuDisplay + System.lineSeparator()+"*** 0 is not a valid option ***" +System.lineSeparator()+System.lineSeparator()+ menuDisplay;

		//Assert.assertTrue("wtf",expected.contains(output.toString()));
		Assert.assertEquals(expected, output.toString());
		//Assert.assertTrue("for real",output.toString().contains("*** 0 is not a valid option ***"));
	}

	@Test
	public void redisplays_menu_if_user_enters_garbage() {
		Object[] options = new Object[] { "Larry", "Curly", "Moe" };
		Menu menu = getMenuForTestingWithUserInput("Mickey Mouse\r\n1\r\n");

		menu.getChoiceFromOptions(options);

		String menuDisplay = "\r\n" + "1) " + options[0].toString() + "\r\n" + "2) " + options[1].toString() + "\r\n" + "3) "
				+ options[2].toString() + "\r\n\r\n" + "Please choose an option >>> ";

		String expected = menuDisplay + "\r\n*** Mickey Mouse is not a valid option ***\r\n\r\n" + menuDisplay;

		Assert.assertEquals(expected, output.toString());
	}

	private Menu getMenuForTestingWithUserInput(String userInput) {
		ByteArrayInputStream input = new ByteArrayInputStream(String.valueOf(userInput).getBytes());
		return new Menu(input, output);
	}

	private Menu getMenuForTesting() {
		return getMenuForTestingWithUserInput("1\r\n");
	}
}
