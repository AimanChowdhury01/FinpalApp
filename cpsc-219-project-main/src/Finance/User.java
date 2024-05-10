package Finance;

import java.util.*;

/**
 * The User class represents an entity for managing personal finance activities within the FinPal application.
 * It encapsulates user details and behaviors, providing functionalities for user authentication,
 * account management, and transaction handling.
 */
public class User {
    // User-specific attributes to maintain identity and transaction history.
    private final String username; // A unique identifier for each user.
    private final String password; // A security measure for user authentication.
    private final String nickname; // A casual name for user interaction purposes.
    // A list to store financial transactions made by the user.
    private final List<Transaction> transactions;
    //
    private final LinkedHashMap<String, Double> budgetLimits;
    //
    private final List<RecurringTransaction> recurringTransactions;
    // Object to parse input into strings
    private final Scanner scanner;

    /**
     * Constructs a new User instance with specified credentials and an empty list for transactions.
     * @param username The user's unique login identifier.
     * @param password The user's secret password for account security.
     * @param nickname A friendly name for the user to enhance personalization.
     */
    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        // Initialize the budgetLimits HashMap in the User constructor
        this.transactions = new ArrayList<>();
        this.budgetLimits = new LinkedHashMap<>();
        this.recurringTransactions = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    /**
     * Manages the user's session, offering an interactive menu for performing various finance-related operations.
     * This static method allows users to navigate through different functionalities like recording transactions,
     * viewing financial insights, generating financial statements, accessing help, or exiting the session.
     */
    public void manageSession() {
        boolean exit = false;
        while (!exit) {
            // prompt user for option
            String[] choices = new String[]{"1", "2", "3", "4", "5", "6","7"};
            String choice = "";
            while (!Arrays.asList(choices).contains(choice)) {
                System.out.println();
                System.out.println("What would you like to do?");
                System.out.println("1. Record a new transaction");
                System.out.println("2. View financial insights");
                System.out.println("3. View financial statement");
                System.out.println("4. Budget Manager");
                System.out.println("5. Manage recurring transactions");
                System.out.println("6. View help message");
                System.out.println("7. Exit ");
                choice = scanner.nextLine();
                System.out.println();

                if (!Arrays.asList(choices).contains(choice)) {
                    System.out.println("Invalid option. Select an input from " + Arrays.toString(choices));
                }
            }
            // call functions depending on user input
            switch (choice) {
                case "1":
                    recordTransaction(); // Handle new transaction recording.
                    break;
                case "2":
                    displayInsights(); // Display financial insights to the user.
                    break;
                case "3":
                    displayFinancialStatement(); // Generate and display financial statement.
                    break;
                case "4":
                    setBudgetForCategory();
                    break;
                case "5":
                    manageRecurringTransactions();
                    break;
                case "6":
                    UserUtility.displayHelp(); // Provide help information about application usage.
                    break;
                case "7":
                    exit = true; // Set flag to exit the session loop.
                    break;
                default:
                    System.out.println("Invalid option. Please try again."); // Notify invalid menu choice.
            }
        }
    }

