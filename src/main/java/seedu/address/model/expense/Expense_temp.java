package seedu.address.model.expense;

/**
 * Temporary class for testing purpose TODO: Remove it after morphing Person to Expense
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Expense_temp {
    private Name name;
    private Category categoryTemp;

    /**
     * Construct a temporary {@code Expense_temp} for testing purpose.
     * @param - Name cannot be null
     * @param - categoryTemp can be null
     * */
    public Expense_temp(Name name, Category categoryTemp) {
        this.name = name;
        this.categoryTemp = categoryTemp;
    }

    public Category getCategoryTemp() {
        return this.categoryTemp;
    }

    @Override
    public String toString() {
        return this.name.expenseName;
    }

}
