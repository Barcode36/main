package seedu.address.model.budget;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.expense.Expense;
import seedu.address.storage.StorageManager;

import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Budget {
    public static final String MESSAGE_BUDGET_CONSTRAINTS =
        "Cost should only take values in the following format: {int}.{digit}{digit}";

    public static final String BUDGET_VALIDATION_REGEX = "(\\d+).(\\d)(\\d)";

    protected static final Logger logger = LogsCenter.getLogger(StorageManager.class);


    protected double budgetCap;
    protected double currentExpenses;

    public Budget (String budget) {
        requireNonNull(budget);
        checkArgument(isValidBudget(budget), BUDGET_VALIDATION_REGEX);
        this.budgetCap = Double.parseDouble(budget);
        this.currentExpenses = 0.0;

    }

    /**
     * Constructs a {@code Budget} with modified current expenses
     * @param budget
     * @param currentExpenses
     */
    public Budget(double budget, double currentExpenses) {
        this.budgetCap = budget;
        this.currentExpenses = currentExpenses;
    }


    /**
     * Returns true if a given string is a valid totalBudget.
     */
    public static boolean isValidBudget(String test) {
        return test.matches(BUDGET_VALIDATION_REGEX);
    }

    /**
     * Modifies the current (@code Budget) to have a new value for its current expenses
     *
     * @param expenses a valid double
     */
    public void modifyExpenses(double expenses) {
        this.currentExpenses = expenses;
    }

    /**
     * Attemps to add expense
     * @param expense a valid expense of type double
     * @return true if expense is successfully added, false if adding expense will result in totalBudget exceeding.
     */

    public boolean addExpense(double expense) {
        this.currentExpenses += expense;
        return this.currentExpenses <= this.budgetCap;
    }

    public void removeExpense(Expense expense) {
        this.currentExpenses -= expense.getCost().getCostValue();
    }

    /**
     * Resets the total expense to 0
     */
    public void clearSpending() {
        this.currentExpenses = 0;
    }

    public double getBudgetCap() {
        return this.budgetCap;
    }

    public double getCurrentExpenses() {
        return this.currentExpenses;
    }

    /**
     * Alters the current total expense
     * @param target valid expense in spending to be removed
     * @param editedExpense new expense to be added
     */
    public void alterSpending(Expense target, Expense editedExpense) {
        this.currentExpenses -= target.getCost().getCostValue();
        this.currentExpenses += editedExpense.getCost().getCostValue();
    }

    @Override
    public boolean equals (Object budget) {
        Budget anotherBudget = (Budget) budget;
        return this.currentExpenses == anotherBudget.currentExpenses
            && this.budgetCap == anotherBudget.budgetCap;
    }

    @Override
    public String toString() {
        return String.format("$%.2f", this.budgetCap);
    }



}
