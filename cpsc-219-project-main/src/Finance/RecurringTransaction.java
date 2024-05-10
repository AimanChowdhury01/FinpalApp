package Finance;

public class RecurringTransaction {
    private final String category;
    private final double amount;
    private final String frequency; // e.g., "monthly", "weekly"
    private final String type; // "Income" or "Expense"

    public RecurringTransaction(String category, double amount, String frequency, String type) {
        this.category = category;
        this.amount = amount;
        this.frequency = frequency;
        this.type = type;
    }

    // Getters and Setters
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getFrequency() { return frequency; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return category + "," + amount + "," + frequency + "," + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecurringTransaction that = (RecurringTransaction) o;

        if (Double.compare(getAmount(), that.getAmount()) != 0) return false;
        if (!getCategory().equals(that.getCategory())) return false;
        if (!getFrequency().equals(that.getFrequency())) return false;
        return getType().equals(that.getType());
    }
}
