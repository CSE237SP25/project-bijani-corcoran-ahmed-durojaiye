package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bankapp.Transaction;
import bankapp.User;
import bankapp.BankAccount;
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
	public void testSimpleWithdrawal() {

		account.deposit(130);

		account.withdraw(25);

		assertEquals(account.getBalance(), 105.0, 0.005);
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
    public void testWithdrawWithDescription() {
        BankAccount account = new CheckingAccount("Test Account");
        account.deposit(200.00);
        account.withdraw(50.00, "Groceries");
        
        List<Transaction> history = account.getTransactionHistory();
        assertEquals(2, history.size());
        Transaction transaction = history.get(1);
        assertEquals("withdraw", transaction.getTransactionType());
        assertEquals(50.00, transaction.getAmount(), 0.001);
        assertEquals("Groceries", transaction.getDescription());
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
        assertTrue(account1Transaction.getDescription().contains("Moving savings"));
        
        BankAccount account2 = user.getAccount("Account2");
        Transaction account2Transaction = account2.getTransactionHistory().get(0);
        assertTrue(account2Transaction.getDescription().contains("Moving savings"));
    }
	
	@Test
	public void testWithdrawalAboveMinimumBalance() {
		account.deposit(150);

		try {
			account.withdraw(60);
			fail("Expected IllegalArgumentException due to minimum balance rule");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("Insufficient balance"));
		}
	}

	@Test
	public void testWithdrawalToMaintainMinimumBalance() {
		account.deposit(150);

		account.withdraw(50);

		assertEquals(100.0, account.getBalance(), 0.005);
	}

	@Test
	public void testNegativeWithdrawal() {
		account.deposit(120);
		try {
			account.withdraw(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}

	@Test
	public void testInvalidWithdraw() {
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
		account.withdraw(25);
		assertEquals(125.0, account.getBalance(), 0.005);

	}

	@Test
	public void testZeroBalance() {
		assertEquals(0.0, account.getBalance(), 0.005);

	}

	@Test
	public void testRecentTransactions() {
		account.deposit(150);
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

	@Test
	public void testExportTransactionHistory() throws IOException {
		String testFileName = "test_export.txt";

		account.deposit(150.00, "Test deposit");
		account.withdraw(25.00, "Test withdrawal");
		
		account.exportTransactionHistory(testFileName);
		
		File exportFile = new File(testFileName);
		assertTrue(exportFile.exists());
		
		List<String> lines = Files.readAllLines(Paths.get(testFileName));
		
		assertTrue(lines.get(0).contains("TestAccount"));
		assertTrue(lines.get(0).contains("Savings"));
		assertTrue(lines.get(1).contains("Balance: $125.0"));
		Files.deleteIfExists(Paths.get(testFileName));
	}

}
