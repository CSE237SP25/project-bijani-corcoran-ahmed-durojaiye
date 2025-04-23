package bankapp;

import java.util.Scanner;

public class CheckingAccount extends BankAccount {

	private boolean isFrozen = false;

	public CheckingAccount(String name) {
		super(name);
	}

	public void freeze() {
		isFrozen = true;
		System.out.println("Account is frozen.");
	}

	public void unfreeze(Scanner scanner) {
		if (promptConfirmation(scanner)) {
			this.isFrozen = false;
			System.out.println("Account unfrozen.");
		} else {
			System.out.println("Unfreeze cancelled.");
		}
	}

	@Override
	public void deposit(double amount, String description) {
		if (isFrozen) throw new IllegalStateException("Account is frozen. Cannot deposit.");
		super.deposit(amount, description);
	}

	@Override
	public void withdraw(double amount, String description) {
		if (isFrozen) throw new IllegalStateException("Account is frozen. Cannot withdraw.");
		super.withdraw(amount, description);
	}

	private boolean promptConfirmation(Scanner scanner) {
		System.out.print("Type 'confirm' to unfreeze: ");
		String input = scanner.nextLine();
		return input.equalsIgnoreCase("confirm");
	}
	

	public String getAccountType() {
		return "Checking";
	}

	public boolean isFrozen() {
		return isFrozen;
	}

}
