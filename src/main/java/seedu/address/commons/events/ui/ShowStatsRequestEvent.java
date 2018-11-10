package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author jonathantjm
/**
 * An event requesting to update the statistics panel.
 */
public class ShowStatsRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
