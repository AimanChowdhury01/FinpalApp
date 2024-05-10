package Finance;

import Finance.*; // Import all classes from the Finance package.
import java.util.Scanner; // Import the Scanner class from the java.util package for reading input.
import javafx.application.Application;

class Main {
    // Declare a static final Scanner object for reading input from the standard input stream (console).
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Application.launch(FinPalApp.class, args);
        // Print a welcome message to the standard output.
        System.out.println("Welcome to FinPal - Your Personal Finance Manager!");

        // Initialize a variable to store the user's choice and ensure it starts as an empty string.
        String choice = "";
        // Use a while loop to repeatedly ask the user to choose between login or account creation until a valid choice is made.
        while (!choice.equals("L") && !choice.equals("C")) {
            System.out.println("Do you want to (L)ogin or (C)reate a new account?");
            // Read the user's choice from the console, trim whitespace, and convert to uppercase to standardize the input handling.
            choice = scanner.nextLine().trim().toUpperCase();

            // If the user's choice is neither L nor C, print an error message and prompt again.
            if (!choice.equals("L") && !choice.equals("C")) {
                System.out.println("Invalid option. Select an input from [L, C]");
            }
        }

        // Declare a User variable to hold the reference to the logged-in or newly created user.
        User user;
        // If the user chooses to log in, call the loginUser method from the User class.
        if (choice.equals("L")) {
            user = UserUtility.loginUser();
        } else {
            // If the user chooses to create a new account, call the createNewUser method from the User class.
            user = UserUtility.createNewUser();
        }

        // Manage the user session by calling the manageUserSession method from the User class.
        user.manageSession();
    }
}
