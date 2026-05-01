package com.mazenfahim.YallaBudget.Manager;

import com.mazenfahim.YallaBudget.Model.BudgetCycle;

import java.time.LocalDate;
import java.util.List;

/**
 * Data manager responsible for budget cycle persistence.
 */
public class BudgetManager {
    public void saveCycle(BudgetCycle cycle) {
        if (cycleExist()) {
            updateCycle(cycle);
        } else {
            insertCycle(cycle);
        }
    }

    public void insertCycle(BudgetCycle cycle) {
        String sql = "INSERT INTO budget_cycle(id, total_allowance, start_date, end_date, remaining_balance) VALUES(1, ?, ?, ?, ?)";
        SQLiteManager.executeUpdate(
                sql,
                cycle.getTotalAllowance(),
                cycle.getStartDate().toString(),
                cycle.getEndDate().toString(),
                cycle.getRemainingBalance()
        );
    }

    public void updateCycle(BudgetCycle cycle) {
        String sql = "UPDATE budget_cycle SET total_allowance = ?, start_date = ?, end_date = ?, remaining_balance = ? WHERE id = 1";
        SQLiteManager.executeUpdate(
                sql,
                cycle.getTotalAllowance(),
                cycle.getStartDate().toString(),
                cycle.getEndDate().toString(),
                cycle.getRemainingBalance()
        );
    }

    public BudgetCycle loadCycle() {
        String sql = "SELECT id, total_allowance, start_date, end_date, remaining_balance FROM budget_cycle WHERE id = 1";
        List<BudgetCycle> cycles = SQLiteManager.executeQuery(
                sql,
                rs -> new BudgetCycle(
                        rs.getInt("id"),
                        rs.getDouble("total_allowance"),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date")),
                        rs.getDouble("remaining_balance")
                )
        );
        return cycles.isEmpty() ? null : cycles.get(0);
    }

    public void deleteCycle() {
        SQLiteManager.executeUpdate("DELETE FROM expense WHERE cycle_id = 1");
        SQLiteManager.executeUpdate("DELETE FROM budget_cycle WHERE id = 1");
    }

    public boolean cycleExist() {
        return SQLiteManager.exists("SELECT 1 FROM budget_cycle WHERE id = 1");
    }

    public void insertCategories() {
        String sql = "INSERT OR IGNORE INTO category(id, name, description) VALUES " +
                "(1, 'Food', 'Meals, snacks, groceries, and dining')," +
                "(2, 'Transportation', 'Public transport, fuel, taxis, and travel')," +
                "(3, 'Entertainment', 'Games, movies, events, and leisure')," +
                "(4, 'Shopping', 'Clothes, electronics, and personal items')," +
                "(5, 'Education', 'Courses, books, and study materials')," +
                "(6, 'Health', 'Medical, pharmacy, and wellness expenses')";
        SQLiteManager.executeUpdate(sql);
    }

    public void updateRemainingBalance(double remainingBalance) {
        SQLiteManager.executeUpdate("UPDATE budget_cycle SET remaining_balance = ? WHERE id = 1", remainingBalance);
    }
}
