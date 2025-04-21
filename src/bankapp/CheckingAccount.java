package bankapp;

public class CheckingAccount extends BankAccount {
	public CheckingAccount(String name) {
		super(name);
	}

	public String getAccountType() {
		return "Checking";
	}
}
