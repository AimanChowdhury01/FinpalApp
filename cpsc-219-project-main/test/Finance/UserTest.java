package Finance;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    // Output stream to print onto instead of the console
    private static final ByteArrayOutputStream out = new ByteArrayOutputStream();

    // Executes before running tests
    // Changes output stream to local variable
    @BeforeAll
    public static void setStream() {
        System.setOut(new PrintStream(out));
    }

    // Resets output stream after each test
    @AfterEach
    public void clearStream() {
        out.reset();
    }

    // Executes after running tests
    // Change back output stream to console
    @AfterAll
    public static void restoreStream() {
        System.setOut(System.out);
    }

    // Tests
    @Test
    void testAddRecurringTransaction_1() {
        // Arrange
        String input = "Rent\n200\nmonthly\nE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        RecurringTransaction desired = new RecurringTransaction("Rent", 200, "monthly", "Expense");
        User user = new User("username", "password", "nickname");
        // Act
        user.addRecurringTransaction();
        // Assert
        assertEquals(desired, user.getRecurringTransactions().getFirst());
    }

    @Test
    void testViewRecurringTransactions_1() {
        // Arrange
        String desired = "Recurring Transactions:" + System.lineSeparator()
                + "[Category], [Amount], [Frequency], [Type]" + System.lineSeparator()
                + "No recurring transactions found." + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        // Act
        user.viewRecurringTransactions();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testViewRecurringTransactions_2() {
        // Arrange
        RecurringTransaction recurringTransaction = new RecurringTransaction("Rent", 200, "monthly", "Expense");
        String desired = "Recurring Transactions:" + System.lineSeparator()
                + "[Category], [Amount], [Frequency], [Type]" + System.lineSeparator()
                + recurringTransaction + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        // Act
        user.addRecurringTransaction(recurringTransaction);
        user.viewRecurringTransactions();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testRecordTransaction_1() {
        // Arrange
        String input = "Paycheck\nI\n1200\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Transaction transaction = new Transaction("Paycheck", 1200, "Income");
        User user = new User("username", "password", "nickname");
        // Act
        user.recordTransaction();
        // Assert
        assertEquals(transaction, user.getTransactions().getFirst());
    }

    @Test
    void testRecordTransaction_2() {
        // Arrange
        String input = "Rent\nE\ntwo-hundred\n200\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Transaction transaction = new Transaction("Rent", 200, "Expense");
        User user = new User("username", "password", "nickname");
        // Act
        user.recordTransaction();
        // Assert
        assertEquals(transaction, user.getTransactions().getFirst());
    }

    @Test
    void testRecordTransaction_3() {
        // Arrange
        String input = "Entertainment\n200\nEntertainment\nE\n400\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        User user = new User("username", "password", "nickname");
        user.setBudgetForCategory();
        String desired = "Available Categories: [Food, Utilities, Rent, Entertainment, Transportation, Other]" + System.lineSeparator()
                + "Enter category to set budget for: "
                + "Enter budget limit for Entertainment: "
                + "Budget set for Entertainment: $200.0" + System.lineSeparator()
                + "Enter transaction category (e.g., Food, Rent): "
                + "Is this an (I)ncome or an (E)xpense? "
                + "Enter amount: "
                + "Warning: This expense exceeds your budget limit for Entertainment" + System.lineSeparator()
                + "Transaction recorded successfully." + System.lineSeparator();
        // Act
        user.recordTransaction();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testDisplayInsights_1() {
        // Arrange
        String desired = "Total Income: $0.0" + System.lineSeparator()
                + "Total Expenses: $0.0" + System.lineSeparator()
                + "Net Savings: $0.0" + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        // Act
        user.displayInsights();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testDisplayInsights_2() {
        // Arrange
        String desired = "Total Income: $1200.0" + System.lineSeparator()
                + "Total Expenses: $200.0" + System.lineSeparator()
                + "Net Savings: $1000.0" + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        user.addTransaction(new Transaction("Pay-cheque", 1200, "Income"));
        user.addTransaction(new Transaction("Rent", 200, "Expense"));
        // Act
        user.displayInsights();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testDisplayInsights_3() {
        // Arrange
        String desired = "Total Income: $1290.0" + System.lineSeparator()
                + "Total Expenses: $200.0" + System.lineSeparator()
                + "Net Savings: $1090.0" + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        user.addRecurringTransaction(new RecurringTransaction("Pay-cheque", 600, "bi-weekly", "Income"));
        user.addRecurringTransaction(new RecurringTransaction("Rent", 200, "monthly", "Expense"));
        // Act
        user.displayInsights();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testDisplayInsights_4() {
        // Arrange
        String input = "Entertainment\n200\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String desired = "Available Categories: [Food, Utilities, Rent, Entertainment, Transportation, Other]" + System.lineSeparator()
                + "Enter category to set budget for: "
                + "Enter budget limit for Entertainment: "
                + "Budget set for Entertainment: $200.0" + System.lineSeparator()
                + "Total Income: $0.0" + System.lineSeparator()
                + "Total Expenses: $400.0" + System.lineSeparator()
                + "Net Savings: $-400.0" + System.lineSeparator()
                + "Consider reducing expenses in Entertainment. You've spent $400.00, which is over your budget of $200.00." + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        user.setBudgetForCategory();
        user.addTransaction(new Transaction("Entertainment", 400, "Expense"));
        // Act
        user.displayInsights();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testDisplayFinancialStatement_1() {
        // Arrange
        String desired = "Upcoming Financial Statement:" + System.lineSeparator()
                + "[DATE], [CATEGORY], [AMOUNT], [TYPE]" + System.lineSeparator()
                + "Projected Total Income: $0.0" + System.lineSeparator()
                + "Projected Total Expenses: $0.0" + System.lineSeparator()
                + "Projected Net Balance: $0.0" + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        // Act
        user.displayFinancialStatement();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testDisplayFinancialStatement_2() {
        // Arrange
        Transaction transaction1 = new Transaction("Pay-cheque", 1200, "Income");
        Transaction transaction2 = new Transaction("Rent", 200, "Expense");
        String desired = "Upcoming Financial Statement:" + System.lineSeparator()
                + "[DATE], [CATEGORY], [AMOUNT], [TYPE]" + System.lineSeparator()
                + transaction1 + System.lineSeparator()
                + transaction2 + System.lineSeparator()
                + "Projected Total Income: $1200.0" + System.lineSeparator()
                + "Projected Total Expenses: $200.0" + System.lineSeparator()
                + "Projected Net Balance: $1000.0" + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        user.addTransaction(transaction1);
        user.addTransaction(transaction2);
        // Act
        user.displayFinancialStatement();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void testDisplayFinancialStatement_3() {
        // Arrange
        Transaction transaction1 = new Transaction("Food", 20, "Expense");
        RecurringTransaction recurringTransaction1 = new RecurringTransaction("Rent", 200, "monthly", "Expense");
        RecurringTransaction recurringTransaction2 = new RecurringTransaction("Pay-cheque", 600, "bi-weekly", "Income");
        String desired = "Upcoming Financial Statement:" + System.lineSeparator()
                + "[DATE], [CATEGORY], [AMOUNT], [TYPE]" + System.lineSeparator()
                + transaction1 + System.lineSeparator()
                + "Next Month Rent (Recurring): -200.0" + System.lineSeparator()
                + "Next Month Pay-cheque (Recurring): +600.0" + System.lineSeparator()
                + "Projected Total Income: $600.0" + System.lineSeparator()
                + "Projected Total Expenses: $220.0" + System.lineSeparator()
                + "Projected Net Balance: $380.0" + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        user.addTransaction(transaction1);
        user.addRecurringTransaction(recurringTransaction1);
        user.addRecurringTransaction(recurringTransaction2);
        // Act
        user.displayFinancialStatement();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void setBudgetForCategory_1() {
        // Arrange
        String input = "Entertainment\n100\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String desired = "Available Categories: [Food, Utilities, Rent, Entertainment, Transportation, Other]" + System.lineSeparator()
                + "Enter category to set budget for: "
                + "Enter budget limit for Entertainment: "
                + "Budget set for Entertainment: $100.0" + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        // Act
        user.setBudgetForCategory();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void setBudgetForCategory_2() {
        // Arrange
        Transaction transaction = new Transaction("Entertainment", 400, "Expense");
        String input = "Entertainment\n200\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String desired = "Available Categories: [Food, Utilities, Rent, Entertainment, Transportation, Other]" + System.lineSeparator()
                + "Enter category to set budget for: "
                + "Enter budget limit for Entertainment: "
                + "Budget set for Entertainment: $200.0" + System.lineSeparator()
                + "Warning: Your current spending in Entertainment is $400.0, which exceeds your new budget limit. Consider adjusting your budget or expenses." + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        user.addTransaction(transaction);
        // Act
        user.setBudgetForCategory();
        // Assert
        assertEquals(desired, out.toString());
    }

    @Test
    void setBudgetForCategory_3() {
        // Arrange
        Transaction transaction = new Transaction("Food", 30, "Expense");
        RecurringTransaction recurringTransaction = new RecurringTransaction("Food", 20, "daily", "Expense");
        String input = "Food\n500\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String desired = "Available Categories: [Food, Utilities, Rent, Entertainment, Transportation, Other]" + System.lineSeparator()
                + "Enter category to set budget for: "
                + "Enter budget limit for Food: "
                + "Budget set for Food: $500.0" + System.lineSeparator()
                + "Warning: Your current spending in Food is $630.0, which exceeds your new budget limit. Consider adjusting your budget or expenses." + System.lineSeparator();
        User user = new User("username", "password", "nickname");
        user.addTransaction(transaction);
        user.addRecurringTransaction(recurringTransaction);
        // Act
        user.setBudgetForCategory();
        // Assert
        assertEquals(desired, out.toString());
    }
}