package bankapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BankAccount {
	protected double balance;
	protected String name;
	protected List<Transaction> transactionHistory = new ArrayList<>();

	public BankAccount(String name) {
		this.name = name;
		this.balance = 0;
	}

	public void deposit(double amount) {
		if (amount <= 0)
			throw new IllegalArgumentException("Invalid deposit");
		balance += amount;
		transactionHistory.add(new Transaction("deposit", amount));
	}

	public void withdraw(double amount) {
		if (amount <= 0 || amount > balance)
			throw new IllegalArgumentException("Invalid withdraw");
		balance -= amount;
		transactionHistory.add(new Transaction("withdraw", amount));
	}

	public double getBalance() {
		return balance;
	}

	public List<Transaction> getTransactionHistory() {
		return transactionHistory;
	}

	public List<Transaction> getRecentTransactions(int n) {
		return transactionHistory.subList(Math.max(0, transactionHistory.size() - n), transactionHistory.size());
	}

	public List<Transaction> searchByAmount(double amount) {
		return transactionHistory.stream().filter(t -> Math.abs(t.getAmount() - amount) < 0.001)
				.collect(Collectors.toList());
	}

	public List<Transaction> searchByDateRange(LocalDateTime start, LocalDateTime end) {
		return transactionHistory.stream()
				.filter(t -> (t.getTimestamp().isEqual(start) || t.getTimestamp().isAfter(start))
						&& (t.getTimestamp().isEqual(end) || t.getTimestamp().isBefore(end)))
				.collect(Collectors.toList());
	}

	public List<Transaction> searchByType(String type) {
		return transactionHistory.stream().filter(t -> t.getTransactionType().equalsIgnoreCase(type))
				.collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public abstract String getAccountType();
}