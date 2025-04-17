package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import org.junit.jupiter.api.Test;

import bankapp.SavingsAccount;

public class SavingsAccountTests {
	@Test
    public void testWithdrawBelow100() {
        SavingsAccount account = new SavingsAccount("Test Account");
        account.deposit(200);
   
        account.withdraw(50.0);

        assertEquals(150.0, account.getBalance(), 0.005);
        
        try {
			account.withdraw(55);
			fail("Expected IllegalArgumentException due to minimum balance rule");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("Insufficient balance"));
		}
        
        assertEquals(150, account.getBalance(), 0.005);
    }
	
}