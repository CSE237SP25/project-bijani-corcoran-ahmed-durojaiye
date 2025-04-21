package tests;

import static org.junit.Assert.assertEquals;



import org.junit.jupiter.api.Test;

import bankapp.CheckingAccount;

public class CheckingAccountTests {
	@Test
    public void testWithdrawBelow100() {
		CheckingAccount account = new CheckingAccount("Test Account");
        account.deposit(200);
   
        account.withdraw(50.0);

        assertEquals(150.0, account.getBalance(), 0.005);
        
		account.withdraw(55);
        
        assertEquals(95.0, account.getBalance(), 0.005);
    }
	
}