package com.example.homework03;

/*
a. Assignment Homework03.
b. File Name: TaskMainActivity.java
c. Full name of the student : Krithika Kasaragod
*/

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class TaskMainActivity extends AppCompatActivity implements TaskInterface {

    TaskDataServices.Task mTask;
    TaskDataServices taskDataServices=new TaskDataServices(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_main);

        //loads ToDoListFragment as initial fragment
        getSupportFragmentManager().beginTransaction().add(R.id.containerFragment,
                new ToDoListFragment(), getString(R.string.label_tagToDoListFragment))
                .commit();
    }

    @Override
    public void gotoCreateTaskFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                new CreateTaskFragment(), getString(R.string.label_tagCreateTaskFragment))
                .addToBackStack(getString(R.string.label_tagToDoListFragment))
                .commit();

    }

    @Override
    public void createdTask(TaskDataServices.Task task) {
        mTask = task;
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, ToDoListFragment.newInstance(mTask),
                getString(R.string.label_tagToDoListFragment)).commit();
    }

    @Override
    public void displayTask(TaskDataServices.Task task, int index) {
        mTask = task;
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                DisplayTaskFragment.newInstance(mTask, index), getString(R.string.label_tagDisplayTaskFragment))
                .addToBackStack(getString(R.string.label_tagToDoListFragment))
                .commit();
    }

    @Override
    public void deleteTask(TaskDataServices.Task task) {
        mTask = task;
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, ToDoListFragment.newInstance(mTask),
                getString(R.string.label_tagToDoListFragment)).commit();
    }

    //called twice from DisplayFragment & CreateFragment in Cancel button
    @Override
    public void gotoTodoFragment() {
        getSupportFragmentManager().popBackStack();
    }
}