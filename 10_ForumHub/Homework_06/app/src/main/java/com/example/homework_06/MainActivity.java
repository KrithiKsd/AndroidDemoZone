package com.example.homework_06;
/*
Assignment #: Homework 06
File Name: MainActivity.java
Full Name of Student 1: Krithika Kasaragod
*/
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements IService {
    public static final String myPreference = "MY_PREFERENCE";
    public static final String creatorName = "creatorName";
    public static final String UID = "UID";

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();
        if(mAuth.getUid()==null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, new LoginFragment(),getString(R.string.loginFragment))
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, new ForumsFragment(),getString(R.string.forumsFragment))
                    .commit();
        }

    }

    @Override
    public void gotoForumsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new ForumsFragment(),getString(R.string.forumsFragment))
                .commit();
    }

    @Override
    public void gotoRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new RegisterFragment(),getString(R.string.registerFragment))
                .commit();
    }

    @Override
    public void gotoPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoLoginFromForumsFragment() {

        SharedPreferences sharedPreferences = getSharedPreferences(myPreference, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.clear();
        myEdit.apply();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new LoginFragment(),getString(R.string.loginFragment))
                .commit();
    }

    @Override
    public void gotoNewForumFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, new NewForumFragment(),getString(R.string.newForumFragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void refreshForumsFragment() {
        ForumsFragment fragment = (ForumsFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.forumsFragment));
        if(fragment!=null){
            fragment.getForumListData();
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoForumFragment(Forum forum) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, ForumFragment.newInstance(forum),"ForumFragment")
                .addToBackStack(null)
                .commit();
    }


}