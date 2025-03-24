package bankapp;

import java.util.Scanner;
import bankapp.BankAccount;


public class Menu {
	
	  private BankAccount theAccount;
	    private Scanner keyboardInput;

	 
	  public enum UserInputOptions {
	        DEPOSIT,
	        WITHDRAW,
	        CHECK_BALANCE,
	        QUIT;
		  
		  public static UserInputOptions fromInt(int choice) {
	            switch (choice) {
	                case 1: return DEPOSIT;
	                case 2: return WITHDRAW;
	                case 3: return CHECK_BALANCE;
	            	case 0: return QUIT;
	                default: return null;
	            }
	        }
	    }
	  

	    public Menu(){
	        theAccount = new BankAccount();
	        keyboardInput = new Scanner(System.in);
	    }

	    //display methods do not need to be tested
	    public void displayOptions(){
	        System.out.println("Please select an option:");
	        System.out.println("1. Deposit");
	        System.out.println("2. Withdraw");
	        System.out.println("3. Check Balance");
	        System.out.println("0. Quit");
	        System.out.println("Example Input: To deposit, type '1' and then the amount (e.g., 1 100.50).");


	    }

	    public UserInputOptions getUserChoice(){
	        int choice = keyboardInput.nextInt();
	        return UserInputOptions.fromInt(choice);
	
	    }

	    //methods that require user input don't need to be tested
	    public double getUserAmount(){
	        return keyboardInput.nextDouble();
	    }

	    //can and should be tested
	    public boolean processUserInput(UserInputOptions choice, double amount){
	    	if (choice == null) {
	             System.out.println("Invalid option.");
	             return true;
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
	             case QUIT:
	                 System.out.println("Exiting program...");
	                 return false;
			default:
				break;
	         }
	         
	        return true;
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
	    
	    
	    public static void main(String[] args) {
	    	 Menu menu = new Menu();

	         while (true) {
	             menu.displayOptions();  // Display the menu options

	             // Get the user's choice
	             UserInputOptions option = menu.getUserChoice();
            	 
	             double amount = 0;  // Default value in case of balance check

	             if(option != UserInputOptions.CHECK_BALANCE && option != UserInputOptions.QUIT) {
	            	 System.out.print("Enter the amount: ");
	            	 if (option != UserInputOptions.CHECK_BALANCE) {
	            		 amount = menu.getUserAmount();  // Get the amount for deposit/withdraw
	            	 }
	             }

	             if (!menu.processUserInput(option, amount)) {
	                 break;  
	             }
	         }

	         menu.keyboardInput.close();  
	    	  
	    }
}


