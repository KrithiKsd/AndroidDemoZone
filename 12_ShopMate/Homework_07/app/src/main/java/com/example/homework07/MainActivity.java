package com.example.homework07;

/*
Assignment #: Homework07
File Name: MainActivity.java
Full Name of Student2: Krithika Kasaragod
 */

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements IService {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getUid() == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, new LoginFragment(), getString(R.string.loginFragment))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, new ShoppingListFragment(), getString(R.string.title_shopping_list_fragment))
                    .commit();
        }
    }

    @Override
    public void goToShoppingListFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new ShoppingListFragment(), getString(R.string.title_shopping_list_fragment))
                .commit();
    }

    @Override
    public void gotoRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new RegisterFragment(), getString(R.string.registerFragment))
                .commit();
    }

    @Override
    public void gotoPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new LoginFragment(), getString(R.string.loginFragment))
                .commit();
    }

    @Override
    public void gotoNewListFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new CreateListFragment(), getString(R.string.label_new_list))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToUserListFragment(String docId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, UserListFragment.newInstance(docId), getString(R.string.title_user_list))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToItemList(String docId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, ItemListFragment.newInstance(docId), getString(R.string.title_item_list))
                .addToBackStack(null)
                .commit();
    }

}