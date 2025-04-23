package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import bankapp.BankAccount;
import bankapp.CheckingAccount;
import bankapp.SavingsAccount;
import bankapp.Transaction;
import bankapp.User;

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

    @Test
    public void testDepositWithDescription() {
        BankAccount account = new CheckingAccount("Test Account");
        account.deposit(100.00, "Birthday money");
        
        List<Transaction> history = account.getTransactionHistory();
        assertEquals(1, history.size());
        Transaction transaction = history.get(0);
        assertEquals("deposit", transaction.getTransactionType());
        assertEquals(100.00, transaction.getAmount(), 0.001);
        assertEquals("Birthday money", transaction.getDescription());
    }

    @Test
    public void testTransferWithDescriptions() {
        User user = new User("testuser", "password");
        user.createAccount("checking", "Account1");
        user.createAccount("savings", "Account2");
        
        BankAccount account1 = user.getAccount("Account1");
        account1.deposit(500.00);
        
        user.transfer("Account1", "Account2", 200.00, "Moving savings");
        
        Transaction account1Transaction = account1.getTransactionHistory().get(1);
        assertEquals("withdraw", account1Transaction.getTransactionType());
        assertEquals(200.00, account1Transaction.getAmount(), 0.001);
        assertTrue(account1Transaction.getDescription().contains("Transfer to Account2"));
        assertTrue(account1Transaction.getDescription().contains("Moving savings"));
        
        BankAccount account2 = user.getAccount("Account2");
        Transaction account2Transaction = account2.getTransactionHistory().get(0);
        assertEquals("deposit", account2Transaction.getTransactionType());
        assertEquals(200.00, account2Transaction.getAmount(), 0.001);
        assertTrue(account2Transaction.getDescription().contains("Transfer from Account1"));
        assertTrue(account2Transaction.getDescription().contains("Moving savings"));
    }

    @Test
	public void testAmountSearchExactMatch() {
        CheckingAccount account = new CheckingAccount("Test");
        account.deposit(100.00);
        account.deposit(200.00);
        
        List<Transaction> results = account.searchByAmount(200.00);
        assertEquals("Expected 1 transaction of $200.00", 1, results.size());
    }

    @Test
	public void testAmountSearchNoMatch() {
        CheckingAccount account = new CheckingAccount("Test");
        account.deposit(100.00);
        
        List<Transaction> results = account.searchByAmount(75.00);
        assertEquals("Expected 0 transactions of $75.00", 0, results.size());
    }

    @Test
	public void testDateRangeAllTransactions() {
        SavingsAccount account = new SavingsAccount("Test");
        LocalDateTime now = LocalDateTime.now();
        account.deposit(100.00);
        account.deposit(200.00);
        
        List<Transaction> results = account.searchByDateRange(
            now.minusDays(1), now.plusDays(1));
        assertEquals("Expected all transactions in wide range", 2, results.size());
    }

    @Test
	public void testTypeSearchDeposit() {
        CheckingAccount account = new CheckingAccount("Test");
        account.deposit(100.00);
        account.withdraw(50.00);
        
        List<Transaction> results = account.searchByType("deposit");
        assertEquals("Expected 1 deposit transaction", 1, results.size());
    }
    
    @Test
    public void testTypeSearchWithdraw() {
        CheckingAccount account = new CheckingAccount("Test");
        account.deposit(100.00);
        account.withdraw(50.00);
        account.withdraw(20.00);
        
        List<Transaction> results = account.searchByType("withdraw");
        assertEquals("Expected 2 withdraw transactions", 2, results.size());
    }
	
}
