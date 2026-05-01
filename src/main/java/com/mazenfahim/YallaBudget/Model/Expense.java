package com.mazenfahim.YallaBudget.Model;

import java.time.LocalDateTime;

/**
 * Entity class representing one logged expense transaction.
 */
public class Expense {
    private int id;
    private double amount;
    private LocalDateTime timestamp;
    private Category category;
    private int cycleId;

    public Expense(double amount, Category category, int cycleId) {
        this.amount = amount;
        this.category = category;
        this.cycleId = cycleId;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCycleId() {
        return cycleId;
    }
}
