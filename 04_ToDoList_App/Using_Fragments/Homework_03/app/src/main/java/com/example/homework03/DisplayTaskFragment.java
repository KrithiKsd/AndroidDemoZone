package com.example.homework03;
/*
a. Assignment Homework03.
b. File Name: DisplayTaskFragment.java
c. Full name of the student : Krithika Kasaragod
*/

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.homework03.databinding.FragmentDisplayTaskBinding;

import java.text.ParseException;

public class DisplayTaskFragment extends Fragment {


    FragmentDisplayTaskBinding displayTaskBinding;
    private static final String ARG_TASK = "TASK";
    private static final String ARG_INDEX = "INDEX";
    TaskDataServices.Task mTask;
    private int index;
    TaskInterface mListener;

    public DisplayTaskFragment() {
    }

    public static DisplayTaskFragment newInstance(TaskDataServices.Task task, int index) {
        DisplayTaskFragment fragment = new DisplayTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTask = (TaskDataServices.Task) getArguments().getSerializable(ARG_TASK);
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        displayTaskBinding = FragmentDisplayTaskBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_todo_display_fragment_title));
        return displayTaskBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayTaskBinding.tvTaskNameResult.setText(mTask.getTaskName());
        try {
            String date = TaskDataServices.formatDate(mTask.getTaskDate(), "yyyy-mm-dd", "mm/dd/yyyy");
            displayTaskBinding.tvTaskDateResult.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        displayTaskBinding.tvTaskPriorityResult.setText(mTask.getTaskPriority());


        displayTaskBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskDataServices.TaskRequest deletedTask = TaskDataServices.deleteTask(mTask, index);
                if (deletedTask.isSuccessful()) { //successful

                    TaskDataServices.Task task = deletedTask.getTask();
                    mListener.deleteTask(task);

                } else { //not successful
                    String error = deletedTask.getErrorMessage();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        displayTaskBinding.btnDisplayCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoTodoFragment();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof TaskInterface) {
                mListener = (TaskInterface) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {
        }
    }
}