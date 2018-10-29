package seedu.address.model.encryption;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.expense.Category;

/**
 * Represents a Expense's category in Expense Tracker in it's encrypted form.
 * Guarantees: immutable;
 */
public class EncryptedCategory extends EncryptedExpenseField<Category> {

    /**
     * A constructor for an EncryptedCategory that takes in an encrypted String representation of the category.
     * @param encryptedString the encrypted String representation of the category
     */
    public EncryptedCategory(String encryptedString) {
        super(encryptedString);
    }

    /**
     * A constructor for an EncryptedCategory that takes in an unencrypted Category instance of the category.
     * @param src the unencrypted Category instance
     * @param key the encryption key to use, must be 128-bit
     * @throws IllegalValueException when the input encryption key is invalid
     */
    public EncryptedCategory(Category src, String key) throws IllegalValueException {
        super(src, key);
    }

    @Override
    public Category getDecrypted(String key) throws IllegalValueException {
        return new Category(EncryptionUtil.decryptString(getEncryptedString(), key));
    }
}
