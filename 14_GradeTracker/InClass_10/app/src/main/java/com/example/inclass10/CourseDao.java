package com.example.inclass10;

/*
a. Assignment #. InClass 10
b. File Name : CourseDao.java
c. Full name of the student 1: Krithika Kasaragod
*/

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    public void insertAll(Course... course);

    @Delete
    public void delete(Course course);

    @Update
    public void update(Course course);

   @Query("SELECT * FROM COURSE")
    public List<Course> getAll();

    @Query("SELECT * FROM COURSE WHERE id= :id limit 1")
    Course getByID(long id);

}
