package com.example.howmework_05;
/*
a. Assignment Homework 05.
b. File Name: MainActivity.java
c. Full name of the student : Krithika Kasaragod
*/
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IServices {

    SharedPreferences sharedpreferences;
    public static final String myPreference = "MY_PREFERENCE";
    public static final String tokenId = "tokenId";
    public static final String fullName = "fullName";
    public static final String userId = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Checks whether user is already logged in or not and goes to the
        Post List Fragment with the current user information if it exists, else goes to Login Fragment */

        sharedpreferences = getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(tokenId)) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PostsListFragment(), getString(R.string.tag_PostsListFragment))
                    .commit();

        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment(), getString(R.string.tag_LoginFragment))
                    .commit();
        }

    }

    @Override
    public void gotoPostListFromLogin(DataService.LoginDetails loginDetails) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(tokenId, loginDetails.getToken());
        editor.putString(fullName, loginDetails.getUserFullName());
        editor.putString(userId, loginDetails.getUserId());
        editor.commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PostsListFragment(), getString(R.string.tag_PostsListFragment))
                .commit();
    }

    @Override
    public void gotoCreateNewAccountFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CreateNewAccountFragment(), getString(R.string.tag_CreateNewAccountFragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoPostListFromRegister(DataService.LoginDetails loginDetails) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(tokenId, loginDetails.getToken());
        editor.putString(fullName, loginDetails.getUserFullName());
        editor.putString(userId, loginDetails.getUserId());
        editor.commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PostsListFragment(), getString(R.string.tag_PostsListFragment))
                .commit();
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoCreatePostFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CreatePostFragment(), getString(R.string.tag_CreatePostFragment))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void refreshPostList() {
        PostsListFragment postList = (PostsListFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_PostsListFragment));
        if (postList != null) {
            postList.getPostList(getString(R.string.page_number));
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void logout() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment(), getString(R.string.tag_LoginFragment))
                .commit();
    }

}