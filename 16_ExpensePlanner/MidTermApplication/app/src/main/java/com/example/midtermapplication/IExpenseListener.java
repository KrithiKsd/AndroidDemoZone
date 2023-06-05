package com.example.midtermapplication;

/*
a. File Name : IExpenseListener.java
b. Full name of the student 1: Krithika Kasaragod
*/

import java.util.ArrayList;

public interface IExpenseListener {

    void gotoPickCategory();
    void gotoAddExpenseFromPickCategory(String categoryName);
    void createdExpense(ExpenseDataServices.Expense task);
    void gotoExpenseFragment();
    void gotoAddExpenseFromExpense();
    void gotoExpenseSummary(ArrayList<ExpenseDataServices.Expense> list);
    void deleteExpense(int index);
}
