package seedu.address.model;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.StatsCommand.StatsMode;
import seedu.address.model.budget.Budget;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.Expense;
import seedu.address.model.notification.Notification;
import seedu.address.model.notification.NotificationHandler;
import seedu.address.model.user.Password;
import seedu.address.model.user.Username;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Expense> PREDICATE_SHOW_ALL_EXPENSES = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyExpenseTracker newData) throws NoUserSelectedException;

    /** Returns the ExpenseTracker */
    ReadOnlyExpenseTracker getExpenseTracker() throws NoUserSelectedException;

    /**
     * Returns true if a expense with the same identity as {@code expense} exists in the address book.
     */
    boolean hasExpense(Expense expense) throws NoUserSelectedException;

    /**
     * Deletes the given expense.
     * The expense must exist in the address book.
     */
    void deleteExpense(Expense target) throws NoUserSelectedException;

    /**
     * Adds the given expense.
     * {@code expense} must not already exist in the address book.
     * @return true if expense is added without warning, else false.
     */
    boolean addExpense(Expense expense) throws NoUserSelectedException;

    /**
     * Replaces the given expense {@code target} with {@code editedExpense}.
     * {@code target} must exist in the address book.
     * The expense identity of {@code editedExpense}
     * must not be the same as another existing expense in the address book.
     */
    void updateExpense(Expense target, Expense editedExpense) throws NoUserSelectedException;

    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<Expense> getFilteredExpenseList() throws NoUserSelectedException;

    /**
     * Updates the filter of the filtered expense list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredExpenseList(Predicate<Expense> predicate) throws NoUserSelectedException;

    /**
     * Updates statsMode to the given {@code mode}.
     */
    void updateStatsMode(StatsMode mode);

    /**
     * Returns statsMode.
     */
    StatsMode getStatsMode();

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoExpenseTracker() throws NoUserSelectedException;

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoExpenseTracker() throws NoUserSelectedException;

    /**
     * Restores the model's address book to its previous state.
     */
    void undoExpenseTracker() throws NoUserSelectedException;

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoExpenseTracker() throws NoUserSelectedException;

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitExpenseTracker() throws NoUserSelectedException;

    /**
     * Selects the ExpenseTracker of the user with the input username to be used.
     * Returns true if successful, false if the input password is incorrect.
     */
    boolean loadUserData(Username username, Optional<Password> password) throws NonExistentUserException;

    /**
     * Logs out the user in the model.
     */
    void unloadUserData();

    /**
     * Returns true if there is a user with the input username in memory.
     */
    boolean isUserExists(Username username);

    /**
     * Adds a user with the given username and gives him/her an empty ExpenseTracker.
     * @throws UserAlreadyExistsException if a user with the given username already exists
     */
    void addUser(Username username) throws UserAlreadyExistsException;

    /**
     * Returns true if a user has been selected to be used. i.e Already logged in
     */
    boolean hasSelectedUser();

    /** Returns an unmodifiable view of the expense stats*/
    ObservableList<Expense> getExpenseStats() throws NoUserSelectedException;

    /**
     * Updates the expense stats
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateExpenseStats(Predicate<Expense> predicate) throws NoUserSelectedException;

    /**
     * Modifies the existing maximum budget for the current user
     */
    void modifyMaximumBudget(Budget budget) throws NoUserSelectedException;

    /**
     * Returns the existing maximum budget for the current user
     */
    Budget getMaximumBudget();

    /**
     * Sets the budget to reset and store spending data after a certain amount of time
     * @param seconds The recurrence frequency
     */
    void setRecurrenceFrequency(long seconds) throws NoUserSelectedException;

    /**
     * Returns a copy of this model.
     */
    Model copy(UserPrefs userPrefs) throws NonExistentUserException, NoUserSelectedException;

    /**
     * Sets the password of the currently logged in user as the new password given.
     * @param newPassword the new password to be set
     */
    void setPassword(Password newPassword) throws NoUserSelectedException;

    /**
     * Checks if the given password matches that of the currently logged in user. If the user does not have any password
     * set, then they are considered to be matching.
     * @param toCheck the password to check as an optional
     * @return true if the password to check matches that of the currently logged in user, false if it doesn't
     */
    boolean isMatchPassword(Optional<Password> toCheck) throws NoUserSelectedException;

    Iterator getCategoryList();

    /**
     * Adds a notification to the list of notification
     * @param notification to add
     * @throws NoUserSelectedException
     */
    void addNotification(Notification notification) throws NoUserSelectedException;

    /**
     * Adds a notification of type {@code WarningNotification}to the list of notification
     * @return true if successfully added
     * @throws NoUserSelectedException
     */
    boolean addWarningNotification() throws NoUserSelectedException;

    /**
     * Adds a notification of type {@code TipNotification}to the list of notification
     * @return true if successfully added.
     * @throws NoUserSelectedException
     */
    boolean addTipNotification() throws NoUserSelectedException;

    /**
     * Returns an {@code ObservableList} of current notifications
     * @return an {@code ObservableList} of current notifications
     * @throws NoUserSelectedException
     */
    ObservableList<Notification> getNotificationList() throws NoUserSelectedException;

    /**
     * Toggles the ability to add {@code TipNotifications}
     * @param toggleOption to toggle to
     * @throws NoUserSelectedException
     */
    void toggleTipNotification(boolean toggleOption) throws NoUserSelectedException;

    /**
     * Toggles the ability to add {@code WarningNotifications}
     * @param toggleOption to toggle to
     * @throws NoUserSelectedException
     */
    void toggleWarningNotification(boolean toggleOption) throws NoUserSelectedException;

    /**
     * Toggles the ability to add {@code WarningNotifications} and {@code TipNotification}
     * @param toggleOption to toggle to
     * @throws NoUserSelectedException
     */
    void toggleBothNotification(boolean toggleOption) throws NoUserSelectedException;

    /**
     * Returns notificationHandler.
     */
    NotificationHandler getNotificationHandler() throws NoUserSelectedException;
}
