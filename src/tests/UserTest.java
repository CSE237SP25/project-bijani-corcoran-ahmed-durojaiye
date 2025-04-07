package tests;

import bankapp.BankAccount;
import bankapp.Transaction;
import bankapp.SavingsAccount;
import bankapp.CheckingAccount;
import bankapp.BankApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;


public class UserTest {
	private BankAccount fromAccount;
    private BankAccount toAccount;

    @BeforeEach
    public void setUp() {
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
                "2",             // Create User
                "testuser",      // Username
                "pass123",       // Password
                "1",             // Login
                "testuser",      // Username
                "pass123",       // Password
                "0",             // Logout from user menu
                "0"              // Exit app
        );

        InputStream input = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(input);

        BankApp.main(new String[0]);
    }
}
