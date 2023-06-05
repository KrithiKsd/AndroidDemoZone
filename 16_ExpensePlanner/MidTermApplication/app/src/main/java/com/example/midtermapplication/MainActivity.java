package com.example.midtermapplication;

/*
a. File Name : MainActivity.java
b. Full name of the student 1: Krithika Kasaragod
*/

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IExpenseListener {


    ArrayList<ExpenseDataServices.Expense> createdExpenseList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExpenseDataServices taskDataServices=new ExpenseDataServices(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerFragment, new ExpenseFragment(), getString(R.string.tag_expense))
                .commit();

    }

    @Override
    public void gotoPickCategory() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment, new PickCategoryFragment(), getString(R.string.tag_pick_category))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoAddExpenseFromPickCategory(String categoryName) {
        AddExpenseFragment fragment= (AddExpenseFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_add_expense));
        if(fragment!=null){
            fragment.setPickCategory(categoryName);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void createdExpense(ExpenseDataServices.Expense task) {
        createdExpenseList.add(task);
        ExpenseFragment fragment= (ExpenseFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_expense));
        if(fragment!=null){
            fragment.setExpenseList(createdExpenseList);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoExpenseFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoAddExpenseFromExpense() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment, new AddExpenseFragment(), getString(R.string.tag_add_expense))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoExpenseSummary(ArrayList<ExpenseDataServices.Expense> list) {
        createdExpenseList.clear();
        createdExpenseList.addAll(list);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment, ExpenseSummaryFragment.newInstance(createdExpenseList, getString(R.string.tag_expense_summary)))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void deleteExpense(int index) {
       if(createdExpenseList.size()>0) {
           createdExpenseList.remove(index);
       }
        ExpenseFragment fragment= (ExpenseFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_expense));
        if(fragment!=null){
            fragment.setExpenseList(createdExpenseList);
        }
        getSupportFragmentManager().popBackStack();

    }
}