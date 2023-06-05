package com.example.homework02;
/*
a. Assignment Homework02.
b. File Name: DisplayTaskActivity.java
c. Full name of the student 1: Krithika Kasaragod
*/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayTaskActivity extends AppCompatActivity {


    TextView tvName, tvDate, tvPriority;
    Button btnDelete, btnCancel;
    final static public String TASK_DELETE = "TASK_DELETE";
    int KEY_Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);
        setTitle(getResources().getString(R.string.label_todo_display_activity));
        tvName = findViewById(R.id.tvTaskNameRes);
        tvDate = findViewById(R.id.tvTaskDateRes);
        tvPriority = findViewById(R.id.tvTaskPriorityRes);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnDisplayCancel);


        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(ToDoListActivity.TASK_KEY_DISPLAY)) {

            Task task = getIntent().getParcelableExtra(ToDoListActivity.TASK_KEY_DISPLAY);
            tvName.setText(task.taskName);
            tvDate.setText(task.taskDate);
            tvPriority.setText(task.taskPriority);

            KEY_Delete = getIntent().getIntExtra(ToDoListActivity.TASK_KEY_DELETE, 0);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(DisplayTaskActivity.TASK_DELETE, KEY_Delete);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}