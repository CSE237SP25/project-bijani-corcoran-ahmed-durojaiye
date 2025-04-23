package bankapp;

public class SavingsAccount extends BankAccount {
	private static final double MIN_BALANCE = 100.0;
	private double savingsGoal = 0.0;

	public SavingsAccount(String name) {
		super(name);
	}

	@Override
	public void withdraw(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount to withdraw must be positive");
		}
		if (getBalance() - amount < MIN_BALANCE) {
			throw new IllegalArgumentException(
					"Insufficient balance to maintain minimum balance of $100 required by all savings accounts.");
		}
		super.withdraw(amount);
	}

	public void setSavingsGoal(double goal) {
		if (goal < 0)
			throw new IllegalArgumentException("Savings goal cannot be negative");
		this.savingsGoal = goal;
	}

	public boolean goalReached() {
		return this.balance >= savingsGoal;
	}

	public double getSavingsGoal() {
		return savingsGoal;
	}

	public String getAccountType() {
		return "Savings";
	}

}