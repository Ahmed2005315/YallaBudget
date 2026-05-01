package com.mazenfahim.YallaBudget.Service;

/**
 * Simple data transfer object used by DashboardService to render chart slices.
 */
public class ChartData {
    private final String category;
    private final double amount;

    public ChartData(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}
