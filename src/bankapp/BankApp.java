package bankapp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BankApp {
    private static Map<String, User> userDB = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome.  1: Login  2: Create User  0: Exit");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) break;
            if (choice == 1) login();
            if (choice == 2) createUser();
        }
    }

    private static void createUser() {
        System.out.print("Choose username: ");
        String username = scanner.nextLine();
        System.out.print("Choose password: ");
        String password = scanner.nextLine();

        if (userDB.containsKey(username)) {
            System.out.println("Username already exists");
            return;
        }

        userDB.put(username, new User(username, password));
        System.out.println("User created!");
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        User user = userDB.get(username);
    
        if (user == null) {
            System.out.println("Invalid credentials");
            return;
        }
    
        if (user.accountIsLocked()) {
            System.out.println("Account is locked due to too many failed login attempts.");
            return;
        }
    
        System.out.print("Password: ");
        String password = scanner.nextLine();
    
        if (!user.checkPassword(password)) {
            user.registerFailedLogin();
            if (user.accountIsLocked()) {
                System.out.println("Too many failed attempts. Your account is now locked.");
            } else {
                System.out.println("Invalid credentials");
            }
            return;
        }
    
        // Success
        user.resetFailedAttempts();
        userMenu(user);
    }    

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\nUser Menu - Logged in as: " + user.getUsername());
            System.out.println("1: Create Account\t 2: Delete Account\t 3: Transfer\t 4: Go Into Account\t 5: Change Password\t 6: Rename Account\t 7: View All Accounts\t 0: Logout");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) break;
            if (choice == 1) {
                handleCreateAccount(user);
            }
            if (choice == 2) {
                handleDeleteAccount(user);
            }
            if (choice == 3) {
                handleTransfer(user);
            }
            if (choice == 4) {
                handleFindAccount(user);
            }
            if (choice == 5) {
                handleChangePassword( user);
            }
            if (choice == 6) {
            	handleChangeAccountName(user);
            }
            if (choice == 7) {
            	handleViewAccounts(user);
            }
        }
    }
    
    private static void handleCreateAccount(User user) {
    	System.out.print("Type (checking/savings): ");
        String type = scanner.nextLine();
        System.out.print("Account name: ");
        String name = scanner.nextLine();
        try {
            user.createAccount(type, name);
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
    
    private static void handleDeleteAccount(User user) {
    	System.out.print("Account to delete: ");
        String name = scanner.nextLine();
        System.out.print("Are you sure? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            try { user.deleteAccount(name); } catch (Exception e) { System.out.println(e.getMessage()); }
        }
    }
    
    private static void handleTransfer(User user) {
        System.out.print("From account: "); 
        String from = scanner.nextLine();
        System.out.print("To account: "); 
        String to = scanner.nextLine();
        System.out.print("Amount: "); 
        double amt = Double.parseDouble(scanner.nextLine());
        System.out.print("Description (optional): ");
        String description = scanner.nextLine();
        try { 
            user.transfer(from, to, amt, description); 
        } catch (Exception e) { 
            System.out.println(e.getMessage()); 
        }
    }
    
    private static void handleFindAccount(User user) {
    	System.out.print("Account name: ");
        String name = scanner.nextLine();
        BankAccount acc = user.getAccount(name);
        if (acc == null) {
            System.out.println("No such account."); return;
        }
        accountMenu(acc);
    }
    
    private static void handleChangePassword(User user) {
    	System.out.print("New password: ");
        String newPass = scanner.nextLine();
        user.changePassword(newPass);
        System.out.println("Password changed!");
    }
    
    private static void handleChangeAccountName(User user){
    	System.out.print("Old account name: ");
        String oldName = scanner.nextLine();
        System.out.print("New account name: ");
        String newName = scanner.nextLine();
        try {
            user.renameAccount(oldName, newName);
            System.out.println("Account renamed.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void handleViewAccounts (User user) {
    	if (user.getAllAccountNames().isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (String accName : user.getAllAccountNames()) {
                BankAccount acc = user.getAccount(accName);
                System.out.println("- " + acc.getName() + " (" + acc.getAccountType() + "): $" + acc.getBalance());
            }
        }
    }
    
    
    

    private static void accountMenu(BankAccount acc) {
        while (true) {
            System.out.println("\nAccount Menu - " + acc.getName() + " (" + acc.getAccountType() + ")");
            System.out.println("1: Deposit  2: Withdraw  3: Balance  4: Full History  5: Recent Transactions  6: Search Transactions  0: Back");

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) break;
            if (choice == 1) {
                System.out.print("Amount: ");
                double amount = Double.parseDouble(scanner.nextLine());
                System.out.print("Description (optional): ");
                String description = scanner.nextLine();
                acc.deposit(amount, description);
            }
            if (choice == 2) {
                System.out.print("Amount: ");
                double amount = Double.parseDouble(scanner.nextLine());
                System.out.print("Description (optional): ");
                String description = scanner.nextLine();
                try { acc.withdraw(amount, description); }
                catch (Exception e) { System.out.println(e.getMessage()); }
            }
            if (choice == 3) {
                System.out.println("Balance: $" + acc.getBalance());
            }
            if (choice == 4) {
                acc.getTransactionHistory().forEach(System.out::println);
            }
            if (choice == 5) {
                System.out.print("How many transactions? ");
                int n = Integer.parseInt(scanner.nextLine());
                acc.getRecentTransactions(n).forEach(System.out::println);
            }
            if (choice == 6) {
                searchTransactionsMenu(acc);
            }
        }
    }

    private static void searchTransactionsMenu(BankAccount acc) {
        System.out.println("\nSearch Transactions:");
        System.out.println("1: By Amount");
        System.out.println("2: By Date Range");
        System.out.println("3: By Transaction Type");
        System.out.println("0: Back");
        
        int choice = Integer.parseInt(scanner.nextLine());
        List<Transaction> results = null;
        
        if (choice == 0) return;
        
        if (choice == 1) { 	results = searchByAmount(acc);  }
        
        if (choice == 2) {	results = searchByDate(acc);}
        
        if (choice == 3) {	results = searchByType(acc); }
        
        if (results != null) {
            System.out.println("\nSearch Results:");
            if (results.isEmpty()) {
                System.out.println("No matching transactions found.");
            } else {
                for (Transaction t : results) {
                    System.out.println(t);
                }
            }
        }
    }
    
    private static List<Transaction> searchByAmount(BankAccount acc) {
        try {
            System.out.print("Enter amount: $");
            double amount = Double.parseDouble(scanner.nextLine());
            return acc.searchByAmount(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return null;
        }
    }
    
    private static List<Transaction> searchByDate(BankAccount acc) {
        try {
            System.out.print("Start date (yyyy-MM-dd HH:mm): ");
            String startInput = scanner.nextLine();
            LocalDateTime start = LocalDateTime.parse(startInput,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            System.out.print("End date (yyyy-MM-dd HH:mm): ");
            String endInput = scanner.nextLine();
            LocalDateTime end = LocalDateTime.parse(endInput,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            return acc.searchByDateRange(start, end);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm");
            return null;
        }
    }
    
    private static List<Transaction> searchByType(BankAccount acc) {
        System.out.println("Transaction types: deposit, withdraw");
        System.out.print("Enter transaction type: ");
        String type = scanner.nextLine().trim().toLowerCase();
        return acc.searchByType(type);
    }

}
