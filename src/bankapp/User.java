package bankapp;
import java.util.*;

public class User {
    private String username;
    private String password;
    private Map<String, BankAccount> accounts = new HashMap<>();
    private int failedLoginAttempts;
    private boolean accountIsLocked;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.failedLoginAttempts = 0;
        this.accountIsLocked = false;
    }

    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }

    public void changePassword(String newPass) {
        this.password = newPass;
    }

    public void createAccount(String accountType, String accountName) {
        if (accounts.containsKey(accountName)) throw new IllegalArgumentException("Duplicate account name");
        BankAccount acc = accountType.equalsIgnoreCase("checking") ?
            new CheckingAccount(accountName) : new SavingsAccount(accountName);
        accounts.put(accountName, acc);
    }

    public void deleteAccount(String accountName) {
        if (!accounts.containsKey(accountName)) throw new IllegalArgumentException("No such account");
        accounts.remove(accountName);
    }

    public void transfer(String from, String to, double amount, String description) {
        if (!accounts.containsKey(from) || !accounts.containsKey(to)) throw new IllegalArgumentException("Accounts missing");
        
        if (accounts.size() < 2) {
            throw new IllegalArgumentException("You need at least two accounts to make a transfer.");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        BankAccount sender = accounts.get(from);
        BankAccount receiver = accounts.get(to);

        sender.withdraw(amount, "Transfer to " + to + (description.isEmpty() ? "" : ": " + description));
        receiver.deposit(amount, "Transfer from " + from + (description.isEmpty() ? "" : ": " + description));
    }

    public void transfer(String from, String to, double amount) {
        transfer(from, to, amount, "");
    }

    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
    } 
    
    public void registerFailedLogin() {
        this.failedLoginAttempts++;
        if (failedLoginAttempts >= 3) {
            accountIsLocked = true;
        }
    }   

    public boolean accountIsLocked() {
        return accountIsLocked;
    }

    public void renameAccount(String oldName, String newName) {
        if (!accounts.containsKey(oldName)) throw new IllegalArgumentException("Account not found.");
        if (accounts.containsKey(newName)) throw new IllegalArgumentException("New account name already exists.");
    
        BankAccount acc = accounts.remove(oldName);
        acc.setName(newName);
        accounts.put(newName, acc);
    }

    public BankAccount getAccount(String name) {
        return accounts.get(name);
    }

    public Set<String> getAllAccountNames() {
        return accounts.keySet();
    }

    public String getUsername() { return username; }
}
