import java.util.HashMap;
import java.util.Scanner;

public class CreateAccount {
    private HashMap<String, String> userDatabase;
    private Scanner scanner;
    private BankAccount theAccount;
    private String accountType;
    private String username;
    private String password;

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
        this.username = scanner.nextLine();
        System.out.println("Enter your password:");
        this.password = scanner.nextLine();

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            return selectAccountType();
        } else {
            System.out.println("Invalid credentials. Try again.");
            return login();
        }
    }

    private BankAccount createAccount() {
        System.out.println("Creating a new account. Enter a username:");
        this.username = scanner.nextLine();

        if (userDatabase.containsKey(username)) {
            System.out.println("Username already exists. Try logging in.");
            return login();
        }

        System.out.println("Enter a password:");
        this.password = scanner.nextLine();
        userDatabase.put(username, password);

        System.out.println("Account created successfully! You are now logged in.");
        return selectAccountType();
    }

    private BankAccount selectAccountType() {
        System.out.println("Select your account type (checking/savings):");
        String type = scanner.nextLine().trim().toLowerCase();

        while (!type.equals("checking") && !type.equals("savings")) {
            System.out.println("Invalid choice. Please enter 'checking' or 'savings':");
            type = scanner.nextLine().trim().toLowerCase();
        }

        this.accountType = type;
        System.out.println("You have selected a " + type + " account.");
        return new BankAccount(type);
    }

    // Getters and Setters
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BankAccount getTheAccount() {
        return theAccount;
    }

    public void setTheAccount(BankAccount theAccount) {
        this.theAccount = theAccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
