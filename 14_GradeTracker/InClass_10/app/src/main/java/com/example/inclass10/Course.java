package com.example.inclass10;

/*
a. Assignment #. InClass 10
b. File Name : Course.java
c. Full name of the student 1: Krithika Kasaragod
*/
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="course")
public class Course {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String courseNumber;
    @ColumnInfo
    public String courseName;
    @ColumnInfo
    public String creditHour;
    @ColumnInfo
    public String grade;
    @ColumnInfo
    public String gradePts;

    public Course(){

    }

    public Course(long id, String courseNumber, String courseName, String creditHour, String grade, String gradePts) {
        this.id = id;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.creditHour = creditHour;
        this.grade = grade;
        this.gradePts = gradePts;
    }

    public Course(String courseNumber, String courseName, String creditHour, String grade, String gradePts) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.creditHour = creditHour;
        this.grade = grade;
        this.gradePts = gradePts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCreditHour() {
        return creditHour;
    }

    public void setCreditHour(String creditHour) {
        this.creditHour = creditHour;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradePts() {
        return gradePts;
    }

    public void setGradePts(String gradePts) {
        this.gradePts = gradePts;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseNumber='" + courseNumber + '\'' +
                ", courseName='" + courseName + '\'' +
                ", creditHour='" + creditHour + '\'' +
                ", grade='" + grade + '\'' +
                ", gradePts='" + gradePts + '\'' +
                '}';
    }
}
