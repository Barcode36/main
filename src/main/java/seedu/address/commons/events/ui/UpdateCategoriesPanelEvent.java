package seedu.address.commons.events.ui;

import java.util.Iterator;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.budget.CategoryBudget;

//@@author snookerballs
/**
 * An event requesting an update in categories display.
 */
public class UpdateCategoriesPanelEvent extends BaseEvent {

    public final Iterator<CategoryBudget> categoryBudgets;

    public UpdateCategoriesPanelEvent(Iterator<CategoryBudget> categoryBudgets) {
        this.categoryBudgets = categoryBudgets;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
