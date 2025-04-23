package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import bankapp.BankAccount;
import bankapp.BankApp;
import bankapp.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;


public class BankAppTests {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Test
    public void testMainMenuLoginAndLogoutFlow() {
        String simulatedInput = String.join("\n", 
            "2",  // Create User
            "testuser", // Username
            "pass123", // Password
            "1",  // Login
            "testuser", // Username
            "pass123", // Password
            "0",  // Logout from user menu
            "0" // Exit app
        );
        InputStream input = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(input);
        BankApp.main(new String[0]);
    }

    @Test
    public void testSimulatedLoginFlowForLockedOutUser() {
        User user = new User("loginUser", "correctPass");
        
        if (!user.checkPassword("wrong1")) user.registerFailedLogin();
        if (!user.checkPassword("wrong2")) user.registerFailedLogin();
        assertFalse(user.accountIsLocked());

        if (user.checkPassword("correctPass")) user.resetFailedAttempts();
        assertFalse(user.accountIsLocked());

        user.registerFailedLogin();
        user.registerFailedLogin();
        user.registerFailedLogin();
        assertTrue(user.accountIsLocked());
    }

    @Test
    public void testSimulatedAccountViewPrintoutWithAssertions() {
        User user = new User("cli", "print");
        user.createAccount("checking", "Lunch");
        user.createAccount("savings", "Tuition");
        user.getAccount("Lunch").deposit(20.50);
        user.getAccount("Tuition").deposit(1000);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        
        System.setOut(new PrintStream(outputStream));
        for (String accName : user.getAllAccountNames()) {
            BankAccount acc = user.getAccount(accName);
            System.out.println("- " + acc.getName() + " (" + acc.getAccountType() + "): $" + acc.getBalance());
        }
        System.setOut(originalOut);
        
        String output = outputStream.toString().trim();
        assertTrue(output.contains("- Lunch (Checking): $20.5"));
        assertTrue(output.contains("- Tuition (Savings): $1000.0"));
    }

    @Test
    public void testPasswordConfirmationMismatchAndRetry() {
        String simulatedInput = String.join("\n",
            "2",           // Choose "Create User"
            "user123",     // Username
            "pass1",       // First password
            "wrongpass",   // Mismatch confirmation
            "pass1",       // Re-enter password
            "pass1",       // Matching confirmation
            "0"            // Exit
        );

        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        BankApp.main(new String[0]);
        String consoleOut = output.toString();

        assertTrue(consoleOut.contains("Passwords do not match"));
        assertTrue(consoleOut.contains("User created!"));
    }

    @Test
    public void testHiddenPasswordFallbackAndLoginSuccess() {
        String simulatedInput = String.join("\n",
            "2",             // Create user
            "usercli",       // Username
            "mypassword",    // Password
            "mypassword",    // Confirm
            "1",             // Login
            "usercli",       // Username
            "mypassword",    // Password
            "0",             // Logout
            "0"              // Exit
        );

        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        assertDoesNotThrow(() -> BankApp.main(new String[0]));
        String outputStr = output.toString();
        assertTrue(outputStr.contains("User created!"));
        assertTrue(outputStr.contains("Logged in as: usercli"));
    }

    @Test
	void testPrintSummaryNoAccounts() {
    	User emptyUser = new User("john_doe", "password123");
		System.setOut(new PrintStream(outputStreamCaptor));
		emptyUser.printSummary();
		assertEquals("No accounts found.\n", outputStreamCaptor.toString());
	}

    @Test
    void testPrintSummaryWithAccounts() {
        User testUser = new User("john_doe", "password123");
        testUser.createAccount("checking", "checking1");
        testUser.createAccount("savings", "savings1");
        testUser.printSummary();
        assertFalse(outputStreamCaptor.toString().contains("No accounts found."));
    }
}
