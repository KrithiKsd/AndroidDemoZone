package com.example.inclassthree;
/*
a. Assignment InClass03.
b. File Name: ProfileActivity.java
c. Full name of the student: Krithika Kasaragod
*/

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {


    TextView tvName, tvEmail, tvID, tvDept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getResources().getString(R.string.label_profile));

        tvName = findViewById(R.id.tvNameValue);
        tvEmail = findViewById(R.id.tvEmailValue);
        tvID = findViewById(R.id.tvIDValue);
        tvDept = findViewById(R.id.tvDeptValue);

        if (getIntent() != null &&
                getIntent().getExtras() != null && getIntent().hasExtra(RegistrationActivity.PROFILE_KEY)) {

            UserProfile userProfile = getIntent().getParcelableExtra(RegistrationActivity.PROFILE_KEY);
            tvName.setText(userProfile.nameUser);
            tvEmail.setText(userProfile.emailUser);
            tvDept.setText(userProfile.deptUser);
            tvID.setText(String.valueOf(userProfile.idUser));


        }
    }
}