package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.MassEditCommand.MESSAGE_EDIT_MULTIPLE_EXPENSE_SUCCESS;
import static seedu.address.logic.commands.MassEditCommand.MESSAGE_NO_EXPENSE_FOUND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.ModelUtil.getTypicalModel;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.expense.EditExpenseDescriptor;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.ExpenseContainsKeywordsPredicate;
import seedu.address.testutil.EditExpenseDescriptorBuilder;
import seedu.address.testutil.ExpenseBuilder;

//@@author jcjxwy
public class MassEditCommandTest {
    private Model model = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_noExpenseFound_failure() {
        Expense editedExpense = new ExpenseBuilder().build();
        EditExpenseDescriptor editExpenseDescriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        ArgumentMultimap keywordsMap = prepareMap("n/test");
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        MassEditCommand command = new MassEditCommand(predicate, editExpenseDescriptor);
        try {
            command.execute(model, commandHistory);
        } catch (Exception e) {
            assertEquals(e.getMessage(), MESSAGE_NO_EXPENSE_FOUND);
        }
    }

    @Test
    public void execute_allFieldsSpecifies_success() throws NoUserSelectedException {
        Expense editedExpense = new ExpenseBuilder().build();
        EditExpenseDescriptor editExpenseDescriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        ArgumentMultimap keywordsMap = prepareMap("n/lunch");
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        MassEditCommand command = new MassEditCommand(predicate, editExpenseDescriptor);

        String expectedMessage = MESSAGE_EDIT_MULTIPLE_EXPENSE_SUCCESS;
        Model expectedModel = getTypicalModel();
        expectedModel.updateFilteredExpenseList(predicate);
        List<Expense> filteredList = expectedModel.getFilteredExpenseList();
        List<Expense> editedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            expectedModel.updateExpense(filteredList.get(i), editedExpense);
            editedList.add(editedExpense);
        }
        Predicate<Expense> newPredicate = e -> editedList.stream().anyMatch(newExpense -> e == newExpense);
        expectedModel.updateFilteredExpenseList(newPredicate);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldSpecified_success() throws NoUserSelectedException {
        EditExpenseDescriptor editExpenseDescriptor =
                new EditExpenseDescriptorBuilder().withTags("test").withCost("1.00").build();
        ArgumentMultimap keywordsMap = prepareMap("n/t");
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        MassEditCommand command = new MassEditCommand(predicate, editExpenseDescriptor);

        String expectedMessage = MESSAGE_EDIT_MULTIPLE_EXPENSE_SUCCESS;
        Model expectedModel = getTypicalModel();
        expectedModel.updateFilteredExpenseList(predicate);
        List<Expense> filteredList = expectedModel.getFilteredExpenseList();
        List<Expense> editedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            Expense expense = filteredList.get(i);
            Expense editedExpense = EditExpenseDescriptor.createEditedExpense(expense, editExpenseDescriptor);
            expectedModel.updateExpense(filteredList.get(i), editedExpense);
            editedList.add(editedExpense);
        }
        Predicate<Expense> newPredicate = e -> editedList.stream().anyMatch(newExpense -> e == newExpense);
        expectedModel.updateFilteredExpenseList(newPredicate);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editCreateDuplicateExpense_success() throws NoUserSelectedException{
        Expense expenseInList = model.getExpenseTracker().getExpenseList().get(INDEX_SECOND_EXPENSE.getZeroBased());
        EditExpenseDescriptor editExpenseDescriptor =
                new EditExpenseDescriptorBuilder(expenseInList).build();
        ArgumentMultimap keywordsMap = prepareMap("n/t");
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        MassEditCommand command = new MassEditCommand(predicate, editExpenseDescriptor);

        String expectedMessage = MESSAGE_EDIT_MULTIPLE_EXPENSE_SUCCESS;
        Model expectedModel = getTypicalModel();
        expectedModel.updateFilteredExpenseList(predicate);
        List<Expense> filteredList = expectedModel.getFilteredExpenseList();
        List<Expense> editedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            Expense expense = filteredList.get(i);
            Expense editedExpense = EditExpenseDescriptor.createEditedExpense(expense, editExpenseDescriptor);
            expectedModel.updateExpense(filteredList.get(i), editedExpense);
            editedList.add(editedExpense);
        }
        Predicate<Expense> newPredicate = e -> editedList.stream().anyMatch(newExpense -> e == newExpense);
        expectedModel.updateFilteredExpenseList(newPredicate);
        expectedModel.commitExpenseTracker();

        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(prepareMap("n/test"));
        EditExpenseDescriptor descriptor =
                new EditExpenseDescriptorBuilder().withCategory("test").withCost("1.11").build();
        final MassEditCommand command = new MassEditCommand(predicate, descriptor);

        //same object -> return true
        assertEquals(command, command);

        //same predicate and descriptor -> return true
        assertEquals(command, new MassEditCommand(predicate, new EditExpenseDescriptor(descriptor)));

        //null -> return false
        assertFalse(command.equals(null));

        //different types -> return false
        assertFalse(command.equals(new EditExpenseDescriptorBuilder()));

        //different predicate -> return false
        ExpenseContainsKeywordsPredicate testPredicate =
                new ExpenseContainsKeywordsPredicate(prepareMap("t/test"));
        assertFalse(command.equals(new MassEditCommand(testPredicate, descriptor)));

        //different descriptor -> return false
        EditExpenseDescriptor testDescriptor = new EditExpenseDescriptorBuilder().withCost("2.00").build();
        assertFalse(command.equals(new MassEditCommand(predicate, testDescriptor)));
    }

    /**
     * Parses the input and stores the keywords in {@code ArgumentMultimap}
     * */
    private ArgumentMultimap prepareMap(String input) {
        String preparedInput = " " + input.trim();
        ArgumentMultimap map = ArgumentTokenizer.tokenize(preparedInput,
                PREFIX_NAME, PREFIX_CATEGORY, PREFIX_COST, PREFIX_DATE, PREFIX_TAG);
        return map;
    }

}
