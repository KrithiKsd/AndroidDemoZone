package com.example.homework03;
/*
a. Assignment Homework03.
b. File Name: ToDoListFragment.java
c. Full name of the student : Krithika Kasaragod
*/

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.homework03.databinding.FragmentToDoListBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;


public class ToDoListFragment extends Fragment {


    FragmentToDoListBinding toDoListBinding;
    TaskInterface mListener;
    TaskDataServices.Task mTask;

    private static final String ARG_TASK = "TASK";


    public static ToDoListFragment newInstance(TaskDataServices.Task task) {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTask = (TaskDataServices.Task) getArguments().getSerializable(ARG_TASK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        toDoListBinding = FragmentToDoListBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_todolist_fragment_title));
        return toDoListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (mTask == null || mTask.getTaskArrayList().isEmpty()) {
            toDoListBinding.tvToDoYouHave.setText(String.format(getString(R.string.label_you_have_task), 0));
            toDoListBinding.tvToDoName.setText(R.string.label_ToDoNone);
        } else {
            Collections.sort(mTask.getTaskArrayList(), new SortTaskDate());
            toDoListBinding.tvToDoYouHave.setText(String.format(getString(R.string.label_you_have_task), mTask.getTaskArrayList().size()));
            toDoListBinding.tvToDoName.setText(mTask.getTaskArrayList().get(0).getTaskName());
            try {
                String date = TaskDataServices.formatDate(mTask.getTaskArrayList().get(0).getTaskDate(), "yyyy-mm-dd", "mm/dd/yyyy");
                toDoListBinding.tvToDoDate.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            toDoListBinding.tvToDoPriority.setText(mTask.getTaskArrayList().get(0).getTaskPriority());
        }

        toDoListBinding.btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoCreateTaskFragment();
            }
        });

        toDoListBinding.btnViewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTask();
            }
        });
    }

    //method to view task
    private void viewTask() {
        try {
            if (mTask.getTaskArrayList().size() == 0) {
                throw new Exception();
            } else {

                String[] charSequence = new String[mTask.getTaskArrayList().size()];
                for (int i = 0; i < mTask.getTaskArrayList().size(); i++) {
                    charSequence[i] = String.valueOf(mTask.getTaskArrayList().get(i).getTaskName());
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.label_alert_title_list)

                        .setItems(charSequence, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                TaskDataServices.Task newTask = new TaskDataServices.Task(mTask.getTaskArrayList().get(which).getTaskName(),
                                        mTask.getTaskArrayList().get(which).getTaskDate(),
                                        mTask.getTaskArrayList().get(which).getTaskPriority());
                                mListener.displayTask(newTask, which);

                            }
                        }).setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.label_empty_list), Toast.LENGTH_SHORT).show();
        }

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

        } catch (RuntimeException e) {
        }
    }

}