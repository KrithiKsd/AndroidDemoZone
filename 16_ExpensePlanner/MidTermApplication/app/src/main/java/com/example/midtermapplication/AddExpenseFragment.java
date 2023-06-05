package com.example.midtermapplication;
/*
a. File Name : AddExpenseFragment.java
b. Full name of the student 1: Krithika Kasaragod
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

import com.example.midtermapplication.databinding.FragmentAddExpenseBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddExpenseFragment extends Fragment {

    FragmentAddExpenseBinding fragmentAddExpenseBinding;
    String finalDate, formattedDate;
    IExpenseListener mListener;
    String mCategory;
    static String datePicked;


    void setPickCategory(String category) {
        mCategory = category;
    }


    public AddExpenseFragment() {
        // Required empty public constructor
    }

    public static AddExpenseFragment newInstance(String param1, String param2) {
        AddExpenseFragment fragment = new AddExpenseFragment();
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
        fragmentAddExpenseBinding = FragmentAddExpenseBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_add_new_expense));
        return fragmentAddExpenseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (mCategory != null) {
            fragmentAddExpenseBinding.tvCategoryName.setText(mCategory);
            if (datePicked != null) {
                fragmentAddExpenseBinding.tvExpenseDateValue.setText(datePicked);
            }
        }

        fragmentAddExpenseBinding.btnSetTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTaskDate();
            }
        });

        fragmentAddExpenseBinding.btnPickCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoPickCategory();
            }
        });

        fragmentAddExpenseBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creteTask();
            }
        });

        fragmentAddExpenseBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoExpenseFragment();
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
                        datePicked = (dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
                        fragmentAddExpenseBinding.tvExpenseDateValue.setText(datePicked);
                        finalDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                    }

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

    private void creteTask() {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date objDate = dateFormat.parse(finalDate);
            //Expected date format
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = dateFormat2.format(objDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String expenseName = fragmentAddExpenseBinding.etExpenseName.getText().toString();
        String amount = fragmentAddExpenseBinding.etAmount.getText().toString();

        if (mCategory != null) {
            ExpenseDataServices.TaskRequest createdTask = ExpenseDataServices.createTask(expenseName, formattedDate, amount, mCategory);
            if (createdTask.isSuccessful()) { //successful
                ExpenseDataServices.Expense task = createdTask.getTask();
                mListener.createdExpense(task);
            } else { //not successful
                String error = createdTask.getErrorMessage();
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.label_validation_pick_category), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IExpenseListener) {
                mListener = (IExpenseListener) context;
            } else {
                throw new RuntimeException();
            }

        } catch (RuntimeException e) {
        }
    }
}