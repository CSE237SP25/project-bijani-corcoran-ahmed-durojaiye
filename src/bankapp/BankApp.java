package bankapp;

import java.io.Console;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BankApp {
	// Instance variables
	private static Map<String, User> userDB = new HashMap<>();
	private static Scanner scanner = new Scanner(System.in);

	// Entry point
	public static void main(String[] args) {
		while (true) {
			System.out.println("Welcome.  1: Login  2: Create User  0: Exit");
			int choice = readInt();
			switch (choice) {
				case 1 -> login();
				case 2 -> createUser();
				case 0 -> {
					System.out.println("Goodbye!");
					return;
				}
				default -> System.out.println("Invalid option.");
			}
		}
	}

	// ===== PUBLIC FACING METHODS =====
	public static boolean isUserInvalidForLogin(User user) {
		return user == null || user.accountIsLocked();
	}

	public static void seeUserSummary(User user) {
		user.printSummary();
	}

	// ===== PRIVATE: Account/User Menus =====
	private static void createUser() {
		String username = prompt("Choose username: ");
		String password, confirmPassword;
		while (true) {
			password = readPassword("Choose password: ");
			confirmPassword = readPassword("Confirm password: ");
			if (password.equals(confirmPassword))
				break;
			System.out.println("Passwords do not match. Try again.");
		}
		if (userDB.containsKey(username)) {
			System.out.println("Username already exists");
			return;
		}
		userDB.put(username, new User(username, password));
		System.out.println("User created!");
	}

	private static void login() {
		String username = prompt("Username: ");
		User user = userDB.get(username);
		if (isUserInvalidForLogin(user)) {
			printLoginError(user);
			return;
		}
		String password = readPassword("Password: ");
		if (!user.checkPassword(password)) {
			user.registerFailedLogin();
			printFailedLoginFeedback(user);
			return;
		}
		user.resetFailedAttempts();
		userMenu(user);
	}

	private static void userMenu(User user) {
		while (true) {
			printUserMenu(user.getUsername());
			int choice = readInt();
			switch (choice) {
				case 0 -> {return;}
				case 1 -> handleCreateAccount(user);
				case 2 -> handleDeleteAccount(user);
				case 3 -> handleTransfer(user);
				case 4 -> handleAccessAccount(user);
				case 5 -> handleChangePassword(user);
				case 6 -> handleRenameAccount(user);
				case 7 -> handleViewAllAccounts(user);
				case 8 -> seeUserSummary(user);
				default -> System.out.println("Invalid option.");
			}
		}
	}

	private static void accountMenu(BankAccount acc) {
		while (true) {
			printAccountMenu(acc);
			int choice = readInt();
			switch (choice) {
				case 0 -> {return;}
				case 1 -> handleDeposit(acc);
				case 2 -> handleWithdraw(acc);
				case 3 -> System.out.println("Balance: $" + acc.getBalance());
				case 4 -> acc.getTransactionHistory().forEach(System.out::println);
				case 5 -> acc.getRecentTransactions(readInt("How many transactions? ")).forEach(System.out::println);
				case 6 -> searchTransactionsMenu(acc);
				case 7 -> handleExportTransactions(acc);
				case 8 -> handleFreezeAccount(acc);
				case 9 -> handleUnfreezeAccount(acc);
				case 10 -> handleSetSavingsGoal(acc);
				default -> System.out.println("Invalid option.");
			}
		}
	}

	// ===== PRIVATE: Account & User Action Handlers =====
	private static void handleCreateAccount(User user) {
		String type = prompt("Type (checking/savings): ");
		String name = prompt("Account name: ");
		try {
			user.createAccount(type, name);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void handleDeleteAccount(User user) {
		String name = prompt("Account to delete: ");
		if (prompt("Are you sure? (yes/no): ").equalsIgnoreCase("yes")) {
			try {
				user.deleteAccount(name);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private static void handleTransfer(User user) {
		String from = prompt("From account: ");
		String to = prompt("To account: ");
		double amt = readDouble("Amount: ");
		String description = prompt("Description (optional): ");
		try {
			user.transfer(from, to, amt, description);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void handleAccessAccount(User user) {
		String name = prompt("Account name: ");
		BankAccount acc = user.getAccount(name);
		if (acc == null) {
			System.out.println("No such account.");
		} else {
			accountMenu(acc);
		}
	}

	private static void handleChangePassword(User user) {
		String newPass = prompt("New password: ");
		user.changePassword(newPass);
		System.out.println("Password changed!");
	}

	private static void handleRenameAccount(User user) {
		String oldName = prompt("Old account name: ");
		String newName = prompt("New account name: ");
		try {
			user.renameAccount(oldName, newName);
			System.out.println("Account renamed.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void handleViewAllAccounts(User user) {
		if (user.getAllAccountNames().isEmpty()) {
			System.out.println("No accounts found.");
			return;
		}
		for (String accName : user.getAllAccountNames()) {
			BankAccount acc = user.getAccount(accName);
			System.out.println("- " + acc.getName());
		}
	}

	private static void handleDeposit(BankAccount acc) {
		try {
			double amount = readDouble("Amount (numbers only): ");
			String description = prompt("Description (optional): ");
			acc.deposit(amount, description.isEmpty() ? "" : description);
		} catch (Exception e) {
			System.out.println("⚠️ " + e.getMessage());
		}
	}
	
	private static void handleWithdraw(BankAccount acc) {
		try {
			double amount = readDouble("Amount (numbers only): ");
			String description = prompt("Description (optional): ");
			acc.withdraw(amount, description.isEmpty() ? "" : description);
		} catch (Exception e) {
			System.out.println("⚠️ " + e.getMessage());
		}
	}	

	private static void handleFreezeAccount(BankAccount acc) {
		if (acc instanceof CheckingAccount checking) {
			checking.freeze();
		} else {
			System.out.println("Only checking accounts can be frozen.");
		}
	}

	private static void handleUnfreezeAccount(BankAccount acc) {
		if (acc instanceof CheckingAccount checking) {
			checking.unfreeze(scanner);
		} else {
			System.out.println("Only checking accounts can be unfrozen.");
		}
	}

	private static void handleSetSavingsGoal(BankAccount acc) {
		if (acc instanceof SavingsAccount savings) {
			double goal = readDouble("Enter your new savings goal: ");
			savings.setSavingsGoal(goal);
			System.out.println("Savings goal set to $" + goal);
		} else {
			System.out.println("Only savings accounts can have savings goals.");
		}
	}

	private static void handleExportTransactions(BankAccount acc) {
		String filename = prompt("Enter filename for export: ");
		try {
			acc.exportTransactionHistory(filename);
			System.out.println("Transaction history exported successfully!");
		} catch (IOException e) {
			System.out.println("Error exporting transaction history: " + e.getMessage());
		}
	}

	// ===== PRIVATE: Search Functionality =====
	private static void searchTransactionsMenu(BankAccount acc) {
		printSearchMenu();
		int choice = readInt();
		List<Transaction> results = switch (choice) {
			case 1 -> acc.searchByAmount(readDouble("Enter amount: $"));
			case 2 -> searchByDateRange(acc);
			case 3 -> acc.searchByType(prompt("Enter transaction type (deposit/withdraw): "));
			case 0 -> null;
			default -> {
				System.out.println("Invalid option.");
				yield null;
			}
		};
		printSearchResults(results);
	}

	private static List<Transaction> searchByDateRange(BankAccount acc) {
		try {
			LocalDateTime start = LocalDateTime.parse(prompt("Start date (yyyy-MM-dd HH:mm): "),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			LocalDateTime end = LocalDateTime.parse(prompt("End date (yyyy-MM-dd HH:mm): "),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			return acc.searchByDateRange(start, end);
		} catch (Exception e) {
			System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm");
			return Collections.emptyList();
		}
	}

	// ===== PRIVATE: Output / Prompting =====
	private static void printUserMenu(String username) {
		System.out.println("\nUser Menu - Logged in as: " + username);
		System.out.println(
				"1: Create Account 2: Delete Account 3: Transfer 4: Manage Account Funds 5: Change Password 6: Rename Account 7: View All Accounts 8: See User Summary 0: Logout");
	}

	private static void printAccountMenu(BankAccount acc) {
		System.out.println("\nAccount Menu - " + acc.getName() + " (" + acc.getAccountType() + ")");
		System.out.println(
				"1: Deposit 2: Withdraw 3: Balance 4: Full History 5: Recent Transactions 6: Search Transactions 7: Export Transactions 8: Freeze Account 9: Unfreeze Account 10: Set Savings Goal 0: Back");
	}

	private static void printSearchMenu() {
		System.out.println("\nSearch Transactions:");
		System.out.println("1: By Amount");
		System.out.println("2: By Date Range");
		System.out.println("3: By Transaction Type");
		System.out.println("0: Back");
	}

	private static void printSearchResults(List<Transaction> results) {
		System.out.println("\nSearch Results:");
		if (results == null || results.isEmpty()) {
			System.out.println("No matching transactions found.");
		} else {
			results.forEach(System.out::println);
		}
	}

	private static void printLoginError(User user) {
		if (user == null)
			System.out.println("Invalid credentials");
		else
			System.out.println("Account is locked due to too many failed login attempts.");
	}

	private static void printFailedLoginFeedback(User user) {
		if (user.accountIsLocked())
			System.out.println("Too many failed attempts. Your account is now locked.");
		else
			System.out.println("Invalid credentials");
	}

	// ===== PRIVATE: Input Reading =====
	private static String prompt(String message) {
		System.out.print(message);
		return scanner.nextLine();
	}

	private static String readPassword(String message) {
		System.out.print(message);
		Console console = System.console();
		if (console != null) {
			char[] passwordChars = console.readPassword();
			return new String(passwordChars);
		} else {
			return scanner.nextLine();
		}
	}

	private static int readInt() {
        if (!scanner.hasNextLine()) {
            System.out.println("⚠️ No more input available. Exiting test.");
            return 0;  // Or throw or exit
        }
        return Integer.parseInt(scanner.nextLine());
    }

	private static int readInt(String message) {
		System.out.print(message);
		return Integer.parseInt(scanner.nextLine());
	}

	private static double readDouble(String message) {
		System.out.print(message);
		return Double.parseDouble(scanner.nextLine());
	}

	public Map<String, User> getUserDB() {
		return userDB;
	}

	public void setUserDB(Map<String, User> userDB) {
		BankApp.userDB = userDB;
	}

}
