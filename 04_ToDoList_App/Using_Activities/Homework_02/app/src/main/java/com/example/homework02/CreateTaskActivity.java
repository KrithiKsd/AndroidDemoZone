package com.example.homework02;
/*
a. Assignment Homework02.
b. File Name: CreateTaskActivity.java
c. Full name of the student 1: Krithika Kasaragod
*/

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity {


    final static public String TASK_DATE = "TASK_DATE";
    Button btnSetDate, btnSubmit, btnClear;
    TextView tvDateValue;
    RadioGroup radioPriority;
    RadioButton radioHigh;
    EditText etTask;
    String finalDate, showDate;
    String finalDateNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        setTitle(getResources().getString(R.string.label_todo_create_activity));

        btnSetDate = findViewById(R.id.btnSetDate);
        tvDateValue = findViewById(R.id.tvDateValue);
        radioPriority = findViewById(R.id.radioPriority);
        btnClear = findViewById(R.id.btnClear);
        btnSubmit = findViewById(R.id.btnSubmit);
        radioHigh = findViewById(R.id.radioHigh);
        etTask = findViewById(R.id.etTaskName);

        radioHigh.setChecked(true);

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar newCalendar = Calendar.getInstance();

                final DatePickerDialog StartTime = new DatePickerDialog(CreateTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {

                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);

                                tvDateValue.setText((dayOfMonth + " / " + (monthOfYear + 1) + " / " + year));
                                showDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                finalDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));

                StartTime.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
                StartTime.show();

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String priorityType = getString(R.string.label_high);
                int priorityValue = radioPriority.getCheckedRadioButtonId();
                if (priorityValue == R.id.radioHigh) {
                    priorityType = getResources().getString(R.string.label_high);
                } else if (priorityValue == R.id.radioMedium) {
                    priorityType = getResources().getString(R.string.label_medium);
                } else if (priorityValue == R.id.radioLow) {
                    priorityType = getResources().getString(R.string.label_low);
                }

                boolean validationResult = validation();
                if (validationResult) {

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date objDate = dateFormat.parse(finalDate);
                        //Expected date format
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                        finalDateNew = dateFormat2.format(objDate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent();
                    intent.putExtra(ToDoListActivity.TASK_KEY, new Task(etTask.getText().toString(),
                            finalDateNew, priorityType));
                    intent.putExtra(CreateTaskActivity.TASK_DATE, showDate);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                setResult(RESULT_CANCELED);
            }
        });

    }

    public boolean validation() {

        if (etTask.getText().toString().length() == 0) {
            Toast.makeText(this, getString(R.string.label_validation_error_taskname), Toast.LENGTH_SHORT).show();
            return false;
        } else if (tvDateValue.length() == 0) {
            Toast.makeText(this, getString(R.string.label_validation_error_setDate), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}