package com.example.homework_04;
/*
a. Assignment Homework04.
b. File Name: SortFragment.java
c. Full name of the student : Krithika Kasaragod
*/
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homework_04.databinding.FragmentSortBinding;

import java.util.ArrayList;
import java.util.Arrays;


public class SortFragment extends Fragment {

    FragmentSortBinding fragmentSortBinding;
    LinearLayoutManager layoutManager;
    SortAdapter adapter;
    IUserInterface mListener;

    public SortFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSortBinding = FragmentSortBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_sort_fragment_title));
        return fragmentSortBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentSortBinding.recyclerSortList.setHasFixedSize(true);
       // layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentSortBinding.recyclerSortList.setLayoutManager(layoutManager);


        String[] sortList = getActivity().getResources().getStringArray(R.array.label_sort_list);
        ArrayList<String> sortNameList = new ArrayList<>(Arrays.asList(sortList));

        Log.d("TAG", "onViewCreated: list"+sortNameList);
        adapter = new SortAdapter(sortNameList, mListener);
        fragmentSortBinding.recyclerSortList.setAdapter(adapter);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IUserInterface) {
                mListener = (IUserInterface) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }

    class SortAdapter extends RecyclerView.Adapter<SortAdapter.RecyclerSortViewHolder> {

        ArrayList<String> sortNameList;
        IUserInterface mListener;

        public SortAdapter(ArrayList<String> sortNameList, IUserInterface mListener) {
            this.sortNameList = sortNameList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public RecyclerSortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_sort_list, parent, false);
            RecyclerSortViewHolder sortViewHolder = new RecyclerSortViewHolder(view, mListener);
            return sortViewHolder;
        }

        @Override
        public int getItemCount() {
            return sortNameList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerSortViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.tvSortName.setText(sortNameList.get(position));
            holder.position = position;
        }

        public class RecyclerSortViewHolder extends RecyclerView.ViewHolder {
            TextView tvSortName;
            ImageView ivAscendingCriteria, ivDescendingCriteria;
            int position;
            IUserInterface mListener;
            String criteria;

            public RecyclerSortViewHolder(@NonNull View itemView, IUserInterface mListener) {
                super(itemView);
                tvSortName = itemView.findViewById(R.id.tvSortName);
                ivAscendingCriteria = itemView.findViewById(R.id.ivAscending);
                ivDescendingCriteria = itemView.findViewById(R.id.ivDescending);
                this.mListener = mListener;

                ivAscendingCriteria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        criteria = sortNameList.get(position);
                        DataServices.User criteriaObj = new DataServices.User();
                        criteriaObj.criteria = criteria + getString(R.string.label_ascending);
                        mListener.gotoUsersFragmentWithSelectedCriteriaNew(criteriaObj);
                    }
                });

                ivDescendingCriteria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        criteria = sortNameList.get(position);
                        DataServices.User criteriaObj = new DataServices.User();
                        criteriaObj.criteria = criteria + getString(R.string.label_descending);
                        mListener.gotoUsersFragmentWithSelectedCriteriaNew(criteriaObj);
                    }
                });
            }
        }
    }
}