package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bankapp.User;
import bankapp.BankAccount;

import java.util.Set;

public class UserTests {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testuser", "testpass");
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
}
