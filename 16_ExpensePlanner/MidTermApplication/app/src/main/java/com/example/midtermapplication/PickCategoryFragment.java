package com.example.midtermapplication;

/*
a. File Name : PickCategoryFragment.java
b. Full name of the student 1: Krithika Kasaragod
*/
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.midtermapplication.databinding.FragmentPickCategoryBinding;

import java.util.ArrayList;


public class PickCategoryFragment extends Fragment {

    FragmentPickCategoryBinding fragmentPickCategoryBinding;
    ArrayList<String> categoryList;
    ArrayAdapter<String> listCategoryAdapter;
    IExpenseListener mListener;
    public PickCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPickCategoryBinding = FragmentPickCategoryBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_pick_category));
        return fragmentPickCategoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryList= DataServices.getCategories();
        listCategoryAdapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1,categoryList);
        fragmentPickCategoryBinding.listViewCategory.setAdapter(listCategoryAdapter);

        fragmentPickCategoryBinding.listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListener.gotoAddExpenseFromPickCategory(categoryList.get(position));
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
}