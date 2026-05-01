package com.mazenfahim.YallaBudget.Manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Central SQLite database manager.
 *
 * This class owns the low-level JDBC connection and reusable SQL helpers.
 * Feature-specific managers call this class instead of opening raw database
 * connections directly from controllers or services.
 */
public class SQLiteManager {
    private static final String URL = "jdbc:sqlite:yallabudget.db";

    private SQLiteManager() {
        // Utility class.
    }

    public static Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    /**
     * Creates all tables needed by the local offline application.
     */
    public static void createTables() {
        String[] statements = {
                """
                CREATE TABLE IF NOT EXISTS budget_cycle (
                    id INTEGER PRIMARY KEY,
                    total_allowance REAL NOT NULL,
                    start_date TEXT NOT NULL,
                    end_date TEXT NOT NULL,
                    remaining_balance REAL NOT NULL
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS user (
                    id INTEGER PRIMARY KEY,
                    username TEXT NOT NULL,
                    pin TEXT NOT NULL
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS category (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL UNIQUE,
                    description TEXT
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS expense (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    amount REAL NOT NULL,
                    category_id INTEGER NOT NULL,
                    cycle_id INTEGER NOT NULL,
                    timestamp TEXT NOT NULL,
                    FOREIGN KEY (category_id) REFERENCES category(id),
                    FOREIGN KEY (cycle_id) REFERENCES budget_cycle(id) ON DELETE CASCADE
                )
                """
        };

        try (Connection connection = connect(); Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not create SQLite tables.", e);
        }
    }

    public static int executeUpdate(String sql, Object... parameters) {
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            bindParameters(statement, parameters);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database update failed.", e);
        }
    }

    public static <T> List<T> executeQuery(String sql, ResultSetMapper<T> mapper, Object... parameters) {
        List<T> results = new ArrayList<>();
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            bindParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed.", e);
        }
        return results;
    }

    public static boolean exists(String sql, Object... parameters) {
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            bindParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database existence check failed.", e);
        }
    }

    private static void bindParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        if (parameters == null) {
            return;
        }
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }

    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet resultSet) throws SQLException;
    }
}
