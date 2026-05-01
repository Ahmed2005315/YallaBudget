package com.mazenfahim.YallaBudget.Model;

/**
 * Entity class representing the single local application user.
 */
public class User {
    private String userName;
    private String pinHash;

    public User(String userName, String pinHash) {
        this.userName = userName;
        this.pinHash = pinHash;
    }

    public String getName() {
        return userName;
    }

    public String getPIN() {
        return pinHash;
    }

    public void updatePIN(String newPinHash) {
        this.pinHash = newPinHash;
    }

    /**
     * Verifies the stored hash against a calculated hash.
     */
    public boolean verifyPIN(String calculatedHash) {
        return pinHash != null && pinHash.equals(calculatedHash);
    }

    /**
     * Backward-compatible method name used by the original implementation.
     */
    public boolean VerifyPIN(String calculatedHash) {
        return verifyPIN(calculatedHash);
    }

    /**
     * Backward-compatible method name used by the original implementation.
     */
    public void UpdatePIN(String newPinHash) {
        updatePIN(newPinHash);
    }
}
