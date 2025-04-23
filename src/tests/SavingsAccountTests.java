package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bankapp.SavingsAccount;

public class SavingsAccountTests {
    @Test
    public void testWithdrawBelow100() {
        SavingsAccount account = new SavingsAccount("Test Account");
        account.deposit(200);

        account.withdraw(50.0);

        assertEquals(150.0, account.getBalance(), 0.005);

        try {
            account.withdraw(55);
            fail("Expected IllegalArgumentException due to minimum balance rule");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Insufficient balance"));
        }

        assertEquals(150, account.getBalance(), 0.005);
    }

    @Test
    public void testSetAndCheckSavingsGoal() {
        SavingsAccount acc = new SavingsAccount("GoalAccount");
        acc.setSavingsGoal(500);
        assertEquals(500, acc.getSavingsGoal(), 0.001);
        assertFalse(acc.goalReached());
        acc.deposit(600);
        assertTrue(acc.goalReached());
    }

    @Test
    public void testSetPositiveSavingsGoal() {
        SavingsAccount acc = new SavingsAccount("TestSaver");
        acc.setSavingsGoal(1000.0);
        assertEquals(1000.0, acc.getSavingsGoal(), 0.001);
    }

    @Test
    public void testSetZeroSavingsGoal() {
        SavingsAccount acc = new SavingsAccount("TestZeroGoal");
        acc.setSavingsGoal(0.0);
        assertEquals(0.0, acc.getSavingsGoal(), 0.001);
    }

    @Test
    public void testSetNegativeSavingsGoalThrowsException() {
        SavingsAccount acc = new SavingsAccount("TestNegativeGoal");
        assertThrows(IllegalArgumentException.class, () -> acc.setSavingsGoal(-500));
    }

    @Test
    public void testGoalNotReachedInitially() {
        SavingsAccount acc = new SavingsAccount("TestGoalCheck");
        acc.setSavingsGoal(500);
        acc.deposit(200);
        assertFalse(acc.goalReached());
    }

    @Test
    public void testGoalReachedExactly() {
        SavingsAccount acc = new SavingsAccount("TestExactGoal");
        acc.setSavingsGoal(500);
        acc.deposit(500);
        assertTrue(acc.goalReached());
    }

    @Test
    public void testGoalExceeded() {
        SavingsAccount acc = new SavingsAccount("TestExceedGoal");
        acc.setSavingsGoal(500);
        acc.deposit(600);
        assertTrue(acc.goalReached());
    }

    @Test
    public void testSavingsGoalUnaffectedByWithdraw() {
        SavingsAccount acc = new SavingsAccount("TestWithdraw");
        acc.setSavingsGoal(400);
        acc.deposit(600);
        acc.withdraw(250); // balance = 350
        assertFalse(acc.goalReached());
    }

    @Test
    public void testSavingsGoalMetAfterMultipleDeposits() {
        SavingsAccount acc = new SavingsAccount("TestMultipleDeposits");
        acc.setSavingsGoal(300);
        acc.deposit(100);
        acc.deposit(200);
        assertTrue(acc.goalReached());
    }

}