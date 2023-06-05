package com.example.homework_04.Sort;
/*
a. Assignment Homework04.
b. File Name: StateAscendingSort.java
c. Full name of the student : Krithika Kasaragod
*/

import com.example.homework_04.DataServices;

import java.util.Comparator;

public class StateAscendingSort implements Comparator<DataServices.User> {
    @Override
    public int compare(DataServices.User user1, DataServices.User user2) {

        return (user1.getState().compareTo(user2.getState()));
    }
}
