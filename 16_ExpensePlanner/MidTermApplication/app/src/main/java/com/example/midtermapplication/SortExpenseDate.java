package com.example.midtermapplication;

/*
a. File Name : SortExpenseDate.java
b. Full name of the student 1: Krithika Kasaragod
*/
import java.util.Comparator;

public class SortExpenseDate implements Comparator<ExpenseDataServices.Expense> {
    @Override
    public int compare(ExpenseDataServices.Expense task1, ExpenseDataServices.Expense task2) {

        return -1* (task1.getExpenseDate().compareTo(task2.getExpenseDate()));
    }
}
