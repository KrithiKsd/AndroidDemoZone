package com.example.inclass05;
/*
a. Assignment #:InCLass05
b. File Name:MainActivity.java
c. Full name of the Student 1:Krithika Kasaragod
*/
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IListviewInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rlContainer,new AppCategoryFragment(),"AppCategoryFragment")
                .commit();
    }
    @Override
    public void gotoAppListFragment(String categoryName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rlContainer,AppListFragment.newInstance(categoryName),"AppListFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoAppDetailsFragment(DataServices.App app) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rlContainer, AppDetailsFragment.newInstance(app),"AppDetailsFragment")
                .addToBackStack(null)
                .commit();
    }
}