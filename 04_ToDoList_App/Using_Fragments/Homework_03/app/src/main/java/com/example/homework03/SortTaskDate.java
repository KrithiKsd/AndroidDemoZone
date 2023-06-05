package com.example.homework03;

/*
a. Assignment Homework03.
b. File Name: SortTaskDate.java
c. Full name of the student : Krithika Kasaragod
*/

import java.util.Comparator;

public class SortTaskDate implements Comparator<TaskDataServices.Task> {
    @Override
    public int compare(TaskDataServices.Task task1, TaskDataServices.Task task2) {

        return (task1.getTaskDate().compareTo(task2.getTaskDate()));
    }
}
