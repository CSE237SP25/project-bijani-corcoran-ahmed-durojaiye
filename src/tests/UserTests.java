package tests;

import bankapp.BankAccount;
import bankapp.SavingsAccount;
import bankapp.CheckingAccount;
import bankapp.BankApp;
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

    @BeforeEach
    public void setUp() {
        user = new User("testuser", "testpass");
        fromAccount = new CheckingAccount("from");
        toAccount = new SavingsAccount("to");

        fromAccount.deposit(500);
        toAccount.deposit(200);
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
    public void testMainMenuLoginAndLogoutFlow() {
        String simulatedInput = String.join("\n",
                "2", // Create User
                "testuser", // Username
                "pass123", // Password
                "1", // Login
                "testuser", // Username
                "pass123", // Password
                "0", // Logout from user menu
                "0" // Exit app
        );

        InputStream input = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(input);

        BankApp.main(new String[0]);
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
        user.renameAccount("DoesNotExist", "NewName");
    }

    @Test
    public void testRenameToExistingAccountName() {
        User user = new User("john", "pass123");
        user.createAccount("checking", "A");
        user.createAccount("savings", "B");

        user.renameAccount("A", "B");  // should throw
    }
}
