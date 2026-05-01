package com.mazenfahim.YallaBudget.Controller;

import com.mazenfahim.YallaBudget.Model.BudgetCycle;
import com.mazenfahim.YallaBudget.Service.BudgetService;
import com.mazenfahim.YallaBudget.Service.ChartData;
import com.mazenfahim.YallaBudget.Service.DashboardService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the dashboard and visual spending insights screen.
 */
public class DashboardController {
    private final BudgetService budgetService = new BudgetService();
    private final DashboardService dashboardService = new DashboardService();

    @FXML
    private Label remainingBalanceLabel;

    @FXML
    private Label dailyLimitLabel;

    @FXML
    private Label totalSpendingLabel;

    @FXML
    private PieChart categoryPieChart;

    @FXML
    private Label thresholdWarningLabel;

    @FXML
    public void initialize() {
        openDashboard();
    }

    public void openDashboard() {
        BudgetCycle cycle = budgetService.getCurrentCycleData();
        if (cycle == null) {
            remainingBalanceLabel.setText("No active cycle");
            dailyLimitLabel.setText("--");
            totalSpendingLabel.setText("--");
            thresholdWarningLabel.setText("Please create a budget cycle first.");
            categoryPieChart.getData().clear();
            return;
        }

        budgetService.applyRollover(cycle);
        remainingBalanceLabel.setText(formatMoney(dashboardService.getRemainingBalance(cycle)));
        dailyLimitLabel.setText(formatMoney(dashboardService.getDailyLimit(cycle)));
        totalSpendingLabel.setText(formatMoney(dashboardService.getTotalSpending(cycle)));
        displayChart(cycle.getId());

        if (budgetService.checkThreshold(cycle)) {
            showNotification(cycle.getPercentageSpending() >= 100
                    ? "Budget exhausted. Please reset or start a new cycle."
                    : "Warning: You have used 80% of your allowance.");
        } else {
            thresholdWarningLabel.setText("");
        }
    }

    public void refreshDashboard() {
        openDashboard();
    }

    public void displayChart(int cycleId) {
        categoryPieChart.getData().clear();
        List<ChartData> chartData = dashboardService.getChartData(cycleId);
        if (chartData.isEmpty()) {
            categoryPieChart.setTitle("No data yet");
            return;
        }
        categoryPieChart.setTitle("Spending by Category");
        for (ChartData data : chartData) {
            categoryPieChart.getData().add(new PieChart.Data(data.getCategory(), data.getAmount()));
        }
    }

    public void showNotification(String message) {
        thresholdWarningLabel.setText(message);
    }

    @FXML
    public void onAddExpenseClicked() {
        navigateSafely("ExpenseEntryView.fxml");
    }

    @FXML
    public void onHistoryClicked() {
        navigateSafely("HistoryView.fxml");
    }

    @FXML
    public void onSettingsClicked() {
        navigateSafely("SettingsView.fxml");
    }

    @FXML
    public void onSetupClicked() {
        navigateSafely("SetupView.fxml");
    }

    private String formatMoney(double value) {
        return String.format("%.2f EGP", value);
    }

    private void navigateSafely(String fxmlFile) {
        try {
            navigateTo(fxmlFile);
        } catch (IOException e) {
            thresholdWarningLabel.setText("Could not open the selected page.");
        }
    }

    private void navigateTo(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mazenfahim/YallaBudget/" + fxmlFile));
        Scene scene = new Scene(loader.load(), 900, 680);
        Stage stage = (Stage) remainingBalanceLabel.getScene().getWindow();
        stage.setScene(scene);
    }
}
