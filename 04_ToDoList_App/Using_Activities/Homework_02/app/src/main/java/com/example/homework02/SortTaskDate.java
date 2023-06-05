package com.example.homework02;

/*
a. Assignment Homework02.
b. File Name: SortTaskDate.java
c. Full name of the student 1: Krithika Kasaragod
*/

import java.util.Comparator;

public class SortTaskDate implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {

        return (task1.taskDate.compareTo(task2.taskDate));
    }
}
