package com.example.midtermapplication;

/*
a. File Name : ExpenseDataServices.java
b. Full name of the student 1: Krithika Kasaragod
*/
import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseDataServices {


    private static final ArrayList<Expense> expenseList = new ArrayList<>();

    private static Context mContext;

    public ExpenseDataServices(Context context) {
        mContext = context;
    }


    //method to create expense list and add to list
    public static TaskRequest createTask(String name, String date, String amount, String category) {

        if (name == null || name.isEmpty()) {
            return new TaskRequest(mContext.getString(R.string.label_validation_error_task_name));
        }

        if (date == null || date.isEmpty()) {
            return new TaskRequest(mContext.getString(R.string.label_validation_error_setDate));
        }
        if (amount == null || amount.isEmpty()) {
            return new TaskRequest(mContext.getString(R.string.label_enter_amount));
        }
        if (category == null || category.isEmpty()) {
            return new TaskRequest(mContext.getString(R.string.label_enter_category));
        }

        Expense createTask = new Expense(name, date, amount, category);
        expenseList.add(createTask);
        createTask.setExpenseArrayList(expenseList);


        return new TaskRequest(createTask);
    }

    public static class Expense implements Serializable {
        String expenseName;
        String expenseDate;
        String expenseAmount;
        String expenseCategory;
        public ArrayList<Expense> expenseArrayList = new ArrayList<>();


        public Expense(String expenseName, String expenseDate, String expenseAmount, String expenseCategory) {
            this.expenseName = expenseName;
            this.expenseDate = expenseDate;
            this.expenseAmount = expenseAmount;
            this.expenseCategory = expenseCategory;
        }

        public Expense(String taskDate, String expenseAmount) {
            this.expenseDate = taskDate;
            this.expenseAmount = expenseAmount;
        }

        public String getExpenseName() {
            return expenseName;
        }

        public String getExpenseDate() {
            return expenseDate;
        }

        public String getExpenseAmount() {
            return expenseAmount;
        }

        public ArrayList<Expense> getExpenseArrayList() {
            return expenseArrayList;
        }

        public void setExpenseArrayList(ArrayList<Expense> expenseArrayList) {
            this.expenseArrayList = expenseArrayList;
        }

        public String getExpenseCategory() {
            return expenseCategory;
        }
    }

    public static class TaskRequest {
        private final boolean isSuccessful;
        private final String errorMessage;
        private final Expense task;

        public TaskRequest(String error) {
            this.isSuccessful = false;
            this.errorMessage = error;
            this.task = null;
        }

        public TaskRequest(Expense account) {
            this.isSuccessful = true;
            this.errorMessage = null;
            this.task = account;
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public Expense getTask() {
            return task;
        }
    }
}

