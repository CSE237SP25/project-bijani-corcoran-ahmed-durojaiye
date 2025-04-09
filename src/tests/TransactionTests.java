package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import bankapp.BankAccount;
import bankapp.CheckingAccount;
import bankapp.SavingsAccount;
import bankapp.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionTests {

	@Test
	public void testTransactionCreateDeposit() {
		BankAccount account = new CheckingAccount("Aly's Checking Account");
		account.deposit(100);
		List<Transaction> transactionHistory = account.getTransactionHistory();
		assertEquals(1, transactionHistory.size());
	}

	private static void testAmountSearchExactMatch() {
        CheckingAccount account = new CheckingAccount("Test");
        account.deposit(100.00);
        account.deposit(200.00);
        
        List<Transaction> results = account.searchByAmount(200.00);
        assertEquals("Expected 1 transaction of $200.00", 1, results.size());
    }

	private static void testAmountSearchNoMatch() {
        CheckingAccount account = new CheckingAccount("Test");
        account.deposit(100.00);
        
        List<Transaction> results = account.searchByAmount(75.00);
        assertEquals("Expected 0 transactions of $75.00", 0, results.size());
    }

	private static void testDateRangeAllTransactions() {
        SavingsAccount account = new SavingsAccount("Test");
        LocalDateTime now = LocalDateTime.now();
        account.deposit(100.00);
        account.deposit(200.00);
        
        List<Transaction> results = account.searchByDateRange(
            now.minusDays(1), now.plusDays(1));
        assertEquals("Expected all transactions in wide range", 2, results.size());
    }

	private static void testTypeSearchDeposit() {
        CheckingAccount account = new CheckingAccount("Test");
        account.deposit(100.00);
        account.withdraw(50.00);
        
        List<Transaction> results = account.searchByType("deposit");
        assertEquals("Expected 1 deposit transaction", 1, results.size());
    }
    
    private static void testTypeSearchWithdraw() {
        CheckingAccount account = new CheckingAccount("Test");
        account.deposit(100.00);
        account.withdraw(50.00);
        account.withdraw(20.00);
        
        List<Transaction> results = account.searchByType("withdraw");
        assertEquals("Expected 2 withdraw transactions", 2, results.size());
    }
	
}
