package com.mazenfahim.YallaBudget.Service;

import com.mazenfahim.YallaBudget.Manager.UserManager;
import com.mazenfahim.YallaBudget.Model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Business logic service for local PIN authentication and user management.
 */
public class UserService {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private final UserManager userManager;
    private int failedAttempts;
    private long lockedUntilMillis;

    public UserService() {
        this(new UserManager());
    }

    public UserService(UserManager userManager) {
        this.userManager = userManager;
    }

    public boolean userExists() {
        return userManager.userExist();
    }

    public void createUser(String username, String rawPin) {
        validatePin(rawPin);
        String safeUsername = (username == null || username.isBlank()) ? "Student" : username.trim();
        userManager.saveUser(new User(safeUsername, hashPin(rawPin)));
    }

    public boolean authenticate(String rawPin) {
        if (rawPin == null || rawPin.isBlank()) {
            return false;
        }
        if (isLockedOut()) {
            return false;
        }

        User user = userManager.loadUser();
        if (user == null) {
            return false;
        }

        boolean valid = user.verifyPIN(hashPin(rawPin)) || user.getPIN().equals(rawPin);
        if (valid) {
            failedAttempts = 0;
            return true;
        }

        failedAttempts++;
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            lockedUntilMillis = System.currentTimeMillis() + 30_000;
        }
        return false;
    }

    public void changePin(String oldPin, String newPin) {
        if (!authenticate(oldPin)) {
            throw new IllegalArgumentException("Old PIN is incorrect.");
        }
        validatePin(newPin);
        User user = userManager.loadUser();
        user.updatePIN(hashPin(newPin));
        userManager.updateUser(user);
    }

    public User loadUser() {
        return userManager.loadUser();
    }

    public boolean isLockedOut() {
        if (lockedUntilMillis == 0) {
            return false;
        }
        if (System.currentTimeMillis() >= lockedUntilMillis) {
            failedAttempts = 0;
            lockedUntilMillis = 0;
            return false;
        }
        return true;
    }

    public int getSecondsUntilUnlock() {
        if (!isLockedOut()) {
            return 0;
        }
        long remainingMillis = lockedUntilMillis - System.currentTimeMillis();
        return (int) Math.max(1, Math.ceil(remainingMillis / 1000.0));
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    private void validatePin(String pin) {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
    }

    private String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte value : encodedHash) {
                builder.append(String.format("%02x", value));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available.", e);
        }
    }
}
