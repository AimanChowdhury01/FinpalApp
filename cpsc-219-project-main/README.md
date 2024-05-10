# Personal Finance Tracker
## Description
This Java application helps users manage their finances by creating accounts, recording transactions, and generating financial insights and statements. It features user-friendly prompts, data persistence, and informative displays.

## Key Features
- Account Creation: Users can create personalized accounts with nicknames.
- Transaction Recording: Users can categorize, amount, and type (income/expense) of transactions.
- Financial Insights: Users can view total income, expenses, and net savings.
- Financial Statement: Users can see a detailed breakdown of their transactions with income/expense markers and calculated totals and also gives projected totals for a month.
- Persistence: Transaction data is saved locally for future access.
- Budget Manager: Set and adjust budget limits for different categories to manage your spending effectively.
- Manage recurring transactions - Add, view, or modify recurring transactions for consistent expenses or income.
- View help message - Displays this help information anytime you need guidance on using the application.

## Installation
- Obtain the Java source code files (`User.java`, `Transaction.java`, `FileManager.java`, etc.).
- Compile: Compile the code using a Java compiler (e.g., `javac *`).
- Run: Execute the main class (e.g., `java PersonalFinanceTracker`).
- Add javaFX files to sdk libraries.
 
## Account Creation
- Enter your desired nickname when prompted.
- Finance.Main Menu: Choose from the following options:
- Record a new transaction
- View financial insights
- View financial statement
- Exit
- Transaction Recording: Input details for category, amount, and type (income/expense).
- Error Handling: Invalid input is gracefully handled, providing clear messages to guide users.

## Implementation Details
- The User class stores user information and transactions.
- The Transaction class represents individual transactions with category, amount, and type.
- The FileManager class handles file I/O for saving and loading user data.
- Robust input validation ensures data integrity.
- Clear and informative output messages enhance user experience.
## Further Enhancements:

- Implement login functionality for multiple users.
- Add currency support.
- Offer budget planning and goal setting features.
- Integrate with financial institutions for automatic transaction imports.
- Improve the UI for a more visually appealing and interactive experience.