package com.example.inclass04;
/*
a. Assignment #. InClass04
b. File Name : MainActivity.java
c. Full name of the student: Krithika Kasaragod
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements FragmentInterface {

    DataServices.Account mAccount;
    DataServices dataServices = new DataServices(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.containerFragment,
                new LoginFragment(), getString(R.string.label_tagLoginFragment))
                .commit();
    }

    @Override
    public void sendAccount(DataServices.Account account) {
        mAccount = account;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.containerFragment,
                        AccountFragment.newInstance(mAccount), getString(R.string.label_tagAccountFragment))
                .commit();

    }

    @Override
    public void sendAccountFromRegister(DataServices.Account account) {
        mAccount = account;

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, AccountFragment.newInstance(mAccount),
                getString(R.string.label_tagAccountFragment))
                .commit();


    }

    @Override
    public void gotoRegisterFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                new RegisterFragment(), getString(R.string.label_tagRegisterFragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoLoginFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoUpdateFragment(DataServices.Account account) {
        mAccount = account;
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                UpdateFragment.newInstance(mAccount), getString(R.string.label_tagUpdateFragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoAccountFragment() {
       getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoAccountFragmentWithAccount(DataServices.Account account) {
        mAccount = account;

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment, AccountFragment.newInstance(mAccount),
                getString(R.string.label_tagAccountFragment)).commit();
    }

    @Override
    public void logoutAccount() {
        mAccount = null;
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                new LoginFragment(), getString(R.string.label_tagLoginFragment))
                .commit();
    }

    //Old working way

/*
    @Override
    public void sendAccount(DataServices.Account account) {
        mAccount = account;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.containerFragment,
                        AccountFragment.newInstance(mAccount), getString(R.string.label_tagAccountFragment)).commit();

    }

    @Override
    public void sendAccountFromRegister(DataServices.Account account) {
        mAccount = account;

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, AccountFragment.newInstance(mAccount),
                getString(R.string.label_tagAccountFragment)).commit();


    }

    @Override
    public void gotoRegisterFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                new RegisterFragment(), getString(R.string.label_tagRegisterFragment))
                .addToBackStack(getString(R.string.label_tagLoginFragment))
                .commit();
    }

    @Override
    public void gotoLoginFragment() {

        FragmentManager fm = this.getSupportFragmentManager();
        fm.popBackStack(getString(R.string.label_tagLoginFragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.label_tagLoginFragment));

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                new LoginFragment(), getString(R.string.label_tagLoginFragment))
                .remove(fragment).commit();

    }

    @Override
    public void gotoUpdateFragment(DataServices.Account account) {
        mAccount = account;
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                UpdateFragment.newInstance(mAccount), getString(R.string.label_tagUpdateFragment))
                .addToBackStack(getString(R.string.label_tagAccountFragment))
                .commit();
    }

    @Override
    public void gotoAccountFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.popBackStack(getString(R.string.label_tagAccountFragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void gotoAccountFragmentWithAccount(DataServices.Account account) {
        mAccount = account;

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, AccountFragment.newInstance(mAccount),
                getString(R.string.label_tagAccountFragment)).commit();
    }

    @Override
    public void logoutAccount() {
        mAccount = null;
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                new LoginFragment(), getString(R.string.label_tagLoginFragment))
                .commit();
    }
*/


}