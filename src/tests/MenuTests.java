package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import bankapp.Menu;
import bankapp.BankAccount;
import bankapp.Menu.UserInputOptions;


import java.awt.*;

public class MenuTests {
	 @Test
	    public void testUserDeposit(){
	        Menu menu = new Menu();
	        menu.processUserInput(UserInputOptions.DEPOSIT, 25.0);
	        BankAccount account = menu.getAccount();
	        assertEquals(25.0, account.getCurrentBalance(), 0.005);
	    }

	    @Test
	    public void testUserWithdraw(){
	        Menu menu = new Menu();
	        menu.processUserInput(UserInputOptions.DEPOSIT, 100.0);
	        menu.processUserInput(UserInputOptions.WITHDRAW, 25.0);
	        BankAccount account = menu.getAccount();
	        assertEquals(75.0, account.getCurrentBalance(), 0.005);
	    }
	    
	    @Test
	    public void testCheckBalance() {
	    	Menu menu = new Menu();
	    	menu.processUserInput(UserInputOptions.DEPOSIT, 100.0);
	    	menu.processUserInput(UserInputOptions.CHECK_BALANCE, 0.0);
	    	BankAccount account = menu.getAccount();
	        assertEquals(100.0, account.getCurrentBalance(), 0.005);

	    }
	    
	    @Test
	    public void testInvalidChoice() {
	        Menu menu = new Menu();
	        menu.processUserInput(null, 20.0);
	        BankAccount account = menu.getAccount();
	        assertEquals(0.0, account.getCurrentBalance(), 0.005);
	    }

}
