package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalExpenses.BENSON;
import static seedu.address.testutil.TypicalExpenses.CARL;
import static seedu.address.testutil.TypicalExpenses.ELLE;
import static seedu.address.testutil.TypicalExpenses.FIONA;
import static seedu.address.testutil.TypicalExpenses.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.ExpenseContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        ArgumentMultimap firstMap = ArgumentTokenizer.tokenize(" n/first", PREFIX_NAME);
        ArgumentMultimap secondMap = ArgumentTokenizer.tokenize(" n/second", PREFIX_NAME);
        ExpenseContainsKeywordsPredicate firstPredicate =
                new ExpenseContainsKeywordsPredicate(firstMap);
        ExpenseContainsKeywordsPredicate secondPredicate =
                new ExpenseContainsKeywordsPredicate(secondMap);

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different expense -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noExpenseFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 0);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("n/ ", PREFIX_NAME);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredExpenseList());
    }

    @Test
    public void execute_multipleNameKeywords_multipleExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 3);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("n/Kurz Elle Kunz", PREFIX_NAME);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredExpenseList());
    }

    @Test
    public void execute_oneCategoryKeyword_multipleExpensesFound() throws NoUserSelectedException {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 1);
        ExpenseContainsKeywordsPredicate predicate = preparePredicate("c/Food", PREFIX_CATEGORY);
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredExpenseList());
    }

    /**
     * Parses {@code userInput} into a {@code ExpenseContainsKeywordsPredicate}.
     */
    private ExpenseContainsKeywordsPredicate preparePredicate(String userInput, Prefix prefix) {
        ArgumentMultimap keywordsMap = ArgumentTokenizer.tokenize(" " + userInput, prefix);
        return new ExpenseContainsKeywordsPredicate(keywordsMap);
    }
}
