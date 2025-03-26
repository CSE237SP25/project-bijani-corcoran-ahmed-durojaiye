package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import bankapp.Menu;
import bankapp.BankAccount;
import bankapp.Transaction;
import java.awt.*;
import java.util.List;
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

	    @Test
	    public void testGetFullTransactionHistory() {
	        BankAccount account = new BankAccount("a");
	        
	        account.deposit(100.0);
	        account.withdraw(20.0);
	        account.deposit(50.0);
	 
	        
	        List<Transaction> history = account.getTransactionHistory();
	        
	        assertEquals(3, history.size());
	        
	        assertEquals("deposit", history.get(0).getType());
	        assertEquals(100.0, history.get(0).getAmount(), 0.001);
	        assertEquals("withdraw", history.get(1).getType());
	        assertEquals(20.0, history.get(1).getAmount(), 0.001);
	        assertEquals("deposit", history.get(2).getType());
	        assertEquals(50.0, history.get(2).getAmount(), 0.001);
	    }
	    
	    @Test
	    public void testGetRecentTransactions() {
	        BankAccount account = new BankAccount("test");
	        
	        account.deposit(100.0);
	        account.withdraw(20.0);
	        account.deposit(50.0);
	        account.withdraw(10.0);
	        account.deposit(25.0);
	        
	        List<Transaction> recentTransactions = account.getRecentTransactions(3);
	        
	        assertEquals(3, recentTransactions.size());
	        
	        assertEquals("deposit", recentTransactions.get(0).getType());
	        assertEquals(50.0, recentTransactions.get(0).getAmount(), 0.001);
	        assertEquals("withdraw", recentTransactions.get(1).getType());
	        assertEquals(10.0, recentTransactions.get(1).getAmount(), 0.001);
	        assertEquals("deposit", recentTransactions.get(2).getType());
	        assertEquals(25.0, recentTransactions.get(2).getAmount(), 0.001);
	    }
	    
}
