package com.example.midtermapplication;

/*
a. File Name : ExpenseSummaryFragment.java
b. Full name of the student 1: Krithika Kasaragod
*/
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.midtermapplication.databinding.FragmentExpenseSummaryBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;


public class ExpenseSummaryFragment extends Fragment {


    FragmentExpenseSummaryBinding fragmentExpenseSummaryBinding;
    ArrayAdapter<ExpenseDataServices.Expense> adapter;
    private static final String ARG_EXPENSE_SUMMARY = "SUMMARY";
    private ArrayList<ExpenseDataServices.Expense> mList;
    public ExpenseSummaryFragment() {
        // Required empty public constructor
    }

    public static ExpenseSummaryFragment newInstance(ArrayList<ExpenseDataServices.Expense> list, String param2) {
        ExpenseSummaryFragment fragment = new ExpenseSummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXPENSE_SUMMARY, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mList = (ArrayList<ExpenseDataServices.Expense>) getArguments().getSerializable(ARG_EXPENSE_SUMMARY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentExpenseSummaryBinding = FragmentExpenseSummaryBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_expense_summary));
        return fragmentExpenseSummaryBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HashMap<String, Double> hashMap = new HashMap<>();
        String[] monthYear;
        String monthYearKey;
        if (mList != null) {
            for (ExpenseDataServices.Expense item : mList) {
                monthYear = item.getExpenseDate().split("-");
                monthYearKey = String.join("_", monthYear[0], monthYear[1]);
                Double count = hashMap.get(monthYearKey);
                if (count == null) {
                    hashMap.put(monthYearKey, Double.valueOf(item.getExpenseAmount()));
                } else {
                    hashMap.put(monthYearKey, count + Double.valueOf(item.getExpenseAmount()));
                }
            }


            ArrayList<ExpenseDataServices.Expense> listMonth_Year = new ArrayList<>();
            ArrayList<ExpenseDataServices.Expense> list_data_from_Map = new ArrayList<>();
            for (HashMap.Entry<String, Double> entry : hashMap.entrySet()) {

                list_data_from_Map.add(new ExpenseDataServices.Expense(entry.getKey(), String.valueOf(entry.getValue())));

            }

            Collections.sort(list_data_from_Map, new Comparator<ExpenseDataServices.Expense>() {
                @Override
                public int compare(ExpenseDataServices.Expense task, ExpenseDataServices.Expense t1) {
                    return -1 * task.expenseDate.compareTo(t1.expenseDate);
                }
            });


            for (ExpenseDataServices.Expense item : list_data_from_Map) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
                    Date objDate = dateFormat.parse(item.getExpenseDate());
                    //Expected date format
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM yyyy");
                    String formattedDate = dateFormat2.format(objDate);

                    listMonth_Year.add(new ExpenseDataServices.Expense(formattedDate, item.expenseAmount));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            adapter = new ExpenseListAdapter(getContext(), R.layout.custom_expense_summary_list, listMonth_Year);
            fragmentExpenseSummaryBinding.listExpenseSummary.setAdapter(adapter);

        }
    }

    public class ExpenseListAdapter extends ArrayAdapter<ExpenseDataServices.Expense> {

        ArrayList<ExpenseDataServices.Expense> expenseList;

        public ExpenseListAdapter(@NonNull Context context, int resource, ArrayList<ExpenseDataServices.Expense> objects) {
            super(context, resource, objects);
            this.expenseList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_expense_summary_list, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.tvExpenseMonthYear = convertView.findViewById(R.id.tvMonth_Year);
                viewHolder.tvExpenseAmount = convertView.findViewById(R.id.tvSummaryAmount);
                convertView.setTag(viewHolder);

            }
            ExpenseDataServices.Expense app = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tvExpenseMonthYear.setText(app.getExpenseDate());
            viewHolder.tvExpenseAmount.setText(getString(R.string.label_dollar_currency)+app.getExpenseAmount());

            return convertView;
        }

        public class ViewHolder {
            TextView tvExpenseMonthYear, tvExpenseAmount;
        }
    }
}