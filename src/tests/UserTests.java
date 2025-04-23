package tests;

import bankapp.BankAccount;
import bankapp.SavingsAccount;
import bankapp.CheckingAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import bankapp.User;
import java.util.Set;

public class UserTests {

	private User user;
	private BankAccount fromAccount;
	private BankAccount toAccount;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	@BeforeEach
	public void setUp() {
		user = new User("testuser", "testpass");
		fromAccount = new CheckingAccount("from");
		toAccount = new SavingsAccount("to");

		fromAccount.deposit(500);
		toAccount.deposit(200);

		System.setOut(new PrintStream(outputStreamCaptor));
	}

	@Test
	public void testSuccessfulTransfer() {
		double transferAmount = 150;

		fromAccount.withdraw(transferAmount);
		toAccount.deposit(transferAmount);

		assertEquals(350, fromAccount.getBalance(), 0.01);
		assertEquals(350, toAccount.getBalance(), 0.01);
	}

	@Test
	public void testTransferMoreThanBalance() {
		double transferAmount = 1000;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fromAccount.withdraw(transferAmount);
			toAccount.deposit(transferAmount);
		});

		assertEquals("Invalid withdraw", exception.getMessage());
		assertEquals(500, fromAccount.getBalance(), 0.01);
		assertEquals(200, toAccount.getBalance(), 0.01);
	}

	@Test
	public void testTransferNegativeAmount() {
		double transferAmount = -50;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fromAccount.withdraw(transferAmount);
			toAccount.deposit(transferAmount);
		});

		assertEquals("Invalid withdraw", exception.getMessage());
	}

	@Test
	public void testCheckPasswordCorrect() {
		assertTrue(user.checkPassword("testpass"));
	}

	@Test
	public void testCheckPasswordIncorrect() {
		assertFalse(user.checkPassword("wrongpass"));
	}

	@Test
	public void testChangePassword() {
		user.changePassword("newpass");
		assertTrue(user.checkPassword("newpass"));
		assertFalse(user.checkPassword("testpass"));
	}

	@Test
	public void testIsLockedInitiallyFalse() {
		User user = new User("user1", "pass1");
		assertFalse(user.accountIsLocked());
	}

	@Test
	public void testRegisterFailedLoginIncrementsAndLocks() {
		User user = new User("user2", "pass2");
		user.registerFailedLogin(); // 1
		assertFalse(user.accountIsLocked());

		user.registerFailedLogin(); // 2
		assertFalse(user.accountIsLocked());

		user.registerFailedLogin(); // 3
		assertTrue(user.accountIsLocked());
	}

	@Test
	public void testResetFailedAttemptsResetsCounter() {
		User user = new User("user3", "pass3");
		user.registerFailedLogin();
		user.registerFailedLogin();
		assertFalse(user.accountIsLocked());

		user.resetFailedAttempts(); // simulate success
		user.registerFailedLogin(); // 1 fail after reset
		assertFalse(user.accountIsLocked());
	}

	@Test
	public void testResetAfterLockDoesNotUnlock() {
		User user = new User("user4", "pass4");
		user.registerFailedLogin();
		user.registerFailedLogin();
		user.registerFailedLogin(); // now locked

		assertTrue(user.accountIsLocked());

		user.resetFailedAttempts(); // shouldn't "unlock"
		assertTrue(user.accountIsLocked()); // still locked
	}

	@Test
	public void testCreateCheckingAccount() {
		user.createAccount("checking", "main");
		BankAccount acc = user.getAccount("main");
		assertNotNull(acc);
		assertEquals("main", acc.getName());
		assertEquals("Checking", acc.getAccountType());
	}

	@Test
	public void testCreateSavingsAccount() {
		user.createAccount("savings", "save");
		BankAccount acc = user.getAccount("save");
		assertNotNull(acc);
		assertEquals("save", acc.getName());
		assertEquals("Savings", acc.getAccountType());
	}

	@Test
	public void testCreateDuplicateAccountThrowsException() {
		user.createAccount("checking", "main");
		assertThrows(IllegalArgumentException.class, () -> {
			user.createAccount("savings", "main");
		});
	}

	@Test
	public void testDeleteAccount() {
		user.createAccount("checking", "main");
		user.deleteAccount("main");
		assertNull(user.getAccount("main"));
	}

	@Test
	public void testDeleteNonexistentAccountThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> {
			user.deleteAccount("ghost");
		});
	}

	@Test
	public void testTransferBetweenAccounts() {
		user.createAccount("checking", "from");
		user.createAccount("savings", "to");

		user.getAccount("from").deposit(100);
		user.transfer("from", "to", 30);

		assertEquals(70, user.getAccount("from").getBalance(), 0.001);
		assertEquals(30, user.getAccount("to").getBalance(), 0.001);
	}

	@Test
	public void testTransferWithMissingAccountsThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> {
			user.transfer("from", "to", 10);
		});
	}

	@Test
	public void testTransferRequiresTwoAccounts() {
		user.createAccount("checking", "Primary");
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			user.transfer("Primary", "Primary", 100.00);
		});
		assertEquals("You need at least two accounts to make a transfer.", exception.getMessage());
	}

	@Test
	public void testTransferRequiresDifferentAccounts() {
		user.createAccount("checking", "Primary");
		user.createAccount("savings", "Savings");

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			user.transfer("Primary", "Primary", 100.00);
		});

		assertEquals("Cannot transfer to the same account", exception.getMessage());
	}

	@Test
	public void testGetAccountReturnsCorrectInstance() {
		user.createAccount("checking", "main");
		BankAccount acc = user.getAccount("main");
		assertNotNull(acc);
	}

	@Test
	public void testGetAllAccountNames() {
		user.createAccount("checking", "acc1");
		user.createAccount("savings", "acc2");
		Set<String> names = user.getAllAccountNames();
		assertEquals(2, names.size());
		assertTrue(names.contains("acc1"));
		assertTrue(names.contains("acc2"));
	}

	@Test
	public void testGetUsername() {
		assertEquals("testuser", user.getUsername());
	}

	@Test
	public void testRenameAccountSuccessfully() {
		User user = new User("john", "pass123");
		user.createAccount("checking", "Groceries");

		user.renameAccount("Groceries", "Food");

		assertNull(user.getAccount("Groceries"));
		assertNotNull(user.getAccount("Food"));
		assertEquals("Food", user.getAccount("Food").getName());
	}

	@Test
	public void testRenameNonexistentAccount() {
		User user = new User("john", "pass123");

		assertThrows(IllegalArgumentException.class, () -> {
			user.renameAccount("DoesNotExist", "NewName");
		});
	}

	@Test
	public void testRenameToExistingAccountName() {
		User user = new User("john", "pass123");
		user.createAccount("checking", "A");
		user.createAccount("savings", "B");

		assertThrows(IllegalArgumentException.class, () -> {
			user.renameAccount("A", "B");
		});
	}

	@Test
	public void testGetAllAccountNamesReturnsCorrectNames() {
		User user = new User("test", "pass");
		user.createAccount("checking", "Spend");
		user.createAccount("savings", "Save");

		Set<String> names = user.getAllAccountNames();
		assertEquals(2, names.size());
		assertTrue(names.contains("Spend"));
		assertTrue(names.contains("Save"));
	}

	@Test
	public void testAccountsReturnCorrectTypes() {
		User user = new User("user", "pass");
		user.createAccount("checking", "MainChecking");
		user.createAccount("savings", "RainyDay");

		BankAccount checking = user.getAccount("MainChecking");
		BankAccount savings = user.getAccount("RainyDay");

		assertEquals("Checking", checking.getAccountType());
		assertEquals("Savings", savings.getAccountType());
	}

	@Test
	public void testAccountsWithDepositsShowCorrectBalances() {
		User user = new User("test", "pass");
		user.createAccount("checking", "Food");
		user.createAccount("savings", "Rent");

		user.getAccount("Food").deposit(150);
		user.getAccount("Rent").deposit(800);

		assertEquals(150, user.getAccount("Food").getBalance(), 0.001);
		assertEquals(800, user.getAccount("Rent").getBalance(), 0.001);
	}

}
