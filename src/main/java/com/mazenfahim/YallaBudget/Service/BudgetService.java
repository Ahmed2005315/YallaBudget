package com.mazenfahim.YallaBudget.Service;

import com.mazenfahim.YallaBudget.Manager.BudgetManager;
import com.mazenfahim.YallaBudget.Model.BudgetCycle;

import java.time.LocalDate;

/**
 * Business logic service for budget cycle management.
 */
public class BudgetService {
    private final BudgetManager budgetManager;

    public BudgetService() {
        this(new BudgetManager());
    }

    public BudgetService(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    public BudgetCycle createCycle(double allowance, LocalDate start, LocalDate end) {
        if (!validateAmount(allowance)) {
            throw new IllegalArgumentException("Allowance must be a positive number.");
        }
        if (!validateDateRange(start, end)) {
            throw new IllegalArgumentException("End date must be after start date.");
        }

        BudgetCycle cycle = new BudgetCycle(allowance, start, end);
        budgetManager.saveCycle(cycle);
        ensureDefaultCategories();
        return cycle;
    }

    public BudgetCycle getCurrentCycleData() {
        return budgetManager.loadCycle();
    }

    public BudgetCycle loadActiveCycle() {
        return budgetManager.loadCycle();
    }

    public boolean cycleExists() {
        return budgetManager.cycleExist();
    }

    public void resetCurrentCycle() {
        budgetManager.deleteCycle();
    }

    public void ensureDefaultCategories() {
        budgetManager.insertCategories();
    }

    public double calculateDailyLimit(BudgetCycle cycle) {
        return cycle == null ? 0 : cycle.calculateDailyLimit();
    }

    public double recalculateDailyLimit(BudgetCycle cycle) {
        return calculateDailyLimit(cycle);
    }

    /**
     * Dynamic rollover is implemented by recalculating the daily limit using
     * the current date and current remaining balance whenever the dashboard opens.
     */
    public double applyRollover(BudgetCycle cycle) {
        return recalculateDailyLimit(cycle);
    }

    public void updateRemainingBalance(double newRemainingBalance) {
        budgetManager.updateRemainingBalance(newRemainingBalance);
    }

    public boolean checkThreshold(BudgetCycle cycle) {
        return cycle != null && cycle.getPercentageSpending() >= 80;
    }

    public boolean validateAmount(double amount) {
        return amount > 0;
    }

    public boolean validateDateRange(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
}
