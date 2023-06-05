package com.example.midtermapplication;

/*
a. File Name : ExpenseFragment.java
b. Full name of the student 1: Krithika Kasaragod
*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.midtermapplication.databinding.CustomExpenseListBinding;
import com.example.midtermapplication.databinding.FragmentExpenseBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ExpenseFragment extends Fragment {


    FragmentExpenseBinding fragmentExpenseBinding;
    ArrayList<ExpenseDataServices.Expense> expenseList= new ArrayList<>();
    LinearLayoutManager layoutManager;

    ExpenseAdapter adapter;
    IExpenseListener mListener;

    void setExpenseList(ArrayList<ExpenseDataServices.Expense> list){

        expenseList.clear();
        expenseList.addAll(list);
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }


    public ExpenseFragment() {
    }
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
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
        fragmentExpenseBinding = FragmentExpenseBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_expense));
        return fragmentExpenseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentExpenseBinding.recyclerExpense.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentExpenseBinding.recyclerExpense.setLayoutManager(layoutManager);

        if(expenseList.size()>0){
            fragmentExpenseBinding.tvRecordsValue.setText(String.valueOf(expenseList.size()));
            double total=0.0;
            for (ExpenseDataServices.Expense item: expenseList) {
               total= total+ Double.valueOf(item.getExpenseAmount());
            }
            fragmentExpenseBinding.tvTotalExpenseValue.setText("$"+String.valueOf(total));

            Collections.sort(expenseList, new SortExpenseDate());

            adapter = new ExpenseAdapter(expenseList, mListener);
            fragmentExpenseBinding.recyclerExpense.setAdapter(adapter);

        }else{
            fragmentExpenseBinding.tvRecordsValue.setText(getString(R.string.label_zero));
            fragmentExpenseBinding.tvTotalExpenseValue.setText(getString(R.string.label_dollar_zero));
        }

        fragmentExpenseBinding.btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoAddExpenseFromExpense();
            }
        });

        fragmentExpenseBinding.btnExpenseSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoExpenseSummary(expenseList);
            }
        });
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


    class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.RecyclerExpenseViewHolder> {
        ArrayList<ExpenseDataServices.Expense> listExpenses;
        IExpenseListener mListener;

        public ExpenseAdapter(ArrayList<ExpenseDataServices.Expense> sortNameList, IExpenseListener mListener) {
            this.listExpenses = sortNameList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public RecyclerExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CustomExpenseListBinding binding = CustomExpenseListBinding.inflate(getLayoutInflater(), parent, false);
            RecyclerExpenseViewHolder sortViewHolder = new RecyclerExpenseViewHolder(binding);
            return sortViewHolder;

        }

        @Override
        public int getItemCount() {
            return listExpenses.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerExpenseViewHolder holder, @SuppressLint("RecyclerView") int position) {

            ExpenseDataServices.Expense item= listExpenses.get(position);
            holder.setUpData(item);
        }

        public class RecyclerExpenseViewHolder extends RecyclerView.ViewHolder {

            CustomExpenseListBinding mBinding;
            ExpenseDataServices.Expense mItem;

            public RecyclerExpenseViewHolder(CustomExpenseListBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setUpData(ExpenseDataServices.Expense item) {
                mItem = item;
                mBinding.tvListName.setText(mItem.getExpenseName());
                mBinding.tvListAmount.setText(getResources().getString(R.string.label_dollar_currency)+mItem.getExpenseAmount());

                String formattedDate=null;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date objDate = dateFormat.parse(mItem.getExpenseDate());
                    //Expected date format
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
                    formattedDate = dateFormat2.format(objDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                mBinding.tvListDate.setText(formattedDate);
                mBinding.tvListCategory.setText(getString(R.string.label_open_bracket)+mItem.getExpenseCategory()+getString(R.string.label_close_bracket));

                mBinding.ivDescending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.deleteExpense(getAdapterPosition());
                    }
                });
            }
        }
    }
}