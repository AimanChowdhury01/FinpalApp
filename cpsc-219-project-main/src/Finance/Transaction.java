package Finance; // Defines the package this class belongs to, which is Finance.

import java.time.LocalDate; // Imports the LocalDate class from the java.time package for date manipulation.

public class Transaction {
    // Declare private final fields for the properties of a Transaction.
    private final LocalDate date; // The date of the transaction.
    private final String category; // The category of the transaction (e.g., groceries, salary).
    private final double amount; // The monetary value of the transaction.
    private final String type; // The type of transaction: "Income" or "Expense".

    // Constructor that initializes a Transaction object with all properties.
    public Transaction(LocalDate date, String category, double amount, String type) {
        this.date = date; // Assigns the provided date to the date field.
        this.category = category; // Assigns the provided category to the category field.
        this.amount = amount; // Assigns the provided amount to the amount field.
        this.type = type; // Assigns the provided type (Income/Expense) to the type field.
    }

    // Overloaded constructor for when the date is not provided, automatically using the current date.
    public Transaction(String category, double amount, String type) {
        this(LocalDate.now(), category, amount, type); // Calls the main constructor with the current date.
    }

    // Getter method for category.
    public String getCategory() {
        return category; // Returns the category of the transaction.
    }

    // Getter method for amount.
    public double getAmount() {
        return amount; // Returns the amount of the transaction.
    }

    // Getter method for type.
    public String getType() {
        return type; // Returns the type of the transaction (Income/Expense).
    }

    // Getter method for date.
    public LocalDate getDate() {
        return date; // Returns the date of the transaction.
    }

    @Override
    // Overrides the toString method to provide a string representation of the Transaction object.
    public String toString() {
        // Returns a string combining date, category, amount, and type, separated by commas.
        return date + "," + category + "," + amount + "," + type;
    }

    /**
     * Compares Transaction object with another object to check if all fields are equal
     * @param o Object to compare and check equality with
     * @return true if each field is equal to eachother; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (Double.compare(getAmount(), that.getAmount()) != 0) return false;
        if (!getDate().equals(that.getDate())) return false;
        if (!getCategory().equals(that.getCategory())) return false;
        return getType().equals(that.getType());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
