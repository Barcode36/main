package seedu.address.ui;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import seedu.address.model.budget.Budget;

//@@author snookerballs
/**
 * Panel containing the budget information.
 */
public class BudgetPanel extends UiPart<Region> {
    private static final String FXML = "BudgetPanel.fxml";

    private static final double ANIMATION_TIME = 0.5;
    private static final double NUM_OF_FRAMES = 10;
    private static final double TIME_OF_KEY_FRAMES = ANIMATION_TIME/ NUM_OF_FRAMES;

    private static Timeline timeline;

    @FXML
    private ProgressBar budgetBar;

    @FXML
    private Text budgetDisplay;

    @FXML
    private Text expenseDisplay;

    private double currentExpenses;
    private double currentBudgetCap;


    public BudgetPanel (Budget budget) {
        super(FXML);
        currentExpenses = 0;
        currentBudgetCap = 0;
        update(budget);
    }

    /**
     * Update the budgetDisplay, expenseDisplay and budgetBar
     * @param budget to update from
     */
    public void update(Budget budget) {
        double budgetCap = budget.getBudgetCap();
        double currentExpenses = budget.getCurrentExpenses();
        updateBudgetBar(budgetCap, currentExpenses);
    }

    /**
     * Update the text to display the correct budget
     * @param budgetCap to display
     */
    public void updateBudgetCapTextDisplay(double budgetCap) {
        this.budgetDisplay.setText("/ $" + String.format("%.2f", budgetCap));
    }

    /**
     * Update the text to display the correct expenses
     * @param expense to display
     */
    public void updateExpenseTextDisplay(double expense) {
        this.expenseDisplay.setText("$" + String.format("%.2f", expense));
    }

    /**
     * Update the percentage of the progress bar
     * @param budgetCap at the current time
     * @param currentExpenses at the current time
     */
    public void updateBudgetBar(double budgetCap, double currentExpenses) {
        double currentPercentage = currentExpenses / budgetCap;
        if(currentPercentage >= 1.0) {
            currentPercentage = 1;
        }

        animateBudgetPanel(currentExpenses, budgetCap, currentPercentage);
    }

    /**
     * Triggers animation for the budget panel
     * @param newExpenses
     * @param newBudgetCap
     * @param newPercentage
     */
    public void animateBudgetPanel(double newExpenses, double newBudgetCap, double newPercentage) {
        timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(budgetBar.progressProperty(), budgetBar.getProgress())
                ),
                new KeyFrame(
                        Duration.seconds(ANIMATION_TIME),
                        new KeyValue(budgetBar.progressProperty(), newPercentage)
                )
        );

        addIncrementKeyFrames(newExpenses, newBudgetCap);
        timeline.playFromStart();
    }

    /**
     * Adds the key frames needed to increment the expense and budget cap text display
     * @param newExpenses to increment expenseDisplay to
     * @param newBudgetCap to increment budgetDisplay to
     */
    public void addIncrementKeyFrames(double newExpenses, double newBudgetCap) {
        double amountToIncrementExpenses = (newExpenses - currentExpenses) / NUM_OF_FRAMES;
        double amountToIncrementBudget = (newBudgetCap - currentBudgetCap) / NUM_OF_FRAMES;

            for (double i = TIME_OF_KEY_FRAMES; i <= ANIMATION_TIME; i += TIME_OF_KEY_FRAMES) {
                KeyFrame frame = new KeyFrame(Duration.seconds(i), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        currentExpenses += amountToIncrementExpenses;
                        currentBudgetCap += amountToIncrementBudget;

                        if(currentExpenses <= 0) {
                            currentExpenses = 0;
                        }

                        if(currentBudgetCap <= 0) {
                            currentBudgetCap = 0;
                        }

                        updateExpenseTextDisplay(currentExpenses);
                        updateBudgetCapTextDisplay(currentBudgetCap);
                    }
                });
                timeline.getKeyFrames().add(frame);
        }
    }


}
