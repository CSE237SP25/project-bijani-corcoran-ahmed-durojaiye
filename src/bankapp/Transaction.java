package bankapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
	private LocalDateTime timestamp;
	private double amount;
	private String transactionType;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public Transaction(String transactionType, double amount) {
		this(transactionType, amount, "");
	}

	public Transaction(String transactionType, double amount, String description) {
		this.timestamp = LocalDateTime.now();
		this.transactionType = transactionType;
		this.amount = amount;
	}

	// Getters for search functionality
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public double getAmount() {
		return amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public String toString() {
		return timestamp.format(FORMATTER) + " - " + transactionType + ": $" + amount;
	}
}