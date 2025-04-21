package bankapp;

public class SavingsAccount extends BankAccount {
	private static final double MIN_BALANCE = 100.0;

	public SavingsAccount(String name) {
		super(name);
	}

	public String getAccountType() {
		return "Savings";
	}

	@Override
	public void withdraw(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount to withdraw must be positive");
		}
		if (getBalance() - amount < MIN_BALANCE) {
			throw new IllegalArgumentException("Insufficient balance to maintain minimum balance of $100 required by all savings accounts.");
		}
		super.withdraw(amount);
	}
}