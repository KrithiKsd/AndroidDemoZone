package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoLoginFromForumsFragment() {
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
}