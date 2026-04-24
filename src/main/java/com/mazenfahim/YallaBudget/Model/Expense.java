package com.mazenfahim.YallaBudget.Model;

import java.time.LocalDate;


public class Expense {
    private int Id;
    private double Amount;
    private LocalDate Timestamp;
    private Category category;
    private BudgetCycle budgetCycle;

    public Expense(double amount,Category category,BudgetCycle budgetCycle){
        this.Amount=amount;
        this.category=category;
        this.budgetCycle=budgetCycle;
        this.Timestamp=LocalDate.now();
    }

    public void setId(int id){
        Id=id;
    }

    public int getId() {
        return Id;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getTimestamp() {
        return Timestamp;
    }

    public double getAmount() {
        return Amount;
    }
}
