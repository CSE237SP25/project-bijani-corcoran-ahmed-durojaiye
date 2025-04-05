package bankapp;

public class SavingsAccount extends BankAccount {
    public SavingsAccount(String name) { super(name); }
    public String getAccountType() { return "Savings"; }
}