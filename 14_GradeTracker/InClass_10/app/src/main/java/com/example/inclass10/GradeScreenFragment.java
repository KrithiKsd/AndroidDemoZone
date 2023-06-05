package com.example.inclass10;

/*
a. Assignment #. InClass 10
b. File Name : GradeScreenFragment.java
c. Full name of the student 1: Krithika Kasaragod
*/
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inclass10.databinding.FragmentAddCourseBinding;
import com.example.inclass10.databinding.FragmentGradeScreenBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class GradeScreenFragment extends Fragment {


    FragmentGradeScreenBinding binding;
    LinearLayoutManager linearLayoutManager;
    CourseAdapter listAdapter;
    ArrayList<Course> courseList = new ArrayList<>();
    double gpa = 0.0, total_gradePoints = 0.0, total_hours = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGradeScreenBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_grades));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        getAllCourseData();

        binding.recyclerCourse.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerCourse.setLayoutManager(linearLayoutManager);
        binding.recyclerCourse.getRecycledViewPool().setMaxRecycledViews(0, 0);


    }

    void getAllCourseData() {
        Log.d("TAG", "onViewCreated: getAll" + MainActivity.db.CDao().getAll());
        if (courseList.size() > 0) {
            courseList.clear();
        }

        courseList.addAll(MainActivity.db.CDao().getAll());
        total_gradePoints=0.0;
        total_hours=0.0;
        gpa =0.0;
        for (Course item : courseList) {
            total_hours = total_hours + (Double.parseDouble(item.getCreditHour()));
            total_gradePoints = total_gradePoints + (Double.parseDouble(item.getGradePts()));
        }

        gpa = total_gradePoints / total_hours;

        binding.tvGPA.setText("GPA:" + String.valueOf(new DecimalFormat("##.##").format(gpa)));
        binding.tvHours.setText("Hours:" + String.valueOf(total_hours));

        listAdapter = new CourseAdapter(courseList);
        binding.recyclerCourse.setAdapter(listAdapter);
    }

    public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

        ArrayList<Course> arrayList;

        public CourseAdapter(ArrayList<Course> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
            CourseViewHolder viewHolder = new CourseViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
            Course course = this.arrayList.get(position);
            holder.tvCourseNumber.setText(this.arrayList.get(position).courseNumber);
            holder.tvCourseName.setText(this.arrayList.get(position).courseName);
            holder.tvCreditHours.setText(this.arrayList.get(position).creditHour);
            holder.tvGrade.setText(this.arrayList.get(position).grade);


            holder.imageViewTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(course);
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

        public class CourseViewHolder extends RecyclerView.ViewHolder {
            TextView tvCourseName, tvGrade, tvCourseNumber, tvCreditHours;
            ImageView imageViewTrash;

            public CourseViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCourseName = itemView.findViewById(R.id.tvCourseNameList);
                tvGrade = itemView.findViewById(R.id.tvGrade);
                tvCourseNumber = itemView.findViewById(R.id.tvCourseNumber);
                tvCreditHours = itemView.findViewById(R.id.tvCredit);
                imageViewTrash = itemView.findViewById(R.id.imageViewTrash);
            }
        }
    }

    private void delete(Course course) {
        MainActivity.db.CDao().delete(course);
        getAllCourseData();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_course, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add:
                mListener.gotoAddCourseFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IService) {
                mListener = (IService) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {
        }
    }

    IService mListener;

    interface IService {
        void gotoAddCourseFragment();
    }
}