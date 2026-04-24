package com.mazenfahim.YallaBudget.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BudgetCycle {
    private int Id;
    private double Total_Allowance;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private double RemainingBalance;
    private List<Expense> Expenses=new ArrayList<>();

    public BudgetCycle(double total_Allowance,LocalDate startDate,LocalDate endDate){
        this.Total_Allowance=total_Allowance;
        this.StartDate=startDate;
        this.EndDate=endDate;
        this.RemainingBalance=total_Allowance;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public int CalculateRemainingDays(){
        return Math.toIntExact(ChronoUnit.DAYS.between(StartDate, EndDate));
    }

    public double CalculateDailyLimit(){
        double DailyLimit=RemainingBalance/CalculateRemainingDays();
        return DailyLimit > 0 ? DailyLimit : 0;
    }

    public void UpdateRemainingBalance(Double amount){
        RemainingBalance-=amount;
    }

    public void AddExpense(Expense expense){
        Expenses.add(expense);
        UpdateRemainingBalance(expense.getAmount());
    }

}
