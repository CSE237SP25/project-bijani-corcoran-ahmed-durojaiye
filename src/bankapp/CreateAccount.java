import java.util.HashMap;
import java.util.Scanner;

//Need to add getters and setters to bottom of file
//Need to add option to choose accountType

public class CreateAccount {
    private HashMap<String, String> userDatabase;
    private Scanner scanner;
    private BankAccount theAccount;

    public CreateAccount(HashMap<String, String> userDatabase, Scanner scanner) {
        this.userDatabase = userDatabase;
        this.scanner = scanner;
    }

    public BankAccount authenticateUser() {
        System.out.println("Welcome! Do you have an account? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            return login();
        } else {
            return createAccount();
        }
    }

    private BankAccount login() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            return new BankAccount();
        } else {
            System.out.println("Invalid credentials. Try again.");
            return login();
        }
    }

    private BankAccount createAccount() {
        System.out.println("Creating a new account. Enter a username:");
        String username = scanner.nextLine();

        if (userDatabase.containsKey(username)) {
            System.out.println("Username already exists. Try logging in.");
            return login();
        }

        System.out.println("Enter a password:");
        String password = scanner.nextLine();

        userDatabase.put(username, password);
        System.out.println("Account created successfully! You are now logged in.");
        return new BankAccount();
    }
}
