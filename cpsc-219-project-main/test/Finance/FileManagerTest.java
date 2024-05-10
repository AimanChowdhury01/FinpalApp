package Finance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {
    // The two files to compare and test
    // Will be deleted after tests are run
    Path path1, path2;
    File file1, file2;
    @TempDir
    Path tempdir;

    // Executes before every test
    // Creates two temporary files
    @BeforeEach
    public void setUp() {
        try {
            path1 = tempdir.resolve( "testfile1.txt" );
            path2 = tempdir.resolve( "testfile2.txt" );
        }
        catch(Exception e) {
            System.err.println("Error in creating files for testing.");
        }

        file1 = path1.toFile();
        file2 = path2.toFile();
    }

    // Tests
    @Test
    void saveUser_1() throws IOException {
        // Arrange
        User user = new User("u", "p", "n");
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
            writer1.write(user.toString()); writer1.newLine();
            writer1.write("TRANSACTIONS:"); writer1.newLine();
            writer1.write("RECURRING TRANSACTIONS:"); writer1.newLine();
            writer1.write("BUDGET LIMITS:"); writer1.newLine();
            writer1.write("---"); writer1.newLine();
        writer1.close();

        // Act
        FileManager.saveUser(user, String.valueOf(file2));

        // Assert
        long mismatch = Files.mismatch(path1, path2);
        assertEquals(-1, mismatch);
    }

    @Test
    void saveUser_2() throws IOException {
        // Arrange
        User user = new User("u", "p", "n");
        user.addTransaction(new Transaction("Food", 10.0, "Expense"));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
            writer1.write(user.toString()); writer1.newLine();
            writer1.write("TRANSACTIONS:"); writer1.newLine();
            writer1.write(user.getTransactions().getFirst().toString()); writer1.newLine();
            writer1.write("RECURRING TRANSACTIONS:"); writer1.newLine();
            writer1.write("BUDGET LIMITS:"); writer1.newLine();
            writer1.write("---"); writer1.newLine();
        writer1.close();
        // Act
        FileManager.saveUser(user, String.valueOf(file2));
        // Assert
        long mismatch = Files.mismatch(path1, path2);
        assertEquals(-1, mismatch);
    }

    @Test
    void saveUser_3() throws IOException {
        // Arrange
        User user1 = new User("a", "b", "c");
        user1.addTransaction(new Transaction("Food", 10.0, "Expense"));
        User user2 = new User("u", "p", "n");
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
            writer1.write(user1.toString()); writer1.newLine();
            writer1.write("TRANSACTIONS:"); writer1.newLine();
            writer1.write(user1.getTransactions().getFirst().toString()); writer1.newLine();
            writer1.write("RECURRING TRANSACTIONS:"); writer1.newLine();
            writer1.write("BUDGET LIMITS:"); writer1.newLine();
            writer1.write("---"); writer1.newLine();
            writer1.write(user2.toString()); writer1.newLine();
            writer1.write("TRANSACTIONS:"); writer1.newLine();
            writer1.write("RECURRING TRANSACTIONS:"); writer1.newLine();
            writer1.write("BUDGET LIMITS:"); writer1.newLine();
            writer1.write("---"); writer1.newLine();
        writer1.close();
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2));
            writer2.write(user1.toString()); writer2.newLine();
            writer2.write("TRANSACTIONS:"); writer2.newLine();
            writer2.write(user1.getTransactions().getFirst().toString()); writer2.newLine();
            writer2.write("RECURRING TRANSACTIONS:"); writer2.newLine();
            writer2.write("BUDGET LIMITS:"); writer2.newLine();
            writer2.write("---"); writer2.newLine();
        writer2.close();
        // Act
        FileManager.saveUser(user2, String.valueOf(file2));
        // Assert
        long mismatch = Files.mismatch(path1, path2);
        assertEquals(-1, mismatch);
    }

    @Test
    void loadUser_1() throws IOException {
        // Arrange
        User user1 = new User("u", "p", "n");
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
            writer1.write(user1.toString()); writer1.newLine();
            writer1.write("TRANSACTIONS:"); writer1.newLine();
            writer1.write("RECURRING TRANSACTIONS:"); writer1.newLine();
            writer1.write("BUDGET LIMITS:"); writer1.newLine();
            writer1.write("---"); writer1.newLine();
        writer1.close();
        // Act
        User user2 = FileManager.loadUser("u", String.valueOf(file1));
        // Assert
        assertEquals(user1, user2);
    }

    @Test
    void loadUser_2() throws IOException {
        // Arrange
        User user1 = new User("u", "p", "n");
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
            writer1.write(user1.toString()); writer1.newLine();
            writer1.write("TRANSACTIONS:"); writer1.newLine();
            writer1.write("RECURRING TRANSACTIONS:"); writer1.newLine();
            writer1.write("BUDGET LIMITS:"); writer1.newLine();
            writer1.write("---"); writer1.newLine();
        writer1.close();
        // Act
        User user2 = FileManager.loadUser("a", String.valueOf(file1));
        // Assert
        assertNull(user2);
    }

    @Test
    void loadUser_3() throws IOException {
        // Arrange
        User user1 = new User("u", "p", "n");
        user1.addTransaction(new Transaction("Food", 10.0, "Expense"));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
            writer1.write(user1.toString()); writer1.newLine();
            writer1.write("TRANSACTIONS:"); writer1.newLine();
            writer1.write(user1.getTransactions().getFirst().toString()); writer1.newLine();
            writer1.write("RECURRING TRANSACTIONS:"); writer1.newLine();
            writer1.write("BUDGET LIMITS:"); writer1.newLine();
            writer1.write("---"); writer1.newLine();
        writer1.close();
        // Act
        User user2 = FileManager.loadUser("u", String.valueOf(file1));
        // Assert
        assertEquals(user1, user2);
    }

    @Test
    void updateUser_1() throws IOException {
        // Arrange
        User user1 = new User("u", "p", "n");
        FileManager.saveUser(user1, String.valueOf(file1));
        user1.addTransaction(new Transaction("Food", 10.0, "Expense"));
        FileManager.saveUser(user1, String.valueOf(file2));
        // Act
        FileManager.updateUser(user1, String.valueOf(file1));
        // Assert
        long mismatch = Files.mismatch(path1, path2);
        assertEquals(-1, mismatch);
    }

    @Test
    void updateUser_2() throws IOException {
        // Arrange
        User user1 = new User("u", "p", "n");
        FileManager.saveUser(user1, String.valueOf(file1));
        FileManager.saveUser(user1, String.valueOf(file2));
        User user2 = new User("a", "b", "c");
        FileManager.saveUser(user2, String.valueOf(file1));
        user2.addTransaction(new Transaction("Food", 10.0, "Expense"));
        FileManager.saveUser(user2, String.valueOf(file2));
        User user3 = new User("d", "e", "f");
        FileManager.saveUser(user3, String.valueOf(file1));
        FileManager.saveUser(user3, String.valueOf(file2));

        // Act
        FileManager.updateUser(user2, String.valueOf(file1));

        // Assert
        long mismatch = Files.mismatch(path1, path2);
        assertEquals(-1, mismatch);
    }

    @Test
    void updateUser_3() throws IOException {
        // Arrange
        User user1 = new User("u", "p", "n");
        FileManager.saveUser(user1, String.valueOf(file1));
        FileManager.saveUser(user1, String.valueOf(file2));

        // Act
        FileManager.updateUser(user1, String.valueOf(file1));

        // Assert
        long mismatch = Files.mismatch(path1, path2);
        assertEquals(-1, mismatch);
    }

    @Test
    void checkExists_1() {
        // Arrange
        User user = new User("u", "p", "n");
        FileManager.saveUser(user, String.valueOf(file1));

        // Act
        boolean result = FileManager.checkExists(user.getUsername(), String.valueOf(file1));

        // Assert
        assertTrue(result);
    }

    @Test
    void checkExists_2() {
        // Arrange
        User user = new User("u", "p", "n");
        FileManager.saveUser(user, String.valueOf(file1));

        // Act
        boolean result = FileManager.checkExists("a", String.valueOf(file1));

        // Assert
        assertFalse(result);
    }

    @Test
    void checkExists_3() {
        // Arrange
        User user1 = new User("u", "p", "n");
        FileManager.saveUser(user1, String.valueOf(file1));
        User user2 = new User("a", "b", "c");
        FileManager.saveUser(user2, String.valueOf(file1));
        User user3 = new User("d", "e", "f");
        FileManager.saveUser(user3, String.valueOf(file1));

        // Act
        boolean result = FileManager.checkExists("a", String.valueOf(file1));

        // Assert
        assertTrue(result);
    }
}