package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;
import bankapp.CheckingAccount;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

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

    @Test
    public void testFreezePreventsWithdraw() {
        CheckingAccount account = new CheckingAccount("Frozen Account");
        account.deposit(200);
        account.freeze();

        assertThrows(IllegalStateException.class, () -> {
            account.withdraw(50);
        });
    }

    @Test
    public void testFreezePreventsDeposit() {
        CheckingAccount account = new CheckingAccount("Frozen Account");
        account.deposit(200);
        account.freeze();

        assertThrows(IllegalStateException.class, () -> {
            account.deposit(50);
        });
    }

    @Test
    public void testUnfreezeAllowsTransaction() {
        // Redirect input to simulate user typing "confirm"
        String simulatedInput = "confirm\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        CheckingAccount account = new CheckingAccount("Recovery Account");
        account.deposit(200);
        account.freeze();

        // Unfreeze with simulated input
        account.unfreeze();

        account.withdraw(50);
        assertEquals(150.0, account.getBalance(), 0.005);
    }
}