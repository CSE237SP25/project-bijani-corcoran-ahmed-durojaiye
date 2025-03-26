package bankapp;
import java.util.Scanner;
import java.util.List;
import bankapp.BankAccount;
public class Menu {
	
	  private BankAccount theAccount;
	  private Scanner keyboardInput;
	 
	  public enum UserInputOptions {
	        DEPOSIT,
	        WITHDRAW,
	        CHECK_BALANCE,
	        TRANSACTION_HISTORY,
	        RECENT_TRANSACTIONS,
	        QUIT;
		  
		  public static UserInputOptions fromInt(int choice) {
	            switch (choice) {
	                case 1: return DEPOSIT;
	                case 2: return WITHDRAW;
	                case 3: return CHECK_BALANCE;
	                case 4: return TRANSACTION_HISTORY;
	                case 5: return RECENT_TRANSACTIONS;
	            	case 0: return QUIT;
	                default: return null;
	            }
	        }
	    }
	  
	    public Menu(){
	        theAccount = new BankAccount("a");
	        keyboardInput = new Scanner(System.in);
	    }
	    //display methods do not need to be tested
	    public void displayOptions(){
	        System.out.println("Please select an option:");
	        System.out.println("1. Deposit");
	        System.out.println("2. Withdraw");
	        System.out.println("3. Check Balance");
	        System.out.println("4. View Full Transaction History");
	        System.out.println("5. Select Number of Most Recent Transactions");
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
	             case TRANSACTION_HISTORY:
	                 handleTransactionHistory();
	                 break;
	             case RECENT_TRANSACTIONS:
	                 handleRecentTransactions();
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
	    	System.out.println("Current Balance: " + theAccount.getCurrentBalance());
	    }
	    
	    private void handleTransactionHistory() {
	        List<Transaction> history = theAccount.getTransactionHistory();
	        
	        if (history.isEmpty()) {
	            System.out.println("No transaction history available.");
	            return;
	        }
	        
	        System.out.println("Full Transaction history: ");
	        for (Transaction transaction : history) {
	            System.out.println(transaction.toString());
	        }
	    }
	    
	    private void handleRecentTransactions() {
	        List<Transaction> history = theAccount.getTransactionHistory();
	        
	        if (history.isEmpty()) {
	            System.out.println("No transaction history available.");
	            return;
	        }
	        
	        System.out.print("How many recent transactions would you like to see? ");
	        int count = (int) getUserAmount();
	        
	        if (count <= 0) {
	            System.out.println("Please enter a positive number.");
	            return;
	        }
	        
	        int startIndex = Math.max(0, history.size() - count);
	        List<Transaction> recentTransactions = history.subList(startIndex, history.size());
	        
	        System.out.println("\nMost recent " + recentTransactions.size() + " transactions:");
	        for (Transaction transaction : recentTransactions) {
	            System.out.println(transaction.toString());
	        }
	    }
	    
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
	             if(option == UserInputOptions.DEPOSIT || option == UserInputOptions.WITHDRAW) {
	            	 System.out.print("Enter the amount: ");
	                 amount = menu.getUserAmount();  // Get the amount for deposit/withdraw
	             }
	             if (!menu.processUserInput(option, amount)) {
	                 break;  
	             }
	         }
	         menu.keyboardInput.close();  
	    	  
	    }
}