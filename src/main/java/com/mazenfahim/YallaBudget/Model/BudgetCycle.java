package com.mazenfahim.YallaBudget.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing the active allowance cycle.
 */
public class BudgetCycle {
    private int id;
    private double totalAllowance;
    private LocalDate startDate;
    private LocalDate endDate;
    private double remainingBalance;
    private final List<Expense> expenses = new ArrayList<>();

    public BudgetCycle(double totalAllowance, LocalDate startDate, LocalDate endDate) {
        this(1, totalAllowance, startDate, endDate, totalAllowance);
    }

    public BudgetCycle(int id, double totalAllowance, LocalDate startDate, LocalDate endDate, double remainingBalance) {
        this.id = id;
        this.totalAllowance = totalAllowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingBalance = remainingBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalAllowance() {
        return totalAllowance;
    }

    /**
     * Backward-compatible getter from the original code.
     */
    public double getTotal_Allowance() {
        return totalAllowance;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = Math.max(remainingBalance, 0);
    }

    /**
     * Calculates remaining days based on today's date to support dynamic rollover.
     */
    public int calculateRemainingDays() {
        LocalDate today = LocalDate.now();
        LocalDate calculationStart = today.isBefore(startDate) ? startDate : today;
        long days = ChronoUnit.DAYS.between(calculationStart, endDate) + 1;
        return (int) Math.max(days, 1);
    }

    public double calculateDailyLimit() {
        return Math.max(remainingBalance / calculateRemainingDays(), 0);
    }

    public void updateRemainingBalance(double amount) {
        remainingBalance -= amount;
        if (remainingBalance < 0) {
            remainingBalance = 0;
        }
    }

    /**
     * Backward-compatible method name from the original code.
     */
    public void UpdateRemainingBalance(Double amount) {
        updateRemainingBalance(amount);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        updateRemainingBalance(expense.getAmount());
    }

    /**
     * Backward-compatible method name from the original code.
     */
    public void AddExpense(Expense expense) {
        addExpense(expense);
    }

    public double getSpending() {
        return Math.max(totalAllowance - remainingBalance, 0);
    }

    public double getPercentageSpending() {
        if (totalAllowance <= 0) {
            return 0;
        }
        return (getSpending() / totalAllowance) * 100;
    }
}
