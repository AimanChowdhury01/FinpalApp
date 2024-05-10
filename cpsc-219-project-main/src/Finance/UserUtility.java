package Finance;

import java.util.Scanner;

public class UserUtility {
    // A common scanner object for input operations shared across all instances of User.
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Handles the login process for a user by continuously verifying credentials against stored data.
     * The user is prompted to re-enter their login credentials if they enter incorrect information.
     *
     * @return The User object if login is successful.
     */
    public static User loginUser() {
        while (true) { // Infinite loop to keep asking for username and password until success
            System.out.print("Enter username: "); // Prompt for username input.
            String username = scanner.nextLine(); // Capture the input username.
            User user = FileManager.loadUser(username); // Attempt to retrieve the user data from storage.

            if (user == null) { // User not found with the given username.
                System.out.println("User not found. Please try again.");
                continue; // Skip the rest of the loop and ask for the username again.
            }

            System.out.print("Enter password: "); // Prompt for password input.
            String password = scanner.nextLine(); // Capture the input password.
            if (user.getPassword().equals(password)) { // Validate the password.
                System.out.println("Login successful. Welcome back, " + user.getNickname() + "!");
                return user; // Return the authenticated user object and exit the loop.
            } else {
                System.out.println("Incorrect password. Please try again."); // Notify incorrect password entry.
                // Loop will automatically continue asking for username again.
            }
        }
    }

    /**
     * Facilitates the creation of a new user account. This static method guides the user through choosing a unique
     * username, setting a password, and selecting a nickname. It ensures username uniqueness before account creation.
     * @return A newly created User object after successful account creation.
     */
    public static User createNewUser() {
        String username;
        do {
            System.out.print("Choose a username: "); // Prompt for username.
            username = scanner.nextLine(); // Capture the input username.
            if (FileManager.checkExists(username)) { // Check for username uniqueness.
                System.out.println("Username is taken. Please try a different one."); // Notify if username is taken.
            }
        } while (FileManager.checkExists(username)); // Loop until a unique username is provided.

        String password;
        do {
            System.out.print("Choose a password: "); // Prompt for password.
            password = scanner.nextLine(); // Capture the input password.
            if (!isPasswordStrong(password)) { // Check if the password is strong.
                System.out.println("Password is not strong enough. It must be at least 8 characters long, " +
                        "include a combination of letters, numbers, and special characters."); // Notify if password is weak.
            }
        } while (!isPasswordStrong(password)); // Loop until a strong password is provided.

        System.out.print("What should we call you? "); // Prompt for nickname.
        String nickname = scanner.nextLine(); // Capture the input nickname.

        User newUser = new User(username, password, nickname); // Instantiate a new User object.
        FileManager.saveUser(newUser); // Persist the new user data.
        System.out.println("Account created successfully. Welcome, " + nickname + "!"); // Acknowledge successful account creation.
        return newUser; // Return the newly created user object.
    }

    /**
     * Checks if the provided password is strong based on defined criteria.
     * @param password The password to check.
     * @return true if the password is considered strong; false otherwise.
     */
    private static boolean isPasswordStrong(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        if (password.length() < 8) {
            return false; // Check for minimum length.
        }

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isLetter(ch)) {
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(ch)) {
                hasSpecial = true;
            }
        }

        return hasLetter && hasDigit && hasSpecial; // Password is strong if it has letters, digits, and special characters.
    }

    /**
     * Calculates the multiplier to project expenses or income over a month based on the frequency.
     * @param frequency The frequency of the recurring transaction (e.g., daily, weekly, bi-weekly, monthly).
     * @return The multiplier to apply to the transaction amount for monthly projection.
     */
    public static double calculateFrequencyMultiplier(String frequency) {
        return switch (frequency.toLowerCase()) {
            case "daily" -> 30; // Approximate days in a month
            case "weekly" -> 4.3; // Approximate weeks in a month
            case "bi-weekly" -> 2.15; // Twice a month
            case "monthly" -> 1; // Once a month
            default -> {
                System.out.println("Unknown frequency. Defaulting to monthly.");
                yield 1;
            }
        };
    }

    /**
     * Displays a comprehensive help message to guide the user through the application's functionalities.
     * It covers the operations that users can perform, including managing transactions, setting budgets,
     * handling recurring transactions, and generating financial insights and statements.
     */
    public static void displayHelp() {
        System.out.println("\nWelcome to FinPal - Your Personal Finance Manager Help:");
        System.out.println("1. Record a new transaction - Log any income or expense to keep track of your financial activities.");
        System.out.println("2. View financial insights - Get a summary of your financial health, including total income, expenses, and net savings. Projected figures are shown if you have recurring transactions.");
        System.out.println("3. View financial statement - Generate a detailed statement that lists all transactions and summarizes your financial status.");
        System.out.println("4. Budget Manager - Set and adjust budget limits for different categories to manage your spending effectively.");
        System.out.println("5. Manage recurring transactions - Add, view, or modify recurring transactions for consistent expenses or income.");
        System.out.println("6. View help message - Displays this help information anytime you need guidance on using the application.");
        System.out.println("7. Exit - Safely exit the application.\n");

        System.out.println("Recurring Transactions:");
        System.out.println("- When adding a recurring transaction, you will be prompted for the category, amount, frequency (daily, weekly, bi-weekly, monthly), and type (income or expense).");
        System.out.println("- The application will calculate and project these recurring transactions into your financial insights and statements to provide a comprehensive view of your finances.");

        System.out.println("\nBudgeting:");
        System.out.println("- Set budget limits for various categories to monitor and control your spending.");
        System.out.println("- The application warns you if your spending in any category exceeds the set budget, helping you make informed decisions.");

        System.out.println("\nSecurity Note:");
        System.out.println("- Your password is stored securely. However, remember to keep it confidential and avoid easy-to-guess passwords.\n");
    }
}
