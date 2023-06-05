package com.example.inclassthree;
/*
a. Assignment InClass03.
b. File Name: RegistrationActivity.java
c. Full name of the student: Krithika Kasaragod
*/

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    final static public String PROFILE_KEY = "PROFILE";
    final String TAG = "Registration";

    Button btnSelectDept, btnSubmit;
    TextView tvDepartment;
    EditText etName, etEmail, etID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(getResources().getString(R.string.label_registration));
        btnSelectDept = findViewById(R.id.btnSelectDept);
        tvDepartment = findViewById(R.id.tvDeptSelect);
        btnSubmit = findViewById(R.id.btnSubmit);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etID = findViewById(R.id.etID);
        Log.d(TAG, "onClick: textDept" + tvDepartment.getText().toString());


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validationResult = validate();
                if (validationResult) {
                    Log.d(TAG, "onClick: Validate Success");
                    Intent intent = new Intent(RegistrationActivity.this, ProfileActivity.class);

                    UserProfile userProfile = new UserProfile(etName.getText().toString(),
                            etEmail.getText().toString(),
                            tvDepartment.getText().toString(),
                            Integer.parseInt(etID.getText().toString()));

                    intent.putExtra(PROFILE_KEY, userProfile);
                    startActivity(intent);
                }
            }
        });

        btnSelectDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegistrationActivity.this, DepartmentActivity.class);
                resultDeptLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> resultDeptLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null && result.getData().hasExtra(DepartmentActivity.SELECTED_DEPT)) {
                    tvDepartment.setText(result.getData().getStringExtra(DepartmentActivity.SELECTED_DEPT));
                } else if (result.getResultCode() == RESULT_CANCELED) {
                    Log.d(TAG, "onActivityResult: OnCancel");
                }
            }
        }
    });

    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean validate() {
        try {
             if (etName.getText().toString().length() > 20) {
                Toast.makeText(this, getResources().getString(R.string.label_validation_error_name_length), Toast.LENGTH_LONG).show();
                return false;

            }  else if (etName.getText().toString().length() ==0) {
                Toast.makeText(this, "Name Should not be null", Toast.LENGTH_LONG).show();
                return false;

            }  else if (etID.getText().toString().length() == 0) {
                Toast.makeText(this, "ID Should not be null", Toast.LENGTH_LONG).show();
                return false;

            } else if (etName.getText().toString().length() == 0 || etID.getText().toString().length() == 0
                    || etEmail.length() == 0) {
                Toast.makeText(this, getResources().getString(R.string.label_validation_error_empty_fields), Toast.LENGTH_LONG).show();
                return false;

            }
            else if (etEmail.length() == 0) {
                Toast.makeText(this, "email Should not be null", Toast.LENGTH_LONG).show();
                return false;

            }

            else if (tvDepartment.getText().toString().isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.label_validation_error_select_department), Toast.LENGTH_LONG).show();
                return false;

            } else if (isValidEmail(etEmail.getText().toString())) {
                return true;

            } else if (!isValidEmail(etEmail.getText().toString())) {
                Toast.makeText(this, getResources().getString(R.string.label_validation_error_email_id), Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Exception e) {
            Log.d(TAG, "validate: exception" + e.getStackTrace());
        }
        return false;
    }

}