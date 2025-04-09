package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import bankapp.BankAccount;
import bankapp.CheckingAccount;
import bankapp.Transaction;
import java.util.List;

public class TransactionTests {

	@Test
	public void testTransactionCreateDeposit() {
		BankAccount account = new CheckingAccount("Aly's Checking Account");
		account.deposit(100);
		List<Transaction> transactionHistory = account.getTransactionHistory();
		assertEquals(1, transactionHistory.size());
	}
}
