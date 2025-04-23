package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bankapp.Transaction;
import bankapp.CheckingAccount;
import bankapp.SavingsAccount;

public class BankAccountTests {
	
	private SavingsAccount account;
	
	@BeforeEach
    public void setup() {
        account = new SavingsAccount("TestAccount");
    }

	@Test
	public void testSimpleDeposit() {
		account.deposit(25);
		
		assertEquals(25.0, account.getBalance(), 0.005);
	}
	
	@Test
	public void testNegativeDeposit() {
		try {
			account.deposit(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}
	

	@Test
	public void testSimpleWithdrawal(){

		account.deposit(30);

		account.withdraw(25);

		assertEquals(account.getBalance(), 5.0, 0.005);
	 }

	 @Test
	 public void testWithdrawalBounds(){
		try{
			account.withdraw(10);
			fail();
		} catch (IllegalArgumentException e){
			assertTrue(e != null);
		}
	 }

	@Test
	public void testNegativeWithdrawal() {
		try {
			account.withdraw(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}
	
	 @Test
	    public void testInvalidWithdraw(){
	        try {
	        	account.withdraw(10);
	            fail();
	        } catch (IllegalArgumentException e) {
	            assertTrue(e != null);
	        }
	    }
	 
	 @Test
	 public void testGetBalance() {
		 account.deposit(150);
		 assertEquals(150.0, account.getBalance(), 0.005);
		 account.withdraw(100);
		 assertEquals(50.0, account.getBalance(), 0.005);
		 
	 }

	 @Test
	 public void testZeroBalance() {
		 assertEquals(0.0, account.getBalance(), 0.005);
 
	 }
	 
	 @Test
	 public void testRecentTransactions() {
		 account.deposit(50);
		 account.deposit(20);
		 account.withdraw(10);
		 List<Transaction> recent = account.getRecentTransactions(2);
		 assertEquals(2, recent.size());
		 
	 }
	
	 @Test
	 public void getTestName() {
		 assertEquals("TestAccount", account.getName());
	 }
	 
	 @Test
	 public void testGetAccountType() {
		 assertEquals("Savings", account.getAccountType());
	 }

}
