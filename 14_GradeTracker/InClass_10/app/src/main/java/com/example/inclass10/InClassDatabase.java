package com.example.inclass10;

/*
a. Assignment #. InClass 10
b. File Name : InClassDatabase.java
c. Full name of the student 1: Krithika Kasaragod
*/
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Course.class},version = 2)
public abstract class InClassDatabase extends RoomDatabase {

    public abstract CourseDao CDao();

}
