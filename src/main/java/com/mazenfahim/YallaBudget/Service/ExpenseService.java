package com.mazenfahim.YallaBudget.Service;

import com.mazenfahim.YallaBudget.Manager.BudgetManager;
import com.mazenfahim.YallaBudget.Manager.ExpenseManager;
import com.mazenfahim.YallaBudget.Model.BudgetCycle;
import com.mazenfahim.YallaBudget.Model.Category;
import com.mazenfahim.YallaBudget.Model.Expense;

import java.util.List;

/**
 * Business logic service for expense logging and editing.
 */
public class ExpenseService {
    private final ExpenseManager expenseManager;
    private final BudgetManager budgetManager;

    public ExpenseService() {
        this(new ExpenseManager(), new BudgetManager());
    }

    public ExpenseService(ExpenseManager expenseManager) {
        this(expenseManager, new BudgetManager());
    }

    public ExpenseService(ExpenseManager expenseManager, BudgetManager budgetManager) {
        this.expenseManager = expenseManager;
        this.budgetManager = budgetManager;
    }

    public boolean validateExpense(double amount, Category category) {
        return amount > 0 && category != null;
    }

    public Expense addExpense(double amount, Category category) {
        BudgetCycle cycle = budgetManager.loadCycle();
        if (cycle == null) {
            throw new IllegalStateException("Please create a budget cycle first.");
        }
        Expense expense = new Expense(amount, category, cycle.getId());
        addExpense(cycle, expense);
        return expense;
    }

    public void addExpense(BudgetCycle cycle, Expense expense) {
        if (cycle == null) {
            throw new IllegalStateException("Please create a budget cycle first.");
        }
        if (expense == null || !validateExpense(expense.getAmount(), expense.getCategory())) {
            throw new IllegalArgumentException("Please enter a valid amount and select a category.");
        }

        cycle.addExpense(expense);
        expenseManager.insertExpense(expense);
        budgetManager.saveCycle(cycle);
    }

    public List<Expense> getExpensesForCycle(int cycleId) {
        return expenseManager.getExpensesByCycle(cycleId);
    }

    public List<Category> getCategories() {
        return expenseManager.getCategories();
    }

    public void editExpense(Expense updatedExpense) {
        expenseManager.updateExpense(updatedExpense);
        recalculateBalanceFromExpenses(updatedExpense.getCycleId());
    }

    public void deleteExpense(Expense expense) {
        if (expense == null) {
            return;
        }
        expenseManager.deleteExpense(expense);
        recalculateBalanceFromExpenses(expense.getCycleId());
    }

    public void deleteExpense(BudgetCycle cycle, Expense expense) {
        deleteExpense(expense);
    }

    private void recalculateBalanceFromExpenses(int cycleId) {
        BudgetCycle cycle = budgetManager.loadCycle();
        if (cycle == null || cycle.getId() != cycleId) {
            return;
        }

        double spent = expenseManager.getExpensesByCycle(cycleId)
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        cycle.setRemainingBalance(cycle.getTotalAllowance() - spent);
        budgetManager.saveCycle(cycle);
    }
}
