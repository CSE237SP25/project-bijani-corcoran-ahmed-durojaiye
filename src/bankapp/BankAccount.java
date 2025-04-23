package bankapp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;


public abstract class BankAccount {
    protected double balance;
    protected String name;
    protected List<Transaction> transactionHistory = new ArrayList<>();

    public BankAccount(String name) {
        this.name = name;
        this.balance = 0;
    }

    public void deposit(double amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid deposit");
        balance += amount;
        transactionHistory.add(new Transaction("deposit", amount, description));
    }
    
    public void deposit(double amount) {
        deposit(amount, "");
    }
    
    public void withdraw(double amount, String description) {
        if (amount <= 0 || amount > balance) throw new IllegalArgumentException("Invalid withdraw");
        balance -= amount;
        transactionHistory.add(new Transaction("withdraw", amount, description));
    }
    
    public void withdraw(double amount) {
        withdraw(amount, "");
    }

    public double getBalance() { return balance; }

    public List<Transaction> getTransactionHistory() { return transactionHistory; }

    public List<Transaction> getRecentTransactions(int n) {
        return transactionHistory.subList(Math.max(0, transactionHistory.size() - n), transactionHistory.size());
    }

    public List<Transaction> searchByAmount(double amount) {
        return transactionHistory.stream()
            .filter(t -> Math.abs(t.getAmount() - amount) < 0.001)
            .collect(Collectors.toList());
    }
    
    public List<Transaction> searchByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionHistory.stream()
            .filter(t -> (t.getTimestamp().isEqual(start) || t.getTimestamp().isAfter(start)) 
                      && (t.getTimestamp().isEqual(end) || t.getTimestamp().isBefore(end)))
            .collect(Collectors.toList());
    }
    
    public List<Transaction> searchByType(String type) {
        return transactionHistory.stream()
            .filter(t -> t.getTransactionType().equalsIgnoreCase(type))
            .collect(Collectors.toList());
    }

    public String getName() { return name; }

    public void setName(String newName) {
        this.name = newName;
    }

    public abstract String getAccountType();

    public void exportTransactionHistory(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Transaction History for " + name + " (" + getAccountType() + ")\n");
            writer.write("Balance: $" + balance + "\n");
            writer.write("--------------------------------------\n");
            
            for (Transaction transaction : transactionHistory) {
                writer.write(transaction.toString() + "\n");
            }
        }
        System.out.println("Transaction history exported to " + Paths.get(filename).toAbsolutePath());
    }
}