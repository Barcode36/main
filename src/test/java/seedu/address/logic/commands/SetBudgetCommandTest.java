package seedu.address.logic.commands;
//author winsonhys

import static junit.framework.TestCase.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalExpenses.getTypicalExpenseTracker;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.budget.Budget;
import seedu.address.model.exceptions.NoUserSelectedException;



public class SetBudgetCommandTest {
    private Model model = new ModelManager(getTypicalExpenseTracker(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * Resets the command history and model before each test
     */
    @BeforeEach
    public void resetModelsAndCommandHistory() {
        this.model = new ModelManager(getTypicalExpenseTracker(), new UserPrefs());
        this.commandHistory = new CommandHistory();
    }

    @Test
    public void execute_setBudget_successful() throws NoUserSelectedException {
        ModelManager expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs());
        Budget toSet = new Budget("2.00");
        expectedModel.modifyMaximumBudget(toSet);
        expectedModel.commitExpenseTracker();
        SetBudgetCommand setBudgetCommand = new SetBudgetCommand(toSet);
        String expectedMessage = String.format(SetBudgetCommand.MESSAGE_SUCCESS, toSet);
        assertCommandSuccess(setBudgetCommand, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(expectedModel.getMaximumBudget().getCurrentExpenses(),
            model.getMaximumBudget().getCurrentExpenses());
    }

}
