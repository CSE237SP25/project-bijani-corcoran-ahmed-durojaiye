package bankapp;

import java.time.LocalDateTime;

public class Transaction {

	private LocalDateTime currentTime;
	private double amount;
	private String transactionType;
	
	public Transaction(String transactionType, double amount){
		this.currentTime = LocalDateTime.now();
		this.transactionType = transactionType;
		this.amount = amount;
	}
	
	public String toString() {
		return transactionType + ": $" + amount + "\n";
	}
	
	public String getType() {
        return transactionType;
    }
    
    public double getAmount() {
        return amount;
    }
}
