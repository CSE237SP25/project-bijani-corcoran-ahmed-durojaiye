package bankapp;

import java.util.Scanner;

public class Menu {
	
	  private BankAccount theAccount;
	 
	  public enum UserInputOptions {
	        DEPOSIT,
	        WITHDRAW,
	        CHECK_BALANCE;
		  
		  public static UserInputOptions fromInt(int choice) {
	            switch (choice) {
	                case 1: return DEPOSIT;
	                case 2: return WITHDRAW;
	                case 3: return CHECK_BALANCE;
	                default: return null;
	            }
	        }
	    }
	  

	    public Menu(){
	        theAccount = new BankAccount();
			theAccount.authenticateUser();
	    }

	    //display methods do not need to be tested
	    public void displayOptions(){
	        System.out.println("Please select an option:");
	        System.out.println("1. Deposit");
	        System.out.println("2. Withdraw");
	        System.out.println("3. Check Balance");
	    }

	    public UserInputOptions getUserChoice(){
	        try (Scanner keyboardInput = new Scanner(System.in)) {
	        	int choice = keyboardInput.nextInt();
	        	return UserInputOptions.fromInt(choice);
			}
	    }

	    //methods that require user input don't need to be tested
	    public double getUserAmount(){
	        try (Scanner keyboardInput = new Scanner(System.in)) {
				return keyboardInput.nextDouble();
			}
	    }

	    //can and should be tested
	    public void processUserInput(UserInputOptions choice, double amount){
	    	if (choice == null) {
	             System.out.println("Invalid option.");
	             return;
	         }
	         
	         switch (choice) {
	             case DEPOSIT:
	                 handleDeposit(amount);
	                 break;
	             case WITHDRAW:
	                 handleWithdraw(amount);
	                 break;
	             case CHECK_BALANCE:
	            	 handleCheckBalance();
	            	 break;
			default:
				break;
	         }
	        
	    }
	    
	    private void handleDeposit(double amount) {
	        theAccount.deposit(amount);
	        System.out.println("Deposited: " + amount);
	    }
	    
	    private void handleWithdraw(double amount) {
	        try {
	            theAccount.withdraw(amount);
	            System.out.println("Withdrew: " + amount);
	        } catch (IllegalArgumentException e) {
	            System.out.println("Invalid withdrawal amount.");
	        }
	    }
	    
	    private void handleCheckBalance() {
	    	System.out.println("Current Balance: " + theAccount.getCurrentBalance());	    }

	    public BankAccount getAccount(){
	        return theAccount;
	    }


}
