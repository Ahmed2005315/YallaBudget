package com.mazenfahim.YallaBudget.Manager;

import com.mazenfahim.YallaBudget.Model.Category;
import com.mazenfahim.YallaBudget.Model.Expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data manager responsible for expense and category persistence.
 */
public class ExpenseManager {
    public void insertExpense(Expense expense) {
        saveExpense(expense);
    }

    public void saveExpense(Expense expense) {
        String sql = "INSERT INTO expense(amount, category_id, cycle_id, timestamp) VALUES(?, ?, ?, ?)";
        SQLiteManager.executeUpdate(
                sql,
                expense.getAmount(),
                expense.getCategory().getId(),
                expense.getCycleId(),
                expense.getTimestamp().toString()
        );
    }

    public void updateExpense(Expense expense) {
        String sql = "UPDATE expense SET amount = ?, category_id = ?, timestamp = ? WHERE id = ? AND cycle_id = ?";
        SQLiteManager.executeUpdate(
                sql,
                expense.getAmount(),
                expense.getCategory().getId(),
                expense.getTimestamp().toString(),
                expense.getId(),
                expense.getCycleId()
        );
    }

    public void deleteExpense(Expense expense) {
        SQLiteManager.executeUpdate("DELETE FROM expense WHERE id = ? AND cycle_id = ?", expense.getId(), expense.getCycleId());
    }

    public List<Expense> getExpensesByCycle(int cycleId) {
        String sql = """
                SELECT e.id, e.amount, e.category_id, e.cycle_id, e.timestamp,
                       c.name AS category_name, c.description AS category_description
                FROM expense e
                JOIN category c ON e.category_id = c.id
                WHERE e.cycle_id = ?
                ORDER BY e.timestamp DESC
                """;

        return SQLiteManager.executeQuery(sql, rs -> {
            Category category = new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("category_description")
            );

            Expense expense = new Expense(
                    rs.getDouble("amount"),
                    category,
                    rs.getInt("cycle_id")
            );
            expense.setId(rs.getInt("id"));
            expense.setTimestamp(parseTimestamp(rs.getString("timestamp")));
            return expense;
        }, cycleId);
    }

    public List<Category> getCategories() {
        String sql = "SELECT id, name, description FROM category ORDER BY id";
        return SQLiteManager.executeQuery(
                sql,
                rs -> new Category(rs.getInt("id"), rs.getString("name"), rs.getString("description"))
        );
    }

    private LocalDateTime parseTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isBlank()) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(timestamp);
        } catch (Exception ignored) {
            return LocalDate.parse(timestamp).atStartOfDay();
        }
    }
}
