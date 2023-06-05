package com.example.inclass10;

/*
a. Assignment #. InClass 10
b. File Name : AddCourseFragment.java
c. Full name of the student 1: Krithika Kasaragod
*/
import static com.example.inclass10.MainActivity.db;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inclass10.databinding.FragmentAddCourseBinding;


public class AddCourseFragment extends Fragment {


    FragmentAddCourseBinding binding;
    String courseName, courseNumber, grade, creditHours;
    double gradePoints=0.0, total_gradePoints=0.0;
    String missingField="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCourseBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_add_course));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.radioButtonA.setChecked(true);
        grade="A";


        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseName = binding.editTextCName.getText().toString();
                courseNumber = binding.editTextCNumber.getText().toString();
                creditHours = binding.editTextCreditHours.getText().toString();

                int checkID = binding.rgGrade.getCheckedRadioButtonId();
                switch (checkID) {
                    case R.id.radioButtonA:
                        grade = "A";
                        gradePoints =4.0;
                        if(!creditHours.isEmpty()) {
                            calculateGradePoints(creditHours, grade);
                        }
                        break;
                    case R.id.radioButtonB:
                        grade = "B";
                        gradePoints =3.0;
                        if(!creditHours.isEmpty()) {
                            calculateGradePoints(creditHours, grade);
                        }
                        break;
                    case R.id.radioButtonC:
                        grade = "C";
                        gradePoints =2.0;
                        if(!creditHours.isEmpty()) {
                            calculateGradePoints(creditHours, grade);
                        }
                        break;
                    case R.id.radioButtonD:
                        grade = "D";
                        gradePoints =1.0;
                        if(!creditHours.isEmpty()) {
                            calculateGradePoints(creditHours, grade);
                        }
                        break;
                    case R.id.radioButtonF:
                        grade = "F";
                        gradePoints =0.0;
                        if(!creditHours.isEmpty()) {
                            calculateGradePoints(creditHours, grade);
                        }
                        break;
                }

                boolean success= validation(courseName,courseNumber,creditHours);
                if(success){
                    calculateGradePoints(creditHours,grade);
                    Log.d("TAG", "onClick:all data "+courseName+" "+courseNumber+" "+creditHours+" "+grade+" "+String.valueOf(total_gradePoints));
                    saveData();
                    mListener.gotoGradeScreenFragment();
                }else{
                    displayAlert(missingField);
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoPreviousFragment();
            }
        });

    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.label_alert))
                .setMessage(message)
                .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private void calculateGradePoints(String creditHours, String grade) {
        switch (grade){
            case "A":
                gradePoints =4.0;
                total_gradePoints = gradePoints * Double.parseDouble(creditHours);
                break;
            case "B":
                gradePoints =3.0;
                total_gradePoints = gradePoints * Double.parseDouble(creditHours);
                break;
            case "C":
                gradePoints =2.0;
                total_gradePoints = gradePoints * Double.parseDouble(creditHours);
                break;
            case "D":
                gradePoints =1.0;
                total_gradePoints = gradePoints * Double.parseDouble(creditHours);
                break;
            case "F":
                gradePoints =0.0;
                total_gradePoints = gradePoints * Double.parseDouble(creditHours);
                break;
        }
    }

    private boolean validation(String courseName, String courseNumber, String creditHours) {
        if(courseName.equals("") && courseNumber.equals("") && creditHours.equals("")){
            missingField=getString(R.string.label_error_all_fields);
            return false;
        }else if(courseName.isEmpty()){
            missingField=getString(R.string.label_error_course_name_field);
            return false;
        }else if(courseNumber.isEmpty()){
            missingField=getString(R.string.label_error_course_number_field);
            return false;
        }else if(creditHours.isEmpty()){
            missingField=getString(R.string.label_error_course_hour_field);
            return false;
        }
        return true;
    }

    private void saveData() {
        db.CDao().insertAll(new Course(courseNumber,courseName,creditHours,grade,String.valueOf(total_gradePoints)));
        Log.d("TAG", "onCreate: all data after: "+db.CDao().getAll());

        binding.editTextCName.setText("");
        binding.editTextCNumber.setText("");
        binding.editTextCreditHours.setText("");
        binding.radioButtonA.setChecked(true);

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IServiceAdd) {
                mListener = (IServiceAdd) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {
        }
    }
    IServiceAdd mListener;
    interface IServiceAdd{
        void gotoGradeScreenFragment();
        void gotoPreviousFragment();
    }
}