package com.mazenfahim.YallaBudget.Model;

/**
 * Entity class representing an expense category.
 */
public class Category {
    private int id;
    private String categoryName;
    private String description;

    public Category(int id, String categoryName, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void updateDetails(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
