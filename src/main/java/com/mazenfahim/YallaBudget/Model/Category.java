package com.mazenfahim.YallaBudget.Model;

public class Category {
    private int Id;
    private String CategoryName;
    private String Description;

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }
}