    protected void manageRecurringTransactions() {
        System.out.println("Manage Recurring Transactions:");
        System.out.println("1. Add a new recurring transaction");
        System.out.println("2. View recurring transactions");
        // Additional options like editing or removing can be added here

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                addRecurringTransaction();
                break;
            case "2":
                viewRecurringTransactions();
                break;
            // Handle other cases
        }
    }

    /**
     * Adds a new recurring transaction for the user, prompting for category, amount, frequency, and type.
     * The projected expense or income is calculated based on the specified frequency.
     */
    protected void addRecurringTransaction() {
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter frequency (daily, weekly, bi-weekly, monthly): ");
        String frequency = scanner.nextLine();

        System.out.print("Is this an (I)ncome or an (E)xpense? (I/E): ");
        String type = scanner.nextLine().trim().toUpperCase();
        while (!type.equals("I") && !type.equals("E")) {
            System.out.println("Invalid option. Please enter 'I' for Income or 'E' for Expense.");
            type = scanner.nextLine().trim().toUpperCase();
        }
        type = type.equals("I") ? "Income" : "Expense";

        // Creating and adding the recurring transaction with projected monthly amount
        RecurringTransaction recurringTransaction = new RecurringTransaction(category, amount, frequency, type);
        addRecurringTransaction(recurringTransaction);
        System.out.println("Recurring transaction added with monthly projection.");
        // Update user data in storage.
        FileManager.updateUser(this);
    }


    protected void viewRecurringTransactions() {
        System.out.println("Recurring Transactions:");
        System.out.println("[Category], [Amount], [Frequency], [Type]");
        for (RecurringTransaction rt : getRecurringTransactions()) {
            System.out.println(rt);
        }

        if (getRecurringTransactions().isEmpty()) {
            System.out.println("No recurring transactions found.");
        }
    }


    protected void recordTransaction() {
        System.out.print("Enter transaction category (e.g., Food, Rent): ");
        String category = scanner.nextLine(); // Capture the category of the transaction.

        // Prompt the user to specify if the transaction is an income (I) or an expense (E) and validate input.
        String type = "";
        while (!type.equals("I") && !type.equals("E")) {
            System.out.print("Is this an (I)ncome or an (E)xpense? ");
            type = scanner.nextLine().trim().toUpperCase();
            if (!type.equals("I") && !type.equals("E")) {
                System.out.println("Invalid option. Select an input from [I, E]");
            }
        }
        type = type.equals("I") ? "Income" : "Expense"; // Convert input to meaningful transaction type.

        // Prompt for transaction amount with input validation to ensure a valid double is entered.
        double amount;
        while (true) {
            System.out.print("Enter amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                break; // Exit loop if input is a valid double.
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Input a valid floating point number.");
            }
        }
        // Inside the recordTransaction method, after capturing the amount and before adding the transaction
        if ("Expense".equals(type)) {
            Double budgetLimit = this.getBudgetLimits().getOrDefault(category, Double.MAX_VALUE);
            if (amount > budgetLimit) {
                System.out.println("Warning: This expense exceeds your budget limit for " + category);
            } else {
                // Check if adding this transaction will exceed the total budget for the category
                double totalSpentOnCategory = this.getTransactions().stream()
                        .filter(t -> t.getCategory().equals(category) && "Expense".equals(t.getType()))
                        .mapToDouble(Transaction::getAmount)
                        .sum();
                if (totalSpentOnCategory + amount > budgetLimit) {
                    System.out.println("Warning: Adding this transaction will exceed your budget for " + category);
                }
            }
        }


        // Create a new Transaction object and add it to the user's transaction list.
        Transaction transaction = new Transaction(category, amount, type);
        addTransaction(transaction);
        FileManager.updateUser(this); // Update user data in storage.
        System.out.println("Transaction recorded successfully.");
    }

    /**
     * Calculates and displays the total income, expenses, and net savings for the user based on their transaction history.
     * Additionally, it provides insights related to the user's budget, including areas where expenses have exceeded budget limits,
     * and offers advice on managing expenses better relative to set budgets.
     */
    protected void displayInsights() {
        double totalIncome = 0;
        double totalExpenses = 0;
        // Initialize maps to track expenses and income by category
        Map<String, Double> categoryExpenses = new HashMap<>();
        Map<String, Double> categoryIncome = new HashMap<>();

        // Process one-time transactions
        for (Transaction transaction : getTransactions()) {
            if ("Income".equals(transaction.getType())) {
                totalIncome += transaction.getAmount();
                categoryIncome.put(transaction.getCategory(), categoryIncome.getOrDefault(transaction.getCategory(), 0.0) + transaction.getAmount());
            } else if ("Expense".equals(transaction.getType())) {
                totalExpenses += transaction.getAmount();
                categoryExpenses.put(transaction.getCategory(), categoryExpenses.getOrDefault(transaction.getCategory(), 0.0) + transaction.getAmount());
            }
        }
        // Process recurring transactions for the next month (as an example)
        for (RecurringTransaction rt : getRecurringTransactions()) {
            if ("Income".equals(rt.getType())) {
                totalIncome += rt.getAmount() * UserUtility.calculateFrequencyMultiplier(rt.getFrequency());
                categoryIncome.put(rt.getCategory(), categoryIncome.getOrDefault(rt.getCategory(), 0.0) + rt.getAmount());
            } else if ("Expense".equals(rt.getType())) {
                totalExpenses += rt.getAmount() * UserUtility.calculateFrequencyMultiplier(rt.getFrequency());
                categoryExpenses.put(rt.getCategory(), categoryExpenses.getOrDefault(rt.getCategory(), 0.0) + rt.getAmount());
            }
        }

        // Display total income, expenses, and net savings
        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expenses: $" + totalExpenses);
        System.out.println("Net Savings: $" + (totalIncome - totalExpenses));

        // Offer advice based on the comparison of expenses with budget limits for each category
        for (Map.Entry<String, Double> entry : getBudgetLimits().entrySet()) {
            String category = entry.getKey();
            Double budget = entry.getValue();
            Double spent = categoryExpenses.getOrDefault(category, 0.0);

            // Advice for categories where spending exceeds budget
            if (spent > budget) {
                System.out.println("Consider reducing expenses in " + category + ". You've spent $" + String.format("%.2f", spent)
                        + ", which is over your budget of $" + String.format("%.2f", budget) + ".");
            }
            // Encouragement for categories where spending is within budget
            else if (spent > 0) {
                System.out.println("Good job on managing your " + category + " expenses. You've spent $"
                        + String.format("%.2f", spent) + " out of your budget of $" + String.format("%.2f", budget) + ".");
            }
        }
    }


    /**
     * Generates and displays a detailed financial statement for the user,
     * listing all transactions and summarizing total income and expenses.
     */
    protected void displayFinancialStatement() {
        System.out.println("Upcoming Financial Statement:");
        double projectedIncome = 0, projectedExpenses = 0;

        // Process and display one-time transactions
        System.out.println("[DATE], [CATEGORY], [AMOUNT], [TYPE]");
        for (Transaction t : getTransactions()) {
            System.out.println(t);
            if ("Income".equals(t.getType())) projectedIncome += t.getAmount();
            else projectedExpenses += t.getAmount();
        }

        // Process and display recurring transactions for the next month
        for (RecurringTransaction rt : getRecurringTransactions()) {
            System.out.println("Next Month " + rt.getCategory() + " (Recurring): " + (rt.getType().equals("Income") ? "+" : "-") + rt.getAmount());
            if ("Income".equals(rt.getType())) projectedIncome += rt.getAmount();
            else projectedExpenses += rt.getAmount();
        }

        System.out.println("Projected Total Income: $" + projectedIncome);
        System.out.println("Projected Total Expenses: $" + projectedExpenses);
        System.out.println("Projected Net Balance: $" + (projectedIncome - projectedExpenses));
    }

    protected void setBudgetForCategory() {
        System.out.println("Available Categories: [Food, Utilities, Rent, Entertainment, Transportation, Other]");
        System.out.print("Enter category to set budget for: ");
        String category = scanner.nextLine();
        System.out.print("Enter budget limit for " + category + ": ");
        // Capture the budget limit.
        double limit = -1;
        while (limit < 0) {
            try {
                limit = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Input a valid non-negative floating point number.");
            }
            if (limit < 0) {
                System.out.println("Invalid option. Input a valid non-negative floating point number.");
            }
        }


        // Calculate total expenses in the category from one-time transactions
        double totalExpenses = getTransactions().stream()
                .filter(t -> t.getCategory().equals(category) && "Expense".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        // Add total expenses from recurring transactions in the same category
        totalExpenses += getRecurringTransactions().stream()
                .filter(rt -> rt.getCategory().equals(category) && "Expense".equals(rt.getType()))
                .mapToDouble(rt -> rt.getAmount() * UserUtility.calculateFrequencyMultiplier(rt.getFrequency()))
                .sum();

        // Set the budget limit for the category
        getBudgetLimits().put(category, limit);
        System.out.println("Budget set for " + category + ": $" + limit);

        // Warn the user if their budget limit is lower than their total expenses in that category
        if (totalExpenses > limit) {
            System.out.println("Warning: Your current spending in " + category + " is $" + totalExpenses +
                    ", which exceeds your new budget limit. Consider adjusting your budget or expenses.");
        }

        FileManager.updateUser(this);
    }

    /**
     * Adds a transaction to this user's list of transactions.
     *
     * @param transaction The transaction to add.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Adds a recurring transaction to the list of recurring transactions
     * @param recurringTransaction instance of a recurring transaction
     */
    public void addRecurringTransaction(RecurringTransaction recurringTransaction) {
        recurringTransactions.add(recurringTransaction);
    }

    // Getter methods
    /**
     * Returns the username of this user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of this user. Note: Real applications should not expose passwords in this manner.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the nickname of this user.
     *
     * @return The nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns an unmodifiable list of transactions for this user.
     *
     * @return The list of transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     *  Returns an unmodifiable list of recurring transactions for this user
     * @return the list of recurring transaction
     */
    public List<RecurringTransaction> getRecurringTransactions() {
        return recurringTransactions;
    }

    /**\
     * Returns an immutable collection of budget limits for this user
     * @return the budget limits represented as doubles mapped to a string
     */
    public LinkedHashMap<String, Double> getBudgetLimits() {
        return budgetLimits;
    }

    /**
     * Provides a string representation of this user, including username, password, and nickname.
     * Note: Including sensitive information like password in the string representation is not recommended.
     * @return A string representation of the user.
     */
    @Override
    public String toString() {
        return username + "," + password + "," + nickname;
    }

    /**
     * Compares User object with another object to check if all fields are equal
     * @param o Object to compare and check equality with
     * @return true if each field is equal to eachother; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!getUsername().equals(user.getUsername())) return false;
        if (!getPassword().equals(user.getPassword())) return false;
        if (!getNickname().equals(user.getNickname())) return false;
        if (!getTransactions().equals(user.getTransactions())) return false;
        if (!getBudgetLimits().equals(user.getBudgetLimits())) return false;
        return getRecurringTransactions().equals(user.getRecurringTransactions());
    }
}