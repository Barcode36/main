package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.StatsCommand.MESSAGE_SUCCESS;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.events.ui.SwapLeftPanelEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.testutil.ModelUtil;
import seedu.address.ui.testutil.EventsCollectorRule;

public class StatsCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = ModelUtil.modelWithTestUser();
    private Model expectedModel = ModelUtil.modelWithTestUser();
    private CommandHistory commandHistory = new CommandHistory();

    public StatsCommandTest() throws UserAlreadyExistsException, NonExistentUserException, NoUserSelectedException {
    }

    @Test
    public void execute_stats_success() {
        assertCommandSuccess(new StatsCommand(), model, commandHistory, MESSAGE_SUCCESS, expectedModel);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof SwapLeftPanelEvent);
        assertEquals(7, eventsCollectorRule.eventsCollector.getSize());
    }
}
