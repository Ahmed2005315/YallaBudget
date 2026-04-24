package com.mazenfahim.YallaBudget.Model;



public class User {
    private String UserName;
    private String PIN;
    private BudgetCycle budgetCycle;
    public User(String name,String pin){
        this.UserName=name;
        this.PIN=pin;
    }

    public String getName(){
        return UserName;
    }
    public boolean VerifyPIN(String pin){
    return PIN.equals(pin);
    }

    public void UpdatePIN(String new_pin){
        PIN=new_pin;
    }

    public void setBudgetCycle(BudgetCycle budgetCycle) {
        this.budgetCycle = budgetCycle;
    }

    public BudgetCycle getBudgetCycle() {
        return budgetCycle;
    }
}
