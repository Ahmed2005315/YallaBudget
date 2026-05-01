package com.mazenfahim.YallaBudget.Service;

import com.mazenfahim.YallaBudget.Manager.ExpenseManager;
import com.mazenfahim.YallaBudget.Model.BudgetCycle;
import com.mazenfahim.YallaBudget.Model.Expense;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic service for dashboard summaries, insights, and chart data.
 */
public class DashboardService {
    private final ExpenseManager expenseManager;

    public DashboardService() {
        this(new ExpenseManager());
    }

    public DashboardService(ExpenseManager expenseManager) {
        this.expenseManager = expenseManager;
    }

    public double getRemainingBalance(BudgetCycle cycle) {
        return cycle == null ? 0 : cycle.getRemainingBalance();
    }

    public double getDailyLimit(BudgetCycle cycle) {
        return cycle == null ? 0 : cycle.calculateDailyLimit();
    }

    public double getTotalSpending(BudgetCycle cycle) {
        return cycle == null ? 0 : cycle.getSpending();
    }

    public Map<String, Double> calculateCategoryTotals(int cycleId) {
        Map<String, Double> categoryTotals = new LinkedHashMap<>();
        List<Expense> expenses = expenseManager.getExpensesByCycle(cycleId);

        for (Expense expense : expenses) {
            String categoryName = expense.getCategory().getCategoryName();
            categoryTotals.put(
                    categoryName,
                    categoryTotals.getOrDefault(categoryName, 0.0) + expense.getAmount()
            );
        }

        return categoryTotals;
    }

    public List<ChartData> prepareChartData(int cycleId) {
        Map<String, Double> totals = calculateCategoryTotals(cycleId);
        List<ChartData> data = new ArrayList<>();

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            data.add(new ChartData(entry.getKey(), entry.getValue()));
        }

        return data;
    }

    public List<ChartData> getChartData(int cycleId) {
        return prepareChartData(cycleId);
    }
}
