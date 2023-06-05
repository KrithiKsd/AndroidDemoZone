package com.example.homework03;

/*
a. Assignment Homework03.
b. File Name: CreateTaskFragment.java
c. Full name of the student : Krithika Kasaragod
*/

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.homework03.databinding.FragmentCreateTaskBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTaskFragment extends Fragment {


    FragmentCreateTaskBinding createTaskBinding;
    TaskInterface mListener;
    String finalDate, showDate, formattedDate;

    public static CreateTaskFragment newInstance(String param1, String param2) {
        CreateTaskFragment fragment = new CreateTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createTaskBinding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_todo_create_fragment_title));
        return createTaskBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createTaskBinding.rbHighPriority.setChecked(true);

        createTaskBinding.btnSetTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTaskDate();
            }
        });


        createTaskBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });

        createTaskBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoTodoFragment();
            }
        });
    }

    private void setTaskDate() {

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        createTaskBinding.tvTaskDateValue.setText((dayOfMonth + " / " + (monthOfYear + 1) + " / " + year));
                        showDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        finalDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                    }

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        StartTime.getDatePicker().setMinDate(calendar.getTimeInMillis());
        StartTime.show();
    }

    private void createTask() {
        String priorityType = getString(R.string.label_high_priority);
        int priorityValue = createTaskBinding.rgTaskPriority.getCheckedRadioButtonId();
        if (priorityValue == R.id.rbHighPriority) {
            priorityType = getResources().getString(R.string.label_high_priority);
        } else if (priorityValue == R.id.rbMediumPriority) {
            priorityType = getResources().getString(R.string.label_medium_priority);
        } else if (priorityValue == R.id.rbLowPriority) {
            priorityType = getResources().getString(R.string.label_low_priority);
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date objDate = dateFormat.parse(finalDate);
            //Expected date format
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = dateFormat2.format(objDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String taskName = createTaskBinding.etTaskName.getText().toString();

        TaskDataServices.TaskRequest createdTask = TaskDataServices.createTask(taskName, formattedDate, priorityType);
        if (createdTask.isSuccessful()) { //successful

            TaskDataServices.Task task = createdTask.getTask();
            mListener.createdTask(task);

        } else { //not successful
            String error = createdTask.getErrorMessage();
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
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