package bankapp;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankApp {
    private static Map<String, User> userDB = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome. 1: Login 2: Create User 0: Exit");
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
            System.out.println("1: Create Account 2: Delete Account 3: Transfer 4: Go Into Account 5: Change Password 6: Rename Account 0: Logout");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) break;
            if (choice == 1) {
                System.out.print("Type (checking/savings): ");
                String type = scanner.nextLine();
                System.out.print("Account name: ");
                String name = scanner.nextLine();
                try {
                    user.createAccount(type, name);
                } catch (Exception e) { System.out.println(e.getMessage()); }
            }
            if (choice == 2) {
                System.out.print("Account to delete: ");
                String name = scanner.nextLine();
                System.out.print("Are you sure? (yes/no): ");
                if (scanner.nextLine().equalsIgnoreCase("yes")) {
                    try { user.deleteAccount(name); } catch (Exception e) { System.out.println(e.getMessage()); }
                }
            }
            if (choice == 3) {
                System.out.print("From account: "); String from = scanner.nextLine();
                System.out.print("To account: "); String to = scanner.nextLine();
                System.out.print("Amount: "); double amt = Double.parseDouble(scanner.nextLine());
                try { user.transfer(from, to, amt); } catch (Exception e) { System.out.println(e.getMessage()); }
            }
            if (choice == 4) {
                System.out.print("Account name: ");
                String name = scanner.nextLine();
                BankAccount acc = user.getAccount(name);
                if (acc == null) {
                    System.out.println("No such account."); continue;
                }
                accountMenu(acc);
            }
            if (choice == 5) {
                System.out.print("New password: ");
                String newPass = scanner.nextLine();
                user.changePassword(newPass);
                System.out.println("Password changed!");
            }
            if (choice == 6) {
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
        }
    }

    private static void accountMenu(BankAccount acc) {
        while (true) {
            System.out.println("\nAccount Menu - " + acc.getName() + " (" + acc.getAccountType() + ")");
            System.out.println("1: Deposit 2: Withdraw 3: Balance 4: Full History 5: Recent Transactions 0: Back");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) break;
            if (choice == 1) {
                System.out.print("Amount: ");
                acc.deposit(Double.parseDouble(scanner.nextLine()));
            }
            if (choice == 2) {
                System.out.print("Amount: ");
                try { acc.withdraw(Double.parseDouble(scanner.nextLine())); }
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
        }
    }
}
