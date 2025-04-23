package bankapp;

import java.util.Scanner;

public class CheckingAccount extends BankAccount {

	private boolean isFrozen = false;

	public CheckingAccount(String name) {
		super(name);
	}

	public String getAccountType() {
		return "Checking";
	}

	public void freeze() {
		isFrozen = true;
		System.out.println("Account is frozen.");
	}

	public void unfreeze() {
		if (promptConfirmation()) {
			this.isFrozen = false;
			System.out.println("Account unfrozen.");
		} else {
			System.out.println("Unfreeze cancelled.");
		}
	}

	public boolean isFrozen() {
		return isFrozen;
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

	private boolean promptConfirmation() {
		System.out.print("Type 'confirm' to unfreeze: ");
		try (Scanner scanner = new Scanner(System.in)) {
			String input = scanner.nextLine();
			return input.equalsIgnoreCase("confirm");
		}
	}

}
