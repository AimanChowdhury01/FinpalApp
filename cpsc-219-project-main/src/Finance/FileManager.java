package Finance;

import java.io.*; // Import classes for handling input and output through files.
import java.time.LocalDate; // Import the LocalDate class for handling dates.
import java.util.ArrayList; // Import the ArrayList class for handling dynamic arrays.
import java.util.List; // Import the List interface to use list collections.
import java.util.Scanner; // Import the Scanner class for reading data from a file.

public class FileManager {
    // Define a constant for the file name where user data is stored.
    private static final String FILE_NAME = "userData.txt";

    /**
     * Serializes fields of a user object into local file
     * @param user object to be initially archived
     * @param fileName file to save user data into
     */
    public static void saveUser(User user, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Write the user's string representation to the file.
            writer.write(user.toString());
            writer.newLine();
            // Iterate through the user's transactions and write them to the file.
            writer.write("TRANSACTIONS:");
            for (Transaction transaction : user.getTransactions()) {
                writer.newLine();
                writer.write(transaction.toString());
            }
            writer.newLine();
            // Iterate through the user's recurring transactions and write them to the file.
            writer.write("RECURRING TRANSACTIONS:");
            for (RecurringTransaction recurringTransaction : user.getRecurringTransactions()) {
                writer.newLine();
                writer.write(recurringTransaction.toString());
            }
            writer.newLine();
            // Iterate through the user's budget limits and write them to the file.
            writer.write("BUDGET LIMITS:");
            for (String budgetLimits : user.getBudgetLimits().keySet()) {
                writer.newLine();
                writer.write(budgetLimits + "," + user.getBudgetLimits().get(budgetLimits));
            }
            writer.newLine();
            writer.write("---"); // Write a separator line after each user's data.
            writer.newLine();
        } catch (IOException e) {
            // Catch and report any IO exceptions during file writing.
            System.out.println("An error occurred while saving the user data.");
        }
    }

    /**
     * Overridden function to use default file for saving user data
     * @param user object to be archived
     */
    public static void saveUser(User user) { saveUser(user, FILE_NAME); }

    /**
     * Deserializes user object specified by its username
     * @param username the username of the user object to deserialize if it exists
     * @param fileName file to save user data from
     * @return user object with appropriate username
     */
    public static User loadUser(String username, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read lines from the file until the end is reached.
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(","); // Split the line by commas.
                if (userData[0].equals(username)) {
                    // If the username matches, create a new User object.
                    User user = new User(userData[0], userData[1], userData[2]);
                    // Add saved transaction data to new User object
                    reader.readLine();
                    while (!(line = reader.readLine()).equals("RECURRING TRANSACTIONS:")) {
                        String[] transactionData = line.split(",");
                        user.addTransaction(new Transaction(LocalDate.parse(transactionData[0]), transactionData[1], Double.parseDouble(transactionData[2]), transactionData[3]));
                    }
                    // Add saved recurring transaction data to new User object
                    while (!(line = reader.readLine()).equals("BUDGET LIMITS:")) {
                        String[] recurringTransactionData = line.split(",");
                        user.addRecurringTransaction(new RecurringTransaction(recurringTransactionData[0],Double.parseDouble(recurringTransactionData[1]), recurringTransactionData[2], recurringTransactionData[3]));
                    }
                    // Add saved budget limit data to new User object
                    while (!(line = reader.readLine()).equals("---")) {
                        String[] budgetLimitData = line.split(",");
                        user.getBudgetLimits().put(budgetLimitData[0], Double.parseDouble(budgetLimitData[1]));
                    }
                    return user; // Return the loaded user.
                }
            }
        } catch (IOException e) {
            // Catch and report any IO exceptions during file reading.
            System.out.println("An error occurred while loading the user data.");
        }
        return null; // Return null if the user was not found.
    }

    /**
     * Overridden function to use default file for loading user data
     * @param username string field of object to be restored
     */
    public static User loadUser(String username) { return loadUser(username, FILE_NAME); }

    /**
     * Updates stored user data; done after a single operation on the user
     * @param user object whose data needs to be updated after being modified
     * @param fileName file to save user data to
     */
    public static void updateUser(User user, String fileName) {
        List<String> fileLines = new ArrayList<>(); // Create a list to hold file lines.
        int index; // Variable to hold the index where the new transaction will be inserted.
        try (Scanner scanner = new Scanner(new File(fileName))) {
            // Read the file content into the list.
            while (scanner.hasNext()) {
                fileLines.add(scanner.nextLine());
            }
            // Insert latest transaction of the user at the calculated index if it did not previously exist.
            index = fileLines.indexOf(user.toString()) + user.getTransactions().size() + 1;
            if (fileLines.get(index).equals("RECURRING TRANSACTIONS:")) {
                fileLines.add(index, user.getTransactions().getLast().toString());
            }
            // Insert latest recurring transaction of the user at the calculated index if it did not previously exist.
            index += user.getRecurringTransactions().size() + 1;
            if (fileLines.get(index).equals("BUDGET LIMITS:")) {
                fileLines.add(index, user.getRecurringTransactions().getLast().toString());
            }
            // Insert latest budget limits of the user at the calculated index if it did not previously exist.
            index += user.getBudgetLimits().size() + 1;
            if (fileLines.get(index).equals("---")) {
                String budgetLimit = user.getBudgetLimits().lastEntry().getKey();
                fileLines.add(index, budgetLimit + "," + user.getBudgetLimits().get(budgetLimit));
            }
        } catch (IOException e) {
            // Catch and report any IO exceptions during file reading.
            System.out.println("An error occurred while loading the user data.");
        }
        // Write the updated data back to the file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : fileLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            // Catch and report any IO exceptions during file writing.
            System.out.println("An error occurred while loading the user data.");
        }
    }

    /**
     * Overridden function to use default file for updating user data
     * @param user object to be archived
     */
    public static void updateUser(User user) { updateUser(user, FILE_NAME); }

    /**
     * Checks stored data to see if user exists with appropriate username
     * @param username to compare with usernames of stored users
     * @param fileName file to read user data from
     * @return true if user exists; false otherwise
     */
    public static boolean checkExists(String username, String fileName) {
        // Try-with-resources to automatically close the BufferedReader after use.
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read lines from the file until the appropriate user data is found or end is reached.
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                // Return true if the username is found.
                if (userData[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Print an error message if an exception occurs during the read process.
            System.out.println("An error occurred while loading the user data.");
        }
        return false; // Return false if the username is not found.
    }

    /**
     * Overridden function to use default file for reading user data
     * @param username specified field needed
     * @return true if user exists; false otherwise
     */
    public static boolean checkExists(String username) { return checkExists(username, FILE_NAME); }
}