package com.example.inclassthree;

/*
a. Assignment InClass03.
b. File Name: DepartmentActivity.java
c. Full name of the student: Krithika Kasaragod
*/
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DepartmentActivity extends AppCompatActivity {

    final static public String SELECTED_DEPT = "Selected_Dept";
    final String TAG = "Department";

    Button btnSelect, btnCancel;
    RadioGroup radioDept;
    String deptName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        setTitle(getResources().getString(R.string.label_department));

        btnSelect = findViewById(R.id.btnSelct);
        btnCancel = findViewById(R.id.btnCancel);
        radioDept = findViewById(R.id.radioDepartment);


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int deptValue = radioDept.getCheckedRadioButtonId();

                if(deptValue<0){
                    Toast.makeText(DepartmentActivity.this, getResources().getString(R.string.label_validation_error_select_dept), Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "onClick: radio" + deptValue);
                    if (deptValue == R.id.radioCompScience) {
                        deptName = getResources().getString(R.string.label_comp);
                    } else if (deptValue == R.id.radioSoftInfo) {
                        deptName = getResources().getString(R.string.label_soft);

                    } else if (deptValue == R.id.radioBioInfo) {
                        deptName = getResources().getString(R.string.label_bio);

                    } else if (deptValue == R.id.radioDataScience) {
                        deptName = getResources().getString(R.string.label_data);
                    }

                    Intent intent = new Intent();
                    intent.putExtra(SELECTED_DEPT, deptName);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}