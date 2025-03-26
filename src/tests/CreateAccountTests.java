package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import bankapp.CreateAccount;
import bankapp.BankAccount;

import java.util.HashMap;
import java.util.Scanner;
import java.io.ByteArrayInputStream;

public class CreateAccountTests {
    private HashMap<String, String> userDatabase;
    private Scanner scanner;
    private CreateAccount createAccount;

    @Before
    public void setUp() {
        userDatabase = new HashMap<>();
    }

    @Test
    public void testCreateNewCheckingAccount() {
        String input = "username123\npassword123\nchecking\n";
        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        createAccount = new CreateAccount(userDatabase, scanner);

        BankAccount account = createAccount.authenticateUser();
        assertNotNull(account);
        assertEquals("username123", createAccount.getUsername());
        assertEquals("password123", createAccount.getPassword());
        assertEquals("password123", userDatabase.get("username123"));
        assertEquals("checking", createAccount.getAccountType());
    }

    @Test
    public void testCreateNewSavingsAccount() {
        String input = "username123\npassword123\nsavings\n";
        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        createAccount = new CreateAccount(userDatabase, scanner);

        BankAccount account = createAccount.authenticateUser();
        assertNotNull(account);
        assertEquals("username123", createAccount.getUsername());
        assertEquals("password123", createAccount.getPassword());
        assertEquals("password123", userDatabase.get("username123"));
        assertEquals("savings", createAccount.getAccountType());
    }

    @Test
    public void testLoginSuccess() {
        userDatabase.put("username123", "password123");
        String input = "username123\npassword123\nchecking\n";
        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        createAccount = new CreateAccount(userDatabase, scanner);

        BankAccount account = createAccount.authenticateUser();
        assertNotNull(account);
        assertEquals("username123", createAccount.getUsername());
        assertEquals("password123", createAccount.getPassword());
        assertEquals("checking", createAccount.getAccountType());
    }

    @Test
    public void testLoginFailureAndRetry() {
        userDatabase.put("username123", "password123");
        String input = "username123\nwrongpassword\nusername123\npassword123\nsavings\n";
        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        createAccount = new CreateAccount(userDatabase, scanner);

        BankAccount account = createAccount.authenticateUser();
        assertNotNull(account);
        assertEquals("username123", createAccount.getUsername());
        assertEquals("password123", createAccount.getPassword());
        assertEquals("savings", createAccount.getAccountType());
    }

    @Test
    public void testInvalidAccountTypeSelection() {
        String input = "username123\npassword123\nwrongType\nchecking\n";
        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        createAccount = new CreateAccount(userDatabase, scanner);

        BankAccount account = createAccount.authenticateUser();
        assertNotNull(account);
        assertEquals("username123", createAccount.getUsername());
        assertEquals("password123", createAccount.getPassword());
        assertEquals("checking", createAccount.getAccountType());
    }

    // More tests for changePassword
    @Test
    public void testSuccessfulPasswordChange() {
        userDatabase.put("user1", "oldPass");

        // Simulate: login → choose to change password → enter correct current password
        // → enter new password → continue to banking → choose account type
        String input = String.join("\n",
                "yes", // have account
                "user1", // username
                "oldPass", // correct password
                "2", // change password
                "oldPass", // re-enter old password
                "newPass", // enter new password
                "1", // continue to banking menu
                "checking" // select account type
        );

        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        createAccount = new CreateAccount(userDatabase, scanner);
        BankAccount account = createAccount.authenticateUser();

        assertEquals("newPass", userDatabase.get("user1"));
        assertEquals("newPass", createAccount.getPassword());
        assertEquals("checking", createAccount.getAccountType());
        assertNotNull(account);
    }

    @Test
    public void testFailedPasswordChangeWrongOldPassword() {
        userDatabase.put("user2", "originalPass");

        // Simulate: login → choose to change password → enter wrong current password →
        // retry → enter correct password → set new password → continue → choose account
        // type
        String input = String.join("\n",
                "yes",
                "user2",
                "originalPass",
                "2",
                "wrongPass", // first try: wrong current password
                "originalPass", // second try: correct password
                "updatedPass",
                "1",
                "savings");

        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        createAccount = new CreateAccount(userDatabase, scanner);
        BankAccount account = createAccount.authenticateUser();

        assertEquals("updatedPass", userDatabase.get("user2"));
        assertEquals("updatedPass", createAccount.getPassword());
        assertEquals("savings", createAccount.getAccountType());
        assertNotNull(account);
    }

}
