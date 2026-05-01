package com.mazenfahim.YallaBudget.Manager;

import com.mazenfahim.YallaBudget.Model.User;

import java.util.List;

/**
 * Data manager responsible for saving and loading local user records.
 */
public class UserManager {
    public boolean userExist() {
        return SQLiteManager.exists("SELECT 1 FROM user WHERE id = 1");
    }

    public void saveUser(User user) {
        String sql = "INSERT OR REPLACE INTO user(id, username, pin) VALUES(1, ?, ?)";
        SQLiteManager.executeUpdate(sql, user.getName(), user.getPIN());
    }

    public User loadUser() {
        String sql = "SELECT username, pin FROM user WHERE id = 1";
        List<User> users = SQLiteManager.executeQuery(
                sql,
                rs -> new User(rs.getString("username"), rs.getString("pin"))
        );
        return users.isEmpty() ? null : users.get(0);
    }

    public void updateUser(User user) {
        SQLiteManager.executeUpdate("UPDATE user SET pin = ? WHERE id = 1", user.getPIN());
    }

    /**
     * Backward-compatible method name from the original code.
     */
    public void updateuser(User user) {
        updateUser(user);
    }
}
