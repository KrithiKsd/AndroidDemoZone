package com.example.homework_04;
/*
a. Assignment Homework04.
b. File Name: MainActivity.java
c. Full name of the student : Krithika Kasaragod
*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IUserInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //By default it displays UsersFragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerFragment, new UsersFragment(), getString(R.string.tag_users_fragment))
                .commit();
    }

    @Override
    public void gotoFilterByStateFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment, new FilterByStateFragment(), getString(R.string.tag_filter_by_state_fragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoUsersFragmentWithSelectedCriteria(DataServices.User user) {
        UsersFragment usersFragment= (UsersFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_users_fragment));
        if(usersFragment!=null){
            usersFragment.setFilterData(user.state);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoUsersFragmentWithSelectedCriteriaNew(DataServices.User user) {
        UsersFragment usersFragment= (UsersFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_users_fragment));
        if(usersFragment!=null){
            usersFragment.setSortData(user.criteria);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoSortFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment, new SortFragment(), getString(R.string.tag_sort_fragment))
                .addToBackStack(null)
                .commit();
    }

}