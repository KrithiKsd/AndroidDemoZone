package com.example.inclass04;
/*
a. Assignment #. InClass04
b. File Name : AccountFragment.java
c. Full name of the student: Krithika Kasaragod
*/

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inclass04.databinding.FragmentAccountBinding;
import com.example.inclass04.databinding.FragmentLoginBinding;


public class AccountFragment extends Fragment {


    private static final String ARG_ACCOUNT = "account";

    FragmentAccountBinding accountBinding;
    DataServices.Account mAccount;
    FragmentInterface mListener;


    public AccountFragment() {
        // Required empty public constructor
    }


    public static AccountFragment newInstance(DataServices.Account mAccount) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACCOUNT, mAccount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccount = (DataServices.Account) getArguments().getSerializable(ARG_ACCOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        accountBinding = FragmentAccountBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_account_fragment_title));
        return accountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountBinding.textViewAccName.setText(mAccount.getName());

        accountBinding.buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoUpdateFragment(mAccount);
            }
        });

        accountBinding.buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mListener.gotoLoginFragment();
                mListener.logoutAccount();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof FragmentInterface) {
                mListener = (FragmentInterface) context;
            } else {
                throw new RuntimeException();
            }

        } catch (RuntimeException e) {
        }
    }
}