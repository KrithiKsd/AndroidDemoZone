package com.example.homework03;

/*
a. Assignment Homework03.
b. File Name: TaskDataServices.java
c. Full name of the student : Krithika Kasaragod
*/

public interface TaskInterface {

    void gotoCreateTaskFragment();

    void createdTask(TaskDataServices.Task task);

    void displayTask(TaskDataServices.Task task, int index);

    void deleteTask(TaskDataServices.Task task);

    void gotoTodoFragment();
}
