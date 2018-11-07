package seedu.address.storage.budget;
//@@author winsonhys

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.budget.CategoryBudget;

/**
 * JAXB-friendly version of Category Budget.
 */

public class XmlAdaptedCategoryBudget extends XmlAdaptedBudget {

    public static final String MESSAGE_INVALID_FIELDS = "Either category or budget have invalid values";

    @XmlElement(required = true)
    private String category;

    public XmlAdaptedCategoryBudget() {
        super();
    }

    public XmlAdaptedCategoryBudget(String category, String budget) {
        super(budget);
        System.out.println("First constructor is called");
        this.category = category;

    }

    public XmlAdaptedCategoryBudget(CategoryBudget cBudget) {
        super(cBudget);
        this.category = cBudget.getCategory().categoryName;
    }

    @Override
    public CategoryBudget toModelType() throws IllegalValueException {
        try {
            return new CategoryBudget(this.category, this.budgetCap, this.currentExpenses);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(MESSAGE_INVALID_FIELDS);
        }
    }
}
