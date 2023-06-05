package com.example.inclass05;

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
import android.widget.ListView;

import com.example.inclass05.databinding.FragmentAppCategoryBinding;

import java.util.ArrayList;
import java.util.Locale;
/*
a. Assignment #:InCLass05
b. File Name:AppCategoryFragment.java
c. Full name of the Student 1:Krithika Kasaragod
*/

public class AppCategoryFragment extends Fragment {



    FragmentAppCategoryBinding appCategoryBinding;
    ArrayAdapter<String> listCategoryAdapter;
    ArrayList<String> categoryList;
    IListviewInterface mListener;

    public static AppCategoryFragment newInstance(String param1, String param2) {
        AppCategoryFragment fragment = new AppCategoryFragment();
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
        appCategoryBinding = FragmentAppCategoryBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_app_categories_title));
        return appCategoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryList = DataServices.getAppCategories();
        listCategoryAdapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1,categoryList);
       appCategoryBinding.listViewCategory.setAdapter(listCategoryAdapter);

       appCategoryBinding.listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListener.gotoAppListFragment(categoryList.get(position));
           }
       });

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IListviewInterface) {
                mListener = (IListviewInterface) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }
}