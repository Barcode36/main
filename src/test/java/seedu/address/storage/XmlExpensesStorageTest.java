package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalExpenses.GAMBLE;
import static seedu.address.testutil.TypicalExpenses.SCHOOLFEE;
import static seedu.address.testutil.TypicalExpenses.STOCK;
import static seedu.address.testutil.TypicalExpenses.getTypicalExpenseTracker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ExpenseTracker;
import seedu.address.model.ReadOnlyExpenseTracker;
import seedu.address.testutil.ModelUtil;

public class XmlExpensesStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlExpensesStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readExpenseTracker_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readExpenseTracker(null);
    }

    private java.util.Optional<ReadOnlyExpenseTracker> readExpenseTracker(String filePath) throws Exception {
        return new XmlExpensesStorage(Paths.get(filePath)).readExpenses(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readExpenseTracker("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readExpenseTracker("NotXmlFormatExpenseTracker.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readExpenseTracker_invalidExpenseExpenseTracker_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readExpenseTracker("invalidExpenseExpenseTracker.xml");
    }

    @Test
    public void readExpenseTracker_invalidAndValidExpenseExpenseTracker_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readExpenseTracker("invalidAndValidExpenseExpenseTracker.xml");
    }

    @Test
    public void readAndSaveExpenseTracker_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempExpenseTracker.xml");
        ExpenseTracker original = getTypicalExpenseTracker();
        XmlExpensesStorage xmlExpenseTrackerStorage = new XmlExpensesStorage(filePath);

        //Save in new file and read back
        xmlExpenseTrackerStorage.saveExpenses(original, filePath);
        ReadOnlyExpenseTracker readBack = xmlExpenseTrackerStorage.readExpenses(filePath).get();
        assertEquals(original, new ExpenseTracker(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addExpense(STOCK);
        original.removeExpense(SCHOOLFEE);
        xmlExpenseTrackerStorage.saveExpenses(original, filePath);
        readBack = xmlExpenseTrackerStorage.readExpenses(filePath).get();
        assertEquals(original, new ExpenseTracker(readBack));

        //Save and read without specifying file path
        original.addExpense(GAMBLE);
        xmlExpenseTrackerStorage.saveExpenses(original); //file path not specified
        readBack = xmlExpenseTrackerStorage.readExpenses().get(); //file path not specified
        assertEquals(original, new ExpenseTracker(readBack));

    }

    @Test
    public void saveExpenseTracker_nullExpenseTracker_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveExpenseTracker(null, "SomeFile.xml");
    }

    /**
     * Saves {@code expenseTracker} at the specified {@code filePath}.
     */
    private void saveExpenseTracker(ReadOnlyExpenseTracker expenseTracker, String filePath) {
        try {
            new XmlExpensesStorage(Paths.get(filePath))
                    .saveExpenses(expenseTracker, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveExpenseTracker_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveExpenseTracker(new ExpenseTracker(ModelUtil.TEST_USERNAME, Optional.empty()), null);
    }


}
