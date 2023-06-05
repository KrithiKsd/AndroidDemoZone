package com.example.homework02;

/*
a. Assignment Homework02.
b. File Name: Task.java
c. Full name of the student 1: Krithika Kasaragod
*/

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    String taskName, taskDate, taskPriority;

    public Task(String taskName, String taskDate, String taskPriority) {
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskPriority = taskPriority;
    }

    public Task() {

    }

    protected Task(Parcel in) {
        taskName = in.readString();
        taskDate = in.readString();
        taskPriority = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(taskName);
        parcel.writeString(taskDate);
        parcel.writeString(taskPriority);
    }
}
