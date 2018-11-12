package seedu.expensetracker.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import seedu.expensetracker.commons.core.LogsCenter;
import seedu.expensetracker.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.expensetracker.model.budget.TotalBudget;


//@@author snookerballs
/**
 * Panel containing the totalBudget information.
 */
public class BudgetPanel extends UiPart<Region> {
    private static final String FXML = "BudgetPanel.fxml";
    private static final double ANIMATION_TIME = 0.5;
    private static final double NUM_OF_FRAMES = 10;
    private static final double TIME_BETWEEN_KEY_FRAMES = ANIMATION_TIME / NUM_OF_FRAMES;
    private static final double FONT_SIZE = 30.0;

    private Timeline timeline;
    private final Logger logger = LogsCenter.getLogger(BudgetPanel.class);

    @FXML
    private ProgressBar budgetBar;

    @FXML
    private Text budgetDisplay;

    @FXML
    private Text expenseDisplay;

    @FXML
    private TextFlow percentageDisplay;

    private double currentExpenses;
    private double currentBudgetCap;


    public BudgetPanel (TotalBudget totalBudget) {
        super(FXML);
        registerAsAnEventHandler(this);
        currentExpenses = 0;
        currentBudgetCap = 0;
        budgetDisplay = new Text("/$0");
        expenseDisplay = new Text("$0");
        budgetDisplay.setStyle("-fx-fill: #555555;");
        budgetDisplay.setFont(Font.font("Futura", FONT_SIZE));
        expenseDisplay.setFont(Font.font("Futura", FONT_SIZE));
        percentageDisplay.getChildren().addAll(expenseDisplay, budgetDisplay);

        update(totalBudget);
    }

    /**
     * Update the budgetDisplay, expenseDisplay and budgetBar
     * @param totalBudget to update from
     */
    public void update(TotalBudget totalBudget) {
        double budgetCap = totalBudget.getBudgetCap();
        double currentExpenses = totalBudget.getCurrentExpenses();

        updateBudgetBar(budgetCap, currentExpenses);
        setBudgetUiColors(budgetCap, currentExpenses);
    }

    /**
     * Update the text to display the correct totalBudget.
     * @param budgetCap to display.
     */
    public void updateBudgetCapTextDisplay(double budgetCap) {
        this.budgetDisplay.setText("/ $" + String.format("%.2f", budgetCap));
    }

    /**
     * Update the text to display the correct expenses.
     * @param expense to display.
     */
    public void updateExpenseTextDisplay(double expense) {
        this.expenseDisplay.setText("$" + String.format("%.2f", expense));
    }

    /**
     * Update the percentage of the progress bar.
     * @param budgetCap at the current time.
     * @param currentExpenses at the current time.
     */
    public void updateBudgetBar(double budgetCap, double currentExpenses) {
        double currentPercentage = currentExpenses / budgetCap;

        if (currentPercentage >= 1.0) {
            currentPercentage = 1;
        }

        if (budgetCap == 0) {
            currentPercentage = 1;
        }

        animateBudgetPanel(currentExpenses, budgetCap, currentPercentage);
    }

    /**
     * Triggers animation for the totalBudget panel.
     * @param newExpenses to update to.
     * @param newBudgetCap to update to.
     * @param newPercentage to update to.
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

        addTextAnimationKeyFrames(newExpenses, newBudgetCap);

        //Safeguard from animation error
        timeline.setOnFinished(temp -> {
            updateExpenseTextDisplay(newExpenses);
            updateBudgetCapTextDisplay(newBudgetCap);
            currentExpenses = newExpenses;
            currentBudgetCap = newBudgetCap;
            alterTextSize();
            timeline.stop();
        });
        timeline.playFromStart();

    }

    /**
     * Adds the key frames needed to increment the expense and totalBudget cap text display
     * @param newExpenses to increment expenseDisplay to
     * @param newBudgetCap to increment budgetDisplay to
     */
    public void addTextAnimationKeyFrames(double newExpenses, double newBudgetCap) {
        double amountToIncrementExpenses = (newExpenses - currentExpenses) / NUM_OF_FRAMES;
        double amountToIncrementBudget = (newBudgetCap - currentBudgetCap) / NUM_OF_FRAMES;

        for (double i = TIME_BETWEEN_KEY_FRAMES; i <= ANIMATION_TIME; i += TIME_BETWEEN_KEY_FRAMES) {
            KeyFrame frame = new KeyFrame(Duration.seconds(i), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentExpenses += amountToIncrementExpenses;
                    currentBudgetCap += amountToIncrementBudget;

                    if (currentExpenses <= 0) {
                        currentExpenses = 0;
                    }

                    if (currentBudgetCap <= 0) {
                        currentBudgetCap = 0;
                    }

                    updateExpenseTextDisplay(currentExpenses);
                    updateBudgetCapTextDisplay(currentBudgetCap);
                    alterTextSize();
                }
            });
            timeline.getKeyFrames().add(frame);

        }
    }

    /**
     * Changes the colors of the expenseDisplay and budgetbar to red if over budget, and green if below
     * budget.
     * @param budgetCap to check against.
     * @param currentExpenses to check against.
     */
    public void setBudgetUiColors(double budgetCap, double currentExpenses) {
        if (budgetCap < currentExpenses) {
            expenseDisplay.setStyle("-fx-fill: #ae3b3b;");
            budgetBar.setStyle("-fx-accent: derive(#ae3b3b, 20%);");
        } else {
            expenseDisplay.setStyle("-fx-fill: #61a15a;");
            budgetBar.setStyle("-fx-accent: derive(#61a15a, 20%);");
        }

    }

    /**
     * Alter the size of texts depending on whether it has overflowed.
     */
    public void alterTextSize() {
        percentageDisplay.setScaleY(percentageDisplay.getMaxHeight() / percentageDisplay.getHeight());
        percentageDisplay.setScaleX(percentageDisplay.getMaxHeight() / percentageDisplay.getHeight());
    }

    @Subscribe
    public void handleUpdateBudgetPanelEvent(UpdateBudgetPanelEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        update(event.totalBudget);
    }

}
