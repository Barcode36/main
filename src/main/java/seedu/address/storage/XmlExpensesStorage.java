package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ExpenseTracker;
import seedu.address.model.ReadOnlyExpenseTracker;
import seedu.address.model.user.Username;

/**
 * A class to access ExpenseTracker data stored as an xml file on the hard disk.
 */
public class XmlExpensesStorage implements ExpensesStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlExpensesStorage.class);

    private Path filePath;
    private Path backupFilePath;

    public XmlExpensesStorage(Path filePath) {
        this.filePath = filePath;
        this.backupFilePath = Paths.get(filePath.toString() + ".backup");
    }

    public Path getExpensesDirPath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyExpenseTracker> readExpenses() throws DataConversionException, IOException {
        return readExpenses(filePath);
    }

    /**
     * Similar to {@link #readExpenses()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyExpenseTracker> readExpenses(Path filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);
        if (!Files.exists(filePath)) {
            logger.info("ExpenseTracker file " + filePath + " not found");
            return Optional.empty();
        }

        XmlSerializableExpenseTracker xmlExpenseTracker = XmlFileStorage.loadDataFromSaveFile(filePath);

        try {
            Optional<ExpenseTracker> expenseTrackerOptional = Optional.of(xmlExpenseTracker.toModelType());
            Username fileName = new Username(filePath.getFileName().toString().replace(".xml", ""));
            expenseTrackerOptional.ifPresent(expenseTracker -> {
                if (!fileName.equals(expenseTracker.getUsername())) {
                    logger.info("File name does not match username. Changing username to \"" + fileName + "\"");
                    expenseTracker.setUsername(fileName);
                }
            });
            return expenseTrackerOptional.map(expenseTracker -> expenseTracker);
            // Typecast to Optional<ReadOnlyExpenseTracker>
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveExpenses(ReadOnlyExpenseTracker expenseTracker) throws IOException {
        saveExpenses(expenseTracker, filePath);
    }

    /**
     * Similar to {@link #saveExpenses(ReadOnlyExpenseTracker)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveExpenses(ReadOnlyExpenseTracker expenseTracker, Path filePath) throws IOException {
        requireNonNull(expenseTracker);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableExpenseTracker(expenseTracker));
        System.out.println("Category budgets: ");
        System.out.println(expenseTracker.getMaximumTotalBudget().getCategoryBudgets());
    }

    @Override
    public void backupExpenses(ReadOnlyExpenseTracker expenseTracker) throws IOException {
        backupExpenses(expenseTracker, backupFilePath);
    }

    /**
     * Similar to {@link #backupExpenses(ReadOnlyExpenseTracker)}
     * @param filePath location of the data. Cannot be null
     */
    public void backupExpenses(ReadOnlyExpenseTracker expenseTracker, Path filePath) throws IOException {
        requireNonNull(expenseTracker);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableExpenseTracker(expenseTracker));
    }
}
