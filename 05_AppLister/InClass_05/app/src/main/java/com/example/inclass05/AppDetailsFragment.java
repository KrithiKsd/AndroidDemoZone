package com.example.inclass05;
/*
a. Assignment #:InCLass05
b. File Name:AppDetailsFragment.java
c. Full name of the Student 1:Krithika Kasaragod
*/
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.inclass05.databinding.FragmentAppDetailsBinding;
import com.example.inclass05.databinding.FragmentAppListBinding;

import java.util.ArrayList;


public class AppDetailsFragment extends Fragment {

    private static final String ARG_DETAIL = "ARG_DETAIL";
    private DataServices.App mApp;
    FragmentAppDetailsBinding fragmentAppDetailsBinding;
    ArrayAdapter<String> adapter;
    ArrayList<String> appList;

    public static AppDetailsFragment newInstance(DataServices.App app) {
        AppDetailsFragment fragment = new AppDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DETAIL, app);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mApp = (DataServices.App) getArguments().getSerializable(ARG_DETAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentAppDetailsBinding = FragmentAppDetailsBinding.inflate(inflater, container, false);
        getActivity().setTitle(R.string.label_app_details_title);
        return fragmentAppDetailsBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentAppDetailsBinding.tvAppName.setText(mApp.name);
        fragmentAppDetailsBinding.tvArtistName.setText(mApp.artistName);
        fragmentAppDetailsBinding.tvReleasedDate.setText(mApp.releaseDate);

        appList= mApp.genres;
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,appList);
        fragmentAppDetailsBinding.listCategory.setAdapter(adapter);

    }
}