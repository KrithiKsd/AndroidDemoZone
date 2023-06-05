package com.example.inclass10;

/*
a. Assignment #. InClass 10
b. File Name : MainActivity.java
c. Full name of the student 1: Krithika Kasaragod
*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements GradeScreenFragment.IService, AddCourseFragment.IServiceAdd {

    static public InClassDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         db = Room.databaseBuilder(this,InClassDatabase.class,"course.db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new GradeScreenFragment(), "GradeScreenFragment")
                .commit();
    }

    @Override
    public void gotoAddCourseFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddCourseFragment(), "AddCourseFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoGradeScreenFragment() {
        GradeScreenFragment fragment = (GradeScreenFragment) getSupportFragmentManager().findFragmentByTag("GradeScreenFragment");
        if(fragment!=null){
            fragment.getAllCourseData();
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }
}