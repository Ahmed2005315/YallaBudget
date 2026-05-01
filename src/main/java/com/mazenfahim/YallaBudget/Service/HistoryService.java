package com.mazenfahim.YallaBudget.Service;

import com.mazenfahim.YallaBudget.Manager.ExpenseManager;
import com.mazenfahim.YallaBudget.Model.Expense;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic service for transaction history sorting and filtering.
 */
public class HistoryService {
    private final ExpenseManager expenseManager;

    public HistoryService() {
        this(new ExpenseManager());
    }

    public HistoryService(ExpenseManager expenseManager) {
        this.expenseManager = expenseManager;
    }

    public List<Expense> getTransactions(int cycleId) {
        return sortTransactionsByDate(expenseManager.getExpensesByCycle(cycleId));
    }

    public List<Expense> sortTransactionsByDate(List<Expense> expenses) {
        return expenses.stream()
                .sorted(Comparator.comparing(Expense::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<Expense> filterByCategory(List<Expense> expenses, int categoryId) {
        return expenses.stream()
                .filter(expense -> expense.getCategory().getId() == categoryId)
                .collect(Collectors.toList());
    }

    public List<Expense> filterByDateRange(List<Expense> expenses, LocalDate from, LocalDate to) {
        return expenses.stream()
                .filter(expense -> {
                    LocalDate date = expense.getTimestamp().toLocalDate();
                    boolean afterFrom = from == null || !date.isBefore(from);
                    boolean beforeTo = to == null || !date.isAfter(to);
                    return afterFrom && beforeTo;
                })
                .collect(Collectors.toList());
    }
}
