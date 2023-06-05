package com.example.homework03;

/*
a. Assignment Homework03.
b. File Name: TaskDataServices.java
c. Full name of the student : Krithika Kasaragod
*/

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskDataServices {


    private static final ArrayList<Task> taskList = new ArrayList<>();

    private static Context mContext;

    public TaskDataServices(Context context) {
        mContext = context;
    }


    //method to create task and add to list
    public static TaskRequest createTask(String name, String date, String priority) {

        if (name == null || name.isEmpty()) {
            return new TaskRequest(mContext.getString(R.string.label_validation_error_task_name));
        }

        if (date == null || date.isEmpty()) {
            return new TaskRequest(mContext.getString(R.string.label_validation_error_setDate));
        }


        Task createTask = new Task(name, date, priority);
        taskList.add(createTask);
        createTask.setTaskArrayList(taskList);


        return new TaskRequest(createTask);
    }

    //method to delete the task from the list
    public static TaskRequest deleteTask(Task task, int index) {
        if (task == null) {
            return new TaskRequest(mContext.getString(R.string.label_task_empty));
        }
        taskList.remove(index);
        task.setTaskArrayList(taskList);
        return new TaskRequest(task);
    }

    public static String formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);
        return parsedDate;
    }

    public static class Task implements Serializable {
        private final String taskName;
        private final String taskDate;
        private final String taskPriority;
        public ArrayList<Task> taskArrayList =new ArrayList<>();

        public Task(String name, String email, String password) {
            this.taskName = name;
            this.taskDate = email;
            this.taskPriority = password;
        }

        public String getTaskName() {
            return taskName;
        }

        public String getTaskDate() {
            return taskDate;
        }

        public String getTaskPriority() {
            return taskPriority;
        }

        public ArrayList<Task> getTaskArrayList() {
            return taskArrayList;
        }

        public void setTaskArrayList(ArrayList<Task> taskArrayList) {
            this.taskArrayList = taskArrayList;
        }
    }

    public static class TaskRequest {
        private final boolean isSuccessful;
        private final String errorMessage;
        private final Task task;

        public TaskRequest(String error) {
            this.isSuccessful = false;
            this.errorMessage = error;
            this.task = null;
        }

        public TaskRequest(Task account) {
            this.isSuccessful = true;
            this.errorMessage = null;
            this.task = account;
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public Task getTask() {
            return task;
        }
    }
}

