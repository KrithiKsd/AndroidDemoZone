package com.example.inclass10;

/*
a. Assignment #. InClass 10
b. File Name : AddCourseActivity.java
c. Full name of the student 1: Krithika Kasaragod
*/

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.inclass10.databinding.ActivityAddCourseBinding;

public class AddCourseActivity extends AppCompatActivity {

    ActivityAddCourseBinding binding;
    String courseName, courseNumber, grade, creditHours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Add Course");
        binding.radioButtonA.setChecked(true);

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseName = binding.editTextCName.getText().toString();
                courseNumber = binding.editTextCNumber.getText().toString();
                creditHours = binding.editTextCreditHours.getText().toString();

                int checkID=binding.rgGrade.getCheckedRadioButtonId();
                switch (checkID){
                    case R.id.radioButtonA:
                        grade = "A";
                        break;
                    case R.id.radioButtonB:
                        grade = "B";
                        break;
                    case R.id.radioButtonC:
                        grade = "C";
                        break;
                    case R.id.radioButtonD:
                        grade = "D";
                        break;
                    case R.id.radioButtonF:
                        grade = "F";
                        break;
                }

            }
        });

    }




}