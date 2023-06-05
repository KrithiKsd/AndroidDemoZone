package com.example.homework02;
/*
a. Assignment Homework02.
b. File Name: ToDoListActivity.java
c. Full name of the student 1: Krithika Kasaragod
*/

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

public class ToDoListActivity extends AppCompatActivity {


    final static public String TASK_KEY = "TASK";
    final static public String TASK_KEY_DISPLAY = "TASK_KEY_DISPLAY";
    final static public String TASK_KEY_DELETE = "TASK_KEY_DELETE";
    final static public int REQ_CODE = 100;
    final static public int REQ_CODE_ = 101;
    Button btnCreateTask, btnViewTask;
    TextView tvUpcoming, tvYouHave, tvToDoName, tvToDoDate, tvToDoPriority;
    final String space = " ";
    ArrayList<Task> tasks = new ArrayList<>();
    int index = -1;
    String showDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        setTitle(getResources().getString(R.string.label_todolist_activity));
        btnCreateTask = findViewById(R.id.btnCreateTask);
        btnViewTask = findViewById(R.id.btnViewtask);

        tvUpcoming = findViewById(R.id.tvToDoDisplay);
        tvYouHave = findViewById(R.id.tvToDoYouHave);
        tvToDoName = findViewById(R.id.tvToDoName);
        tvToDoDate = findViewById(R.id.tvToDoDate);
        tvToDoPriority = findViewById(R.id.tvToDoPriority);


        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoListActivity.this, CreateTaskActivity.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });


        btnViewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (tasks.size() == 0) {
                        throw new Exception();
                    } else {

                        String[] charSequence = new String[tasks.size()];
                        for (int i = 0; i < tasks.size(); i++) {
                            charSequence[i] = String.valueOf(tasks.get(i).taskName);
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(ToDoListActivity.this);
                        builder.setTitle(R.string.label_alert_title_list)
                                .setItems(charSequence, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(ToDoListActivity.this, DisplayTaskActivity.class);
                                        intent.putExtra(TASK_KEY_DISPLAY, new Task(tasks.get(which).taskName,
                                                tasks.get(which).taskDate, tasks.get(which).taskPriority));
                                        intent.putExtra(TASK_KEY_DELETE, which);

                                        startActivityForResult(intent, REQ_CODE_);
                                    }
                                }).setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }

                } catch (Exception e) {
                    Toast.makeText(ToDoListActivity.this, getString(R.string.label_empty_list), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (tasks.isEmpty()) {
            String youHave = getString(R.string.label_ToDoYouHave) + space + getString(R.string.label_zero) + space + getString(R.string.label_ToDoYouHave_task);
            tvYouHave.setText(youHave);
            tvToDoName.setText(R.string.label_ToDoNone);
            tvToDoDate.setText("");
            tvToDoPriority.setText("");

        } else {
            Collections.sort(tasks, new SortTaskDate());
            String youHave = getString(R.string.label_ToDoYouHave) + space + tasks.size() + space + getString(R.string.label_ToDoYouHave_task);
            tvYouHave.setText(youHave);

            //half code done to display based on priority
           /* ArrayList<Task> taskSameDateList = new ArrayList<>();

            HashSet<String> setData= new HashSet<>();

            ArrayList<Task> taskNewTest= new ArrayList<>();
           // taskNewTest.add(new Task("task1","2022-04-27","High"))
            Log.d("TAG", "onResume: taskDate "+tasks.get(0).taskDate);
            for (Task item:tasks) {
                if(!setData.add(item.taskDate)){
                    taskSameDateList.add(item);
                }
            }
            Log.d("TAG", "onResume:taskSameDateList "+taskSameDateList.size());

            if(taskSameDateList.size()>0) {
                for (Task item1 : taskSameDateList) {
                    if(item1.taskPriority.equals("High")){

                    }
                }
            }*/

            tvToDoName.setText(tasks.get(0).taskName);
            tvToDoDate.setText(showDate);
            tvToDoPriority.setText(tasks.get(0).taskPriority);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {

            if (resultCode == RESULT_OK) {
                if (data != null && data.hasExtra(ToDoListActivity.TASK_KEY)) {
                    Task task = data.getParcelableExtra(ToDoListActivity.TASK_KEY);
                    tasks.add(new Task(task.taskName, task.taskDate, task.taskPriority));
                }
                if (data != null && data.hasExtra(CreateTaskActivity.TASK_DATE)) {
                    showDate = data.getStringExtra(CreateTaskActivity.TASK_DATE);
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == REQ_CODE_) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.hasExtra(DisplayTaskActivity.TASK_DELETE)) {
                    index = data.getIntExtra(DisplayTaskActivity.TASK_DELETE, -1);
                    tasks.remove(index);
                }
            }
        }
    }
}